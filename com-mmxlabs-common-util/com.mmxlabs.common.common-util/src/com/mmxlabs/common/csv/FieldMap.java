/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FieldMap implements IFieldMap {
	private Map<String, String> delegate;

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
	public boolean containsKey(Object key) {
		notifyAccess(key + "");
		return delegate.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		final Set<java.util.Map.Entry<String, String>> es = new HashSet<Entry<String, String>>();
		for (final Entry<String, String> de : delegate.entrySet()) {
			es.add(new Entry<String, String>() {

				@Override
				public String setValue(String value) {
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
	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	@Override
	public String get(Object key) {
		if (key instanceof String)
			notifyAccess((String) key);
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
	public String put(String key, String value) {
		return delegate.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		delegate.putAll(m);
	}

	@Override
	public String remove(Object key) {
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
	public IFieldMap getSubMap(String keyPrefix) {
		final HashMap<String, String> subDelegate = new HashMap<String, String>();
		for (final String key : delegate.keySet()) {
			if (key.startsWith(keyPrefix)) {
				subDelegate.put(key.substring(keyPrefix.length()), delegate.get(key));
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

	private Set<String> missedKeys = new HashSet<String>();

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
	public boolean containsPrefix(String keyPrefix) {
		for (final String key : delegate.keySet()) {
			if (key.startsWith(keyPrefix)) {
				return true;
			}
		}
		return false;
	}
}
