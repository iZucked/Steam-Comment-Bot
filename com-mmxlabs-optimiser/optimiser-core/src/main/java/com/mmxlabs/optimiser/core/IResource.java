/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * This interface defines an object representing the resource assigned to a {@link ISequence}.
 * 
 * @author Simon Goodall
 * 
 */
public interface IResource extends IIndexedObject {

	/**
	 * Returns the name of the resource
	 * 
	 * @return
	 */
	String getName();

}
