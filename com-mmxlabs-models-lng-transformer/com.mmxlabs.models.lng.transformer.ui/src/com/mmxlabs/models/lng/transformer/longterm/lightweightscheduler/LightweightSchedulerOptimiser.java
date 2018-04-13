/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.minimaxlabs.rnd.representation.LightWeightOutputData;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintChecker;
import com.mmxlabs.models.lng.transformer.longterm.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.longterm.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.longterm.ILongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimisationData;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiserHelper;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiserHelper.ShippingType;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Light weight scheduling optimisation.
 * 
 * @author achurchill
 *
 */
public class LightweightSchedulerOptimiser {

	@interface NonLDD {
		
	}
	
	private enum CargoType {
		SHIPPED, NON_SHIPPED, ALL
	}
	
	@Inject
	private ILongTermSlotsProvider longTermSlotsProvider;

	@Inject
	private ILongTermMatrixOptimiser matrixOptimiser;

	@Inject
	private LongTermOptimisationData optimiserRecorder;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private ICargoToCargoCostCalculator cargoToCargoCostCalculator;

	@Inject
	private ICargoVesselRestrictionsMatrixProducer cargoVesselRestrictionsMatrixProducer;
	
	@Inject
	private ILightWeightSequenceOptimiser lightWeightSequenceOptimiser;

	private List<IPairwiseConstraintChecker> constraintCheckers = new LinkedList<>();

	private static Set<VesselInstanceType> ALLOWED_VESSEL_TYPES = Sets.newHashSet(VesselInstanceType.FLEET, VesselInstanceType.SPOT_CHARTER, VesselInstanceType.TIME_CHARTER);

	/**
	 * Perform a long term (nominal) trading optimisation
	 * 
	 * @param executorService
	 * @param dataTransformer
	 * @param charterInMarket
	 * @return
	 */
	public Pair<ISequences, Long> optimise(final LNGDataTransformer dataTransformer, CharterInMarket charterInMarket) {
		// (1) Identify LT slots
		@NonNull
		Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();
		List<ILoadOption> loads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		List<IDischargeOption> discharges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m).collect(Collectors.toCollection(ArrayList::new));
		optimiserRecorder.init(loads, discharges);
		
		// (2) Generate S2S bindings matrix for LT slots
		ExecutorService es = Executors.newSingleThreadExecutor();
		LongTermOptimiserHelper.getS2SBindings(loads, discharges, charterInMarket, es, dataTransformer, optimiserRecorder);
		
		// now using our profits recorder we have a full matrix of constraints and pnl
		Long[][] profit = optimiserRecorder.getProfit();

		// (3) Optimise matrix
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges(), optimiserRecorder.getValid());
		
		if (pairingsMatrix == null) {
			return null;
		}
		// (4) Export the pairings matrix to a Map
		Map<ILoadOption, IDischargeOption> pairingsMap = new HashMap<>();
		for (ILoadOption load : loads) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		// create data for optimiser
		List<List<IPortSlot>> shippedCargoes = getCargoes(loads, discharges, pairingsMatrix, CargoType.SHIPPED);
		List<@NonNull IVesselAvailability> vessels = vesselProvider.getSortedResources().stream().map(v -> vesselProvider.getVesselAvailability(v)).filter(v -> isShippedVessel(v)).collect(Collectors.toList());
		
		@NonNull
		IVesselAvailability pnlVessel = getPNLVessel(dataTransformer, charterInMarket);
		Long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);
		ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels, getResourceAllocationConstraintChecker(this.constraintCheckers), getPortExclusionConstraintChecker(this.constraintCheckers)));
		long[] cargoPNL = getCargoPNL(profit, shippedCargoes, loads, discharges, pnlVessel);
		int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);
		int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);
		
		List<List<Integer>> sequences = lightWeightSequenceOptimiser.optimise(shippedCargoes, vessels, cargoPNL, cargoToCargoCostsOnAvailability, cargoVesselRestrictions, minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel);
//		List<List<Integer>> sequences = null;
//		try {
//			sequences = getStoredSequences("/tmp/gurobiOutput.gb");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// (5) Export the pairings matrix to the raw sequences
		ModifiableSequences rawSequences = new ModifiableSequences(dataTransformer.getInitialSequences());
		// update shipped
		updateSequences(rawSequences, sequences, shippedCargoes, vessels, pairingsMap, vesselProvider.getResource(pnlVessel));
		// update non-shipped
		LongTermOptimiserHelper.updateVirtualSequences(rawSequences, pairingsMap, vesselProvider.getResource(pnlVessel), portSlotProvider, virtualVesselSlotProvider, vesselProvider, ShippingType.NON_SHIPPED);
		return new Pair<>(rawSequences, 0L);
	}

	@NonLDD
	private long[] getCargoPNL(Long[][] profit, List<List<IPortSlot>> cargoes, List<ILoadOption> loads, List<IDischargeOption> discharges, @NonNull IVesselAvailability pnlVessel) {
		long[] pnl = new long[cargoes.size()];
		int idx = 0;
		for (List<IPortSlot> cargo : cargoes) {
			pnl[idx++] = profit[loads.indexOf(cargo.get(0))][discharges.indexOf(cargo.get(cargo.size() - 1))]/pnlVessel.getVessel().getCargoCapacity();
		}
		return pnl;
	}

	@NonLDD
	private List<List<IPortSlot>> getCargoes(List<ILoadOption> loads, List<IDischargeOption> discharges, boolean[][] pairingsMatrix, CargoType cargoFilter) {
		List<List<IPortSlot>> cargoes = new LinkedList<>();
		for (int loadId = 0; loadId < pairingsMatrix.length; loadId++) {
			for (int dischargeId = 0; dischargeId < pairingsMatrix[loadId].length; dischargeId++) {
				if (cargoFilter == CargoType.SHIPPED || cargoFilter == CargoType.NON_SHIPPED) {
					// FOB/DES check if filter is on
					boolean shipped = loads.get(loadId) instanceof ILoadSlot && discharges.get(dischargeId) instanceof IDischargeSlot;
					boolean expression = cargoFilter == CargoType.SHIPPED ? !shipped : shipped;
					if (expression) {
						continue;
					}
				} 
				if (pairingsMatrix[loadId][dischargeId]) {
					cargoes.add(Lists.newArrayList(loads.get(loadId), discharges.get(dischargeId)));
				}
			}
		}
		return cargoes;
	}

	@Inject
	private void injectConstraintChecker(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences, final List<IConstraintChecker> injectedConstraintCheckers) {
		this.constraintCheckers = new LinkedList<>();
		for (final IConstraintChecker checker : injectedConstraintCheckers) {
			if (checker instanceof IPairwiseConstraintChecker) {
				final IPairwiseConstraintChecker constraintChecker = (IPairwiseConstraintChecker) checker;
				constraintCheckers.add(constraintChecker);

				// Prep with initial sequences.
				constraintChecker.checkConstraints(initialRawSequences, null);
			}
		}
	}

	/**
	 * Updates the raw sequences given an allocations matrix
	 * 
	 * @param rawSequences
	 * @param sequences 
	 * @param cargoes 
	 * @param vessels
	 * @param pairingsMap
	 * @param nominal
	 */
	@NonLDD
	private void updateSequences(@NonNull IModifiableSequences rawSequences, List<List<Integer>> sequences, List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, @NonNull Map<ILoadOption, IDischargeOption> pairingsMap, @NonNull IResource nominal) {
		LongTermOptimiserHelper.moveElementsToUnusedList(rawSequences, portSlotProvider);
		for (int vesselIndex = 0; vesselIndex < vessels.size(); vesselIndex++) {
			IVesselAvailability vesselAvailability = vessels.get(vesselIndex);
			IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(vesselProvider.getResource(vesselAvailability));
			int insertIndex = 0;
			for (int i = 0; i < modifiableSequence.size(); i++) {
				if (portSlotProvider.getPortSlot(modifiableSequence.get(i)) != null && portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.End) {
					break;
				}
				insertIndex++;
			}
			List<ISequenceElement> unusedElements = rawSequences.getModifiableUnusedElements();
			List<Integer> cargoIndexes = sequences.get(vesselIndex);
			for (int cargoIndex : cargoIndexes) {
				List<IPortSlot> cargo = cargoes.get(cargoIndex);
				ILoadOption loadOption = (ILoadOption) cargo.get(0);
				IDischargeOption dischargeOption = (IDischargeOption) cargo.get(1);
				// skip if unallocated
				if (dischargeOption == null)
					continue;
				if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
					// FOB-DES
					modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(loadOption));
					modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(dischargeOption));
					unusedElements.remove(portSlotProvider.getElement(loadOption));
					unusedElements.remove(portSlotProvider.getElement(dischargeOption));
				} else if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeOption) {
					// Fob Sale
					IResource resource = LongTermOptimiserHelper.getVirtualResource(dischargeOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence fobSale = rawSequences.getModifiableSequence(resource);
						LongTermOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, fobSale, portSlotProvider);
					}
				} else if (loadOption instanceof ILoadOption && dischargeOption instanceof IDischargeSlot) {
					// DES purchase
					IResource resource = LongTermOptimiserHelper.getVirtualResource(loadOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence desPurchase = rawSequences.getModifiableSequence(resource);
						LongTermOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, desPurchase, portSlotProvider);
					}
				}
			}
		}
	}

	private IVesselAvailability getPNLVessel(final LNGDataTransformer dataTransformer, CharterInMarket charterInMarket) {
		IVesselAvailability nominalMarketAvailability = null;
		final ISpotCharterInMarket o_nominalMarket = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);
		for (final IResource resource : vesselProvider.getSortedResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getSpotCharterInMarket() == o_nominalMarket && vesselAvailability.getSpotIndex() == -1) {
				nominalMarketAvailability = vesselAvailability;
				break;
			}
		}
		assert nominalMarketAvailability != null;
		return nominalMarketAvailability;
	}

	private boolean isShippedVessel(@NonNull IVesselAvailability v) {
		return ALLOWED_VESSEL_TYPES.contains(v.getVesselInstanceType());
	}

	private ResourceAllocationConstraintChecker getResourceAllocationConstraintChecker(List<IPairwiseConstraintChecker> constraintCheckers) {
		return constraintCheckers.parallelStream().filter(c -> (c instanceof ResourceAllocationConstraintChecker)).map(c -> (ResourceAllocationConstraintChecker) c).findFirst().get();
	}

	private PortExclusionConstraintChecker getPortExclusionConstraintChecker(List<IPairwiseConstraintChecker> constraintCheckers) {
		return constraintCheckers.parallelStream().filter(c -> (c instanceof PortExclusionConstraintChecker)).map(c -> (PortExclusionConstraintChecker) c).findFirst().get();
	}

	private static List<List<Integer>> getStoredSequences(String path) throws IOException {
		ObjectInputStream objectinputstream = null;
		LightWeightOutputData readCase = null;
		try {
			FileInputStream streamIn = new FileInputStream(path);
			objectinputstream = new ObjectInputStream(streamIn);
			readCase = (LightWeightOutputData) objectinputstream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectinputstream != null) {
				objectinputstream.close();
			}
		}
		return readCase.sequences;
	}

}
