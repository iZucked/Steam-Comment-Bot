/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Default implementation of {@link VoyageOptions}. This is @link {Cloneable} for use with @link{VoyagePlanOptimiser} use.
 * 
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

	private boolean charterOutIdleTime;

	private long availableLNG;

	/**
	 * If shouldBeCold is true: If true, a cooldown will be considered If false, enough heel will be retained to avoid warming up If shouldBeCold is false, this should be false and will be ignored
	 * (vessel will be allowed to warm up).
	 */
	private boolean cooldown;
	/**
	 * If true, the vessel should be cold at the end of the voyage + idle. If false, it doesn't matter.
	 */
	private boolean shouldBeCold;

	/**
	 * If true, the vessel is warm at the start of this voyage, because it is coming out of a drydock or something similar.
	 */
	private boolean startWarm;

	private String route;

	private VesselState vesselState;
	
	private int charterOutHourlyRate;

	public VoyageOptions() {

	}

	public VoyageOptions(final VoyageOptions options) {
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
		setAllowCooldown(options.getAllowCooldown());
		setShouldBeCold(options.shouldBeCold());
		setCharterOutHourlyRate(options.getCharterOutHourlyRate());
		setCharterOutIdleTime(options.isCharterOutIdleTime());
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

	public void setAvailableLNG(final long availableLNG) {
		assert availableLNG >= 0 : "available LNG should be non-negative for every voyage";
		this.availableLNG = availableLNG;
	}

	public final boolean getAllowCooldown() {
		return cooldown;
	}

	public final void setAllowCooldown(final boolean cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * If true, the vessel should be cold at the end of this voyage, possibly by triggering a cooldown.
	 * 
	 * @return
	 */
	public final boolean shouldBeCold() {
		return shouldBeCold;
	}

	public final void setShouldBeCold(final boolean shouldBeCold) {
		this.shouldBeCold = shouldBeCold;
	}

	public final boolean isWarm() {
		return startWarm;
	}

	public final void setWarm(final boolean startWarm) {
		this.startWarm = startWarm;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof VoyageOptions) {
			final VoyageOptions vo = (VoyageOptions) obj;

			// @formatter:off
			return Objects.equal(charterOutIdleTime, vo.charterOutIdleTime)
				&& Objects.equal(useNBOForIdle, vo.useNBOForIdle)
				&& Objects.equal(useNBOForTravel, vo.useNBOForTravel)
				&& Objects.equal(useFBOForSupplement, vo.useFBOForSupplement)
				&& Objects.equal(availableTime, vo.availableTime)
				&& Objects.equal(distance, vo.distance)
				&& Objects.equal(nboSpeed, vo.nboSpeed)
				&& Objects.equal(vesselState, vo.vesselState)
				&& Objects.equal(route, vo.route)
				&& Objects.equal(vessel, vo.vessel)
				&& Objects.equal(fromPortSlot, vo.fromPortSlot)
				&& Objects.equal(toPortSlot, vo.toPortSlot)
				&& Objects.equal(availableLNG, vo.availableLNG)
				&& Objects.equal(cooldown, vo.cooldown)
				&& Objects.equal(shouldBeCold, vo.shouldBeCold)
				&& Objects.equal(startWarm, vo.startWarm)
				;
			// @formatter:on
		}
		return false;
	}

	@Override
	public final VoyageOptions clone() throws CloneNotSupportedException {

		return (VoyageOptions) super.clone();
	}

	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(VoyageOptions.class)
				.add("charterOutIdleTime", charterOutIdleTime)
				.add("charterOutHourlyRate", charterOutHourlyRate)
				.add("distance", distance)
				.add("vessel", vessel)
				.add("fromPortSlot", fromPortSlot)
				.add("toPortSlot", toPortSlot)
				.add("nboSpeed", nboSpeed)
				.add("useNBOForIdle", useNBOForIdle)
				.add("useNBOForTravel", useNBOForTravel)
				.add("useFBOForSupplement", useFBOForSupplement)
				.add("availableLNG", availableLNG)
				.add("cooldown", cooldown)
				.add("shouldBeCold", shouldBeCold)
				.add("startWarm", startWarm)
				.add("route", route)
				.add("vesselState", vesselState)
				.toString();
		// @formatter:on
	}

	/**
	 * @since 2.0
	 * @return
	 */
	public boolean isCharterOutIdleTime() {
		return charterOutIdleTime;
	}

	/**
	 * @since 2.0
	 * @param charterOutIdleTime
	 */
	public void setCharterOutIdleTime(final boolean charterOutIdleTime) {
		this.charterOutIdleTime = charterOutIdleTime;
	}

	/**
	 * @since 2.0
	 */
	public int getCharterOutHourlyRate() {
		return charterOutHourlyRate;
	}

	/**
	 * @since 2.0
	 */
	public void setCharterOutHourlyRate(final int charterOutHourlyRate) {
		this.charterOutHourlyRate = charterOutHourlyRate;
	}

}
