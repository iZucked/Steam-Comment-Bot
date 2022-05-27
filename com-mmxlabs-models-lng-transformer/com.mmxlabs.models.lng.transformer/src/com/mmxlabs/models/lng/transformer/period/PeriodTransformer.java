/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
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
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
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
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.manager.ClonedScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;

/***
 * The PeriodTransformer aims to take a slice of the full scenario for optimisation. Given the user supplied dates (the boundary) we will add +/- 1 month (the cutoff) and remove cargoes, events etc
 * outside of this range. For slots or events outside of the boundary we will lockdown the variables (dates/windows, vessel, option to rewire). The aim of the boundary cargoes is to be able to take
 * into account any knock on P&L changes as a result of changes to the optimisable period without any direct changes.
 * 
 * There are a number of gotchas etc in the period. We can never fully represent the behaviour of the full scenario within the period scenario, but aim to capture as much as possible.
 * 
 * Key points; - We lockdown dates before the period on the assumption they have already happened. We tend to just lock the vessel assignment after the period to allow some greater flexibility in
 * re-wiring. - We tend to work with the scheduled dates rather that original windows as lateness can shift cargoes/event in or out of the period in conflict with the original window - We reduce the
 * min/max duration values of a vessel by the usage time chopped out of the scenario. However the duration is whole days whereas the chopped elements time is counted in hours (and thus may not be
 * whole days)
 * 
 * -- TODO Note there are a few "compat with master" messages with old buggy behaviour.
 * 
 * @author Simon Goodall
 * 
 */
public class PeriodTransformer {

	private static final @NonNull ZoneId ZONEID_UTC = ZoneId.of("UTC");
	private static final int NOMINAL_INDEX = -1;
	@Inject(optional = true)
	private Iterable<IPeriodTransformerExtension> extensions;
	
	@Inject
	@Named(SchedulerConstants.Key_UseHeelRetention)
	private boolean retainHeel;

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
					bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseHeelRetention))//
					.toInstance(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_HEEL_RETENTION));
				}
			};
			injector = Guice.createInjector(m);
		}
		if (injector != null) {
			injector.injectMembers(this);
		}
	}

	@Inject
	private InclusionChecker inclusionChecker;

	public static class PeriodTransformResult {
		public PeriodTransformResult(final @NonNull IScenarioDataProvider sdp, final @NonNull EditingDomain editingDomain, final @Nullable ExtraDataProvider extraDataProvider) {
			this.sdp = sdp;
			this.editingDomain = editingDomain;
			this.extraDataProvider = extraDataProvider;
		}

		public @NonNull IScenarioDataProvider sdp;
		public @NonNull EditingDomain editingDomain;
		public @Nullable ExtraDataProvider extraDataProvider;
	}

	public @NonNull PeriodTransformResult transform(@NonNull final IScenarioDataProvider wholeScenario, @NonNull final Schedule schedule, @Nullable final ExtraDataProvider extraDataProvider,
			@NonNull final UserSettings userSettings, @NonNull final IScenarioEntityMapping mapping) {
		final PeriodRecord periodRecord = createPeriodRecord(userSettings, wholeScenario.getTypedScenario(LNGScenarioModel.class));
		return transform(wholeScenario, schedule, extraDataProvider, periodRecord, mapping);
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

			if (scenario.getSchedulingEndDate() != null) {
				periodRecord.schedulingEndDate = scenario.getSchedulingEndDate().atStartOfDay();
			}
		}

		return periodRecord;
	}

	enum SlotLockMode {
		UNSET, VESSEL_ONLY, VESSEL_AND_DATES
	}

	enum Status {
		NotChecked, // Initial status. This should be updated by the end of the transformation
		ToLockdown, // Some or all of the object should be locked down (dates if before the boundary, vessel, maybe wiring)
		ToRemove, // Object is not needed in the final result
		Readded, // Object is a calculation dependency and is to be re-added but fully locked down.
		ToKeep // Object is a keeper and can be optimised
	}

	/**
	 * A record created for each input in the scenario (cargo, vessel event or open slot). This records the links to the schedule model objects (if present), some handy object references to make some
	 * code lookups easier later on. Most importantly it records the Status of the object i.e. what we will do with it for the purpose of the period (basically keep, lockdown or remove).
	 */
	static class InclusionRecord {
		EObject object; // Vessel Event (may be open), Cargo or Open slot
		Map<EObject, PortVisit> event = new HashMap<>();
		InclusionType inclusionType;
		Position position;
		Status status = Status.NotChecked;

		Map<Slot<?>, SlotLockMode> slotLockMode = new HashMap<>();

		Sequence sequence;
		VesselCharter vesselCharter; // Current VA. This is not expected to be updated if status is Readded (to review)
		boolean isHeelSource = false;
		boolean isHeelSink = false;
	}

	public @NonNull PeriodTransformResult transform(@NonNull final IScenarioDataProvider wholeScenarioDataProvider, @NonNull final Schedule wholeScenarioSchedule,
			@Nullable final ExtraDataProvider extraDataProvider, @NonNull final PeriodRecord periodRecord, @NonNull final IScenarioEntityMapping mapping) {

		// assert - passed validation

		// Take a copy to manipulate.
		final Triple<IScenarioDataProvider, ExtraDataProvider, Schedule> p = copyScenario(wholeScenarioDataProvider, wholeScenarioSchedule, extraDataProvider, mapping);
		final IScenarioDataProvider outputDataProvider = p.getFirst();
		final LNGScenarioModel output = outputDataProvider.getTypedScenario(LNGScenarioModel.class);
		final Schedule schedule = p.getThird();

		// Do not allow the prompt period to extend past the optimisation period
		if (periodRecord.upperBoundary != null && periodRecord.promptEnd != null) {
			if (periodRecord.upperBoundary.isBefore(periodRecord.promptEnd.atStartOfDay(ZONEID_UTC))) {
				output.setPromptPeriodEnd(periodRecord.upperBoundary.toLocalDate());
			}
		}

		final EditingDomain internalDomain = outputDataProvider.getEditingDomain();

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(output);
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(output);
		final PortModel portModel = ScenarioModelUtil.getPortModel(output);
		final ModelDistanceProvider modelDistanceProvider = ScenarioModelUtil.getModelDistanceProvider(outputDataProvider);

		// Init extensions
		if (extensions != null) {
			for (final IPeriodTransformerExtension extension : extensions) {
				extension.init(cargoModel, schedule);
			}
		}
		
		// Set-up for the heel carry
		final Map<Cargo, CargoAllocation> sortedCargoes = new LinkedHashMap<>();
		final List<HeelCarryCargoPair> heelCarryCargoPairs = new LinkedList<>();

		// Create the basic records based on scheduled dates etc. No modifications are performed yet as following stages can update the records
		final Map<EObject, InclusionRecord> records = generateInclusionRecords(schedule, cargoModel, periodRecord, sortedCargoes, heelCarryCargoPairs);

		// Step two, some vessel events require the preceeding events to still be included. If they are marked to be removed, the upgrade to locked.
		// Note this implicitly upgrades events marked as ToRemove and already skipped over if they are before an event to be included.
		for (final InclusionRecord inclusionRecord : records.values()) {
			if (inclusionRecord.object instanceof VesselEvent) {
				final VesselEvent event = (VesselEvent) inclusionRecord.object;
				// ...actually this is all current event types...
				if (inclusionRecord.status != Status.ToRemove && (event instanceof DryDockEvent || event instanceof MaintenanceEvent || event instanceof CharterOutEvent)) {

					final VesselEventVisit visit = (VesselEventVisit) inclusionRecord.event.get(event);
					if (visit != null) {
						final Collection<EObject> eventsAndCargoesToKeep = getPriorDependenciesForEvent(event, visit);
						if (eventsAndCargoesToKeep != null) {
							eventsAndCargoesToKeep.forEach(e -> {
								final InclusionRecord ir = records.get(e);
								if (ir.status == Status.ToRemove) {
									ir.status = Status.ToLockdown;
								}
							});
						}
					}
				}
			}
		}

		// List of new vessel charters for cargoes outside normal range
		final List<VesselCharter> newVesselCharters = new LinkedList<>();
		// Custom phase
		if (true) {
			// TODO: This code needs to be re-introduced. This is where any dependents to the P&L calculation need to have their status changed to "ReAdded" if they are marked ToRemove.
			// Any cloned vessel charters should be configured here too. These will typically be a copy of the original vessel with the start and end heel set to match the boundary conditions of
			// the cargo to allow an identical P&l calculation.

			// TODO: Method naming and doc
			checkIfRemovedSlotsAreStillNeeded(records, newVesselCharters);

			if (extensions != null) {
				final Set<Slot<?>> excludedSlots = new HashSet<>();
				final Set<Cargo> excludedCargoes = new HashSet<>();
				for (final InclusionRecord rec : records.values()) {
					if (rec.object instanceof Cargo cargo) {
						if (rec.status == Status.ToRemove) {
							excludedCargoes.add(cargo);
						}
					} else if (rec.object instanceof Slot<?> slot) {
						if (rec.status == Status.ToRemove) {
							excludedSlots.add(slot);
						}
					}
				}
				for (final IPeriodTransformerExtension ext : extensions) {
					ext.processSlotInclusionsAndExclusions(cargoModel, schedule, excludedSlots, excludedCargoes);
				}
			}
		}

		if (retainHeel) {
			for (final HeelCarryCargoPair pair : heelCarryCargoPairs) {
				if (pair.firstRecord.isHeelSource && pair.secondRecord.isHeelSink) {
					Status first = pair.firstRecord.status;
					Status second = pair.secondRecord.status;
					if (first != Status.ToRemove && second == Status.ToRemove) {
						pair.secondRecord.status = Status.ToLockdown;
						pair.secondRecord.inclusionType = pair.firstRecord.inclusionType;
					} else if (second != Status.ToRemove && first == Status.ToRemove) {
						pair.firstRecord.status = Status.ToLockdown;
						pair.firstRecord.inclusionType = pair.secondRecord.inclusionType;
					}
				}
			}
		}
		
		// Step 3 lockdown vessels and dates if needed.
		// ToLockdown may be a partial lock if a cargo is partially In the period.
		// ReAdded is always a full lockdown.
		lockDownRecords(periodRecord, records);

		// Update vessel charters based on cargoes/events being removed.
		updateVesselCharters(cargoModel, spotMarketsModel, portModel, schedule, modelDistanceProvider, records, periodRecord, mapping);

		if (retainHeel) {
			// Sanity check that heel sink and sink sources number is equal
			for (final HeelCarryCargoPair pair : heelCarryCargoPairs) {
				assert pair.firstRecord.isHeelSource && pair.secondRecord.isHeelSink//
				&& (pair.firstRecord.status != Status.ToRemove && pair.secondRecord.status != Status.ToRemove//
				|| pair.firstRecord.status == Status.ToRemove && pair.secondRecord.status == Status.ToRemove);
			}
		}
		// Sanity check - all records should have a valid status
		for (final InclusionRecord r : records.values()) {
			assert r.status != Status.NotChecked;
		}

		// Reduce the spot charter-in markets based on removed cargoes and generate the re-numbering mapping (as by limiting the count we may have invalid sopt indices)
		updateSpotCharterInMarkets(mapping, records);

		// Remove excluded cargoes, slots and events.
		removeExcludedObjects(internalDomain, mapping, records);

		// Remove vessels based on updated start / end dates
		removeVesselCharters(internalDomain, periodRecord, cargoModel, mapping);

		// Sort out Canal bookings
		lockAndRemoveCanalBookings(records, output);

		// Trim the spot market curves largely by converting the constant into a curve for the optimisation period and set the constant to 0 for outside of the period.
		trimSpotMarketCurves(periodRecord, output, wholeScenarioDataProvider.getTypedScenario(LNGScenarioModel.class));

		// Add any vessel created from the ReAdded cargoes
		output.getCargoModel().getVesselCharters().addAll(newVesselCharters);

		// Final scenario clean up

		// Remove schedule model
		output.getScheduleModel().setSchedule(null);
		LNGSchedulerJobUtils.clearAnalyticsResults(output.getAnalyticsModel());

		// Clear this date as we have fixed everything and it will conflict with rules in schedule transformer.
		output.unsetSchedulingEndDate();

		return new PeriodTransformResult(outputDataProvider, internalDomain, p.getSecond());
	}

	public void lockDownRecords(final PeriodRecord periodRecord, final Map<EObject, InclusionRecord> records) {
		for (final InclusionRecord inclusionRecord : records.values()) {
			// Check for relevant statues
			if (inclusionRecord.status == Status.ToLockdown || inclusionRecord.status == Status.Readded) {

				if (inclusionRecord.object instanceof Slot) {
					// An open slot is marked "keep open"
					final Slot<?> slot = (Slot<?>) inclusionRecord.object;
					slot.setLocked(true);
				} else if (inclusionRecord.object instanceof Cargo) {
					final Cargo cargo = (Cargo) inclusionRecord.object;
					// Check for full lockdown
					if (inclusionRecord.status == Status.Readded //
							|| inclusionRecord.inclusionType == InclusionType.Out // Completely out
							|| (inclusionRecord.inclusionType == InclusionType.Boundary && cargo.getVesselAssignmentType() == null) // Non-shipped cargo
							|| (inclusionRecord.inclusionType == InclusionType.Boundary && inclusionRecord.position == Position.Both) // Cargo crosses the whole optimisation period
					) {
						// Lockdown cargo dates before the optimisation period or ReAdded cargoes.
						boolean lockDates = inclusionRecord.status == Status.Readded;
						if (!lockDates) {
							// Are any slots in the before position?
							for (final Slot<?> slot : cargo.getSlots()) {
								// Note this code is slightly different to the #getObjectInclusionType. We check window END in the slot case and window START in the cargo case for the BOUNDARY/BEFORE
								// combination.
								lockDates = inclusionChecker.getSlotPosition(slot, periodRecord) == Position.Before;
								if (lockDates) {
									break;
								}
							}
						}

						// Development assertion - can we replace the above loop with this check instead?
						// We currently can not due to different in the check logic noted above.
						// assert lockDates == (inclusionRecord.status == Status.Readded || inclusionRecord.position == Position.Before || inclusionRecord.position == Position.Both);

						lockDownCargoDates(inclusionRecord, cargo, lockDates);
					} else {
						// Partially optimisable cargo.

						// Will be set to true if any of the slots are IN the period
						boolean oneIsIn = false;
						final List<Slot<?>> slots = new LinkedList<>(cargo.getSortedSlots());
						// Examine the slot "window" (rather than scheduled date - hmm is this the desired behaviour? 2021-08-17 -- SG)
						// and if the cargo is partially optimisable, only lock down the slots which are not IN the period.
						for (final Slot<?> slot : slots) {

							final SlotLockMode mode = inclusionRecord.slotLockMode.getOrDefault(slot, SlotLockMode.UNSET);
							if (mode != SlotLockMode.UNSET) {
								// The slot window is in the Out or Boundary zone, lock it down.
								final SlotAllocation slotAllocation = ((SlotVisit) inclusionRecord.event.get(slot)).getSlotAllocation();
								lockDownSlotDates(slot, slotAllocation, mode == SlotLockMode.VESSEL_AND_DATES);
							} else {
								oneIsIn = true;
							}
						}
						if (!oneIsIn) {
							// No slots are in the optimisation period, mark the cargo as not optimisable
							cargo.setAllowRewiring(false);
							if (cargo.getCargoType() == CargoType.FLEET) {
								cargo.setLocked(true);
							}
						}

					}

					for (final Slot<?> s : cargo.getSlots()) {
						if (periodRecord.upperBoundary != null) {
							if (s.getSchedulingTimeWindow().getStart().isAfter(periodRecord.upperBoundary)) {
								s.setLocked(true);
								continue;
							}
						}
						if (periodRecord.lowerBoundary != null) {
							if (s.getSchedulingTimeWindow().getEnd().isBefore(periodRecord.lowerBoundary)) {
								s.setLocked(true);
								continue;
							}
						}
					}

				} else if (inclusionRecord.object instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) inclusionRecord.object;
					if (vesselEvent instanceof CharterOutEvent) {
						// If in boundary, limit available vessels to assigned vessel
						final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
						final VesselCharter vesselCharter = ((VesselCharter) charterOutEvent.getVesselAssignmentType());
						if (vesselCharter != null) {
							charterOutEvent.getAllowedVessels().clear();
							charterOutEvent.getAllowedVessels().add(vesselCharter.getVessel());
						} else {
							// Unused optional event, strip out as it can't be used and we do not have a "lock as unused" option.
							if (charterOutEvent.isOptional()) {
								inclusionRecord.status = Status.ToRemove;
							}
						}
						// Note this is new code by SG added 2021/08/12
						final boolean lockDates = inclusionRecord.position != Position.After;
						// Re-check lockdown status after optional charter out check as lockdown the event dates
						if (lockDates && inclusionRecord.status == Status.ToLockdown) {
							// disabled due to compat with master
							vesselEvent.setLocked(true);
							// final ZonedDateTime localStart = inclusionRecord.event.get(vesselEvent).getStart();
							// vesselEvent.setStartAfter(localStart.toLocalDateTime());
							// vesselEvent.setStartBy(localStart.toLocalDateTime());
						}
					}
				}
			}
		}
	}

	/**
	 * This method removes any bookings associated with elements that are being removed from the optimisation so they are not available. Locked-down events will allocate the vessel to the booking.
	 * Note: this does not strictly assign the booking to the event as we no longer have this capability.
	 * 
	 * @param records
	 * @param output
	 */
	private void lockAndRemoveCanalBookings(final Map<EObject, InclusionRecord> records, final LNGScenarioModel output) {
		final CargoModel cargoModel = output.getCargoModel();
		final CanalBookings canalBookings = cargoModel.getCanalBookings();
		if (canalBookings != null) {
			// data structures

			final Set<CanalBookingSlot> bookingToRemove = new HashSet<>();

			for (final InclusionRecord inclusionRecord : records.values()) {
				if (inclusionRecord.status == Status.ToRemove) {
					// Find bookings to remove
					if (inclusionRecord.object instanceof VesselEvent) {
						final VesselEventVisit vev = (VesselEventVisit) inclusionRecord.event.get(inclusionRecord.object);
						if (vev != null) {
							final Event nextEvent = vev.getNextEvent();
							if (nextEvent instanceof Journey) {
								final Journey journey = (Journey) nextEvent;
								if (journey.getCanalBooking() != null) {
									bookingToRemove.add(journey.getCanalBooking());
								}
							}
						}
					}
					if (inclusionRecord.object instanceof Cargo) {
						final Cargo cargo = (Cargo) inclusionRecord.object;
						for (final Slot<?> slot : cargo.getSlots()) {

							final SlotVisit slotVisit = (SlotVisit) inclusionRecord.event.get(slot);
							if (slotVisit != null) {
								if (slotVisit.getNextEvent() instanceof Journey) {
									final Journey journey = (Journey) slotVisit.getNextEvent();
									if (journey.getCanalBooking() != null) {
										bookingToRemove.add(journey.getCanalBooking());
									}
								}
							}
						}
					}
				} else if (inclusionRecord.status == Status.ToLockdown || inclusionRecord.status == Status.Readded) {
					// Bookings to *try* to assign to the current event
					if (inclusionRecord.object instanceof VesselEvent) {
						final VesselEventVisit vev = (VesselEventVisit) inclusionRecord.event.get(inclusionRecord.object);
						if (vev != null) {
							final Event nextEvent = vev.getNextEvent();
							if (nextEvent instanceof Journey) {
								final Journey journey = (Journey) nextEvent;
								if (journey.getCanalBooking() != null) {
									final CanalBookingSlot canalBooking = journey.getCanalBooking();
									if (canalBooking != null) {
										canalBooking.setVessel(vev.getSequence().getVesselCharter().getVessel());
									}
								}
							}
						}
					}
					if (inclusionRecord.object instanceof Cargo) {
						final Cargo cargo = (Cargo) inclusionRecord.object;
						for (final Slot<?> slot : cargo.getSlots()) {

							final SlotVisit slotVisit = (SlotVisit) inclusionRecord.event.get(slot);
							if (slotVisit != null) {
								if (slotVisit.getNextEvent() instanceof Journey) {
									final Journey journey = (Journey) slotVisit.getNextEvent();

									final CanalBookingSlot canalBooking = journey.getCanalBooking();
									if (canalBooking != null) {
										canalBooking.setVessel(slotVisit.getSequence().getVesselCharter().getVessel());
									}
								}
							}
						}
					}
				}

			}

			// Remove the bookings
			for (final CanalBookingSlot bookingSlot : bookingToRemove) {
				canalBookings.getCanalBookingSlots().remove(bookingSlot);
			}
		}
	}

	/**
	 * For spot charter markets where all cargoes on a used charter option are outside of the period we reduce the spot charter in count. As spot charter are index from 0 to spot count, we need to
	 * maintain a mapping between the original scenario spot index and the period scenario spot index to avoid issues with merging on the period export.
	 * 
	 * @param periodMapping
	 * @param cargoModel
	 * @param spotMarketsModel
	 */
	private void updateSpotCharterInMarkets(final IScenarioEntityMapping periodMapping, final Map<EObject, InclusionRecord> records) {

		// // Generate the list of all spot charter ins used by all the cargoes in the scenario. An option may appear multiple times.
		final List<Pair<CharterInMarket, Integer>> total = getSpotCharterInUseForCargoes(records, false);
		//
		final Map<Pair<CharterInMarket, Integer>, Long> counter = total.stream() //
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		// Just the numbers for the removed cargoes
		final List<Pair<CharterInMarket, Integer>> spotCharterUse = getSpotCharterInUseForCargoes(records, true);

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

		// Apply the renumbering
		for (final InclusionRecord inclusionRecord : records.values()) {
			if (inclusionRecord.object instanceof Cargo) {
				// Skip this cargo as it will be removed.
				if (inclusionRecord.status == Status.ToRemove) {
					continue;
				}

				final Cargo cargo = (Cargo) inclusionRecord.object;
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
	}

	@NonNull
	private List<Pair<CharterInMarket, Integer>> getSpotCharterInUseForCargoes(final Map<EObject, InclusionRecord> records, final boolean onlyRemoved) {
		final List<Pair<CharterInMarket, Integer>> result = new LinkedList<>();

		for (final InclusionRecord inclusionRecord : records.values()) {
			if (inclusionRecord.object instanceof Cargo && (!onlyRemoved || inclusionRecord.status == Status.ToRemove)) {
				final Cargo cargo = (Cargo) inclusionRecord.object;
				final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
				if (vesselAssignmentType instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
					final int spotIndex = cargo.getSpotIndex();
					final Pair<CharterInMarket, Integer> p = new Pair<>(charterInMarket, spotIndex);
					result.add(p);
				}
			}
		}
		return result;

	}

	public void removeVesselCharters(@NonNull final EditingDomain internalDomain, @NonNull final PeriodRecord periodRecord, @NonNull final CargoModel cargoModel,
			@NonNull final IScenarioEntityMapping mapping) {
		final Set<VesselCharter> vesselChartersToRemove = new HashSet<>();

		for (final VesselCharter vesselCharter : cargoModel.getVesselCharters()) {
			assert vesselCharter != null;
			if (inclusionChecker.getVesselCharterInclusionType(vesselCharter, periodRecord).getFirst() == InclusionType.Out) {
				// inclusionChecker.getObjectInclusionType(vesselCharter, periodRecord);
				vesselChartersToRemove.add(vesselCharter);

				final VesselCharter originalFromCopy = mapping.getOriginalFromCopy(vesselCharter);
				assert originalFromCopy != null; // We should not be null in the transformer
				mapping.registerRemovedOriginal(originalFromCopy);

			}
		}
		internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, vesselChartersToRemove));

	}

	/**
	 * Scan through the slots processed and if still in use check for removed slots and cargoes which are required to e.g. complete P&L evaluation and bring them back in on dedicated round trip cargo
	 * models.
	 * 
	 * @param records
	 * @param newVesselCharters
	 */
	public void checkIfRemovedSlotsAreStillNeeded(final Map<EObject, InclusionRecord> records, final @NonNull List<VesselCharter> newVesselCharters) {

		boolean changed = true;
		while (changed) {
			changed = false;
			for (final InclusionRecord inclusionRecord : records.values()) {
				if (inclusionRecord.status != Status.ToRemove) {
					if (inclusionRecord.object instanceof Cargo) {
						final Cargo cargo = (Cargo) inclusionRecord.object;
						for (final Slot<?> slot : cargo.getSlots()) {
							final Set<Slot<?>> slotDependencies = new HashSet<>(getExtraDependenciesForSlot(slot));
							changed |= updateSlotDependencies(newVesselCharters, slotDependencies, records);
						}
					}
				}
			}
		}
	}

	private boolean updateSlotDependencies(final @NonNull List<VesselCharter> newVesselCharters, final Set<Slot<?>> slotDependencies, final Map<EObject, InclusionRecord> records) {

		boolean changed = false;
		for (final Slot<?> dep : slotDependencies) {

			final Cargo cargo = dep.getCargo();
			if (cargo == null) {
				final InclusionRecord inclusionRecord = records.get(dep);
				if (inclusionRecord.status == Status.ToRemove) {
					inclusionRecord.status = Status.Readded;
					changed = true;
				}
			} else {
				final InclusionRecord inclusionRecord = records.get(cargo);
				if (inclusionRecord.status == Status.ToRemove) {
					inclusionRecord.status = Status.Readded;
					changed = true;
					final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
					if (vesselAssignmentType instanceof VesselCharter) {
						final VesselCharter vesselCharter = (VesselCharter) vesselAssignmentType;
						final VesselCharter newVesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
						newVesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
						newVesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());
						newVesselCharter.setVessel(vesselCharter.getVessel());
						newVesselCharter.setCharterNumber(vesselCharter.getCharterNumber());

						newVesselCharter.setTimeCharterRate(vesselCharter.getTimeCharterRate());
						newVesselCharter.setEntity(vesselCharter.getCharterOrDelegateEntity());

						// Ignore Ballast bonus/repositioning - should not be part of P&L...
						// ... unless linked to a curve price.
						// Do not set optional, as this is no longer optional!

						newVesselCharters.add(newVesselCharter);

						cargo.setVesselAssignmentType(newVesselCharter);

						updateStartConditions(newVesselCharter, inclusionRecord);
						updateEndConditions(newVesselCharter, inclusionRecord);
					}
				}
			}
		}
		return changed;
	}

	/**
	 * Remove the surplus slots and cargoes from the period scenario and record the deletion in the mapping object.
	 * 
	 * @param internalDomain
	 * @param mapping
	 * @param slotsToRemove
	 * @param cargoesToRemove
	 */
	public void removeExcludedObjects(final EditingDomain internalDomain, final IScenarioEntityMapping mapping, final Map<EObject, InclusionRecord> records) {
		final List<EObject> objectsToDelete = new LinkedList<>();
		for (final InclusionRecord inclusionRecord : records.values()) {

			if (inclusionRecord.status != Status.ToRemove) {
				continue;
			}

			if (inclusionRecord.object instanceof Slot) {
				final Slot<?> slot = (Slot<?>) inclusionRecord.object;
				final Slot<?> originalFromCopy = mapping.getOriginalFromCopy(slot);
				assert originalFromCopy != null; // We should not be null in the transformer
				mapping.registerRemovedOriginal(originalFromCopy);

				objectsToDelete.add(slot);

			} else if (inclusionRecord.object instanceof Cargo) {
				final Cargo cargo = (Cargo) inclusionRecord.object;
				{
					final Cargo originalFromCopy = mapping.getOriginalFromCopy(cargo);
					assert originalFromCopy != null; // We should not be null in the transformer
					mapping.registerRemovedOriginal(originalFromCopy);
					objectsToDelete.add(cargo);
				}
				for (final Slot<?> slot : cargo.getSlots()) {
					final @Nullable Slot<?> originalFromCopy = mapping.getOriginalFromCopy(slot);
					assert originalFromCopy != null; // We should not be null in the transformer
					mapping.registerRemovedOriginal(originalFromCopy);

					objectsToDelete.add(slot);
				}

			} else if (inclusionRecord.object instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) inclusionRecord.object;
				final VesselEvent originalFromCopy = mapping.getOriginalFromCopy(vesselEvent);
				assert originalFromCopy != null; // We should not be null in the transformer
				mapping.registerRemovedOriginal(originalFromCopy);
				objectsToDelete.add(vesselEvent);
			} else {
				throw new IllegalStateException();
			}
		}

		if (!objectsToDelete.isEmpty()) {
			internalDomain.getCommandStack().execute(DeleteCommand.create(internalDomain, objectsToDelete));
		}
	}

	public void lockDownCargoDates(final InclusionRecord inclusionRecord, final Cargo cargo, final boolean doLockDates) {

		Vessel lockedVessel = null;
		final VesselAssignmentType vat = cargo.getVesselAssignmentType();
		if (vat instanceof VesselCharter) {
			lockedVessel = (((VesselCharter) vat).getVessel());
		} else if (vat instanceof CharterInMarket) {
			lockedVessel = (((CharterInMarket) vat).getVessel());
		} else if (vat instanceof CharterInMarketOverride) {
			lockedVessel = (((CharterInMarketOverride) vat).getCharterInMarket().getVessel());
		}

		// TODO: Look into lockDownSlotDates method. Lots of duplication!
		for (final Slot<?> slot : cargo.getSlots()) {
			// Load and discharge window are often part of the pricing for a DES purchase or FOB sale, so if we change
			// the window, then the price incurred might change, hence we only fix one side of the cargo and leave the
			// window untouched.
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
			if (doLockDates) {
				final SlotAllocation cargoSlotAllocation = ((SlotVisit) inclusionRecord.event.get(slot)).getSlotAllocation();
				if (cargoSlotAllocation != null) {
					slot.setWindowFlex(0);
					if (!slot.isWindowCounterParty()) {
						slot.setWindowSize(0);
					}
					final ZonedDateTime localStart = cargoSlotAllocation.getSlotVisit().getStart();
					slot.setWindowStart(localStart.toLocalDate());
					slot.setWindowStartTime(localStart.getHour());
				}
			}

			if (lockedVessel != null) {
				slot.getRestrictedVessels().clear();
				slot.getRestrictedVessels().add(lockedVessel);
				slot.setRestrictedVesselsOverride(true);
				slot.setRestrictedVesselsArePermissive(true);
			}
			slot.setLocked(true);
		}
		cargo.setAllowRewiring(false);
		if (cargo.getCargoType() == CargoType.FLEET) {
			cargo.setLocked(true);
		}
	}

	public void lockDownSlotDates(final Slot<?> slot, final SlotAllocation cargoSlotAllocation, final boolean doLockDates) {
		// Load and discharge window are often part of the pricing for a DES purchase or FOB sale, so if we change
		// the window, then the price incurred might change, hence we only fix one side of the cargo and leave the
		// window untouched.
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

		if (cargoSlotAllocation != null) {
			if (doLockDates) {
				slot.setWindowFlex(0);
				if (!slot.isWindowCounterParty()) {
					slot.setWindowSize(0);
				}
				final ZonedDateTime localStart = cargoSlotAllocation.getSlotVisit().getStart();
				slot.setWindowStart(localStart.toLocalDate());
				slot.setWindowStartTime(localStart.getHour());
			}

		}
		final Cargo cargo = slot.getCargo();
		if (cargo != null) {
			final VesselAssignmentType vat = cargo.getVesselAssignmentType();
			Vessel lockedVessel = null;
			if (vat instanceof VesselCharter) {
				lockedVessel = (((VesselCharter) vat).getVessel());
			} else if (vat instanceof CharterInMarket) {
				lockedVessel = (((CharterInMarket) vat).getVessel());
			} else if (vat instanceof CharterInMarketOverride) {
				lockedVessel = (((CharterInMarketOverride) vat).getCharterInMarket().getVessel());
			}

			slot.getRestrictedVessels().clear();
			slot.getRestrictedVessels().add(lockedVessel);
			slot.setRestrictedVesselsOverride(true);
			slot.setRestrictedVesselsArePermissive(true);
		}
		slot.setLocked(true);
	}

	private boolean isInPrompt(@NonNull final Cargo cargo, @NonNull final PeriodRecord periodRecord) {
		final Slot<?> first = cargo.getSlots().get(0);
		if (periodRecord.promptEnd != null && first.getWindowStart().isBefore(periodRecord.promptEnd)) {
			// start is within the prompt
			return true;
		}
		return false;
	}

	protected List<Slot<?>> getExtraDependenciesForSlot(final Slot<?> slot) {

		if (extensions == null) {
			return Collections.emptyList();
		}
		final List<Slot<?>> extraDependencies = new LinkedList<>();
		for (final IPeriodTransformerExtension ext : extensions) {
			final List<Slot<?>> extraDependenciesForSlot = ext.getExtraDependenciesForSlot(slot);
			if (extraDependenciesForSlot != null) {
				extraDependencies.addAll(extraDependenciesForSlot);
			}
		}

		return extraDependencies;
	}

	public void updateVesselCharters(@NonNull final CargoModel cargoModel, @NonNull final SpotMarketsModel spotMarketsModel, @NonNull final PortModel portModel, final Schedule schedule,
			@NonNull final ModelDistanceProvider modelDistanceProvider, final Map<EObject, InclusionRecord> records, final PeriodRecord periodRecord, final IScenarioEntityMapping mapping) {

		final List<CollectedAssignment> collectedAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider);
		assert collectedAssignments != null;
		updateVesselCharters(collectedAssignments, schedule, records, periodRecord, mapping);
	}

	/**
	 * Update the vessel dates. We need to take into account any lateness, trimed cargoes and events, schedule horizon, optimiser end dates etc.
	 * 
	 * @param collectedAssignments
	 * @param schedule
	 * @param records
	 * @param periodRecord
	 * @param mapping
	 */
	public void updateVesselCharters(@NonNull final List<CollectedAssignment> collectedAssignments, final Schedule schedule, final Map<EObject, InclusionRecord> records,
			final PeriodRecord periodRecord, final IScenarioEntityMapping mapping) {

		// Here we loop through all the collected assignments, trimming the vessel charter to anything outside of the date range.
		// This can handle out-of-order assignments by checking to see whether or not a cargo has already been trimmed out of the date range before updating
		for (final CollectedAssignment collectedAssignment : collectedAssignments) {
			// Find the matching sequence
			Sequence sequence = null;
			for (final Sequence seq : schedule.getSequences()) {
				if (collectedAssignment.isSpotVessel()) {
					if (collectedAssignment.getCharterInMarket() == seq.getCharterInMarket() //
							&& collectedAssignment.getSpotIndex() == seq.getSpotIndex() //
					) {
						sequence = seq;
						break;
					}
				} else {
					if (collectedAssignment.getVesselCharter() == seq.getVesselCharter()) {
						sequence = seq;
						break;
					}
				}
			}
			// Find end event if applicable
			EndEvent endEvent = null;
			if (sequence != null && !collectedAssignment.isSpotVessel() && !sequence.getEvents().isEmpty()) {
				endEvent = (EndEvent) sequence.getEvents().get(sequence.getEvents().size() - 1);
			}

			updateVesselCharter(collectedAssignment, endEvent, records, periodRecord, mapping);
		}
	}

	public void updateVesselCharter(@NonNull final CollectedAssignment collectedAssignment, final @Nullable EndEvent endEvent, final Map<EObject, InclusionRecord> records,
			final PeriodRecord periodRecord, final IScenarioEntityMapping mapping) {

		// Ignore nominals
		if (collectedAssignment.isSetSpotIndex()) {
			if (collectedAssignment.getSpotIndex() == NOMINAL_INDEX) {
				return;
			}
		}

		final List<AssignableElement> assignedObjects = collectedAssignment.getAssignedObjects();

		final VesselCharter vesselCharter = collectedAssignment.getVesselCharter();
		int hoursBeforeNewStart = 0;
		int hoursAfterNewEnd = 0;

		boolean updatedBefore = false;
		boolean updatedAfter = false;

		for (final AssignableElement assignedObject : assignedObjects) {
			assert assignedObject != null;

			final InclusionRecord inclusionRecord = records.get(assignedObject);
			final Status status = inclusionRecord.status;
			if (status == Status.ToKeep) {
				continue;
			}
			if (status == Status.ToRemove || status == Status.Readded) {

				final Position position = inclusionRecord.position;

				// Element has been removed from this sequence
				// This *should* be working in sorted order. Thus keep that last #Before case and terminate loop at the first #After case
				if (position == Position.Before) {
					// Overwrite any previous trimmed before call
					mapping.setLastTrimmedBefore(collectedAssignment.getVesselAssignmentType(), collectedAssignment.getSpotIndex(), assignedObject);
					updatedBefore = true;

				} else if (position == Position.After) {
					// Only expect to do this once
					mapping.setLastTrimmedAfter(collectedAssignment.getVesselAssignmentType(), collectedAssignment.getSpotIndex(), assignedObject);
					updatedAfter = true;
					break;
				} else {
					assert false;
				}
			}
		}

		// The rounding is over-constraining the problem
		if (vesselCharter != null) {

			// Update starting conditions of the vessel
			if (updatedBefore) {
				final AssignableElement newFirstElement = mapping.getLastTrimmedBefore(collectedAssignment.getVesselAssignmentType(), collectedAssignment.getSpotIndex());
				final InclusionRecord inclusionRecord = records.get(newFirstElement);
				updateStartConditions(vesselCharter, inclusionRecord);

				Event event = inclusionRecord.sequence.getEvents().get(0);
				// Compat with master - ignore orphan ballast legs
				{
					event = event.getNextEvent();
					while (event != null) {
						if (event instanceof PortVisit) {
							break;
						}
						event = event.getNextEvent();
					}
				}
				final int duration = Hours.between(event.getStart(), vesselCharter.getStartAfterAsDateTime());
				hoursBeforeNewStart = duration;
			}

			// Update ending conditions of the vessel
			if (updatedAfter) {
				final AssignableElement newLastElement = mapping.getLastTrimmedAfter(collectedAssignment.getVesselAssignmentType(), collectedAssignment.getSpotIndex());
				final InclusionRecord inclusionRecord = records.get(newLastElement);
				updateEndConditions(vesselCharter, inclusionRecord);

				final EList<Event> events = inclusionRecord.sequence.getEvents();
				final Event event = events.get(events.size() - 1);
				final int duration = Hours.between(vesselCharter.getEndByAsDateTime(), event.getEnd());
				hoursAfterNewEnd = duration;
			}

			// Disconnect charter contract
			copyCharterContract(vesselCharter, updatedBefore, updatedAfter);

			// If there is no explicit end date set, update with the implicit end dates (schedule horizon or the optimiser defined date - which should be the current end event start date)
			if (!updatedAfter) {
				// Extend the vessel end date to cover late ending if present in input scenario. This does not cover max duration breaches.

				// (Note) This used to be done before the update end conditions call
				// Do we have an end date set?
				if (vesselCharter.isSetEndBy() && endEvent != null) {
					final ZonedDateTime endDate = vesselCharter.getEndByAsDateTime();
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
									if (inclusionChecker.getObjectInVesselCharterRange(portVisit, vesselCharter) == InclusionType.Out) {
										// Change the vessel charter end date to match exported end date
										vesselCharter.setEndBy(endEvent.getEnd().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
									}
								}
							}
						}
					}
				}

				if (!vesselCharter.isSetMinDuration() || !vesselCharter.isSetMaxDuration()) {
					if (!vesselCharter.isSetEndAfter() && !vesselCharter.isSetEndBy()) {
						if (periodRecord.schedulingEndDate != null && periodRecord.upperBoundary != null
								&& periodRecord.schedulingEndDate.isBefore(periodRecord.upperBoundary.toLocalDate().atStartOfDay())) {
							vesselCharter.setEndAfter(periodRecord.schedulingEndDate);
							vesselCharter.setEndBy(periodRecord.upperBoundary.toLocalDateTime());
						} else if (endEvent != null) {
							vesselCharter.setEndAfter(endEvent.getEnd().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
							vesselCharter.setEndBy(endEvent.getEnd().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
						}
					} else if (vesselCharter.isSetEndAfter()) {
						if (periodRecord.schedulingEndDate != null && periodRecord.upperBoundary != null && periodRecord.schedulingEndDate.isBefore(vesselCharter.getEndAfter())) {
							if (vesselCharter.getEndAfter().isAfter(periodRecord.schedulingEndDate)) {
								vesselCharter.setEndAfter(periodRecord.schedulingEndDate);
							}
						}
					}

					// // Only update if the dates changed?
					// if (!vesselCharter.getEndAt().isEmpty() && endEvent != null) {
					// vesselCharter.getEndAt().clear();
					// vesselCharter.getEndAt().add(endEvent.getPort());
					// }
				}
			}

			// If we have change the start or end conditions we may need to update the min/max durations
			if (vesselCharter.getCharterOrDelegateMinDuration() != 0) {
				int minDurationInDays = vesselCharter.getCharterOrDelegateMinDuration();

				if (hoursBeforeNewStart > 0 && hoursAfterNewEnd > 0) {
					vesselCharter.setMinDuration(0);
				} else {
					final int hoursAlreadyUsed = hoursBeforeNewStart + hoursAfterNewEnd;
					// Over-compensate the amount used to avoid constraint violation due to rounding
					minDurationInDays -= Math.ceil((double) ((double) hoursAlreadyUsed / (double) 24));
					minDurationInDays = Math.max(minDurationInDays, 0);

					vesselCharter.setMinDuration(minDurationInDays);
				}
			}
			if (vesselCharter.getCharterOrDelegateMaxDuration() != 0) {
				int maxDurationInDays = vesselCharter.getCharterOrDelegateMaxDuration();

				if (hoursBeforeNewStart > 0 && hoursAfterNewEnd > 0) {
					vesselCharter.setMaxDuration(0);
				} else {
					final int hoursAlreadyUsed = hoursBeforeNewStart + hoursAfterNewEnd;
					// Under-compensate the amount used to avoid constraint violation due to rounding
					maxDurationInDays -= Math.floor((double) ((double) hoursAlreadyUsed / (double) 24));
					maxDurationInDays = Math.max(maxDurationInDays, 0);

					vesselCharter.setMaxDuration(maxDurationInDays);
				}
			}
		}
	}

	private void copyCharterContract(final VesselCharter vesselCharter, final boolean clearStart, final boolean clearEnd) {
		// Create a copy of any shared charter contract to avoid changes impacting all vessels
		GenericCharterContract gcc = null;
		if (vesselCharter.isCharterContractOverride()) {
			gcc = vesselCharter.getContainedCharterContract();
		} else {
			if (vesselCharter.getGenericCharterContract() != null) {
				final Copier copier = new Copier();
				gcc = (GenericCharterContract) copier.copy(vesselCharter.getGenericCharterContract());

				// Min/max duration is ignored on contained charter contracts, so copy values then clear
				if (!vesselCharter.isSetMinDuration()) {
					vesselCharter.setMinDuration(gcc.getMinDuration());
				}
				if (!vesselCharter.isSetMaxDuration()) {
					vesselCharter.setMaxDuration(gcc.getMaxDuration());
				}
				gcc.unsetMinDuration();
				gcc.unsetMaxDuration();

				// Start and end heels are ignored on vessel charter contracts (contained or shared)
				gcc.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
				gcc.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

				vesselCharter.setCharterContractOverride(true);
				vesselCharter.unsetGenericCharterContract();
				vesselCharter.setContainedCharterContract(gcc);
			}
		}

		if (gcc != null) {
			if (clearStart) {
				gcc.setRepositioningFeeTerms(null);
			}
			if (clearEnd) {
				gcc.setBallastBonusTerms(null);
			}
		}
	}

	/**
	 * For each cargo, open slot and vessel event determine whether or not it is part of the optimisation period
	 * 
	 * @param schedule
	 * @param cargoModel
	 * @param periodRecord
	 * @return
	 */
	public Map<EObject, InclusionRecord> generateInclusionRecords(@NonNull final Schedule schedule, @NonNull final CargoModel cargoModel, //
			final PeriodRecord periodRecord, final Map<Cargo, CargoAllocation> sortedCargoes, final List<HeelCarryCargoPair> heelCarryCargoPairs) {

		final Map<EObject, InclusionRecord> records = new HashMap<>();

		// Create the basic object structure. Source object, child object and schedule mapping, real vessel allocation

		//// Step 1, record the scheduled objects

		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (event instanceof SlotVisit slotVisit) {
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					final Slot<?> slot = slotAllocation.getSlot();
					final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
					
					final Cargo cargo = slot.getCargo();
					assert cargo != null;
					assert cargoAllocation != null;
					
					sortedCargoes.putIfAbsent(cargo, cargoAllocation);

					final InclusionRecord inclusionRecord = records.computeIfAbsent(cargo, c -> {
						final InclusionRecord r = new InclusionRecord();
						r.object = c;
						r.sequence = sequence;
						r.vesselCharter = sequence.getVesselCharter();
						return r;
					});

					inclusionRecord.event.put(slot, slotVisit);
				} else if (event instanceof VesselEventVisit vesselEventVisit) {
					final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
					final InclusionRecord inclusionRecord = new InclusionRecord();
					inclusionRecord.object = vesselEvent;
					inclusionRecord.event.put(vesselEvent, vesselEventVisit);
					inclusionRecord.sequence = sequence;
					inclusionRecord.vesselCharter = sequence.getVesselCharter();
					records.put(vesselEvent, inclusionRecord);
				}
			}
		}
		
		if (retainHeel) {
			Map.Entry<Cargo, CargoAllocation> previousEntry = null;
			for(final Map.Entry<Cargo, CargoAllocation> entry : sortedCargoes.entrySet()) {
				if (entry.getValue().isIsHeelSource()) {
					previousEntry = entry;
				} else if (entry.getValue().isIsHeelSink() && previousEntry != null) {
					HeelCarryCargoPair pair = new HeelCarryCargoPair();
					pair.firstCargo = previousEntry.getKey();
					pair.firstCargoAllocation = previousEntry.getValue();
					pair.secondCargo = entry.getKey();
					pair.secondCargoAllocation = entry.getValue();
					heelCarryCargoPairs.add(pair);
					previousEntry = null;
				}
			}
		}

		//// Step 2, record the unallocated or open slots and events

		// Open events
		for (final VesselEvent vesselEvent : cargoModel.getVesselEvents()) {
			if (vesselEvent.getVesselAssignmentType() == null) {
				final InclusionRecord inclusionRecord = new InclusionRecord();
				inclusionRecord.object = vesselEvent;
				records.put(vesselEvent, inclusionRecord);
			}
		}

		// open load slots
		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			if (slot.getCargo() == null) {
				final InclusionRecord inclusionRecord = new InclusionRecord();
				inclusionRecord.object = slot;
				records.put(slot, inclusionRecord);
			}
		}

		// Open discharge slots
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			if (slot.getCargo() == null) {
				final InclusionRecord inclusionRecord = new InclusionRecord();
				inclusionRecord.object = slot;
				records.put(slot, inclusionRecord);
			}
		}

		// For each record, determine whether it is in or out of the optimisation
		records.values().forEach(r -> {
			final NonNullPair<InclusionType, Position> p = inclusionChecker.getObjectInclusionType(r, periodRecord);
			r.inclusionType = p.getFirst();
			r.position = p.getSecond();

			if (r.inclusionType == InclusionType.Out) {
				r.status = Status.ToRemove;
			} else if (r.inclusionType == InclusionType.Boundary) {
				// Object is to be kept, but limited or no optimisation scope
				r.status = Status.ToLockdown;
			} else {
				r.status = Status.ToKeep;
			}
		});
		
		if (retainHeel) {
			records.values().forEach(r -> {
				
				if (r.object instanceof Cargo c) {
					final CargoAllocation cargoAllocation = sortedCargoes.get(c);
					if (cargoAllocation.isIsHeelSource()) {
						r.isHeelSource = true;
						findPair(heelCarryCargoPairs, c).firstRecord = r;
					}
					if (cargoAllocation.isIsHeelSink()) {
						r.isHeelSink = true;
						findPair(heelCarryCargoPairs, c).secondRecord = r;
					}
				}
			});
		}
		
		return records;

	}
	
	class HeelCarryCargoPair{
		Cargo firstCargo;
		CargoAllocation firstCargoAllocation;
		InclusionRecord firstRecord;
		Cargo secondCargo;
		CargoAllocation secondCargoAllocation;
		InclusionRecord secondRecord;
	}
	
	private static HeelCarryCargoPair findPair(final List<HeelCarryCargoPair> heelCarryCargoes, final Cargo cargo) {
		for (final HeelCarryCargoPair pair : heelCarryCargoes) {
			if (pair.firstCargo.equals(cargo) || pair.secondCargo.equals(cargo)) {
				return pair;
			}
		}
		throw new IllegalStateException(String.format("Cargo %s is in heel carry pair list, but has no pair. Please contact Minimax Labs.", cargo.getLoadName()));
	}

	/**
	 * A vessel event typically need the previous cargo/event to work out heel information correctly. Return the list of prior events that are needed to compute this correctly. This could be zero to
	 * many object depending on the sequence. We are looking for the cargo before the event (which could even be cargo, other event, other event, this event)
	 * 
	 * @param ve
	 * @param interestingVesselEventVisit
	 * @return
	 */
	private Collection<EObject> getPriorDependenciesForEvent(final VesselEvent ve, final VesselEventVisit interestingVesselEventVisit) {
		final Set<EObject> previousCargoes = new HashSet<>();

		if (interestingVesselEventVisit != null) {
			Event currentEvent = interestingVesselEventVisit.getPreviousEvent();
			boolean foundCargo = false;
			while (currentEvent.getPreviousEvent() != null) {
				if (currentEvent instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) currentEvent;
					previousCargoes.add(slotVisit.getSlotAllocation().getSlot().getCargo());
					foundCargo = true;
					break;
				} else if (currentEvent instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) currentEvent;
					previousCargoes.add(vesselEventVisit.getVesselEvent());
				}
				currentEvent = currentEvent.getPreviousEvent();
			}
			if (foundCargo) {
				return previousCargoes;
			}
		}
		return null;
	}

	@NonNull
	public Triple<IScenarioDataProvider, ExtraDataProvider, Schedule> copyScenario(@NonNull final IScenarioDataProvider wholeScenarioDataProvider, final Schedule baseSchedule,
			@Nullable final ExtraDataProvider extraDataProvider, @NonNull final IScenarioEntityMapping mapping) {
		final Copier copier = new Copier();

		final LNGScenarioModel wholeScenario = wholeScenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final LNGScenarioModel output = (LNGScenarioModel) copier.copy(wholeScenarioDataProvider.getScenario());
		assert output != null;
		final Schedule copyBaseSchedule = (Schedule) copier.copy(baseSchedule);

		ExtraDataProvider copyExtraDataProvider = null;
		if (extraDataProvider != null) {
			copyExtraDataProvider = new ExtraDataProvider();
			final ExtraDataProvider pCopyExtraDataProvider = copyExtraDataProvider;
			if (extraDataProvider.extraCharterInMarketOverrides != null) {
				extraDataProvider.extraCharterInMarketOverrides.forEach(e -> pCopyExtraDataProvider.extraCharterInMarketOverrides.add((CharterInMarketOverride) copier.copy(e)));
			}
			if (extraDataProvider.extraCharterInMarkets != null) {
				extraDataProvider.extraCharterInMarkets.forEach(e -> pCopyExtraDataProvider.extraCharterInMarkets.add((CharterInMarket) copier.copy(e)));
			}
			if (extraDataProvider.extraDischarges != null) {
				extraDataProvider.extraDischarges.forEach(e -> pCopyExtraDataProvider.extraDischarges.add((DischargeSlot) copier.copy(e)));
			}
			if (extraDataProvider.extraLoads != null) {
				extraDataProvider.extraLoads.forEach(e -> pCopyExtraDataProvider.extraLoads.add((LoadSlot) copier.copy(e)));
			}
			if (extraDataProvider.extraVesselCharters != null) {
				extraDataProvider.extraVesselCharters.forEach(e -> pCopyExtraDataProvider.extraVesselCharters.add((VesselCharter) copier.copy(e)));
			}
			if (extraDataProvider.extraVesselEvents != null) {
				extraDataProvider.extraVesselEvents.forEach(e -> pCopyExtraDataProvider.extraVesselEvents.add((VesselEvent) copier.copy(e)));
			}
		}

		copier.copyReferences();

		// Remove schedule model references from copier before passing into the mapping object.
		{
			final Schedule schedule = wholeScenario.getScheduleModel().getSchedule();
			if (schedule != null) {
				final Iterator<EObject> itr = schedule.eAllContents();
				while (itr.hasNext()) {
					copier.remove(itr.next());
				}
				// schedule.eAllContents().forEachRemaining(t -> copier.remove(t));
				copier.remove(schedule);
			}
		}
		{
			if (baseSchedule != null) {
				final Iterator<EObject> itr = baseSchedule.eAllContents();
				while (itr.hasNext()) {
					copier.remove(itr.next());
				}
				// schedule.eAllContents().forEachRemaining(t -> copier.remove(t));
				copier.remove(baseSchedule);
			}
		}
		mapping.createMappings(copier);

		final ClonedScenarioDataProvider sdp = ClonedScenarioDataProvider.make(output, wholeScenarioDataProvider);
		// Apply this base schedule to the scenario. This may be an alternative starting state e.g. from sandbox
		final Command updateCommand = LNGSchedulerJobUtils.derive(sdp.getEditingDomain(), sdp.getTypedScenario(MMXRootObject.class), copyBaseSchedule, ScenarioModelUtil.getCargoModel(sdp), null);
		sdp.getEditingDomain().getCommandStack().execute(updateCommand);

		return new Triple<>(sdp, copyExtraDataProvider, copyBaseSchedule);
	}

	public void generateSlotAllocationMap(@NonNull final Schedule schedule, @NonNull final Map<Slot<?>, SlotAllocation> slotAllocationMap) {
		for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
			slotAllocationMap.put(slotAllocation.getSlot(), slotAllocation);
		}
	}

	/**
	 * Given a vessel charter, update the starting conditions around the input AssignableElement which is assumed to be outside of the current optimisation period scope. We want to set the
	 * starting point to be equal to the end conditions of the AssignableElement sequence.
	 * 
	 * @param vesselCharter
	 * @param assignedObject
	 * @param startConditionMap
	 */
	public void updateStartConditions(@NonNull final VesselCharter vesselCharter, final InclusionRecord inclusionRecord) {

		PortVisit portVisit;
		if (inclusionRecord.object instanceof Cargo cargo) {
			portVisit = inclusionRecord.event.get(cargo.getSortedSlots().get(cargo.getSlots().size() - 1));
		} else if (inclusionRecord.object instanceof VesselEvent) {
			portVisit = inclusionRecord.event.get(inclusionRecord.object);
		} else {
			// assert false;
			throw new IllegalStateException();
		}

		Event evt = portVisit;
		while (evt != null) {
			evt = evt.getNextEvent();
			if (evt instanceof PortVisit) {
				portVisit = (PortVisit) evt;
				break;
			}
		}

		{

			vesselCharter.setStartAt(null);
			if (portVisit instanceof VesselEventVisit && ((VesselEventVisit) portVisit).getVesselEvent() instanceof CharterOutEvent) {
				vesselCharter.setStartAt(((VesselEventVisit) portVisit).getVesselEvent().getPort());
			} else {
				vesselCharter.setStartAt(portVisit.getPort());
			}

			vesselCharter.setStartAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
			vesselCharter.setStartBy(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());

			// Check end after bounds. Do they still apply?
			// TODO: Add this to unit tests
			if (vesselCharter.isSetEndAfter()) {
				if (vesselCharter.getEndAfterAsDateTime().isBefore(portVisit.getStart())) {
					vesselCharter.setEndAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
				}
			}

			// TODO: Set CV, price
			final int heelAtStart = portVisit.getHeelAtStart();
			if (heelAtStart == 0) {
				vesselCharter.getStartHeel().setMinVolumeAvailable(0);
				vesselCharter.getStartHeel().setMaxVolumeAvailable(0);
				vesselCharter.getStartHeel().setCvValue(0.0);
				vesselCharter.getStartHeel().setPriceExpression("");
			} else {
				vesselCharter.getStartHeel().setMinVolumeAvailable(heelAtStart);
				vesselCharter.getStartHeel().setMaxVolumeAvailable(heelAtStart);
				vesselCharter.getStartHeel().setCvValue(22.8);
				vesselCharter.getStartHeel().setPriceExpression("");
			}

			copyCharterContract(vesselCharter, true, false);
		}
	}

	public void updateEndConditions(@NonNull final VesselCharter vesselCharter, final InclusionRecord inclusionRecord) {
		final PortVisit portVisit;
		if (inclusionRecord.object instanceof Cargo cargo) {
			portVisit = inclusionRecord.event.get(cargo.getSortedSlots().get(0));
		} else if (inclusionRecord.object instanceof VesselEvent) {
			portVisit = inclusionRecord.event.get(inclusionRecord.object);
		} else {
			// assert false;
			throw new IllegalStateException();
		}

		vesselCharter.getEndAt().clear();
		// Standard case
		vesselCharter.getEndAt().add(portVisit.getPort());
		// Special case for charter outs start/end ports can differ
		if (portVisit instanceof VesselEventVisit vesselEventVisit) {
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			if (vesselEvent instanceof CharterOutEvent charterOutEvent) {
 				if (charterOutEvent.isSetRelocateTo()) {
					final Port port = charterOutEvent.getPort();
					vesselCharter.getEndAt().clear();
					vesselCharter.getEndAt().add(port);
				}
			}
		}

		vesselCharter.setEndAfter(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());
		vesselCharter.setEndBy(portVisit.getStart().withZoneSameInstant(ZONEID_UTC).toLocalDateTime());

		if (vesselCharter.getEndHeel() == null) {
			vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());
		}

		// Set must arrive cold with target heel volume
		final int heel = portVisit.getHeelAtStart();
		if (heel > 0 || portVisit.getPreviousEvent() instanceof Cooldown) {
			vesselCharter.getEndHeel().setMinimumEndHeel(heel);
			vesselCharter.getEndHeel().setMaximumEndHeel(heel);
			vesselCharter.getEndHeel().setPriceExpression("");
			if (portVisit.getPreviousEvent() instanceof Cooldown) {
				// We had a cooldown before, so end either way
				vesselCharter.getEndHeel().setTankState(EVesselTankState.EITHER);
			} else {
				vesselCharter.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
			}
		} else {
			vesselCharter.getEndHeel().setMinimumEndHeel(0);
			vesselCharter.getEndHeel().setMaximumEndHeel(0);
			vesselCharter.getEndHeel().setPriceExpression("");
			vesselCharter.getEndHeel().setTankState(EVesselTankState.MUST_BE_WARM);
		}

		copyCharterContract(vesselCharter, false, true);
	}

	@NonNull
	public InclusionChecker getInclusionChecker() {
		return inclusionChecker;
	}

	public void setInclusionChecker(@NonNull final InclusionChecker inclusionChecker) {
		this.inclusionChecker = inclusionChecker;
	}

	public void trimSpotMarketCurves(@NonNull final PeriodRecord periodRecord, @NonNull final LNGScenarioModel scenario, final LNGScenarioModel wholeScenario) {
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

		if (wholeScenario != null) {
			final Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTimesForWholeScenario = LNGScenarioUtils.findEarliestAndLatestTimes(wholeScenario);
			// Make sure the spot markets do no start any earlier than in the parent scenario
			if (earliestAndLatestTimesForWholeScenario.getFirst().isAfter(earliestDate)) {
				earliestDate = earliestAndLatestTimesForWholeScenario.getFirst();
			}
			// Make sure the spot markets do no finish any later than in the parent scenario
			if (earliestAndLatestTimesForWholeScenario.getSecond().isBefore(latestDate)) {
				latestDate = earliestAndLatestTimesForWholeScenario.getSecond();
			}
		}
		// Sanity re-check!
		if (earliestDate == null) {
			throw new IllegalStateException("Unable to find earliest scenario date");
		}
		if (latestDate == null) {
			throw new IllegalStateException("Unable to find latest scenario date");
		}

		trimSpotMarketCurves(spotMarketsModel.getDesPurchaseSpotMarket(), earliestDate, latestDate);
		trimSpotMarketCurves(spotMarketsModel.getDesSalesSpotMarket(), earliestDate, latestDate);
		trimSpotMarketCurves(spotMarketsModel.getFobPurchasesSpotMarket(), earliestDate, latestDate);
		trimSpotMarketCurves(spotMarketsModel.getFobSalesSpotMarket(), earliestDate, latestDate);
	}

	public void trimSpotMarketCurves(@Nullable final SpotMarketGroup spotMarketGroup, @NonNull final ZonedDateTime earliestDate, @NonNull final ZonedDateTime latestDate) {
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
