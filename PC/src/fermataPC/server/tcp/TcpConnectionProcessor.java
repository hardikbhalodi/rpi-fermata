package fermataPC.server.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

class TcpConnectionProcessor implements Runnable
{
	private Socket socket;

	private BlockingQueue<String> inbound;
	private DataInputStream userInput;
	private DataOutputStream output;

	public TcpConnectionProcessor(Socket incoming_socket, BlockingQueue<String> inbound, BlockingQueue<String> outbound, String greetingMessage)
	{
		this.socket = incoming_socket;
		this.inbound = inbound;

		try
		{
			userInput = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

	
			if(greetingMessage != null)
			{
				output.writeUTF(greetingMessage);
				output.flush();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()
	{
		try
		{
			//Prepare to receive data
			System.out.println("waiting for input");
			String data;

			//Receive the data
			while (true) 
			{
				data = userInput.readUTF();
				inbound.add(data);
			}

		}
		catch(IOException ex)
		{
			System.out.println("Connection Closed\n");

			try 
			{
				socket.close();
			} 
			catch (IOException e) {

			}
		}
		catch(Exception ex)
		{

		}

	}
}