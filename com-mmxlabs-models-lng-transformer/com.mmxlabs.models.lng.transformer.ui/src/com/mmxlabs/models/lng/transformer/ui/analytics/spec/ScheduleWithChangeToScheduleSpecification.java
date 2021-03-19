/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.spec;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.models.lng.analytics.CargoChange;
import com.mmxlabs.models.lng.analytics.Change;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.OpenSlotChange;
import com.mmxlabs.models.lng.analytics.RealSlotDescriptor;
import com.mmxlabs.models.lng.analytics.SlotDescriptor;
import com.mmxlabs.models.lng.analytics.SlotType;
import com.mmxlabs.models.lng.analytics.SpotMarketSlotDescriptor;
import com.mmxlabs.models.lng.analytics.VesselEventChange;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.rcp.common.ecore.EMFCopier;

public class ScheduleWithChangeToScheduleSpecification {

	private static final String PREFIX_MARKET = "market-";
	private static final String PREFIX_SLOT = "slot-";
	private final Function<Pair<CharterInMarket, Integer>, CharterInMarketOverride> buildMarketAvailability = (oldAvailability) -> {
		final CharterInMarketOverride newAvailability = CargoFactory.eINSTANCE.createCharterInMarketOverride();
		newAvailability.setCharterInMarket(oldAvailability.getFirst());
		newAvailability.setSpotIndex(oldAvailability.getSecond());
		return newAvailability;
	};
	private final UnaryOperator<VesselAvailability> buildVesselAvailability = (oldAvailability) -> {
		final VesselAvailability newAvailability = CargoFactory.eINSTANCE.createVesselAvailability();

		newAvailability.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
		newAvailability.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

		newAvailability.setVessel(oldAvailability.getVessel());
		newAvailability.setEntity(oldAvailability.getEntity());
		newAvailability.setFleet(oldAvailability.isFleet());
		newAvailability.setCharterNumber(oldAvailability.getCharterNumber());
		newAvailability.setOptional(oldAvailability.isOptional());

		if (oldAvailability.isSetTimeCharterRate()) {
			newAvailability.setTimeCharterRate(oldAvailability.getTimeCharterRate());
		}
		return newAvailability;
	};
	private final BiConsumer<CharterInMarketOverride, PortVisit> processFirstEvent_MarketAvailability = (newAvailability, visit) -> {
		newAvailability.setStartDate(visit.getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		// newAvailability.setStartAt(visit.getPort());
		final StartHeelOptions startHeelOptions = CommercialFactory.eINSTANCE.createStartHeelOptions();
		newAvailability.setStartHeel(startHeelOptions);
		newAvailability.getStartHeel().setMinVolumeAvailable(visit.getHeelAtStart());
		newAvailability.getStartHeel().setMaxVolumeAvailable(visit.getHeelAtStart());
		newAvailability.getStartHeel().setPriceExpression("");
		newAvailability.getStartHeel().setCvValue(22.6); // Does not matter...
	};
	private final BiConsumer<VesselAvailability, PortVisit> processFirstEvent_VesselAvailability = (newAvailability, visit) -> {
		newAvailability.setStartAfter(visit.getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		newAvailability.setStartBy(visit.getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		newAvailability.setStartAt(visit.getPort());
		newAvailability.getStartHeel().setMinVolumeAvailable(visit.getHeelAtStart());
		newAvailability.getStartHeel().setMaxVolumeAvailable(visit.getHeelAtStart());
		newAvailability.getStartHeel().setPriceExpression("");
		newAvailability.getStartHeel().setCvValue(22.6); // Does not matter...
	};
	private final BiConsumer<CharterInMarketOverride, PortVisit> processLastSegmentEvent_MarketAvailability = (newAvailability, visit) -> {
		newAvailability.setEndDate(visit.getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		newAvailability.setEndPort(visit.getPort());
		final EndHeelOptions endHeelOptions = CommercialFactory.eINSTANCE.createEndHeelOptions();
		newAvailability.setEndHeel(endHeelOptions);
		newAvailability.getEndHeel().setMinimumEndHeel(visit.getHeelAtStart());
		newAvailability.getEndHeel().setMaximumEndHeel(visit.getHeelAtStart());
		newAvailability.getEndHeel().setPriceExpression("");
		EVesselTankState endState = EVesselTankState.EITHER;
		if (visit instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) visit;
			final Slot slot = slotVisit.getSlotAllocation().getSlot();
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isSetArriveCold()) {
					if (loadSlot.isArriveCold()) {
						endState = EVesselTankState.MUST_BE_COLD;
					}
				} else {
					final Port p = loadSlot.getPort();
					if (!p.isAllowCooldown()) {
						endState = EVesselTankState.MUST_BE_COLD;
					}
				}
			}
		} else if (visit instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) visit;
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			if (vesselEvent instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
				endState = charterOutEvent.getRequiredHeel().getTankState();
				newAvailability.getEndHeel().setMinimumEndHeel(charterOutEvent.getRequiredHeel().getMinimumEndHeel());
				newAvailability.getEndHeel().setMaximumEndHeel(charterOutEvent.getRequiredHeel().getMaximumEndHeel());
			}
		}
		newAvailability.getEndHeel().setTankState(endState);

		newAvailability.setMinDuration(0);
		newAvailability.setMaxDuration(0);

		newAvailability.setIncludeBallastBonus(true);

	};
	private final BiConsumer<VesselAvailability, PortVisit> processLastSegmentEvent_VesselAvailability = (newAvailability, visit) -> {
		newAvailability.setEndAfter(visit.getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		newAvailability.setEndBy(visit.getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		newAvailability.getEndAt().add(visit.getPort());

		newAvailability.getEndHeel().setMinimumEndHeel(visit.getHeelAtStart());
		newAvailability.getEndHeel().setMaximumEndHeel(visit.getHeelAtStart());
		newAvailability.getEndHeel().setPriceExpression("");
		EVesselTankState endState = EVesselTankState.EITHER;
		if (visit instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) visit;
			final Slot slot = slotVisit.getSlotAllocation().getSlot();
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isSetArriveCold()) {
					if (loadSlot.isArriveCold()) {
						endState = EVesselTankState.MUST_BE_COLD;
					}
				} else {
					final Port p = loadSlot.getPort();
					if (!p.isAllowCooldown()) {
						endState = EVesselTankState.MUST_BE_COLD;
					}
				}
			}
		} else if (visit instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) visit;
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			if (vesselEvent instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
				endState = charterOutEvent.getRequiredHeel().getTankState();
				newAvailability.getEndHeel().setMinimumEndHeel(charterOutEvent.getRequiredHeel().getMinimumEndHeel());
				newAvailability.getEndHeel().setMaximumEndHeel(charterOutEvent.getRequiredHeel().getMaximumEndHeel());
			}
		}
		newAvailability.getEndHeel().setTankState(endState);

		newAvailability.setMinDuration(0);
		newAvailability.setMaxDuration(0);
	};
	private final TriConsumer<Pair<CharterInMarket, Integer>, CharterInMarketOverride, PortVisit> processLastSequenceEvent_MarketAvailability = (oldAvailabilityPair, newAvailability,
			segmentStart) -> {

		final CharterInMarket oldAvailability = oldAvailabilityPair.getFirst();

		newAvailability.setIncludeBallastBonus(true);

		if (oldAvailability.getMarketOrContractMinDuration() != 0 || oldAvailability.getMarketOrContractMaxDuration() != 0) {

			final Event firstSequenceEvent = segmentStart.getSequence().getEvents().get(0);
			final ZonedDateTime sequenceStart = firstSequenceEvent.getStart();
			final ZonedDateTime vesselStart = segmentStart.getStart();

			final int hours = Hours.between(sequenceStart, vesselStart);
			if (hours > 0) {
				if (oldAvailability.getMarketOrContractMinDuration() != 0) {
					int minDurationInHours = oldAvailability.getMarketOrContractMinDuration() * 24;
					minDurationInHours -= hours;
					if (minDurationInHours < 0) {
						minDurationInHours = 0;
					}
					newAvailability.setMinDuration(minDurationInHours / 24);
				}
				if (oldAvailability.getMarketOrContractMaxDuration() != 0) {
					int maxDurationInHours = oldAvailability.getMarketOrContractMaxDuration() * 24;
					maxDurationInHours -= hours;
					if (maxDurationInHours < 0) {
						maxDurationInHours = 0;
					}
					newAvailability.setMaxDuration(maxDurationInHours / 24);
				}
			}

		}
	};
	private final TriConsumer<VesselAvailability, VesselAvailability, PortVisit> processLastSequenceEvent_VesselAvailability = (oldAvailability, newAvailability, segmentStart) -> {
		if (oldAvailability.isSetEndAfter()) {
			newAvailability.setEndAfter(oldAvailability.getEndAfter());
		}
		if (oldAvailability.isSetEndBy()) {
			newAvailability.setEndBy(oldAvailability.getEndBy());
		}
		newAvailability.getEndAt().addAll(oldAvailability.getEndAt());
		newAvailability.setEndHeel(EMFCopier.copy(oldAvailability.getEndHeel()));

		if (oldAvailability.getGenericCharterContract() != null) {
			newAvailability.setGenericCharterContract(EMFCopier.copy(oldAvailability.getGenericCharterContract()));
		}
		if (oldAvailability.getGenericCharterContract() != null) {
			newAvailability.setGenericCharterContract(oldAvailability.getGenericCharterContract());
		}

		if (oldAvailability.getCharterOrDelegateMinDuration() != 0 || oldAvailability.getCharterOrDelegateMaxDuration() != 0) {

			final Event firstSequenceEvent = segmentStart.getSequence().getEvents().get(0);
			final ZonedDateTime vesselStart = newAvailability.getStartAfterAsDateTime();
			final ZonedDateTime sequenceStart = firstSequenceEvent.getStart();

			final int hours = Hours.between(sequenceStart, vesselStart);
			if (hours > 0) {
				if (oldAvailability.getCharterOrDelegateMinDuration() != 0) {
					int minDurationInHours = oldAvailability.getCharterOrDelegateMinDuration() * 24;
					minDurationInHours -= hours;
					if (minDurationInHours < 0) {
						minDurationInHours = 0;
					}
					newAvailability.setMinDuration(minDurationInHours / 24);
				}
				if (oldAvailability.getCharterOrDelegateMaxDuration() != 0) {
					int maxDurationInHours = oldAvailability.getCharterOrDelegateMaxDuration() * 24;
					maxDurationInHours -= hours;
					if (maxDurationInHours < 0) {
						maxDurationInHours = 0;
					}
					newAvailability.setMaxDuration(maxDurationInHours / 24);
				}
			}

		}
	};

	public Pair<Pair<ScheduleSpecification, ExtraDataProvider>, Pair<ScheduleSpecification, ExtraDataProvider>> generateScheduleSpecifications(final LNGScenarioModel scenarioModel,
			final ScheduleModel baseSchedule, final ScheduleModel targetSchedule, final ChangeDescription changeDescription) {

		// Maybe do this in two passes, old then new

		// final Schedule schedule;
		// Map Slot (key)to SlotAllocation, CargoAllocation, OpenAllocation.

		final Set<CargoAllocation> baseCargoAllocations = new LinkedHashSet<>();
		final Set<VesselEventVisit> baseVesselEventVisits = new LinkedHashSet<>();
		final Set<OpenSlotAllocation> baseOpenSlotAllocations = new LinkedHashSet<>();

		final Set<CargoAllocation> targetCargoAllocations = new LinkedHashSet<>();
		final Set<VesselEventVisit> targetVesselEventVisits = new LinkedHashSet<>();
		final Set<OpenSlotAllocation> targetOpenSlotAllocations = new LinkedHashSet<>();

		final Function<String, EObject> baseFinderFunction = buildFinderFunction(baseSchedule.getSchedule());
		final Function<String, EObject> targetFinderFunction = buildFinderFunction(targetSchedule.getSchedule());

		for (final Change change : changeDescription.getChanges()) {
			if (change instanceof CargoChange) {
				final CargoChange cargoChange = (CargoChange) change;
				for (final SlotDescriptor descriptor : cargoChange.getSlotDescriptors()) {
					if (descriptor instanceof RealSlotDescriptor) {
						final RealSlotDescriptor d = (RealSlotDescriptor) descriptor;
						final SlotType slotType = d.getSlotType();
						final String key = PREFIX_SLOT + slotType + "-" + d.getSlotName();
						fileObject(baseFinderFunction.apply(key), baseCargoAllocations, baseVesselEventVisits, baseOpenSlotAllocations);
						fileObject(targetFinderFunction.apply(key), targetCargoAllocations, targetVesselEventVisits, targetOpenSlotAllocations);
					} else if (descriptor instanceof SpotMarketSlotDescriptor) {
						final SpotMarketSlotDescriptor d = (SpotMarketSlotDescriptor) descriptor;
						final SlotType slotType = d.getSlotType();
						final String key = PREFIX_MARKET + slotType + "-" + d.getMarketName() + String.format("%04d-%02d", d.getDate().getYear(), d.getDate().getMonthValue());
						fileObject(baseFinderFunction.apply(key), baseCargoAllocations, baseVesselEventVisits, baseOpenSlotAllocations);
						fileObject(targetFinderFunction.apply(key), targetCargoAllocations, targetVesselEventVisits, targetOpenSlotAllocations);
					} else {
						assert false;
					}
				}
			} else if (change instanceof VesselEventChange) {
				final VesselEventChange vesselEventChange = (VesselEventChange) change;
				final String key = "event-" + vesselEventChange.getVesselEventDescriptor().getEventName();
				fileObject(baseFinderFunction.apply(key), baseCargoAllocations, baseVesselEventVisits, baseOpenSlotAllocations);
				fileObject(targetFinderFunction.apply(key), targetCargoAllocations, targetVesselEventVisits, targetOpenSlotAllocations);
			} else if (change instanceof OpenSlotChange) {
				final OpenSlotChange openSlotChange = (OpenSlotChange) change;
				final RealSlotDescriptor d = (RealSlotDescriptor) openSlotChange.getSlotDescriptor();
				final SlotType slotType = d.getSlotType();
				final String key = PREFIX_SLOT + slotType + "-" + d.getSlotName();
				fileObject(baseFinderFunction.apply(key), baseCargoAllocations, baseVesselEventVisits, baseOpenSlotAllocations);
				fileObject(targetFinderFunction.apply(key), targetCargoAllocations, targetVesselEventVisits, targetOpenSlotAllocations);
			}
		}

		// Now construct new data model
		return new Pair<>(buildScheduleSpecification(baseCargoAllocations, baseVesselEventVisits, baseOpenSlotAllocations), //
				buildScheduleSpecification(targetCargoAllocations, targetVesselEventVisits, targetOpenSlotAllocations));

	}

	private Function<String, EObject> buildFinderFunction(final Schedule schedule) {

		final Map<String, EObject> slotMap = new HashMap<>();
		final Map<String, List<SlotAllocation>> marketSlotMap = new HashMap<>();
		final Map<String, VesselEventVisit> eventMap = new HashMap<>();

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			final Slot slot = openSlotAllocation.getSlot();
			final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
			final String key = PREFIX_SLOT + slotType + "-" + slot.getName();
			slotMap.put(key, openSlotAllocation);
		}
		for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
			final Slot slot = slotAllocation.getSlot();
			if (slot instanceof SpotSlot) {
				final SpotSlot spotSlot = (SpotSlot) slot;
				final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
				final String key = PREFIX_MARKET + slotType + "-" + spotSlot.getMarket().getName() + String.format("%04d-%02d", slot.getWindowStart().getYear(), slot.getWindowStart().getMonthValue());
				marketSlotMap.computeIfAbsent(key, k -> new LinkedList<>()).add(slotAllocation);
			} else {

				final SlotType slotType = ScheduleSpecificationHelper.getSlotType(slot);
				final String key = PREFIX_SLOT + slotType + "-" + slot.getName();
				slotMap.put(key, slotAllocation);
			}
		}
		for (final Sequence seq : schedule.getSequences()) {
			for (final Event evt : seq.getEvents()) {
				if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) evt;
					eventMap.put("event-" + vev.getVesselEvent().getName(), vev);
				}
			}
		}

		return (key) -> {
			if (key.startsWith("event-")) {
				return eventMap.get(key);
			} else if (key.startsWith(PREFIX_SLOT)) {
				return slotMap.get(key);
			} else if (key.startsWith(PREFIX_MARKET)) {
				// What to do here? Just ignore for now I think
				// return marketSlotMap.get(key);
			}

			return null;
		};
	}

	private void fileObject(final EObject obj, final Set<CargoAllocation> targetCargoAllocations, final Set<VesselEventVisit> targetVesselEventVisits,
			final Set<OpenSlotAllocation> targetOpenSlotAllocations) {
		if (obj == null) {
			return;
		}
		if (obj instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) obj;
			targetVesselEventVisits.add(vesselEventVisit);
		} else if (obj instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) obj;
			targetOpenSlotAllocations.add(openSlotAllocation);
		} else if (obj instanceof SlotAllocation) {
			final SlotAllocation slotAllocation = (SlotAllocation) obj;
			targetCargoAllocations.add(slotAllocation.getCargoAllocation());
		} else {
			throw new IllegalArgumentException();
		}
	}

	private Pair<ScheduleSpecification, ExtraDataProvider> buildScheduleSpecification(final Set<CargoAllocation> targetCargoAllocations, final Set<VesselEventVisit> targetVesselEventVisits,
			final Set<OpenSlotAllocation> targetOpenSlotAllocations) {

		final ScheduleSpecification scheduleSpecification = CargoFactory.eINSTANCE.createScheduleSpecification();
		final List<VesselAvailability> newAvailabilities = new LinkedList<>();
		final List<CharterInMarketOverride> newCharterInMarketOverrides = new LinkedList<>();

		final Map<VesselAvailability, List<Pair<EventGrouping, Integer>>> oldAvailabilityToList = new LinkedHashMap<>();
		final Map<Pair<CharterInMarket, Integer>, List<Pair<EventGrouping, Integer>>> oldMarketToList = new LinkedHashMap<>();

		for (final CargoAllocation cargoAllocation : targetCargoAllocations) {
			if (cargoAllocation.getCargoType() == CargoType.FOB || cargoAllocation.getCargoType() == CargoType.DES) {
				final NonShippedCargoSpecification cargoSpec = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
					slotSpec.setSlot(slotAllocation.getSlot());
					cargoSpec.getSlotSpecifications().add(slotSpec);
				}
				scheduleSpecification.getNonShippedCargoSpecifications().add(cargoSpec);
			} else {
				final VesselScheduleSpecification vesselSpec = CargoFactory.eINSTANCE.createVesselScheduleSpecification();

				final Sequence sequence = cargoAllocation.getSequence();
				if (sequence.getCharterInMarket() != null) {

					// TODO: What if there are multiple cargoes on the vessel? Need to consider!
					if (sequence.getSpotIndex() != -1) {
						final int seqHint = 1 + cargoAllocation.getSequence().getEvents().indexOf(cargoAllocation.getSlotAllocations().get(0).getSlotVisit());
						oldMarketToList.computeIfAbsent(new Pair<>(sequence.getCharterInMarket(), sequence.getSpotIndex()), k -> new LinkedList<>()).add(new Pair<>(cargoAllocation, seqHint));
						continue;
					}
					// Nominals
					vesselSpec.setVesselAllocation(sequence.getCharterInMarket());
					vesselSpec.setSpotIndex(sequence.getSpotIndex());

					for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
						final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
						slotSpec.setSlot(slotAllocation.getSlot());
						vesselSpec.getEvents().add(slotSpec);
					}
					scheduleSpecification.getVesselScheduleSpecifications().add(vesselSpec);
					continue;

				} else if (sequence.getVesselAvailability() != null) {
					// TODO: Share with vessel event code
					final VesselAvailability oldAvailability = sequence.getVesselAvailability();

					final int seqHint = 1 + cargoAllocation.getSequence().getEvents().indexOf(cargoAllocation.getSlotAllocations().get(0).getSlotVisit());
					oldAvailabilityToList.computeIfAbsent(oldAvailability, k -> new LinkedList<>()).add(new Pair<>(cargoAllocation, seqHint));
				} else {
					assert false;
				}
			}
		}
		// Do vessel events
		for (final VesselEventVisit vesselEventVisit : targetVesselEventVisits) {
			final Sequence sequence = vesselEventVisit.getSequence();
			if (sequence.getCharterInMarket() != null) {

				// TODO: What if there are multiple cargoes on the vessel? Need to consider!
				if (sequence.getSpotIndex() != -1) {
					final int seqHint = 1 + vesselEventVisit.getSequence().getEvents().indexOf(vesselEventVisit);
					oldMarketToList.computeIfAbsent(new Pair<>(sequence.getCharterInMarket(), sequence.getSpotIndex()), k -> new LinkedList<>()).add(new Pair<>(vesselEventVisit, seqHint));
					continue;
				}
			} else if (sequence.getVesselAvailability() != null) {
				// TODO: Share with vessel event code
				final VesselAvailability oldAvailability = sequence.getVesselAvailability();

				final int seqHint = 1 + vesselEventVisit.getSequence().getEvents().indexOf(vesselEventVisit);
				oldAvailabilityToList.computeIfAbsent(oldAvailability, k -> new LinkedList<>()).add(new Pair<>(vesselEventVisit, seqHint));
			} else {
				assert false;
			}
		}

		// Break up into consecutive chunks
		final Map<VesselAvailability, List<List<Pair<EventGrouping, Integer>>>> oldAvailabilityToSplitList = new LinkedHashMap<>();
		for (final Map.Entry<VesselAvailability, List<Pair<EventGrouping, Integer>>> e : oldAvailabilityToList.entrySet()) {

			final List<Pair<EventGrouping, Integer>> sortedEvents = new ArrayList<>(e.getValue());
			Collections.sort(sortedEvents, (a, b) -> Integer.compare(a.getSecond(), b.getSecond()));

			int lastIndex = -1;
			List<Pair<EventGrouping, Integer>> currentList = new LinkedList<>();
			for (final Pair<EventGrouping, Integer> p : sortedEvents) {
				if (lastIndex != -1 && p.getSecond() != lastIndex + 1) {
					oldAvailabilityToSplitList.computeIfAbsent(e.getKey(), k -> new LinkedList<>()).add(currentList);
					currentList = new LinkedList<>();
					lastIndex = -1;
				}
				currentList.add(p);
				lastIndex = p.getSecond();
			}
			if (!currentList.isEmpty()) {
				oldAvailabilityToSplitList.computeIfAbsent(e.getKey(), k -> new LinkedList<>()).add(currentList);
			}
		}

		// Break up into consecutive chunks
		final Map<Pair<CharterInMarket, Integer>, List<List<Pair<EventGrouping, Integer>>>> oldMarketToSplitList = new LinkedHashMap<>();
		for (final Map.Entry<Pair<CharterInMarket, Integer>, List<Pair<EventGrouping, Integer>>> e : oldMarketToList.entrySet()) {

			final List<Pair<EventGrouping, Integer>> sortedEvents = new ArrayList<>(e.getValue());
			Collections.sort(sortedEvents, (a, b) -> Integer.compare(a.getSecond(), b.getSecond()));

			int lastIndex = -1;
			List<Pair<EventGrouping, Integer>> currentList = new LinkedList<>();
			for (final Pair<EventGrouping, Integer> p : sortedEvents) {
				if (lastIndex != -1 && p.getSecond() != lastIndex + 1) {
					oldMarketToSplitList.computeIfAbsent(e.getKey(), k -> new LinkedList<>()).add(currentList);
					currentList = new LinkedList<>();
					lastIndex = -1;
				}
				currentList.add(p);
				lastIndex = p.getSecond();
			}
			if (!currentList.isEmpty()) {
				oldMarketToSplitList.computeIfAbsent(e.getKey(), k -> new LinkedList<>()).add(currentList);
			}
		}

		buildAvailabilities(scheduleSpecification, newAvailabilities, oldAvailabilityToSplitList, buildVesselAvailability, processFirstEvent_VesselAvailability,
				processLastSegmentEvent_VesselAvailability, processLastSequenceEvent_VesselAvailability);
		buildAvailabilities(scheduleSpecification, newCharterInMarketOverrides, oldMarketToSplitList, buildMarketAvailability, processFirstEvent_MarketAvailability,
				processLastSegmentEvent_MarketAvailability, processLastSequenceEvent_MarketAvailability);

		for (final OpenSlotAllocation openSlotAllocation : targetOpenSlotAllocations) {
			final SlotSpecification spec = CargoFactory.eINSTANCE.createSlotSpecification();
			spec.setSlot(openSlotAllocation.getSlot());
			scheduleSpecification.getOpenEvents().add(spec);
		}

		return new Pair<>(scheduleSpecification, new ExtraDataProvider(newAvailabilities, null, newCharterInMarketOverrides, null, null, null, null));
	}

	private <K, V extends VesselAssignmentType> void buildAvailabilities(final ScheduleSpecification scheduleSpecification, final List<V> newAvailabilities,
			final Map<K, List<List<Pair<EventGrouping, Integer>>>> data, final Function<K, V> initAvailability, final BiConsumer<V, PortVisit> processFirstEvent,
			final BiConsumer<V, PortVisit> processLastSegmentEvent, final TriConsumer<K, V, PortVisit> processLastSequenceEvent) {
		for (final Map.Entry<K, List<List<Pair<EventGrouping, Integer>>>> e : data.entrySet()) {

			final K oldAvailability = e.getKey();

			for (final List<Pair<EventGrouping, Integer>> sortedEvents : e.getValue()) {

				final VesselScheduleSpecification vesselSpec = CargoFactory.eINSTANCE.createVesselScheduleSpecification();
				final V newAvailability = initAvailability.apply(oldAvailability);

				boolean firstEvent = true;
				EventGrouping lastEventGrouping = null;
				PortVisit segmentStart = null;
				for (final Pair<EventGrouping, Integer> p : sortedEvents) {
					final EventGrouping eventGrouping = p.getFirst();
					if (firstEvent) {
						// Start conditions
						final PortVisit visit = (PortVisit) eventGrouping.getEvents().get(0);
						segmentStart = visit;
						processFirstEvent.accept(newAvailability, visit);
						// TODO: Calculate total time of preceeding events
						firstEvent = false;
					}

					if (eventGrouping instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) eventGrouping;

						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							final SlotSpecification slotSpec = CargoFactory.eINSTANCE.createSlotSpecification();
							slotSpec.setSlot(slotAllocation.getSlot());
							vesselSpec.getEvents().add(slotSpec);
						}
					} else {
						for (final Event event : eventGrouping.getEvents()) {
							if (event instanceof VesselEventVisit) {
								final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
								final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
								final VesselEventSpecification vesselEventSpecification = CargoFactory.eINSTANCE.createVesselEventSpecification();
								vesselEventSpecification.setVesselEvent(vesselEvent);
								vesselSpec.getEvents().add(vesselEventSpecification);
								break;
							}
						}
					}

					lastEventGrouping = eventGrouping;
				}

				if (lastEventGrouping != null) {
					final EventGrouping eventGrouping = lastEventGrouping;

					final Event lastEvent = eventGrouping.getEvents().get(eventGrouping.getEvents().size() - 1);
					final boolean isLastEventInSequence = lastEvent.getNextEvent() instanceof EndEvent;

					// End conditions
					if (isLastEventInSequence) {
						processLastSequenceEvent.accept(oldAvailability, newAvailability, segmentStart);

					} else {
						final PortVisit visit = (PortVisit) eventGrouping.getEvents().get(eventGrouping.getEvents().size() - 1).getNextEvent();
						// Need to be careful here...
						processLastSegmentEvent.accept(newAvailability, visit);
					}
				}

				vesselSpec.setVesselAllocation(newAvailability);
				newAvailabilities.add(newAvailability);
				scheduleSpecification.getVesselScheduleSpecifications().add(vesselSpec);
			}
		}
	}

}
