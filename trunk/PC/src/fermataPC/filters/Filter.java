package fermataPC.filters;

public abstract class Filter
{
	private String name;
	private int UID;
	
	public String getName()
	{
		return name;
	}
	
	protected void setUID(int UID)
	{
		this.UID = UID;
	}
	public int getUID()
	{
		return UID;
	}
	
	public abstract int getAxes();
	
	public abstract int getDefaultValue();
	
	
	public abstract void filterStream();
}
