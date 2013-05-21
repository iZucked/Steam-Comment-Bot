/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
	 * @param vesselSets
	 * @param vessel
	 * @since 5.0
	 * @return
	 */
	public <T extends ObjectSet<T, U>, U> boolean contains(final Iterable<T> vesselSets, final U vessel) {
		return getObjects(vesselSets).contains(vessel);
	}

	/**
	 * Return a set containing each vessel in the given vessel sets
	 * 
	 * @param portSets
	 * @since 5.0
	 * @return
	 */
	public static <T extends ObjectSet<T, U>, U> Set<U> getObjects(final Iterable<T> portSets) {
		final HashSet<U> result = new HashSet<U>();
		addObjects(portSets, result);
		return result;
	}

	/**
	 * return a set containing each vessel in the given vessel set.
	 * 
	 * @param portSet
	 * @return
	 * @since 5.0
	 */
	public static <T extends ObjectSet<T, U>, U> Set<U> getObjects(final T portSet) {
		final HashSet<U> result = new HashSet<U>();
		addObjects(portSet, result);
		return result;
	}

	/**
	 * @since 5.0
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final Iterable<T> portSets, Collection<U> output) {
		addObjects(portSets, output, new UniqueEList<T>());
	}

	/**
	 * @since 5.0
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final Iterable<T> portSets, Collection<U> output, EList<T> marks) {
		for (final T set : portSets)
			addObjects(set, output, marks);
	}

	/**
	 * @since 5.0
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final T portSet, Collection<U> output, EList<T> marks) {
		output.addAll(portSet.collect(marks));
	}

	/**
	 * @since 5.0
	 */
	public static <T extends ObjectSet<T, U>, U> void addObjects(final T portSet, Collection<U> output) {
		addObjects(portSet, output, new UniqueEList<T>());
	}
}
