package fermataPC.filters;

import fermataPC.util.Coordinate;

public abstract class Filter
{
	protected String name;
	private int UID;
	protected Coordinate coord;
	
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
	
	public abstract int getAxes();
	
	public abstract int getDefaultValue();
	
	public abstract void filterStream();
}
