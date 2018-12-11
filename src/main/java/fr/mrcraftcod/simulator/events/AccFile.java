package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Client;
import fr.mrcraftcod.simulator.Simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class AccFile extends AbstractEvent{
	private final Client client;
	
	public AccFile(double time, Client client){
		super(time);
		this.client = client;
	}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		data.q.add(client);
		if(data.b == 0)
		{
			data.queue.add(new AccService(getTime()));
		}
	}
}
