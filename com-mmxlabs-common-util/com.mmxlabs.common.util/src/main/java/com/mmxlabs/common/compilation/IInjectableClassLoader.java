/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.compilation;

/**
 * An interface for clients of {@link MethodChainGenerator} to provide access to their classloader.
 * 
 * @author Tom Hinton
 * 
 */
public interface IInjectableClassLoader {
	public Class<?> injectAndLoadClass(String qualifiedName, byte[] bytecode) throws ClassNotFoundException;
}
