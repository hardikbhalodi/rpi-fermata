package fermataPC.filters;

import fermataPC.osc.OSCSender;

public class OSCFilter extends Filter
{
	private int axis;
	
	public OSCFilter(int axis)
	{
		this.axis = axis;
		name = "OSC Pass-through ("+ (axis == 0 ? "x" : "y") + ")";
	}
	
	@Override
	public int getAxes()
	{
		return axis;
	}

	@Override
	public void filterStream()
	{
		OSCSender.sendCoordinate(coord);
	}

	@Override
	public int getDefaultValue()
	{
		return 0;
	}

	
	
}
