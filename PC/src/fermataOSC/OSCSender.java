package fermataOSC;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import fermataUtil.Coordinate;

/**
 * The OSCSender class exists to coordinate the sending of OSC messages
 * through OSC pass-through.
 * 
 * The OSCSender class sends OSC messages. Most of its functionality is
 * static, barring a single function to initialize some settings. As such, once
 * it is set up (the startOSCService function should be called as soon as
 * Fermata is opened), OSCMessages can be passed to it and it will send them
 * out. It will keep track of the current port and IP address it sends to,
 * and ensure that these are all valid.
 * 
 * @author katzj2
 *
 */
public abstract class OSCSender
{
	public static final int DEFAULT_PORT = 7777;
	private static InetAddress localHost;
	
	private static int port;
	private static InetAddress IP = null;
	
	private static OSCPortOut sender;
	
	private static Boolean enabled = false;
	
	private static Boolean started = false;
	
	/**
	 * This function does initial setup of the OSCSender so it can send
	 * messages.
	 */
	public static final void startOSCService()
	{
		if (started)
			return;
		
		try
		{
			localHost = InetAddress.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			System.out.println("Can't find localHost.");
			localHost = null;
			e.printStackTrace();
		}
		
		try
		{
			sender = new OSCPortOut();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IP = localHost;
		port = DEFAULT_PORT;
		
		started = true;
	}
	
	/**
	 * Sets the current UDP port the OSCSender sends messages on. If the port
	 * is invalid, it keeps the old port.
	 * @param port The number of the new UDP port.
	 * @return The UDP port OSCSender sends messages on after the function is
	 * called.
	 */
	public static final int setPort(int port)
	{
		if (port > 1023 && port <= 65535)
		{
			OSCSender.port = port;
			disableOSC(); //closes current socket
			enableOSC(); // opens new socket.
		}
		return OSCSender.port;
	}
	
	/**
	 * This function enables OSC pass-through. If OSC pass-through is enabled,
	 * OSCSender will intercept Coordinates and send them out. This function
	 * also opens the OSCSender so it can send messages.
	 * @return true if the socket can be opened; false otherwise.
	 */
	public static final Boolean enableOSC()
	{
		try
		{
			sender = new OSCPortOut(IP, port);
			enabled = true;
		}
		catch (SocketException e)
		{
			enabled = false;
		}
		return enabled;
	}
	/**
	 * This function disables OSC pass-through, stopping it from intercepting
	 * Coordinates as they come in from the phone.
	 */
	public static final void disableOSC()
	{
		sender.close();
		enabled = false;
	}

	/**
	 * Sets the IP address the UDP socket will attempt to open on so the sender
	 * can send OSCmessages through it. If the IP is invalid, the sender will
	 * keep its old IP.
	 * @param IP The IP address to set the sender to.
	 * @return The IP address the sender uses after the function is called.
	 */
	public static final String setIP(String IP)
	{
		try
		{
			OSCSender.IP = InetAddress.getByName(IP);
			disableOSC();
			enableOSC();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return OSCSender.IP.getHostName();
	}
	
	/**
	 * Sends an OSC message over the currently opened UDP socket.
	 * @param ocm The OSC Message to send through the socket.
	 * @return true if the send is successful; false otherwise.
	 */
	public static final Boolean sendMessage(OSCMessage ocm)
	{
		if (enabled)
		{
			try
			{
				sender.send(ocm);
				return true;
			}
			catch (IOException e)
			{
				System.out.println("Send failed!");
				e.printStackTrace();
				return false;
			}
		}
		else return false;
	}
	
	/**
	 * Sends a single coordinate without the hassle of making your own message,
	 * by creating a message out of it and then calling the other sendMessage.
	 * @param c the Coordinate to wrap into a message and send.
	 * @return true if the send is successful; false otherwise.
	 */
	public static final Boolean sendCoordinate(Coordinate c)
	{
		if (enabled)
		{
			CoordinateMessage cm = new CoordinateMessage(c);
			
			return sendMessage(cm);
		}
		else return false;
	}
}
