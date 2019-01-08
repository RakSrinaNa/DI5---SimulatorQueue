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
	
	public final static SimulationMode SIMULATION_MODE = SimulationMode.EMPIRICAL;
	private static final int REPLICATION_COUNT = 100; //Number of replications
	private final static int COUNTER_COUNT = 1; //Number of counter(s)
	private final static boolean GEN_LOGS = true; //Generate CSV logs
	
	/**
	 * Main method.
	 *
	 * @param args Not used.
	 *
	 * @throws IOException If an input file couldn't be read.
	 */
	public static void main(String[] args) throws IOException{
		final var results = new HashMap<Integer, Simulator.SimulatorData>();
		for(int i = 0; i < REPLICATION_COUNT; i++){
			LOGGER.info("Starting replication {}/{}", i + 1, REPLICATION_COUNT);
			
			//If we need to read the input file, read it
			if(SIMULATION_MODE == SimulationMode.REPLAY || SIMULATION_MODE == SimulationMode.EMPIRICAL){
				LOGGER.info("Reading DataAppels files");
				final var replayData = loadReplayData("DataAppels.txt");
				ArrClEvent.setReplayData(replayData.getX());
				AccService.setReplayData(replayData.getY());
				LOGGER.info("Read file DataAppels");
			}
			
			//Set up laws
			ArrClEvent.setLaw(new ExponentialDistribution(1 / 0.175));
			AccService.setLaw(new BetaDistribution(2.5, 6.4));
			
			//Run simulator
			final var simulator = new Simulator(GEN_LOGS ? new File(String.format("DataLog-%d.csv", i + 1)) : null);
			simulator.start(COUNTER_COUNT);
			results.put(i, simulator.getData());
		}
		LOGGER.info("Final data:\n{}", results.entrySet().stream().map(entry -> String.format("Replication %d: %s", entry.getKey() + 1, entry.getValue().toString())).collect(Collectors.joining("\n")));
		
		LOGGER.info("Final Data as CSV:\n{}", asCSV(results));
	}
	
	/**
	 * Load the replay data from a CSV file.
	 *
	 * @param filename The path of the file to open.
	 *
	 * @return The data read.
	 *
	 * @throws IOException If the file couldn't be read.
	 */
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
	
	/**
	 * Exports the results as a CSV to be imported in a spreadsheet.
	 *
	 * @param results The results to export.
	 *
	 * @return A CSV String.
	 */
	private static String asCSV(HashMap<Integer, Simulator.SimulatorData> results){
		return String.format("%s;Q;N;AireQ;TempsMoy;TempsMax\n", IntStream.range(0, COUNTER_COUNT).mapToObj(i -> "B" + (1 + i)).collect(Collectors.joining(";"))) + results.values().stream().map(Simulator.SimulatorData::asCsv).collect(Collectors.joining("\n"));
	}
}
