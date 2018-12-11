package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Simulator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class AccService extends AbstractEvent{
	private static LinkedList<Double> replayData = null;
	
	public AccService(double time){super(time);}
	
	public static void setReplayData(LinkedList<Double> replayData){
		AccService.replayData = replayData;
	}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		data.b = 1;
		final var client = data.q.poll();
		if(Objects.nonNull(client))
		{
			data.tempsMax = Math.max(data.tempsMax, getTime() - client.getTpsArr());
			data.queue.add(new DepService(getTime() + getServiceTime()));
		}
	}
	
	private double getServiceTime(){
		if(Objects.nonNull(AccService.replayData))
		{
			final var value = AccService.replayData.poll();
			if(Objects.nonNull(value))
				return value;
		}
		return 5.156735016;
	}
}
