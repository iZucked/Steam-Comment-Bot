/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link ObjectRegistry} stores object references first by {@link Class} and then by a key.
 * 
 * @author Simon Goodall
 * 
 */
public final class ObjectRegistry {

	private final Map<Class<?>, Map<Object, Object>> registry = new HashMap<Class<?>, Map<Object, Object>>();

	/**
	 * Returns the object registered under the given key for the specified {@link Class}. Returns null if no object is found.
	 * 
	 * @param <T>
	 * @param cls
	 * @param key
	 * @return
	 */
	public <T> T getValue(final Class<T> cls, final Object key) {
		if (registry.containsKey(cls)) {
			final Map<Object, Object> m = registry.get(cls);
			if (m.containsKey(key)) {
				final Object o = m.get(key);
				return cls.cast(o);
			}
		}
		return cls.cast(null);
	}

	/**
	 * Store the given object value under the specified Class and key.
	 * 
	 * @param <T>
	 * @param cls
	 * @param key
	 * @param value
	 *            Object to store
	 */
	public <T> void setValue(final Class<T> cls, final Object key, final T value) {

		final Map<Object, Object> m;
		if (registry.containsKey(cls)) {
			m = registry.get(cls);
		} else {
			m = new HashMap<Object, Object>();
			registry.put(cls, m);
		}
		m.put(key, value);
	}

	/**
	 * Returns true if there is an object registered under the given class and key.
	 * 
	 * @param cls
	 * @param key
	 * @return
	 */
	public boolean containsValue(final Class<?> cls, final Object key) {
		if (registry.containsKey(cls)) {
			final Map<Object, Object> m = registry.get(cls);
			return (m.containsKey(key));
		}
		return false;
	}

	public void dispose() {
		registry.clear();
	}
}
