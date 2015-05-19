/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

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
	 * A workaround for the horrible effect of autoboxing on collections. Convert a collection of Integers to an array of ints.
	 * 
	 * @param integers
	 * @return
	 */
	public static @NonNull int[] integersToIntArray(@NonNull final Collection<Integer> integers) {
		final int[] result = new int[integers.size()];
		int ix = 0;
		for (final int x : integers) {
			result[ix++] = x;
		}
		return result;
	}

	/**
	 * Convert a {@link Collection} of {@link Long}s to an array of longs.
	 * 
	 * @param longs
	 * @return
	 */
	public static @NonNull long[] longsToLongArray(@NonNull final Collection<Long> longs) {
		final long[] result = new long[longs.size()];
		int ix = 0;
		for (final long x : longs) {
			result[ix++] = x;
		}
		return result;
	}

	/**
	 * A workaround for the horrible effect of autoboxing on collections. Convert an array of ints to an {@link ArrayList} of {@link Integer}s.
	 * 
	 * @param a
	 * @return
	 */
	public static @NonNull ArrayList<Integer> toArrayList(@NonNull final int[] a) {
		final ArrayList<Integer> list = new ArrayList<Integer>(a.length);
		for (final int v : a) {
			list.add(v);
		}
		return list;
	}

	/**
	 * A workaround for the horrible effect of autoboxing on collections. Convert an array of longs to an {@link ArrayList} of {@link Long}s.
	 * 
	 * @param a
	 * @return
	 */
	@NonNull
	public static final ArrayList<Long> toArrayList(@NonNull final long[] a) {
		final ArrayList<Long> list = new ArrayList<Long>(a.length);
		for (final long v : a) {
			list.add(v);
		}
		return list;
	}

	/**
	 * A workaround for the horrible effect of autoboxing on collections. Convert an array of shorts to an {@link ArrayList} of {@link Short}s.
	 * 
	 * @param a
	 * @return
	 */
	public static @NonNull ArrayList<Short> toArrayList(@NonNull final short[] a) {
		final ArrayList<Short> list = new ArrayList<Short>(a.length);
		for (final short v : a) {
			list.add(v);
		}
		return list;
	}

	/**
	 * Create a {@link ArrayList} of objects from an unbounded list.
	 * 
	 * @param <T>
	 * @param sequences
	 * @return
	 */
	@NonNull
	public static <T> List<T> makeArrayList(final T... elements) {

		final List<T> collection = new ArrayList<T>(elements.length);

		for (final T e : elements) {
			collection.add(e);
		}

		return collection;
	}

	/**
	 * Create a {@link ArrayList} of objects from an unbounded list.
	 * 
	 * @param <T>
	 * @param sequences
	 * @return
	 */
	@NonNull
	public static <T> List<T> makeArrayList2(@NonNull final Class<T> cls, final T... elements) {

		final List<T> collection = new ArrayList<T>(elements.length);

		for (final T e : elements) {
			collection.add(e);
		}

		return collection;
	}

	/**
	 * Create a new {@link HashMap} from the list of elements. This method assumes an even number of elements in the form of { key1, value1, key2, value2,...}
	 * 
	 * @param <K>
	 * @param <V>
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@NonNull
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
	 * Create a hash set containing the given elements
	 * 
	 * @param <T>
	 * @param elements
	 * @return
	 */
	public static <T> Set<T> makeHashSet(final T... elements) {
		final HashSet<T> result = new HashSet<T>();
		for (int i = 0; i < elements.length; i++) {
			result.add(elements[i]);
		}
		return result;
	}

	/**
	 * Returns a value from a {@link Map} if the key exists, otherwise return the specified default value.
	 * 
	 * @param <K>
	 * @param <T>
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static <K, T> T getValue(final Map<K, T> map, final K key, final T defaultValue) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return defaultValue;
	}

	public static class Sets {
		public static <T> Set<T> merge(Set<T>... sets) {
			Set<T> merged = new HashSet<T>();
			for (Set<T> set : sets) {
				merged.addAll(set);
			}
			return merged;
		}
	}

	public static class ASet {
		public static <T> Set<T> of(T... items) {
			Set<T> set = new HashSet<T>();
			set.addAll(Arrays.asList(items));
			return set;
		}
	}

}
