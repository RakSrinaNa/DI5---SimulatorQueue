package fr.mrcraftcod.simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public enum SimulationMode{REPLAY, //Replay the old data
	AVERAGE, //Use the averages as the inter-arrival time & service time
	LAW_AVERAGE, //Use the law for the inter-arrivals & the average for the service time
	LAW, //Use the laws for the inter-arrivals & the service time
	EMPIRICAL //Map a uniform law onto the replay data}
