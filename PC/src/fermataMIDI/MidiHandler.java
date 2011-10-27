package fermataMIDI;

import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import fermataUI.MidiDeviceBox;

public final class MidiHandler
{
	private Vector<MidiDevice> validDevices;
	private MidiDevice activeDev;
	private Transmitter activeTransmit;
	
	private MidiReceiver mr;
	@SuppressWarnings("unused")
	private MidiSweeper ms;
	private MidiDeviceBox mdb;
	
	public MidiHandler(MidiDeviceBox mdb)
	{
		this.mdb = mdb;
		
		validDevices = new Vector<MidiDevice>();		
		
		mr = new MidiReceiver();
		
		mdb.setMidiHandler(this);
		ms = new MidiSweeper(this);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void updateDeviceList(Vector<MidiDevice> deviceList)
	{
		if (!validDevices.equals(deviceList))
		{
			System.out.println("Updating midi device selection box");
			mdb.updateList(deviceList);
			validDevices = (Vector<MidiDevice>) deviceList.clone();
		}
	}
	
	public void setActiveDevice(MidiDevice md)
	{
		if (md == null)
		{
			if (activeDev != null)
			{
				activeDev.close();
				mr.close();
				activeDev = null;
			}
			return;
		}
		
		if (activeDev == null)
		{
			activeDev = md;
			try
			{
				activeTransmit = md.getTransmitter();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Transmitter activation fail");
				e.printStackTrace();
			}
			activeTransmit.setReceiver(mr);
			try
			{
				activeDev.open();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Could not open device");
				e.printStackTrace();
			}
		}
		else
		{
			activeDev.close();
			mr.close();
			
			activeDev = md;
			try
			{
				activeTransmit = md.getTransmitter();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Transmitter activation fail");
				e.printStackTrace();
			}
			activeTransmit.setReceiver(mr);
			try
			{
				activeDev.open();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Could not open device");
				e.printStackTrace();
			}
		}
	}
}
