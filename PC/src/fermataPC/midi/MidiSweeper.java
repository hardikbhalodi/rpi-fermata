package fermataPC.midi;

import java.util.Arrays;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiDevice.Info;

/**
 * The MidiSweeper constantly checks the status of MIDI devices on the system.
 * 
 * The MidiSweeper, once instantiated, this opens a new thread
 * and runs it constantly to check the status of MIDI devices;
 * and forwards the new information along to the MidiHandler.
 * Creating more than one sweeper is unnecessary.
 * @author katzj2
 *
 */
public final class MidiSweeper implements Runnable
{
	private Vector<Info> midiInfo;
	
	private static boolean started = false;
	
	/**
	 * The constructor starts the background process running,
	 * unless one has already been started.
	 */
	public MidiSweeper()
	{		
		if (started)
			return;
		Thread t = new Thread(this);
		
		t.start();
		started = true;
	}
	
	/*
	 * This eventually loops endlessly to query the system for MIDI devices, so
	 * that the MidiHandler can be updated.
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		Vector<MidiDevice> tempDev = new Vector<MidiDevice>();
		for(;;) //loop endlessly.
		{
			midiInfo = new Vector<Info>(Arrays.asList(MidiSystem.getMidiDeviceInfo())); // gets devices
			for (Info i: midiInfo) // for each device;
			{			
				MidiDevice md;
				try
				{
					md = MidiSystem.getMidiDevice(i); // recover the device from its description
					if (md.getMaxTransmitters() != 0 // if it's a transmitter, or can transmit
							&& !md.getDeviceInfo().getName().equals("Real Time Sequencer")) // and isn't made up Java bullshit.
					{ 
						tempDev.add(md); // we consider it valid and add it to our list.
					}
				}
				catch (MidiUnavailableException e)
				{
					e.printStackTrace();
				}
			}
			
			MidiHandler.getMidiHandler().updateDeviceList(tempDev); // we call the Handler to update the list
			tempDev.clear(); //clear.
			try
			{
				Thread.sleep(1000); // and sleep.
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}