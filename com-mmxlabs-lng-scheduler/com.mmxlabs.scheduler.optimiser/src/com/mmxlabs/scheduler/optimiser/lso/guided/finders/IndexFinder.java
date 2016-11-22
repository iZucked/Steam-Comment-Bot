package com.mmxlabs.scheduler.optimiser.lso.guided.finders;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequence;

public class IndexFinder implements IFinder {

	private final int index;

	public IndexFinder(int index) {
		this.index = index;
	}

	@Override
	public int findInsertionIndex(final @NonNull ISequence sequence) {
		return index;
	}
}
