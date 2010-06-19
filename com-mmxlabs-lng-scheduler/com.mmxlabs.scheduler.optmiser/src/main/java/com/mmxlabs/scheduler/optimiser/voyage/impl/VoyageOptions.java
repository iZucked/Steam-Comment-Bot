package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;

/**
 * Default implementation of {@link IVoyageOptions}.
 * 
 * @author Simon Goodall
 * 
 */
public class VoyageOptions implements IVoyageOptions {

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
}
