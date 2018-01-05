/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionplan.independence;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.actionplan.Change;

/**
 * Insert cargo change
 * @author achurchill
 *
 */
public class InsertUnusedCargoChange extends Change implements Serializable {
	private transient ISequenceElement loadA;
	private transient ISequenceElement dischargeA;
	private transient ISequenceElement unusedLoad;
	private transient ISequenceElement unusedDischarge;
	private transient IResource resourceA;
	
	public InsertUnusedCargoChange(@NonNull String description, @NonNull ISequenceElement loadA, @NonNull ISequenceElement dischargeA, @NonNull ISequenceElement unusedLoad, @NonNull ISequenceElement unusedDischarge, @NonNull IResource resourceA) {
		super(description);
		
		this.setLoadA(loadA);
		this.setDischargeA(dischargeA);
		this.setUnusedLoad(unusedLoad);
		this.setUnusedDischarge(unusedDischarge);
		this.setResourceA(resourceA);
	}

	@NonNull
	public ISequenceElement getLoadA() {
		return loadA;
	}

	
	public void setLoadA(ISequenceElement loadA) {
		this.loadA = loadA;
	}

	@NonNull
	public ISequenceElement getDischargeA() {
		return dischargeA;
	}

	public void setDischargeA(ISequenceElement dischargeA) {
		this.dischargeA = dischargeA;
	}

	@NonNull
	public ISequenceElement getUnusedLoad() {
		return unusedLoad;
	}

	public void setUnusedLoad(ISequenceElement unusedLoad) {
		this.unusedLoad = unusedLoad;
	}

	@NonNull
	public ISequenceElement getUnusedDischarge() {
		return unusedDischarge;
	}

	public void setUnusedDischarge(ISequenceElement unusedDischarge) {
		this.unusedDischarge = unusedDischarge;
	}

	@NonNull
	public IResource getResourceA() {
		return resourceA;
	}

	public void setResourceA(IResource resourceA) {
		this.resourceA = resourceA;
	}
	
	
}
