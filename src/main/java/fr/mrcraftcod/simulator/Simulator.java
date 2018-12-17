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
	private final DecimalFormatSymbols symbols;
	private final DecimalFormat nf;
	private double time;
	
	public class SimulatorData{
		public int guichetCount;
		public int[] b;
		public LinkedList<Client> q;
		public int n;
		public double aireQ;
		public double tempsMoy;
		public double tempsMax;
		public PriorityQueue<Event> queue;
		
		@Override
		public String toString(){
			return new StringJoiner(", ", SimulatorData.class.getSimpleName() + "[", "]").add("b=" + b).add("q=" + q.size()).add("n=" + n).add("aireQ=" + aireQ).add("tempsMoy=" + tempsMoy).add("tempsMax=" + tempsMax).toString();
		}
		
		public String asCsv(){
			var sb = new StringBuilder();
			sb.append(Arrays.stream(b).mapToObj(i -> "" + i).collect(Collectors.joining(";")));
			sb.append(";");
			sb.append(q.size());
			sb.append(";");
			sb.append(n);
			sb.append(";");
			sb.append(nf.format(aireQ));
			sb.append(";");
			sb.append(nf.format(tempsMoy));
			sb.append(";");
			sb.append(nf.format(tempsMax));
			return sb.toString();
		}
	}
	
	public Simulator(File file) throws FileNotFoundException{
		this.data = new SimulatorData();
		this.dataLog = new PrintWriter(new FileOutputStream(file));
		symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		nf = new DecimalFormat("#,##0.00#", symbols);
		nf.setGroupingUsed(false);
	}
	
	public void start(int guichetCount){
		if(Objects.nonNull(dataLog)){
			dataLog.println(String.format("time;willExecute;%s;Q;N;AireQ;TempsMoy;TempsMax", IntStream.range(0, guichetCount).mapToObj(i -> "B" + (1 + i)).collect(Collectors.joining(";"))));
		}
		time = 0;
		this.data.queue = new PriorityQueue<>(Comparator.comparingDouble(Event::getTime));
		new StartEvent(guichetCount).execute(data);
		Event evt;
		while((evt = data.queue.poll()) != null){
			LOGGER.debug("Executing event at t={} => {}", evt.getTime(), evt);
			majAreas(data, time, evt.getTime());
			writeDataFile(evt.getClass().getSimpleName(), evt.getTime());
			time = evt.getTime();
			evt.execute(data);
		}
		if(Objects.nonNull(dataLog)){
			dataLog.close();
		}
	}
	
	private void writeDataFile(String simpleName, double time){
		if(Objects.nonNull(dataLog)){
			dataLog.println(String.format("%s;%s;%s", nf.format(time), simpleName, data.asCsv()));
		}
	}
	
	private void majAreas(SimulatorData data, double time, double evtTime){
		data.aireQ += (evtTime - time) * data.q.size();
	}
	
	public SimulatorData getData(){
		return this.data;
	}
}
