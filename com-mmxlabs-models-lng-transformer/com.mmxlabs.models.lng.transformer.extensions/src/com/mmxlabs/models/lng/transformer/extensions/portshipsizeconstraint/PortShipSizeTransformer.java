/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * Used to convert (transform) EMF model objects to cached compatible optimisation model objects.
 * Note: In case of the constraint checker not being efficient enough, change to just caching IResource rather than looking up IVessel.
 */
public class PortShipSizeTransformer implements ISlotTransformer {

	private ModelEntityMap modelEntityMap;
	
	/**
	 * Provides a bridge between the transformer and the constraint checker, so that the transformer can
	 * pass data into the provider editor, to be used by the constraint checker after transformation is complete. 
	 */
	@Inject
	private IPortShipSizeProviderEditor portShipSizeProviderEditor;

	/**
	 * Converts between port slots and sequence elements + vice versa.
	 */
	@Inject
	private IPortSlotProvider portSlotProvider;

	/**
	 * The scenario model.
	 */
	private LNGScenarioModel rootObject;
		
	/**
	 * Temporary data structure for mapping between ports and sequence elements. 
	 */
	private final Map<Port, Collection<@NonNull ISequenceElement>> portMap = new HashMap<>();
	
	/**
	 * Temporary data structure for mapping between slots and sequence elements. 
	 */
	private final Map<Slot<?>, Collection<@NonNull ISequenceElement>> slotMap = new HashMap<>();
	
	/**
	 * Temporary list of load slots.
	 */
	private final List<LoadSlot> loadSlots = new LinkedList<>();
	
	/**
	 * Temporary list of discharge slots.
	 */
	private final List<DischargeSlot> dischargeSlots = new LinkedList<>();

	/**
	 * Temporary list of sequence elements.
	 */
	private final Set<ISequenceElement> allElements = new HashSet<>();

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.modelEntityMap = modelEntityMap;
	}

	@Override
	public void finishTransforming() {
		final CommercialModel commercialModel = rootObject.getReferenceModel().getCommercialModel();
	
		final HashSet<Vessel> doneVessels = new HashSet<>();
		
		if (commercialModel != null) {
			final LNGReferenceModel referenceModel = rootObject.getReferenceModel();
			final FleetModel fleetModel = referenceModel.getFleetModel();	
			
			for (Vessel vessel : fleetModel.getVessels()) {

				if (!doneVessels.contains(vessel)) {
					
					final IVessel iVessel = modelEntityMap.getOptimiserObjectNullChecked(vessel, IVessel.class);
					
					// Process purchase contract restrictions - these are the follower restrictions
					for (final LoadSlot slot : loadSlots) {
						addIncompatibleVesselSlotCombinationsToCache(vessel, iVessel, slot);
					}	

					// Process sales contract restrictions - these are the preceding restrictions
					for (final DischargeSlot slot : dischargeSlots) {
						addIncompatibleVesselSlotCombinationsToCache(vessel, iVessel, slot);
					}
				}

				doneVessels.add(vessel);
			}
		}

		clear();
	}

	private void addIncompatibleVesselSlotCombinationsToCache(Vessel vessel, final IVessel iVessel, final Slot<?> slot) {
		Collection<@NonNull ISequenceElement> elements = this.slotMap.get(slot);
		if (elements != null && iVessel != null && !this.checkCompatible(vessel, slot)) {
			this.portShipSizeProviderEditor.addIncompatibleResourceElements(iVessel, elements);
		}
	}
	
	/**
	 * Check whether a particular vessel is compatible with a particular slot with respect to the port.
	 * @param vessel
	 * @param slot
	 * @return true, if port on slot is compatible, false, otherwise.
	 */
	private boolean checkCompatible(Vessel vessel, Slot<?> slot) {
		Port port = slot.getPort();
		if (port != null) {
			return checkCompatible(port, vessel);
		}
		else {
			//Consider in future discussions, JKTC - slot maybe linked to several ports in the future.
			//If port is null, then it must be a non shipped cargo, so we ignore these in this constraint check.
			return true;
		}
	}
	
	/**
	 * Check whether a particular port is suitable for a particular vessel.
	 * @param port
	 * @param vessel
	 * @return false, if not compatible, true, otherwise.
	 */
	private boolean checkCompatible(Port port, Vessel vessel) {
		//Check that the minimum and maximum vessel sizes, if set, are not violated for both cases.
		return (!(port.isSetMinVesselSize() && vessel.getVesselOrDelegateCapacity() < port.getMinVesselSize()) &&
				!(port.isSetMaxVesselSize() && vessel.getVesselOrDelegateCapacity() > port.getMaxVesselSize()));
	}
	
	/**
	 * Clean out all the temporary data structures of this transformer.
	 */
	private void clear() {
		rootObject = null;
		modelEntityMap = null;
		portMap.clear();
		slotMap.clear();
		allElements.clear();
	}

	@Override
	public void slotTransformed(@NonNull final Slot<?> modelSlot, @NonNull final IPortSlot optimiserSlot) {
		final ISequenceElement sequenceElement = portSlotProvider.getElement(optimiserSlot);
		
		//Add to the list of all elements.
		allElements.add(sequenceElement);

		//Add the slot to either the list of load slots or discharge slots.
		if (modelSlot instanceof LoadSlot) {
			loadSlots.add((LoadSlot) modelSlot);
		}
		if (modelSlot instanceof DischargeSlot) {
			dischargeSlots.add((DischargeSlot) modelSlot);
		}

		//Add the slot.
		slotMap.computeIfAbsent(modelSlot, s -> new HashSet<>()).add(sequenceElement);
		
		//Add the port.
		final Port port = modelSlot.getPort();
		if (port != null) {
			portMap.computeIfAbsent(port, p -> new HashSet<>()).add(sequenceElement);
		}
	}
}
