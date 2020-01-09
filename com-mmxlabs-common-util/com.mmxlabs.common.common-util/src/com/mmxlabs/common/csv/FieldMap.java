/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FieldMap implements IFieldMap {
	private final Map<String, String> delegate;

	private String prefix;
	private FieldMap superMap;

	public FieldMap(final Map<String, String> delegate) {
		this.delegate = delegate;
		missedKeys.addAll(delegate.keySet());
	}

	/**
	 */
	public FieldMap(final Map<String, String> delegate, final String prefix, final FieldMap owner) {
		this(delegate);
		this.prefix = prefix;
		this.superMap = owner;
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public boolean containsKey(final Object key) {
		notifyAccess(key + "");
		return delegate.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return delegate.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		final Set<java.util.Map.Entry<String, String>> es = new HashSet<>();
		for (final Entry<String, String> de : delegate.entrySet()) {
			es.add(new Entry<String, String>() {

				@Override
				public String setValue(final String value) {
					return de.setValue(value);
				}

				@Override
				public String getValue() {
					final String key = de.getKey();
					notifyAccess(key);
					return de.getValue();
				}

				@Override
				public String getKey() {
					final String key = de.getKey();
					notifyAccess(key);
					return key;
				}
			});
		}
		return delegate.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return delegate.equals(o);
	}

	@Override
	public String get(final Object key) {
		if (key instanceof String) {
			notifyAccess((String) key);
		}
		return delegate.get(key);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return delegate.keySet();
	}

	@Override
	public String put(final String key, final String value) {
		return delegate.put(key, value);
	}

	@Override
	public void putAll(final Map<? extends String, ? extends String> m) {
		delegate.putAll(m);
	}

	@Override
	public String remove(final Object key) {
		return delegate.remove(key);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public Collection<String> values() {
		return delegate.values();
	}

	@Override
	public IFieldMap getSubMap(final String keyPrefix) {
		final HashMap<String, String> subDelegate = new HashMap<>();
		for (final Map.Entry<String, String> e : delegate.entrySet()) {
			final String key = e.getKey();
			if (key.startsWith(keyPrefix)) {
				subDelegate.put(key.substring(keyPrefix.length()), e.getValue());
			}
		}
		return new FieldMap(subDelegate, keyPrefix, this);
	}

	@Override
	public Set<String> getUnreadKeys() {
		return missedKeys;
	}

	@Override
	public String getLastAccessedKey() {
		return lastAccessedKey;
	}

	private final Set<String> missedKeys = new HashSet<>();

	private String lastAccessedKey = "";

	protected void notifyAccess(final String key) {
		this.lastAccessedKey = key;
		missedKeys.remove(key);
		if (superMap != null) {
			superMap.notifyAccess(prefix + key);
		}
	}

	/**
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 */
	public FieldMap getSuperMap() {
		return superMap;
	}

	/**
	 */
	@Override
	public boolean containsPrefix(final String keyPrefix) {
		for (final String key : delegate.keySet()) {
			if (key.startsWith(keyPrefix)) {
				return true;
			}
		}
		return false;
	}
}
