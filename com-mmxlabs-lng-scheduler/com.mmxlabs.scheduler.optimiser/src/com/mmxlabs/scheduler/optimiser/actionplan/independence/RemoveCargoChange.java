/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionplan.independence;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.actionplan.Change;

/**
 * Rewire change
 * @author achurchill
 *
 */
public class RemoveCargoChange extends Change implements Serializable {
	private transient ISequenceElement loadA;
	private transient ISequenceElement dischargeA;
	private transient IResource resourceA;
	
	public RemoveCargoChange(@NonNull String description, @NonNull ISequenceElement loadA, @NonNull ISequenceElement dischargeA, @NonNull IResource resourceA) {
		super(description);
		
		this.setLoadA(loadA);
		this.setDischargeA(dischargeA);
		
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
	public IResource getResourceA() {
		return resourceA;
	}

	public void setResourceA(IResource resourceA) {
		this.resourceA = resourceA;
	}
	
	
}
