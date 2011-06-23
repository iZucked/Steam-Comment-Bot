/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.Arrays;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Tom Hinton
 * 
 */
public class CompiledEMFPath extends EMFPath {
	final IEMFPathDelegate delegate;

	/**
	 * @param failSilently
	 * @param path
	 */
	public CompiledEMFPath(boolean failSilently, Object[] path) {
		super(failSilently, path);
		if (failSilently) {
			delegate = PathDelegateCache.getInstance().getPathDelegate(
					Arrays.asList(path));
		} else {
			delegate = null;
		}
	}

	@Override
	public Object get(final EObject input) {
		if (delegate == null) return super.get(input);
		return delegate.getValue(input);
	}
}
