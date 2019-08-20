/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoUtils;
import com.mmxlabs.models.lng.scenario.actions.impl.FixSlotWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FixVesselEventWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeCargoChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeSlotChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeVesselEventChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveCargoChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveSlotChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveVesselAvailabilityChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveVesselEventChange;
import com.mmxlabs.models.lng.scenario.actions.impl.UpdateVesselAvailabilityChange;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class RollForwardEngine {

	@SuppressWarnings("serial")
	public final class RollForwardDescriptor extends HashMap<String, Object> {
		public static final String FreezeDate = "date-freeze";
		public static final String RemoveDate = "date-remove";
		public static final String RemoveVessels = "vessels-remove";
		public static final String UpdateVessels = "vessels-update";
		public static final String UpdateWindows = "windows-update";

		public RollForwardDescriptor() {
			super();
		}
	}

	/**
	 * Computes a list of changes that are necessary for the data model, if a particular date is "in the past".
	 * 
	 * @param descriptor A configuration object describing what changes should be made to the model.
	 * @param domain The editing domain for the data model.
	 * @param scenarioModel A data model relating to an LNG scenario.
	 * @param freezeDate The date before which changes cannot be made.
	 * @return A list of IRollForwardChange objects describing the changes necessary to the model, if a particular date is now "in the past".
	 */
	public List<IRollForwardChange> generateChanges(@NonNull final RollForwardDescriptor descriptor, @NonNull final EditingDomain domain, //
			@NonNull final LNGScenarioModel scenarioModel, @NonNull final LocalDate freezeDate, @Nullable List<IRollForwardChange> listToAppendTo) {

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
		// Step 1 - Sanity Check model
		{
			final Schedule schedule = scheduleModel.getSchedule();
			if (schedule == null || scheduleModel.isDirty()) {
				// Need to perform an evaluation first
				throw new IllegalStateException("Schedule is dirty - evaluate");
			}
		}

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

		// Check for items to remove or freeze

		final List<Slot<?>> slotsToFreeze = getSlotsToFreeze(freezeDate, cargoModel);
		final List<Cargo> cargoesToFreeze = getCargoesToFreeze(freezeDate, cargoModel);
		final List<VesselEvent> eventsToFreeze = getEventsToFreeze(freezeDate, cargoModel);

		List<IRollForwardChange> changes = generateFreezeChanges(domain, scenarioModel, cargoesToFreeze, eventsToFreeze, slotsToFreeze, listToAppendTo);
		
		final Boolean updateWindows = (Boolean) descriptor.get(RollForwardDescriptor.UpdateWindows);
		if (updateWindows) {
			changes = generateWindowUpdateChanges(domain, scenarioModel, cargoesToFreeze, eventsToFreeze, changes);
		}
		
		// TODO: create changes to fix the date on vessel events or other objects
		/*
		
		final Boolean updateVessels = (Boolean) descriptor.get(RollForwardDescriptor.UpdateVessels);

		if (updateVessels != null && updateVessels) {
			// Update vessel start states
			Map<VesselAvailability, EObject> firstCommitment = findFirstCommitments(scheduleModel, slotsToFreeze, eventsToFreeze);

			changes = generateVesselAvailabilityChanges(domain, firstCommitment, changes); 
		}
		*/

		return changes;

	}



	/**
	 * Returns a list of IRollForwardChange objects that update the start date on VesselAvailability objects, or appends these objects to a specified list.
	 * 
	 * @param domain The editing domain.
	 * @param firstCommitments A map from VesselAvailability objects to their first commitments.
	 * @param listToAppendTo An optional list to append the results to.
	 * @return The list to which the UpdateVesselAvailabilityChange objects has been appended.
	 */
	protected List<IRollForwardChange> generateVesselAvailabilityChanges(@NonNull final EditingDomain domain, 
			@NonNull final Map<VesselAvailability, EObject> firstCommitments, final List<IRollForwardChange> listToAppendTo) {
		// create an empty list if there wasn't one specified to append to
		List<IRollForwardChange> result = (listToAppendTo == null ? new LinkedList<IRollForwardChange>() : listToAppendTo); 
		
		for (final Map.Entry<VesselAvailability, EObject> e : firstCommitments.entrySet()) {
			final VesselAvailability vesselAvailability = e.getKey();
			final EObject eObject = e.getValue();
			// Dummy heel options. Should be derived from left over heel etc..
			// Enough to generally avoid arriving warm.
			final StartHeelOptions heelOptions = CargoFactory.eINSTANCE.createStartHeelOptions();
			heelOptions.setCvValue(22.8);
			heelOptions.setMaxVolumeAvailable(500);
			heelOptions.setMinVolumeAvailable(0);
			heelOptions.setPriceExpression("1");

			if (vesselAvailability != null) {
				if (eObject instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) eObject;
					result.add(new UpdateVesselAvailabilityChange(vesselAvailability, slotVisit, heelOptions, domain));
				} else if (eObject instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) eObject;
					result.add(new UpdateVesselAvailabilityChange(vesselAvailability, vesselEventVisit, heelOptions, domain));
				} else if (eObject instanceof Slot) {
					final Slot<?> slot = (Slot<?>) eObject;
					result.add(new UpdateVesselAvailabilityChange(vesselAvailability, slot, heelOptions, domain));
				} else if (eObject instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) eObject;
					result.add(new UpdateVesselAvailabilityChange(vesselAvailability, vesselEvent, heelOptions, domain));
				} else {
					// Error!
				}			
			}
		}
		
		return result;
	}

	
	/**
	 * Data class to store an object (VesselEventVisit, VesselEvent, SlotVisit or Slot) along with an associated date. Used by findFirstCommitment()
	 * 
	 * @author simonmcgregor
	 *
	 */
	private static class Commitment {
		public final EObject object;
		public final ZonedDateTime date;
		
		public Commitment(EObject o, ZonedDateTime d) {
			object = o;
			date = d;
		}
		
		public boolean isAfter(Commitment x) {
			return date.isAfter(x.date);
		}
	}
	
	/**
	 * Get the commitment (if any) associated with a particular Event. This will be either a VesselEventVisit, a VesselEvent, a SlotVisit, or a Slot. 
	 * 
	 * @param event
	 * @param slotsToFreeze
	 * @param eventsToFreeze
	 * @return
	 */
	protected Commitment getCommitment(@Nullable Event event, @NonNull final List<Slot<?>> slotsToFreeze,
			final List<VesselEvent> eventsToFreeze) {
		// for a VesselEventVisit, return the associated VesselEvent,
		// unless it is marked to be frozen, in which case return the VesselEventVisit
		if (event instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			
			if (eventsToFreeze.contains(vesselEvent)) {
				return new Commitment(vesselEventVisit, vesselEventVisit.getStart());
			}
			else {
				return new Commitment(vesselEvent, vesselEvent.getStartAfterAsDateTime());
			}						
		}
		// for a SlotVisit, return the associated Slot,
		// unless it is marked to be frozen, in which case return the SlotVisit
		else if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			final Slot<?> slot = slotVisit.getSlotAllocation().getSlot();

			if (slotsToFreeze.contains(slot)) {
				return new Commitment(slotVisit, slotVisit.getStart());
			} 
			else {
				return new Commitment(slot, slot.getSchedulingTimeWindow().getStart());
			}
			
		}
		
		// for any other event, return null 
		return null;		
		
	}
	
	/**
	 * Return a map from VesselAvailability objects to Commitment objects, representing the first commitment in each availability. 
	 * 
	 * @param scheduleModel
	 * @param slotsToFreeze
	 * @param eventsToFreeze
	 */
	protected @NonNull Map<VesselAvailability, EObject> findFirstCommitments(@NonNull final ScheduleModel scheduleModel, @NonNull final List<Slot<?>> slotsToFreeze,
			final List<VesselEvent> eventsToFreeze) {
		final HashMap<VesselAvailability, EObject> result = new HashMap<>();
		// Find first remaining event/cargoes on each vessel
		final Schedule schedule = scheduleModel.getSchedule();
		if (schedule != null) {
			// traverse every vessel schedule Sequence in the model
			for (final Sequence sequence : schedule.getSequences()) {

				final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
				if (vesselAvailability == null) {
					continue;
				}				
					
				Commitment firstCommitment = null;

				// traverse every event in the vessel's schedule
				for (final Event event : sequence.getEvents()) {
					// if it is the first commitment so far, store it temporarily
					Commitment commitment = getCommitment(event, slotsToFreeze, eventsToFreeze);
					if (firstCommitment == null || firstCommitment.isAfter(commitment)) {
						firstCommitment = commitment;
					}
				}

				// if there is a first commitment, store it in the method return result
				if (firstCommitment != null) {
					result.put(vesselAvailability, firstCommitment.object);
				}
			}
		}
		
		return result;
	}

	
	/**
	 * Returns a map from Slot or VesselEvent objects to the SlotVisit or VesselEventVisit objects they are associated with.
	 * 
	 * @param scheduleModel
	 * @return
	 */
	protected Map<Object, Event> getEventsForSlotOrVesselEvents(ScheduleModel scheduleModel) {
		final Map<Object, Event> result = new HashMap<>();
		final Schedule schedule = scheduleModel.getSchedule();
		
		// populate the objectToEventMap, mapping Slot objects to SlotVisit objects and VesselEvent objects to VesselEventVisit objects 
		if (schedule != null) {
			for (final Sequence seq : scheduleModel.getSchedule().getSequences()) {
				for (final Event evt : seq.getEvents()) {
					if (evt instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) evt;
						result.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
					} else if (evt instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
						result.put(vesselEventVisit.getVesselEvent(), vesselEventVisit);
					}
				}
			}
		}
		
		return result;
		
	}
	
	/**
	 * Generates a list of IRollForwardChange objects from a list of cargoes, events, and slots to freeze. Optionally, appends the result to @code{listToAppendTo}. The appended 
	 * objects should be instances of {@link FreezeSlotChange}, {@link FreezeCargoChange} or {@link FreezeVesselEventChange}.
	 * 
	 * @param domain
	 * @param scenarioModel
	 * @param cargoesToFreeze
	 * @param eventsToFreeze
	 * @param slotsToFreeze
	 * @param listToAppendTo A list to append the generated objects to. If <code>null</code>, create a new list.
	 * @return A new list of objects, or the original list with the new objects appended to it.
	 */
	protected List<IRollForwardChange> generateFreezeChanges(@NonNull final EditingDomain domain, @NonNull final LNGScenarioModel scenarioModel, 
			@NonNull final List<Cargo> cargoesToFreeze, @NonNull final List<VesselEvent> eventsToFreeze, final List<Slot<?>> slotsToFreeze, List<IRollForwardChange> listToAppendTo) {
		
		List<IRollForwardChange> result = (listToAppendTo == null ? new LinkedList<IRollForwardChange>() : listToAppendTo);
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
		
		for (final Cargo cargo : cargoesToFreeze) {
			result.add(new FreezeCargoChange(cargo, domain));
		}

		for (final Slot s : slotsToFreeze) {
		  result.add(new FreezeSlotChange(s, domain));
		}

		for (final VesselEvent e : eventsToFreeze) {
			result.add(new FreezeVesselEventChange(e, domain));
		}
		
		return result;
	}

	/**
	 * Generates a list of {@link IRollForwardChange} objects representing the collapse of historical slot window times. Optionally, appends the result to @code{listToAppendTo}.
	 * The appended objects should be instances of {@link FixSlotWindowChange} or {@link FixVesselEventWindowChange}.
	 * 
	 * @param domain
	 * @param scenarioModel
	 * @param cargoesToFreeze
	 * @param eventsToFreeze
	 * @param listToAppendTo A list to append the generated objects to. If <code>null</code>, create a new list.
	 * @return A new list of objects, or the original list with the new objects appended to it.
	 */
	protected List<IRollForwardChange> generateWindowUpdateChanges(@NonNull final EditingDomain domain, @NonNull final LNGScenarioModel scenarioModel, 
			@NonNull final List<Cargo> cargoesToFreeze, @NonNull final List<VesselEvent> eventsToFreeze, List<IRollForwardChange> listToAppendTo)
	{
		List<IRollForwardChange> result = (listToAppendTo == null ? new LinkedList<IRollForwardChange>() : listToAppendTo);
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
		
		Map<Object, Event> objectToEventMap = getEventsForSlotOrVesselEvents(scheduleModel);

		for (final Cargo cargo : cargoesToFreeze) {
			for (final Slot<?> slot : cargo.getSlots()) {
				final Event evt = objectToEventMap.get(slot);
				if (evt != null) {
					result.add(new FixSlotWindowChange(slot, evt.getStart(), domain));
				}
			}
		}


		for (final VesselEvent e : eventsToFreeze) {
			final Event evt = objectToEventMap.get(e);
			if (evt != null) {
				result.add(new FixVesselEventWindowChange(e, evt.getStart(), domain));
			}
		}
		
		return result;		
	}
	
	/**
	 * Returns a value indicating whether a particular Cargo should be frozen, based on a "freeze date" indicating when the prompt (i.e. the future) starts.
	 * 
	 * @param cargo
	 * @param freezeDate
	 * @return
	 */
	protected boolean shouldFreeze(@Nullable final Cargo cargo, @Nullable final LocalDate freezeDate)
	{
		// Current implementation: the cargo is only frozen if *all* its slots are in the past.
		
		// don't freeze if the cargo is null, or if the date is null
		if (cargo == null || freezeDate == null) {
			return false;
		}
		
		// a cargo should not be frozen unless its discharge slot occurs entirely before the cutoff date
		
		EList<Slot<?>> slots = cargo.getSortedSlots();
		Slot<?> slot = slots.get(slots.size() - 1);
		
		LocalDateTime windowEnd = slot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
		return windowEnd.toLocalDate().isBefore(freezeDate);
		

		// previous logic: freeze a cargo if any of its slots should be frozen
		/*
		for (final Slot<?> slot: cargo.getSlots()) {
			// don't freeze if there are any slots that shouldn't be frozen
			if (!shouldFreeze(slot, freezeDate)) {
				return false;
			}
		}
		
		
		// freeze if all slots should be frozen (including if there are no slots)
		return true;
		*/
	}
	
	/**
	 * Returns a value indicating whether a particular Slot should be frozen, based on a "freeze date" indicating when the prompt (i.e. the future) starts.
	 * 
	 * @param slot
	 * @param freezeDate
	 * @return
	 */
	protected boolean shouldFreeze(@Nullable final Slot<?> slot, @Nullable final LocalDate freezeDate)
	{
		// Current implementation: an open slot is frozen if its start window opens in the past.
		
		if (slot == null || freezeDate == null) {
			return false;
		}
		
		LocalDateTime dt = slot.getSchedulingTimeWindow().getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

		return dt.toLocalDate().isBefore(freezeDate);
	}
	

	/**
	 * Returns a value indicating whether a particular VesselEvent should be frozen, based on a "freeze date" indicating when the prompt (i.e. the future) starts.
	 * 
	 * @param event
	 * @param freezeDate
	 * @return
	 */
	protected boolean shouldFreeze(@Nullable final VesselEvent event, @Nullable final LocalDate freezeDate) {
		if (event == null || freezeDate == null) {
			return false;
		}
		
		LocalDateTime earliestStart = event.getStartAfter();
		return earliestStart.toLocalDate().isBefore(freezeDate);		
	}


	/**
	 * Returns a list of vessel events in the specified cargo model that should be frozen if history starts at the specified date.
	 * @param freezeDate
	 * @param cargoModel
	 * @return
	 */
	protected @NonNull List<VesselEvent> getEventsToFreeze(@Nullable final LocalDate freezeDate, @NonNull final CargoModel cargoModel) 
	{
		List<VesselEvent> result = new LinkedList<>();
		
		for (final VesselEvent event: cargoModel.getVesselEvents()) {
			if (shouldFreeze(event, freezeDate)) {
				result.add(event);
			}
		}
		
		
		return result;
	}
	
	/**
	 * Returns a list of slots in the specified cargo model that should be frozen if history starts at the specified date.
	 * @param freezeDate
	 * @param cargoModel
	 * @return
	 */
	protected @NonNull List<Slot<?>> getSlotsToFreeze(@Nullable final LocalDate freezeDate, @NonNull final CargoModel cargoModel)
	{
		List<Slot<?>> result = new ArrayList<>();
		
		// N.B. Current implementation: freeze only "open" slots in the past (i.e. not attached to a cargo).
		// This is because freezing slots that belong to a cargo causes a validation error.
		
		for (final Slot<?> slot: CargoUtils.getOpenSlotsIterable(cargoModel)) {
			if (shouldFreeze(slot, freezeDate)) {
				result.add(slot);
			}			
		}
				
		return result;
	}
	
	/**
	 * Returns a list of cargoes in the specified cargo model that should be frozen if history starts at the specified date.
	 * @param freezeDate
	 * @param cargoModel
	 * @return
	 */
	protected @NonNull List<Cargo> getCargoesToFreeze(@NonNull LocalDate freezeDate, CargoModel cargoModel) {
		List<Cargo> result = new LinkedList<>();
		
		for (final Cargo cargo: cargoModel.getCargoes()) 
		{
			if (shouldFreeze(cargo, freezeDate)) {
				result.add(cargo);
			}
		}
		
		return result;
	}
	
	// obsolete code in pre-refactored form, using removals logic
	public List<IRollForwardChange> generateChangesObsolete(@NonNull final EditingDomain domain, @NonNull final LNGScenarioModel scenarioModel, @NonNull final RollForwardDescriptor descriptor) {

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
		// Step 1 - Sanity Check model
		{
			final Schedule schedule = scheduleModel.getSchedule();
			if (schedule == null || scheduleModel.isDirty()) {
				// Need to perform an evaluation first
				throw new IllegalStateException("Schedule is dirty - evaluate");
			}
		}

		final List<IRollForwardChange> changes = new LinkedList<>();

		final LocalDate freezeDate = (LocalDate) descriptor.get(RollForwardDescriptor.FreezeDate);
		final LocalDate removeDate = (LocalDate) descriptor.get(RollForwardDescriptor.RemoveDate);

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

		// Check for items to remove or freeze

		// Slots not linked to cargoes
		final List<Slot<?>> slotsToRemove = new LinkedList<>();
		final List<Slot<?>> slotsToFreeze = new LinkedList<>();

		final List<Cargo> cargoesToFreeze = new LinkedList<>();
		final List<Cargo> cargoesToRemove = new LinkedList<>();

		final List<VesselEvent> eventsToRemove = new LinkedList<>();
		final List<VesselEvent> eventsToFreeze = new LinkedList<>();

		{
			examineSlotsAndCargoesObsolete(freezeDate, removeDate, cargoModel, slotsToRemove, slotsToFreeze, cargoesToFreeze, cargoesToRemove);
			examineVesselEventsObsolete(freezeDate, removeDate, cargoModel, eventsToRemove, eventsToFreeze);
		}

		// Generate the changes
		{
			generateCargoAndEventChangesObsolete(domain, scenarioModel, changes, cargoesToRemove, cargoesToFreeze, eventsToRemove, eventsToFreeze, slotsToRemove, slotsToFreeze);
		}

		final Boolean updateVessels = (Boolean) descriptor.get(RollForwardDescriptor.UpdateVessels);
		final Boolean removeVessels = (Boolean) descriptor.get(RollForwardDescriptor.RemoveVessels);

		if (updateVessels != null && updateVessels) {
			// Update vessel start states
			final Map<VesselAvailability, EObject> firstCommitment = new HashMap<>();
			final Set<VesselAvailability> vesselsToRemove = new HashSet<>();
			{
				examineVesselAvailabilitiesObsolete(scheduleModel, removeDate, slotsToFreeze, cargoesToRemove, eventsToRemove, eventsToFreeze, removeVessels, firstCommitment, vesselsToRemove);
			}

			// Generate vessel changes
			{
				generateVesselAvailabilityChangesObsolete(domain, changes, firstCommitment, vesselsToRemove);
			}
		}

		return changes;

	}

	// obsolete code in pre-refactored form, using removals logic
	protected void generateVesselAvailabilityChangesObsolete(@NonNull final EditingDomain domain, @NonNull final List<IRollForwardChange> changes,
			@NonNull final Map<VesselAvailability, EObject> firstCommitment, @NonNull final Set<VesselAvailability> vesselsToRemove) {
		for (final Map.Entry<VesselAvailability, EObject> e : firstCommitment.entrySet()) {
			final VesselAvailability vesselAvailability = e.getKey();
			final EObject eObject = e.getValue();
			// Dummy heel options. Should be derived from left over heel etc..
			// Enough to generally avoid arriving warm.
			final StartHeelOptions heelOptions = CargoFactory.eINSTANCE.createStartHeelOptions();
			heelOptions.setCvValue(22.8);
			heelOptions.setMaxVolumeAvailable(500);
			heelOptions.setMinVolumeAvailable(0);
			heelOptions.setPriceExpression("1");

			if (eObject instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) eObject;
				changes.add(new UpdateVesselAvailabilityChange(vesselAvailability, slotVisit, heelOptions, domain));
			} else if (eObject instanceof VesselEventVisit) {
				final VesselEventVisit vesselEventVisit = (VesselEventVisit) eObject;
				changes.add(new UpdateVesselAvailabilityChange(vesselAvailability, vesselEventVisit, heelOptions, domain));
			} else if (eObject instanceof Slot) {
				final Slot<?> slot = (Slot<?>) eObject;
				changes.add(new UpdateVesselAvailabilityChange(vesselAvailability, slot, heelOptions, domain));
			} else if (eObject instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) eObject;
				changes.add(new UpdateVesselAvailabilityChange(vesselAvailability, vesselEvent, heelOptions, domain));
			} else {
				// Error!
			}
		}

		for (final VesselAvailability vesselAvailability : vesselsToRemove) {
			changes.add(new RemoveVesselAvailabilityChange(vesselAvailability, domain));
		}
	}

	// obsolete code in pre-refactored form, using removals logic
	protected void examineVesselAvailabilitiesObsolete(@NonNull final ScheduleModel scheduleModel, @Nullable final LocalDate removeDate, @NonNull final List<Slot<?>> slotsToFreeze,
			@NonNull final List<Cargo> cargoesToRemove, @NonNull final List<VesselEvent> eventsToRemove, final List<VesselEvent> eventsToFreeze, @Nullable final Boolean removeVessels,
			@NonNull final Map<VesselAvailability, EObject> firstCommitment, @NonNull final Set<VesselAvailability> vesselsToRemove) {
		// Find first remaining event/cargoes on each vessel
		final Schedule schedule = scheduleModel.getSchedule();
		if (schedule != null) {
			for (final Sequence sequence : schedule.getSequences()) {

				final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
				if (vesselAvailability == null) {
					continue;
				}

				if (removeVessels != null && removeDate != null && removeVessels) {
					if (vesselAvailability.isSetEndBy()) {
						if (removeDate.atStartOfDay().isAfter(vesselAvailability.getEndBy())) {
							vesselsToRemove.add(vesselAvailability);
							continue;
						}
					}
				}

				ZonedDateTime firstDate = null;
				EObject firstObject = null;

				for (final Event event : sequence.getEvents()) {
					if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
						if (eventsToRemove.contains(vesselEvent)) {
							continue;
						}
						if (eventsToFreeze.contains(vesselEvent)) {
							// Get fixed start date - get scheduled time
							if (firstDate == null || firstDate.isAfter(vesselEventVisit.getStart())) {

								firstObject = vesselEventVisit;
								firstDate = vesselEventVisit.getStart();
							}
						} else {
							if (firstDate == null || firstDate.isAfter(vesselEvent.getStartAfterAsDateTime())) {
								firstDate = vesselEvent.getStartAfterAsDateTime();
								firstObject = vesselEvent;
							}
						}

					} else if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						final Slot<?> slot = slotVisit.getSlotAllocation().getSlot();

						if (cargoesToRemove.contains(slot.getCargo())) {
							continue;
						}
						if (slotsToFreeze.contains(slot)) {
							if (firstDate == null || firstDate.isAfter(slotVisit.getStart())) {
								firstDate = slotVisit.getStart();
								firstObject = slotVisit;
							}
						} else {
							if (firstDate == null || firstDate.isAfter(slot.getSchedulingTimeWindow().getStart())) {
								firstDate = slot.getSchedulingTimeWindow().getStart();
								firstObject = slot;
							}
						}
					}
				}

				if (firstObject != null) {
					firstCommitment.put(vesselAvailability, firstObject);
				}
			}
		}
	}

	// obsolete code in pre-refactored form, using removals logic
	protected void generateCargoAndEventChangesObsolete(@NonNull final EditingDomain domain, @NonNull final LNGScenarioModel scenarioModel, @NonNull final List<IRollForwardChange> changes,
			final List<Cargo> cargoesToRemove, @NonNull final List<Cargo> cargoesToFreeze, @NonNull final List<VesselEvent> eventsToRemove, @NonNull final List<VesselEvent> eventsToFreeze,
			@NonNull final List<Slot<?>> slotsToRemove, final List<Slot<?>> slotsToFreeze) {
		final Map<Object, Event> objectToEventMap = new HashMap<>();
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		if (schedule != null) {
			for (final Sequence seq : scheduleModel.getSchedule().getSequences()) {
				for (final Event evt : seq.getEvents()) {
					if (evt instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) evt;
						objectToEventMap.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
					} else if (evt instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
						objectToEventMap.put(vesselEventVisit.getVesselEvent(), vesselEventVisit);
					}
				}
			}
		}

		for (final Cargo c : cargoesToRemove) {
			for (final Slot<?> s : c.getSlots()) {
				changes.add(new RemoveSlotChange(s, domain));
			}
			changes.add(new RemoveCargoChange(c, domain));
		}
		for (final Slot<?> s : slotsToRemove) {
			changes.add(new RemoveSlotChange(s, domain));
		}
		for (final VesselEvent e : eventsToRemove) {
			changes.add(new RemoveVesselEventChange(e, domain));
		}

		for (final Cargo c : cargoesToFreeze) {
			changes.add(new FreezeCargoChange(c, domain));
			for (final Slot<?> s : c.getSlots()) {
				final Event evt = objectToEventMap.get(s);
				if (evt != null) {
					changes.add(new FixSlotWindowChange(s, evt.getStart(), domain));
				}
			}
		}
		// Can't freeze slots...
		// for (final Slot s : slotsToFreeze) {
		// changes.add(new FreezeSlotChange(s, domain));
		// }

		for (final VesselEvent e : eventsToFreeze) {
			changes.add(new FreezeVesselEventChange(e, domain));
			final Event evt = objectToEventMap.get(e);
			if (evt != null) {
				changes.add(new FixVesselEventWindowChange(e, evt.getStart(), domain));
			}
		}
	}

	// obsolete code in pre-refactored form, using removals logic
	protected void examineVesselEventsObsolete(@Nullable final LocalDate freezeDate, @Nullable final LocalDate removeDate, @NonNull final CargoModel cargoModel,
			@NonNull final List<VesselEvent> eventsToRemove, @NonNull final List<VesselEvent> eventsToFreeze) {

		if (removeDate == null && freezeDate == null) {
			return;
		}

		if (removeDate != null && freezeDate != null) {
			Preconditions.checkArgument(removeDate.isBefore(freezeDate) || removeDate.equals(freezeDate), "Remove date should be the same or before freeze date");
		}
		for (final VesselEvent e : cargoModel.getVesselEvents()) {
			boolean couldFreeze = false;
			boolean couldRemove;

			LocalDateTime dt = e.getStartAfter();
			if (freezeDate != null && dt.toLocalDate().isBefore(freezeDate)) {
				couldFreeze = true;
			}
			dt = dt.plusDays(e.getDurationInDays());
			if (removeDate != null && dt.toLocalDate().isBefore(removeDate)) {
				couldRemove = true;
			} else {
				couldRemove = false;
			}

			if (couldRemove) {
				eventsToRemove.add(e);
			} else if (couldFreeze) {
				eventsToFreeze.add(e);
			}
		}
	}

	// obsolete code in pre-refactored form, using removals logic
	protected void examineSlotsAndCargoesObsolete(@Nullable final LocalDate freezeDate, @Nullable final LocalDate removeDate, @NonNull final CargoModel cargoModel, @NonNull final List<Slot<?>> slotsToRemove,
			@NonNull final List<Slot<?>> slotsToFreeze, @NonNull final List<Cargo> cargoesToFreeze, @NonNull final List<Cargo> cargoesToRemove) {

		if (removeDate == null && freezeDate == null) {
			return;
		}

		if (removeDate != null && freezeDate != null) {
			Preconditions.checkArgument(removeDate.isBefore(freezeDate) || removeDate.equals(freezeDate), "Remove date should be the same or before freeze date");
		}

		final Set<Slot<?>> seenSlots = new HashSet<>();
		for (final Cargo cargo : cargoModel.getCargoes()) {
			boolean couldFreeze = false;
			boolean couldRemove = true;
			for (final Slot<?> s : cargo.getSlots()) {
				// Mark slot as seen.
				seenSlots.add(s);

				LocalDateTime dt = s.getSchedulingTimeWindow().getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

				if (freezeDate != null && dt.toLocalDate().isBefore(freezeDate)) {
					couldFreeze = true;
				}

				dt = dt.plusHours(s.getDuration());

				if (removeDate != null && dt.toLocalDate().isBefore(removeDate)) {
					couldRemove &= true;
				} else {
					couldRemove = false;
				}
			}

			if (couldRemove) {
				// All slots are removable
				cargoesToRemove.add(cargo);
			} else if (couldFreeze) {
				cargoesToFreeze.add(cargo);
			}
		}

		for (final Slot<?> s : cargoModel.getLoadSlots()) {
			if (seenSlots.contains(s)) {
				continue;
			}
			boolean couldFreeze = false;
			boolean couldRemove;

			LocalDateTime dt = s.getSchedulingTimeWindow().getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

			if (freezeDate != null && dt.toLocalDate().isBefore(freezeDate)) {
				couldFreeze = true;
			}

			dt = dt.plusHours(s.getDuration());

			if (removeDate != null && dt.toLocalDate().isBefore(removeDate)) {
				couldRemove = true;
			} else {
				couldRemove = false;
			}

			if (couldRemove) {
				slotsToRemove.add(s);
			} else if (couldFreeze) {
				slotsToFreeze.add(s);
			}
		}

		for (final Slot<?> s : cargoModel.getDischargeSlots()) {
			if (seenSlots.contains(s)) {
				continue;
			}
			boolean couldFreeze = false;
			boolean couldRemove;

			LocalDateTime dt = s.getSchedulingTimeWindow().getStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

			if (freezeDate != null && dt.toLocalDate().isBefore(freezeDate)) {
				couldFreeze = true;
			}

			dt = dt.plusHours(s.getDuration());

			if (removeDate != null && dt.toLocalDate().isBefore(removeDate)) {
				couldRemove = true;
			} else {
				couldRemove = false;
			}

			if (couldRemove) {
				slotsToRemove.add(s);
			} else if (couldFreeze) {
				slotsToFreeze.add(s);
			}
		}
	}
}
