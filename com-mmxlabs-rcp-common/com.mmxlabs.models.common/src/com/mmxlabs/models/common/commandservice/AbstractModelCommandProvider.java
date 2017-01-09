/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.common.commandservice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract implementation of {@link IModelCommandProvider} tracking per-thread provision depths and custom data structures.
 * 
 */
public abstract class AbstractModelCommandProvider<T> implements IModelCommandProvider {

	private final ThreadLocal<AtomicInteger> provisionStack = new ThreadLocal<AtomicInteger>();
	private final ThreadLocal<T> provisionContext = new ThreadLocal<T>();

	public AbstractModelCommandProvider() {
		super();
	}

	protected void setContext(final T context) {
		provisionContext.set(context);
	}

	protected T getContext() {
		return provisionContext.get();
	}

	protected int getProvisionDepth() {
		return provisionStack.get().get();
	}

	@Override
	public void startCommandProvision() {
		if (provisionStack.get() == null) {
			provisionStack.set(new AtomicInteger(0));
		}
		int andIncrement = provisionStack.get().getAndIncrement();
		if (andIncrement == 0) {
			provisionContext.set((T) null);
		}
	}

	@Override
	public void endCommandProvision() {
		if (provisionStack.get().decrementAndGet() == 0) {
			provisionContext.set((T) null);
		}
	}

}