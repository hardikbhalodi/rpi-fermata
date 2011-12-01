package fermataPC.filters;

import fermataPC.util.Coordinate;
import com.jsyn.unitgen.Multiply;

public class VolumeFilter extends Filter
{

	private Multiply volFilter;
	
	public VolumeFilter(int axis)
	{	
		super.axis = axis;
		name = "Volume ("+ (axis == 0 ? "x" : "y") + ")";
		volFilter = new Multiply();
		
		FilterProcessor.synth.add(volFilter);
		
		input = volFilter.inputA;
		output = volFilter.output;
		
	}
	@Override
	public void setCoordinate(Coordinate coord) 
	{
		// TODO Auto-generated method stub
		double val = coord.getValue();
		val = val/200;
		
		volFilter.inputB.set(val);
	}

	@Override
	public void cancelFilter() 
	{
		// TODO Auto-generated method stub
		FilterProcessor.synth.remove(volFilter);
		
		volFilter.inputA.disconnectAll(0);
		volFilter.output.disconnectAll(0);	
		
	}	
	
}



