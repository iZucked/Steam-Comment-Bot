package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public class ThreadLocalStartRequirement implements IStartRequirement {

	private final ThreadLocal<IStartRequirement> reference = new ThreadLocal<>();
	private IStartRequirement globalRef;

	public ThreadLocalStartRequirement(IStartRequirement startRequirement) {
		globalRef = startRequirement;
	}

	public void setStartRequirement(IStartRequirement startRequirement) {
		if (startRequirement == null) {
			reference.remove();
		} else {
			reference.set(startRequirement);
		}
	}

	@Override
	public IHeelOptionSupplier getHeelOptions() {
		if (reference.get() != null) {
			return reference.get().getHeelOptions();
		} else {
			return globalRef.getHeelOptions();
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
	public IPort getLocation() {
		if (reference.get() != null) {
			return reference.get().getLocation();
		} else {
			return globalRef.getLocation();
		}
	}

	@Override
	public ITimeWindow getTimeWindow() {
		if (reference.get() != null) {
			return reference.get().getTimeWindow();
		} else {
			return globalRef.getTimeWindow();
		}
	}

	@Override
	public Collection<@NonNull IPort> getLocations() {
		if (reference.get() != null) {
			return reference.get().getLocations();
		} else {
			return globalRef.getLocations();
		}
	}

}
