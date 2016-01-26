/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.emfpath;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;

/**
 * A device for finding things relative to an EObject; it can follow a sequence of EReferences and EOperations from a root object.
 * 
 * @author hinton
 * 
 */
public class EMFPath implements IEMFPath {
	final ETypedElement[] path;
	final boolean failSilently;

	public EMFPath(final boolean failSilently, final Iterable<ETypedElement> path) {
		final List<ETypedElement> scratch = new ArrayList<>();

		for (final ETypedElement o : path) {
			scratch.add(o);
		}

		this.path = scratch.toArray(new ETypedElement[scratch.size()]);
		checkPathIsValid();
		this.failSilently = failSilently;
	}

	public EMFPath(final boolean failSilently, final ETypedElement... path) {
		this.path = path.clone();
		checkPathIsValid();
		this.failSilently = failSilently;
	}

	private void checkPathIsValid() {
		for (final ETypedElement object : path) {
			assert object instanceof EStructuralFeature || object instanceof EOperation;
		}
	}

	@Override
	public Object get(final EObject root, final int depth) {
		if (failSilently) {
			try {
				return actuallyGet(root, depth);
			} catch (final Throwable ex) {
				return null;
			}
		} else {
			try {
				return actuallyGet(root, depth);
			} catch (final InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private Object actuallyGet(EObject root, final int depth) throws InvocationTargetException {
		if (depth == path.length)
			return root;
		for (int i = 0; i < path.length - (1 + depth); i++) {
			final ETypedElement el = path[i];
			root = (EObject) chase(root, el);
		}
		if (path.length > 0) {
			return chase(root, path[path.length - (1 + depth)]);
		} else {
			return root;
		}
	}

	private Object chase(final EObject root, final ETypedElement el) throws InvocationTargetException {

		if (root == null) {
			if (failSilently) {
				return null;
			} else {
				throw new NullPointerException();
			}
		}
		if (el instanceof EOperation) {
			if (failSilently && !root.eClass().getEAllOperations().contains(el)) {
				return null;
			}
			return root.eInvoke((EOperation) el, null);
		} else {
			if (failSilently && !root.eClass().getEAllStructuralFeatures().contains(el)) {
				return null;
			}
			return root.eGet((EStructuralFeature) el);
		}
	}

	/**
	 * @param input
	 * @param i
	 * @return
	 */
	@Override
	public Object get(final EObject input) {
		return get(input, 0);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final Object el : path) {
			if (sb.length() > 0)
				sb.append(".");
			if (el instanceof EOperation) {
				sb.append(((EOperation) el).getName());
			} else {
				sb.append(((EStructuralFeature) el).getName());
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (failSilently ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(path);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EMFPath other = (EMFPath) obj;
		if (failSilently != other.failSilently)
			return false;
		if (!Arrays.equals(path, other.path))
			return false;
		return true;
	}
}
