/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.emfpath;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.compilation.ITransformer;
import com.mmxlabs.common.compilation.InjectableClassLoader;
import com.mmxlabs.common.compilation.MethodChainGenerator;

/**
 * @author Tom Hinton
 * 
 */
public class PathDelegateCache {
	private static final PathDelegateCache INSTANCE = new PathDelegateCache();

	private final MethodChainGenerator generator = new MethodChainGenerator();

	public static PathDelegateCache getInstance() {
		return INSTANCE;
	}

	private final HashMap<Pair<ClassLoader, List<ETypedElement>>, ITransformer> cache = new HashMap<>();

	protected PathDelegateCache() {

	}

	public synchronized ITransformer getPathDelegate(final ClassLoader loader, final List<ETypedElement> path) {
		final Pair<ClassLoader, List<ETypedElement>> key = new Pair<>(loader, path);
		if (cache.containsKey(key)) {
			return cache.get(key);
		} else {
			final ITransformer answer = compileDelegate(new InjectableClassLoader(loader), path);
			cache.put(key, answer);
			return answer;
		}
	}

	/**
	 * Create a new delegate
	 * 
	 * @param injectableClassLoader
	 * @param path
	 * @return
	 */
	private synchronized ITransformer compileDelegate(final InjectableClassLoader injectableClassLoader, final List<ETypedElement> path) {
		final List<Method> methods = new LinkedList<Method>();

		// find Methods for ESF/EOps.

		for (final ETypedElement o : path) {
			final Class<?> containerClass;
			final String methodName;
			if (o instanceof EStructuralFeature) {
				containerClass = ((EStructuralFeature) o).getContainerClass();
				final String refName = ((EStructuralFeature) o).getName();
				methodName = "get" + Character.toUpperCase(refName.charAt(0)) + refName.substring(1);
			} else if (o instanceof EOperation) {
				containerClass = ((EOperation) o).getEContainingClass().getInstanceClass();
				methodName = ((EOperation) o).getName();
			} else {
				return null;
			}
			if (containerClass == null) {
				return null;
			}
			try {
				methods.add(containerClass.getDeclaredMethod(methodName));
			} catch (final Exception ex) {
				return null;
			}
		}

		final Class<ITransformer> tc = (Class<ITransformer>) generator.createTransformer(methods, injectableClassLoader);
		try {
			return tc.newInstance();
		} catch (final Exception ex) {
			return null; // fallthrough to no transformer
		}
	}
}
