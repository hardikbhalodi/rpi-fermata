package fermataPC.filters;

import com.jsyn.unitgen.FilterHighPass;

import fermataPC.util.Coordinate;

public class HiPassFilter extends Filter
{	

	private FilterHighPass fhp;
	
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
		
		Double freq = Math.pow(val , 1.662137217); //0 to 10,000
		fhp.frequency.set(freq);
	}
	@Override
	public void cancelFilter()
	{		
		fhp.input.disconnectAll(0);
		fhp.output.disconnectAll(0);
	}
}
