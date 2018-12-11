package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Client;
import fr.mrcraftcod.simulator.Simulator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class ArrClEvent extends AbstractEvent{
	private static LinkedList<Double> replayData = null;
	
	public ArrClEvent(double time){super(time);}
	
	public static void setReplayData(LinkedList<Double> replayData){
		ArrClEvent.replayData = replayData;
	}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		data.n++;
		final var client = new Client(getTime());
		data.queue.add(new ArrClEvent(getTime() + getInterTime()));
		data.queue.add(new AccFile(getTime(), client));
	}
	
	public static double getInterTime(){
		if(Objects.nonNull(ArrClEvent.replayData))
		{
			final var value = ArrClEvent.replayData.poll();
			if(Objects.nonNull(value))
				return value;
		}
		return 5.680876085;
	}
}
