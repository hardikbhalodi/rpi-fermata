import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class TcpConnectionHandler implements Runnable
{

	public Vector<Thread> threads = new Vector<Thread>();
	
	private BlockingQueue<byte []> inbound;
	private BlockingQueue<byte []> outbound;
	private int port;

	public TcpConnectionHandler(int port, BlockingQueue<byte []> inbound, BlockingQueue<byte []> outbound)
	{
		this.inbound = inbound;
		this.outbound = outbound;
		this.port = 9876;
	}

	@Override
	public void run() 
	{
		System.out.println("TCP Server Started - Listening on port: " + port);
		try
		{
			//Create a listener socket
			ServerSocket serverSocket = new ServerSocket(port);

			while(true){

				// Listen for a connection request
				System.out.println("TCP Server waiting for connection...");
				Socket socket = serverSocket.accept();

				System.out.println("TCP Server accepted connection");
				//Spawn a new threadHandler to handle the thread
				TcpConnectThreadHandler TcpConnectThreadHandler = new TcpConnectThreadHandler(socket, inbound, outbound);
				Thread thread = new Thread(TcpConnectThreadHandler);
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

