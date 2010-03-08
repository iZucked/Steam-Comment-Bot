package com.mmxlabs.optimiser.lso.impl.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.lso.IMove;

/**
 * Utility class for {@link IMove} implementations.
 * 
 * @author Simon Goodall
 * 
 */
public class MoveUtil {

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
}
