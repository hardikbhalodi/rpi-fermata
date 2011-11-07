package fermataPC.server;

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
				System.out.println(temp);
				//TODO parse the message, apply it.
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
}
