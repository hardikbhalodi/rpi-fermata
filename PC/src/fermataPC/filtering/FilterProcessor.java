package fermataPC.filtering;

import java.util.Vector;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.AudioDeviceManager;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;



/**
 * The FilterProcessor class handles the coordination of audio and filters.
 * When a filter is activated, the old filter on its axis must be deactivated
 * and the audio connections between the various filters available must be
 * re-routed so the signal goes through the new processor instead of the old.
 * This class coordinates that activity.
 * 
 * The filter processor is a singleton.
 * @author katzj2
 *
 */
public final class FilterProcessor
{	
	/**
	 * This is the JSyn synthesizer. It MUST exist before anything can be done
	 * using JSyn audio, as it coordinates actions of all JSyn objects.
	 */
	public static final Synthesizer synth = JSyn.createSynthesizer();	
	
	/**
	 * Only one can exist, so once it is initialized, self is returned to all
	 * requests for FilterProcessors.
	 */
	private static final FilterProcessor self = new FilterProcessor();
	
	/**
	 * This is the list of AudioInputs the FilterProcessor listens to.
	 */
	private Vector<UnitOutputPort> filterInputs;
	
	/**
	 * The LineOut sends audio to the sound card.
	 */
	private final LineOut lineOut = new LineOut();
	
	/**
	 * The currently active filters on the x and y axes.
	 */
	private Filter xFilter, yFilter;
	
	/**
	 * The constructor is private, as only one FilterProcessor should exist
	 * at a time, as such, FilterProcessors can only be acquired using the
	 * getFilterProcessor() function which calls this only the first time.
	 */
	private FilterProcessor()
	{
		super();
		synth.add(lineOut);
		synth.start( 44100, AudioDeviceManager.USE_DEFAULT_DEVICE, 2, AudioDeviceManager.USE_DEFAULT_DEVICE,
				2 );
		double timeNow = synth.getCurrentTime();
		double time = timeNow + .5;
		synth.startUnit(lineOut, time);
		filterInputs = new Vector<UnitOutputPort>();
	}
	
	/**
	 * This is the only way to get a FilterProcessor; it ensures there is only
	 * one. If it has not been constructed yet, constructs it.
	 * @return The only FilterProcessor allowed.
	 */
	public static FilterProcessor getFilterProcessor()
	{
		return self;
	}
	
	/**
	 * Takes an output port and listens to it by adding it to the list of
	 * inputs. Calls reRouteFilters to force filters to listen to the new
	 * input.
	 * @param listenTo The new port to listen to.
	 */
	public void connectOutput(UnitOutputPort listenTo)
	{
		filterInputs.add(listenTo);
		reRouteFilters();
	}
	
	/**
	 * activateFilter activates the filter it is passed on that filter's axis,
	 * and deactivates whatever filter follows it.
	 * 
	 * activateFilter is the only non-static function aside from the
	 * connstructor. By necessitating that a FilterProcessor exists to call it,
	 * we ensure that the Synth has been started and that processing truly
	 * can take place.
	 * 
	 * @param f The filter to activate
	 * @return True if successful, false otherwise.
	 */
	public Boolean activateFilter(Filter f)
	{
		if (xFilter == f || yFilter == f)
			return false;
		
		if (f.getAxes() == 0)
		{
			deactivateFilter(xFilter);
			xFilter = f;
			reRouteFilters();
		}
		else if (f.getAxes() == 1)
		{
			deactivateFilter(yFilter);
			yFilter = f;
			reRouteFilters();
		}
		else if (f.getAxes() == 2)
		{
			deactivateFilter(xFilter);
			deactivateFilter(yFilter);
			xFilter = f;
			reRouteFilters();
		}
		
		return true; // false if failed. It can't fail yet, though.
	}
	
	/**
	 * reRouteFilters re-routes the audio chain through the FilterProcessor
	 * to the LineOut so as to send audio from all of the filterInputs
	 * through the x filter, then the y filter, and then through the lineOut.
	 */
	private void reRouteFilters()
	{
		System.out.println("xFilter is " + xFilter.name);
		if (yFilter != null)
			System.out.println("yfilter is " + yFilter.name);
		System.out.println("Re-routing filters");
		
		if (filterInputs.size() == 0)
			return;
		
		// In case any inputs are bad (from having been removed elsewhere, etc)
		//  we should remove here them to reduce clutter.
		Vector<UnitOutputPort> badInputs = new Vector<UnitOutputPort>();
		for (UnitOutputPort filterInput : filterInputs)
		{
			if (filterInput == null || filterInput.getUnit() == null)
			{ // Add to the list of bad inputs if it is bad.
				badInputs.add(filterInput);
				continue;
			}
			
			// Otherwise, disconnect all outputs from this filter
			//  and connect to the xFilter's input.
			filterInput.disconnectAll(0);		
			filterInput.connect(0, xFilter.input, 0);
		}
		
		for (UnitOutputPort badInput : badInputs)
		{ // remove bad filters.
			filterInputs.remove(badInput);
		}
	
		// make sure lineOut isn't listening to dead filters.
		lineOut.input.disconnectAll(0);
		lineOut.input.disconnectAll(1);
		
		if (yFilter == null || xFilter.getAxes() == 2)
		{
			xFilter.output.connect(0, lineOut.input, 0);
			xFilter.output.connect(0, lineOut.input, 1);
		}
		else
		{
			xFilter.output.connect(0, yFilter.input, 0);
			
			yFilter.output.connect(0, lineOut.input, 0);
			yFilter.output.connect(0, lineOut.input, 1);
		}
	}
	
	/**
	 * This can be called to force the deactivation of a filter
	 * @param f The filter to deactivate
	 * @return true if filter removed, false otherwise (say, if the filter
	 * was null)
	 */
	private static Boolean deactivateFilter(Filter f)
	{
		if (f != null)
		{
			f.cancelFilter();
			return true;
		}
		return false;
	}
}
