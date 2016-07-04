/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * The {@link PerChainUnitScope} is a {@link Guice} {@link Scope} intended to be bound to {@link PerChainUnitScopeImpl} and entered/exited at the start and end of a optimisation run. This scope
 * annotation indicates that the bound variable should be {@link ThreadLocal} within that run. Note: such injected variables will need to be injected into object instances created outside of this
 * scope will need a {@link Provider} to select the correct thread local instance.
 * 
 * @author Simon Goodall
 *
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@ScopeAnnotation
public @interface PerChainUnitScope {
}
