/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.finders;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class BeforeElementFinder implements IFinder {

	private final @NonNull ISequenceElement element;

	public BeforeElementFinder(final @NonNull ISequenceElement element) {
		this.element = element;
	}

	@Override
	public int findInsertionIndex(final @NonNull ISequence sequence) {

		for (int i = 0; i < sequence.size(); ++i) {
			if (element == sequence.get(i)) {
				return i;
			}
		}
		return -1;
	}
}
