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
 * Unused Load swap change
 * @author achurchill
 *
 */
public class UnusedToUsedLoadChange extends Change implements Serializable {
	private transient ISequenceElement discharge;
	private transient ISequenceElement usedLoad;
	private transient ISequenceElement unusedLoad;
	private transient IResource resource;
	
	public UnusedToUsedLoadChange(String description, ISequenceElement discharge, ISequenceElement usedLoad, ISequenceElement unusedLoad, IResource resource) {
		super(description);
		
		this.setDischarge(discharge);
		this.setUsedLoad(usedLoad);
		this.setUnusedLoad(unusedLoad);
		
		this.setResource(resource);
	}

	@NonNull
	public IResource getResource() {
		return resource;
	}

	public void setResource(IResource resource) {
		this.resource = resource;
	}

	@NonNull
	public ISequenceElement getUsedLoad() {
		return usedLoad;
	}

	public void setUsedLoad(ISequenceElement usedLoad) {
		this.usedLoad = usedLoad;
	}
	
	@NonNull
	public ISequenceElement getUnusedLoad() {
		return unusedLoad;
	}

	public void setUnusedLoad(ISequenceElement unusedLoad) {
		this.unusedLoad = unusedLoad;
	}

	@NonNull
	public ISequenceElement getDischarge() {
		return discharge;
	}

	public void setDischarge(ISequenceElement discharge) {
		this.discharge = discharge;
	}
	
	
}
