package fermataPC.filters;

import com.jsyn.unitgen.FilterBandStop;

import fermataPC.util.Coordinate;

public class BandStopFilter extends Filter
{	

	private FilterBandStop bsf;

	public BandStopFilter(int axis)
	{		
		super.axis = axis;
		name = "Bandstop ("+ (axis == 0 ? "x" : "y") + ")";
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
		
		Double freq = Math.pow(val , 1.662137217); //0 to 10,000
		bsf.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{
		FilterProcessor.synth.remove(bsf);
		
		bsf.input.disconnectAll(0);
		bsf.output.disconnectAll(0);	
	}
}
