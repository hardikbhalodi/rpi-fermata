import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerManager
{

	private BlockingQueue<byte []> inbound;
	private BlockingQueue<byte []> outbound;
	
	public ServerManager()
	{
		inbound = new LinkedBlockingQueue();
		outbound = new LinkedBlockingQueue();
		
		Thread btConnectionHandler = new Thread(new BTConnectionHandler(inbound, outbound));
		btConnectionHandler.start();
		
		Thread tcpConnectionHandler = new Thread(new TcpConnectionHandler(9876, inbound, outbound));
		tcpConnectionHandler.start();
	}
	
	public byte[] getTopInbound()
	{
		byte[] temp = inbound.peek();
		inbound.remove();
		return temp;
	}
	
}
