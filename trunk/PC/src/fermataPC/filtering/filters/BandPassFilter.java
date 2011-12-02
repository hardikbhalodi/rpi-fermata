package fermataPC.filtering.filters;

import com.jsyn.unitgen.FilterBandPass;

import fermataPC.filtering.Filter;
import fermataPC.filtering.FilterProcessor;
import fermataPC.util.Coordinate;

/**
 * A band-pass filter passes audio in a narrow band around the cutoff frequency
 * @author katzj2
 *
 */
public class BandPassFilter extends Filter
{	
	/**
	 * JSyn's built in band-pass
	 */
	private FilterBandPass bpf;

	/**
	 * Constructs the filter on the specified axis
	 * @param axis 0 for x, 1 for y
	 */
	public BandPassFilter(int axis)
	{		
		super.axis = axis;
		super.name = "Bandpass ("+ (axis == 0 ? "x" : "y") + ")";
		bpf = new FilterBandPass();

		FilterProcessor.synth.add(bpf);
		
		bpf.frequency.setMaximum(10000);
		bpf.frequency.setMinimum(0);
		
		bpf.amplitude.set(1);
		
		bpf.Q.set(5);
		
		bpf.frequency.set(440);
		
		input = bpf.input;
		output = bpf.output;
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		int val = coord.getValue();
		
		Double freq = Math.pow(val , 1.53705); //0 to 5,000
		bpf.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{		
		bpf.input.disconnectAll(0);
		bpf.output.disconnectAll(0);	
	}
}
