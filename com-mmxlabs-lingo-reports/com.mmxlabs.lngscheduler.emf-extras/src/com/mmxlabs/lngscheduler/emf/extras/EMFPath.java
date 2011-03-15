/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A device for finding things relative to an EObject; it can follow a sequence
 * of EReferences and EOperations from a root object.
 * 
 * @author hinton
 * 
 */
public class EMFPath {
	final Object[] path;
	final boolean failSilently;
	public EMFPath(boolean failSilently, final Iterable<?> path) {
		final ArrayList<Object> scratch = new ArrayList<Object>();
		
		for (final Object o : path) {
			scratch.add(o);
		}
		
		this.path = scratch.toArray();
		checkPathIsValid();
		this.failSilently = failSilently;
	}
	
	public EMFPath(boolean failSilently, final Object... path) {
		this.path = path.clone();
		checkPathIsValid();
		this.failSilently = failSilently;
	}

	private void checkPathIsValid() {
		for (final Object object : path) {
			assert object instanceof EStructuralFeature
					|| object instanceof EOperation;
		}
	}

	public Object get(EObject root) {
		if (failSilently) {
			try {
				return actuallyGet(root);
			} catch (Throwable ex) {
				return null;	
			}
		} else {
			try {
				return actuallyGet(root);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public EClassifier getTargetType() {
		if (path.length == 0) return null;
		final Object el = path[path.length - 1];
		if (el instanceof EOperation) {
			return ((EOperation)el).getEType();
		} else {
			return ((EStructuralFeature)el).getEType();
		}
	}
	
	private Object actuallyGet(EObject root) throws InvocationTargetException {
		for (int i = 0; i < path.length - 1; i++) {
			final Object el = path[i];
			root = (EObject) chase(root, el);
		}
		if (path.length > 0) {
			return chase(root, path[path.length - 1]);
		} else {
			return root;
		}
	}

	private Object chase(final EObject root, final Object el) throws InvocationTargetException {
		if (el instanceof EOperation) {
			return root.eInvoke((EOperation) el, null);
		} else {
			return root.eGet((EStructuralFeature) el);
		}
	}
	
	
}
