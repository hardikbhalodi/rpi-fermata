package fermataPC.filters;

import com.jsyn.unitgen.Latch;

import fermataPC.osc.OSCSender;
import fermataPC.util.Coordinate;

public class OSCFilter extends Filter
{
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
		OSCSender.sendCoordinate(coord);
	}

	@Override
	public void cancelFilter()
	{
		output.disconnectAll(0);
		input.disconnectAll(0);
	}
}
