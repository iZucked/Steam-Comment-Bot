/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker.DifferenceType;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class Difference {
	public final @NonNull DifferenceType move;
	public final ISequenceElement load;
	public final ISequenceElement discharge;

	// Current resource, not the target
	public final IResource currentResource;

	public Difference(final @NonNull DifferenceType move, final ISequenceElement load, final ISequenceElement discharge, final IResource currentResource) {
		this.move = move;
		this.load = load;
		this.discharge = discharge;
		this.currentResource = currentResource;

		// Validate inputs
		if (move == DifferenceType.CARGO_NOT_IN_TARGET) {
			Preconditions.checkNotNull(currentResource);
			Preconditions.checkNotNull(load);
			Preconditions.checkNotNull(discharge);
		}
		// TODO: Validate other DifferenceTypes
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Difference) {
			return this.move.equals(((Difference) obj).move) && this.load == ((Difference) obj).load && this.discharge == ((Difference) obj).discharge
					&& this.currentResource == ((Difference) obj).currentResource;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %s", this.move, this.load, this.discharge, this.currentResource);
	}

}