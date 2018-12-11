package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class DepService extends AbstractEvent{
	public DepService(double time){super(time);}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		if(!data.q.isEmpty())
		{
			data.queue.add(new AccService(getTime()));
		}
		data.b = 0;
	}
}
