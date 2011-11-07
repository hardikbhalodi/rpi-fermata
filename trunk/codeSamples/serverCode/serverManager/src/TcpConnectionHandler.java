import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class TcpConnectionHandler implements Runnable
{

	public Vector<Thread> threads = new Vector<Thread>();
	
	private BlockingQueue<String> inbound;
	private BlockingQueue<String> outbound;
	private int port;
	private String greetingMessage;

	public TcpConnectionHandler(int port, BlockingQueue<String> inbound, BlockingQueue<String> outbound, String greetingMessage)
	{
		this.inbound = inbound;
		this.outbound = outbound;
		this.port = 9876;
		this.greetingMessage = greetingMessage;
	}

	@Override
	public void run() 
	{
		System.out.println("TCP Server Started - Listening on port: " + port);
		try
		{
			//Create a listener socket
			ServerSocket serverSocket = new ServerSocket(port);

			while(true)
			{
				// Listen for a connection request
				System.out.println("TCP Server waiting for connection...");
				Socket socket = serverSocket.accept();

				System.out.println("TCP Server accepted connection");
				//Spawn a new threadHandler to handle the thread
				TcpConnectionProcessor TcpConnectionProcessor = new TcpConnectionProcessor(socket, inbound, outbound, greetingMessage);
				Thread thread = new Thread(TcpConnectionProcessor);
				threads.add(thread);
				thread.start();
			}

		}
		catch ( IOException ex )
		{
			System.err.println( ex );
		}
	}
}

