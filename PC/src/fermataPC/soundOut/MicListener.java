package fermataPC.soundOut;

import com.jsyn.unitgen.LineIn;

import fermataPC.filters.FilterProcessor;

public abstract class MicListener
{
	private static LineIn lineIn;
	
	private static boolean started = false;
	
	public static final void startService()
	{
		if (started)
			return;
		
		FilterProcessor.synth.add(lineIn = new LineIn());
		started = true;		
	}
	
	public static void startListening()
	{
		FilterProcessor.connectOutput(lineIn.output);
	}
	
	public static void stopListening()
	{
		lineIn.output.disconnectAll(0);
	}
}