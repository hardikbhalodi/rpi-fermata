package fermataUtil;

public class Coordinate
{
	private int filterID = -1;
	private int value = 0;
	
	public Coordinate(int filterID, int value)
	{
		this.filterID = filterID < -1 ? -1 : filterID;
		this.value = value < 0 ? 0 : value >= 256 ? 255 : value; 
	}
	
	public Coordinate()
	{
	}
	
	public void setFilter(int filterID)
	{
		this.filterID = filterID < -1 ? -1 : filterID;
	}
	
	public void setValue(int value)
	{
		this.value = value < 0 ? 0 : value >= 256 ? 255 : value;
	}
	
	public int getFilterID() { return filterID;}
	
	public int getValue() { return value;}
		
}
