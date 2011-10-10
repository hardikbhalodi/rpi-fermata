package oscSender;

import java.net.InetAddress;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class main
{
	public static void main(String[] args)
	{
		OSCPortOut sender;

		try
		{
			// We want to send on local host, but I guess we could also
			//  make that user configurable. . . (probably not, though; it
			//  adds a whole mess of complications for end users.
			InetAddress inet = InetAddress.getLocalHost();
			
			// Setting the address and the port. We should definitely make
			// the port configurable.
			sender = new OSCPortOut(inet, 7400);
			Object message[] = new Object[2];
			message[0] = new Integer(4);
			message[1] = new String("Test");
			
			OSCMessage msg = new OSCMessage("/sayhello", message);
			
			while (1 == 1)
			{
				try
				{
					sender.send(msg);
				}
				catch (Exception e)
				{
					System.out.println("Nope.");
					e.printStackTrace();
				}
				Thread.sleep(5000);
			}
		}
		catch (Exception e)
		{
			System.out.println("nope");
			e.printStackTrace();
		}		
		System.out.println("Success?");
		
		System.exit(0);
	}
}
