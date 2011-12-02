package fermataPC.filtering.filters;

import com.jsyn.unitgen.FilterBandStop;

import fermataPC.filtering.Filter;
import fermataPC.filtering.FilterProcessor;
import fermataPC.util.Coordinate;

/**
 * a band-stop filter silences audio in a narrow band around the cutoff
 * frequency.
 * @author katzj2
 *
 */
public class BandStopFilter extends Filter
{	

	/**
	 * JSyn's built in band-stop.
	 */
	private FilterBandStop bsf;

	/**
	 * Constructs the filter on the specified axis
	 * @param axis 0 for x, 1 for y
	 */
	public BandStopFilter(int axis)
	{		
		super.axis = axis;
		super.name = "Bandstop ("+ (axis == 0 ? "x" : "y") + ")";
		bsf = new FilterBandStop();

		FilterProcessor.synth.add(bsf);
		
		bsf.frequency.setMaximum(10000);
		bsf.frequency.setMinimum(0);
		
		bsf.amplitude.set(1);
		
		bsf.Q.set(5);
		
		bsf.frequency.set(440);
		
		input = bsf.input;
		output = bsf.output;
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		int val = coord.getValue();
		
		Double freq = Math.pow(val , 1.53705); //0 to 5,000
		bsf.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{		
		bsf.input.disconnectAll(0);
		bsf.output.disconnectAll(0);	
	}
}
