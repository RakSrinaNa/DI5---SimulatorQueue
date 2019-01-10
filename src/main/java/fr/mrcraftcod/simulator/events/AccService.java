package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Main;
import fr.mrcraftcod.simulator.Simulator;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class AccService extends AbstractEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccService.class);
	private static LinkedList<Double> replayData = null;
	private static AbstractRealDistribution law = null;
	private static List<Double> empirical;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	public AccService(double time){super(time);}
	
	/**
	 * Get the service time based on the current mode.
	 *
	 * @return The service time.
	 */
	private double getServiceTime(){
		switch(Main.SIMULATION_MODE){
			case AVERAGE:
			case LAW_AVERAGE:
				return 5.156735016; //Average computed from the replay data.
			case REPLAY:
				final var value = AccService.replayData.poll();
				if(Objects.nonNull(value)){
					return value;
				}
				LOGGER.error("No more data in replay");
				return 1;
			case LAW:
				return (18.24 - 0.15) * law.sample() + 0.15; //Normalized (max - min) * rnd + min
			case EMPIRICAL:
				return genEmpirical();
		}
		LOGGER.error("Impossible enum value");
		return 1;
	}
	
	/**
	 * Get the service time with an empirical model.
	 *
	 * @return The service time.
	 */
	@SuppressWarnings("Duplicates")
	private double genEmpirical(){
		if(Objects.nonNull(replayData)){
			var rnd = ThreadLocalRandom.current().nextDouble(); //Uniform law in [0;1[
			
			//Map it into the empirical data
			var index = rnd * (empirical.size() - 1);
			var indexInt = (int) index;
			var indexFloat = index - indexInt;
			var x1 = empirical.get(indexInt);
			var x2 = empirical.get(indexInt + 1);
			return x1 + indexFloat * (x2 - x1);
		}
		return 1;
	}
	
	/**
	 * Set the law to use.
	 *
	 * @param law The law to set.
	 */
	public static void setLaw(AbstractRealDistribution law){
		AccService.law = law;
		if(replayData != null)
			AccService.empirical = replayData.stream().sorted().collect(Collectors.toList());
	}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		IntStream.range(0, data.guichetCount).filter(i -> data.b[i] == 0).findFirst().ifPresent(index -> {
			data.b[index] = 1;
			final var client = data.q.poll();
			if(Objects.nonNull(client)){
				data.tempsMax = Math.max(data.tempsMax, getTime() - client.getTpsArr());
				data.queue.add(new DepService(getTime() + getServiceTime()));
				data.waitTimeClient.add(getTime() - client.getTpsArr());
			}
		});
	}
	
	/**
	 * Set the replay data.
	 *
	 * @param replayData The replay data to set.
	 */
	public static void setReplayData(LinkedList<Double> replayData){
		AccService.replayData = replayData;
	}
}
