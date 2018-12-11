package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.Event;
import fr.mrcraftcod.simulator.Simulator;
import java.util.LinkedList;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class StartEvent implements Event{
	@Override
	public void execute(Simulator.SimulatorData data){
		data.b = 0;
		data.q = new LinkedList<>();
		data.n = 0;
		data.aireQ = 0;
		data.tempsMoy = 0;
		data.tempsMax = 0;
		data.queue.add(new ArrClEvent(getTime() + ArrClEvent.getInterTime()));
		data.queue.add(new EndEvent(7200));
	}
	
	@Override
	public double getTime(){
		return 0;
	}
}
