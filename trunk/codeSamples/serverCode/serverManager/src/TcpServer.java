import java.util.concurrent.BlockingQueue;


public class TcpServer
{
	TcpConnectionHandler connectionHandler;
	
	public TcpServer(BlockingQueue<byte []> inbound, BlockingQueue<byte []> outbound) 
	{
		connectionHandler = new TcpConnectionHandler(9876, inbound, outbound);
	}
		
}
