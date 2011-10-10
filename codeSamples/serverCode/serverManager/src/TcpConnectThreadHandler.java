import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

class TcpConnectThreadHandler implements Runnable
{
	@SuppressWarnings("unused")
	private Socket socket;
	
	private BlockingQueue<byte []> inbound;
	private BlockingQueue<byte []> outbound;
	
	
	public TcpConnectThreadHandler(Socket incoming_socket, BlockingQueue<byte []> inbound, BlockingQueue<byte []> outbound){
		this.socket = incoming_socket;
		this.inbound = inbound;
		this.outbound = outbound;
	}

	public void run()
	{
		try{

			while(true)
			{
				// prepare to receive data
				InputStream inputStream = socket.getInputStream();	
				System.out.println("waiting for input");
				while (true) 
				{
					//System.out.println(inputStream.read());
					StringBuffer bufarray = new StringBuffer();
					byte[] buffer = new byte[8];
					for(int i=0; i<8; i++)
					{
						
						bufarray.append((char)inputStream.read());
						
						if(bufarray.toString().compareToIgnoreCase("stop") == 0)
						{
							System.out.println("Client disconnected");
							return;
						}
					}
					
					System.out.println(bufarray);					
				}
			}
		}
		catch(IOException ex)
		{
			System.out.println("Jesus fuck\n");
		}
		catch(Exception ex)
		{
			
		}

	}
}