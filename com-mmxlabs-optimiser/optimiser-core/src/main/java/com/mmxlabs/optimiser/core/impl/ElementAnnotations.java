/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IElementAnnotations;

/**
 * Class to store "additional info" objects based upon keys.
 * 
 * @author Simon Goodall
 * 
 */
public final class ElementAnnotations implements IElementAnnotations {

	private final Map<String, Object> additionalInfo = new HashMap<String, Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optmiser.fitness.ISequenceSchedulerAdditionalInfo
	 * #get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <U> U get(final String key, final Class<U> clz) {

		if (additionalInfo.containsKey(key)) {
			return clz.cast(additionalInfo.get(key));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optmiser.fitness.ISequenceSchedulerAdditionalInfo
	 * #put(java.lang.String, java.lang.Object)
	 */
	@Override
	public void put(final String key, final Object value) {
		additionalInfo.put(key, value);
	}

	@Override
	public boolean containsKey(final String key) {
		return additionalInfo.containsKey(key);
	}

	@Override
	public void dispose() {
		additionalInfo.clear();
	}
}
