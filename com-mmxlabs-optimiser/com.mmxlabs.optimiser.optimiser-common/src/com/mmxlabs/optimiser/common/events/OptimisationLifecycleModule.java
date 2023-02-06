/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.events;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class OptimisationLifecycleModule extends AbstractModule {

	Map<Object, Object> lastMessage = new ConcurrentHashMap<>();

	private final EventBus eventBus = new EventBus("Optimisation Lifecycle EventBus") {

		public void post(Object event) {
			if (event instanceof IReplayableEvent) {
				lastMessage.put(event.getClass(), event);
			}
			super.post(event);
		};
	};

	private final EventBus eventBus2 = new EventBus("Replay event bus");

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
						// Register the main event bus
						eventBus.register(i);

						// Temporarily register the replay bus.
						if (!lastMessage.isEmpty()) {
							eventBus2.register(i);
							try {
								lastMessage.values().forEach(eventBus2::post);
							} finally {
								eventBus2.unregister(i);
							}
						}
					}
				});
			}
		});
	}
}
