package fermataPC.server.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class TcpConnectionProcessor implements Runnable
{
	private Socket socket;

	private String[] inbound;
	private DataInputStream userInput;
	private DataOutputStream output;

	public TcpConnectionProcessor(Socket incoming_socket, String[] inbound2, String[] outbound, String greetingMessage)
	{
		this.socket = incoming_socket;
		this.inbound = inbound2;

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
				inbound[0] = data;
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