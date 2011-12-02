package fermataPC.sound.debug;

import fermataPC.sound.midiPlayer;

public class DummyMIDISender implements Runnable
{
	public DummyMIDISender()
	{
		Thread t = new Thread(this);
		
		t.start();
	}

	@Override
	public void run()
	{
		try{
			for(;;)
			{
				System.out.println("note on");
				midiPlayer.noteOn(69 , 63);
				
				Thread.sleep(4000);
				System.out.println("note off");
				midiPlayer.noteOff(69, 63);
				
				Thread.sleep(4000);
				
				System.out.println("note on 2");
				
				midiPlayer.noteOn(69 , 63);
				
				Thread.sleep(10000);
				System.out.println("note off 2");
				midiPlayer.noteOff(69 , 63);
				
				Thread.sleep(10000);
			}
		}
		catch (Exception e)
		{
			System.out.println("Dummy stopped working");
			e.printStackTrace();
		}
	}
	
	
	
	
}