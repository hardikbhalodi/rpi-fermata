package fermataPC.server.tcp;

public class TcpServer
{
	TcpConnectionHandler connectionHandler;
	
	public TcpServer(String[] inbound, String[] outbound, String greetingMessage) 
	{
		connectionHandler = new TcpConnectionHandler(9876, inbound, outbound, greetingMessage);
	}
		
}
