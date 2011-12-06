package fermataPC.filtering.filters;

import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.Multiply;

import fermataPC.filtering.Filter;
import fermataPC.filtering.FilterProcessor;
import fermataPC.util.Coordinate;

/**
 * The TremeloFilter cycles smoothly between high volume (full) and low volume (almost none).
 * This filters behaves like the Tremelo functionality found on many guitar amplifiers.
 * @author Tyler Sammann
 *
 */
public class TremeloFilter extends Filter
{	
	/**
	 * JSyn's built in multiply
	 */
	private Multiply volF;
	/**
	 * JSyn's sine oscillator, used as an oscillator here.
	 */
	private SineOscillator oscil;
	/**
	 * An adder to correct the output of the oscillator
	 */
	private Add add1;

	/**
	 * Constructs the filter on the specified axis
	 * @param axis 0 for x, 1 for y
	 */	
	public TremeloFilter(int axis)
	{		
		super.axis = axis;
		name = "Tremelo ("+ (axis == 0 ? "x" : "y") + ")";
		
		FilterProcessor.synth.add(oscil = new SineOscillator());
		FilterProcessor.synth.add(volF = new Multiply());
		FilterProcessor.synth.add(add1 = new Add());
		
		oscil.amplitude.set(1);
		
		oscil.output.connect(0, add1.inputA, 0);
		add1.inputB.set(1); // amplitude from 0.5 to 1.5 for smooth tremelo effect
		
		add1.output.connect(0, volF.inputB, 0);
		
		super.input = volF.inputA;
		super.output = volF.output;	
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		double val = coord.getValue();
		
		//want range .5-8
		double freq;
		
		freq = val / (255/7.5) + .5;
		
		oscil.frequency.set(freq);	
	}
	@Override
	public void cancelFilter()
	{		
		volF.inputA.disconnectAll(0);
		volF.output.disconnectAll(0);
	}
}