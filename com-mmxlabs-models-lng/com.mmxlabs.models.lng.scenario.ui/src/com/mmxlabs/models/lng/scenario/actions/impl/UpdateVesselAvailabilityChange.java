/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class UpdateVesselAvailabilityChange implements IRollForwardChange {

	private final VesselAvailability availability;
	private final CompoundCommand command;

	public UpdateVesselAvailabilityChange(@NonNull final VesselAvailability vesselAvailability, @NonNull final Slot slot, @Nullable StartHeelOptions heelOptions, @NonNull final EditingDomain domain) {
		this.availability = vesselAvailability;
		this.command = new CompoundCommand("Update start date");

		ZonedDateTime dt = slot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(ZoneId.of("UTC"));

		LocalDateTime startTime = dt.toLocalDateTime();
		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER, startTime));
		SchedulingTimeWindow tw = slot.getSchedulingTimeWindow();
		int value = tw.getSize();
		switch (tw.getSizeUnits()) {
		case DAYS:
			startTime = startTime.plusDays(value);
			break;
		case HOURS:
			startTime = startTime.plusHours(value);
			break;
		case MONTHS:
			startTime = startTime.plusMonths(value);
			break;
		default:
			throw new IllegalStateException();
		}
		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY, startTime));
		appendUpdatePortCommand(vesselAvailability, domain, slot.getPort());
		appendUpdateHeelCommand(vesselAvailability, domain, heelOptions);
	}

	public UpdateVesselAvailabilityChange(@NonNull final VesselAvailability vesselAvailability, @NonNull final SlotVisit slotVisit, @Nullable StartHeelOptions heelOptions,
			@NonNull final EditingDomain domain) {
		this.availability = vesselAvailability;
		this.command = new CompoundCommand("Update start date");
		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER, slotVisit.getStart()));
		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY, slotVisit.getStart()));
		appendUpdatePortCommand(vesselAvailability, domain, slotVisit.getPort());
		appendUpdateHeelCommand(vesselAvailability, domain, heelOptions);
	}

	public UpdateVesselAvailabilityChange(@NonNull final VesselAvailability vesselAvailability, @NonNull final VesselEvent vesselEvent, @Nullable StartHeelOptions heelOptions,
			@NonNull final EditingDomain domain) {
		this.availability = vesselAvailability;
		this.command = new CompoundCommand("Update start date");

		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER, vesselEvent.getStartAfter()));
		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY, vesselEvent.getStartBy()));
		appendUpdatePortCommand(vesselAvailability, domain, vesselEvent.getPort());
		appendUpdateHeelCommand(vesselAvailability, domain, heelOptions);
	}

	public UpdateVesselAvailabilityChange(@NonNull final VesselAvailability vesselAvailability, @NonNull final VesselEventVisit vesselEventVisit, @Nullable StartHeelOptions heelOptions,
			@NonNull final EditingDomain domain) {
		this.availability = vesselAvailability;
		this.command = new CompoundCommand("Update start date");

		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER, vesselEventVisit.getStart()));
		this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY, vesselEventVisit.getStart()));
		appendUpdatePortCommand(vesselAvailability, domain, vesselEventVisit.getPort());
		appendUpdateHeelCommand(vesselAvailability, domain, heelOptions);
	}

	/**
	 * Protected constructor to help unit tests
	 * 
	 * @param vesselAvailability
	 * @param domain
	 */
	protected UpdateVesselAvailabilityChange(@NonNull final VesselAvailability vesselAvailability, @NonNull final EditingDomain domain) {
		this.availability = vesselAvailability;
		this.command = new CompoundCommand("Update start date");
	}

	@Override
	@NonNull
	public EObject getChangedObject() {
		return availability;
	}

	@Override
	@NonNull
	public String getMessage() {
		return String.format("Updated vessel %s's start availability", availability.getVessel().getName());
	}

	@Override
	@NonNull
	public Command getCommand() {
		return command;
	}

	protected void appendUpdatePortCommand(final VesselAvailability vesselAvailability, final EditingDomain domain, final Port port) {
		final Port current = vesselAvailability.getStartAt();
		if (current != port) {
			this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AT, port));
		}
	}

	protected void appendUpdateHeelCommand(@NonNull final VesselAvailability vesselAvailability, @NonNull final EditingDomain domain, @Nullable StartHeelOptions heelOptions) {

		if (heelOptions == null) {
			this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL, SetCommand.UNSET_VALUE));
		} else {
			this.command.append(SetCommand.create(domain, vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL, heelOptions));
		}
	}
}