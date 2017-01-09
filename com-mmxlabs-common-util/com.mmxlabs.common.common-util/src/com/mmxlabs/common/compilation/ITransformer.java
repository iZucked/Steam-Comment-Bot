/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.compilation;

/**
 * A very minimal interface for generated classes to use
 * 
 * @author Tom Hinton
 * 
 */
public interface ITransformer {
	public Object transform(Object input);
}
