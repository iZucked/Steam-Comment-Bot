/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.emfpath;

import org.eclipse.emf.ecore.EObject;

import com.google.common.base.Joiner;

/**
 * A composite {@link IEMFPath} implementation returning the first object found.
 * 
 * @author Simon Goodall
 * 
 */
public class EMFMultiPath implements IEMFPath {
	private final IEMFPath[] paths;
	final boolean failSilently;

	public EMFMultiPath(final boolean failSilently, final IEMFPath... paths) {
		this.paths = paths;
		this.failSilently = failSilently;
	}

	@Override
	public Object get(final EObject root, final int depth) {

		Object result = null;
		Throwable t = null;
		for (final IEMFPath p : paths) {
			try {
				result = p.get(root, depth);
				if (result != null) {
					return result;
				}
			} catch (final Throwable pt) {
				if (t == null) {
					t = pt;
				}
			}

		}

		if (t != null && !failSilently) {
			throw new RuntimeException(t);
		}
		return result;
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
		return "[" + Joiner.on(",").join(paths).toString() + "]";
	}
}
