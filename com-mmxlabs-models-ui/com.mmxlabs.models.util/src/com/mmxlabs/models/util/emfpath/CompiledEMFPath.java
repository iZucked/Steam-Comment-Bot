/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.util.emfpath;

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

	/**
	 * Create a new path by appending to an existing path. 
	 * @param failSilently
	 * @param path
	 * @param append
	 */
	public CompiledEMFPath(final EMFPath path,
			final Object append) {
		this(path.failSilently, appendPath(path, append));
	}

	/**
	 * @param path1
	 * @param append
	 * @return
	 */
	private static Object[] appendPath(final EMFPath path, final Object append) {
		final Object[] result = Arrays.copyOf(path.path, path.path.length+1);
		result[result.length-1] = append;
		return result;
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
