/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.compilation;

import org.eclipse.jdt.annotation.NonNull;

/**
 * @author Tom Hinton
 * 
 */
public class InjectableClassLoader extends ClassLoader implements IInjectableClassLoader {
	String fqn = null;
	byte[] bytecode = null;

	/**
	 * @param classLoader
	 */
	public InjectableClassLoader(final ClassLoader classLoader) {
		super(classLoader);
	}

	@Override
	public synchronized <T> Class<T> injectAndLoadClass(final @NonNull String qualifiedName, final byte @NonNull [] bytecode) throws ClassNotFoundException {
		fqn = qualifiedName;
		this.bytecode = bytecode;
		@SuppressWarnings("unchecked")
		final Class<T> result = (Class<T>) this.loadClass(qualifiedName);
		this.bytecode = null;
		fqn = null;
		return result;
	}

	@Override
	protected synchronized Class<?> findClass(final String name) throws ClassNotFoundException {
		if (name.equals(fqn)) {
			return defineClass(fqn, bytecode, 0, bytecode.length);
		}
		return super.findClass(name);
	}
}
