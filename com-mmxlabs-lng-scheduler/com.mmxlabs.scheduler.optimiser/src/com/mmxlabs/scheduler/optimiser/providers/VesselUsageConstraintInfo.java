/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public class VesselUsageConstraintInfo<P, C, T extends IPortSlot> {
	P contractProfile;
	C profileConstraint;
	Set<@NonNull T> slots;
	Set<@NonNull ? extends IVessel> vessels;
	int bound;
	public enum ViolationType {
		Min, Max
	}
	ViolationType violationType;
	
	int violatedAmount = 0;
	
	public VesselUsageConstraintInfo(final P contractProfile, final C profileConstraint, final Set<T> slots, final Set<? extends IVessel> vessels, int bound) {
		this.contractProfile = contractProfile;
		this.profileConstraint = profileConstraint;
		this.slots = slots;
		this.vessels = vessels;
		this.bound = bound;
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
	
	public Set<IVessel> getVessels() {
		return (Set<IVessel>) vessels;
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

