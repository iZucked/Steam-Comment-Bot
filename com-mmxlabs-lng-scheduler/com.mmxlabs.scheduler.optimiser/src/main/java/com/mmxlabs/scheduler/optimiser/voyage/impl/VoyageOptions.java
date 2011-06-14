/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Default implementation of {@link VoyageOptions}.
 * This is @link {Cloneable} for use with @link{VoyagePlanOptimiser} use.
 * @author Simon Goodall
 * 
 */
public final class VoyageOptions implements Cloneable {
	private int availableTime;
	private int distance;
	private IVessel vessel;
	private IPortSlot fromPortSlot;
	private IPortSlot toPortSlot;
	private int nboSpeed;

	private boolean useNBOForIdle;
	private boolean useNBOForTravel;
	private boolean useFBOForSupplement;
	
	private long availableLNG;

	private String route;

	private VesselState vesselState;

	public VoyageOptions() {
		
	}
	
	public VoyageOptions(VoyageOptions options) {
		setAvailableTime(options.getAvailableTime());
		setDistance(options.getDistance());
		setVessel(options.getVessel());
		setFromPortSlot(options.getFromPortSlot());
		setToPortSlot(options.getToPortSlot());
		setNBOSpeed(options.getNBOSpeed());
		setUseNBOForTravel(options.useNBOForTravel());
		setUseFBOForSupplement(options.useFBOForSupplement());
		setUseNBOForIdle(options.useNBOForIdle());
		setRoute(options.getRoute());
		setVesselState(options.getVesselState());
		setAvailableLNG(options.getAvailableLNG());
	}

	public final int getAvailableTime() {
		return availableTime;
	}

	public final int getDistance() {
		return distance;
	}

	public final IPortSlot getFromPortSlot() {
		return fromPortSlot;
	}

	public final int getNBOSpeed() {
		return nboSpeed;
	}

	public final String getRoute() {
		return route;
	}

	public final IPortSlot getToPortSlot() {
		return toPortSlot;
	}

	public final IVessel getVessel() {
		return vessel;
	}

	public final VesselState getVesselState() {
		return vesselState;
	}

	public final boolean useFBOForSupplement() {
		return useFBOForSupplement;
	}

	public final boolean useNBOForIdle() {
		return useNBOForIdle;
	}

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

	public long getAvailableLNG() {
		return availableLNG;
	}

	public void setAvailableLNG(long availableLNG) {
		this.availableLNG = availableLNG;
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

	@Override
	public String toString() {
		return "VoyageOptions [availableTime=" + availableTime + ", distance="
				+ distance + ", vessel=" + vessel + ", fromPortSlot="
				+ fromPortSlot + ", toPortSlot=" + toPortSlot + ", nboSpeed="
				+ nboSpeed + ", useNBOForIdle=" + useNBOForIdle
				+ ", useNBOForTravel=" + useNBOForTravel
				+ ", useFBOForSupplement=" + useFBOForSupplement + ", route="
				+ route + ", vesselState=" + vesselState + "]";
	}
	
	
}
