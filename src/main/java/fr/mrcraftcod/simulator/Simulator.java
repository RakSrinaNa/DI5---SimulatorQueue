package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.events.StartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class Simulator{
	private static final Logger LOGGER = LoggerFactory.getLogger(Simulator.class);
	
	private final SimulatorData data;
	private final PrintWriter dataLog;
	private final DecimalFormat nf;
	
	/**
	 * Our simulation data.
	 */
	public class SimulatorData{
		public int guichetCount;
		public int[] b;
		public LinkedList<Client> q;
		public int n;
		public double aireQ;
		public double tempsMoy;
		public double tempsMax;
		public PriorityQueue<Event> queue;
		public List<Double> waitTimeClient;
		
		@Override
		public String toString(){
			return new StringJoiner(", ", SimulatorData.class.getSimpleName() + "[", "]").add("b=" + Arrays.toString(b)).add("q=" + q.size()).add("n=" + n).add("aireQ=" + aireQ).add("tempsMoy=" + tempsMoy).add("tempsMax=" + tempsMax).toString();
		}
		
		public String asCsv(){
			return Arrays.stream(b).mapToObj(i -> "" + i).collect(Collectors.joining(";")) + ";" + q.size() + ";" + n + ";" + nf.format(aireQ) + ";" + nf.format(tempsMoy) + ";" + nf.format(tempsMax);
		}
	}
	
	/**
	 * Constructor.
	 *
	 * @param file The file to write the CSV log into.
	 *
	 * @throws FileNotFoundException If the file couldn't be create.
	 */
	public Simulator(File file) throws FileNotFoundException{
		this.data = new SimulatorData();
		this.dataLog = new PrintWriter(new FileOutputStream(file));
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		nf = new DecimalFormat("#,##0.00#", symbols);
		nf.setGroupingUsed(false);
	}
	
	/**
	 * Starts the simulator.
	 *
	 * @param counterCount The number of counter.
	 */
	public void start(int counterCount){
		if(Objects.nonNull(dataLog)){
			dataLog.println(String.format("time;willExecute;%s;Q;N;AireQ;TempsMoy;TempsMax", IntStream.range(0, counterCount).mapToObj(i -> "B" + (1 + i)).collect(Collectors.joining(";"))));
		}
		double time = 0;
		this.data.queue = new PriorityQueue<>(Comparator.comparingDouble(Event::getTime));
		new StartEvent(counterCount).execute(data); //Start the first event instantly
		Event evt;
		while((evt = data.queue.poll()) != null){ //While we have an event to execute
			LOGGER.debug("Executing event at t={} => {}", evt.getTime(), evt);
			majAreas(data, time, evt.getTime()); //Update the areas
			writeDataFile(data, evt.getClass().getSimpleName(), evt.getTime());
			time = evt.getTime();
			evt.execute(data); //Execute event
		}
		if(Objects.nonNull(dataLog)){
			dataLog.close();
		}
	}
	
	/**
	 * Update the areas.
	 *
	 * @param data    The datas to update.
	 * @param time    The current time.
	 * @param evtTime The time of the next event.
	 */
	private void majAreas(SimulatorData data, double time, double evtTime){
		data.aireQ += (evtTime - time) * data.q.size();
	}
	
	/**
	 * Write current datas to the CSV log.
	 *
	 * @param data       The datas to write.
	 * @param simpleName The name of the event.
	 * @param time       The time of the event.
	 */
	private void writeDataFile(SimulatorData data, String simpleName, double time){
		if(Objects.nonNull(dataLog)){
			dataLog.println(String.format("%s;%s;%s", nf.format(time), simpleName, data.asCsv()));
		}
	}
	
	/**
	 * Get the current data of the simulator.
	 *
	 * @return The data of the simulator.
	 */
	public SimulatorData getData(){
		return this.data;
	}
}
