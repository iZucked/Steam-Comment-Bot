/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.inject.scopes;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Scopes a single execution of a block of code. Apply this scope with a try/finally block:
 * 
 * <pre>
 * <code>
 *
 *   scope.enter();
 *   try {
 *     // explicitly seed some seed objects...
 *     scope.seed(Key.get(SomeObject.class), someObject);
 *     // create and access scoped objects
 *   } finally {
 *     scope.exit();
 *   }
 * </code>
 * </pre>
 * 
 * @author Simon Goodall
 */
public class PerChainUnitScopeImpl implements Scope, AutoCloseable {

	private static final Provider<Object> SEEDED_KEY_PROVIDER = new Provider<Object>() {
		@Override
		public Object get() {
			throw new IllegalStateException("If you got here then it means that" + " your code asked for scoped object which should have been" + " explicitly seeded in this scope by calling"
					+ " SimpleScope.seed(), but was not.");
		}
	};

	private final ThreadLocal<Map<Key<?>, Object>> values = new ThreadLocal<>();

	public void enter() {
		checkState(values.get() == null, "A scoping block is already in progress");
		values.set(Maps.<Key<?>, Object> newHashMap());
	}

	@Override
	public void close() {
		// For Autoclosable
		exit();
	}

	public void exit() {
		checkState(values.get() != null, "No scoping block in progress");
		values.remove();
	}

	/**
	 * An alternative {@link #exit()} method to clean up state for a given {@link Thread}. Normally {@link #exit()} should be called on the running {@link Thread}. However in certain circumstances
	 * this may not be possible and this method needs to be invoked instead.Note this uses reflection to clean up the state and as such uses internal APIs. It is not recommended to use this method
	 * unless there is no other option.
	 * 
	 * @param thread
	 */
	public void exit(final @NonNull Thread thread) {
		try {
			//// From ThreadLocal#remove: ThreadLocalMap m = getMap(Thread.currentThread());

			// Get a reference to the thread locals table of the current thread
			final Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
			threadLocalsField.setAccessible(true);
			final Object threadLocalTable = threadLocalsField.get(thread);

			// Get a reference to the array holding the thread local variables inside the
			// ThreadLocalMap of the current thread
			final Class<?> threadLocalMapClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");

			//// From ThreadLocal#remove: if (m != null)
			if (threadLocalTable != null) {
				//// From ThreadLocal#remove: m.remove(this);
				final Method removeMethod = threadLocalMapClass.getDeclaredMethod("remove", ThreadLocal.class);
				removeMethod.setAccessible(true);
				removeMethod.invoke(threadLocalTable, values);
				removeMethod.setAccessible(false);
			}
		} catch (final Exception e) {
			// We will tolerate an exception here and just log it
			throw new IllegalStateException(e);
		}
	}

	public <T> void seed(final Key<T> key, final T value) {
		final Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);
		checkState(!scopedObjects.containsKey(key), "A value for the key %s was " + "already seeded in this scope. Old value: %s New value: %s", key, scopedObjects.get(key), value);
		scopedObjects.put(key, value);
	}

	public <T> void seed(final Class<T> clazz, final T value) {
		seed(Key.get(clazz), value);
	}

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
		return new Provider<T>() {
			@Override
			public T get() {
				final Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);

				@SuppressWarnings("unchecked")
				T current = (T) scopedObjects.get(key);
				if (current == null && !scopedObjects.containsKey(key)) {
					current = unscoped.get();

					// // don't remember proxies; these exist only to serve circular dependencies
					// // TODO: Requires Guice 4.0
					// if (Scopes.isCircularProxy(current)) {
					// return current;
					// }

					scopedObjects.put(key, current);
				}
				return current;
			}
		};
	}

	private <T> Map<Key<?>, Object> getScopedObjectMap(final Key<T> key) {
		final Map<Key<?>, Object> scopedObjects = values.get();
		if (scopedObjects == null) {
			throw new OutOfScopeException("Cannot access " + key + " outside of a scoping block");
		}
		return scopedObjects;
	}

	/**
	 * Returns a provider that always throws exception complaining that the object in question must be seeded before it can be injected.
	 *
	 * @return typed provider
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> Provider<T> seededKeyProvider() {
		return (Provider<T>) SEEDED_KEY_PROVIDER;
	}
}