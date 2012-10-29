package com.mmxlabs.models.common.commandservice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract implementation of {@link IModelCommandProvider} tracking per-thread provision depths and custom data structures.
 * 
 * @since 2.0
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
		if (provisionStack.get().getAndIncrement() == 1) {
			provisionContext.set(null);
		}
	}

	@Override
	public void endCommandProvision() {
		if (provisionStack.get().decrementAndGet() == 1) {
			provisionContext.set(null);
		}
	}
}