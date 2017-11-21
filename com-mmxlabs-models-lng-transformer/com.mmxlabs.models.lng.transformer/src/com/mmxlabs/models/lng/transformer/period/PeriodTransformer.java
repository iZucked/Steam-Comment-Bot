/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.InclusionType;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.Position;
import com.mmxlabs.models.lng.transformer.period.extensions.IPeriodTransformerExtension;
import com.mmxlabs.models.lng.transformer.util.LNGScenarioUtils;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;

/***
 * TODO
 * 
 * Break up for unit tests!
 * 
 * Update for extension points
 * 
 * Update for multiple availabilities
 * 
 * Update for end conditions
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class PeriodTransformer {

	private static final @NonNull ZoneId ZONEID_UTC = ZoneId.of("UTC");
	private static final int NOMINAL_INDEX = -1;
	@Inject(optional = true)
	private Iterable<IPeriodTransformerExtension> extensions;

	public PeriodTransformer() {
		injectExtensions();
	}

	private void injectExtensions() {

		Injector injector = null;
		if (Platform.isRunning()) {
			final AbstractModule m = new AbstractModule() {

				@Override
				protected void configure() {
					install(Peaberry.osgiModule(FrameworkUtil.getBundle(PeriodTransformer.class).getBundleContext()));
					bind(TypeLiterals.iterable(IPeriodTransformerExtension.class)).toProvider(Peaberry.service(IPeriodTransformerExtension.class).multiple());
				}
			};
			injector = Guice.createInjector(m);
		}
		if (injector != null) {
			injector.injectMembers(this);
		}
	}

	@Inject
	@NonNull
	private InclusionChecker inclusionChecker;

	@NonNull
	public NonNullPair<LNGScenarioModel, EditingDomain> transform(@NonNull final LNGScenarioModel wholeScenario, @NonNull final UserSettings userSettings,
			@NonNull final IScenarioEntityMapping mapping) {
		final PeriodRecord periodRecord = createPeriodRecord(userSettings, wholeScenario);
		return transform(wholeScenario, periodRecord, mapping);
	}

	@NonNull
	public PeriodRecord createPeriodRecord(@NonNull final UserSettings userSettings, final LNGScenarioModel scenario) {

		final PeriodRecord periodRecord = new PeriodRecord();

		if (userSettings.getPeriodStartDate() == null && userSettings.getPeriodEnd() == null) {
			return periodRecord;
		}

		final LocalDate startDate = userSettings.getPeriodStartDate();
		final YearMonth endDate = userSettings.getPeriodEnd();

		final int boundaryFlexInMonths = 1;

		// Get dates with flex
		if (startDate != null) {
			final ZonedDateTime lowerBoundary = startDate.atStartOfDay(ZONEID_UTC);
			periodRecord.lowerBoundary = lowerBoundary;
			periodRecord.lowerCutoff = lowerBoundary.minusMonths(boundaryFlexInMonths);
		}

		if (endDate != null) {
			final ZonedDateTime upperBoundary = endDate.atDay(1).atStartOfDay(ZONEID_UTC);
			periodRecord.upperBoundary = upperBoundary;
			periodRecord.upperCutoff = upperBoundary.plusMonths(boundaryFlexInMonths);
		}

		if (scenario != null) {
			periodRecord.promptStart = scenario.getPromptPeriodStart();
			periodRecord.promptEnd = scenario.getPromptPeriodEnd();
		}

		return periodRecord;
	}

	@NonNull
	public NonNullPair<LNGScenarioModel, EditingDomain> transform(@NonNull final LNGScenarioModel wholeScenario, @NonNull final PeriodRecord periodRecord,
			@NonNull final IScenarioEntityMapping mapping) {
		// assert - passed validation

		// Take a copy to manipulate.
		final LNGScenarioModel output = copyScenario(wholeScenario, mapping);

		// Do not allow the prompt period to extend past the optimisation period
		if (periodRecord.upperBoundary != null && periodRecord.promptEnd != null) {

			if (periodRecord.upperBoundary.isBefore(periodRecord.promptEnd.atStartOfDay(ZONEID_UTC))) {
				output.setPromptPeriodEnd(periodRecord.upperBoundary.toLocalDate());
			}
		}
		// Evaluate copy!
		final EditingDomain internalDomain = createEditingDomain(output);

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(output);
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(output);
		final PortModel portModel = ScenarioModelUtil.getPortModel(output);

		// Init extensions
		for (final IPeriodTransformerExtension extension : extensions) {
			extension.init(cargoModel, ScenarioModelUtil.getScheduleModel(output).getSchedule());
		}

		// Generate the schedule map - maps cargoes and events to schedule information for date, port and heel data extraction
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		generateObjectToPortVisitMap(output, objectToPortVisitMap);

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		generateStartAndEndConditionsMap(output, startConditionMap, endConditionMap);

		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();
		generateSlotAllocationMap(output, slotAllocationMap);

		// final Map<Cargo, CargoAllocation> fullMap = originalScenario.getScheduleModel().getSchedule().getCargoAllocations().stream() //
		// .collect(Collectors.toMap(CargoAllocation::getInputCargo, Function.identity()));

		final Map<VesselAvailability, Event> map = output.getScheduleModel().getSchedule().getSequences().stream() //
				.filter(s -> s.getVesselAvailability() != null) //
				.collect(Collectors.toMap(Sequence::getVesselAvailability, s -> s.getEvents().get(s.getEvents().size() - 1)));

		// Extend the vessel end date to cover late ending if present in input scenario
		for (final Sequence seq : output.getScheduleModel().getSchedule().getSequences()) {
			final VesselAvailability va = seq.getVesselAvailability();
			if (va != null) {
				// Do we have an end date set?
				if (va.isSetEndBy() && seq.getEvents().size() > 0) {
					final ZonedDateTime endDate = va.getEndByAsDateTime();
					final Event endEvent = seq.getEvents().get(seq.getEvents().size() - 1);
					// Does the vessel end late?
					if (endEvent.getEnd().isAfter(endDate)) {
						// Is the required vessel end date in or before the boundary?
						if (periodRecord.lowerBoundary != null) {
							if (periodRecord.lowerBoundary.isAfter(endDate)) {
								// Is the last event (slot or vessel event) "locked"? (OUT or BOUNDARY)
								Event evt = endEvent.getPreviousEvent();
								while (evt != null && !(evt instanceof PortVisit)) {
									evt = evt.getPreviousEvent();
								}
								if (evt instanceof PortVisit) {
									final PortVisit portVisit = (PortVisit) evt;
									if (inclusionChecker.getObjectInVesselAvailabilityRange(portVisit, va) == InclusionType.Out) {
										// Change the vessel availability end date to match exported end date
										va.setEndBy(endEvent.getEnd().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
									}
								}
							}
						}
					}
				}
			}
		}

		// Set initial end conditions - if open, use the evaluated end date to keep long ballast leg P&L the same
		for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
			final EndEvent endEvent = (EndEvent) map.get(vesselAvailability);
			if (endEvent != null) {
				if (!vesselAvailability.isSetEndAfter() && !vesselAvailability.isSetEndBy()) {
					if (output.isSetSchedulingEndDate() && periodRecord.upperBoundary != null && output.getSchedulingEndDate().isBefore(periodRecord.upperBoundary.toLocalDate())) {
						vesselAvailability.setEndAfter(output.getSchedulingEndDate().atStartOfDay());
						vesselAvailability.setEndBy(periodRecord.upperBoundary.toLocalDateTime());
					} else {
						vesselAvailability.setEndAfter(endEvent.getEnd().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
						vesselAvailability.setEndBy(endEvent.getEnd().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
					}

					vesselAvailability.setForceHireCostOnlyEndRule(true);
				} else if (vesselAvailability.isSetEndAfter()) {
					if (output.isSetSchedulingEndDate() && periodRecord.upperBoundary != null && output.getSchedulingEndDate().atStartOfDay().isBefore(vesselAvailability.getEndAfter())) {
						if (vesselAvailability.getEndAfter().isAfter(output.getSchedulingEndDate().atStartOfDay())) {
							vesselAvailability.setEndAfter(output.getSchedulingEndDate().atStartOfDay());
							vesselAvailability.setForceHireCostOnlyEndRule(true);
						}
					}
				}
				if (!vesselAvailability.getEndAt().isEmpty()) {
					vesselAvailability.getEndAt().add(endEvent.getPort());
				}
			}
		}

		// List of new vessel availabilities for cargoes outside normal range
		final List<VesselAvailability> newVesselAvailabilities = new LinkedList<>();
		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> slotsToRemove = new HashSet<>();
		final Set<Cargo> cargoesToRemove = new HashSet<>();

		final Set<Cargo> lockedCargoes = new HashSet<>();
		final Set<Slot> lockedSlots = new HashSet<>();

		// Filter out slots and cargoes, create new availabilities for special cases.
		findSlotsAndCargoesToRemove(internalDomain, periodRecord, cargoModel, seenSlots, slotsToRemove, cargoesToRemove, slotAllocationMap, objectToPortVisitMap, lockedCargoes, lockedSlots);
		final Triple<Set<Cargo>, Set<Event>, Set<VesselEvent>> eventDependencies = findVesselEventsToRemoveAndDependencies(output.getScheduleModel().getSchedule(), periodRecord, cargoModel,
				objectToPortVisitMap);
		updateSlotsToRemoveWithDependencies(slotAllocationMap, slotsToRemove, cargoesToRemove, eventDependencies.getFirst(), lockedCargoes);

		// Update vessel availabilities
		updateVesselAvailabilities(periodRecord, cargoModel, spotMarketsModel, portModel, startConditionMap, endConditionMap, eventDependencies.getFirst(), eventDependencies.getSecond(),
				objectToPortVisitMap, mapping);
		checkIfRemovedSlotsAreStillNeeded(seenSlots, slotsToRemove, cargoesToRemove, newVesselAvailabilities, startConditionMap, endConditionMap, slotAllocationMap, lockedCargoes);

		if (extensions != null) {
			for (final IPeriodTransformerExtension ext : extensions) {
				ext.processSlotInclusionsAndExclusions(cargoModel, output.getScheduleModel().getSchedule(), slotsToRemove, cargoesToRemove);
			}
		}

		final List<Pair<CharterInMarket, Integer>> spotCharterUse = getSpotCharterInUseForCargoes(cargoesToRemove);
		updateSpotCharterMarkets(mapping, spotCharterUse, cargoModel, spotMarketsModel, cargoesToRemove);

		removeExcludedSlotsAndCargoes(internalDomain, mapping, slotsToRemove, cargoesToRemove);
		// Some slots are brought in from outside the period, make sure they are locked
		lockOpenSlots(output.getCargoModel(), periodRecord, objectToPortVisitMap);

		// Sort out Canal bookings
		lockAndRemoveCanalBookings(slotsToRemove, cargoesToRemove, lockedSlots, lockedCargoes, slotAllocationMap, output);

		// TODO: We can probably get rid of this now -- need some unit tests to verify

		// TEMP HACK UNTIL MULTIPLE AVAILABILITES PROPERLY IN PLACE AND filterSlotsAndCargoes can properly handle this.
		for (final VesselAvailability newVA : newVesselAvailabilities) {
			for (final VesselAvailability vesselAvailability : wholeScenario.getCargoModel().getVesselAvailabilities()) {
				if (newVA.getVessel() == mapping.getCopyFromOriginal(vesselAvailability.getVessel())) {
					newVA.setEntity(mapping.getCopyFromOriginal(vesselAvailability.getEntity()));
					if (vesselAvailability.isSetTimeCharterRate()) {
						newVA.setTimeCharterRate(vesselAvailability.getTimeCharterRate());
					}
				}
			}
		}
		// Filter out vessel events
		filterVesselEvents(internalDomain, eventDependencies.getThird(), cargoModel, mapping, periodRecord, objectToPortVisitMap);

		// Filter out vessels
		filterVesselAvailabilities(internalDomain, periodRecord, cargoModel, mapping, objectToPortVisitMap);

		output.getCargoModel().getVesselAvailabilities().addAll(newVesselAvailabilities);

		trimSpotMarketCurves(periodRecord, output);

		// Remove schedule model
		output.getScheduleModel().setSchedule(null);

		// Clear this date as we have fixed everything and it will conflict with rules in schedule transformer.
		output.unsetSchedulingEndDate();

		return new NonNullPair<>(output, internalDomain);
	}

	private void lockAndRemoveCanalBookings(final Set<Slot> slotsToRemove, final Set<Cargo> cargoesToRemove, final Set<Slot> lockedSlots, final Set<Cargo> lockedCargoes,
			final Map<Slot, SlotAllocation> slotAllocationMap, final LNGScenarioModel output) {
		final CargoModel cargoModel = output.getCargoModel();
		final CanalBookings canalBookings = cargoModel.getCanalBookings();
		if (canalBookings != null) {
			// data structures
			final Map<Slot, CanalBookingSlot> slotToCanalBooking = new HashMap<>();
			final Map<Slot, Slot> slotToNextSlot = new HashMap<>();

			final Map<Slot, CanalBookingSlot> preBookedBookings = new HashMap<>();
			final Map<Slot, CanalBookingSlot> bookingToLock = new HashMap<>();
			final Set<CanalBookingSlot> bookingToRemove = new HashSet<>();

			final Set<Slot> allSlotsRemoved = new HashSet<>(slotsToRemove);
			allSlotsRemoved.addAll(cargoesToRemove.stream().map(c -> c.getSortedSlots()).flatMap(c -> c.stream()).collect(Collectors.toSet()));
			final Set<Slot> allSlotsLocked = new HashSet<>(lockedSlots);
			allSlotsLocked.addAll(lockedCargoes.stream().map(c -> c.getSortedSlots()).flatMap(c -> c.stream()).collect(Collectors.toSet()));

			for (final CanalBookingSlot booking : canalBookings.getCanalBookingSlots()) {
				if (booking.getSlot() != null) {
					preBookedBookings.put(booking.getSlot(), booking);
				}
			}

			for (final Slot slot : allSlotsRemoved) {
				final SlotAllocation slotAllocation = slotAllocationMap.get(slot);
				if (slotAllocation != null) {
					final SlotVisit slotVisit = slotAllocation.getSlotVisit();
					if (slotVisit != null) {
						if (slotVisit.getNextEvent() instanceof Journey) {
							final Journey journey = (Journey) slotVisit.getNextEvent();
							if (journey.getCanalBooking() != null) {
								bookingToRemove.add(journey.getCanalBooking());
							}
						}
					}
				}
				if (preBookedBookings.get(slot) != null) {
					bookingToRemove.add(preBookedBookings.get(slot));
				}
			}

			for (final Slot slot : allSlotsLocked) {
				final SlotAllocation slotAllocation = slotAllocationMap.get(slot);
				if (slotAllocation != null) {
					final SlotVisit slotVisit = slotAllocation.getSlotVisit();
					if (slotVisit != null) {
						if (slotVisit.getNextEvent() instanceof Journey) {
							final Journey journey = (Journey) slotVisit.getNextEvent();
							if (journey.getCanalBooking() != null) {
								Slot nextSlot = null;
								Event nextEvent = slotVisit.getNextEvent();
								while (nextEvent.getNextEvent() != null) {
									if (nextEvent instanceof SlotVisit) {
										nextSlot = ((SlotVisit) nextEvent).getSlotAllocation().getSlot();
										break;
									}
									nextEvent = nextEvent.getNextEvent();
								}
								if (nextSlot != null && allSlotsLocked.contains(nextSlot)) {
									bookingToLock.put(slot, journey.getCanalBooking());
									if (preBookedBookings.get(slot) != null && preBookedBookings.get(slot) != journey.getCanalBooking()) {
										bookingToRemove.add(preBookedBookings.get(slot));
									}
								}
							}
						}
					}
				}
			}

			for (final CanalBookingSlot bookingSlot : bookingToRemove) {
				canalBookings.getCanalBookingSlots().remove(bookingSlot);
			}

			for (final Entry<Slot, CanalBookingSlot> bookingSlot : bookingToLock.entrySet()) {
				bookingSlot.getValue().setSlot(bookingSlot.getKey());
			}
		}
	}

	/**
	 * For spot charter markets where all cargoes on a used charter option are outside of the period we reduce the spot charter in count. As spot charter are index from 0 to spot count, we need to
	 * maintain a mapping between the original scenario spot index and the period scenario spot index to avoid issues with merging on the period export.
	 * 
	 * @param periodMapping
	 * @param spotCharterUse
	 * @param cargoModel
	 * @param spotMarketsModel
	 */
	private void updateSpotCharterMarkets(final IScenarioEntityMapping periodMapping, final List<Pair<CharterInMarket, Integer>> spotCharterUse, final CargoModel cargoModel,
			final SpotMarketsModel spotMarketsModel, final Collection<Cargo> cargoesToRemove) {
		// Generate the list of all spot charter ins used by all the cargoes in the scenario. An option may appear multiple times.
		final List<Pair<CharterInMarket, Integer>> total = getSpotCharterInUseForCargoes(cargoModel.getCargoes());
		// Convert the list into an accumulated map count.

		final Map<Pair<CharterInMarket, Integer>, Long> counter_jav8 = total.stream() //
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		final Map<Pair<CharterInMarket, Integer>, Long> counter = new HashMap<>();
		// Accumulate total charter use
		for (final Pair<CharterInMarket, Integer> p : total) {
			long t = 0;
			if (counter.containsKey(p)) {
				t = counter.get(p);
			}
			counter.put(p, t + 1);
		}
		assert counter.equals(counter_jav8);

		//// Next - determine which charter in vessels are no longer used (all allocated cargoes are outside of the period)

		// Mapping of charter market to spot index mapping
		final Map<CharterInMarket, int[]> mapping = new HashMap<>();
		final List<Pair<CharterInMarket, Integer>> chartersToRemove = new LinkedList<>();
		for (final Pair<CharterInMarket, Integer> p : spotCharterUse) {
			final int t = (int) (counter.get(p) - 1);
			if (t == 0) {
				chartersToRemove.add(p);
				// Initialise the mapping array
				final int[] m = new int[p.getFirst().getSpotCharterCount()];
				for (int i = 0; i < m.length; ++i) {
					m[i] = i;
				}
				mapping.put(p.getFirst(), m);
			}
			assert t >= 0;
			counter.put(p, (long) t);
		}

		// Remove the spot charter in options and update the mapping arrays
		for (final Pair<CharterInMarket, Integer> p : chartersToRemove) {
			final int spotCharterCount = p.getFirst().getSpotCharterCount();
			if (p.getSecond() >= 0) {
				assert spotCharterCount > 0;
				p.getFirst().setSpotCharterCount(spotCharterCount - 1);
				final int idx = p.getSecond();
				final int[] m = mapping.get(p.getFirst());
				// Blank out mapping
				m[idx] = NOMINAL_INDEX;
				for (int i = idx; i < m.length; ++i) {
					if (m[i] >= 0) {
						m[i]--;
					}
				}
			}
		}

		// Store the mapping data in the periodMapping
		for (final Map.Entry<CharterInMarket, int[]> e : mapping.entrySet()) {
			final int[] m = e.getValue();
			for (int i = 0; i < m.length; ++i) {
				if (m[i] != NOMINAL_INDEX) {
					periodMapping.setSpotCharterInMapping(e.getKey(), i, m[i]);
				}
			}
		}

		// Finally update the spot index on the cargoes.
		for (final Cargo cargo : cargoModel.getCargoes()) {
			// Skip this cargo as it will be removed.
			if (cargoesToRemove.contains(cargo)) {
				continue;
			}

			final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				final int spotIndex = cargo.getSpotIndex();
				if (spotIndex < 0) {
					continue;
				}
				final int[] m = mapping.get(charterInMarket);
				if (m == null) {
					continue;
				}
				cargo.setSpotIndex(m[spotIndex]);
			}
		}

	}

	@NonNull
	private List<Pair<CharterInMarket, Integer>> getSpotCharterInUseForCargoes(final Collection<Cargo> cargoesToRemove) {
		final List<Pair<CharterInMarket, Integer>> result = new LinkedList<>();
		for (final Cargo cargo : cargoesToRemove) {
			final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				final int spotIndex = cargo.getSpotIndex();
				final Pair<CharterInMarket, Integer> p = new Pair<>(charterInMarket, spotIndex);
				result.add(p);
			}
		}
		return result;

	}

	private void lockOpenSlots(@NonNull final CargoModel cargoModel, @NonNull final PeriodRecord periodRecord, @NonNull final Map<EObject, PortVisit> objectToPortVisitMap) {
		for (final List<Slot> slotList : new List[] { cargoModel.getLoadSlots(), cargoModel.getDischargeSlots() }) {
			for (final Slot slot : slotList) {
				if (inclusionChecker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord).getFirst() != InclusionType.In) {
					if (slot.getCargo() != null && isNominalInPrompt(slot.getCargo(), periodRecord)) {
						// break out if part of a nominal cargo
						continue;
					}
					slot.setLocked(true);
				}
			}

		}
	}

	private void updateSlotsToRemoveWithDependencies(@NonNull final Map<Slot, SlotAllocation> slotAllocationMap, @NonNull final Set<Slot> slotsToRemove, @NonNull final Set<Cargo> cargoesToRemove,
			@NonNull final Set<Cargo> cargoesToKeep, final Set<Cargo> lockedCargoes) {
		for (final Cargo cargo : cargoesToKeep) {
			if (cargoesToRemove.contains(cargo)) {
				lockDownCargoDates(slotAllocationMap, cargo, lockedCargoes);
			}
			slotsToRemove.removeAll(cargo.getSlots());
			cargoesToRemove.remove(cargo);
		}
	}

	public void filterVesselAvailabilities(@NonNull final EditingDomain internalDomain, @NonNull final PeriodRecord periodRecord, @NonNull final CargoModel cargoModel,
			@NonNull final IScenarioEntityMapping mapping, @NonNull final Map<EObject, PortVisit> objectToPortVisitMap) {
		final Set<VesselAvailability> vesselsToRemove = new HashSet<>();

		for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
			assert vesselAvailability != null;
			if (inclusionChecker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord).getFirst() == InclusionType.Out) {
				inclusionChecker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord);
				vesselsToRemove.add(vesselAvailability);
				@Nullable
				final VesselAvailability originalFromCopy = mapping.getOriginalFromCopy(vesselAvailability);
				assert originalFromCopy != null; // We should not be null in the transformer
				mapping.registerRemovedOriginal(originalFromCopy);

			}
		}
		internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, vesselsToRemove));

	}

	public void filterVesselEvents(@NonNull final EditingDomain internalDomain, @NonNull final Set<VesselEvent> eventsToRemove, @NonNull final CargoModel cargoModel,
			@NonNull final IScenarioEntityMapping mapping, @NonNull final PeriodRecord periodRecord, @NonNull final Map<EObject, PortVisit> scheduledEventMap) {
		for (final VesselEvent event : cargoModel.getVesselEvents()) {
			if (event instanceof CharterOutEvent) {
				// If in boundary, limit available vessels to assigned vessel

				final CharterOutEvent charterOutEvent = (CharterOutEvent) event;
				@NonNull
				final NonNullPair<InclusionType, Position> p = inclusionChecker.getObjectInclusionType(event, scheduledEventMap, periodRecord);
				if (p.getFirst() != InclusionType.In) {
					charterOutEvent.getAllowedVessels().clear();
					final VesselAvailability vesselAvailability = ((VesselAvailability) charterOutEvent.getVesselAssignmentType());
					if (vesselAvailability != null) {
						charterOutEvent.getAllowedVessels().add(vesselAvailability.getVessel());
					} else {
						if (charterOutEvent.isOptional()) {
							eventsToRemove.add(event);
						}
					}
				}
			}
		}
		for (final VesselEvent event : eventsToRemove) {
			@Nullable
			final VesselEvent originalFromCopy = mapping.getOriginalFromCopy(event);
			assert originalFromCopy != null; // We should not be null in the transformer
			mapping.registerRemovedOriginal(originalFromCopy);
		}
		internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, eventsToRemove));
	}

	@NonNull
	public Triple<Set<Cargo>, Set<Event>, Set<VesselEvent>> findVesselEventsToRemoveAndDependencies(@NonNull final Schedule schedule, @NonNull final PeriodRecord periodRecord,
			@NonNull final CargoModel cargoModel, @NonNull final Map<EObject, PortVisit> objectToPortVisitMap) {
		final Set<VesselEvent> eventsToRemove = new HashSet<>();
		final Set<Event> eventsToKeep = new HashSet<>();
		final Set<Cargo> cargoesToKeep = new HashSet<>();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {
			boolean removed = false;
			if (inclusionChecker.getObjectInclusionType(event, objectToPortVisitMap, periodRecord).getFirst() == InclusionType.Out) {
				eventsToRemove.add(event);
				removed = true;
			}
			if (removed == false && (event instanceof DryDockEvent || event instanceof MaintenanceEvent)) {
				final Pair<Set<Cargo>, Set<Event>> eventsAndCargoesToKeep = getDependenciesForEvent(schedule, event);
				if (eventsAndCargoesToKeep != null) {
					cargoesToKeep.addAll(eventsAndCargoesToKeep.getFirst());
					eventsToKeep.addAll(eventsAndCargoesToKeep.getSecond());
				}
			}
		}
		// update events to remove
		eventsToRemove.removeAll(eventsToKeep);
		return new Triple<>(cargoesToKeep, eventsToKeep, eventsToRemove);
	}

	public void findSlotsAndCargoesToRemove(@NonNull final EditingDomain internalDomain, @NonNull final PeriodRecord periodRecord, @NonNull final CargoModel cargoModel,
			@NonNull final Set<Slot> seenSlots, @NonNull final Collection<Slot> slotsToRemove, @NonNull final Collection<Cargo> cargoesToRemove,
			@NonNull final Map<Slot, SlotAllocation> slotAllocationMap, @NonNull final Map<EObject, PortVisit> objectToPortVisitMap, final Set<Cargo> lockedCargoes, final Set<Slot> lockedSlots) {

		for (final Cargo cargo : cargoModel.getCargoes()) {
			assert cargo != null;
			final NonNullPair<InclusionType, Position> inclusionResult = inclusionChecker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord);
			final InclusionType inclusionType = inclusionResult.getFirst();
			final Position pos = inclusionResult.getSecond();

			if (inclusionType == InclusionType.Out) {
				cargoesToRemove.add(cargo);
				slotsToRemove.addAll(cargo.getSlots());
			} else if (inclusionType == InclusionType.Boundary) {
				if (pos == Position.Both || cargo.getVesselAssignmentType() == null) {
					// lock whole cargo if both slots are outside period or if there is no vessel
					lockDownCargoDates(slotAllocationMap, cargo, lockedCargoes);
				} else {
					// lock only one slot
					final NonNullPair<Slot, Slot> slots = inclusionChecker.getFirstAndLastSlots(cargo);
					if (pos == Position.After) {
						if (inclusionChecker.getObjectInclusionType(slots.getFirst(), objectToPortVisitMap, periodRecord).getFirst() == InclusionType.In) {
							if (!isNominalInPrompt(cargo, periodRecord)) {
								lockDownSlotDates(slotAllocationMap, slots.getSecond(), lockedSlots);
							}
						} else {
							lockDownCargoDates(slotAllocationMap, cargo, lockedCargoes);
						}
					} else {
						if (inclusionChecker.getObjectInclusionType(slots.getSecond(), objectToPortVisitMap, periodRecord).getFirst() == InclusionType.In) {
							if (!isNominalInPrompt(cargo, periodRecord)) {
								lockDownSlotDates(slotAllocationMap, slots.getFirst(), lockedSlots);
							}
						} else {
							lockDownCargoDates(slotAllocationMap, cargo, lockedCargoes);
						}
					}
				}
			}

			// These slots have been considered
			seenSlots.addAll(cargo.getSlots());
		}

		// open slots
		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			assert slot != null;
			if (seenSlots.contains(slot)) {
				continue;
			}
			seenSlots.add(slot);
			if (inclusionChecker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord).getFirst() == InclusionType.Out) {
				slotsToRemove.add(slot);
			}
		}

		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			assert slot != null;
			if (seenSlots.contains(slot)) {
				continue;
			}
			seenSlots.add(slot);
			if (inclusionChecker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord).getFirst() == InclusionType.Out) {
				slotsToRemove.add(slot);
			}
		}
	}

	/**
	 * Scan through the slots processed and if still in use check for removed slots and cargoes which are required to e.g. complete P&L evaluation and bring them back in on dedicated round trip cargo
	 * models.
	 * 
	 * @param seenSlots
	 * @param slotsToRemove
	 * @param cargoesToRemove
	 * @param newVesselAvailabilities
	 * @param startConditionMap
	 * @param endConditionMap
	 * @param slotAllocationMap
	 * @param lockedCargoes
	 *            TODO
	 */
	public void checkIfRemovedSlotsAreStillNeeded(final @NonNull Set<Slot> seenSlots, final @NonNull Collection<Slot> slotsToRemove, final @NonNull Collection<Cargo> cargoesToRemove,
			final @NonNull List<VesselAvailability> newVesselAvailabilities, final @NonNull Map<AssignableElement, PortVisit> startConditionMap,
			final @NonNull Map<AssignableElement, PortVisit> endConditionMap, final @NonNull Map<Slot, SlotAllocation> slotAllocationMap, final Set<Cargo> lockedCargoes) {

		for (final Slot slot : seenSlots) {
			// Slot has already been removed, ignore it.
			// FIXME: This slot may be a dependency of a slot later in the list and have it's own dependencies which will not be included. We should probably loop over a cloned list which can be added
			// to.
			if (slotsToRemove.contains(slot)) {
				continue;
			}
			final Set<Slot> slotDependencies = new HashSet<>(getExtraDependenciesForSlot(slot));
			updateSlotDependencies(slotsToRemove, cargoesToRemove, newVesselAvailabilities, startConditionMap, endConditionMap, slotAllocationMap, slotDependencies, lockedCargoes);
		}
	}

	private void updateSlotDependencies(final @NonNull Collection<Slot> slotsToRemove, final @NonNull Collection<Cargo> cargoesToRemove,
			final @NonNull List<VesselAvailability> newVesselAvailabilities, final @NonNull Map<AssignableElement, PortVisit> startConditionMap,
			@NonNull final Map<AssignableElement, PortVisit> endConditionMap, @NonNull final Map<Slot, SlotAllocation> slotAllocationMap, final Set<Slot> slotDependencies,
			final Set<Cargo> lockedCargoes) {
		for (final Slot dep : slotDependencies) {
			if (slotsToRemove.contains(dep)) {
				slotsToRemove.remove(dep);
				final Cargo depCargo = dep.getCargo();
				if (depCargo != null) {
					// Include all deps too
					slotsToRemove.removeAll(depCargo.getSlots());
					cargoesToRemove.remove(depCargo);
					if (depCargo.getCargoType() == CargoType.FLEET) {
						final VesselAssignmentType vesselAssignmentType = depCargo.getVesselAssignmentType();
						if (vesselAssignmentType instanceof VesselAvailability) {
							final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
							final VesselAvailability newVesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
							newVesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
							newVesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
							newVesselAvailability.setVessel(vesselAvailability.getVessel());

							// TODO: set charter rate, set entity. Once multiple avail complete, grab from assignment.
							newVesselAvailability.setTimeCharterRate(vesselAvailability.getTimeCharterRate());
							newVesselAvailability.setFleet(vesselAvailability.isFleet());

							newVesselAvailability.setFleet(vesselAvailability.isFleet());
							newVesselAvailability.setEntity(vesselAvailability.getEntity());

							// Ignore Ballast bonus/repositioning - should not be part of P&L...
							// ... unless linked to a curve price.
							// Do not set optional, as this is no longer optional!

							newVesselAvailabilities.add(newVesselAvailability);
							
							depCargo.setVesselAssignmentType(newVesselAvailability);
							
							updateVesselAvailabilityConditions(newVesselAvailability, depCargo, startConditionMap, endConditionMap);
						}

					}
					// Remove any vessel & time window flexibility
					lockDownCargoDates(slotAllocationMap, depCargo, lockedCargoes);
				}
			}
		}
	}

	/**
	 * Remove the surplus slots and cargoes from the period scenario and record the deletion in the mapping object.
	 * 
	 * @param internalDomain
	 * @param mapping
	 * @param slotsToRemove
	 * @param cargoesToRemove
	 */
	public void removeExcludedSlotsAndCargoes(final EditingDomain internalDomain, final IScenarioEntityMapping mapping, final Collection<@NonNull Slot> slotsToRemove,
			final Collection<@NonNull Cargo> cargoesToRemove) {
		// <<<
		// Delete slots and cargoes outside of range.
		final List<EObject> objectsToDelete = new LinkedList<>();
		for (final Slot slot : slotsToRemove) {
			@Nullable
			final Slot originalFromCopy = mapping.getOriginalFromCopy(slot);
			assert originalFromCopy != null; // We should not be null in the transformer
			mapping.registerRemovedOriginal(originalFromCopy);
			if (slot instanceof LoadSlot) {
				// cargoModel.getLoadSlots().remove(slot);
			} else if (slot instanceof DischargeSlot) {
				// cargoModel.getDischargeSlots().remove(slot);
			} else {
				throw new IllegalStateException("Unknown slot type");
			}
			objectsToDelete.add(slot);
		}
		for (final Cargo cargo : cargoesToRemove) {
			// cargoModel.getCargoes().remove(cargo);
			@Nullable
			final Cargo originalFromCopy = mapping.getOriginalFromCopy(cargo);
			assert originalFromCopy != null; // We should not be null in the transformer
			mapping.registerRemovedOriginal(originalFromCopy);
			objectsToDelete.add(cargo);
		}
		internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, objectsToDelete));
	}

	public void lockDownCargoDates(final Map<Slot, SlotAllocation> slotAllocationMap, final Cargo cargo, final Set<Cargo> lockedCargoes) {

		final VesselAssignmentType vat = cargo.getVesselAssignmentType();
		AVesselSet<Vessel> lockedVessel = null;
		if (vat instanceof VesselAvailability) {
			lockedVessel = (((VesselAvailability) vat).getVessel());
		} else if (vat instanceof CharterInMarket) {
			lockedVessel = (((CharterInMarket) vat).getVesselClass());
		}

		for (final Slot slot : cargo.getSlots()) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {
					// Dates defined by other slots
					continue;
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (dischargeSlot.isFOBSale()) {
					// Dates defined by other slots
					continue;
				}
			}
			// if (slot instanceof SpotSlot) {
			// // Problems with spot slots...
			// continue;
			// }
			final SlotAllocation cargoSlotAllocation = slotAllocationMap.get(slot);
			if (cargoSlotAllocation != null) {
				slot.setWindowSize(0);
				slot.setWindowFlex(0);
				final ZonedDateTime localStart = cargoSlotAllocation.getSlotVisit().getStart();
				slot.setWindowStart(localStart.toLocalDate());
				slot.setWindowStartTime(localStart.getHour());
			}

			if (lockedVessel != null) {
				slot.getAllowedVessels().clear();
				slot.getAllowedVessels().add(lockedVessel);
			}
			slot.setLocked(true);
		}
		cargo.setAllowRewiring(false);
		if (cargo.getCargoType() == CargoType.FLEET) {
			cargo.setLocked(true);
		}
		lockedCargoes.add(cargo);
	}

	public void lockDownSlotDates(final Map<Slot, SlotAllocation> slotAllocationMap, final Slot slot, final Set<Slot> lockedSlots) {
		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isDESPurchase()) {
				// Dates defined by other slots
				return;
			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (dischargeSlot.isFOBSale()) {
				// Dates defined by other slots
				return;
			}
		}

		final SlotAllocation cargoSlotAllocation = slotAllocationMap.get(slot);
		if (cargoSlotAllocation != null) {
			slot.setWindowSize(0);
			slot.setWindowFlex(0);
			final ZonedDateTime localStart = cargoSlotAllocation.getSlotVisit().getStart();
			slot.setWindowStart(localStart.toLocalDate());
			slot.setWindowStartTime(localStart.getHour());
			slot.getAllowedVessels().clear();
			final VesselAssignmentType vat = slot.getCargo().getVesselAssignmentType();
			if (vat instanceof VesselAvailability) {
				slot.getAllowedVessels().add(((VesselAvailability) vat).getVessel());
			} else if (vat instanceof CharterInMarket) {
				slot.getAllowedVessels().add(((CharterInMarket) vat).getVesselClass());
			}
		}
		slot.setLocked(true);
		lockedSlots.add(slot);
	}

	private boolean isNominalInPrompt(@NonNull final Cargo cargo, @NonNull final PeriodRecord periodRecord) {

		if (!isInPrompt(cargo, periodRecord)) {
			return false;
		}
		final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
		if (vesselAssignmentType instanceof CharterInMarket) {
			if (cargo.getSpotIndex() == NOMINAL_INDEX && LicenseFeatures.isPermitted("features:no-nominal-in-prompt")) {
				return true;
			}
		}
		return false;
	}

	private boolean isInPrompt(@NonNull final Cargo cargo, @NonNull final PeriodRecord periodRecord) {
		final Slot first = cargo.getSlots().get(0);
		if (periodRecord.promptEnd != null && first.getWindowStart().isBefore(periodRecord.promptEnd)) {
			// start is within the prompt
			return true;
		}
		return false;
	}

	protected List<Slot> getExtraDependenciesForSlot(final Slot slot) {

		if (extensions == null) {
			return Collections.emptyList();
		}
		final List<Slot> extraDependencies = new LinkedList<Slot>();
		for (final IPeriodTransformerExtension ext : extensions) {
			final List<Slot> extraDependenciesForSlot = ext.getExtraDependenciesForSlot(slot);
			if (extraDependenciesForSlot != null) {
				extraDependencies.addAll(extraDependenciesForSlot);
			}
		}

		return extraDependencies;
	}

	public void updateVesselAvailabilities(@NonNull final PeriodRecord periodRecord, @NonNull final CargoModel cargoModel, @NonNull final SpotMarketsModel spotMarketsModel,
			@NonNull final PortModel portModel, @NonNull final Map<AssignableElement, PortVisit> startConditionMap, @NonNull final Map<AssignableElement, PortVisit> endConditionMap,
			@NonNull final Set<Cargo> cargoesToKeep, @NonNull final Set<Event> eventsToKeep, @NonNull final Map<EObject, PortVisit> objectToPortVisitMap, final IScenarioEntityMapping mapping) {

		final List<CollectedAssignment> collectedAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel);

		updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, cargoesToKeep, eventsToKeep, objectToPortVisitMap, mapping);
	}

	public void updateVesselAvailabilities(@NonNull final PeriodRecord periodRecord, @NonNull final List<CollectedAssignment> collectedAssignments,
			@NonNull final Map<AssignableElement, PortVisit> startConditionMap, @NonNull final Map<AssignableElement, PortVisit> endConditionMap, @NonNull final Set<Cargo> cargoesToKeep,
			@NonNull final Set<Event> eventsToKeep, @NonNull final Map<EObject, PortVisit> objectToPortVisitMap, final IScenarioEntityMapping mapping) {

		// Here we loop through all the collected assignments, trimming the vessel availability to anything outside of the date range.
		// This can handle out-of-order assignments by checking to see whether or not a cargo has already been trimmed out of the date range before updating
		COLLECTED_ASSIGNMENT_LOOP: for (final CollectedAssignment collectedAssignment : collectedAssignments) {

			final List<AssignableElement> assignedObjects = collectedAssignment.getAssignedObjects();

			VesselAvailability vesselAvailability = null;
			int hoursBeforeNewStart = 0;
			int hoursAfterNewEnd = 0;

			for (final AssignableElement assignedObject : assignedObjects) {
				assert assignedObject != null;
				if (assignedObject instanceof Cargo) {
					if (cargoesToKeep.contains(assignedObject)) {
						continue;
					}
				}
				if (assignedObject instanceof VesselEvent) {
					if (eventsToKeep.contains(assignedObject)) {
						continue;
					}
				}
				final NonNullPair<InclusionType, Position> result = inclusionChecker.getObjectInclusionType(assignedObject, objectToPortVisitMap, periodRecord);
				if (collectedAssignment.isSetSpotIndex()) {
					if (collectedAssignment.getSpotIndex() == NOMINAL_INDEX) {
						continue;
					}
					if (result.getFirst() == InclusionType.Out) {
						final Position position = result.getSecond();

						// This *should* be working in sorted order. Thus keep that last #Before case and terminate loop at the first #After case
						if (position == Position.Before) {
							mapping.setLastTrimmedAfter(collectedAssignment.getCharterInMarket(), collectedAssignment.getSpotIndex(), assignedObject);
						} else if (position == Position.After) {
							mapping.setLastTrimmedAfter(collectedAssignment.getCharterInMarket(), collectedAssignment.getSpotIndex(), assignedObject);
							continue COLLECTED_ASSIGNMENT_LOOP;
						}
					}
				} else {
					vesselAvailability = collectedAssignment.getVesselAvailability();

					if (vesselAvailability != null) {
						if (result.getFirst() == InclusionType.Out) {
							final Position position = result.getSecond();
							final PortVisit startPortVisit = startConditionMap.get(assignedObject);
							final PortVisit endPortVisit = endConditionMap.get(assignedObject);

							final int duration = Hours.between(endPortVisit.getStart(), startPortVisit.getStart());

							if (position == Position.Before) {
								// Update availability start heel
								hoursBeforeNewStart += duration;
								updateStartConditions(vesselAvailability, assignedObject, startConditionMap, mapping);
							} else if (position == Position.After) {
								// Update availability end heel
								hoursAfterNewEnd += duration;
								updateEndConditions(vesselAvailability, assignedObject, endConditionMap, mapping);
							}
						}
					}
				}
			}

			// The rounding is over-constraining the problem
			if (vesselAvailability != null) {
				if (vesselAvailability.getAvailabilityOrContractMinDuration() != 0) {
					int minDurationInDays = vesselAvailability.getAvailabilityOrContractMinDuration();

					if (hoursBeforeNewStart > 0 && hoursAfterNewEnd > 0) {
						vesselAvailability.setMinDuration(0);
					} else {
						final int hoursAlreadyUsed = hoursBeforeNewStart + hoursAfterNewEnd;
						minDurationInDays -= Math.ceil((double) ((double) hoursAlreadyUsed / (double) 24));
						minDurationInDays = Math.max(minDurationInDays, 0);

						vesselAvailability.setMinDuration(minDurationInDays);
					}
				}
				if (vesselAvailability.getAvailabilityOrContractMaxDuration() != 0) {
					int maxDurationInDays = vesselAvailability.getAvailabilityOrContractMaxDuration();

					if (hoursBeforeNewStart > 0 && hoursAfterNewEnd > 0) {
						vesselAvailability.setMaxDuration(0);
					} else {
						final int hoursAlreadyUsed = hoursBeforeNewStart + hoursAfterNewEnd;
						maxDurationInDays -= Math.floor((double) ((double) hoursAlreadyUsed / (double) 24));
						maxDurationInDays = Math.max(maxDurationInDays, 0);

						vesselAvailability.setMaxDuration(maxDurationInDays);
					}
				}
			}
		}
	}

	public void generateObjectToPortVisitMap(@NonNull final LNGScenarioModel output, @NonNull final Map<EObject, PortVisit> objectToPortVisitMap) {

		final ScheduleModel scheduleModel = output.getScheduleModel();
		final Schedule schedule = scheduleModel.getSchedule();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					final Slot slot = slotVisit.getSlotAllocation().getSlot();
					objectToPortVisitMap.put(slot, slotVisit);
				} else if (event instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
					final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
					objectToPortVisitMap.put(vesselEvent, vesselEventVisit);
				}
			}
		}
	}

	public void generateStartAndEndConditionsMap(@NonNull final LNGScenarioModel output, @NonNull final Map<AssignableElement, PortVisit> startConditionMap,
			@NonNull final Map<AssignableElement, PortVisit> endConditionMap) {
		final ScheduleModel scheduleModel = output.getScheduleModel();
		final Schedule schedule = scheduleModel.getSchedule();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				final Event segmentStart = ScheduleModelUtils.getSegmentStart(event);
				if (event == segmentStart) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						// Get the cargo for the set of visits., set start/end heel links
						final Cargo cargo = slotVisit.getSlotAllocation().getSlot().getCargo();
						endConditionMap.put(cargo, slotVisit);

						// skip to next segment - start of next sequence. Otherwise this could e.g. cargo discharge slot.
						// TODO: Note, what about FOB/DES here?
						Event segmentEnd = ScheduleModelUtils.getSegmentEnd(event).getNextEvent();
						while (segmentEnd != null && !(segmentEnd instanceof PortVisit)) {
							segmentEnd = segmentEnd.getNextEvent();
						}

						startConditionMap.put(cargo, (PortVisit) segmentEnd);

					} else if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						// Find sequence start and sequence end and map to event.
						final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
						endConditionMap.put(vesselEvent, vesselEventVisit);

						Event segmentEnd = ScheduleModelUtils.getSegmentEnd(event);
						while (segmentEnd != null && !(segmentEnd instanceof PortVisit)) {
							segmentEnd = segmentEnd.getNextEvent();
						}
						startConditionMap.put(vesselEvent, (PortVisit) segmentEnd);

					}
				}
			}
		}
	}

	private Pair<Set<Cargo>, Set<Event>> getDependenciesForEvent(final Schedule schedule, final VesselEvent ve) {
		VesselEventVisit interestingVesselEventVisit = null;
		final Set<Cargo> previousCargoes = new HashSet<>();
		final Set<Event> previousEvents = new HashSet<>();
		final Set<Vessel> vessels = SetUtils.getObjects(ve.getAllowedVessels());

		for (final Sequence sequence : schedule.getSequences()) {

			if (!(sequence.getSequenceType() == SequenceType.VESSEL && vessels.contains(sequence.getVesselAvailability().getVessel()))) {
				continue;
			}
			for (final Event event : sequence.getEvents()) {
				if (event instanceof VesselEventVisit) {
					if (((VesselEventVisit) event).getVesselEvent() == ve) {
						interestingVesselEventVisit = (VesselEventVisit) event;
						break;
					}
				}
			}
			if (interestingVesselEventVisit != null) {
				break;
			}
		}
		if (interestingVesselEventVisit != null) {
			Event currentEvent = interestingVesselEventVisit.getPreviousEvent();
			boolean foundCargo = false;
			while (currentEvent.getPreviousEvent() != null) {
				if (currentEvent instanceof SlotVisit) {
					previousCargoes.add(((SlotVisit) currentEvent).getSlotAllocation().getSlot().getCargo());
					foundCargo = true;
					break;
				} else if (currentEvent instanceof PortVisit) {
					previousEvents.add(currentEvent);
				}
				currentEvent = currentEvent.getPreviousEvent();
			}
			if (foundCargo) {
				return new Pair<>(previousCargoes, previousEvents);
			}
		}
		return null;
	}

	@NonNull
	public LNGScenarioModel copyScenario(@NonNull final LNGScenarioModel wholeScenario, @NonNull final IScenarioEntityMapping mapping) {
		final Copier copier = new Copier();
		final LNGScenarioModel output = (LNGScenarioModel) copier.copy(wholeScenario);
		assert output != null;

		copier.copyReferences();

		// Remove schedule model references from copier before passing into the mapping object.
		final Schedule schedule = wholeScenario.getScheduleModel().getSchedule();
		if (schedule != null) {
			final Iterator<EObject> itr = schedule.eAllContents();
			while (itr.hasNext()) {
				copier.remove(itr.next());
			}
			// schedule.eAllContents().forEachRemaining(t -> copier.remove(t));
			copier.remove(schedule);
		}

		mapping.createMappings(copier);

		return output;
	}

	public void generateSlotAllocationMap(@NonNull final LNGScenarioModel output, @NonNull final Map<Slot, SlotAllocation> slotAllocationMap) {
		final ScheduleModel scheduleModel = output.getScheduleModel();
		final Schedule schedule = scheduleModel.getSchedule();
		for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
			slotAllocationMap.put(slotAllocation.getSlot(), slotAllocation);
		}
	}

	@NonNull
	public EditingDomain createEditingDomain(@NonNull final LNGScenarioModel output) {
		//
		// Set<String> hints = LNGTransformerHelper.getHints(optimiserSettings);
		// final LNGDataTransformer transformer = new LNGDataTransformer(output, optimiserSettings, hints, services);
		//
		// final ModelEntityMap modelEntityMap = transformer.getModelEntityMap();

		// Construct internal command stack to generate correct output schedule
		final EditingDomain ed = LNGSchedulerJobUtils.createLocalEditingDomain();

		// Delete commands need a resource set on the editing domain
		final Resource r = new XMIResourceImpl();
		r.getContents().add(output);
		ed.getResourceSet().getResources().add(r);
		//
		// Injector evaluationInjector;
		// {
		// final List<Module> modules = new LinkedList<>();
		// modules.add(new InputSequencesModule(transformer.getInitialSequences()));
		// modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(transformer.getOptimiserSettings()), services,
		// IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
		// modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		// evaluationInjector = transformer.getInjector().createChildInjector(modules);
		// }
		//
		//
		// final Pair<Command, Schedule> p = LNGSchedulerJobUtils.exportSolution(evaluationInjector, output, transformer.getOptimiserSettings(), ed, modelEntityMap,
		// transformer.getInitialSequences(), null);
		// ed.getCommandStack().execute(p.getFirst());

		return ed;
	}

	/**
	 * Given a vessel availability, update the starting conditions around the input AssignableElement which is assumed to be outside of the current optimisation period scope. We want to set the
	 * starting point to be equal to the end conditions of the AssignableElement sequence.
	 * 
	 * @param vesselAvailability
	 * @param assignedObject
	 * @param startConditionMap
	 */
	public void updateStartConditions(@NonNull final VesselAvailability vesselAvailability, @NonNull final AssignableElement assignedObject,
			@NonNull final Map<AssignableElement, PortVisit> startConditionMap, @NonNull final IScenarioEntityMapping mapping) {
		final PortVisit portVisit = startConditionMap.get(assignedObject);

		if (inclusionChecker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability) == InclusionType.In) {

			mapping.setLastTrimmedBefore(vesselAvailability, 0, assignedObject);

			vesselAvailability.setStartAt(null);
			if (portVisit instanceof VesselEventVisit && ((VesselEventVisit) portVisit).getVesselEvent() instanceof CharterOutEvent) {
				vesselAvailability.setStartAt(((VesselEventVisit) portVisit).getVesselEvent().getPort());
			} else {
				vesselAvailability.setStartAt(portVisit.getPort());
			}

			vesselAvailability.setStartAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
			vesselAvailability.setStartBy(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());

			// Check end after bounds. Do they still apply?
			// TODO: Add this to unit tests
			if (vesselAvailability.isSetEndAfter()) {
				if (vesselAvailability.getEndAfterAsDateTime().isBefore(portVisit.getStart())) {
					vesselAvailability.setEndAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
				}
			}

			// TODO: Set CV, price
			final int heelAtStart = portVisit.getHeelAtStart();
			if (heelAtStart == 0) {
				vesselAvailability.getStartHeel().setMinVolumeAvailable(0);
				vesselAvailability.getStartHeel().setMaxVolumeAvailable(0);
				vesselAvailability.getStartHeel().setCvValue(0.0);
				vesselAvailability.getStartHeel().setPriceExpression("");
				vesselAvailability.setRepositioningFee("");
			} else {
				vesselAvailability.getStartHeel().setMinVolumeAvailable(heelAtStart);
				vesselAvailability.getStartHeel().setMaxVolumeAvailable(heelAtStart);
				vesselAvailability.getStartHeel().setCvValue(22.8);
				vesselAvailability.getStartHeel().setPriceExpression("");
				vesselAvailability.setRepositioningFee("");
			}
		}
	}

	public void updateEndConditions(@NonNull final VesselAvailability vesselAvailability, @NonNull final AssignableElement assignedObject,
			@NonNull final Map<AssignableElement, PortVisit> endConditionMap, @NonNull final IScenarioEntityMapping mapping) {

		final PortVisit portVisit = endConditionMap.get(assignedObject);
		assert portVisit != null;

		if (inclusionChecker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability) == InclusionType.In) {

			mapping.setLastTrimmedAfter(vesselAvailability, 0, assignedObject);

			vesselAvailability.getEndAt().clear();
			// Standard case
			vesselAvailability.getEndAt().add(portVisit.getPort());
			// Special case for charter outs start/end ports can differ
			if (portVisit instanceof VesselEventVisit) {
				final VesselEventVisit vesselEventVisit = (VesselEventVisit) portVisit;
				final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
				if (vesselEvent instanceof CharterOutEvent) {
					final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
					if (charterOutEvent.isSetRelocateTo()) {
						final Port port = charterOutEvent.getPort();
						vesselAvailability.getEndAt().clear();
						vesselAvailability.getEndAt().add(port);
					}
				}
			}

			vesselAvailability.setEndAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
			vesselAvailability.setEndBy(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
			vesselAvailability.setForceHireCostOnlyEndRule(false);

			if (vesselAvailability.getEndHeel() == null) {
				vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
			}

			// Set must arrive cold with target heel volume
			final int heel = portVisit.getHeelAtStart();
			if (heel > 0 || portVisit.getPreviousEvent() instanceof Cooldown) {
				vesselAvailability.getEndHeel().setMinimumEndHeel(heel);
				vesselAvailability.getEndHeel().setMaximumEndHeel(heel);
				vesselAvailability.getEndHeel().setPriceExpression("");
				if (portVisit.getPreviousEvent() instanceof Cooldown) {
					// We had a cooldown before, so end either way
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.EITHER);
				} else {
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}
			} else {
				vesselAvailability.getEndHeel().setMinimumEndHeel(0);
				vesselAvailability.getEndHeel().setMaximumEndHeel(0);
				vesselAvailability.getEndHeel().setPriceExpression("");
				vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_WARM);
			}
			vesselAvailability.setBallastBonusContract(null);
		}
	}

	@NonNull
	public InclusionChecker getInclusionChecker() {
		return inclusionChecker;
	}

	public void setInclusionChecker(@NonNull final InclusionChecker inclusionChecker) {
		this.inclusionChecker = inclusionChecker;
	}

	/**
	 * Given a new vessel availability, update the start and end conditions around the input AssignableElement. This is assumed to be a single element modelled statically outside the main period
	 * scope.
	 * 
	 * @param vesselAvailability
	 * @param assignedObject
	 * @param startConditionMap
	 * @param endConditionMap
	 */

	public void updateVesselAvailabilityConditions(@NonNull final VesselAvailability vesselAvailability, @NonNull final AssignableElement assignedObject,
			@NonNull final Map<AssignableElement, PortVisit> startConditionMap, @NonNull final Map<AssignableElement, PortVisit> endConditionMap) {

		// TODO: Refactor the other conditions methods to avoid code duplication.
		{
			final PortVisit portVisit = endConditionMap.get(assignedObject);
			vesselAvailability.setStartAt(portVisit.getPort());

			vesselAvailability.setStartAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
			vesselAvailability.setStartBy(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());

			// Check end after bounds. Do they still apply?
			// TODO: Add this to unit tests
			if (vesselAvailability.isSetEndAfter()) {
				if (vesselAvailability.getEndAfterAsDateTime().isBefore(portVisit.getStart())) {
					vesselAvailability.unsetEndAfter();
				}
			}

			// TODO: Set CV, price
			final int heelAtStart = portVisit.getHeelAtStart();
			if (heelAtStart == 0) {
				vesselAvailability.getStartHeel().setMinVolumeAvailable(0);
				vesselAvailability.getStartHeel().setMaxVolumeAvailable(0);
				vesselAvailability.getStartHeel().setCvValue(0.0);
				vesselAvailability.getStartHeel().setPriceExpression("0.0");
			} else {
				vesselAvailability.getStartHeel().setMinVolumeAvailable(heelAtStart);
				vesselAvailability.getStartHeel().setMaxVolumeAvailable(heelAtStart);
				vesselAvailability.getStartHeel().setCvValue(22.8);
				vesselAvailability.getStartHeel().setPriceExpression("0.01");
			}
		}
		{
			final PortVisit portVisit = startConditionMap.get(assignedObject);
			vesselAvailability.getEndAt().clear();
			vesselAvailability.getEndAt().add(portVisit.getPort());

			vesselAvailability.setEndAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
			vesselAvailability.setEndBy(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
			vesselAvailability.setForceHireCostOnlyEndRule(false);

			// Set must arrive cold with target heel volume
			final int heel = portVisit.getHeelAtStart();
			if (heel > 0 || portVisit.getPreviousEvent() instanceof Cooldown) {
				if (vesselAvailability.getEndHeel() == null) {
					vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
				}
				vesselAvailability.getEndHeel().setMinimumEndHeel(heel);
				vesselAvailability.getEndHeel().setMaximumEndHeel(heel);
				vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
			} else {
				if (vesselAvailability.getEndHeel() == null) {
					vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
				}
				vesselAvailability.getEndHeel().setMinimumEndHeel(0);
				vesselAvailability.getEndHeel().setMaximumEndHeel(0);
				vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_WARM);
			}
		}
	}

	public void trimSpotMarketCurves(@NonNull final PeriodRecord periodRecord, @NonNull final LNGScenarioModel scenario) {
		final SpotMarketsModel spotMarketsModel = scenario.getReferenceModel().getSpotMarketsModel();
		ZonedDateTime earliestDate = periodRecord.lowerBoundary;
		ZonedDateTime latestDate = periodRecord.upperBoundary;
		if (periodRecord.lowerBoundary == null || periodRecord.upperBoundary == null) {
			final Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTimes = LNGScenarioUtils.findEarliestAndLatestTimes(scenario);
			if (periodRecord.lowerBoundary == null) {
				earliestDate = earliestAndLatestTimes.getFirst();
			}
			if (periodRecord.upperBoundary == null) {
				latestDate = earliestAndLatestTimes.getSecond();
			}
		}

		if (earliestDate == null) {
			throw new IllegalStateException("Unable to find earliest scenario date");
		}
		if (latestDate == null) {
			throw new IllegalStateException("Unable to find latest scenario date");
		}

		trimSpotMarketCurves(periodRecord, spotMarketsModel.getDesPurchaseSpotMarket(), earliestDate, latestDate);
		trimSpotMarketCurves(periodRecord, spotMarketsModel.getDesSalesSpotMarket(), earliestDate, latestDate);
		trimSpotMarketCurves(periodRecord, spotMarketsModel.getFobPurchasesSpotMarket(), earliestDate, latestDate);
		trimSpotMarketCurves(periodRecord, spotMarketsModel.getFobSalesSpotMarket(), earliestDate, latestDate);
	}

	public void trimSpotMarketCurves(@NonNull final PeriodRecord periodRecord, @Nullable final SpotMarketGroup spotMarketGroup, @NonNull final ZonedDateTime earliestDate,
			@NonNull final ZonedDateTime latestDate) {
		if (spotMarketGroup != null) {
			for (final SpotMarket spotMarket : spotMarketGroup.getMarkets()) {
				final SpotAvailability availability = spotMarket.getAvailability();

				// If the constant is set, get the value and replace with zero. Later create new curve points with the original constant value.
				int constantValue = 0;
				if (availability.isSetConstant() && availability.getConstant() > 0) {
					constantValue = availability.getConstant();
					availability.unsetConstant();
				}

				// Create lookup of curve dates and remove those outside optimisation range.
				final Set<YearMonth> seenDates = new HashSet<>();
				// Create a curve if necessary
				DataIndex<Integer> curve = availability.getCurve();
				if (curve == null) {
					curve = PricingFactory.eINSTANCE.createDataIndex();
					availability.setCurve(curve);
				}
				final List<IndexPoint<Integer>> pointsToRemove = new LinkedList<>();
				for (final IndexPoint<Integer> value : curve.getPoints()) {
					final YearMonth date = value.getDate();
					final ZonedDateTime dateAsDateTime = date.atDay(1).atStartOfDay(ZONEID_UTC);
					if (date.isBefore(getDateFromStartOfMonth(earliestDate))) {
						// remove
						pointsToRemove.add(value);
						continue;
					}
					if (dateAsDateTime.isAfter(latestDate) || dateAsDateTime.equals(latestDate)) {
						// remove
						pointsToRemove.add(value);
						continue;
					}
					seenDates.add(date);
				}
				curve.getPoints().removeAll(pointsToRemove);

				// Set the constant, and add curve data across known period instead

				// Fill in curve gaps with the original constant value.
				if (constantValue != 0) {
					YearMonth cal = getDateFromStartOfMonth(earliestDate);
					while (cal.atDay(1).atStartOfDay(ZONEID_UTC).isBefore(latestDate)) {
						if (!seenDates.contains(cal)) {
							final IndexPoint<Integer> newValue = PricingFactory.eINSTANCE.createIndexPoint();
							newValue.setDate(cal);
							newValue.setValue(constantValue);
							// Add
							curve.getPoints().add(newValue);
						}
						// Increment calendar
						cal = cal.plusMonths(1);
					}
				}
				// replace availability with new curve
				availability.setCurve(curve);
			}
		}
	}

	@NonNull
	private YearMonth getDateFromStartOfMonth(@NonNull final ZonedDateTime date) {
		return YearMonth.of(date.getYear(), date.getMonthValue());
	}

}
