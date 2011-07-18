/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation.context;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;

/**
 * This is a singleton (sorry) for tracking information for validators that
 * cannot be passed in through the EMF validation framework, with a minimum of
 * ugliness.
 * 
 * Currently just provides methods for indicating the desired container (if any)
 * to constraints.
 * 
 * @author Tom Hinton
 * 
 */
public class ValidationSupport {
	private static final ValidationSupport INSTANCE = new ValidationSupport();

	private final WeakHashMap<EObject, Pair<EObject, EReference>> containers = new WeakHashMap<EObject, Pair<EObject, EReference>>();

	private final WeakHashMap<EObject, WeakHashMap<EReference, WeakHashMap<EObject, Boolean>>> reverseMap = new WeakHashMap<EObject, WeakHashMap<EReference, WeakHashMap<EObject, Boolean>>>();

	private final WeakHashMap<EObject, Boolean> ignoredObjects = new WeakHashMap<EObject, Boolean>();

	protected ValidationSupport() {

	}

	public static ValidationSupport getInstance() {
		return INSTANCE;
	}

	public synchronized void ignoreObjects(
			final Collection<? extends EObject> objects) {
		for (final EObject o : objects) {
			ignoredObjects.put(o, true);
		}
	}

	public synchronized void unignoreObjects(
			final Collection<? extends EObject> objects) {
		for (final EObject o : objects) {
			ignoredObjects.remove(o);
		}
	}

	public synchronized void clearContainers(
			final Collection<? extends EObject> objects) {
		for (final EObject o : objects) {
			final Pair<EObject, EReference> p = containers.get(o);
			if (p != null) {
				reverseMap.get(p.getFirst()).get(p.getSecond()).remove(o);
				containers.remove(o);
			}
		}
	}

	public synchronized void setContainers(
			final Collection<? extends EObject> objects,
			final EObject container, final EReference containingFeature) {
		final Pair<EObject, EReference> p = new Pair<EObject, EReference>(
				container, containingFeature);		
		for (final EObject o : objects) {
			containers.put(o, p);
		}

		WeakHashMap<EReference, WeakHashMap<EObject, Boolean>> extras = reverseMap
				.get(container);
		if (extras == null) {
			extras = new WeakHashMap<EReference, WeakHashMap<EObject, Boolean>>();
			reverseMap.put(container, extras);
		}

		WeakHashMap<EObject, Boolean> refs = extras.get(containingFeature);
		if (refs == null) {
			refs = new WeakHashMap<EObject, Boolean>();
			extras.put(containingFeature, refs);
		}

		for (final EObject o : objects) {
			refs.put(o, true);
		}
	}

	public synchronized Pair<EObject, EReference> getContainer(
			final EObject object) {
		if (containers.containsKey(object)) {
			return containers.get(object);
		} else {
			return new Pair<EObject, EReference>(object.eContainer(),
					(EReference) object.eContainingFeature());
		}
	}

	public synchronized List<EObject> getContents(final EObject container,
			final EReference containment) {
		final LinkedList<EObject> result = new LinkedList<EObject>();
		result.addAll((EList<EObject>) container.eGet(containment));

		final WeakHashMap<EReference, WeakHashMap<EObject, Boolean>> extras = reverseMap
				.get(container);
		if (extras != null) {
			final WeakHashMap<EObject, Boolean> extras2 = extras.get(containment);
			if (extras2 != null) {
				result.addAll(extras2.keySet());
			}
		}

		result.removeAll(ignoredObjects.keySet());
		
		return result;
	}
}
