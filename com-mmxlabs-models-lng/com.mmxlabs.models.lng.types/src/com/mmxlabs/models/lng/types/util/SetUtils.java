/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;

import com.mmxlabs.models.lng.types.ObjectSet;

/**
 * Utility class to expand vessel sets and port sets.
 * 
 * @author hinton
 * 
 */
public class SetUtils {

	/**
	 * True iff the given objects sets expand to contain the given object
	 * 
	 * @param objectSets
	 * @param object
	 * @return
	 */
	public <T extends ObjectSet<T, U>, U> boolean contains(final Iterable<T> objectSets, final U object) {
		return getObjects(objectSets).contains(object);
	}

	/**
	 * Return a set containing each object in the given object sets
	 * 
	 * @param objectSets
	 * @return
	 */
	public static <T extends ObjectSet<T, U>, U> Set<U> getObjects(final Iterable<T> objectSets) {
		final HashSet<U> result = new HashSet<U>();
		addObjects(objectSets, result);
		return result;
	}

	/**
	 * return a set containing each object in the given object set.
	 * 
	 * @param objectSet
	 * @return
	 */
	public static <T extends ObjectSet<T, U>, U> Set<U> getObjects(final T objectSet) {
		final HashSet<U> result = new HashSet<U>();
		addObjects(objectSet, result);
		return result;
	}

	/**
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final Iterable<T> objectSets, Collection<U> output) {
		addObjects(objectSets, output, new UniqueEList<T>());
	}

	/**
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final Iterable<T> objectSets, Collection<U> output, EList<T> marks) {
		for (final T set : objectSets)
			addObjects(set, output, marks);
	}

	/**
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final T objectSet, Collection<U> output, EList<T> marks) {
		output.addAll(objectSet.collect(marks));
	}

	/**
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final T objectSet, Collection<U> output) {
		addObjects(objectSet, output, new UniqueEList<T>());
	}
}
