package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LightWeightOptimisationData implements ILightWeightOptimisationData {
    
	
    private PortType[] cargoTypes;
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
	private double[] cargoesVolumes;
	

    
	public LightWeightOptimisationData(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, double[] vesselCapacities,
			long[] cargoPNL, Long[][][] cargoToCargoCostsOnAvailability, ArrayList<Set<Integer>> cargoVesselRestrictions,
			int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Map<ILoadOption, IDischargeOption> pairingsMap,
			int[] desiredVesselCargoCount, long[] desiredVesselCargoWeight, double[] cargoesVolumes, PortType[] cargoTypes) {
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
				this.setDesiredVesselCargoWeight(desiredVesselCargoWeight);
				this.cargoesVolumes = cargoesVolumes;
				this.cargoTypes = cargoTypes;
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
	public void setCargoes(List<List<IPortSlot>> cargoes) {
		this.cargoes = cargoes;
	}

	public Map<ILoadOption, IDischargeOption> getPairingsMap() {
		return pairingsMap;
	}

	public void setPairingsMap(Map<ILoadOption, IDischargeOption> pairingsMap) {
		this.pairingsMap = pairingsMap;
	}

	public int[] getDesiredVesselCargoCount() {
		return desiredVesselCargoCount;
	}

	public void setDesiredVesselCargoCount(int[] desiredVesselCargoCount) {
		this.desiredVesselCargoCount = desiredVesselCargoCount;
	}

	public double[] getVesselCapacities() {
		return vesselCapacities;
	}

	public void setVesselCapacities(double[] vesselCapacities) {
		this.vesselCapacities = vesselCapacities;
	}

	public long[] getDesiredVesselCargoWeight() {
		return desiredVesselCargoWeight;
	}

	public void setDesiredVesselCargoWeight(long[] desiredVesselCargoWeight) {
		this.desiredVesselCargoWeight = desiredVesselCargoWeight;
	}

	public double[] getCargoesVolumes() {
		return cargoesVolumes;
	}

	public void setCargoesVolumes(double[] cargoesVolume) {
		this.cargoesVolumes = cargoesVolume;
	}
	
}
