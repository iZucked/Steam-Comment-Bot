/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation.context;

import java.util.Collection;
import java.util.WeakHashMap;

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

	protected ValidationSupport() {

	}

	public static ValidationSupport getInstance() {
		return INSTANCE;
	}

	public synchronized void clearContainers(
			final Collection<? extends EObject> objects) {
		for (final EObject o : objects) {
			containers.remove(o);
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
}
