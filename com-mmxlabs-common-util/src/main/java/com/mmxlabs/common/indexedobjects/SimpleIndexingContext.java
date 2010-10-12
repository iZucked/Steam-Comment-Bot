package com.mmxlabs.common.indexedobjects;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.common.Pair;

public class SimpleIndexingContext implements IIndexingContext {
	private List<Pair<Class<? extends Object>, Integer>> indices = new ArrayList<Pair<Class<? extends Object>, Integer>>();

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
	 * This method is slow and clumsy, but we need it to avoid unexpected consequences of
	 * subclassing a type which is indexed and suddenly breaking all our indexed data structures.
	 * 
	 * @param type
	 * @return
	 */
	private final Pair<Class<? extends Object>, Integer> getLowestSuperclass(
			Class<? extends Object> type) {
		while (true) {
			for (final Pair<Class<? extends Object>, Integer> index : indices) {
				if (index.getFirst().equals(type))
					return index;
			}
			type = type.getSuperclass();
		}
	}
}
