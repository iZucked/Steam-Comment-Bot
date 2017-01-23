/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class EndRequirementEndPortSlot extends PortSlot implements IEndPortSlot {

	private final IEndRequirement endRequirement;

	public EndRequirementEndPortSlot(final @NonNull String id, final IPort port, final @NonNull IEndRequirement endRequirement) {
		super(id, PortType.End, port, null);
		this.endRequirement = endRequirement;
	}

	@Override
	public void setTimeWindow(@NonNull ITimeWindow timeWindow) {
		throw new UnsupportedOperationException();
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return endRequirement.getTimeWindow();
	}

	public boolean isEndCold() {
		return endRequirement.isEndCold();
	}

	public long getTargetEndHeelInM3() {
		return endRequirement.getTargetHeelInM3();
	}

}
