package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.events.AccService;
import fr.mrcraftcod.simulator.events.ArrClEvent;
import fr.mrcraftcod.simulator.laws.BetaLaw;
import fr.mrcraftcod.simulator.laws.ExponentialLaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class Main{
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	public static SimulationMode mode = SimulationMode.LAW_AVERAGE;
	private static final int maxRepl = 10;
	
	public static void main(String[] args) throws IOException{
		final var results = new HashMap<Integer, Simulator.SimulatorData>();
		for(int i = 0; i < maxRepl; i++){
			LOGGER.info("Starting replication {}/{}", i, maxRepl);
			if(mode == SimulationMode.REPLAY){
				LOGGER.info("Reading DataAppels files");
				final var replayData = loadReplayData("DataAppels.txt");
				ArrClEvent.setReplayData(replayData.getX());
				AccService.setReplayData(replayData.getY());
			}
			ArrClEvent.setLaw(new ExponentialLaw(0.175));
			AccService.setLaw(new BetaLaw(2.5, 6.4));
			final var simulator = new Simulator();
			simulator.start();
			results.put(i, simulator.getData());
		}
		LOGGER.info("Final data: {}", results);
	}
	
	private static Pair<LinkedList<Double>, LinkedList<Double>> loadReplayData(@SuppressWarnings("SameParameterValue") final String filename) throws IOException{
		final var lines = Files.readAllLines(Paths.get(new File(filename).toURI()));
		final var interTime = new LinkedList<Double>();
		final var serviceTime = new LinkedList<Double>();
		var lastArrival = 0D;
		for(final var line : lines){
			final var linePart = line.split(" ");
			final var a = Double.parseDouble(linePart[0]);
			interTime.add(a - lastArrival);
			serviceTime.add(Double.parseDouble(linePart[1]));
			lastArrival = a;
		}
		return new Pair<>(interTime, serviceTime);
	}
}
