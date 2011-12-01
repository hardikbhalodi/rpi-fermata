package fermataPC.util;

/**
 * This format will be used internally to represent a Coordinate such as
 * the Android tool sends. Each coordinate is a filter ID and a scalar,
 * the scalar being an integer from 0-255.
 * 
 * @author katzj2
 *
 */
public class Coordinate
{
	private int filterID = -1;
	private int value = 0;
	
	/**
	 * Creates the Coordinate. Filter IDs can range from -1 to infty, and are
	 * the ID of the filter, which is dynamic and linked to the filter
	 * superclass. -1 indicates an unset filter.. The scalar value
	 * must be between 0 and 255.
	 * @param filterID The FilterID of the filter which this coordinate is
	 * created for.
	 * @param value the value of this filter's scalar.
	 */
	public Coordinate(int filterID, int value)
	{
		this.filterID = filterID < -1 ? -1 : filterID;
		this.value = value < 0 ? 0 : value >= 256 ? 255 : value; 
	}
	
	/**
	 * An empty constructor, creating a Coordinate which can be modified later.
	 */
	public Coordinate(){}
	
	/**
	 * Setting the Filter ID for this coordinate.
	 * @param filterID the coordinate's FilterID
	 */
	public void setFilter(int filterID)
	{
		this.filterID = filterID < -1 ? -1 : filterID;
	}
	
	/**
	 * Setting the scalar value for this coordinate.
	 * @param value the coordinate's scalar value.
	 */
	public void setValue(int value)
	{
		this.value = value < 0 ? 0 : value >= 256 ? 255 : value;
	}
	
	/**
	 * Gets the Coordinate's FilterID
	 * @return the coordinate's current filterID.
	 */
	public int getFilterID() { return filterID;}
	
	/**
	 * Gets the Coordinate's scalar value.
	 * @return the coordinate's current scalar value.
	 */
	public int getValue() { return value;}
	
	public boolean equals(Coordinate coord)
	{
		return (coord.filterID == filterID && coord.value == value);
	}
		
}
