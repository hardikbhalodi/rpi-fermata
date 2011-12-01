package fermataPC.filters;

import com.jsyn.unitgen.FilterBandPass;

import fermataPC.util.Coordinate;

public class BandPassFilter extends Filter
{	

	private FilterBandPass bpf;

	public BandPassFilter(int axis)
	{		
		super.axis = axis;
		name = "Bandpass ("+ (axis == 0 ? "x" : "y") + ")";
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
		
		Double freq = Math.pow(val , 1.662137217); //0 to 10,000
		bpf.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{
		FilterProcessor.synth.remove(bpf);
		
		bpf.input.disconnectAll(0);
		bpf.output.disconnectAll(0);	
	}
}
