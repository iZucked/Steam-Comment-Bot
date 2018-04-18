package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class LightWeightOptimisationData implements ILightWeightOptimisationData {
	private List<List<IPortSlot>> cargoes;
	private List<IVesselAvailability> vessels;
	private long[] cargoPNL;
	private Long[][][] cargoToCargoCostsOnAvailability;
	private ArrayList<Set<Integer>> cargoVesselRestrictions;
	private int[][][] cargoToCargoMinTravelTimes;
	private int[][] cargoMinTravelTimes;
	private Map<ILoadOption, IDischargeOption> pairingsMap;
	
	public LightWeightOptimisationData(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, long[] cargoPNL,
			Long[][][] cargoToCargoCostsOnAvailability, ArrayList<Set<Integer>> cargoVesselRestrictions,
			int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Map<ILoadOption, IDischargeOption> pairingsMap) {
				this.cargoes = cargoes;
				this.vessels = vessels;
				this.cargoPNL = cargoPNL;
				this.cargoToCargoCostsOnAvailability = cargoToCargoCostsOnAvailability;
				this.cargoVesselRestrictions = cargoVesselRestrictions;
				this.cargoToCargoMinTravelTimes = cargoToCargoMinTravelTimes;
				this.cargoMinTravelTimes = cargoMinTravelTimes;
				this.pairingsMap = pairingsMap;
	}
	
	public List<IVesselAvailability> getVessels() {
		return vessels;
	}
	public void setVessels(List<IVesselAvailability> vessels) {
		this.vessels = vessels;
	}
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightOptimisationData#getCargoPNL()
	 */
	@Override
	public long[] getCargoPNL() {
		return cargoPNL;
	}
	public void setCargoPNL(long[] cargoPNL) {
		this.cargoPNL = cargoPNL;
	}
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightOptimisationData#getCargoToCargoCostsOnAvailability()
	 */
	@Override
	public Long[][][] getCargoToCargoCostsOnAvailability() {
		return cargoToCargoCostsOnAvailability;
	}
	public void setCargoToCargoCostsOnAvailability(Long[][][] cargoToCargoCostsOnAvailability) {
		this.cargoToCargoCostsOnAvailability = cargoToCargoCostsOnAvailability;
	}
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightOptimisationData#getCargoVesselRestrictions()
	 */
	@Override
	public ArrayList<Set<Integer>> getCargoVesselRestrictions() {
		return cargoVesselRestrictions;
	}
	public void setCargoVesselRestrictions(ArrayList<Set<Integer>> cargoVesselRestrictions) {
		this.cargoVesselRestrictions = cargoVesselRestrictions;
	}
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightOptimisationData#getCargoToCargoMinTravelTimes()
	 */
	@Override
	public int[][][] getCargoToCargoMinTravelTimes() {
		return cargoToCargoMinTravelTimes;
	}
	public void setCargoToCargoMinTravelTimes(int[][][] cargoToCargoMinTravelTimes) {
		this.cargoToCargoMinTravelTimes = cargoToCargoMinTravelTimes;
	}
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightOptimisationData#getCargoMinTravelTimes()
	 */
	@Override
	public int[][] getCargoMinTravelTimes() {
		return cargoMinTravelTimes;
	}
	public void setCargoMinTravelTimes(int[][] cargoMinTravelTimes) {
		this.cargoMinTravelTimes = cargoMinTravelTimes;
	}
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightOptimisationData#getCargoes()
	 */
	@Override
	public List<List<IPortSlot>> getCargoes() {
		return cargoes;
	}
	public void setCargoes(List<List<IPortSlot>> cargoes) {
		this.cargoes = cargoes;
	}

	public Map<ILoadOption, IDischargeOption> getPairingsMap() {
		return pairingsMap;
	}

	public void setPairingsMap(Map<ILoadOption, IDischargeOption> pairingsMap) {
		this.pairingsMap = pairingsMap;
	}
}
