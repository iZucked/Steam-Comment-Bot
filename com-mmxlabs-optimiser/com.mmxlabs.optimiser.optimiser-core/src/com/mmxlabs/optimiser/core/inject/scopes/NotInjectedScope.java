/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.inject.scopes;

/**
 * 
 * @author Simon Goodall
 * @see https://github.com/google/guice/wiki/CustomScopes
 */

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.Guice;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.ScopeAnnotation;

/**
 * * Simple Scope to forbid object use in the injector.
 * 
 * @author Simon Goodall
 *
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@ScopeAnnotation
public @interface NotInjectedScope {
}
