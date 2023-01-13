/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class ConstraintInfo<P, C, T extends IPortSlot> {
	P contractProfile;
	C profileConstraint;
	Object months;
	Set<@NonNull T> slots;
	int bound;
	public enum ViolationType {
		Min, Max
	};
	ViolationType violationType;
	
	int violatedAmount = 0;
	
	public ConstraintInfo(final P contractProfile, final C profileConstraint, final Set<T> slots, int bound) {
		this.contractProfile = contractProfile;
		this.profileConstraint = profileConstraint;
		this.slots = slots;
		this.bound = bound;
	}
	
	public ConstraintInfo(final P contractProfile, final C profileConstraint, final Set<T> slots, int bound, final Object months) {
		this(contractProfile, profileConstraint, slots, bound);
		this.months = months;
	}
	
	public void setViolatedAmount(ViolationType vt, int violatedAmount) {
		this.violationType = vt;
		this.violatedAmount = violatedAmount;
	}
	
	public ViolationType getViolationType() {
		return violationType;
	}
	
	public int getViolatedAmount() {
		return violatedAmount;
	}
	
	public Set<@NonNull T> getSlots() {
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

	public Object getMonths() {
		return months;
	}
}

