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
public class RewireChange extends Change implements Serializable {
	private transient ISequenceElement element1A;
	private transient ISequenceElement element2A;
	private transient ISequenceElement element1B;
	private transient ISequenceElement element2B;
	private transient IResource resourceA;
	private transient IResource resourceB;
	
	public RewireChange(String description, ISequenceElement loadA, ISequenceElement dischargeA, ISequenceElement loadB, ISequenceElement dischargeB, IResource resourceA, IResource resourceB) {
		super(description);
		
		this.setElement1A(loadA);
		this.setElement2A(dischargeA);
		this.setElement1B(loadB);
		this.setElement2B(dischargeB);
		
		this.setResourceA(resourceA);
		this.setResourceB(resourceB);
	}
	
	@NonNull
	public IResource getResourceA() {
		return resourceA;
	}

	public void setResourceA(IResource resourceA) {
		this.resourceA = resourceA;
	}

	@NonNull
	public ISequenceElement getElement1A() {
		return element1A;
	}

	public void setElement1A(ISequenceElement element1a) {
		element1A = element1a;
	}

	@NonNull
	public ISequenceElement getElement2A() {
		return element2A;
	}

	public void setElement2A(ISequenceElement element2a) {
		element2A = element2a;
	}

	@NonNull
	public ISequenceElement getElement1B() {
		return element1B;
	}

	public void setElement1B(ISequenceElement element1b) {
		element1B = element1b;
	}

	@NonNull
	public ISequenceElement getElement2B() {
		return element2B;
	}

	public void setElement2B(ISequenceElement element2b) {
		element2B = element2b;
	}

	@NonNull
	public IResource getResourceB() {
		return resourceB;
	}

	public void setResourceB(IResource resourceB) {
		this.resourceB = resourceB;
	}
	
	
}
