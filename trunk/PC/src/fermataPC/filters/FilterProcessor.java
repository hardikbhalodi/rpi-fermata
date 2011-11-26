package fermataPC.filters;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.PowerOfTwo;

public final class FilterProcessor
{
	@SuppressWarnings("unused")
	private static PowerOfTwo powerOfTwo;
	
	private static FilterProcessor self;
	//public static UnitInputPort input;
	public static final Synthesizer synth = JSyn.createSynthesizer();
	
	private static UnitOutputPort filterInput;
	
	public static final LineOut lineOut = new LineOut();
	
	private static Filter xFilter, yFilter;
	
	private FilterProcessor()
	{		
		super();
			
		synth.add(lineOut);
		
		synth.add(powerOfTwo = new PowerOfTwo());
		
		synth.start();
		
		double timeNow = synth.getCurrentTime();
		
		double time = timeNow + .5;
		
		synth.startUnit(lineOut, time);
		
		self = this;
	}
	
	public static FilterProcessor getFilterProcessor()
	{
		if (self == null)
		{
			self = new FilterProcessor();
		}
		return self;
	}
	
	public static void connectOutput(UnitOutputPort listenTo)
	{
		filterInput = listenTo;
		reRouteFilters();
	}
	
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
	
	private static void reRouteFilters()
	{
		
		System.out.println("xFilter is " + xFilter.name);
		if (yFilter != null)
			System.out.println("yfilter is " + yFilter.name);
		System.out.println("Re-routing filters");
		
		if (filterInput == null)
			return;
		
		filterInput.disconnectAll(0);
		
		filterInput.connect(0, xFilter.input, 0);
		
		
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
