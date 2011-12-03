package fermataPC.sound;

import com.jsyn.unitgen.LineIn;

import fermataPC.filtering.FilterProcessor;

/**
 * The MicListener allows play-through of the system's line in to be 
 * turned on and off. MicListener is a singleton.
 * @author katzj2
 *
 */
public class MicListener
{
	private static LineIn lineIn;
	
	private static final MicListener self = new MicListener();
	
	/**
	 * startService starts the MicListening service, intializing what little
	 * needs to be initialized.
	 */
	private MicListener()
	{		
		FilterProcessor.synth.add(lineIn = new LineIn());
	}
	
	/**
	 * Gets the sole instance of the MicListener singleton
	 * @return 
	 */
	public static MicListener getMicListener()
	{
		return self;
	}
	
	/**
	 * Turns on the microphone.
	 */
	public void startListening()
	{
		FilterProcessor.getFilterProcessor().connectOutput(lineIn.output);
	}
	
	/**
	 * Turns off the microphone.
	 */
	public void stopListening()
	{
		lineIn.output.disconnectAll(0);
	}
}