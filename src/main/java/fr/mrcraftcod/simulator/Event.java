package fr.mrcraftcod.simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public interface Event{
	/**
	 * Execute the event.
	 *
	 * @param data The data of the simulation.
	 */
	void execute(Simulator.SimulatorData data);
	
	/**
	 * Get the time of the event.
	 *
	 * @return The time of the event.
	 */
	double getTime();
}
