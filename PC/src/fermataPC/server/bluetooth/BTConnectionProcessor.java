package fermataPC.server.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.StreamConnection;

public class BTConnectionProcessor implements Runnable
{

	private String[] inbound;
	private StreamConnection threadConnection;
	/**
	 * The constructor requires a given stream connection.
	 * @param connection
	 */

	public BTConnectionProcessor(StreamConnection connection, String[] inbound2, String[] outbound, String greetingMessage)
	{
		threadConnection = connection;
		this.inbound = inbound2;

		if(greetingMessage != null)
		{
			DataOutputStream output;
			try 
			{
				output = threadConnection.openDataOutputStream();
				output.writeUTF(greetingMessage);
				output.flush();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * The thread runnable function receives 4 byte messages
	 */
	@Override
	public void run() 
	{
		try {
			// prepare to receive data
			InputStream inputStream = threadConnection.openInputStream();	
			System.out.println("waiting for input");
			while (true) 
			{
				/*
				//System.out.println(inputStream.read());
				StringBuffer bufarray = new StringBuffer();
				byte[] buffer = new byte[8];
				for(int i=0; i<8; i++){

					bufarray.append((char)inputStream.read());
				}
				//buffer = bufarray.toString();

				//System.out.println(bufarray);
				 * 
				 */
				// prepare to receive data
				DataInputStream userInput = new DataInputStream(inputStream);
				System.out.println("waiting for input");
				String data = null;
				while (true) 
				{
					try
					{
						data = userInput.readUTF();
					}
					catch (Exception e)
					{
						//TODO
					}
					inbound[0] = data;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//private StreamConnection threadConnection;

}
