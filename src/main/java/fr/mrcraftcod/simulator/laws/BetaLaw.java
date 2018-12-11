package fr.mrcraftcod.simulator.laws;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-12-11.
 *
 * @author Thomas Couchoud
 * @since 2018-12-11
 */
public class BetaLaw implements StatisticLaw{
	private final double alpha;
	private final double beta;
	
	public BetaLaw(double alpha, double beta){
		this.alpha = alpha;
		this.beta = beta;
	}
	
	@Override
	public double get(){
		return 0;
	}
}
