package fermataOSC;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.illposed.osc.OSCPortOut;

import fermataUtil.Coordinate;

public final class OSCSender
{
	private static int defaultPort = 7777;
	private static InetAddress localHost;
	
	private static int port;
	private static InetAddress IP = null;
	
	private static OSCPortOut sender;
	
	private static Boolean enabled = false;
	

	public OSCSender()
	{
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
		
		port = defaultPort;
	}
	
	public static int setPort(int port)
	{
		if (port > 1023 && port <= 65535)
			OSCSender.port = port;
		
		return OSCSender.port;
	}
	
	public static boolean enableOSC()
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
	public static void disableOSC()
	{
		sender.close();
		enabled = false;
	}

	public static String setIP(String IP)
	{
		try
		{
			OSCSender.IP = InetAddress.getByName(IP);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return OSCSender.IP.getHostName();
	}

	public static int getDefaultPort()
	{
		return defaultPort;
	}
	
	public static Boolean sendMessage(CoordinateMessage cm)
	{
		if (enabled)
		{
			try
			{
				sender.send(cm);
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
	
	public static Boolean sendCoordinate(Coordinate c)
	{
		if (enabled)
		{
			CoordinateMessage cm = new CoordinateMessage(c);
			
			try
			{
				sender.send(cm);
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
}
