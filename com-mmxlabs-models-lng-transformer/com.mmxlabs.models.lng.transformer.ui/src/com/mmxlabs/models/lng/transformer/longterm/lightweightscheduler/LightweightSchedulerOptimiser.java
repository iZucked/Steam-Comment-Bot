/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.minimaxlabs.rnd.representation.LightWeightOutputData;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
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
		
		List<List<Integer>> sequences = lightWeightSequenceOptimiser.optimise(lightWeightOptimisationData.getCargoes(), lightWeightOptimisationData.getVessels(),
				lightWeightOptimisationData.getCargoPNL(), lightWeightOptimisationData.getCargoToCargoCostsOnAvailability(), lightWeightOptimisationData.getCargoVesselRestrictions(),
				lightWeightOptimisationData.getCargoToCargoMinTravelTimes(), lightWeightOptimisationData.getCargoMinTravelTimes(), constraintCheckers, fitnessFunctions);

//		try {
//			sequences = getStoredSequences("/tmp/gurobiOutput.gb");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// (5) Export the pairings matrix to the raw sequences
		ModifiableSequences rawSequences = new ModifiableSequences(dataTransformer.getInitialSequences());
		
		@NonNull
		IVesselAvailability pnlVessel = LongTermOptimiserHelper.getPNLVessel(dataTransformer, charterInMarket, vesselProvider);

		// update shipped
		updateSequences(rawSequences, sequences, lightWeightOptimisationData.getCargoes(),
				lightWeightOptimisationData.getVessels(), lightWeightOptimisationData.getPairingsMap());
		// update non-shipped
		LongTermOptimiserHelper.updateVirtualSequences(rawSequences, lightWeightOptimisationData.getPairingsMap(),
				vesselProvider.getResource(pnlVessel), portSlotProvider, virtualVesselSlotProvider, vesselProvider, ShippingType.NON_SHIPPED);
		HashMap<ILoadOption, IDischargeOption> ununsedMap = new HashMap<>(lightWeightOptimisationData.getPairingsMap());
		for (List<Integer> sequence : sequences) {
			for (Integer idx : sequence) {
				ununsedMap.remove(lightWeightOptimisationData.getCargoes().get(idx).get(0));
			}
		}
		LongTermOptimiserHelper.updateVirtualSequences(rawSequences, ununsedMap, vesselProvider.getResource(pnlVessel),
				portSlotProvider, virtualVesselSlotProvider, vesselProvider, ShippingType.SHIPPED);
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
	private void updateSequences(@NonNull IModifiableSequences rawSequences, List<List<Integer>> sequences, List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels, @NonNull Map<ILoadOption, IDischargeOption> pairingsMap) {
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
