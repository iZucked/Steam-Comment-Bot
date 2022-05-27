/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.actions.IRollForwardChange;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class UpdateVesselCharterChange implements IRollForwardChange {

	private final VesselCharter charter;
	private final CompoundCommand command;

	public UpdateVesselCharterChange(@NonNull final VesselCharter vesselCharter, @NonNull final Slot slot, @Nullable StartHeelOptions heelOptions, @NonNull final EditingDomain domain) {
		this.charter = vesselCharter;
		this.command = new CompoundCommand("Update start date");

		ZonedDateTime dt = slot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(ZoneId.of("UTC"));

		LocalDateTime startTime = dt.toLocalDateTime();
		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, startTime));
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
		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_BY, startTime));
		appendUpdatePortCommand(vesselCharter, domain, slot.getPort());
		appendUpdateHeelCommand(vesselCharter, domain, heelOptions);
	}

	public UpdateVesselCharterChange(@NonNull final VesselCharter vesselCharter, @NonNull final SlotVisit slotVisit, @Nullable StartHeelOptions heelOptions,
			@NonNull final EditingDomain domain) {
		this.charter = vesselCharter;
		this.command = new CompoundCommand("Update start date");
		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, slotVisit.getStart()));
		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_BY, slotVisit.getStart()));
		appendUpdatePortCommand(vesselCharter, domain, slotVisit.getPort());
		appendUpdateHeelCommand(vesselCharter, domain, heelOptions);
	}

	public UpdateVesselCharterChange(@NonNull final VesselCharter vesselCharter, @NonNull final VesselEvent vesselEvent, @Nullable StartHeelOptions heelOptions,
			@NonNull final EditingDomain domain) {
		this.charter = vesselCharter;
		this.command = new CompoundCommand("Update start date");

		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, vesselEvent.getStartAfter()));
		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_BY, vesselEvent.getStartBy()));
		appendUpdatePortCommand(vesselCharter, domain, vesselEvent.getPort());
		appendUpdateHeelCommand(vesselCharter, domain, heelOptions);
	}

	public UpdateVesselCharterChange(@NonNull final VesselCharter vesselCharter, @NonNull final VesselEventVisit vesselEventVisit, @Nullable StartHeelOptions heelOptions,
			@NonNull final EditingDomain domain) {
		this.charter = vesselCharter;
		this.command = new CompoundCommand("Update start date");

		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, vesselEventVisit.getStart()));
		this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_BY, vesselEventVisit.getStart()));
		appendUpdatePortCommand(vesselCharter, domain, vesselEventVisit.getPort());
		appendUpdateHeelCommand(vesselCharter, domain, heelOptions);
	}

	/**
	 * Protected constructor to help unit tests
	 * 
	 * @param vesselCharter
	 * @param domain
	 */
	protected UpdateVesselCharterChange(@NonNull final VesselCharter vesselCharter, @NonNull final EditingDomain domain) {
		this.charter = vesselCharter;
		this.command = new CompoundCommand("Update start date");
	}

	@Override
	@NonNull
	public EObject getChangedObject() {
		return charter;
	}

	@Override
	@NonNull
	public String getMessage() {
		return String.format("Updated vessel %s's start availability", charter.getVessel().getName());
	}

	@Override
	@NonNull
	public Command getCommand() {
		return command;
	}

	protected void appendUpdatePortCommand(final VesselCharter vesselCharter, final EditingDomain domain, final Port port) {
		final Port current = vesselCharter.getStartAt();
		if (current != port) {
			this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AT, port));
		}
	}

	protected void appendUpdateHeelCommand(@NonNull final VesselCharter vesselCharter, @NonNull final EditingDomain domain, @Nullable StartHeelOptions heelOptions) {

		if (heelOptions == null) {
			this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_HEEL, SetCommand.UNSET_VALUE));
		} else {
			this.command.append(SetCommand.create(domain, vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_HEEL, heelOptions));
		}
	}
}