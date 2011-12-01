/*package fermataPC.filters;
 * 
 */
//TODO get delay filters working in the future
/*
import com.jfhpsyn.unitgen.InterpolatingDelay;

import fermataPC.util.Coordinate;

public class DelayFilter extends Filter
{
	private InterpolatingDelay delFilter;
	
	public DelayFilter(int axis)
	{	
		super.axis = axis;
		name = "Delay ("+ (axis == 0 ? "x" : "y") + ")";
		delFilter = new InterpolatingDelay();
		
		FilterProcessor.synth.add(delFilter);
		delFilter.allocate(10000);
		
		input = delFilter.input;
		output = delFilter.output;
		
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		int val = coord.getValue();
		
		
	}

	@Override
	public void cancelFilter() 
	{
		// TODO Auto-generated method stub
		
		//delFilter.stop();
		delFilter.input.disconnectAll(0);
		delFilter.output.disconnectAll(0);	
		
	}	
	
}*/