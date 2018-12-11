package fr.mrcraftcod.simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public interface Event{
	void execute(Simulator.SimulatorData data);
	
	double getTime();
}
