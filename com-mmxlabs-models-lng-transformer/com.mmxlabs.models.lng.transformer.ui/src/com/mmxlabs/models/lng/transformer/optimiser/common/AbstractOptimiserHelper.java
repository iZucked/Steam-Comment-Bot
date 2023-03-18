/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.optimiser.pairing.PairingOptimisationData;
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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public abstract class AbstractOptimiserHelper {
	public enum ShippingType {
		SHIPPED, NON_SHIPPED, ALL
	}

	private static Set<VesselInstanceType> ALLOWED_VESSEL_TYPES = Sets.newHashSet(VesselInstanceType.FLEET, VesselInstanceType.SPOT_CHARTER, VesselInstanceType.TIME_CHARTER);

	/**
	 * Moves everything to the unused list
	 * 
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
				if (portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.Load
						|| portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.Discharge
						|| portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.CharterOut
						|| portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.DryDock) {
					ISequenceElement removed = modifiableSequence.remove(i);
					modifiableUnusedElements.add(removed);
				}
			}
		}
	}

	public static IResource getVirtualResource(IPortSlot portSlot, IPortSlotProvider portSlotProvider, IVirtualVesselSlotProvider virtualVesselSlotProvider, IVesselProvider vesselProvider) {
		IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(portSlotProvider.getElement(portSlot));
		IResource resource = vesselProvider.getResource(vesselCharter);
		return resource;
	}

	public static void insertCargo(List<ISequenceElement> unusedElements, ILoadOption loadOption, IDischargeOption dischargeOption, IModifiableSequence desPurchase,
			IPortSlotProvider portSlotProvider) {
		desPurchase.insert(1, portSlotProvider.getElement(loadOption));
		unusedElements.remove(portSlotProvider.getElement(loadOption));
		desPurchase.insert(2, portSlotProvider.getElement(dischargeOption));
		unusedElements.remove(portSlotProvider.getElement(dischargeOption));
	}

	public static IResource getNominal(ModifiableSequences rawSequences, CharterInMarket charterInMarket, IVesselProvider vesselProvider) {
		for (IResource resource : rawSequences.getResources()) {
			IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP && vesselCharter.getSpotCharterInMarket() != null
					&& vesselCharter.getSpotCharterInMarket().getName().contains(charterInMarket.getName())) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Produces serialized data that can be used by Gurobi
	 * 
	 * @param longTermOptimisationData
	 * @param path
	 */
	public static void produceDataForGurobi(PairingOptimisationData longTermOptimisationData, String path) {
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
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path, false))) {
			oos.writeObject(serializable);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void printPairings(List<ILoadOption> loads, boolean[][] pairingsMatrix, PairingOptimisationData optimiserRecorder) {
		for (ILoadOption load : loads) {
			System.out.println(String.format("%s to %s", load.getId(),
					optimiserRecorder.getPairedDischarge(load, pairingsMatrix) == null ? "null" : optimiserRecorder.getPairedDischarge(load, pairingsMatrix).getId()));
		}
	}

	public static <T> T getPrestoredData(String path) throws IOException {
		T readCase = null;
		try (ObjectInputStream objectinputstream = new ObjectInputStream(new FileInputStream(path))) {
			readCase = (T) objectinputstream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
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

	/**
	 * Updates the raw sequences given an allocations matrix Note: Assumes that
	 * elements are on unused list already
	 * 
	 * @param rawSequences
	 * @param pairingsMap
	 * @param nominal
	 * @param portSlotProvider
	 * @param virtualVesselSlotProvider
	 * @param vesselProvider
	 */
	public static void updateVirtualSequences(@NonNull IModifiableSequences rawSequences, @NonNull List<List<IPortSlot>> cargoes, @NonNull Map<ILoadOption, IDischargeOption> pairingsMap,
			@NonNull IResource nominal, IPortSlotProvider portSlotProvider, IVirtualVesselSlotProvider virtualVesselSlotProvider, IVesselProvider vesselProvider, ShippingType shippingType) {
		IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(nominal);
		int insertIndex = 0;
		for (int i = 0; i < modifiableSequence.size(); i++) {
			if (portSlotProvider.getPortSlot(modifiableSequence.get(i)) != null && portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.End) {
				break;
			}
			insertIndex++;
		}
		List<ISequenceElement> unusedElements = rawSequences.getModifiableUnusedElements();
		for (List<IPortSlot> cargo : cargoes) {
			Optional<ILoadOption> load = cargo.stream().filter(s -> s instanceof ILoadOption).map(s -> (ILoadOption) s).findFirst();
			if (!load.isPresent()) {
				continue;
			}
			ILoadOption loadOption = load.get();
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
					IResource resource = AbstractOptimiserHelper.getVirtualResource(dischargeOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence fobSale = rawSequences.getModifiableSequence(resource);
						AbstractOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, fobSale, portSlotProvider);
					}
				}
			} else if (loadOption instanceof ILoadOption && dischargeOption instanceof IDischargeSlot) {
				// DES purchase
				if (shippingType == ShippingType.NON_SHIPPED || shippingType == ShippingType.ALL) {
					IResource resource = AbstractOptimiserHelper.getVirtualResource(loadOption, portSlotProvider, virtualVesselSlotProvider, vesselProvider);
					if (resource != null) {
						IModifiableSequence desPurchase = rawSequences.getModifiableSequence(resource);
						AbstractOptimiserHelper.insertCargo(unusedElements, loadOption, dischargeOption, desPurchase, portSlotProvider);
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

	public static boolean isShippedVessel(@NonNull IVesselCharter v) {
		return ALLOWED_VESSEL_TYPES.contains(v.getVesselInstanceType());
	}

	public static IVesselCharter getPNLVessel(final LNGDataTransformer dataTransformer, CharterInMarket charterInMarket, IVesselProvider vesselProvider) {
		IVesselCharter nominalMarketAvailability = null;
		final ISpotCharterInMarket o_nominalMarket = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);
		for (final IResource resource : vesselProvider.getSortedResources()) {
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			if (vesselCharter.getSpotCharterInMarket() == o_nominalMarket && vesselCharter.getSpotIndex() == -1) {
				nominalMarketAvailability = vesselCharter;
				break;
			}
		}
		assert nominalMarketAvailability != null;
		return nominalMarketAvailability;
	}

	public static long[] getCargoPNL(Long[][] profit, List<List<IPortSlot>> cargoes, List<ILoadOption> loads, List<IDischargeOption> discharges, @NonNull IVesselCharter pnlVessel) {
		long[] pnl = new long[cargoes.size()];
		int idx = 0;
		for (List<IPortSlot> cargo : cargoes) {
			pnl[idx++] = profit[loads.indexOf(cargo.get(0))][discharges.indexOf(cargo.get(cargo.size() - 1))] / pnlVessel.getVessel().getCargoCapacity();
		}
		return pnl;
	}

	public static void addTargetEventsToProvider(ILongTermSlotsProviderEditor longTermSlotsProviderEditor, Collection<IPortSlot> allPortSlots) {
		Set<PortType> eventsPortType = Sets.newHashSet(PortType.DryDock, PortType.Maintenance, PortType.CharterOut, PortType.CharterLength);
		allPortSlots.forEach(e -> {
			if (eventsPortType.contains(e.getPortType())) {
				if (e instanceof IVesselEventPortSlot) {
					longTermSlotsProviderEditor.addEvent(((IVesselEventPortSlot) e).getEventPortSlots());
				} else {
					longTermSlotsProviderEditor.addEvent(Collections.singletonList(e));
				}
			}
		});
	}

	public static void addTargetSlotsToProvider(ILongTermSlotsProviderEditor longTermSlotsProviderEditor, Collection<IPortSlot> allPortSlots) {
		Set<PortType> salesPortType = Sets.newHashSet(PortType.Load, PortType.Discharge);

		allPortSlots.forEach(e -> {
			if (salesPortType.contains(e.getPortType())) {
				longTermSlotsProviderEditor.addLongTermSlot(e);
			}
		});
	}

}
