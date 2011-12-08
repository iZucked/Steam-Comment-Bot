/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * The {@link ISequenceElement} is the object manipulated in the sequences passed to the optimiser. It represents a unique element in the sequences and is used as the key to many
 * {@link IDataComponentProvider}s implementations. {@link IDataComponentProvider}s provide additional information for these elements.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequenceElement extends IIndexedObject {

	/**
	 * A name for this element.
	 * 
	 * @return
	 */
	String getName();
}
