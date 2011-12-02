import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.AudioDeviceManager;
import com.jsyn.unitgen.FilterBandPass;
import com.jsyn.unitgen.LineIn;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;

/**
 * Play a tone using a phase modulated sinewave oscillator.
 * Phase modulation (PM) is very similar to frequency modulation (FM) but is easier to control.
 * 
 * @author Phil Burk (C) 2010 Mobileer Inc
 * 
 */
public class SineWaver
{
/*	private Synthesizer synth;
	SineOscillator sinwaver;
	LineOut lineOut;

	public void init()
	{
		synth = JSyn.createSynthesizer();
		// Add a tone generator.
		synth.add( sinwaver = new SineOscillator() );
		// add an lfo;
		SineOscillator lfo = new SineOscillator();
		synth.add(lfo);
		
		FilterBandPass bpf = new FilterBandPass();
		synth.add(bpf);
	//	bpf.amplitude.set(.5);
		
		// Add an output mixer.
		synth.add( lineOut = new LineOut());

		sinwaver.frequency.set(440);
		sinwaver.output.connect(0, bpf.input, 0);
			
		lfo.frequency.set(.25);
		lfo.amplitude.setMaximum(440);
		lfo.amplitude.set(440);
		lfo.output.connect(bpf.frequency);
			
		bpf.output.connect(0, lineOut.input, 0);
		bpf.output.connect(0, lineOut.input, 1);				
	}

	public void start()
	{
		// Start synthesizer using default stereo output at 44100 Hz.
		synth.start();
	//	scope.start();
		// We only need to start the LineOut. It will pull data from the
		// oscillator.
		synth.startUnit( lineOut );
	}

	public void stop()
	{
		synth.stop();
	}

	/* Can be run as either an application or as an applet. */
/*	public static void main( String args[] )
	{
		SineWaver applet = new SineWaver();
		
		applet.init();
		applet.start();
	}	/**/
	
	Synthesizer synth;
	LineIn lineIn;
	LineOut lineOut;

	private void test()
	{
		// Create a context for the synthesizer.
		synth = JSyn.createSynthesizer();
		// Add an audio input.
		synth.add( lineIn = new LineIn() );
		// Add an audio output.
		synth.add( lineOut = new LineOut() );
		// Connect the input to the output.
		lineIn.output.connect( 0, lineOut.input, 0 );
		lineIn.output.connect( 1, lineOut.input, 1 );

		// Both stereo.
		int numInputChannels = 2;
		int numOutputChannels = 2;
		synth.start( 44100, AudioDeviceManager.USE_DEFAULT_DEVICE, numInputChannels, AudioDeviceManager.USE_DEFAULT_DEVICE,
				numOutputChannels );

		// We only need to start the LineOut. It will pull data from the LineIn.
		lineOut.start();
		// Sleep a while.
		try
		{
			double time = synth.getCurrentTime();
			// Sleep for a few seconds.
			synth.sleepUntil( time + 4.0 );
		} catch( InterruptedException e )
		{
			e.printStackTrace();
		}
		// Stop everything.
		synth.stop();
	}

	public static void main( String[] args )
	{
		new SineWaver().test();
	}
	
}