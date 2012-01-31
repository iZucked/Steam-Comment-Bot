/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.compilation;

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
	public synchronized Class<?> injectAndLoadClass(final String qualifiedName, final byte[] bytecode) throws ClassNotFoundException {
		fqn = qualifiedName;
		this.bytecode = bytecode;
		final Class<?> result = this.loadClass(qualifiedName);
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
