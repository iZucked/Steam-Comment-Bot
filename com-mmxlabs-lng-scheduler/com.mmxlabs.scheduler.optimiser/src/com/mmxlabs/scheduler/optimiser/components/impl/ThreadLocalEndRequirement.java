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
		if (reference.get() != null) {
			return reference.get().hasPortRequirement();
		} else {
			return globalRef.hasPortRequirement();
		}
	}

	@Override
	public boolean hasTimeRequirement() {
		if (reference.get() != null) {
			return reference.get().hasTimeRequirement();
		} else {
			return globalRef.hasTimeRequirement();
		}
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		if (reference.get() != null) {
			return reference.get().getTimeWindow();
		} else {
			return globalRef.getTimeWindow();
		}
	}

	@Override
	public @Nullable IPort getLocation() {
		if (reference.get() != null) {
			return reference.get().getLocation();
		} else {
			return globalRef.getLocation();
		}
	}

	@Override
	public @NonNull Collection<@NonNull IPort> getLocations() {
		if (reference.get() != null) {
			return reference.get().getLocations();
		} else {
			return globalRef.getLocations();
		}
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptions() {
		if (reference.get() != null) {
			return reference.get().getHeelOptions();
		} else {
			return globalRef.getHeelOptions();
		}
	}

	@Override
	public int getMinDurationInHours() {
		if (reference.get() != null) {
			return reference.get().getMinDurationInHours();
		} else {
			return globalRef.getMinDurationInHours();
		}
	}

	@Override
	public int getMaxDurationInHours() {
		if (reference.get() != null) {
			return reference.get().getMaxDurationInHours();
		} else {
			return globalRef.getMaxDurationInHours();
		}
	}

	@Override
	public void setMinDurationInHours(int hours) {
		if (reference.get() != null) {
			reference.get().setMinDurationInHours(hours);
		} else {
			globalRef.setMinDurationInHours(hours);
		}
	}

	@Override
	public void setMaxDurationInHours(int hours) {
		if (reference.get() != null) {
			reference.get().setMaxDurationInHours(hours);
		} else {
			globalRef.setMaxDurationInHours(hours);
		}
	}

	@Override
	public boolean isMinDurationSet() {
		if (reference.get() != null) {
			return reference.get().isMinDurationSet();
		} else {
			return globalRef.isMinDurationSet();
		}
	}

	@Override
	public boolean isMaxDurationSet() {
		if (reference.get() != null) {
			return reference.get().isMaxDurationSet();
		} else {
			return globalRef.isMaxDurationSet();
		}
	}

}
