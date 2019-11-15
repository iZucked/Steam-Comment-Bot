/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
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
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

public class LightWeightOptimisationDataFactory {

	@Inject
	private PairingOptimisationData optimiserRecorder;
	@Inject
	private ILongTermSlotsProvider longTermSlotsProvider;
	@Inject
	private IPairingMatrixOptimiser matrixOptimiser;
	@Inject
	private IPortSlotProvider portSlotProvider;
	@Inject
	private IVesselProvider vesselProvider;
	@Inject
	private IVirtualVesselSlotProvider virtualVesselProvider;
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
	private Injector injector;

	public ILightWeightOptimisationData createLightWeightOptimisationData(final IVesselAvailability pnlVessel, final LoadDischargePairValueCalculatorStep calculator,
			final CleanableExecutorService executorService, final IProgressMonitor monitor) {
		// get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
		final boolean[][] pairingsMatrix = createSlotPairingMatrix(pnlVessel, calculator, executorService, monitor);

		if (pairingsMatrix == null) {
			return null;
		}

		// Export the pairings matrix to a Map
		final Map<ILoadOption, IDischargeOption> pairingsMap = new LinkedHashMap<>();
		for (final ILoadOption load : optimiserRecorder.getSortedLoads()) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (LightWeightSchedulerStage2Module.DEBUG) {
			printPairings(pairingsMap);
		}

		// Create data for optimiser

		// add cargoes
		final Set<Integer> cargoIndexes = new HashSet<>();
		final List<List<IPortSlot>> shippedCargoes = PairingOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix,
				ShippingType.SHIPPED);
		final List<List<IPortSlot>> nonshippedCargoes = PairingOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix,
				ShippingType.NON_SHIPPED);
		if (shippedCargoes.isEmpty() && nonshippedCargoes.isEmpty()) {
			// No cargoes found!
			return null;
		}

		for (int i = 0; i < shippedCargoes.size(); i++) {
			cargoIndexes.add(i);
		}

		// add events
		final Set<Integer> eventIndexes = new HashSet<>();
		longTermSlotsProvider.getLongTermEvents().forEach(e -> {
			shippedCargoes.add(e);
			eventIndexes.add(shippedCargoes.size() - 1);
		});

		// add vessels
		final List<@NonNull IVesselAvailability> vessels = getVessels();

		// calculate shipping costs between two cargoes
		final long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);

		// set vessel restrictions
		final ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels));

		// Cargo Detail
		final LightWeightCargoDetails[] cargoDetails = getCargoDetails(shippedCargoes, vessels);

		// Cargo PNL
		final long[] cargoPNL = LightWeightOptimiserHelper.getCargoPNL(optimiserRecorder.getProfit(), shippedCargoes, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(),
				pnlVessel, cargoDetails);

		// Min Travel Time
		final int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);

		// Time window
		final int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);

		// Charter costs
		final long[][] cargoCharterCostPerAvailability = cargoToCargoCostCalculator.getCargoCharterCostPerAvailability(shippedCargoes, vessels);

		// Vessel Capacity
		final long[] capacity = getVesselCapacities(vessels);

		// Cargo Volume
		final long[] cargoesVolumes = getCargoVolumes(shippedCargoes);

		// Vessel windows
		final ITimeWindow[] vesselStartWindows = vessels.stream().map(v -> v.getStartRequirement().getTimeWindow()).toArray(size -> new ITimeWindow[size]);
		final ITimeWindow[] vesselEndWindows = vessels.stream().map(v -> v.getEndRequirement().getTimeWindow()).toArray(size -> new ITimeWindow[size]);

		// Cargo windows
		final int[] cargoStartSlotDurations = cargoToCargoCostCalculator.getCargoStartSlotDurations(shippedCargoes);
		final int[] cargoEndSlotDurations = cargoToCargoCostCalculator.getCargoEndSlotDurations(shippedCargoes);
		final CargoWindowData[] cargoWindows = calculateCargoWindows(shippedCargoes, minCargoStartToEndSlotTravelTimesPerVessel, cargoIndexes);

		final LightWeightOptimisationData lightWeightOptimisationData = new LightWeightOptimisationData(shippedCargoes, nonshippedCargoes, vessels, capacity, cargoPNL, cargoToCargoCostsOnAvailability,
				cargoVesselRestrictions, minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel, pairingsMap, desiredVesselCargoCount, desiredVesselCargoWeight,
				cargoesVolumes, cargoDetails, cargoCharterCostPerAvailability, cargoIndexes, eventIndexes, vesselStartWindows, vesselEndWindows, cargoStartSlotDurations, cargoEndSlotDurations,
				cargoWindows);

		return lightWeightOptimisationData;
	}

	public ISequences createNominalADP(final IVesselAvailability pnlVessel, final LoadDischargePairValueCalculatorStep calculator, final CleanableExecutorService executorService,
			final IProgressMonitor monitor) {
		// get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
		final boolean[][] pairingsMatrix = createSlotPairingMatrix(pnlVessel, calculator, executorService, monitor);

		if (pairingsMatrix == null) {
			return null;
		}

		// Export the pairings matrix to a Map
		final Map<ILoadOption, IDischargeOption> pairingsMap = new LinkedHashMap<>();
		for (final ILoadOption load : optimiserRecorder.getSortedLoads()) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (LightWeightSchedulerStage2Module.DEBUG) {
			printPairings(pairingsMap);
		}

		// Create data for optimiser

		// add cargoes
		final List<List<IPortSlot>> shippedCargoes = PairingOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix, ShippingType.ALL);
		if (shippedCargoes.isEmpty()) {
			// No cargoes found!
			return null;
		}

		final Collection<IResource> resources = new LinkedHashSet<>();
		final IResource o_resource = vesselProvider.getResource(pnlVessel);
		resources.add(o_resource);

		// Gather FOB/DES resources
		for (final List<IPortSlot> cargo : shippedCargoes) {
			for (final IPortSlot e : cargo) {
				final IVesselAvailability va = virtualVesselProvider.getVesselAvailabilityForElement(portSlotProvider.getElement(e));
				if (va != null) {
					resources.add(vesselProvider.getResource(va));
				}
			}
		}

		final IModifiableSequences sequences = new ModifiableSequences(new ArrayList<>(resources));
		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));
		Set<ISequenceElement> usedElements = new HashSet<>();

		for (final List<IPortSlot> cargo : shippedCargoes) {
			// Grab FOB/DES vessel
			IVesselAvailability va = null;
			for (final IPortSlot e : cargo) {
				va = virtualVesselProvider.getVesselAvailabilityForElement(portSlotProvider.getElement(e));
				if (va != null) {
					break;
				}
			}

			IModifiableSequence thisModifiableSequence = modifiableSequence;
			IResource thisResource = null;

			if (va != null) {
				thisResource = vesselProvider.getResource(va);
				thisModifiableSequence = sequences.getModifiableSequence(thisResource);

				thisModifiableSequence.add(startEndRequirementProvider.getStartElement(thisResource));
			}

			for (final IPortSlot e : cargo) {
				thisModifiableSequence.add(portSlotProvider.getElement(e));
				usedElements.add(portSlotProvider.getElement(e));
			}

			if (va != null) {
				thisResource = vesselProvider.getResource(va);
				thisModifiableSequence.add(startEndRequirementProvider.getEndElement(thisResource));
			}
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		List<@NonNull ISequenceElement> unusedElements = sequences.getModifiableUnusedElements();
		optimiserRecorder.getSortedLoads().stream() //
				.map(portSlotProvider::getElement) //
				.filter(e -> !usedElements.contains(e)) //
				.forEach(unusedElements::add);

		optimiserRecorder.getSortedDischarges().stream() //
				.map(portSlotProvider::getElement) //
				.filter(e -> !usedElements.contains(e)) //
				.forEach(unusedElements::add);

		// TODO: Vessel Events?

		return sequences;

	}

	private long[] getVesselCapacities(final List<@NonNull IVesselAvailability> vessels) {
		final long[] capacity = vessels.stream().mapToLong(v -> v.getVessel().getCargoCapacity()).toArray();
		return capacity;
	}

	private long[] getCargoVolumes(final List<List<IPortSlot>> shippedCargoes) {
		final long[] cargoesVolumes = shippedCargoes.stream().mapToLong(x -> {
			if (!(x.get(0) instanceof ILoadOption)) {
				return 1;
			}
			final long loadVolume = ((ILoadOption) x.get(0)).getMaxLoadVolume();
			final long dischargeVolume = ((IDischargeOption) x.get(1)).getMaxDischargeVolume(23);
			return Math.min(loadVolume, dischargeVolume);
		}).toArray();
		return cargoesVolumes;
	}

	private LightWeightCargoDetails[] getCargoDetails(final List<List<IPortSlot>> shippedCargoes, final List<@NonNull IVesselAvailability> vessels) {
		final LightWeightCargoDetails[] cargoDetails = shippedCargoes.stream().map(x -> {
			return new LightWeightCargoDetails(x.get(0).getPortType());
		}).toArray(LightWeightCargoDetails[]::new);
		return cargoDetails;
	}

	private List<@NonNull IVesselAvailability> getVessels() {
		final List<@NonNull IVesselAvailability> vessels = initialSequences.getResources().stream() //
				.sorted((a, b) -> a.getName().compareTo(b.getName())) //
				.map(v -> vesselProvider.getVesselAvailability(v)) //
				.filter(v -> PairingOptimiserHelper.isShippedVessel(v)).collect(Collectors.toList());
		return vessels;
	}

	private List<@NonNull IVesselAvailability> getAllVessels() {
		final List<@NonNull IVesselAvailability> vessels = initialSequences.getResources().stream() //
				.map(v -> vesselProvider.getVesselAvailability(v)) //
				.collect(Collectors.toList());
		return vessels;
	}

	/**
	 * Get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1) ALEX_TODO: move this to LongTerm package
	 * 
	 * @param pnlVessel
	 * @param calculator
	 * @param executorService
	 * @param monitor
	 * @return
	 */
	public boolean[][] createSlotPairingMatrix(final IVesselAvailability pnlVessel, final LoadDischargePairValueCalculatorStep calculator, final CleanableExecutorService executorService,
			final IProgressMonitor monitor) {
		// (1) Identify LT slots
		@NonNull
		final Collection<IPortSlot> longTermSlots = getLongTermSlots();

		final List<ILoadOption> longtermLoads = getLongTermLoads(longTermSlots);
		final List<IDischargeOption> longTermDischarges = getLongTermDischarges(longTermSlots);
		optimiserRecorder.init(longtermLoads, longTermDischarges);

		// (2) Generate Slot to Slot bindings matrix for LT slots
		calculator.run(pnlVessel, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), new ProfitAndLossExtractor(optimiserRecorder), executorService, monitor,
				getAllVessels());

		// (3) Optimise matrix
		final boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(),
				optimiserRecorder.getOptionalDischarges(), optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCount(), optimiserRecorder.getMinDischargeGroupCount(),
				optimiserRecorder.getMaxLoadGroupCount(), optimiserRecorder.getMinLoadGroupCount());
		return pairingsMatrix;
	}

	private Collection<IPortSlot> getLongTermSlots() {
		@NonNull
		final Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();
		return longTermSlots;
	}

	private List<IDischargeOption> getLongTermDischarges(final Collection<IPortSlot> longTermSlots) {
		final List<IDischargeOption> longTermDischarges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m)
				.collect(Collectors.toCollection(ArrayList::new));
		return longTermDischarges;
	}

	private List<ILoadOption> getLongTermLoads(final Collection<IPortSlot> longTermSlots) {
		final List<ILoadOption> longtermLoads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		return longtermLoads;
	}

	private CargoWindowData[] calculateCargoWindows(final List<List<IPortSlot>> cargoes, final int[][] cargoMinTravelTimes, final Set<Integer> cargoIndexes) {
		final CargoWindowData[] cargoWindows = new CargoWindowData[cargoes.size()];

		for (int i = 0; i < cargoes.size(); i++) {
			if (cargoIndexes.contains(i)) {
				final ITimeWindow loadTW = cargoes.get(i).get(0).getTimeWindow();
				final ITimeWindow dischargeTW = cargoes.get(i).get(1).getTimeWindow();

				cargoWindows[i] = new CargoWindowData(loadTW.getInclusiveStart(), loadTW.getExclusiveEnd(), dischargeTW.getInclusiveStart(), dischargeTW.getExclusiveEnd());
			} else {
				final ITimeWindow tw = cargoes.get(i).get(0).getTimeWindow();

				// vessel events are vessel independent for duration so get time from first vessel
				cargoWindows[i] = new CargoWindowData(tw.getInclusiveStart(), tw.getExclusiveEnd(), tw.getInclusiveStart() + cargoMinTravelTimes[i][0],
						tw.getExclusiveEnd() + cargoMinTravelTimes[i][0]);
			}
		}

		return cargoWindows;
	}

	private void printSlotsToFile(final List<? extends IPortSlot> loads, final List<? extends IPortSlot> discharges) {
		final LocalDateTime date = LocalDateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/slots-%s-%s-%s.txt", date.getHour(), date.getMinute(), date.getSecond()), "UTF-8");
			loads.forEach(l -> writer.println(l.getId()));
			discharges.forEach(d -> writer.println(d.getId()));
			writer.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printPairingsToFile(final List<ILoadOption> sortedLoads, final Map<ILoadOption, IDischargeOption> pairingsMap) {
		final LocalDateTime date = LocalDateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/pairings-%s-%s-%s.txt", date.getHour(), date.getMinute(), date.getSecond()), "UTF-8");
			for (final ILoadOption load : sortedLoads) {
				if (pairingsMap.get(load) != null) {
					writer.println(String.format("%s -> %s", load.getId(), pairingsMap.get(load)));
				}
			}
			writer.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printPairings(final Map<ILoadOption, IDischargeOption> pairingsMap) {
		System.out.println("####Pairings####");
		for (final Entry<ILoadOption, IDischargeOption> entry : pairingsMap.entrySet()) {
			if (entry.getValue() != null) {
				System.out.println(String.format("%s -> %s", entry.getKey(), entry.getValue()));
			}
		}
	}

	private void printShippedCargoesToFile(final List<List<IPortSlot>> shippedCargoes) {
		final LocalDateTime date = LocalDateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/shipped-cargoes-%s-%s-%s.txt", date.getHour(), date.getMinute(), date.getSecond()), "UTF-8");
			for (final List<IPortSlot> cargoe : shippedCargoes) {
				writer.println(cargoe.stream().map(s -> s.getId()).collect(Collectors.joining("-")));
			}
			writer.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
