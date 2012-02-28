/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

	private final WeakHashMap<EObject, EObject> editedObjects = new WeakHashMap<EObject, EObject>();

	protected ValidationSupport() {

	}

	public static ValidationSupport getInstance() {
		return INSTANCE;
	}

	public synchronized void startEditingObjects(final Collection<? extends EObject> real, final Collection<? extends EObject> copy) {
		final Iterator<? extends EObject> i1 = real.iterator();
		final Iterator<? extends EObject> i2 = copy.iterator();
		while (i1.hasNext() && i2.hasNext()) {
			final EObject r = i1.next();
			final EObject c = i2.next();

			editedObjects.put(r, c);

			final Pair<EObject, EReference> container = getContainer(r);

			setContainers(Collections.singleton(c), container.getFirst(), container.getSecond());
		}
		ignoreObjects(real);
	}

	public boolean isSame(EObject a, EObject b) {
		if (editedObjects.containsKey(a)) {
			a = editedObjects.get(a);
		}
		if (editedObjects.containsKey(b)) {
			b = editedObjects.get(b);
		}

		return a == b;
	}

	public synchronized void endEditingObjects(final Collection<? extends EObject> real, final Collection<? extends EObject> copy) {
		clearContainers(copy);
		unignoreObjects(real);
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

	public <T> T getParentObjectType(Class<T> cls, EObject eObject) {
		while (!cls.isInstance(eObject)) {
			eObject = getContainer(eObject).getFirst();
			if (eObject == null) return null;
		}
		return cls.cast(eObject);
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

	@SuppressWarnings("unchecked")
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
