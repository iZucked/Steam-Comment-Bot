/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for to create java.util Collections objects.
 * 
 * @author Simon Goodall
 * 
 */
public final class CollectionsUtil {

	private CollectionsUtil() {
		
	}
	
	/**
	 * Create a {@link ArrayList} of objects from an unbounded list.
	 * 
	 * @param <T>
	 * @param sequences
	 * @return
	 */
	public static <T> List<T> makeArrayList(final T... elements) {

		final List<T> collection = new ArrayList<T>(elements.length);

		for (final T e : elements) {
			collection.add(e);
		}

		return collection;
	}

	/**
	 * Create a new {@link HashMap} from the list of elements. This method
	 * assumes an even number of elements in the form of { key1, value1, key2,
	 * value2,...}
	 * 
	 * @param <K>
	 * @param <V>
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> makeHashMap(final Object... elements) {

		final Map<K, V> map = new HashMap<K, V>();

		for (int i = 0; i < elements.length; i += 2) {
			final K key = (K) elements[i];
			final V value = (V) elements[i + 1];
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Returns a value from a {@link Map} if the key exists, otherwise return
	 * the specified default value.
	 * 
	 * @param <K>
	 * @param <T>
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <K, T> T getValue(final Map<K, T> map, final K key,
			final T defaultValue) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return defaultValue;
	}

	/**
	 * Create a hash set containing the given elements
	 * @param <T>
	 * @param elements
	 * @return
	 */
	public static <T> Set<T> makeHashSet(final T ... elements) {
		HashSet<T> result = new HashSet<T>();
		for (int i = 0; i<elements.length; i++) {
			result.add(elements[i]);
		}
		return result;
	}
}
