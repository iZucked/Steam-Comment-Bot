package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class LightWeightOptimisationData implements ILightWeightOptimisationData {
    
	
    private LightWeightCargoDetails[] cargoDetails;
	private List<List<IPortSlot>> cargoes;
	private List<IVesselAvailability> vessels;
	private long[] cargoPNL;
	private double[] vesselCapacities;
	private Long[][][] cargoToCargoCostsOnAvailability;
	private ArrayList<Set<Integer>> cargoVesselRestrictions;
	private int[][][] cargoToCargoMinTravelTimes;
	private int[][] cargoMinTravelTimes;
	private Map<ILoadOption, IDischargeOption> pairingsMap;
	private int[] desiredVesselCargoCount;
	private long[] desiredVesselCargoWeight;
	
	// Cargo volumes in mmbtu
	private long[] cargoesVolumes;
	
	private long[][] cargoCharterCostPerAvailability;
	private Set<Integer> cargoIndexes;
	private Set<Integer> eventIndexes;
	
	public LightWeightOptimisationData(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, double[] vesselCapacities,
			long[] cargoPNL, Long[][][] cargoToCargoCostsOnAvailability, ArrayList<Set<Integer>> cargoVesselRestrictions,
			int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Map<ILoadOption, IDischargeOption> pairingsMap,
			int[] desiredVesselCargoCount, long[] desiredVesselCargoWeight, long[] cargoesVolumes, LightWeightCargoDetails[] cargoDetails,
			long[][] cargoCharterCostPerAvailability, Set<Integer> cargoIndexes, Set<Integer> eventIndexes) {
				this.cargoes = cargoes;
				this.vessels = vessels;
				this.vesselCapacities = vesselCapacities;
				this.cargoPNL = cargoPNL;
				this.cargoToCargoCostsOnAvailability = cargoToCargoCostsOnAvailability;
				this.cargoVesselRestrictions = cargoVesselRestrictions;
				this.cargoToCargoMinTravelTimes = cargoToCargoMinTravelTimes;
				this.cargoMinTravelTimes = cargoMinTravelTimes;
				this.pairingsMap = pairingsMap;
				this.desiredVesselCargoCount = desiredVesselCargoCount;
				this.cargoCharterCostPerAvailability = cargoCharterCostPerAvailability;
				this.cargoIndexes = cargoIndexes;
				this.eventIndexes = eventIndexes;
				this.setDesiredVesselCargoWeight(desiredVesselCargoWeight);
				this.cargoesVolumes = cargoesVolumes;
				this.cargoDetails = cargoDetails;
	}
	
	public List<IVesselAvailability> getVessels() {
		return vessels;
	}
	public void setVessels(List<IVesselAvailability> vessels) {
		this.vessels = vessels;
	}

	@Override
	public long[] getCargoPNL() {
		return cargoPNL;
	}
	public void setCargoPNL(long[] cargoPNL) {
		this.cargoPNL = cargoPNL;
	}

	@Override
	public Long[][][] getCargoToCargoCostsOnAvailability() {
		return cargoToCargoCostsOnAvailability;
	}
	public void setCargoToCargoCostsOnAvailability(Long[][][] cargoToCargoCostsOnAvailability) {
		this.cargoToCargoCostsOnAvailability = cargoToCargoCostsOnAvailability;
	}

	@Override
	public ArrayList<Set<Integer>> getCargoVesselRestrictions() {
		return cargoVesselRestrictions;
	}
	public void setCargoVesselRestrictions(ArrayList<Set<Integer>> cargoVesselRestrictions) {
		this.cargoVesselRestrictions = cargoVesselRestrictions;
	}

	@Override
	public int[][][] getCargoToCargoMinTravelTimes() {
		return cargoToCargoMinTravelTimes;
	}
	public void setCargoToCargoMinTravelTimes(int[][][] cargoToCargoMinTravelTimes) {
		this.cargoToCargoMinTravelTimes = cargoToCargoMinTravelTimes;
	}

	@Override
	public int[][] getCargoMinTravelTimes() {
		return cargoMinTravelTimes;
	}
	public void setCargoMinTravelTimes(int[][] cargoMinTravelTimes) {
		this.cargoMinTravelTimes = cargoMinTravelTimes;
	}

	@Override
	public List<List<IPortSlot>> getCargoes() {
		return cargoes;
	}
	
	@Override
	public LightWeightCargoDetails[] getCargoDetails() {
		return this.cargoDetails;
	}
	
	public void setCargoes(List<List<IPortSlot>> cargoes) {
		this.cargoes = cargoes;
	}

	@Override
	public Map<ILoadOption, IDischargeOption> getPairingsMap() {
		return pairingsMap;
	}

	public void setPairingsMap(Map<ILoadOption, IDischargeOption> pairingsMap) {
		this.pairingsMap = pairingsMap;
	}

	@Override
	public int[] getDesiredVesselCargoCount() {
		return desiredVesselCargoCount;
	}

	public void setDesiredVesselCargoCount(int[] desiredVesselCargoCount) {
		this.desiredVesselCargoCount = desiredVesselCargoCount;
	}

	@Override
	public double[] getVesselCapacities() {
		return vesselCapacities;
	}

	public void setVesselCapacities(double[] vesselCapacities) {
		this.vesselCapacities = vesselCapacities;
	}

	@Override
	public long[] getDesiredVesselCargoWeight() {
		return desiredVesselCargoWeight;
	}

	public void setDesiredVesselCargoWeight(long[] desiredVesselCargoWeight) {
		this.desiredVesselCargoWeight = desiredVesselCargoWeight;
	}

	@Override
	public long[] getCargoesVolumes() {
		return cargoesVolumes;
	}

	public void setCargoesVolumes(long[] cargoesVolume) {
		this.cargoesVolumes = cargoesVolume;
	}

	@Override
	public long[][] getCargoCharterCostPerAvailability() {
		return cargoCharterCostPerAvailability;
	}

	public void setCargoCharterCostPerAvailability(long[][] cargoCharterCostPerAvailability) {
		this.cargoCharterCostPerAvailability = cargoCharterCostPerAvailability;
	}
	
	@Override
	public Set<Integer> getCargoIndexes() {
		return cargoIndexes;
	}

	@Override
	public Set<Integer> getEventIndexes() {
		return eventIndexes;
	}
}
