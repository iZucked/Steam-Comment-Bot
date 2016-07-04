/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

/**
 * Default implementation of {@link VoyageOptions}. This is @link {Cloneable} for use with @link{VoyagePlanOptimiser} use.
 * 
 * @author Simon Goodall
 * 
 */
public final class VoyageOptions implements Cloneable, IOptionsSequenceElement {
	private int availableTime;
	private int distance;
	private long routeCost;
	private IVessel vessel;
	private @NonNull IPortSlot fromPortSlot;
	private @NonNull IPortSlot toPortSlot;
	private int nboSpeed;

	private boolean useNBOForIdle;
	private boolean useNBOForTravel;

	private boolean useFBOForSupplement;

	private boolean charterOutIdleTime;

	private int cargoCV;

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

	private ERouteOption route;

	private VesselState vesselState;

	private int charterOutDailyRate;

	public VoyageOptions(@NonNull final IPortSlot from, @NonNull final IPortSlot to) {
		this.fromPortSlot = from;
		this.toPortSlot = to;
	}

	public VoyageOptions(final VoyageOptions options) {
		this.fromPortSlot = options.getFromPortSlot();
		this.toPortSlot = options.getToPortSlot();
		setAvailableTime(options.getAvailableTime());
		setVessel(options.getVessel());
		setNBOSpeed(options.getNBOSpeed());
		setUseNBOForTravel(options.useNBOForTravel());
		setUseFBOForSupplement(options.useFBOForSupplement());
		setUseNBOForIdle(options.useNBOForIdle());
		setRoute(options.getRoute(), options.getDistance(), options.getRouteCost());
		setVesselState(options.getVesselState());
		setAllowCooldown(options.getAllowCooldown());
		setShouldBeCold(options.shouldBeCold());
		setCharterOutDailyRate(options.getCharterOutDailyRate());
		setCharterOutIdleTime(options.isCharterOutIdleTime());
		setCargoCVValue(options.getCargoCVValue());
	}

	public final int getAvailableTime() {
		return availableTime;
	}

	public final int getDistance() {
		return distance;
	}

	public long getRouteCost() {
		return routeCost;
	}

	public final @NonNull IPortSlot getFromPortSlot() {
		return fromPortSlot;
	}

	public final int getNBOSpeed() {
		return nboSpeed;
	}

	public final ERouteOption getRoute() {
		return route;
	}

	public final @NonNull IPortSlot getToPortSlot() {
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

	public final void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	@Deprecated // Only used for Client E
	public final void setFromPortSlot(final @NonNull IPortSlot fromPortSlot) {
		this.fromPortSlot = fromPortSlot;
	}

	@Deprecated // Only used for Client E
	public final void setToPortSlot(final @NonNull IPortSlot toPortSlot) {
		this.toPortSlot = toPortSlot;
	}

	public final void setRoute(@NonNull final ERouteOption route, final int distance, final long routeCost) {
		this.route = route;
		this.distance = distance;
		this.routeCost = routeCost;
	}

	public final void setVesselState(final VesselState vesselState) {
		this.vesselState = vesselState;
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
				&& Objects.equal(routeCost, vo.routeCost)
				&& Objects.equal(nboSpeed, vo.nboSpeed)
				&& Objects.equal(vesselState, vo.vesselState)
				&& Objects.equal(route, vo.route)
				&& Objects.equal(vessel, vo.vessel)
				&& Objects.equal(fromPortSlot, vo.fromPortSlot)
				&& Objects.equal(toPortSlot, vo.toPortSlot)
				&& Objects.equal(cargoCV, vo.cargoCV)
				&& Objects.equal(cooldown, vo.cooldown)
				&& Objects.equal(shouldBeCold, vo.shouldBeCold)
				&& Objects.equal(startWarm, vo.startWarm)
				;
			// @formatter:on
		}
		return false;
	}

	@Override
	public final @NonNull VoyageOptions clone() {
		return new VoyageOptions(this);
	}

	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(VoyageOptions.class)
				.add("availableTime", availableTime)
				.add("charterOutIdleTime", charterOutIdleTime)
				.add("charterOutDailyRate", charterOutDailyRate)
				.add("distance", distance)
				.add("vessel", vessel)
				.add("fromPortSlot", fromPortSlot)
				.add("toPortSlot", toPortSlot)
				.add("nboSpeed", nboSpeed)
				.add("useNBOForIdle", useNBOForIdle)
				.add("useNBOForTravel", useNBOForTravel)
				.add("useFBOForSupplement", useFBOForSupplement)
				.add("cargoCV", cargoCV)
				.add("cooldown", cooldown)
				.add("shouldBeCold", shouldBeCold)
				.add("startWarm", startWarm)
				.add("route", route)
				.add("routeCost", routeCost)
				.add("vesselState", vesselState)
				.toString();
		// @formatter:on
	}

	/**
	 * @return
	 */
	public boolean isCharterOutIdleTime() {
		return charterOutIdleTime;
	}

	/**
	 * @param charterOutIdleTime
	 */
	public void setCharterOutIdleTime(final boolean charterOutIdleTime) {
		this.charterOutIdleTime = charterOutIdleTime;
	}

	public int getCharterOutDailyRate() {
		return charterOutDailyRate;
	}

	public void setCharterOutDailyRate(final int charterOutDailyRate) {
		this.charterOutDailyRate = charterOutDailyRate;
	}

	public int getCargoCVValue() {
		return cargoCV;
	}

	public void setCargoCVValue(final int cargoCV) {
		this.cargoCV = cargoCV;
	}
}
