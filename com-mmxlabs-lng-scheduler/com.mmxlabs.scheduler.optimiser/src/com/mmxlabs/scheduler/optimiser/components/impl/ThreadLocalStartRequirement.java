package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public class ThreadLocalStartRequirement implements IStartRequirement{
	
	private final ThreadLocal<IStartRequirement> reference = new ThreadLocal<>();
	private IStartRequirement globalRef;
	public ThreadLocalStartRequirement(IStartRequirement startRequirement) {
		globalRef = startRequirement;
	}
	public void setStartRequirement(IStartRequirement startRequirement) {
		if(startRequirement == null) {
			reference.remove();
		}
		else {
			reference.set(startRequirement);
		}
	}
	
	@Override
	public IHeelOptionSupplier getHeelOptions() {
		if(reference.get() != null){
			return reference.get().getHeelOptions();
		}
		else if(globalRef != null){
			return globalRef.getHeelOptions();
		}
		//FIXME: CHANGE TO EXCEPTION
		return null;
		
	}
	@Override
	public boolean hasPortRequirement() {
		if(reference.get() != null){
			return reference.get().hasPortRequirement();
		}
		else if(globalRef != null){
			return globalRef.hasPortRequirement();
		}
		//FIXME: CHANGE TO EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public boolean hasTimeRequirement() {
		if(reference.get() != null){
			return reference.get().hasTimeRequirement();
		}
		else if(globalRef != null){
			return globalRef.hasTimeRequirement();
		}
		//FIXME: CHANGE TO EXCEPTION
		throw new RuntimeException();
	}

	@Override
	public IPort getLocation() {
		if(reference.get() != null){
			return reference.get().getLocation();
		}
		else if(globalRef != null){
			return globalRef.getLocation();
		}
		//FIXME: CHANGE TO EXCEPTION
		return null;
	}

	@Override
	public ITimeWindow getTimeWindow() {
		if(reference.get() != null){
			return reference.get().getTimeWindow();
		}
		else if(globalRef != null){
			return globalRef.getTimeWindow();
		}
		//FIXME: CHANGE TO EXCEPTION
		return null;
	}

	@Override
	public Collection<@NonNull IPort> getLocations() {
		if(reference.get() != null){
			return reference.get().getLocations();
		}
		else if(globalRef != null){
			return globalRef.getLocations();
		}
		//FIXME: CHANGE TO EXCEPTION
		throw new RuntimeException();
	}
	
	
	
	
	

}
