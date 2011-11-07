package fermataPC.filters;

public class OSCFilter extends Filter
{
	
	public OSCFilter(int axis)
	{
		super.axis = axis;
		super.defaultValue = 0;
		name = "OSC Pass-through ("+ (axis == 0 ? "x" : "y") + ")";
	}

	@Override
	public void filterStream()
	{
		//TODO anything.
	}

	@Override
	public String generateStringSummary()
	{
		return new String(name + "," + UID + "," + axis + "," + 0 +";");
	}
}
