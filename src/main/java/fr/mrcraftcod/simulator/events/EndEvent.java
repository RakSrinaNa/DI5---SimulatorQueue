package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class EndEvent extends AbstractEvent{
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	public EndEvent(double time){super(time);}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		data.queue.clear();
		data.tempsMoy = 1.0 / data.n * data.aireQ;
	}
}
