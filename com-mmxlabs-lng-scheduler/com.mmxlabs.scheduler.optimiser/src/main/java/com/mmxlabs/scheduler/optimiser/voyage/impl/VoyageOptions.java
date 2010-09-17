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
public class VoyageOptions implements IVoyageOptions, Cloneable {

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
	public int getAvailableTime() {
		return availableTime;
	}

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public IPortSlot getFromPortSlot() {
		return fromPortSlot;
	}

	@Override
	public int getNBOSpeed() {
		return nboSpeed;
	}

	@Override
	public String getRoute() {
		return route;
	}

	@Override
	public IPortSlot getToPortSlot() {
		return toPortSlot;
	}

	@Override
	public IVessel getVessel() {
		return vessel;
	}

	@Override
	public VesselState getVesselState() {
		return vesselState;
	}

	@Override
	public boolean useFBOForSupplement() {
		return useFBOForSupplement;
	}

	@Override
	public boolean useNBOForIdle() {
		return useNBOForIdle;
	}

	@Override
	public boolean useNBOForTravel() {
		return useNBOForTravel;
	}

	public void setNBOSpeed(final int nboSpeed) {
		this.nboSpeed = nboSpeed;
	}

	public void setUseNBOForIdle(final boolean useNBOForIdle) {
		this.useNBOForIdle = useNBOForIdle;
	}

	public void setUseNBOForTravel(final boolean useNBOForTravel) {
		this.useNBOForTravel = useNBOForTravel;
	}

	public void setUseFBOForSupplement(final boolean useFBOForSupplement) {
		this.useFBOForSupplement = useFBOForSupplement;
	}

	public void setAvailableTime(final int availableTime) {
		this.availableTime = availableTime;
	}

	public void setDistance(final int distance) {
		this.distance = distance;
	}

	public void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	public void setFromPortSlot(final IPortSlot fromPortSlot) {
		this.fromPortSlot = fromPortSlot;
	}

	public void setToPortSlot(final IPortSlot toPortSlot) {
		this.toPortSlot = toPortSlot;
	}

	public void setRoute(final String route) {
		this.route = route;
	}

	public void setVesselState(final VesselState vesselState) {
		this.vesselState = vesselState;
	}

	@Override
	public boolean equals(final Object obj) {

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
