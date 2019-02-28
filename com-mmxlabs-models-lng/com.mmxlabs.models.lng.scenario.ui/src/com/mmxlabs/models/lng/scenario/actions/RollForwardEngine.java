package com.mmxlabs.models.lng.scenario.actions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

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
import com.mmxlabs.models.lng.scenario.actions.impl.FixSlotWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FixVesselEventWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeCargoChange;
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

		public RollForwardDescriptor() {
			super();
		}
	}

	public List<IRollForwardChange> generateChanges(@NonNull final EditingDomain domain, @NonNull final LNGScenarioModel scenarioModel, @NonNull final RollForwardDescriptor descriptor) {

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
			examineSlotsAndCargoes(freezeDate, removeDate, cargoModel, slotsToRemove, slotsToFreeze, cargoesToFreeze, cargoesToRemove);
			examineVesselEvents(freezeDate, removeDate, cargoModel, eventsToRemove, eventsToFreeze);
		}

		// Generate the changes
		{
			generateCargoAndEventChanges(domain, scenarioModel, changes, cargoesToRemove, cargoesToFreeze, eventsToRemove, eventsToFreeze, slotsToRemove, slotsToFreeze);
		}

		final Boolean updateVessels = (Boolean) descriptor.get(RollForwardDescriptor.UpdateVessels);
		final Boolean removeVessels = (Boolean) descriptor.get(RollForwardDescriptor.RemoveVessels);

		if (updateVessels != null && updateVessels) {
			// Update vessel start states
			final Map<VesselAvailability, EObject> firstCommitment = new HashMap<>();
			final Set<VesselAvailability> vesselsToRemove = new HashSet<>();
			{
				examineVesselAvailabilities(scheduleModel, removeDate, slotsToFreeze, cargoesToRemove, eventsToRemove, eventsToFreeze, removeVessels, firstCommitment, vesselsToRemove);
			}

			// Generate vessel changes
			{
				generateVesselAvailabilityChanges(domain, changes, firstCommitment, vesselsToRemove);
			}
		}

		return changes;

	}

	protected void generateVesselAvailabilityChanges(@NonNull final EditingDomain domain, @NonNull final List<IRollForwardChange> changes,
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

	protected void examineVesselAvailabilities(@NonNull final ScheduleModel scheduleModel, @Nullable final LocalDate removeDate, @NonNull final List<Slot<?>> slotsToFreeze,
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
							if (firstDate == null || firstDate.isAfter(slot.getWindowStartWithSlotOrPortTime())) {
								firstDate = slot.getWindowStartWithSlotOrPortTime();
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

	protected void generateCargoAndEventChanges(@NonNull final EditingDomain domain, @NonNull final LNGScenarioModel scenarioModel, @NonNull final List<IRollForwardChange> changes,
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

	protected void examineVesselEvents(@Nullable final LocalDate freezeDate, @Nullable final LocalDate removeDate, @NonNull final CargoModel cargoModel,
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

	protected void examineSlotsAndCargoes(@Nullable final LocalDate freezeDate, @Nullable final LocalDate removeDate, @NonNull final CargoModel cargoModel, @NonNull final List<Slot<?>> slotsToRemove,
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

				LocalDateTime dt = s.getWindowStartWithSlotOrPortTime().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

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

			LocalDateTime dt = s.getWindowStartWithSlotOrPortTime().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

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

			LocalDateTime dt = s.getWindowStartWithSlotOrPortTime().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

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
