package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.events.AccService;
import fr.mrcraftcod.simulator.events.ArrClEvent;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class Main{
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private static final int maxRepl = 100;
	public static SimulationMode mode = SimulationMode.AVERAGE;
	private static int guichetCount = 1;
	
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
			ArrClEvent.setLaw(new ExponentialDistribution(1 / 0.175));
			AccService.setLaw(new BetaDistribution(2.5, 6.4));
			final var simulator = new Simulator();
			simulator.start(guichetCount);
			results.put(i, simulator.getData());
		}
		LOGGER.info("Final data:\n{}", results.entrySet().stream().map(entry -> String.format("Replication %d: %s", entry.getKey() + 1, entry.getValue().toString())).collect(Collectors.joining("\n")));
		
		LOGGER.info("Final Data as CSV:\n{}", asCSV(results));
	}
	
	private static String asCSV(HashMap<Integer, Simulator.SimulatorData> results){
		final var sb = new StringBuilder();
		sb.append(String.format("%s;Q;N;AireQ;TempsMoy;TempsMax\n", IntStream.range(0, guichetCount).mapToObj(i -> "B" + (1 + i)).collect(Collectors.joining(";"))));
		for(var result : results.values()){
			sb.append(Arrays.stream(result.b).mapToObj(i -> "" + i).collect(Collectors.joining(";")));
			sb.append(";");
			sb.append(result.q.size());
			sb.append(";");
			sb.append(result.n);
			sb.append(";");
			sb.append(result.aireQ);
			sb.append(";");
			sb.append(result.tempsMoy);
			sb.append(";");
			sb.append(result.tempsMax);
			sb.append("\n");
		}
		return sb.toString();
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
