/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public interface ISequencesAttributesProvider {

	/**
	 * Return an instance of a shared provider type specific to this sequences *and*
	 * any derived instance. And derived instance should also contain the same
	 * instances of providers.
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 */

	<T> @Nullable T getProvider(Class<T> cls);
}
