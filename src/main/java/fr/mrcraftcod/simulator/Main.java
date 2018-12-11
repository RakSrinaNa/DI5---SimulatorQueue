package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.events.AccService;
import fr.mrcraftcod.simulator.events.ArrClEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class Main{
	public static void main(String[] args) throws IOException{
		if(true){
			final var replayData = loadReplayData("DataAppels.txt");
			ArrClEvent.setReplayData(replayData.getX());
			AccService.setReplayData(replayData.getY());
		}
		final var simulator = new Simulator();
		simulator.start();
		final var data = simulator.getData();
		System.out.println(data);
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
