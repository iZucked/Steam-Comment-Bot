/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.minimaxlabs.rnd.representation.LightWeightOutputData;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper.ShippingType;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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
public class LightWeightSchedulerOptimiser {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private ILightWeightSequenceOptimiser lightWeightSequenceOptimiser;

	@Inject
	private ILightWeightOptimisationData lightWeightOptimisationData;

	@Inject
	private List<ILightWeightConstraintChecker> constraintCheckers;

	@Inject
	private List<ILightWeightFitnessFunction> fitnessFunctions;

	@Inject
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	private ISequences initialSequences;

	@Inject
	@Named(OptimiserConstants.DEFAULT_VESSEL)
	private IVesselAvailability pnlVesselAvailability;

	/**
	 * Perform a long term (nominal) trading optimisation
	 * 
	 * @param monitor
	 * 
	 * @param executorService
	 * @param dataTransformer
	 * @param charterInMarket
	 * @return
	 */
	public Pair<ISequences, Long> optimise(final IVesselAvailability pnlVessel, @NonNull IProgressMonitor monitor) {
		// Get optimised sequences from our injected sequences optimiser
		List<List<Integer>> sequences = lightWeightSequenceOptimiser.optimise(lightWeightOptimisationData, constraintCheckers, fitnessFunctions, monitor);

		// Export the pairings matrix to the raw sequences:
		
		ModifiableSequences rawSequences = new ModifiableSequences(initialSequences);
		// (a) update shipped
		updateSequences(rawSequences, sequences, lightWeightOptimisationData.getCargoes(), lightWeightOptimisationData.getVessels(), lightWeightOptimisationData.getPairingsMap());
		
		// (b) update non-shipped
		LightWeightOptimiserHelper.updateVirtualSequences(rawSequences, lightWeightOptimisationData.getPairingsMap(), vesselProvider.getResource(pnlVessel), portSlotProvider,
				virtualVesselSlotProvider, vesselProvider, ShippingType.NON_SHIPPED);
		
		// (c) update unused
		HashMap<ILoadOption, IDischargeOption> unusedMap = new HashMap<>(lightWeightOptimisationData.getPairingsMap());
		for (List<Integer> sequence : sequences) {
			for (Integer idx : sequence) {
				unusedMap.remove(lightWeightOptimisationData.getCargoes().get(idx).get(0));
			}
		}
		LightWeightOptimiserHelper.updateVirtualSequences(rawSequences, unusedMap, vesselProvider.getResource(pnlVessel), portSlotProvider, virtualVesselSlotProvider, vesselProvider,
				ShippingType.SHIPPED);
		
		return new Pair<>(rawSequences, 0L);
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
	private void updateSequences(@NonNull IModifiableSequences rawSequences, List<List<Integer>> sequences, List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels,
			@NonNull Map<ILoadOption, IDischargeOption> pairingsMap) {
		LightWeightOptimiserHelper.moveElementsToUnusedList(rawSequences, portSlotProvider);
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
				ILoadOption loadOption = LightWeightOptimiserHelper.getLoadOption(cargo);
				IDischargeOption dischargeOption = LightWeightOptimiserHelper.getDischargeOption(cargo);
				IPortSlot vesselEvent = LightWeightOptimiserHelper.getVesselEvent(cargo);
				if (vesselEvent != null) {
					modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(vesselEvent));
					unusedElements.remove(portSlotProvider.getElement(vesselEvent));
				} else if (dischargeOption == null) { // skip if unallocated
					continue;
				} else if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
					// FOB-DES
					modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(loadOption));
					modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(dischargeOption));
					unusedElements.remove(portSlotProvider.getElement(loadOption));
					unusedElements.remove(portSlotProvider.getElement(dischargeOption));
				} else if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeOption) {
					// Fob Sale
					IResource resource = LightWeightOptimiserHelper.getVirtualResource(dischargeOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence fobSale = rawSequences.getModifiableSequence(resource);
						LightWeightOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, fobSale, portSlotProvider);
					}
				} else if (loadOption instanceof ILoadOption && dischargeOption instanceof IDischargeSlot) {
					// DES purchase
					IResource resource = LightWeightOptimiserHelper.getVirtualResource(loadOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence desPurchase = rawSequences.getModifiableSequence(resource);
						LightWeightOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, desPurchase, portSlotProvider);
					}
				}
			}
		}
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
