package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class ConstraintInfo<P, C, T extends IPortSlot> {
	P contractProfile;
	C profileConstraint;
	Set<T> slots;
	int bound;
	
	public ConstraintInfo(final P contractProfile, final C profileConstraint, final Set<T> slots, int bound) {
		this.contractProfile = contractProfile;
		this.profileConstraint = profileConstraint;
		this.slots = slots;
		this.bound = bound;
	}
	
	public Set<T> getSlots() {
		return slots;
	}
	
	public int getBound() {
		return bound;
	}
	
	public P getContractProfile() {
		return contractProfile;
	}
	
	public C getProfileConstraint() {
		return profileConstraint;
	}
}

