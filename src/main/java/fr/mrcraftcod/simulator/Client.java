package fr.mrcraftcod.simulator;

import java.util.StringJoiner;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class Client{
	private final double tpsArr;
	
	/**
	 * Constructor.
	 *
	 * @param tpsArr Its arrival time.
	 */
	public Client(double tpsArr){
		this.tpsArr = tpsArr;
	}
	
	/**
	 * Get the arrival time of the client.
	 *
	 * @return The arrival time.
	 */
	public double getTpsArr(){
		return this.tpsArr;
	}
	
	@Override
	public String toString(){
		return new StringJoiner(", ", Client.class.getSimpleName() + "[", "]").add("tpsArr=" + tpsArr).toString();
	}
}
