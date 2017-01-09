/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown.independence;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Rewire change
 * @author achurchill
 *
 */
public class UnusedToUsedDischargeChange extends Change implements Serializable {
	private transient ISequenceElement loadA;
	private transient ISequenceElement dischargeA;
	private transient ISequenceElement unusedDischarge;
	private transient IResource resourceA;
	
	public UnusedToUsedDischargeChange(String description, ISequenceElement loadA, ISequenceElement dischargeA, ISequenceElement unusedDischarge, IResource resourceA) {
		super(description);
		
		this.setLoadA(loadA);
		this.setDischargeA(dischargeA);
		this.setUnusedDischarge(unusedDischarge);
		
		this.setResourceA(resourceA);
	}

	@NonNull
	public IResource getResourceA() {
		return resourceA;
	}

	public void setResourceA(IResource resourceA) {
		this.resourceA = resourceA;
	}

	@NonNull
	public ISequenceElement getDischargeA() {
		return dischargeA;
	}

	public void setDischargeA(ISequenceElement dischargeA) {
		this.dischargeA = dischargeA;
	}

	@NonNull
	public ISequenceElement getUnusedDischarge() {
		return unusedDischarge;
	}

	public void setUnusedDischarge(ISequenceElement unusedDischarge) {
		this.unusedDischarge = unusedDischarge;
	}

	@NonNull
	public ISequenceElement getLoadA() {
		return loadA;
	}

	public void setLoadA(ISequenceElement loadA) {
		this.loadA = loadA;
	}
	
	
}
