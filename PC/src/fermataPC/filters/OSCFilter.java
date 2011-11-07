package fermataPC.filters;

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
		//TODO anything.
	}
}
