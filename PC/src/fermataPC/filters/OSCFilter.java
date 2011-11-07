package fermataPC.filters;

import fermataPC.osc.OSCSender;
import fermataPC.util.Coordinate;

public class OSCFilter extends Filter
{
	
	public OSCFilter(int axis)
	{
		super.axis = axis;
		name = "OSC Pass-through ("+ (axis == 0 ? "x" : "y") + ")";
	}

	@Override
	public void filterStream()
	{
		//we actually don't do any filtering with OSC pass-through.
	}

	@Override
	public void setCoordinate(Coordinate coord)
	{
		OSCSender.sendCoordinate(coord);
	}
}
