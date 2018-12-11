package fr.mrcraftcod.simulator.laws;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class ExponentialLaw implements StatisticLaw{
	private final double lambda;
	
	public ExponentialLaw(double lambda){
		this.lambda = lambda;
	}
	
	@Override
	public double get(){
		return -(1 / lambda) * Math.log(1 - ThreadLocalRandom.current().nextDouble());
	}
}
