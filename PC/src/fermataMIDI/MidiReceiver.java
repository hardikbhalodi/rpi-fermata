package fermataMIDI;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public final class MidiReceiver implements Receiver
{	
	@Override
	public void close()
	{
		System.out.println("Closed?");
	}

	@Override
	public void send(MidiMessage message , long timeStamp)
	{
		parseMessage(message);
	}
	
	public void parseMessage(MidiMessage message)
	{
		byte[] bitRay = message.getMessage();
		
		Byte status = bitRay[0];
		Byte data1 = bitRay[1];
		Byte data2 = bitRay[2];

		Byte command = (byte) ((status >> 4) & 15);
		Byte chan = (byte) (status & 15);
		
		System.out.println("Command: " + command + "; chan: " + chan);
		switch (command)
		{
		case 8:
			System.out.println("Note off:" + data1);
			break;
		case 9:
			System.out.println("Note on: " + data1 + "; Velocity: " + data2);
			break;
		case 10:
			System.out.println("Aftertouch? Pfft.");
			break;
		case 11: 
			System.out.println("Channel property change.");
			break;
		case 13:
			System.out.println("Channel aftertouch.");
			break;
		case 14:
			System.out.println("Pitch wheel change");
			break;
		default:
			System.out.println("What does this instruction mean?");
			break;
		}
		
		System.out.println();
	}
}
