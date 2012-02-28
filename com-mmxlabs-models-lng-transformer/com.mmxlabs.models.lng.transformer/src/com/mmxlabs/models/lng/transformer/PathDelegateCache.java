/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.compilation.ITransformer;
import com.mmxlabs.common.compilation.InjectableClassLoader;
import com.mmxlabs.common.compilation.MethodChainGenerator;

/**
 * @author Tom Hinton
 * 
 */
public class PathDelegateCache {
	private static final PathDelegateCache INSTANCE = new PathDelegateCache();
	private final InjectableClassLoader loader = new InjectableClassLoader(getClass().getClassLoader());
	private final MethodChainGenerator generator = new MethodChainGenerator();
	public static PathDelegateCache getInstance() {
		return INSTANCE;
	}

	private final HashMap<List<Object>, ITransformer> cache = new HashMap<List<Object>, ITransformer>();
	
	protected PathDelegateCache() {

	}

	public synchronized ITransformer getPathDelegate(final List<Object> path) {
		if (cache.containsKey(path)) {
			return cache.get(path);
		} else {
			final ITransformer answer = compileDelegate(path);
			cache.put(path, answer);
			return answer;
		}
	}

	/**
	 * Create a new delegate
	 * @param path
	 * @return
	 */
	private synchronized ITransformer compileDelegate(
			final List<Object> path) {
		final List<Method> methods = new LinkedList<Method>();
		
		// find Methods for ESF/EOps.
		
		for (final Object o : path) {
			final Class<?> containerClass;
			final String methodName;
			if (o instanceof EStructuralFeature) {
				containerClass = ((EStructuralFeature) o).getContainerClass();
				final String refName = ((EStructuralFeature) o).getName();
				methodName = "get" + Character.toUpperCase(refName.charAt(0)) + 
					refName.substring(1);
			} else if (o instanceof EOperation) {
				containerClass = ((EOperation) o).getEContainingClass().getInstanceClass();
				methodName = ((EOperation) o).getName();
			} else {
				return null;
			}
			try {
				methods.add(containerClass.getDeclaredMethod(methodName));
			} catch (final Exception ex) {
				return null;
			}
		}
		
		final Class<ITransformer> tc = (Class<ITransformer>) generator
				.createTransformer(methods, loader);
		try {
			return tc.newInstance();
		} catch (final Exception ex) {
			return null; //fallthrough to no transformer
		}
	}
}
