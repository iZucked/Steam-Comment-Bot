/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.events;

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class OptimisationLifecycleModule extends AbstractModule {
	private final EventBus eventBus = new EventBus("Optimisation Lifecycle EventBus");

	@Override
	protected void configure() {
		bind(EventBus.class).toInstance(eventBus);
		bind(OptimisationLifecycleManager.class).in(Singleton.class);

		bindListener(Matchers.any(), new TypeListener() {
			@Override
			public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
				typeEncounter.register(new InjectionListener<I>() {
					@Override
					public void afterInjection(final I i) {
						eventBus.register(i);
					}
				});
			}
		});
	}
}
