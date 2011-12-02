package fermataPC.sound;

import com.jsyn.unitgen.LineIn;

import fermataPC.filtering.FilterProcessor;

/**
 * The MicListener allows play-through of the system's line in to be 
 * turned on and off.
 * @author katzj2
 *
 */
public abstract class MicListener
{
	private static LineIn lineIn;
	
	private static boolean started = false;
	
	/**
	 * startService starts the MicListening service, intializing what little
	 * needs to be initialized.
	 */
	public static final void startService()
	{
		if (started)
			return;
		
		FilterProcessor.synth.add(lineIn = new LineIn());
		started = true;		
	}
	
	/**
	 * Turns on the microphone.
	 */
	public static void startListening()
	{
		FilterProcessor.connectOutput(lineIn.output);
	}
	
	/**
	 * Turns off the microphone.
	 */
	public static void stopListening()
	{
		lineIn.output.disconnectAll(0);
	}
}