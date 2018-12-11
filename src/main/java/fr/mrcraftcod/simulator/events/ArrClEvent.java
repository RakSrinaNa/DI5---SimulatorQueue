package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Client;
import fr.mrcraftcod.simulator.Main;
import fr.mrcraftcod.simulator.Simulator;
import fr.mrcraftcod.simulator.laws.StatisticLaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class ArrClEvent extends AbstractEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(ArrClEvent.class);
	private static LinkedList<Double> replayData = null;
	private static StatisticLaw law = null;
	
	public ArrClEvent(double time){super(time);}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		data.n++;
		final var client = new Client(getTime());
		data.queue.add(new ArrClEvent(getTime() + getInterTime()));
		data.queue.add(new AccFile(getTime(), client));
	}
	
	public static double getInterTime(){
		switch(Main.mode){
			case AVERAGE:
				return 5.680876085;
			case REPLAY:
				final var value = ArrClEvent.replayData.poll();
				if(Objects.nonNull(value)){
					return value;
				}
				LOGGER.error("No more data in replay");
				return 1;
			case LAW:
			case LAW_AVERAGE:
				return law.get();
		}
		LOGGER.error("Impossible enum value");
		return 1;
	}
	
	public static void setLaw(StatisticLaw law){
		ArrClEvent.law = law;
	}
	
	public static void setReplayData(LinkedList<Double> replayData){
		ArrClEvent.replayData = replayData;
	}
}
