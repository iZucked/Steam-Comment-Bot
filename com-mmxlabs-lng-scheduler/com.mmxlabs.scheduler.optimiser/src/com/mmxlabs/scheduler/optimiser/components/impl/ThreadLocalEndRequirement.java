package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class ThreadLocalEndRequirement implements IEndRequirement {
	private final ThreadLocal<IEndRequirement> reference = new ThreadLocal<>();
	private final IEndRequirement globalRef;

	public ThreadLocalEndRequirement(IEndRequirement endRequirement) {
		globalRef = endRequirement;
	}

	public void setEndRequirment(IEndRequirement endRequirement) {
		if (endRequirement == null) {
			reference.remove();
		} else {
			reference.set(endRequirement);
		}
	}

	@Override
	public boolean hasPortRequirement() {
		return getUnderlyingEndRequirement().hasPortRequirement();
	}

	@Override
	public boolean hasTimeRequirement() {
		return getUnderlyingEndRequirement().hasTimeRequirement();
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return getUnderlyingEndRequirement().getTimeWindow();
	}

	@Override
	public @Nullable IPort getLocation() {
		return getUnderlyingEndRequirement().getLocation();
	}

	@Override
	public @NonNull Collection<@NonNull IPort> getLocations() {
		return getUnderlyingEndRequirement().getLocations();
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptions() {
		return getUnderlyingEndRequirement().getHeelOptions();
	}

	@Override
	public int getMinDurationInHours() {
		return getUnderlyingEndRequirement().getMinDurationInHours();
	}

	@Override
	public int getMaxDurationInHours() {
		return getUnderlyingEndRequirement().getMaxDurationInHours();
	}

	@Override
	public void setMinDurationInHours(int hours) {
		getUnderlyingEndRequirement().setMinDurationInHours(hours);
	}

	@Override
	public void setMaxDurationInHours(int hours) {
		getUnderlyingEndRequirement().setMaxDurationInHours(hours);
	}

	@Override
	public boolean isMinDurationSet() {
		return getUnderlyingEndRequirement().isMinDurationSet();
	}

	@Override
	public boolean isMaxDurationSet() {
		return getUnderlyingEndRequirement().isMaxDurationSet();
	}
	
	private IEndRequirement getUnderlyingEndRequirement() {
		if (reference.get() != null) {
			return reference.get();
		} else {
			return globalRef;
		}
	}

}
