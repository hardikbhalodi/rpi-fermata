package fermataPC.filters;

import java.util.Vector;

import fermataPC.util.Coordinate;

/**
 * The FilterHandler coordinates the communication between the server (phone)
 * and the FilterProcessor, which actually filters audio. As such, the
 * FilterHandler maintains a list of filters (which it initially fills), sets
 * defaults, and assigns UIDs to each filter as they are loaded.
 * 
 * FilterHandler is a singleton.
 * @author katzj2
 *
 */
public abstract class FilterHandler
{
	private static Vector<Filter> availableFilters;
	private static FilterProcessor fp;
	/**
	 * This starts the FilterHandler neatly, giving it
	 * filters to play with and handling activation of defaults.
	 */
	public static void startService()
	{
		availableFilters = new Vector<Filter>();	
		fp = FilterProcessor.getFilterProcessor();
		
		//Add available filters here as we create them.
		addFilter(new OSCFilter(0, 1));
		addFilter(new OSCFilter(1, 1));
	
		addFilter(new BandPassFilter(0));
		addFilter(new BandPassFilter(1));
		
		addFilter(new BandStopFilter(0));
		addFilter(new BandStopFilter(1));
		
		addFilter(new HiPassFilter(0));
		addFilter(new HiPassFilter(1));
		
		addFilter(new LowPassFilter(0));
		addFilter(new LowPassFilter(1));
		
		addFilter(new VolumeFilter(0));
		addFilter(new VolumeFilter(1));
		
		addFilter(new BassWobbleFilter(0));
		addFilter(new BassWobbleFilter(1));
		
		// Default filters are just OSC filters on x and y axes.
		activateFilter(0);
		activateFilter(1);
	}
	
	/**
	 * Adds a filter to the list of filters, and assigns that filter its UID
	 * so that information about it can be sent to the phone.
	 * @param newFilter The filter to add to the filter list.
	 */
	private static void addFilter(Filter newFilter)
	{
		Integer newUID = availableFilters.size();
		newFilter.setUID(newUID);
		availableFilters.add(newFilter);
	}
	
	/**
	 * Applies a coordinate to a filter. If the filter is not already active,
	 * this causes the filter to be activated, so don't apply coordinates to
	 * inactive filters unless you want them activated. In such a fashion,
	 * whatever coordinates the phone is sending -- whatever filters it uses --
	 * will be the active ones at the time.
	 * @param coord The Coordinate recieved from the phone to apply.
	 */
	public static void applyCoordinate(Coordinate coord)
	{
		Integer filterID = coord.getFilterID();
		if (filterID >= 0 && filterID < availableFilters.size())
		{
			Filter f = availableFilters.get(filterID);
			f.setCoordinate(coord);
			activateFilter(coord.getFilterID());
		}
	}	
	
	/**
	 * Activates a filter in the FilterProcessor, which will deal itself with
	 * deactivation of incompatible filters. The filterProcessor will also
	 * deal with duplicate activations of the same filter.
	 * @param UID The UID of the filter to activate.
	 */
	private static void activateFilter(int UID)
	{
		//The UID is just the filter's index in the list; that's how the
		// UID was assigned.
		Filter f = availableFilters.get(UID);
		fp.activateFilter(f);
	}
	
	/**
	 * Generates a string containing basic information about each filter,
	 * which can be passed to the phone. This function doesn't really need to
	 * know the syntax, so much as each filter itself needs to know the syntax,
	 * as this function merely concatenates each filter's description of itself.
	 * @return A string summarizing information about each filter for the
	 * Android Tool to use.
	 */
	public static String generateFilterListString()
	{
		StringBuffer filterList = new StringBuffer();
		for (Filter f : availableFilters)
		{
			String sum = f.generateStringSummary();
			filterList.append(sum);
		}
		return filterList.toString();
	}
}
