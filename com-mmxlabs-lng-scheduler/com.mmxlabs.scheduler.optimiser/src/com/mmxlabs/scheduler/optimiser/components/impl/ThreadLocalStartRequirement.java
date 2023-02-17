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
		return getUnderlyingStartRequirement().getHeelOptions();
	}

	@Override
	public boolean hasPortRequirement() {
		return getUnderlyingStartRequirement().hasPortRequirement();
	}

	@Override
	public boolean hasTimeRequirement() {
		return getUnderlyingStartRequirement().hasTimeRequirement();
	}

	@Override
	public IPort getLocation() {
		return getUnderlyingStartRequirement().getLocation();
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return getUnderlyingStartRequirement().getTimeWindow();
	}

	@Override
	public Collection<@NonNull IPort> getLocations() {
		return getUnderlyingStartRequirement().getLocations();
	}
	
	private IStartRequirement getUnderlyingStartRequirement() {
		if (reference.get() != null) {
			return reference.get();
		} else {
			return globalRef;
		}
	}

}
