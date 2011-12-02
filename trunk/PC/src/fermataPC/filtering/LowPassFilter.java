package fermataPC.filtering;

import com.jsyn.unitgen.FilterLowPass;

import fermataPC.util.Coordinate;

/**
 * The a low-pass filter passes audio with frequencies below the cutoff.
 * @author katzj2
 *
 */
public class LowPassFilter extends Filter
{	
	/**
	 * JSyn's built-in low pass filter.
	 */
	private FilterLowPass flp;
	
	/**
	 * Constructs a low-pass filter to be used on the specified axis
	 * @param axis 0 for x, 1 for y.
	 */
	public LowPassFilter(int axis)
	{		
		super.axis = axis;
		name = "Lowpass ("+ (axis == 0 ? "x" : "y") + ")";
		flp = new FilterLowPass();

		FilterProcessor.synth.add(flp);
		
		flp.frequency.setMaximum(10000);
		flp.frequency.setMinimum(0);

		flp.Q.set(5);
		flp.amplitude.set(1);
		
		flp.frequency.set(220);
		
		input = flp.input;
		output = flp.output;
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		int val = coord.getValue();
		Double freq;
		
		if (val < 230)
			if (val > 3)
				freq = Math.pow(val , 1.53705); //0 to 10,000. . . normally.
			else freq = 0.0;
		else // val > 230.
			freq = Math.pow(val, 1.9) - (Math.pow(230, 1.9) - Math.pow(230, 1.53705)); // increase steepness, normalize to remove bump.
		flp.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{		
		flp.input.disconnectAll(0);
		flp.output.disconnectAll(0);
	}
}
