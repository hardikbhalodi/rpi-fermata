package fermataPC.filters;

import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.FilterHighPass;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.TriangleOscillator;

import fermataPC.util.Coordinate;

/**
 * The BassWobble filter applies basic wobble to the low frequencies at
 * a frequency set by the coordinate input.
 * @author katzj2
 *
 */
public class BassWobbleFilter extends Filter
{	
	/**
	 * JSyn's built in lowpass
	 */
	private FilterLowPass flp;
	/**
	 * JSyn's high pass
	 */
	private FilterHighPass fhp;
	/**
	 * JSyn's triangle oscillator, used as an lfo here.
	 */
	private TriangleOscillator lfo;
	/**
	 * Dummy inputs and outputs so audio can be split within and
	 * recombined.
	 */
	private PassThrough realIn, realOut;
	/**
	 * An adder to correct the output of the lfo
	 */
	private Add add1;

	/**
	 * Constructs the filter on the specified axis
	 * @param axis 0 for x, 1 for y
	 */
	
	private Multiply mux1;
	
	public BassWobbleFilter(int axis)
	{		
		super.axis = axis;
		name = "Bass Wobbler ("+ (axis == 0 ? "x" : "y") + ")";
		
		FilterProcessor.synth.add(lfo = new TriangleOscillator());
		FilterProcessor.synth.add(fhp = new FilterHighPass());
		FilterProcessor.synth.add(flp = new FilterLowPass());
		FilterProcessor.synth.add(realIn = new PassThrough());
		FilterProcessor.synth.add(realOut = new PassThrough());
		FilterProcessor.synth.add(add1 = new Add());
		FilterProcessor.synth.add(mux1 = new Multiply());
		
		realIn.output.connect(0, flp.input, 0);
		
		realOut.input.connect(0, flp.output, 0);
		
		fhp.frequency.set(240);
		
		realIn.output.connect(0, fhp.input, 0);
		fhp.output.connect(0, realOut.input, 0);
		
		fhp.output.connect(0, mux1.inputA, 0);
		
		mux1.inputB.set(.8);
		
		mux1.output.connect(0, realOut.input, 0);
		
		flp.amplitude.set(1);
		
		flp.Q.set(5);
		
		lfo.amplitude.set(200);
		
		lfo.output.connect(0, add1.inputA, 0);
		add1.inputB.set(120); // 20hz to 220hz
		
		add1.output.connect(0, flp.frequency, 0);
		
		super.input = realIn.input;
		super.output = realOut.output;	
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		double val = coord.getValue();
		
		//want range .5-8
		double freq;
		
		freq = val / (255/7.5) + .5;
		
		lfo.frequency.set(freq);		
	}
	@Override
	public void cancelFilter()
	{		
		flp.input.disconnectAll(0);
		flp.output.disconnectAll(0);
		fhp.input.disconnectAll(0);
		fhp.output.disconnectAll(0);
		realIn.input.disconnectAll(0);
		realOut.output.disconnectAll(0);
	}
}
