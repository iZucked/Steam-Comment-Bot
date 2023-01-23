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
		if(endRequirement == null) {
			reference.remove();
		}
		else {
			reference.set(endRequirement);
		}
	}
	
	@Override
	public boolean hasPortRequirement() {
		if(reference.get() != null) {
			return reference.get().hasPortRequirement();
		} else if(globalRef != null) {
			return globalRef.hasPortRequirement();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public boolean hasTimeRequirement() {
		if(reference.get() != null) {
			return reference.get().hasTimeRequirement();
		} else if(globalRef != null) {
			return globalRef.hasTimeRequirement();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		if(reference.get() != null) {
			return reference.get().getTimeWindow();
		} else if(globalRef != null) {
			return globalRef.getTimeWindow();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public @Nullable IPort getLocation() {
		if(reference.get() != null) {
			return reference.get().getLocation();
		} else if(globalRef != null) {
			return globalRef.getLocation();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public @NonNull Collection<@NonNull IPort> getLocations() {
		if(reference.get() != null) {
			return reference.get().getLocations();
		} else if(globalRef != null) {
			return globalRef.getLocations();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptions() {
		if(reference.get() != null) {
			return reference.get().getHeelOptions();
		} else if(globalRef != null) {
			return globalRef.getHeelOptions();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public int getMinDurationInHours() {
		if(reference.get() != null) {
			return reference.get().getMinDurationInHours();
		} else if(globalRef != null) {
			return globalRef.getMinDurationInHours();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public int getMaxDurationInHours() {
		if(reference.get() != null) {
			return reference.get().getMaxDurationInHours();
		}else if(globalRef != null) {
			return globalRef.getMaxDurationInHours();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public void setMinDurationInHours(int hours) {
		if(reference.get() != null) {
			reference.get().setMinDurationInHours(hours);
		}else if(globalRef != null) {
			globalRef.setMinDurationInHours(hours);
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public void setMaxDurationInHours(int hours) {
		if(reference.get() != null) {
			reference.get().setMaxDurationInHours(hours);
		}else if(globalRef != null) {
			globalRef.setMaxDurationInHours(hours);
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public boolean isMinDurationSet() {
		if(reference.get() != null) {
			return reference.get().isMinDurationSet();
		}else if(globalRef != null) {
			return globalRef.isMinDurationSet();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public boolean isMaxDurationSet() {
		if(reference.get() != null) {
			return reference.get().isMaxDurationSet();
		}else if(globalRef != null) {
			return globalRef.isMaxDurationSet();
		}
		//FIXME: ADD EXCEPTION
		throw new RuntimeException();
	}

}
