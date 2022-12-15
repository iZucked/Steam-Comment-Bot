package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.Nullable;

public record VesselStartState(int startTime, @Nullable IPort startPort) {

}
