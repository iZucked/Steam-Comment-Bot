/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VoyageSpecification;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.insertion.SequencesHitchHikerHelper;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

public class ScheduleSpecificationTransformer {
	private static final Logger log = LoggerFactory.getLogger(ScheduleSpecificationTransformer.class);

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	@NonNull
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@NonNull
	public ISequences createSequences(@NonNull final ScheduleSpecification scheduleSpecification, LNGDataTransformer dataTransformer) {

		final Set<ISequenceElement> usedElements = new HashSet<>();

		@NonNull
		final ModelEntityMap mem = dataTransformer.getModelEntityMap();

		final List<IResource> orderedResources = new LinkedList<>();
		final Map<IResource, ISequence> sequences = new HashMap<>();
		final List<ISequenceElement> unusedElements = new LinkedList<>();
		IOptimisationData optimisationData = dataTransformer.getOptimisationData();
		for (final VesselScheduleSpecification vesselSpecificiation : scheduleSpecification.getVesselScheduleSpecifications()) {
			final VesselAssignmentType vesselAllocation = vesselSpecificiation.getVesselAllocation();
			IResource resource = null;
			if (vesselAllocation instanceof VesselAvailability) {
				final VesselAvailability e_vesselAvailability = (VesselAvailability) vesselAllocation;
				final IVesselAvailability o_vesselAvailability = mem.getOptimiserObjectNullChecked(e_vesselAvailability, IVesselAvailability.class);
				resource = vesselProvider.getResource(o_vesselAvailability);
			} else if (vesselAllocation instanceof CharterInMarketOverride) {
				final CharterInMarketOverride e_charterInMarketOverride = (CharterInMarketOverride) vesselAllocation;
				final IVesselAvailability o_vesselAvailability = mem.getOptimiserObjectNullChecked(e_charterInMarketOverride, IVesselAvailability.class);
				resource = vesselProvider.getResource(o_vesselAvailability);
			} else if (vesselAllocation instanceof CharterInMarket) {
				final CharterInMarket e_charterInMarket = (CharterInMarket) vesselAllocation;
				int spotIndex = vesselSpecificiation.getSpotIndex();
				final ISpotCharterInMarket o_market = mem.getOptimiserObjectNullChecked(e_charterInMarket, ISpotCharterInMarket.class);

				for (final IResource o_resource : optimisationData.getResources()) {
					final IVesselAvailability o_vesselAvailability = vesselProvider.getVesselAvailability(o_resource);
					ISpotCharterInMarket spotCharterInMarket = o_vesselAvailability.getSpotCharterInMarket();
					if (spotCharterInMarket != o_market) {
						continue;
					}
					if (o_vesselAvailability.getSpotIndex() != spotIndex) {
						continue;
					}

					resource = o_resource;
					break;
				}
			} else {
				assert false;
			}
			assert resource != null;

			final List<ISequenceElement> elements = new LinkedList<>();
			final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
			elements.add(startElement);

			for (final ScheduleSpecificationEvent event : vesselSpecificiation.getEvents()) {
				if (event instanceof SlotSpecification) {
					final SlotSpecification slotSpecification = (SlotSpecification) event;
					final Slot e_slot = slotSpecification.getSlot();
					final IPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_slot, IPortSlot.class);
					final ISequenceElement e = portSlotProvider.getElement(o_slot);
					elements.add(e);
				} else if (event instanceof VesselEventSpecification) {
					final VesselEventSpecification vesselEventSpecification = (VesselEventSpecification) event;
					final VesselEvent e_vesselEvent = vesselEventSpecification.getVesselEvent();
					final IVesselEventPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_vesselEvent, IVesselEventPortSlot.class);
					elements.addAll(o_slot.getEventSequenceElements());
				} else if (event instanceof VoyageSpecification) {
					final VoyageSpecification voyageSpecification = (VoyageSpecification) event;

					// Not supported
				} else {
					assert false;
				}
			}

			final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
			elements.add(endElement);

			orderedResources.add(resource);
			sequences.put(resource, new ListModifiableSequence(elements));

			for (ISequenceElement e : elements) {
				if (!usedElements.add(e)) {
					int ii = 0;
				}

			}

		}

		for (final NonShippedCargoSpecification nonShippedCargoSpecification : scheduleSpecification.getNonShippedCargoSpecifications()) {
			IResource resource = null;

			final List<ISequenceElement> elements = new LinkedList<>();
			for (final SlotSpecification slotSpecification : nonShippedCargoSpecification.getSlotSpecifications()) {
				final Slot e_slot = slotSpecification.getSlot();
				final IPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_slot, IPortSlot.class);
				final ISequenceElement e = portSlotProvider.getElement(o_slot);
				elements.add(e);

				IVesselAvailability o_vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(e);
				if (o_vesselAvailability != null) {
					resource = vesselProvider.getResource(o_vesselAvailability);
				}
			}

			for (ISequenceElement e : elements) {
				if (!usedElements.add(e)) {
					int ii = 0;
				}

			}

			assert resource != null;
			orderedResources.add(resource);

			final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
			elements.add(0, startElement);

			final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
			elements.add(endElement);

			sequences.put(resource, new ListModifiableSequence(elements));
		}

		for (final ScheduleSpecificationEvent event : scheduleSpecification.getOpenEvents()) {
			if (event instanceof SlotSpecification) {
				final SlotSpecification slotSpecification = (SlotSpecification) event;
				final Slot e_slot = slotSpecification.getSlot();
				final IPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_slot, IPortSlot.class);
				final ISequenceElement e = portSlotProvider.getElement(o_slot);
				unusedElements.add(e);
			} else if (event instanceof VesselEventSpecification) {
				final VesselEventSpecification vesselEventSpecification = (VesselEventSpecification) event;
				final VesselEvent e_vesselEvent = vesselEventSpecification.getVesselEvent();
				final IVesselEventPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_vesselEvent, IVesselEventPortSlot.class);
				unusedElements.addAll(o_slot.getEventSequenceElements());
			} else {
				assert false;
			}
		}

		// Optionally - add in spot market slots?
		{
		}
		ISequences seq = new Sequences(orderedResources, sequences, unusedElements);

		// assert SequencesHitchHikerHelper.checkValidSequences(seq);
		return seq;
	}
}
