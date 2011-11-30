package fermataPC.filters;

import com.jsyn.unitgen.FilterBandPass;

import fermataPC.util.Coordinate;

public class BandPassFilter extends Filter
{	

	private FilterBandPass bpf1;
	private FilterBandPass bpf2;

	public BandPassFilter(int axis)
	{		
		super.axis = axis;
		name = "Bandpass ("+ (axis == 0 ? "x" : "y") + ")";
		bpf1 = new FilterBandPass();
		bpf2 = new FilterBandPass();

		FilterProcessor.synth.add(bpf1);
		FilterProcessor.synth.add(bpf2);
		
		bpf1.frequency.setMaximum(10000);
		bpf1.frequency.setMinimum(0);
		bpf2.frequency.setMaximum(10000);
		bpf2.frequency.setMinimum(0);
		
		bpf1.amplitude.set(2);
		
		bpf1.amplitude.set(100);
		bpf2.amplitude.set(.01);
		
		bpf1.Q.set(5);
		bpf2.Q.set(5);
		
		bpf1.frequency.set(440);
		bpf2.frequency.set(440);
		
		input = bpf1.input;
		output = bpf2.output;
		bpf1.output.connect(bpf2.input);
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		int val = coord.getValue();
		
		Double freq = Math.pow(val , 1.662137217); //0 to 10,000
		bpf1.frequency.set(freq);
		bpf2.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{
		FilterProcessor.synth.remove(bpf1);
		
		bpf1.input.disconnectAll(0);
		bpf2.input.disconnectAll(0);
		bpf1.output.disconnectAll(0);
		bpf2.output.disconnectAll(0);		
	}
}
