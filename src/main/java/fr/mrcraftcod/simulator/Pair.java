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
	
	public Pair(T x, V y){
		this.x = x;
		this.y = y;
	}
	
	public T getX(){
		return x;
	}
	
	public V getY(){
		return y;
	}
}
