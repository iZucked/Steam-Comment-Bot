/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class for to create java.util Collections objects.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class CollectionsUtil {

	private CollectionsUtil() {
	}

	/**
	 * A workaround for the horrible effect of autoboxing on collections. Convert a
	 * collection of Integers to an array of ints.
	 * 
	 * @param integers
	 * @return
	 */
	public static int[] integersToIntArray(final Collection<Integer> integers) {
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
	public static long[] longsToLongArray(final Collection<Long> longs) {
		final long[] result = new long[longs.size()];
		int ix = 0;
		for (final long x : longs) {
			result[ix++] = x;
		}
		return result;
	}

	/**
	 * A workaround for the horrible effect of autoboxing on collections. Convert an
	 * array of ints to an {@link ArrayList} of {@link Integer}s.
	 * 
	 * @param a
	 * @return
	 */
	public static List<Integer> toArrayList(final int[] a) {
		final ArrayList<Integer> list = new ArrayList<>(a.length);
		for (final int v : a) {
			list.add(v);
		}
		return list;
	}

	/**
	 * A workaround for the horrible effect of autoboxing on collections. Convert an
	 * array of longs to an {@link ArrayList} of {@link Long}s.
	 * 
	 * @param a
	 * @return
	 */

	public static final List<Long> toArrayList(final long[] a) {
		final ArrayList<Long> list = new ArrayList<>(a.length);
		for (final long v : a) {
			list.add(v);
		}
		return list;
	}

	/**
	 * A workaround for the horrible effect of autoboxing on collections. Convert an
	 * array of shorts to an {@link ArrayList} of {@link Short}s.
	 * 
	 * @param a
	 * @return
	 */
	public static List<Short> toArrayList(final short[] a) {
		final ArrayList<Short> list = new ArrayList<>(a.length);
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
	public static <T> List<T> makeArrayList(final T... elements) {

		final List<T> collection = new ArrayList<>(elements.length);
		Collections.addAll(collection, elements);
		return collection;
	}

	/**
	 * Create a new {@link HashMap} from the list of elements. This method assumes
	 * an even number of elements in the form of { key1, value1, key2, value2,...}
	 * 
	 * @param <K>
	 * @param <V>
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> makeHashMap(final Object... elements) {

		final Map<K, V> map = new HashMap<>();

		for (int i = 0; i < elements.length; i += 2) {
			final K key = (K) elements[i];
			final V value = (V) elements[i + 1];
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Create a new {@link HashMap} from the list of elements. This method takes
	 * a list of 
	 * 
	 * @param <K>
	 * @param <V>
	 * @param elements
	 * @return
	 */
	public static <K, V> HashMap<K, V> makeHashMap(final List<Pair<K, V>> elements) {

		final HashMap<K, V> map = new HashMap<>();
		for (Pair<K, V> pair : elements) {
			map.put(pair.getFirst(), pair.getSecond());
		}
		return map;
	}

	/**
	 * Create a new {@link HashMap} from the list of elements. This method assumes
	 * an even number of elements in the form of { key1, value1, key2, value2,...}
	 * 
	 * @param <K>
	 * @param <V>
	 * @param elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> makeLinkedHashMap(final Object... elements) {

		final Map<K, V> map = new LinkedHashMap<>();

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
		final Set<T> result = new HashSet<>();
		Collections.addAll(result, elements);
		return result;
	}

	/**
	 * Create a hash set containing the given elements
	 * 
	 * @param <T>
	 * @param elements
	 * @return
	 */
	public static <T> Set<T> makeLinkedHashSet(final T... elements) {
		final Set<T> result = new LinkedHashSet<>();
		Collections.addAll(result, elements);
		return result;
	}

	public static <T> List<T> makeLinkedList(final T... elements) {
		LinkedList<T> newList = new LinkedList<>();
		Collections.addAll(newList, elements);
		return newList;
	}

	/**
	 * Returns a value from a {@link Map} if the key exists, otherwise return the
	 * specified default value.
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

		private Sets() {

		}

		public static <T> Set<T> merge(final Set<T>... sets) {
			final Set<T> merged = new HashSet<>();
			for (final Set<T> set : sets) {
				merged.addAll(set);
			}
			return merged;
		}
	}

	public static class ASet {
		private ASet() {

		}

		public static <T> Set<T> of(final T... items) {
			final Set<T> set = new HashSet<>();
			set.addAll(Arrays.asList(items));
			return set;
		}
	}

	public static <T> @Nullable T getFirstElement(List<T> list) {
		return list.isEmpty() ? null : list.get(0);
	}

	public static <T> @Nullable T getLastElement(List<T> list) {
		return list.isEmpty() ? null : list.get(list.size() - 1);
	}
}
