package fermataPC.server;

import fermataPC.util.Coordinate;

public class ServerScanner implements Runnable
{
	private ServerManager theServer;
	
	
	public ServerScanner(ServerManager theServer)
	{
		this.theServer = theServer;
		
		Thread t = new Thread(this);
		
		t.start();
	}
	
	@Override
	public void run()
	{
		String temp;
		
		for(;;)
		{
			temp = theServer.getTopInbound();
			if (temp != null)
			{
				parseMessage(temp);
			}
			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	private static void parseMessage(String message)
	{
		String[] messages = message.split(";");
		String[] coord1 = messages[0].split(",");
		String[] coord2 = messages[1].split(",");
		
		System.out.println("UID1: " + coord1[0] + "; scalar: " + coord1[1] + ";UID2: " + coord2[0] + "; scalar: " + coord2[1]);
		
		Integer UID1 = Integer.parseInt(coord1[0]);
		Integer UID2 = Integer.parseInt(coord2[0]);
		
		Integer scalar1 = Integer.parseInt(coord1[1]);
		Integer scalar2 = Integer.parseInt(coord2[1]);
		
		Coordinate c1 = new Coordinate(UID1, scalar1);
		Coordinate c2 = new Coordinate(UID2, scalar2);
		
	}
}
