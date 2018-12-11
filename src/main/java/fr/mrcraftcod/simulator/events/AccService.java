package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Main;
import fr.mrcraftcod.simulator.Simulator;
import fr.mrcraftcod.simulator.laws.StatisticLaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class AccService extends AbstractEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccService.class);
	private static LinkedList<Double> replayData = null;
	private StatisticLaw law = null;
	
	public AccService(double time){super(time);}
	
	public static void setReplayData(LinkedList<Double> replayData){
		AccService.replayData = replayData;
	}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		data.b = 1;
		final var client = data.q.poll();
		if(Objects.nonNull(client)){
			data.tempsMax = Math.max(data.tempsMax, getTime() - client.getTpsArr());
			data.queue.add(new DepService(getTime() + getServiceTime()));
		}
	}
	
	private double getServiceTime(){
		switch(Main.mode){
			case AVERAGE:
			case LAW_AVERAGE:
				return 5.156735016;
			case REPLAY:
				final var value = AccService.replayData.poll();
				if(Objects.nonNull(value)){
					return value;
				}
				LOGGER.error("No more data in replay");
				return 1;
			case LAW:
				return law.get();
		}
		LOGGER.error("Impossible enum value");
		return 1;
	}
}
