/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.name.Named;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.LightWeightSchedulerStage2Module;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper.ShippingType;
import com.mmxlabs.models.lng.transformer.optimiser.pairing.IPairingMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.pairing.PairingOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.pairing.PairingOptimiserHelper;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorStep;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossExtractor;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class LightWeightOptimisationDataFactory {

	@Inject
	private PairingOptimisationData optimiserRecorder;
	@Inject
	private ILongTermSlotsProvider longTermSlotsProvider;
	@Inject
	private IPairingMatrixOptimiser matrixOptimiser;
	@Inject
	private IVesselProvider vesselProvider;
	@Inject
	private ICargoToCargoCostCalculator cargoToCargoCostCalculator;
	@Inject
	private ICargoVesselRestrictionsMatrixProducer cargoVesselRestrictionsMatrixProducer;
	@Inject
	private @Named(LightWeightSchedulerStage2Module.LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT) int[] desiredVesselCargoCount;
	@Inject
	private @Named(LightWeightSchedulerStage2Module.LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT) long[] desiredVesselCargoWeight;
	@Inject
	private @Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialSequences;

	public ILightWeightOptimisationData createLightWeightOptimisationData(IVesselAvailability pnlVessel, LoadDischargePairValueCalculatorStep calculator, CleanableExecutorService executorService,
			IProgressMonitor monitor) {
		// get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
		boolean[][] pairingsMatrix = createSlotPairingMatrix(pnlVessel, calculator, executorService, monitor);

		if (pairingsMatrix == null) {
			return null;
		}

		// Export the pairings matrix to a Map
		Map<ILoadOption, IDischargeOption> pairingsMap = new LinkedHashMap<>();
		for (ILoadOption load : optimiserRecorder.getSortedLoads()) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (LightWeightSchedulerStage2Module.DEBUG) {
			printPairings(pairingsMap);
		}


		// Create data for optimiser
		
		// add cargoes
		Set<Integer> cargoIndexes = new HashSet<>();
		List<List<IPortSlot>> shippedCargoes = PairingOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix, ShippingType.SHIPPED);
		if (shippedCargoes.isEmpty()) {
			// No cargoes found!
			return null;
		}
		
		for (int i = 0; i < shippedCargoes.size(); i++) {
			cargoIndexes.add(i);
		}
		
		// add events
		Set<Integer> eventIndexes = new HashSet<>();
		longTermSlotsProvider.getLongTermEvents().forEach(e -> {
			shippedCargoes.add(CollectionsUtil.makeLinkedList(e));
			eventIndexes.add(shippedCargoes.size() - 1);
		});

		// add vessels
		List<@NonNull IVesselAvailability> vessels = getVessels();

		// calculate shipping costs between two cargoes
		long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);

		// set vessel restrictions
		ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels));

		// Cargo Detail
		LightWeightCargoDetails[] cargoDetails = getCargoDetails(shippedCargoes, vessels);

		// Cargo PNL
		long[] cargoPNL = LightWeightOptimiserHelper.getCargoPNL(optimiserRecorder.getProfit(), shippedCargoes, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pnlVessel, cargoDetails);

		// Min Travel Time
		int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);

		// Time window
		int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);

		// Charter costs
		long[][] cargoCharterCostPerAvailability = cargoToCargoCostCalculator.getCargoCharterCostPerAvailability(shippedCargoes, vessels);

		// Vessel Capacity
		long[] capacity = getVesselCapacities(vessels);

		// Cargo Volume
		long[] cargoesVolumes = getCargoVolumes(shippedCargoes);
		
		// Vessel windows
		ITimeWindow[] vesselStartWindows = vessels.stream().map(v->v.getStartRequirement().getTimeWindow()).toArray(size -> new ITimeWindow[size]);
		ITimeWindow[] vesselEndWindows = vessels.stream().map(v->v.getEndRequirement().getTimeWindow()).toArray(size -> new ITimeWindow[size]);

		// Cargo windows
		int[] cargoStartSlotDurations = cargoToCargoCostCalculator.getCargoStartSlotDurations(shippedCargoes);
		int[] cargoEndSlotDurations = cargoToCargoCostCalculator.getCargoEndSlotDurations(shippedCargoes);
		CargoWindowData[] cargoWindows = calculateCargoWindows(shippedCargoes, minCargoStartToEndSlotTravelTimesPerVessel, cargoIndexes);

		LightWeightOptimisationData lightWeightOptimisationData = new LightWeightOptimisationData(shippedCargoes, vessels, capacity, cargoPNL, cargoToCargoCostsOnAvailability, cargoVesselRestrictions,
				minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel, pairingsMap, desiredVesselCargoCount, desiredVesselCargoWeight, cargoesVolumes, cargoDetails,
				cargoCharterCostPerAvailability, cargoIndexes, eventIndexes, vesselStartWindows, vesselEndWindows, cargoStartSlotDurations, cargoEndSlotDurations, cargoWindows);

		return lightWeightOptimisationData;
	}

	private long[] getVesselCapacities(List<@NonNull IVesselAvailability> vessels) {
		long[] capacity = vessels.stream().mapToLong(v -> v.getVessel().getCargoCapacity()).toArray();
		return capacity;
	}

	private long[] getCargoVolumes(List<List<IPortSlot>> shippedCargoes) {
		long[] cargoesVolumes = shippedCargoes.stream().mapToLong(x -> {
			if (!(x.get(0) instanceof ILoadOption)) {
				return 1;
			}
			long loadVolume = ((ILoadOption) x.get(0)).getMaxLoadVolume();
			long dischargeVolume = ((IDischargeOption) x.get(1)).getMaxDischargeVolume(23);
			return Math.min(loadVolume, dischargeVolume);
		}).toArray();
		return cargoesVolumes;
	}

	private LightWeightCargoDetails[] getCargoDetails(List<List<IPortSlot>> shippedCargoes, List<@NonNull IVesselAvailability> vessels) {
		LightWeightCargoDetails[] cargoDetails = shippedCargoes.stream().map(x -> {
			return new LightWeightCargoDetails(x.get(0).getPortType());
		}).toArray(LightWeightCargoDetails[]::new);
		return cargoDetails;
	}

	private List<@NonNull IVesselAvailability> getVessels() {
		List<@NonNull IVesselAvailability> vessels = initialSequences.getResources().stream() //
				.sorted((a, b) -> a.getName().compareTo(b.getName())) //
				.map(v -> vesselProvider.getVesselAvailability(v)) //
				.filter(v -> PairingOptimiserHelper.isShippedVessel(v)).collect(Collectors.toList());
		return vessels;
	}

	/**
	 * Get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
	 * ALEX_TODO: move this to LongTerm package
	 * @param pnlVessel
	 * @param calculator
	 * @param executorService
	 * @param monitor
	 * @return
	 */
	public boolean[][] createSlotPairingMatrix(IVesselAvailability pnlVessel, LoadDischargePairValueCalculatorStep calculator, CleanableExecutorService executorService, IProgressMonitor monitor) {
		// (1) Identify LT slots
		@NonNull
		Collection<IPortSlot> longTermSlots = getLongTermSlots();

		List<ILoadOption> longtermLoads = getLongTermLoads(longTermSlots);
		List<IDischargeOption> longTermDischarges = getLongTermDischarges(longTermSlots);

		optimiserRecorder.init(longtermLoads, longTermDischarges);

		// (2) Generate Slot to Slot bindings matrix for LT slots
		calculator.run(pnlVessel, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), new ProfitAndLossExtractor(optimiserRecorder), executorService, monitor);

		// (3) Optimise matrix
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges(),
				optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCount(), optimiserRecorder.getMinDischargeGroupCount(), optimiserRecorder.getMaxLoadGroupCount(),
				optimiserRecorder.getMinLoadGroupCount());
		return pairingsMatrix;
	}

	private Collection<IPortSlot> getLongTermSlots() {
		@NonNull
		Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();
		return longTermSlots;
	}

	private List<IDischargeOption> getLongTermDischarges(Collection<IPortSlot> longTermSlots) {
		List<IDischargeOption> longTermDischarges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m).collect(Collectors.toCollection(ArrayList::new));
		return longTermDischarges;
	}
	
	private List<ILoadOption> getLongTermLoads(Collection<IPortSlot> longTermSlots) {
		List<ILoadOption> longtermLoads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		return longtermLoads;
	}
	
	private CargoWindowData[] calculateCargoWindows(List<List<IPortSlot>> cargoes, int[][] cargoMinTravelTimes, Set<Integer> cargoIndexes) {
		CargoWindowData[] cargoWindows = new CargoWindowData[cargoes.size()];

		for (int i = 0; i < cargoes.size(); i++) {
			if (cargoIndexes.contains(i)) {
				ITimeWindow loadTW = cargoes.get(i).get(0).getTimeWindow();
				ITimeWindow dischargeTW = cargoes.get(i).get(1).getTimeWindow();
				
				cargoWindows[i] = new CargoWindowData(loadTW.getInclusiveStart(), loadTW.getExclusiveEnd(),
						dischargeTW.getInclusiveStart(), dischargeTW.getExclusiveEnd());
			} else {
				ITimeWindow tw  = cargoes.get(i).get(0).getTimeWindow();
				
				// vessel events are vessel independent for duration so get time from first vessel
				cargoWindows[i] = new CargoWindowData(tw.getInclusiveStart(), tw.getExclusiveEnd(),
						tw.getInclusiveStart() + cargoMinTravelTimes[i][0],
						tw.getExclusiveEnd() + cargoMinTravelTimes[i][0]);
			}
		}
		
		return cargoWindows;
	}


	private void printPairings(Map<ILoadOption, IDischargeOption> pairingsMap) {
		System.out.println("####Pairings####");
		for (Entry<ILoadOption, IDischargeOption> entry : pairingsMap.entrySet()) {
			if (entry.getValue() != null) {
				System.out.println(String.format("%s -> %s", entry.getKey(), entry.getValue()));
			}
		}
	}
}
