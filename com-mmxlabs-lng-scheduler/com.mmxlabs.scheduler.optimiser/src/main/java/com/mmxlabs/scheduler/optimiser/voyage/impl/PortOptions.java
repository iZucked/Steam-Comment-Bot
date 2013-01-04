/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.google.common.base.Objects;
import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Default implementation of {@link PortOptions}. This is @link {Cloneable} for use with @link{VoyagePlanOptimiser} use.
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public final class PortOptions implements Cloneable {
	private int visitDuration;
	private IVessel vessel;
	private IPortSlot portSlot;

	
	public PortOptions() {

	}
	
	public PortOptions(int visitDuration, IVessel vessel, IPortSlot portSlot, VesselState vesselState) {
		setVisitDuration(visitDuration);
		setVessel(vessel);
		setPortSlot(portSlot);
	}
	
	
	public PortOptions(final PortOptions options) {
		setVisitDuration(options.getVisitDuration());
		setVessel(options.getVessel());
		setPortSlot(options.getPortSlot());
	}

	public final int getVisitDuration() {
		return visitDuration;
	}

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

	public final void setPortSlot(final IPortSlot portSlot) {
		this.portSlot = portSlot;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof PortOptions) {
			final PortOptions vo = (PortOptions) obj;

			if (visitDuration != vo.visitDuration) {
				return false;
			}

			if (!Equality.isEqual(vessel, vo.vessel)) {
				return false;
			}
			if (!Equality.isEqual(portSlot, vo.portSlot)) {
				return false;
			}

			return true;
		}
		return false;
	}

	@Override
	public final PortOptions clone()  {

		return new PortOptions(this);
	}

	@Override
	public String toString() {
		return "PortOptions [availableTime=" + visitDuration + ", vessel=" + vessel + ", portSlot=" + portSlot + "]";
	}

	@Override
	public final int hashCode() {
		return Objects.hashCode(visitDuration, portSlot, vessel);
	}
}
