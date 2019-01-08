package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Simulator;
import java.util.stream.IntStream;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class DepService extends AbstractEvent{
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	public DepService(double time){super(time);}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		if(!data.q.isEmpty()){
			data.queue.add(new AccService(getTime()));
		}
		IntStream.range(0, data.guichetCount).filter(i -> data.b[i] == 1).findFirst().ifPresent(index -> data.b[index] = 0);
	}
}
