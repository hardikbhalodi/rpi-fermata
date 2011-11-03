package fermataPC.filters;

public class OSCFilter extends Filter
{

	private int axis;
	
	public OSCFilter(int axis)
	{
		this.axis = axis;
	}
	
	@Override
	public int getAxes()
	{
		return axis;
	}

	@Override
	public void filterStream()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDefaultValue()
	{
		return 0;
	}

}
