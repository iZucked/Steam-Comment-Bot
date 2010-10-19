/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;

/**
 * A sequences manipulator which applies a sequence of other sequences
 * manipulators. Use the {@code addDelegate(..)} method to add another
 * manipulator to the list of delegates.
 * 
 * @author hinton
 * 
 * @param <T>
 */
public class ChainedSequencesManipulator<T> implements ISequencesManipulator<T> {
	List<ISequencesManipulator<T>> delegates = new ArrayList<ISequencesManipulator<T>>();

	public void addDelegate(ISequencesManipulator<T> delegate) {
		delegates.add(delegate);
	}

	@Override
	public void manipulate(IModifiableSequences<T> sequences) {
		for (ISequencesManipulator<T> manipulator : delegates) {
			manipulator.manipulate(sequences);
		}
	}

	@Override
	public void dispose() {
		for (ISequencesManipulator<T> manipulator : delegates) {
			manipulator.dispose();
		}
		delegates.clear();
		delegates = null;
	}

}
