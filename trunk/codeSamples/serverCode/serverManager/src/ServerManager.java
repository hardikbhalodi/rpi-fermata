import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerManager
{

	private BlockingQueue<String> inbound;
	private BlockingQueue<String> outbound;
	
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
