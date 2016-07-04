/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;

/**
 * A sequences manipulator which applies a sequence of other sequences manipulators. Use the {@code addDelegate(..)} method to add another manipulator to the list of delegates.
 * 
 * @author hinton
 * 
 */
public class ChainedSequencesManipulator implements ISequencesManipulator {
	@NonNull
	private final List<ISequencesManipulator> delegates = new ArrayList<ISequencesManipulator>();

	public void addDelegate(final ISequencesManipulator delegate) {
		delegates.add(delegate);
	}

	@Override
	public void manipulate(@NonNull final IModifiableSequences sequences) {
		for (final ISequencesManipulator manipulator : delegates) {
			manipulator.manipulate(sequences);
		}
	}
}
