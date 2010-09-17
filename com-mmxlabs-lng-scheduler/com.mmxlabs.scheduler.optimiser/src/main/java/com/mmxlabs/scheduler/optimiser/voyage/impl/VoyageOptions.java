package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;

/**
 * Default implementation of {@link IVoyageOptions}.
 * This is @link {Cloneable} for use with @link{VoyagePlanOptimiser} use.
 * @author Simon Goodall
 * 
 */
public final class VoyageOptions implements IVoyageOptions, Cloneable {

	private int availableTime;
	private int distance;
	private IVessel vessel;
	private IPortSlot fromPortSlot;
	private IPortSlot toPortSlot;
	private int nboSpeed;

	private boolean useNBOForIdle;
	private boolean useNBOForTravel;
	private boolean useFBOForSupplement;

	private String route;

	private VesselState vesselState;

	@Override
	public final int getAvailableTime() {
		return availableTime;
	}

	@Override
	public final int getDistance() {
		return distance;
	}

	@Override
	public final IPortSlot getFromPortSlot() {
		return fromPortSlot;
	}

	@Override
	public final int getNBOSpeed() {
		return nboSpeed;
	}

	@Override
	public final String getRoute() {
		return route;
	}

	@Override
	public final IPortSlot getToPortSlot() {
		return toPortSlot;
	}

	@Override
	public final IVessel getVessel() {
		return vessel;
	}

	@Override
	public final VesselState getVesselState() {
		return vesselState;
	}

	@Override
	public final boolean useFBOForSupplement() {
		return useFBOForSupplement;
	}

	@Override
	public final boolean useNBOForIdle() {
		return useNBOForIdle;
	}

	@Override
	public final boolean useNBOForTravel() {
		return useNBOForTravel;
	}

	public final void setNBOSpeed(final int nboSpeed) {
		this.nboSpeed = nboSpeed;
	}

	public final void setUseNBOForIdle(final boolean useNBOForIdle) {
		this.useNBOForIdle = useNBOForIdle;
	}

	public final void setUseNBOForTravel(final boolean useNBOForTravel) {
		this.useNBOForTravel = useNBOForTravel;
	}

	public final void setUseFBOForSupplement(final boolean useFBOForSupplement) {
		this.useFBOForSupplement = useFBOForSupplement;
	}

	public final void setAvailableTime(final int availableTime) {
		this.availableTime = availableTime;
	}

	public final void setDistance(final int distance) {
		this.distance = distance;
	}

	public final void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	public final void setFromPortSlot(final IPortSlot fromPortSlot) {
		this.fromPortSlot = fromPortSlot;
	}

	public final void setToPortSlot(final IPortSlot toPortSlot) {
		this.toPortSlot = toPortSlot;
	}

	public final void setRoute(final String route) {
		this.route = route;
	}

	public final void setVesselState(final VesselState vesselState) {
		this.vesselState = vesselState;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof VoyageOptions) {
			final VoyageOptions vo = (VoyageOptions) obj;

			if (useNBOForIdle != vo.useNBOForIdle)
				return false;
			if (useNBOForTravel != vo.useNBOForTravel)
				return false;
			if (useFBOForSupplement != vo.useFBOForSupplement) {
				return false;
			}

			if (availableTime != vo.availableTime) {
				return false;
			}
			if (distance != vo.distance) {
				return false;
			}
			if (nboSpeed != vo.nboSpeed) {
				return false;
			}

			if (!Equality.isEqual(vesselState, vo.vesselState)) {
				return false;
			}

			if (!Equality.isEqual(route, vo.route)) {
				return false;
			}
			if (!Equality.isEqual(vessel, vo.vessel)) {
				return false;
			}
			if (!Equality.isEqual(fromPortSlot, vo.fromPortSlot)) {
				return false;
			}
			if (!Equality.isEqual(toPortSlot, vo.toPortSlot)) {
				return false;
			}

			return true;
		}
		return false;
	}

	@Override
	public final VoyageOptions clone() throws CloneNotSupportedException {

		return (VoyageOptions) super.clone();
	}
}
