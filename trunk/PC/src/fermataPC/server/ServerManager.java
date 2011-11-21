package fermataPC.server;

import fermataPC.server.bluetooth.BTConnectionHandler;
import fermataPC.server.tcp.TcpConnectionHandler;

public class ServerManager
{
	private String[] inbound;
	private String[] outbound; 
	@SuppressWarnings("unused")
	private ServerScanner servScan;
	
	Thread btConnectionHandler;
	Thread tcpConnectionHandler;
	
	public ServerManager(int port, String greetingMessage)
	{		
		inbound  = new String[10];
		outbound = new String[10];
		
		btConnectionHandler = new Thread(new BTConnectionHandler(inbound, outbound, greetingMessage));
		btConnectionHandler.start();
		
		tcpConnectionHandler = new Thread(new TcpConnectionHandler(port, inbound, outbound, greetingMessage));
		tcpConnectionHandler.start();
		
		servScan = new ServerScanner(this);
	}
	
	public String getTopInbound()
	{	
		return inbound[0];
	}
	
	public void sendStringAll(String message)
	{
		
	}
	
}
