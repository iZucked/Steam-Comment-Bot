/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * A sequences manipulator which applies a sequence of other sequences manipulators. Use the {@code addDelegate(..)} method to add another manipulator to the list of delegates.
 * 
 * @author hinton
 * 
 */
public class ChainedSequencesManipulator implements ISequencesManipulator {
	private final List<ISequencesManipulator> delegates = new ArrayList<ISequencesManipulator>();

	public void addDelegate(final ISequencesManipulator delegate) {
		delegates.add(delegate);
	}

	@Override
	public void manipulate(final IModifiableSequences sequences) {
		for (final ISequencesManipulator manipulator : delegates) {
			manipulator.manipulate(sequences);
		}
	}

	@Override
	public void dispose() {
		for (final ISequencesManipulator manipulator : delegates) {
			manipulator.dispose();
		}
		delegates.clear();
	}

	/**
	 * Init method. This will call {@link ISequencesManipulator#init(IOptimisationData)} on all delegates.
	 */
	@Override
	public void init(final IOptimisationData data) {
		for (final ISequencesManipulator manipulator : delegates) {
			manipulator.init(data);
		}
	}
}
