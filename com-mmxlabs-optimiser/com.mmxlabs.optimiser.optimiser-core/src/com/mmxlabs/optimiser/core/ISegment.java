/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

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
	ISequence getSequence();

}
