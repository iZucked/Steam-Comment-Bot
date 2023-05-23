/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VoyageSpecification;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.SequencesAttributesProviderImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.insertion.SequencesHitchHikerHelper;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.IVoyageSpecificationProvider;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.VoyageSpecificationProviderImpl;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;

@NonNullByDefault
public class ScheduleSpecificationTransformer {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject
	private DateAndCurveHelper internalDateProvider;

	@Inject
	private IAlternativeElementProvider alternativeElementProvider;

	public ISequences createSequences(final ScheduleSpecification scheduleSpecification, final LNGDataTransformer dataTransformer, final boolean includeSpotSlots) {

		@NonNull
		final ModelEntityMap mem = dataTransformer.getModelEntityMap();
		final IOptimisationData optimisationData = dataTransformer.getOptimisationData();
		return createSequences(scheduleSpecification, mem, optimisationData, dataTransformer.getInjector(), includeSpotSlots);
	}

	public ISequences createSequences(final ScheduleSpecification scheduleSpecification, final ModelEntityMap mem, final IOptimisationData optimisationData, final Injector injector,
			final boolean includeSpotSlots) {

		final Set<ISequenceElement> usedElements = new LinkedHashSet<>();

		final List<IResource> orderedResources = new LinkedList<>();
		final Map<IResource, IModifiableSequence> sequences = new HashMap<>();

		final VoyageSpecificationProviderImpl voyageSpecificationProvider = new VoyageSpecificationProviderImpl();

		for (final VesselScheduleSpecification vesselSpecificiation : scheduleSpecification.getVesselScheduleSpecifications()) {
			final VesselAssignmentType vesselAllocation = vesselSpecificiation.getVesselAllocation();
			IResource resource = null;
			if (vesselAllocation instanceof VesselCharter e_vesselCharter) {
				final IVesselCharter o_vesselCharter = mem.getOptimiserObjectNullChecked(e_vesselCharter, IVesselCharter.class);
				resource = vesselProvider.getResource(o_vesselCharter);
			} else if (vesselAllocation instanceof CharterInMarketOverride e_charterInMarketOverride) {
				final IVesselCharter o_vesselCharter = mem.getOptimiserObjectNullChecked(e_charterInMarketOverride, IVesselCharter.class);
				resource = vesselProvider.getResource(o_vesselCharter);
			} else if (vesselAllocation instanceof CharterInMarket e_charterInMarket) {
				final int spotIndex = vesselSpecificiation.getSpotIndex();
				final ISpotCharterInMarket o_market = mem.getOptimiserObjectNullChecked(e_charterInMarket, ISpotCharterInMarket.class);
				final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
				for (final IResource o_resource : optimisationData.getResources()) {
					final IVesselCharter o_vesselCharter = vesselProvider.getVesselCharter(o_resource);
					final ISpotCharterInMarket spotCharterInMarket = o_vesselCharter.getSpotCharterInMarket();
					if (spotCharterInMarket != o_market) {
						continue;
					}
					if (o_vesselCharter.getSpotIndex() != spotIndex) {
						continue;
					}

					resource = o_resource;
					break;
				}
			} else {
				assert false;
			}

			// FIXME HERE
			// ALSO TEST THE REVALUATION
			// ADD TO PROGRESS MONITOR!
			if (resource == null && vesselSpecificiation.getEvents().isEmpty()) {
				// HACK! - why is the nominal vessel here anyway? not a nominal market
				continue;
			}
			assert resource != null;

			final List<ISequenceElement> elements = new LinkedList<>();
			final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
			elements.add(startElement);

			IPortSlot lastSlot = null;
			for (final ScheduleSpecificationEvent event : vesselSpecificiation.getEvents()) {
				if (event instanceof SlotSpecification slotSpecification) {
					final Slot<?> e_slot = slotSpecification.getSlot();
					final IPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_slot, IPortSlot.class);
					final ISequenceElement e = portSlotProvider.getElement(o_slot);
					elements.add(e);

					LocalDateTime arrivalTime = slotSpecification.getArrivalDate();
					if (arrivalTime != null) {
						if (e_slot.getPort() != null) {
							voyageSpecificationProvider.setArrivalTime(o_slot, internalDateProvider.convertTime(arrivalTime.atZone(e_slot.getPort().getZoneId())));
						} else {
							voyageSpecificationProvider.setArrivalTime(o_slot, internalDateProvider.convertTime(arrivalTime.atZone(ZoneId.of("Etc/UTC"))));
						}
					}

					lastSlot = o_slot;
				} else if (event instanceof VesselEventSpecification vesselEventSpecification) {
					final VesselEvent e_vesselEvent = vesselEventSpecification.getVesselEvent();
					final IVesselEventPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_vesselEvent, IVesselEventPortSlot.class);
					elements.addAll(o_slot.getEventSequenceElements());

					LocalDateTime arrivalTime = vesselEventSpecification.getArrivalDate();
					if (arrivalTime != null) {
						// Get first slot in event sequence rather than the last one.
						IPortSlot t = o_slot.getEventPortSlots().get(0);
						if (e_vesselEvent.getPort() != null) {
							voyageSpecificationProvider.setArrivalTime(t, internalDateProvider.convertTime(arrivalTime.atZone(e_vesselEvent.getPort().getZoneId())));
						} else {
							voyageSpecificationProvider.setArrivalTime(t, internalDateProvider.convertTime(arrivalTime.atZone(ZoneId.of("Etc/UTC"))));
						}
					}

					lastSlot = o_slot;
				} else if (event instanceof VoyageSpecification voyageSpecification) {
					if (lastSlot != null && voyageSpecification.isSetRouteOption()) {
						switch (voyageSpecification.getRouteOption()) {
						case DIRECT:
							voyageSpecificationProvider.setVoyageRouteOption(lastSlot, ERouteOption.DIRECT);
							break;
						case PANAMA:
							voyageSpecificationProvider.setVoyageRouteOption(lastSlot, ERouteOption.PANAMA);
							break;
						case SUEZ:
							voyageSpecificationProvider.setVoyageRouteOption(lastSlot, ERouteOption.SUEZ);
							break;
						default:
							break;

						}
					}
					if (lastSlot != null && voyageSpecification.isSetFuelChoice()) {
						switch (voyageSpecification.getFuelChoice()) {
						case NBO_BUNKERS:
							voyageSpecificationProvider.setFuelChoice(lastSlot, TravelFuelChoice.NBO_PLUS_BUNKERS);
							break;
						case NBO_FBO:
							voyageSpecificationProvider.setFuelChoice(lastSlot, TravelFuelChoice.NBO_PLUS_FBO);
							break;
						default:
							break;

						}
					}
					// Not supported
				} else {
					assert false;
				}
			}

			final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
			elements.add(endElement);

			orderedResources.add(resource);
			sequences.put(resource, new ListModifiableSequence(elements));

			// Important code
			for (final ISequenceElement e : elements) {
				if (!usedElements.add(e)) {
					final int ii = 0;
				}
			}
		}

		for (final NonShippedCargoSpecification nonShippedCargoSpecification : scheduleSpecification.getNonShippedCargoSpecifications()) {
			IResource resource = null;

			final List<ISequenceElement> elements = new LinkedList<>();
			for (final SlotSpecification slotSpecification : nonShippedCargoSpecification.getSlotSpecifications()) {
				final Slot<?> e_slot = slotSpecification.getSlot();
				final IPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_slot, IPortSlot.class);
				final ISequenceElement e = portSlotProvider.getElement(o_slot);
				elements.add(e);

				LocalDateTime arrivalTime = slotSpecification.getArrivalDate();
				if (arrivalTime != null) {
					if (e_slot.getPort() != null) {
						voyageSpecificationProvider.setArrivalTime(o_slot, internalDateProvider.convertTime(arrivalTime.atZone(e_slot.getPort().getZoneId())));
					} else {
						voyageSpecificationProvider.setArrivalTime(o_slot, internalDateProvider.convertTime(arrivalTime.atZone(ZoneId.of("Etc/UTC"))));
					}
				}

				final IVesselCharter o_vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(e);
				if (o_vesselCharter != null) {
					resource = vesselProvider.getResource(o_vesselCharter);
				}
			}

			// Important code
			for (final ISequenceElement e : elements) {
				if (!usedElements.add(e)) {
					final int ii = 0;
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

		final List<ISequenceElement> unusedElements = new LinkedList<>();

		for (final ScheduleSpecificationEvent event : scheduleSpecification.getOpenEvents()) {
			if (event instanceof SlotSpecification slotSpecification) {
				final Slot e_slot = slotSpecification.getSlot();
				final IPortSlot o_slot = mem.getOptimiserObjectNullChecked(e_slot, IPortSlot.class);
				final ISequenceElement e = portSlotProvider.getElement(o_slot);
				unusedElements.add(e);
				// Mark as used for spot slot check later
				usedElements.add(e);

				final IVesselCharter va = virtualVesselSlotProvider.getVesselCharterForElement(e);
				if (va != null) {
					final IResource resource = vesselProvider.getResource(va);

					orderedResources.add(resource);
					final List<ISequenceElement> elements = new LinkedList<>();
					final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
					elements.add(0, startElement);

					final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
					elements.add(endElement);

					sequences.put(resource, new ListModifiableSequence(elements));
				}

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
			final ISpotMarketSlotsProvider spotMarketSlotsProvider = injector.getInstance(ISpotMarketSlotsProvider.class);
			for (final SpotMarket sm : mem.getAllModelObjects(SpotMarket.class)) {
				final ISpotMarket oSM = mem.getOptimiserObjectNullChecked(sm, ISpotMarket.class);
				for (final ISequenceElement e : spotMarketSlotsProvider.getElementsFor(oSM)) {
					if (usedElements.contains(e)) {
						continue;
					}
					if (!unusedElements.contains(e)) {
						unusedElements.add(e);
					}
					final IVesselCharter va = virtualVesselSlotProvider.getVesselCharterForElement(e);
					if (va != null) {
						final IResource resource = vesselProvider.getResource(va);
						assert !orderedResources.contains(resource);

						orderedResources.add(resource);
						final List<ISequenceElement> elements = new LinkedList<>();
						final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
						elements.add(0, startElement);

						final ISequenceElement endElement = startEndRequirementProvider.getEndElement(resource);
						elements.add(endElement);

						sequences.put(resource, new ListModifiableSequence(elements));
					}
				}
			}
		}

		// Reproduce original sort order - By type, then by speed.
		Collections.sort(orderedResources, (o1, o2) -> {
			final IVesselCharter vesselCharter1 = vesselProvider.getVesselCharter(o1);
			final IVesselCharter vesselCharter2 = vesselProvider.getVesselCharter(o2);
			final VesselInstanceType vit1 = vesselCharter1.getVesselInstanceType();
			final VesselInstanceType vit2 = vesselCharter2.getVesselInstanceType();

			int x = vit1.compareTo(vit2);
			if (x == 0) {
				x = ((Integer) vesselCharter1.getVessel().getMaxSpeed()).compareTo(vesselCharter2.getVessel().getMaxSpeed());
			}
			return x;
		});

		// Is this needed?
		// unusedElements.removeAll(alternativeElementProvider.getAllAlternativeElements());

		final SequencesAttributesProviderImpl providers = new SequencesAttributesProviderImpl();
		providers.addProvider(IVoyageSpecificationProvider.class, voyageSpecificationProvider);

		final ModifiableSequences seq = new ModifiableSequences(orderedResources, sequences, unusedElements, providers);
		assert SequencesHitchHikerHelper.checkValidSequences(seq);

		return seq;
	}
}
