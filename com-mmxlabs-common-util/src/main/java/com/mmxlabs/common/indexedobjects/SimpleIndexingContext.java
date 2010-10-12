package com.mmxlabs.common.indexedobjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mmxlabs.common.Pair;

public class SimpleIndexingContext implements IIndexingContext {
	/**
	 * The list of indices; each element is a pair containing a type, and the
	 * next index for objects of that type or (or with that as their closest
	 * superclass).
	 */
	private final List<Pair<Class<? extends Object>, Integer>> indices = new ArrayList<Pair<Class<? extends Object>, Integer>>();
	/**
	 * A set to keep track of what types we have complained about indexing as
	 * plain Objects
	 */
	private final Set<Class<? extends Object>> warnedTypes = new HashSet<Class<? extends Object>>();

	public SimpleIndexingContext() {
		super();
		// always track objects
		registerType(Object.class);
	}

	@Override
	public void registerType(Class<? extends Object> type) {
		for (final Pair<Class<? extends Object>, Integer> index : indices) {
			if (index.getFirst().equals(type))
				throw new RuntimeException(type
						+ " has already been registered");
			if (index.getSecond().equals(0) == false)
				throw new RuntimeException(
						"This context has been used - no more types can be registered, for the sake of index consistency");
		}
		indices.add(new Pair<Class<? extends Object>, Integer>(type, 0));
	}

	@Override
	public synchronized int assignIndex(final Object indexedObject) {
		final Pair<Class<? extends Object>, Integer> index = getLowestSuperclass(indexedObject
				.getClass());

		final int value = index.getSecond();
		index.setSecond(value + 1);

		return value;
	}

	/**
	 * This method is slow and clumsy, but we need it to avoid unexpected
	 * consequences of subclassing a type which is indexed and suddenly breaking
	 * all our indexed data structures.
	 * 
	 * @param type
	 * @return
	 */
	private final Pair<Class<? extends Object>, Integer> getLowestSuperclass(
			final Class<? extends Object> baseType) {
		Class<? extends Object> type = baseType;
		while (true) {
			for (final Pair<Class<? extends Object>, Integer> index : indices) {
				if (index.getFirst().equals(type)) {
					if (index.getFirst().equals(Object.class)
							&& !warnedTypes.contains(baseType)) {
						System.err.println("Warning: using object index for "
								+ baseType.getSimpleName());
						warnedTypes.add(baseType);
					}
					return index;
				}
			}
			type = type.getSuperclass();
		}
	}
}
