package fermataPC.filters;

public class OSCFilter extends Filter
{
	
	public OSCFilter(int axis)
	{
		super.axis = axis;
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
		//TODO anything.
	}

	@Override
	public int getDefaultValue()
	{
		return 0;
	}

	@Override
	public String generateStringSummary()
	{
		return new String(name + "," + UID + "," + axis + "," + 0);
	}
}
