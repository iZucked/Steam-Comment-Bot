/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker.DifferenceType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class Difference {
	public DifferenceType move;
	public ISequenceElement load;
	public ISequenceElement discharge;
	public IResource resource;
	
	public Difference(DifferenceType move, ISequenceElement load, ISequenceElement discharge, IResource resource) {
		this.move = move;
		this.load = load;
		this.discharge = discharge;
		this.resource = resource;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Difference) {
		return this.move.equals(((Difference) obj).move) && this.load==((Difference) obj).load && this.discharge==((Difference) obj).discharge && this.resource==((Difference) obj).resource;
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s", this.move, this.load, this.discharge, this.resource);
	}

}