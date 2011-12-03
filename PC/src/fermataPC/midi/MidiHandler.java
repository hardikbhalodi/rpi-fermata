package fermataPC.midi;

import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import fermataPC.ui.MidiDeviceBox;

/**
 * MidiHandler organizes the handling of external MIDI devices in Fermata
 * 
 * MidiHandler is the hub of all MIDI action in Fermata. It keeps track
 * of MIDI devices, makes sure the MidiReceiver is linked to the correct
 * device at any given time -- it keeps everything MIDI in sync.
 * 
 * MidiHandler is a singleton.
 * @author katzj2
 *
 */
public class MidiHandler
{
	private Vector<MidiDevice> validDevices;
	private MidiDevice activeDev;
	private Transmitter activeTransmit;
	
	private MidiReceiver mr;
	@SuppressWarnings("unused")
	private MidiSweeper ms;
	private static MidiDeviceBox mdb;
	
	private static final MidiHandler self = new MidiHandler();
	
	private MidiHandler()
	{
		validDevices = new Vector<MidiDevice>();		
		
		mr = MidiReceiver.getInstance();
		
		ms = new MidiSweeper();
	}
	
	public static final MidiHandler getMidiHandler()
	{
		return self;
	}
	
	/**
	 * This function updates the list of currently valid MIDI devices, taking
	 * as its argument a new list. Any classes or objects which need to be
	 * made aware of the new listing are to be informed by this function. Only
	 * MIDI transmitters are to be placed on the list, as the Fermata doesn't
	 * send MIDI messages; it only receives them.
	 * @param deviceList A new list of valid MIDI devices.
	 */
	@SuppressWarnings("unchecked")
	public final void updateDeviceList(Vector<MidiDevice> deviceList)
	{
		if (!validDevices.equals(deviceList)) // if the list is different
		{
			// we update the list of valid devices.
			validDevices = (Vector<MidiDevice>) deviceList.clone();
			
			if (mdb != null) // if our mididevicebox exists yet 
				mdb.updateList(deviceList); // we update its cognate list
			//else we do nothing.
		}
		//else the list hasn't been changed and we don't want to do anything.
	}
	
	/**
	 * Sets the MidiDeviceBox that the handler sends messages to when necessary
	 * @param mdb The new MidiDeviceBox.
	 */
	public final void setDeviceBox(MidiDeviceBox mdb)
	{
		MidiHandler.mdb = mdb;
		mdb.updateList(validDevices);
	}
	
	/**
	 * Sets the new acive MIDI device, which is the device Fermata will lsiten
	 * to for MIDI messages.
	 * @param md The new active MIDI device.
	 */
	public final void setActiveDevice(MidiDevice md)
	{
		if (md == null) //If this is null, we turn off MIDI instead.
		{
			if (activeDev != null) // if we didn't already do this once.
			{
				activeDev.close(); // we stop the active device
				mr.close();
				activeDev = null;
			}
			return;
		}
		
		else if (activeDev == null) // If no device is currently active.
		{ // we just set this new device as the  active device.
			activeDev = md;
			try
			{
				activeTransmit = md.getTransmitter(); // getting the transmitter.
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Transmitter activation fail");
				e.printStackTrace();
			}
			activeTransmit.setReceiver(mr); // setting the reciever.
			try
			{
				activeDev.open(); // starts listening to the device.
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Could not open device");
				e.printStackTrace();
			}
		}
		else // activeDev != null; another device is already set so we close it
		{	//first
			activeDev.close(); // closing it.
			mr.close();
			
			//rinsing and repeating.
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
