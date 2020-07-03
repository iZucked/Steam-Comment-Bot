/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class LightWeightOptimisationData implements ILightWeightOptimisationData {
	private LightWeightCargoDetails[] cargoDetails;
	private List<List<IPortSlot>> shippedCargoes;
	private List<List<IPortSlot>> nonShippedCargoes;
	private List<IVesselAvailability> vessels;
	private long[] cargoPNL;
	private long[] vesselCapacities;
	private long[][][] cargoToCargoCostsOnAvailability;
	private ArrayList<Set<Integer>> cargoVesselRestrictions;
	private int[][][] cargoToCargoMinTravelTimes;
	private int[][] cargoMinTravelTimes;
	private Map<ILoadOption, IDischargeOption> pairingsMap;
	private int[] desiredVesselCargoCount;
	private long[] desiredVesselCargoWeight;
	private long[] cargoesVolumesInM3;

	private long[][] cargoCharterCostPerAvailability;
	private Set<Integer> cargoIndexes;
	private Set<Integer> eventIndexes;
	private ITimeWindow[] vesselStartWindows;
	private ITimeWindow[] vesselEndWindows;
	private int[] cargoStartSlotDurations;
	private int[] cargoEndSlotDurations;
	private CargoWindowData[] cargoWindows;

	public LightWeightOptimisationData(List<List<IPortSlot>> shippedCargoes, List<List<IPortSlot>> nonShippedCargoes, List<IVesselAvailability> vessels, long[] vesselCapacities, long[] cargoPNL,
			long[][][] cargoToCargoCostsOnAvailability, ArrayList<Set<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes,
			Map<ILoadOption, IDischargeOption> pairingsMap, int[] desiredVesselCargoCount, long[] desiredVesselCargoWeight, long[] cargoesVolumes, LightWeightCargoDetails[] cargoDetails,
			long[][] cargoCharterCostPerAvailability, Set<Integer> cargoIndexes, Set<Integer> eventIndexes, ITimeWindow[] vesselStartWindows, ITimeWindow[] vesselEndWindows,
			int[] cargoStartSlotDurations, int[] cargoEndSlotDurations, CargoWindowData[] cargoWindows) {
		this.shippedCargoes = shippedCargoes;
		this.nonShippedCargoes = nonShippedCargoes;
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
		this.vesselStartWindows = vesselStartWindows;
		this.vesselEndWindows = vesselEndWindows;
		this.cargoStartSlotDurations = cargoStartSlotDurations;
		this.cargoEndSlotDurations = cargoEndSlotDurations;
		this.cargoWindows = cargoWindows;
		this.setDesiredVesselCargoWeight(desiredVesselCargoWeight);
		this.cargoesVolumesInM3 = cargoesVolumes;
		this.cargoDetails = cargoDetails;
	}

	public CargoWindowData[] getCargoWindows() {
		return cargoWindows;
	}

	public List<IVesselAvailability> getVessels() {
		return vessels;
	}

	public void setVessels(List<IVesselAvailability> vessels) {
		this.vessels = vessels;
	}

	@Override
	public long[] getCargoPNLPerM3() {
		return cargoPNL;
	}

	public void setCargoPNLPerM3(long[] cargoPNL) {
		this.cargoPNL = cargoPNL;
	}

	@Override
	public long[][][] getCargoToCargoCostsOnAvailability() {
		return cargoToCargoCostsOnAvailability;
	}

	public void setCargoToCargoCostsOnAvailability(long[][][] cargoToCargoCostsOnAvailability) {
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
	public List<List<IPortSlot>> getShippedCargoes() {
		return shippedCargoes;
	}

	@Override
	public LightWeightCargoDetails[] getCargoDetails() {
		return this.cargoDetails;
	}

	public void setShippedCargoes(List<List<IPortSlot>> shippedCargoes) {
		this.shippedCargoes = shippedCargoes;
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
	public long[] getVesselCapacities() {
		return vesselCapacities;
	}

	public void setVesselCapacities(long[] vesselCapacities) {
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
	public long[] getCargoesVolumesInM3() {
		return cargoesVolumesInM3;
	}

	public void setCargoesVolumesInM3(long[] cargoesVolume) {
		this.cargoesVolumesInM3 = cargoesVolume;
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

	@Override
	public ITimeWindow[] getVesselStartWindows() {
		return vesselStartWindows;
	}

	@Override
	public ITimeWindow[] getVesselEndWindows() {
		return vesselEndWindows;
	}

	@Override
	public int[] getCargoStartSlotDurations() {
		return cargoStartSlotDurations;
	}

	@Override
	public int[] getCargoEndSlotDurations() {
		return cargoEndSlotDurations;
	}

	@Override
	public int getCargoCount() {
		return shippedCargoes.size();
	}

	public List<List<IPortSlot>> getNonShippedCargoes() {
		return nonShippedCargoes;
	}
}
