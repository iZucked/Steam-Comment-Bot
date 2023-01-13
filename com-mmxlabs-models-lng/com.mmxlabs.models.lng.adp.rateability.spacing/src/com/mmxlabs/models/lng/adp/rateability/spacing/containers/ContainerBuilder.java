/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.spacing.containers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.IntervalVar;

@NonNullByDefault
public class ContainerBuilder {
	@Nullable
	private IntVar loadStart = null;
	@Nullable
	private IntVar loadEnd = null;
	@Nullable
	private IntVar loadDuration = null;
	@Nullable
	private IntervalVar loadInterval = null;
	@Nullable
	private IntVar dischargeStart = null;
	@Nullable
	private IntVar dischargeEnd = null;
	@Nullable
	private IntVar dischargeDuration = null;
	@Nullable
	private IntervalVar dischargeInterval = null;
	@Nullable
	private IntVar ladenEnd = null;
	@Nullable
	private IntVar ladenDuration = null;
	@Nullable
	private IntervalVar ladenInterval = null;
	@Nullable
	private IntVar ballastEnd = null;
	@Nullable
	private IntVar ballastDuration = null;
	@Nullable
	private IntervalVar ballastInterval = null;
	@Nullable
	private IntervalVar cargoInterval = null;

	ContainerBuilder() {
	}

	public ContainerBuilder withLoadStart(final IntVar loadStart) {
		this.loadStart = loadStart;
		return this;
	}

	public ContainerBuilder withLoadEnd(final IntVar loadEnd) {
		this.loadEnd = loadEnd;
		return this;
	}

	public ContainerBuilder withLoadDuration(final IntVar loadDuration) {
		this.loadDuration = loadDuration;
		return this;
	}

	public ContainerBuilder withLoadInterval(final IntervalVar loadInterval) {
		this.loadInterval = loadInterval;
		return this;
	}

	public ContainerBuilder withDischargeStart(final IntVar dischargeStart) {
		this.dischargeStart = dischargeStart;
		return this;
	}

	public ContainerBuilder withDischargeEnd(final IntVar dischargeEnd) {
		this.dischargeEnd = dischargeEnd;
		return this;
	}

	public ContainerBuilder withDischargeDuration(final IntVar dischargeDuration) {
		this.dischargeDuration = dischargeDuration;
		return this;
	}

	public ContainerBuilder withDischargeInterval(final IntervalVar dischargeInterval) {
		this.dischargeInterval = dischargeInterval;
		return this;
	}

	public ContainerBuilder withLadenEnd(final IntVar ladenEnd) {
		this.ladenEnd = ladenEnd;
		return this;
	}

	public ContainerBuilder withLadenDuration(final IntVar ladenDuration) {
		this.ladenDuration = ladenDuration;
		return this;
	}

	public ContainerBuilder withLadenInterval(final IntervalVar ladenInterval) {
		this.ladenInterval = ladenInterval;
		return this;
	}

	public ContainerBuilder withBallastEnd(final IntVar ballastEnd) {
		this.ballastEnd = ballastEnd;
		return this;
	}

	public ContainerBuilder withBallastDuration(final IntVar ballastDuration) {
		this.ballastDuration = ballastDuration;
		return this;
	}

	public ContainerBuilder withBallastInterval(final IntervalVar ballastInterval) {
		this.ballastInterval = ballastInterval;
		return this;
	}
	
	public ContainerBuilder withCargoInterval(final IntervalVar cargoInterval) {
		this.cargoInterval = cargoInterval;
		return this;
	}

	public ShippedCargoModellingContainer build() {
		final InPortContainer loadPortVariables;
		final TravelContainer ladenTravelVariables;
		if (loadStart != null && loadEnd != null && loadDuration != null && loadInterval != null) {
			loadPortVariables = new InPortContainer(loadStart, loadEnd, loadDuration, loadInterval);
			if (ladenDuration != null && ladenEnd != null && ladenInterval != null) {
				ladenTravelVariables = new TravelContainer((@NonNull IntVar) loadEnd, ladenEnd, ladenDuration, ladenInterval);
			} else {
				throw new IllegalStateException("Laden solver variables should not be null");
			}
		} else {
			throw new IllegalStateException("Load solver variables should not be null");
		}
		final InPortContainer dischargePortVariables;
		final TravelContainer ballastTravelVariables;
		if (dischargeStart != null && dischargeEnd != null && dischargeDuration != null && dischargeInterval != null) {
			dischargePortVariables = new InPortContainer(dischargeStart, dischargeEnd, dischargeDuration, dischargeInterval);
			if (ballastDuration != null && ballastEnd != null && ballastInterval != null) {
				ballastTravelVariables = new TravelContainer((@NonNull IntVar) dischargeEnd, ballastEnd, ballastDuration, ballastInterval);
			} else {
				throw new IllegalStateException("Ballast solver variables should not be null");
			}
		} else {
			throw new IllegalStateException("Discharge solver variables should not be null");
		}
		if (cargoInterval != null) {
			return new ShippedCargoModellingContainer(loadPortVariables, ladenTravelVariables, dischargePortVariables, ballastTravelVariables, cargoInterval);
		} else {
			throw new IllegalStateException("Cargo solver variables should not be null");
		}
	}
}
