package fermataPC.filters;

import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

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
	 * JSyn's unit oscillator, used as an lfo here.
	 */
	private UnitOscillator lfo;
	/**
	 * An adder to correct the output of the lfo
	 */
	private Add add1;

	/**
	 * Constructs the filter on the specified axis
	 * @param axis 0 for x, 1 for y
	 */	
	public BassWobbleFilter(int axis)
	{		
		super.axis = axis;
		name = "Bass Wobbler ("+ (axis == 0 ? "x" : "y") + ")";
		
		FilterProcessor.synth.add(lfo = new SineOscillator());
		FilterProcessor.synth.add(flp = new FilterLowPass());
		FilterProcessor.synth.add(add1 = new Add());

		
		flp.amplitude.set(1);
		
		flp.Q.set(5);
		
		lfo.amplitude.set(200);
		
		lfo.output.connect(0, add1.inputA, 0);
		add1.inputB.set(120); // 20hz to 220hz
		
		add1.output.connect(0, flp.frequency, 0);
		
		super.input = flp.input;
		super.output = flp.output;	
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
	}
}
