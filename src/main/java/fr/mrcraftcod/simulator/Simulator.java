package fr.mrcraftcod.simulator;

import fr.mrcraftcod.simulator.events.StartEvent;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.StringJoiner;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public class Simulator{
	private final SimulatorData data;
	private double time;
	
	public class SimulatorData{
		public int b;
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
	}
	
	public Simulator(){
		this.data = new SimulatorData();
	}
	
	public void start(){
		time = 0;
		this.data.queue = new PriorityQueue<>(Comparator.comparingDouble(Event::getTime));
		new StartEvent().execute(data);
		Event evt;
		while((evt = data.queue.poll()) != null){
			System.out.println("Executing event (t=" + evt.getTime() + "): " + evt);
			majAreas(data, time, evt.getTime());
			time = evt.getTime();
			evt.execute(data);
		}
	}
	
	private void majAreas(SimulatorData data, double time, double evtTime){
		data.aireQ += (evtTime - time) * data.q.size();
	}
	
	public SimulatorData getData(){
		return this.data;
	}
}
