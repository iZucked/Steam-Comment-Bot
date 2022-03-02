/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class ScheduleToSequencesTransformer {

	public ISequences createSequences(final Schedule schedule, @NonNull final LNGDataTransformer dataTransformer) {

		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
		final Map<IResource, IModifiableSequence> resourceMap = new HashMap<>();
		final List<IResource> resources = new LinkedList<>();
		for (final Sequence seq : schedule.getSequences()) {
			if (seq.getSequenceType() == SequenceType.DES_PURCHASE || seq.getSequenceType() == SequenceType.FOB_SALE) {
				final Collection<CargoAllocation> allocations = new LinkedHashSet<>();
				for (final Event event : seq.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						allocations.add(slotVisit.getSlotAllocation().getCargoAllocation());
					}
				}
				for (final CargoAllocation ca : allocations) {
					final List<EObject> events = new LinkedList<>();
					LoadSlot purchase = null;
					DischargeSlot sale = null;
					for (final SlotAllocation sa : ca.getSlotAllocations()) {
						events.add(sa.getSlot());
						if (sa.getSlot() instanceof LoadSlot) {
							purchase = (LoadSlot) sa.getSlot();
						} else {
							sale = (DischargeSlot) sa.getSlot();
						}
					}
					final Pair<IResource, IModifiableSequence> optSeq = SequenceHelper.createFOBDESSequence(dataTransformer.getInjector(), purchase, sale);
					resourceMap.put(optSeq.getFirst(), optSeq.getSecond());
					resources.add(optSeq.getFirst());
				}
			} else if (seq.isFleetVessel() || seq.isTimeCharterVessel()) {
				final VesselAvailability vesselAvailability = seq.getVesselAvailability();
				final List<EObject> events = new LinkedList<>();
				for (final Event event : seq.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						events.add(slotVisit.getSlotAllocation().getSlot());
					} else if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						events.add(vesselEventVisit.getVesselEvent());
					}
				}
				final Pair<IResource, IModifiableSequence> optSeq = SequenceHelper.makeSequence(dataTransformer.getInjector(), vesselAvailability, events);
				resourceMap.put(optSeq.getFirst(), optSeq.getSecond());
				resources.add(optSeq.getFirst());
			} else if (seq.isSpotVessel()) {
				final CharterInMarket charterInMarket = seq.getCharterInMarket();
				final int spotIndex = seq.getSpotIndex();
				final List<EObject> events = new LinkedList<>();
				for (final Event event : seq.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						events.add(slotVisit.getSlotAllocation().getSlot());
					} else if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						events.add(vesselEventVisit.getVesselEvent());
					}
				}
				final Pair<IResource, IModifiableSequence> optSeq = SequenceHelper.makeSequence(dataTransformer.getInjector(), charterInMarket, spotIndex, events);
				resourceMap.put(optSeq.getFirst(), optSeq.getSecond());
				resources.add(optSeq.getFirst());
			} else {
				// ??
				assert false;
			}
		}
		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);

		final IModifiableSequences initialSequences = new ModifiableSequences(resources, resourceMap);

		// Add in open positions
		schedule.getOpenSlotAllocations().stream() //
				.map(OpenSlotAllocation::getSlot) //
				.map(s -> modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class)) //
				.map(portSlotProvider::getElement) //
				.forEach(initialSequences.getModifiableUnusedElements()::add);

		return initialSequences;

	}
}
