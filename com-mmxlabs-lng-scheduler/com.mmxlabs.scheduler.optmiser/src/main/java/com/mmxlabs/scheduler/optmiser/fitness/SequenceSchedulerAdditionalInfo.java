package com.mmxlabs.scheduler.optmiser.fitness;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to store "additional info" objects based upon keys.
 * 
 * @author Simon Goodall
 * 
 */
public final class SequenceSchedulerAdditionalInfo {

	private final Map<String, Object> additionalInfo = new HashMap<String, Object>();

	public <U> U get(final String key, final Class<U> clz) {

		if (additionalInfo.containsKey(key)) {
			return clz.cast(additionalInfo.get(key));
		}
		return null;
	}

	public void put(final String key, final Object value) {
		additionalInfo.put(key, value);
	}
}
