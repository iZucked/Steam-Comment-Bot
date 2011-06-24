/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.Arrays;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.compilation.ITransformer;

/**
 * @author Tom Hinton
 * 
 */
public class CompiledEMFPath extends EMFPath {
	final ITransformer delegate;

	/**
	 * @param failSilently
	 * @param path
	 */
	public CompiledEMFPath(boolean failSilently, Object... path) {
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
		if (delegate == null)
			return super.get(input);
		try {
			return delegate.transform(input);
		} catch (Exception ex) {
			return null;
		}
	}
}
