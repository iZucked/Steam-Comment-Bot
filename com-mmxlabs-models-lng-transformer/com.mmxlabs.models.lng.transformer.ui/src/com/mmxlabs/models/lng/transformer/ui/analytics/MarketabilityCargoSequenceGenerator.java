/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.Sets;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ui.analytics.marketability.MarketabilityWindowTrimmer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.SequencesAttributesProviderImpl;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.impl.EndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.RouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.impl.StartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalVessel;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.IPanamaAllowedBookingsProvider;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.IVoyageSpecificationProvider;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.PanamaAllowedBookingsProviderImpl;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.VoyageSpecificationProviderImpl;

public class MarketabilityCargoSequenceGenerator {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IVesselProviderEditor vesselProvider;

	@Inject
	private @NonNull IPortSlotProviderEditor portSlotProvider;

	@Inject
	private @NonNull IStartEndRequirementProviderEditor startEndRequirementProvider;

	@Inject
	private @NonNull ModelEntityMap modelEntityMap;

	@Inject
	private @NonNull IPanamaBookingsProvider panamaBookings;

	@Inject
	private @NonNull DateAndCurveHelper dateHelper;

	@Inject
	private @NonNull IReturnElementProvider returnElementProvider;

	@Inject
	private @NonNull MarketabilityWindowTrimmer trimmer;

	private boolean compareSlots(IPortSlot slot, Slot<?> slot2) {
		IPortSlot oSlot2 = modelEntityMap.getOptimiserObjectNullChecked(slot2, IPortSlot.class);
		return slot == oSlot2;
	}

	private @NonNull IModifiableSequence createDiversionSequence(final Schedule schedule, final @NonNull IResource targetResource, final ISequenceElement load, final ISequenceElement saleMarket,
			final ISequence sequence, final SequencesAttributesProviderImpl providers, final Integer vesselSpeed) {

		IVesselCharter vesselCharter = vesselProvider.getVesselCharter(targetResource);

		ISequenceElement nextEvent = null;
		for (int i = 0; i < sequence.size(); i++) {
			if (sequence.get(i) == load) {
				nextEvent = sequence.get(i + 2);
				break;
			}
		}
		assert nextEvent != null;
		if (vesselSpeed != null) {
			clampVesselSpeed(vesselCharter, OptimiserUnitConvertor.convertToInternalSpeed(vesselSpeed));
		}
		ISequenceElement start = startEndRequirementProvider.getStartElement(targetResource);
		ISequenceElement end = startEndRequirementProvider.getEndElement(targetResource);

		IPortSlot nextEventSlot = portSlotProvider.getPortSlot(nextEvent);
		Optional<@NonNull SlotAllocation> nextEventAllocation = schedule.getSlotAllocations().stream()//
				.filter(x -> compareSlots(nextEventSlot, x.getSlot()))//
				.findFirst();
		assert load != null;
		{
			IPortSlot loadSlot = portSlotProvider.getPortSlot(load);
			SlotVisit visit = schedule.getSlotAllocations().stream().filter(x -> compareSlots(loadSlot, x.getSlot()))//
					.map(SlotAllocation::getSlotVisit)//
					.findFirst()//
					.orElseThrow();
			int startTime = dateHelper.convertTime(visit.getStart());
			IStartRequirement newStartRequirement = new StartRequirement(loadSlot.getPort(), true, false, new TimeWindow(startTime, startTime + 1),
					vesselCharter.getStartRequirement().getHeelOptions());
			IEndRequirement newEndRequirement = null;
			IStartRequirement threadStartRequirement = startEndRequirementProvider.getStartRequirement(targetResource);
			IEndRequirement threadEndRequirement = startEndRequirementProvider.getEndRequirement(targetResource);

			if (nextEventAllocation.isPresent()) {
				// Next event is not the end event
				SlotVisit nextEventVisit = nextEventAllocation.get().getSlotVisit();

				int endTime = dateHelper.convertTime(nextEventVisit.getStart());
				TimeWindow endWindow = new TimeWindow(endTime, endTime + 1);
				ISequenceElement returnElement = returnElementProvider.getReturnElement(targetResource, nextEventSlot.getPort());
				newEndRequirement = new EndRequirement(List.of(nextEventSlot.getPort()), true, true, endWindow, (vesselCharter.getEndRequirement().getHeelOptions()));
				end = returnElement;
				assert end != null;
			} else {
				newEndRequirement = null;
			}

			if (threadStartRequirement instanceof ThreadLocalStartRequirement sRequirement && threadEndRequirement instanceof ThreadLocalEndRequirement eRequirement) {
				sRequirement.setStartRequirement(newStartRequirement);
				eRequirement.setEndRequirment(newEndRequirement);
			}
		}

		IPortSlot portSlot = portSlotProvider.getPortSlot(load);
		SlotVisit visit = schedule.getSlotAllocations().stream().filter(x -> compareSlots(portSlot, x.getSlot())).map(SlotAllocation::getSlotVisit).findFirst().orElseThrow();
		int loadTime = dateHelper.convertTime(visit.getStart());

		VoyageSpecificationProviderImpl voyageProvider = new VoyageSpecificationProviderImpl();
		voyageProvider.setArrivalTime(portSlot, loadTime);
		providers.addProvider(IVoyageSpecificationProvider.class, voyageProvider);
		setAllowedPanamaBookings(providers, schedule, portSlot, portSlot, targetResource);

		return new ListModifiableSequence(List.of(start, load, saleMarket, end));

	}

	private void clampVesselSpeed(IVesselCharter vc, int maxSpeed) {
		IVessel vessel = vc.getVessel();
		if (!(vessel instanceof ThreadLocalVessel)) {
			throw new IllegalArgumentException();
		}
		ThreadLocalVessel oVessel = (ThreadLocalVessel) vessel;
		oVessel.setMinSpeed(maxSpeed);
		oVessel.setMaxSpeed(maxSpeed);
	}

	@NonNullByDefault
	private void setAllowedPanamaBookings(SequencesAttributesProviderImpl providers, Schedule schedule, IPortSlot load, IPortSlot nextEvent, IResource resource) {

		final PanamaAllowedBookingsProviderImpl panamaAllowedBookingsProvider = new PanamaAllowedBookingsProviderImpl();
		Set<IRouteOptionBooking> otherCargoBookings = new HashSet<>();

		for (Sequence evaluatedSequence : schedule.getSequences()) {
			List<Event> events = evaluatedSequence.getEvents();
			if (evaluatedSequence.getVesselCharter() == null) {
				continue;
			}

			Event currentEvent = events.get(0);
			while (currentEvent.getNextEvent() != null) {
				if (currentEvent instanceof SlotVisit slotVisit) {
					Slot<?> targetSlot = slotVisit.getSlotAllocation().getSlot();
					IPortSlot oTargetSlot = modelEntityMap.getOptimiserObjectNullChecked(targetSlot, IPortSlot.class);
					if (oTargetSlot == load) {
						do {
							if (currentEvent instanceof SlotVisit sv) {
								targetSlot = sv.getSlotAllocation().getSlot();
								oTargetSlot = modelEntityMap.getOptimiserObjectNullChecked(targetSlot, IPortSlot.class);
							}
							currentEvent = currentEvent.getNextEvent();
						} while (!oTargetSlot.equals(nextEvent) && currentEvent != null);
						if (currentEvent == null) {
							break;
						}
					}
				} else if (currentEvent instanceof Journey journey && journey.getCanalBooking() != null) {
					// Add bookings used by other cargoes
					final IRouteOptionBooking booking = modelEntityMap.getOptimiserObjectNullChecked(journey.getCanalBooking(), IRouteOptionBooking.class);
					otherCargoBookings.add(booking);
				}
				currentEvent = currentEvent.getNextEvent();
			}

		}

		Set<IRouteOptionBooking> allPanamaBookings = panamaBookings.getAllBookings().values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
		// Marketable Cargo can only use unused Panama bookings and bookings used in the
		// original cargo
		Set<IRouteOptionBooking> allowedBookings = Sets.difference(allPanamaBookings, otherCargoBookings);

		// Optimiser avoids applying Panama bookings to a vessel if the booking could be applied to multiple vessels 
		// Transforms the original Panama bookings into bookings that contain only the vessel in the marketable cargo, if the vessel is in the original booking vessel group
		List<@NonNull IRouteOptionBooking> mappedBookings = allowedBookings.stream().map(booking -> {
			final IVessel oVessel = vesselProvider.getVesselCharter(resource).getVessel();
			if (booking.getVessels().isEmpty() || booking.getVessels().contains(oVessel)) {
				return Optional.of(new RouteOptionBooking(booking.getBookingDate(), booking.getEntryPoint(), booking.getRouteOption(), Set.of(oVessel)));
			} else {
				return Optional.empty();
			}
		}).filter(Optional::isPresent).map(Optional::get).map(IRouteOptionBooking.class::cast).toList();

		panamaAllowedBookingsProvider.setAllowedBookings(mappedBookings);

		providers.addProvider(IPanamaAllowedBookingsProvider.class, panamaAllowedBookingsProvider);
	}

	@NonNullByDefault
	public void generateOptions(final Schedule schedule, final ISequences sequences, final List<@NonNull ISequenceElement> orderedCargoElements, final IResource targetResource,
			final IPortSlot portSlot, final Integer vesselSpeed, final ToBooleanFunction<ISequences> action) {

		for (final ISequenceElement e : orderedCargoElements) {
			if (!helper.checkResource(e, targetResource)) {
				return;
			}
		}
		final ISequence seq = sequences.getSequence(targetResource);
		final ISequenceElement load = orderedCargoElements.get(0);
		final ISequenceElement salesMarket = orderedCargoElements.get(orderedCargoElements.size() - 1);
		assert salesMarket != null;
		final SequencesAttributesProviderImpl providers = new SequencesAttributesProviderImpl();
		final @NonNull IModifiableSequence modifiableSequence = createDiversionSequence(schedule, targetResource, load, salesMarket, seq, providers, vesselSpeed);
		final Map<IResource, IModifiableSequence> sequenceMap = new HashMap<>();
		sequenceMap.put(targetResource, modifiableSequence);
		final IModifiableSequences newSequences = new ModifiableSequences(List.of(targetResource), sequenceMap, new ArrayList<>(), providers);

		trimmer.setTrim(portSlot, MarketabilityWindowTrimmer.Mode.EARLIEST, 0);
		final boolean earliestValid = action.accept(newSequences);
		trimmer.setTrim(portSlot, MarketabilityWindowTrimmer.Mode.LATEST, 0);
		final boolean latestValid = action.accept(newSequences);

		if (earliestValid && !latestValid) {
			for (int j = 0; j < 31 * 24; j++) {
				trimmer.setTrim(portSlot, MarketabilityWindowTrimmer.Mode.SHIFT, j);
				final boolean newValid = action.accept(newSequences);
				if (newValid)
					break;
			}
		}
		trimmer.resetTrim();
	}
}
