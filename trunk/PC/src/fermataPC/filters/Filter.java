package fermataPC.filters;

import fermataPC.util.Coordinate;

public abstract class Filter
{
	protected String name;
	protected int UID;
	protected Coordinate coord;
	protected int axis;
	protected int defaultValue;
	
	public abstract String generateStringSummary();
	
	public final String getName()
	{
		return name;
	}
	
	protected final void setUID(int UID)
	{
		this.UID = UID;
	}
	public final int getUID()
	{
		return UID;
	}	
	
	public final void setCoordinate(Coordinate coord)
	{
		this.coord = coord;
	}
	
	public final int getAxes()
	{
		return axis;
	}
	
	public abstract void filterStream();
}
