/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.compilation;

import org.eclipse.jdt.annotation.NonNull;

/**
 * An interface for clients of {@link MethodChainGenerator} to provide access to their classloader.
 * 
 * @author Tom Hinton
 * 
 */
public interface IInjectableClassLoader {
	<T> Class<T> injectAndLoadClass(@NonNull String qualifiedName, byte @NonNull [] bytecode) throws ClassNotFoundException;
}
