package fermataPC.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import fermataPC.server.bluetooth.BTConnectionHandler;
import fermataPC.server.tcp.TcpConnectionHandler;

public class ServerManager
{
	private BlockingQueue<String> inbound;
	private BlockingQueue<String> outbound;
	@SuppressWarnings("unused")
	private ServerScanner servScan;
	
	Thread btConnectionHandler;
	Thread tcpConnectionHandler;
	
	public ServerManager(int port, String greetingMessage)
	{		
		inbound = new LinkedBlockingQueue<String>();
		outbound = new LinkedBlockingQueue<String>();
		
		btConnectionHandler = new Thread(new BTConnectionHandler(inbound, outbound, greetingMessage));
		btConnectionHandler.start();
		
		tcpConnectionHandler = new Thread(new TcpConnectionHandler(port, inbound, outbound, greetingMessage));
		tcpConnectionHandler.start();
		
		servScan = new ServerScanner(this);
	}
	
	public String getTopInbound()
	{
		String temp = inbound.peek();
		try
		{
			inbound.remove();
		}
		catch(Exception e)
		{
			return null;
		}
		
		return temp;
	}
	
	public void sendStringAll(String message)
	{
		
	}
	
}
