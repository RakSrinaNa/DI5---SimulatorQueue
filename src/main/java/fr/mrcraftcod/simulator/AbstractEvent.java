package fr.mrcraftcod.simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-30.
 *
 * @author Thomas Couchoud
 * @since 2018-11-30
 */
public abstract class AbstractEvent implements Event{
	private final double time;
	
	/**
	 * Constructor.
	 *
	 * @param time The time of the event.
	 */
	protected AbstractEvent(double time){
		this.time = time;
	}
	
	@Override
	public double getTime(){
		return this.time;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
}
