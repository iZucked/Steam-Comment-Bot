/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.longterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Long term optimisation. Drives a long term optimisation using an {@link ILongTermMatrixOptimiser}.
 * 
 * @author achurchill
 *
 */
public class LongTermOptimiser {

	private static final boolean DEBUG = false;

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

	/**
	 * Perform a long term (nominal) trading optimisation
	 * 
	 * @param executorService
	 * @param dataTransformer
	 * @param charterInMarket
	 * @return
	 */
	public Pair<ISequences, Long> optimise(final ExecutorService executorService, final LNGDataTransformer dataTransformer, CharterInMarket charterInMarket) {
		// (1) Identify LT slots
		@NonNull
		Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();
		List<ILoadOption> loads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		List<IDischargeOption> discharges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m).collect(Collectors.toCollection(ArrayList::new));
		optimiserRecorder.init(loads, discharges);

		if (true) {
			throw new UnsupportedOperationException("Long term optimiser not supported");
		}
		// (2) Generate S2S bindings matrix for LT slots
		ExecutorService es = Executors.newSingleThreadExecutor();
		// LongTermOptimiserHelper.getS2SBindings(loads, discharges, charterInMarket, es, dataTransformer, optimiserRecorder);
		// now using our profits recorder we have a full matrix of constraints
		// and pnl
		Long[][] profit = optimiserRecorder.getProfit();

		// (3) Optimise matrix
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges(),
				optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCount(), optimiserRecorder.getMinDischargeGroupCount(), optimiserRecorder.getMaxLoadGroupCount(),
				optimiserRecorder.getMinLoadGroupCount());
		if (pairingsMatrix == null) {
			return null;
		}

		// (4) Export the pairings matrix to a Map
		Map<ILoadOption, IDischargeOption> pairingsMap = new HashMap<>();
		for (ILoadOption load : loads) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (DEBUG) {
			// print pairings for debug
			LongTermOptimiserHelper.printPairings(loads, pairingsMatrix, optimiserRecorder);
		}

		// (5) Export the pairings matrix to the raw sequences
		ModifiableSequences rawSequences = new ModifiableSequences(dataTransformer.getInitialSequences());
		IResource nominal = LongTermOptimiserHelper.getNominal(rawSequences, charterInMarket, vesselProvider);
		assert nominal != null;
		updateSequences(rawSequences, pairingsMap, nominal);
		return new Pair<>(rawSequences, 0L);
	}

	/**
	 * Updates the raw sequences given an allocations matrix
	 * 
	 * @param rawSequences
	 * @param pairingsMap
	 * @param nominal
	 */
	private void updateSequences(@NonNull IModifiableSequences rawSequences, @NonNull Map<ILoadOption, IDischargeOption> pairingsMap, @NonNull IResource nominal) {
		LongTermOptimiserHelper.moveElementsToUnusedList(rawSequences, portSlotProvider);
		IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(nominal);
		int insertIndex = 0;
		for (int i = 0; i < modifiableSequence.size(); i++) {
			if (portSlotProvider.getPortSlot(modifiableSequence.get(i)) != null && portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.End) {
				break;
			}
			insertIndex++;
		}
		List<ISequenceElement> unusedElements = rawSequences.getModifiableUnusedElements();
		for (ILoadOption loadOption : pairingsMap.keySet()) {
			IDischargeOption dischargeOption = pairingsMap.get(loadOption);
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
