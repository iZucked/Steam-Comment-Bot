/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Default implementation of {@link PortOptions}.
 * 
 * @author Simon Goodall
 * 
 */
public final class PortOptions implements IOptionsSequenceElement {
	private int visitDuration;
	private IVessel vessel;
	private @NonNull IPortSlot portSlot;
	private int cargoCV;

	public PortOptions(@NonNull final IPortSlot portSlot) {
		this.portSlot = portSlot;

	}

	public PortOptions(final int visitDuration, final IVessel vessel, @NonNull final IPortSlot portSlot) {
		this.portSlot = portSlot;
		setVisitDuration(visitDuration);
		setVessel(vessel);
	}

	public PortOptions(final PortOptions options) {
		this.portSlot = options.getPortSlot();
		setVisitDuration(options.getVisitDuration());
		setVessel(options.getVessel());
		setCargoCVValue(options.getCargoCVValue());
	}

	public final int getVisitDuration() {
		return visitDuration;
	}

	@NonNull
	public final IPortSlot getPortSlot() {
		return portSlot;
	}

	public final IVessel getVessel() {
		return vessel;
	}

	public final void setVisitDuration(final int t) {
		this.visitDuration = t;
	}

	public final void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	public final void setPortSlot(final @NonNull IPortSlot portSlot) {
		this.portSlot = portSlot;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof PortOptions) {
			final PortOptions vo = (PortOptions) obj;

			if (cargoCV != vo.cargoCV) {
				return false;
			}
			if (visitDuration != vo.visitDuration) {
				return false;
			}

			if (!Objects.equals(vessel, vo.vessel)) {
				return false;
			}
			if (!Objects.equals(portSlot, vo.portSlot)) {
				return false;
			}

			return true;
		}
		return false;
	}

	public final @NonNull PortOptions copy() {

		return new PortOptions(this);
	}

	@Override
	public String toString() {
		return "PortOptions [availableTime=" + visitDuration + ", vessel=" + vessel + ", portSlot=" + portSlot + " , cargoCV=" + cargoCV + "]";
	}

	@Override
	public final int hashCode() {
		return Objects.hash(visitDuration, portSlot, vessel, cargoCV);
	}

	public int getCargoCVValue() {
		return cargoCV;
	}

	public void setCargoCVValue(final int cargoCV) {
		this.cargoCV = cargoCV;
	}
}
