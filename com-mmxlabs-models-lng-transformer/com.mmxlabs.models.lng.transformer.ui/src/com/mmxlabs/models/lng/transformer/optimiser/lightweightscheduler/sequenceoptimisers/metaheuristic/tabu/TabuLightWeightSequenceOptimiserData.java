/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightCargoDetails;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class TabuLightWeightSequenceOptimiserData {
	double[][][] cargoToCargoCostsProcessed;
	List<List<Integer>> cargoVesselRestrictionAsList;
	double[] capacity;
	double[] cargoPNLasDouble;
	private CargoWindowData[] cargoWindows;
	LightWeightCargoDetails[] cargoDetails;
	private double[][] cargoDailyCharterCostPerAvailabilityProcessed;
	private double[] cargoesVolumesProcessed;

	public TabuLightWeightSequenceOptimiserData(ILightWeightOptimisationData lightWeightOptimisationData) {
		List<List<IPortSlot>> cargoes = lightWeightOptimisationData.getCargoes();
		List<IVesselAvailability> vessels = lightWeightOptimisationData.getVessels();
		long[] cargoPNL = lightWeightOptimisationData.getCargoPNL();
		Long[][][] cargoToCargoCostsOnAvailability = lightWeightOptimisationData.getCargoToCargoCostsOnAvailability();
		List<Set<Integer>> cargoVesselRestrictions = lightWeightOptimisationData.getCargoVesselRestrictions();
		this.cargoDetails = lightWeightOptimisationData.getCargoDetails();
		cargoDailyCharterCostPerAvailabilityProcessed = processCharterCosts(lightWeightOptimisationData);
		cargoesVolumesProcessed = LongStream.of(lightWeightOptimisationData.getCargoesVolumesInM3())
				.mapToDouble(d-> ((double) d) / (double) Calculator.ScaleFactor).toArray();
		
		// Capacity
		this.capacity = vessels.stream().mapToDouble(v -> v.getVessel().getCargoCapacity() / (double) Calculator.ScaleFactor).toArray();
		
		// CargoPNL
		this.cargoPNLasDouble = new double[cargoPNL.length];
		for (int i = 0; i < cargoPNLasDouble.length; i++) {
			cargoPNLasDouble[i] = (double) cargoPNL[i];
		}
		
		// ALEX_NOTE: why change set to list?
		// Cargo Restriction
		this.cargoVesselRestrictionAsList = new ArrayList<List<Integer>>(cargoVesselRestrictions.size());
		for (Set<Integer> restriction : cargoVesselRestrictions) {
			cargoVesselRestrictionAsList.add(restriction.stream().collect(Collectors.toList()));
		}

		// Cargo To Cargo cost
		this.cargoToCargoCostsProcessed = new double[cargoToCargoCostsOnAvailability.length][cargoToCargoCostsOnAvailability[0].length][cargoToCargoCostsOnAvailability[0][0].length];
		for (int i = 0; i < cargoToCargoCostsOnAvailability.length; i++) {
			for (int j = 0; j < cargoToCargoCostsOnAvailability[i].length; j++) {
				for (int k = 0; k < cargoToCargoCostsOnAvailability[i][j].length; k++) {
					this.cargoToCargoCostsProcessed[i][j][k] = (double) cargoToCargoCostsOnAvailability[i][j][k] / (double) Calculator.HighScaleFactor;
				}
			}
		}

		this.cargoWindows = calculateCargoWindows(lightWeightOptimisationData, cargoes);
	}

	private CargoWindowData[] calculateCargoWindows(ILightWeightOptimisationData lightWeightOptimisationData, List<List<IPortSlot>> cargoes) {
		CargoWindowData[] cargoWindows = new CargoWindowData[cargoes.size()];

		for (int i = 0; i < cargoes.size(); i++) {
			if (lightWeightOptimisationData.getCargoIndexes().contains(i)) {
				ITimeWindow loadTW = cargoes.get(i).get(0).getTimeWindow();
				ITimeWindow dischargeTW = cargoes.get(i).get(1).getTimeWindow();
				
				cargoWindows[i] = new CargoWindowData(loadTW.getInclusiveStart(), loadTW.getExclusiveEnd(),
						dischargeTW.getInclusiveStart(), dischargeTW.getExclusiveEnd());
			} else {
				ITimeWindow tw  = cargoes.get(i).get(0).getTimeWindow();
				
				// vessel events are vessel independent for duration so get time from first vessel
				cargoWindows[i] = new CargoWindowData(tw.getInclusiveStart(), tw.getExclusiveEnd(),
						tw.getInclusiveStart() + lightWeightOptimisationData.getCargoMinTravelTimes()[i][0],
						tw.getExclusiveEnd() + lightWeightOptimisationData.getCargoMinTravelTimes()[i][0]);
			}
		}
		
		return cargoWindows;
	}

	private double[][] processCharterCosts(ILightWeightOptimisationData lightWeightOptimisationData) {
		double[][] cargoDailyCharterCostPerAvailabilityProcessed = new double[lightWeightOptimisationData
          .getCargoCharterCostPerAvailability().length][lightWeightOptimisationData.getCargoCharterCostPerAvailability()[0].length];
		for (int i = 0; i < lightWeightOptimisationData.getCargoCharterCostPerAvailability().length; i++) {
			long[] cargoAvailabilitiesCost = lightWeightOptimisationData.getCargoCharterCostPerAvailability()[i];
			for (int j = 0; j < cargoAvailabilitiesCost.length; j++) {
				long availabilityCost = cargoAvailabilitiesCost[j];
				cargoDailyCharterCostPerAvailabilityProcessed[i][j] = ((double) availabilityCost) / (double) Calculator.ScaleFactor;
			}
		}
		return cargoDailyCharterCostPerAvailabilityProcessed;
	}

	public double[][][] getCargoToCargoCostsProcessed() {
		return cargoToCargoCostsProcessed;
	}

	public List<List<Integer>> getCargoVesselRestrictionAsList() {
		return cargoVesselRestrictionAsList;
	}

	public double[] getCapacity() {
		return capacity;
	}

	public double[] getCargoPNLasDouble() {
		return cargoPNLasDouble;
	}

	public LightWeightCargoDetails[] getCargoDetails() {
		return cargoDetails;
	}

	public double[][] getCargoDailyCharterCostPerAvailabilityProcessed() {
		return cargoDailyCharterCostPerAvailabilityProcessed;
	}

	public void setCargoDailyCharterCostPerAvailabilityProcessed(double[][] cargoDailyCharterCostPerAvailabilityProcessed) {
		this.cargoDailyCharterCostPerAvailabilityProcessed = cargoDailyCharterCostPerAvailabilityProcessed;
	}

	public double[] getCargoesVolumesProcessed() {
		return cargoesVolumesProcessed;
	}

	public CargoWindowData[] getCargoWindows() {
		return cargoWindows;
	}
}
