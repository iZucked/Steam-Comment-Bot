/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public class GeneratedCharterLengthEvent implements IGeneratedCharterLengthEvent {
	private @Nullable ITimeWindow timeWindow;
	private IPort port;
	private IHeelOptionConsumer heelConsumer;
	private IHeelOptionSupplier heelSupplier;

	public GeneratedCharterLengthEvent(@Nullable ITimeWindow timeWindow, IPort port, final IHeelOptionConsumer heelConsumer, IHeelOptionSupplier heelSupplier) {
		this.timeWindow = timeWindow;
		this.port = port;
		this.heelConsumer = heelConsumer;
		this.heelSupplier = heelSupplier;
	}

	@Override
	public void setTimeWindow(final ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	@Override
	public void setPort(final IPort port) {
		this.port = port;
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	@Override
	public IPort getStartPort() {
		return port;
	}

	@Override
	public IPort getEndPort() {
		return port;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.heelConsumer, this.heelSupplier,
				// this.startPort,
				// this.endPort,
				this.timeWindow);
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		// DO NOT DO IDENTITY CHECK AS CONTENTS ARE MUTABLE WITH REGARDS TO HASHMAP

		if (obj instanceof GeneratedCharterLengthEvent) {
			final GeneratedCharterLengthEvent other = (GeneratedCharterLengthEvent) obj;
			return  true //
					// && Objects.equals(this.startPort, other.startPort) //
					// && Objects.equals(this.endPort, other.endPort) //
					&& Objects.equals(this.heelConsumer, other.heelConsumer) //
					&& Objects.equals(this.heelSupplier, other.heelSupplier) //
					&& Objects.equals(this.timeWindow, other.timeWindow); //

		}

		return false;
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

	@Override
	public int getDurationHours() {
		return 0;
	}

}
