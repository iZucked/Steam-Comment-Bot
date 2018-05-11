/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorUnit;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossExtractor;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenIdleTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.MinMaxVolumeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LongTermOptimiserHelper {
	public static enum ShippingType {
		SHIPPED,
		NON_SHIPPED,
		ALL
	}
	
	private static Set<VesselInstanceType> ALLOWED_VESSEL_TYPES = Sets.newHashSet(VesselInstanceType.FLEET, VesselInstanceType.SPOT_CHARTER, VesselInstanceType.TIME_CHARTER);

	/**
	 * Moves everything to the unused list
	 * @param sequences
	 * @param portSlotProvider 
	 */
	public static void moveElementsToUnusedList(IModifiableSequences sequences, IPortSlotProvider portSlotProvider) {
		@NonNull
		List<@NonNull IResource> resources = sequences.getResources();
		for (@NonNull
		IResource resource : resources) {
			IModifiableSequence modifiableSequence = sequences.getModifiableSequence(resource);
			List<ISequenceElement> modifiableUnusedElements = sequences.getModifiableUnusedElements();
			for (int i = modifiableSequence.size() - 1; i > -1; i--) {
				if (portSlotProvider.getPortSlot(modifiableSequence.get(i)) instanceof IDischargeOption || portSlotProvider.getPortSlot(modifiableSequence.get(i)) instanceof ILoadOption) {
					ISequenceElement removed = modifiableSequence.remove(i);
					modifiableUnusedElements.add(removed);
				}
			}
		}
	}
	
	public static IResource getVirtualResource(IPortSlot portSlot, IPortSlotProvider portSlotProvider, IVirtualVesselSlotProvider virtualVesselSlotProvider, IVesselProvider vesselProvider) {
		IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(portSlotProvider.getElement(portSlot));
		IResource resource = vesselProvider.getResource(vesselAvailability);
		return resource;
	}

	public static void insertCargo(List<ISequenceElement> unusedElements, ILoadOption loadOption, IDischargeOption dischargeOption, IModifiableSequence desPurchase, IPortSlotProvider portSlotProvider) {
		desPurchase.insert(1, portSlotProvider.getElement(loadOption));
		unusedElements.remove(portSlotProvider.getElement(loadOption));
		desPurchase.insert(2, portSlotProvider.getElement(dischargeOption));
		unusedElements.remove(portSlotProvider.getElement(dischargeOption));
	}

	public static IResource getNominal(ModifiableSequences rawSequences, CharterInMarket charterInMarket, IVesselProvider vesselProvider) {
		for (IResource resource : rawSequences.getResources()) {
			IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP && vesselAvailability.getSpotCharterInMarket() != null && vesselAvailability.getSpotCharterInMarket().getName().contains(charterInMarket.getName())) {
				return resource;
			}
		}
		return null;
	}
	
	/**
	 * Produces serialized data that can be used by Gurobi
	 * @param longTermOptimisationData
	 * @param path
	 */
	public static void produceDataForGurobi(LongTermOptimisationData longTermOptimisationData, String path) {
		long[][] profitAsPrimitive = longTermOptimisationData.getProfitAsPrimitive();
		boolean[][] valid = longTermOptimisationData.getValid();
		boolean[] optionalLoads = longTermOptimisationData.getOptionalLoads();
		boolean[] optionalDischarges = longTermOptimisationData.getOptionalDischarges();
		serialize(profitAsPrimitive, path + "profit.lt");
		serialize(valid, path + "constraints.lt");
		serialize(optionalLoads, path + "loads.lt");
		serialize(optionalDischarges, path + "discharges.lt");
	}

	public static void serialize(Serializable serializable, String path) {
		try {
			File f = new File(path);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(path, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(serializable);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void printPairings(List<ILoadOption> loads, boolean[][] pairingsMatrix, LongTermOptimisationData optimiserRecorder) {
		for (ILoadOption load : loads) {
			System.out.println(String.format("%s to %s", load.getId(),
					optimiserRecorder.getPairedDischarge(load, pairingsMatrix) == null ? "null" : optimiserRecorder.getPairedDischarge(load, pairingsMatrix).getId()));
		}
	}
	
	public static <T> T getPrestoredData(String path) throws IOException {
		ObjectInputStream objectinputstream = null;
		T readCase = null;
		try {
			FileInputStream streamIn = new FileInputStream(path);
			objectinputstream = new ObjectInputStream(streamIn);
			readCase = (T) objectinputstream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectinputstream != null) {
				objectinputstream.close();
			}
		}
		return readCase;
	}
	
	/**
	 * Create a slot 2 slot value matrix
	 * 
	 * @param loads
	 * @param discharges
	 * @param nominalMarket
	 * @param executorService
	 * @param dataTransformer
	 * @param optimiserRecorder
	 */
	public static void getS2SBindings(final List<ILoadOption> loads, final List<IDischargeOption> discharges, final @NonNull CharterInMarket nominalMarket, ExecutorService executorService,
			final LNGDataTransformer dataTransformer, final LongTermOptimisationData optimiserRecorder) {
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		// TODO: Filter
		final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
		while (iterator.hasNext()) {
			final Constraint constraint = iterator.next();
			if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
		}
		ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);
		ScenarioUtils.createOrUpdateContraints(MinMaxVolumeConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);
		ScenarioUtils.createOrUpdateContraints(LadenIdleTimeConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

		final LoadDischargePairValueCalculatorUnit calculator = new LoadDischargePairValueCalculatorUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(),
				constraintAndFitnessSettings, executorService, dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());
		calculator.run(nominalMarket, loads, discharges, new NullProgressMonitor(), new ProfitAndLossExtractor(optimiserRecorder));
	}

	/**
	 * Updates the raw sequences given an allocations matrix
	 * Note: Assumes that elements are on unused list already
	 * @param rawSequences
	 * @param pairingsMap
	 * @param nominal
	 * @param portSlotProvider 
	 * @param virtualVesselSlotProvider 
	 * @param vesselProvider 
	 */
	public static void updateVirtualSequences(@NonNull IModifiableSequences rawSequences, @NonNull Map<ILoadOption, IDischargeOption> pairingsMap, @NonNull IResource nominal, IPortSlotProvider portSlotProvider, IVirtualVesselSlotProvider virtualVesselSlotProvider, IVesselProvider vesselProvider, ShippingType shippingType) {
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
				if (shippingType == ShippingType.SHIPPED || shippingType == ShippingType.ALL) {
					modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(loadOption));
					modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(dischargeOption));
					unusedElements.remove(portSlotProvider.getElement(loadOption));
					unusedElements.remove(portSlotProvider.getElement(dischargeOption));
				}
			} else if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeOption) {
				// Fob Sale
				if ((shippingType == ShippingType.NON_SHIPPED || shippingType == ShippingType.ALL)) {
					IResource resource = LongTermOptimiserHelper.getVirtualResource(dischargeOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence fobSale = rawSequences.getModifiableSequence(resource);
						LongTermOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, fobSale, portSlotProvider);
					}
				}
			} else if (loadOption instanceof ILoadOption && dischargeOption instanceof IDischargeSlot) {
				// DES purchase
				if (shippingType == ShippingType.NON_SHIPPED || shippingType == ShippingType.ALL) {
					IResource resource = LongTermOptimiserHelper.getVirtualResource(loadOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence desPurchase = rawSequences.getModifiableSequence(resource);
						LongTermOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, desPurchase, portSlotProvider);
					}
				}
			}
		}
	}
	
	public static List<List<IPortSlot>> getCargoes(List<ILoadOption> loads, List<IDischargeOption> discharges, boolean[][] pairingsMatrix, ShippingType cargoFilter) {
		List<List<IPortSlot>> cargoes = new LinkedList<>();
		for (int loadId = 0; loadId < pairingsMatrix.length; loadId++) {
			for (int dischargeId = 0; dischargeId < pairingsMatrix[loadId].length; dischargeId++) {
				if (cargoFilter == ShippingType.SHIPPED || cargoFilter == ShippingType.NON_SHIPPED) {
					// FOB/DES check if filter is on
					boolean shipped = loads.get(loadId) instanceof ILoadSlot && discharges.get(dischargeId) instanceof IDischargeSlot;
					boolean expression = cargoFilter == ShippingType.SHIPPED ? !shipped : shipped;
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

	public static boolean isShippedVessel(@NonNull IVesselAvailability v) {
		return ALLOWED_VESSEL_TYPES.contains(v.getVesselInstanceType());
	}
	
	public static IVesselAvailability getPNLVessel(final LNGDataTransformer dataTransformer, CharterInMarket charterInMarket, IVesselProvider vesselProvider) {
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

	public static long[] getCargoPNL(Long[][] profit, List<List<IPortSlot>> cargoes, List<ILoadOption> loads, List<IDischargeOption> discharges, @NonNull IVesselAvailability pnlVessel) {
		long[] pnl = new long[cargoes.size()];
		int idx = 0;
		for (List<IPortSlot> cargo : cargoes) {
			pnl[idx++] = profit[loads.indexOf(cargo.get(0))][discharges.indexOf(cargo.get(cargo.size() - 1))]/pnlVessel.getVessel().getCargoCapacity();
		}
		return pnl;
	}



}
