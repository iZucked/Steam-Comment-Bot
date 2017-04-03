/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public class GeneratedCharterOutVesselEvent implements IGeneratedCharterOutVesselEvent {
	private @Nullable ITimeWindow timeWindow;
	private IPort startPort, endPort;
	private IHeelOptionConsumer heelConsumer;
	private IHeelOptionSupplier heelSupplier;
	private int durationHours;
	private long repositioning;
	private long ballastBonus;
	private long hireCost;

	public GeneratedCharterOutVesselEvent(@Nullable ITimeWindow timeWindow, IPort startPort, IPort endPort, final IHeelOptionConsumer heelConsumer, IHeelOptionSupplier heelSupplier) {
		this.timeWindow = timeWindow;
		this.startPort = startPort;
		this.endPort = endPort;
		this.heelConsumer = heelConsumer;
		this.heelSupplier = heelSupplier;
	}

	@Override
	public void setTimeWindow(final ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	@Override
	public void setDurationHours(final int durationHours) {
		this.durationHours = durationHours;
	}

	@Override
	public void setStartPort(final IPort startPort) {
		this.startPort = startPort;
	}

	@Override
	public void setEndPort(final IPort endPort) {
		this.endPort = endPort;
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public int getDurationHours() {
		return durationHours;
	}

	@Override
	public IPort getStartPort() {
		return startPort;
	}

	@Override
	public IPort getEndPort() {
		return endPort;
	}

	@Override
	public long getHireOutRevenue() {
		return hireCost;
	}

	@Override
	public long getRepositioning() {
		return repositioning;
	}

	@Override
	public void setHireOutRevenue(final long hireCost) {
		this.hireCost = hireCost;
	}

	@Override
	public void setRepositioning(final long repositioning) {
		this.repositioning = repositioning;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.durationHours, this.heelConsumer, this.heelSupplier, this.hireCost, this.repositioning,
				// this.startPort,
				// this.endPort,
				this.timeWindow);
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		// DO NOT DO IDENTITY CHECK AS CONTENTS ARE MUTABLE WITH REGARDS TO HASHMAP

		if (obj instanceof GeneratedCharterOutVesselEvent) {
			final GeneratedCharterOutVesselEvent other = (GeneratedCharterOutVesselEvent) obj;
			return this.durationHours == other.durationHours //
					&& this.repositioning == other.repositioning //
					&& this.ballastBonus == other.ballastBonus //
					// && Objects.equals(this.startPort, other.startPort) //
					// && Objects.equals(this.endPort, other.endPort) //
					&& Objects.equals(this.heelConsumer, other.heelConsumer) //
					&& Objects.equals(this.heelSupplier, other.heelSupplier) //
					&& Objects.equals(this.timeWindow, other.timeWindow); //

		}

		return false;
	}

	@Override
	public long getBallastBonus() {
		return ballastBonus;
	}

	@Override
	public void setBallastBonus(final long ballastBonus) {
		this.ballastBonus = ballastBonus;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptionsConsumer() {
		return heelConsumer;
	}

	@Override
	public @NonNull IHeelOptionSupplier getHeelOptionsSupplier() {
		return heelSupplier;
	}

	@Override
	public void setHeelConsumer(@NonNull IHeelOptionConsumer heelConsumer) {
		this.heelConsumer = heelConsumer;
	}

	@Override
	public void setHeelSupplier(@NonNull IHeelOptionSupplier heelSupplier) {
		this.heelSupplier = heelSupplier;
	}
}
