package fermataPC.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import fermataPC.soundOut.midiPlayer;

/**
 * The MidiReceiver deals with messages from whatever transmitter is currently
 * active.
 * 
 *  The MidiReceiver reads messages from the active MidiTransmitter (this is
 *  set by MidiHandler), parses them and dispatches commands to the synthesizer
 * @author katzj2
 *
 */
public final class MidiReceiver implements Receiver
{	
	@Override
	public void close()
	{
		//TODO: link to synth.
		System.out.println("Active MIDI disconnected/disabled");
	}

	@Override
	public void send(MidiMessage message , long timeStamp)
	{
		parseMessage(message);
	}
	
	/**
	 * This parses MIDI messages into their component parts so that proper
	 * commands can be dispatched to the synthesizer.
	 * @param message The MIDI message to parse.
	 */
	public void parseMessage(MidiMessage message)
	{
		//TODO: Link to synth.
		byte[] bitRay = message.getMessage();
		
		Byte status = bitRay[0]; // the first byte is the status byte.
		Byte data1 = bitRay[1]; // the remaining two are data bytes.
		Byte data2 = bitRay[2];

		// The first four bits of the status byte are the "command", and the
		// remaining four are generally the channel the command is to be
		// applied to.
		
		Byte command = (byte) ((status >> 4) & 15); // isolating the command
		Byte chan = (byte) (status & 15); // isolating the channel;

		System.out.println("Command: " + command + "; Channel: " + chan + "; Data 1: " + data1 + "; Data 2: " + data2);
		switch (command)
		{
		case 8: // Note off command; data1 is note.
			System.out.println("Note off:" + data1);
			midiPlayer.noteOff(data1, data2);
			break;
		case 9: // note on command; data 1 is note, data 2 is velocity.
			System.out.println("Note on: " + data1 + "; Velocity: " + data2);
			midiPlayer.noteOn(data1 , data2);
			break;
		case 10: // aftertouch (velocity changes). data 1 is the note; data 2
			// is the new velocity. We will likely not support this.
			System.out.println("Aftertouch? Pfft.");
			break;
		case 11:  // we will definitely not support this.
			System.out.println("Channel property change.");
			break;
		case 13:  // we will definitely not support this.
			System.out.println("Channel aftertouch.");
			break;
		case 14:  // we will definitely not support this.
			System.out.println("Pitch wheel change");
			break;
		default:
			break;
		}
		
		System.out.println();
	}
}
