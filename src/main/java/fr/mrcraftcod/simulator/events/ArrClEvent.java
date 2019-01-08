package fr.mrcraftcod.simulator.events;

import fr.mrcraftcod.simulator.AbstractEvent;
import fr.mrcraftcod.simulator.Client;
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

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class ArrClEvent extends AbstractEvent{
	private static final Logger LOGGER = LoggerFactory.getLogger(ArrClEvent.class);
	private static LinkedList<Double> replayData = null;
	private static AbstractRealDistribution law = null;
	private static List<Double> empirical;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	public ArrClEvent(double time){super(time);}
	
	@Override
	public void execute(Simulator.SimulatorData data){
		data.n++;
		final var client = new Client(getTime());
		data.queue.add(new ArrClEvent(getTime() + getInterTime()));
		data.queue.add(new AccFile(getTime(), client));
	}
	
	/**
	 * Get the inter-arrival time based on the current mode.
	 *
	 * @return The inter-arrival time.
	 */
	public static double getInterTime(){
		switch(Main.SIMULATION_MODE){
			case AVERAGE:
				return 5.680876085; //Average computed from the replay data.
			case REPLAY:
				final var value = ArrClEvent.replayData.poll();
				if(Objects.nonNull(value)){
					return value;
				}
				LOGGER.error("No more data in replay");
				return 1;
			case LAW:
			case LAW_AVERAGE:
				return law.sample();
			case EMPIRICAL:
				return genEmpirical();
		}
		LOGGER.error("Impossible enum value");
		return 1;
	}
	
	/**
	 * Get the inter-arrival time with an empirical model.
	 *
	 * @return The inter-arrival time.
	 */
	@SuppressWarnings("Duplicates")
	private static double genEmpirical(){
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
		ArrClEvent.law = law;
	}
	
	/**
	 * Set the replay data.
	 *
	 * @param replayData The replay data to set.
	 */
	public static void setReplayData(LinkedList<Double> replayData){
		ArrClEvent.replayData = replayData;
		ArrClEvent.empirical = replayData.stream().sorted().collect(Collectors.toList());
	}
}
