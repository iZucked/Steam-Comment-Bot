/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.Nullable;

/**
 * 
 * @author Simon Goodall
 * 
 */
public interface ISegment extends Iterable<ISequenceElement> {

	/**
	 * Return size of segment
	 * 
	 * @return
	 */
	int size();

	/**
	 * Return an element within the segment.
	 * 
	 * @param index
	 * @return
	 */
	@Nullable
	ISequenceElement get(int index);

	/**
	 * Return segment start index in originating sequence
	 * 
	 * @return
	 */
	int getSequenceStart();

	/**
	 * Return segment end index in originating sequence
	 * 
	 * @return
	 */
	int getSequenceEnd();

	/**
	 * Return originating {@link ISequence} instance.
	 * 
	 * @return
	 */
	@Nullable
	ISequence getSequence();

}
