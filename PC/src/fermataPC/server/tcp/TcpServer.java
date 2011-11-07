package fermataPC.server.tcp;

import java.util.concurrent.BlockingQueue;


public class TcpServer
{
	TcpConnectionHandler connectionHandler;
	
	public TcpServer(BlockingQueue<String> inbound, BlockingQueue<String> outbound, String greetingMessage) 
	{
		connectionHandler = new TcpConnectionHandler(9876, inbound, outbound, greetingMessage);
	}
		
}
