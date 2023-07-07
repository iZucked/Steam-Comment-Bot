/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;

@NonNullByDefault
public interface IPreSequencedSegmentProvider {

	boolean validSequence(ISequenceElement a, ISequenceElement b);

	/**
	 * Return true if this element is the first element in a pre-sequenced pair
	 * 
	 * @param a
	 * @return
	 */
	boolean isPreSequenced(ISequenceElement a);
}
