package fermataPC.filtering.filters;

import com.jsyn.unitgen.Latch;

import fermataPC.filtering.Filter;
import fermataPC.filtering.FilterProcessor;
import fermataPC.osc.OSCSender;
import fermataPC.util.Coordinate;

/**
 * The OSC filter passes coordinate messages it recieves to the
 * OSC output. It either passes audio through or silences it,
 * based on its construction.
 * @author katzj2
 *
 */
public class OSCFilter extends Filter
{
	private final OSCSender oscSend = OSCSender.getOSCSender();
	
	/**
	 * Constructs an OSC Pass-through. We send the audio it receives through
	 * a latch, which we turn either on or off with the passAudio argument.
	 * @param axis 0 for x, 1 for y.
	 * @param passAudio < 1 : silent, >= 1 : pass-through.
	 */
	public OSCFilter(int axis, int passAudio)
	{
		super.axis = axis;
		name = "OSC Pass-through ("+ (axis == 0 ? "x" : "y") + ")";
		
		Latch latch = new Latch();
		
		FilterProcessor.synth.add(latch);
		
		output = latch.output;
		input = latch.input;
		
		latch.gate.set(passAudio);
		
	}

	@Override
	public void setCoordinate(Coordinate coord)
	{
		//sends it right along to the OSC sender.
		oscSend.sendCoordinate(coord);
	}

	@Override
	public void cancelFilter()
	{
		output.disconnectAll(0);
		input.disconnectAll(0);
	}
}
