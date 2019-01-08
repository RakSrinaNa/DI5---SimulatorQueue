package fr.mrcraftcod.simulator;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class Pair<T, V>{
	private final T x;
	private final V y;
	
	/**
	 * Constructor.
	 *
	 * @param x The first element.
	 * @param y The second element.
	 */
	public Pair(T x, V y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get the first element.
	 *
	 * @return The first element.
	 */
	public T getX(){
		return x;
	}
	
	/**
	 * Get the second element.
	 *
	 * @return The second element.
	 */
	public V getY(){
		return y;
	}
}
