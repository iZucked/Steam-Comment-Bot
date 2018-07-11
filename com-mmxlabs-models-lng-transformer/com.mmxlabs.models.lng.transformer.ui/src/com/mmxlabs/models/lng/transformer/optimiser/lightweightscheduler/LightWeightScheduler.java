package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper.ShippingType;
import com.mmxlabs.models.lng.transformer.optimiser.common.SlotValueHelper;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints.LightWeightShippingRestrictionsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions.DefaultPNLLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions.VesselCargoCountLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ILongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.LongTermOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.LongTermOptimiserHelper;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculator;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorStep;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossExtractor;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselSlotCountFitnessProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LightWeightScheduler {

	@Inject
	private LongTermOptimisationData optimiserRecorder;
	@Inject
	private ILongTermSlotsProvider longTermSlotsProvider;
	@Inject
	private ILongTermMatrixOptimiser matrixOptimiser;
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
	@Inject
	private IAllowedVesselProvider allowedVesselProvider;

	public ILightWeightOptimisationData calculateLightWeightOptimisationData(IVesselAvailability pnlVessel, LoadDischargePairValueCalculatorStep calculator, CleanableExecutorService executorService,
			IProgressMonitor monitor) {
		// get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
		boolean[][] pairingsMatrix = getSlotPairingMatrix(pnlVessel, calculator, executorService, monitor);

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

		Set<Integer> cargoIndexes = new HashSet<>();
		Set<Integer> eventIndexes = new HashSet<>();

		// create data for optimiser
		// Cargoes
		List<List<IPortSlot>> shippedCargoes = LongTermOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix, ShippingType.SHIPPED);
		for (int i = 0; i < shippedCargoes.size(); i++) {
			cargoIndexes.add(i);
		}
		// add events
		longTermSlotsProvider.getLongTermEvents().forEach(e -> {
			shippedCargoes.add(CollectionsUtil.makeLinkedList(e));
			eventIndexes.add(shippedCargoes.size() - 1);
		});

		// Now we add vessel events

		// Vessel
		List<@NonNull IVesselAvailability> vessels = initialSequences.getResources().stream() //
				.sorted((a, b) -> a.getName().compareTo(b.getName())) //
				.map(v -> vesselProvider.getVesselAvailability(v)) //
				.filter(v -> LongTermOptimiserHelper.isShippedVessel(v)).collect(Collectors.toList());

		// Cost
		Long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);

		// VesselRestriction
		ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels));

		// Cargo Detail
		LightWeightCargoDetails[] cargoDetails = shippedCargoes.stream().map(x -> {

			// REVIEW: It is alright to only check the first element ?
			// ALEX_NOTE: not sure this is necessary
			PortType portType = x.get(0).getPortType();

			if (portType == PortType.CharterOut || portType == PortType.DryDock) {
				Collection<IVessel> permittedVessels = allowedVesselProvider.getPermittedVessels(x.get(0));
				int index = -1;
				for (int i = 0; i < vessels.size(); i++) {
					if (permittedVessels.contains(vessels.get(i).getVessel())) {
						index = i;
					}
				}
				if (index != -1) {
					return new LightWeightCargoDetails(x.get(0).getPortType(), index);
				} else {
					return new LightWeightCargoDetails(x.get(0).getPortType());
				}
			}
			return new LightWeightCargoDetails(x.get(0).getPortType());
		}).toArray(LightWeightCargoDetails[]::new);

		// Cargo PNL
		long[] cargoPNL = LightWeightOptimiserHelper.getCargoPNL(optimiserRecorder.getProfit(), shippedCargoes, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pnlVessel, cargoDetails);

		// Min Travel Time
		int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);

		// Time window
		int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);

		// Charter costs
		long[][] cargoCharterCostPerAvailability = cargoToCargoCostCalculator.getCargoCharterCostPerAvailability(shippedCargoes, vessels);

		// Vessel Capacity
		double[] capacity = vessels.stream().mapToDouble(v -> v.getVessel().getCargoCapacity() / 1000).toArray();

		// Cargo Volume
		long[] cargoesVolumes = shippedCargoes.stream().mapToLong(x -> {
			if (!(x.get(0) instanceof ILoadOption)) {
				// volume of 1
				return 1;
			}
			long loadVolume = ((ILoadOption) x.get(0)).getMaxLoadVolume();
			long dischargeVolume = ((IDischargeOption) x.get(1)).getMaxDischargeVolume(23);
			return Math.min(loadVolume, dischargeVolume);
		}).toArray();

		LightWeightOptimisationData lightWeightOptimisationData = new LightWeightOptimisationData(shippedCargoes, vessels, capacity, cargoPNL, cargoToCargoCostsOnAvailability, cargoVesselRestrictions,
				minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel, pairingsMap, desiredVesselCargoCount, desiredVesselCargoWeight, cargoesVolumes, cargoDetails,
				cargoCharterCostPerAvailability, cargoIndexes, eventIndexes);

		return lightWeightOptimisationData;
	}

	/**
	 * Get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
	 * @param pnlVessel
	 * @param calculator
	 * @param executorService
	 * @param monitor
	 * @return
	 */
	public boolean[][] getSlotPairingMatrix(IVesselAvailability pnlVessel, LoadDischargePairValueCalculatorStep calculator, CleanableExecutorService executorService, IProgressMonitor monitor) {
		// (1) Identify LT slots
		@NonNull
		Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();

		List<ILoadOption> longtermLoads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		List<IDischargeOption> longTermDischarges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m).collect(Collectors.toCollection(ArrayList::new));

		optimiserRecorder.init(longtermLoads, longTermDischarges);

		// (2) Generate Slot to Slot bindings matrix for LT slots
		calculator.run(pnlVessel, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), new ProfitAndLossExtractor(optimiserRecorder), executorService, monitor);

		// (3) Optimise matrix
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges(),
				optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCount(), optimiserRecorder.getMinDischargeGroupCount(), optimiserRecorder.getMaxLoadGroupCount(),
				optimiserRecorder.getMinLoadGroupCount());
		return pairingsMatrix;
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
