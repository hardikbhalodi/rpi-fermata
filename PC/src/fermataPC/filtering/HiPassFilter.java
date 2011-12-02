package fermataPC.filtering;

import com.jsyn.unitgen.FilterHighPass;

import fermataPC.util.Coordinate;

/**
 * A high-pass filter passes audio with frequencies higher than the cutoff.
 * @author katzj2
 *
 */
public class HiPassFilter extends Filter
{	
	/**
	 * JSyn's built in High Pass filter.
	 */
	private FilterHighPass fhp;

	/**
	 * Constructs a high pass filter for use on the specified axis
	 * @param axis 0 for x, 1 for y.
	 */
	public HiPassFilter(int axis)
	{		
		super.axis = axis;
		name = "Highpass ("+ (axis == 0 ? "x" : "y") + ")";
		fhp = new FilterHighPass();

		FilterProcessor.synth.add(fhp);
		
		fhp.frequency.setMaximum(10000);
		fhp.frequency.setMinimum(0);

		fhp.Q.set(5);
		fhp.amplitude.set(1);
		
		fhp.frequency.set(220);
		
		input = fhp.input;
		output = fhp.output;
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		int val = coord.getValue();
		Double freq;
		
		if (val < 230)
			if (val > 3)
				freq = Math.pow(val , 1.662137217); //0 to 10,000. . . normally.
			else freq = 0.0;
		else // val > 230.
			freq = Math.pow(val, 1.8604) - (Math.pow(230, 1.8604) -Math.pow(230, 1.662137217)); // increase steepness, normalize to remove bump.
		
		fhp.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{		
		fhp.input.disconnectAll(0);
		fhp.output.disconnectAll(0);
	}
}
