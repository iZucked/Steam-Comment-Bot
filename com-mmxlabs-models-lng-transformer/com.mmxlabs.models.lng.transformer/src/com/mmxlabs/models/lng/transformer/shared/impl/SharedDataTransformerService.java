/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.shared.impl;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.shared.IPortAndDistanceData;
import com.mmxlabs.models.lng.transformer.shared.ISharedDataTransformerService;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;

public class SharedDataTransformerService implements ISharedDataTransformerService {

	// Using static to make it easier to re-use the cache. We should really make this class a OSGi service (or similar)
	private static final ConcurrentHashMap<String, SoftReference<IPortAndDistanceData>> cache = new ConcurrentHashMap<>();

	@Override
	public IPortAndDistanceData getPortAndDistanceProvider(@NonNull IScenarioDataProvider dataProvider) {
		final PortModel portModel = ScenarioModelUtil.getPortModel(dataProvider);

		ModelDistanceProvider modelDistanceProvder = dataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
		if (true) {
			String distanceModelVersion = modelDistanceProvder.getVersion();

			if (distanceModelVersion == null) {
				// Cannot create valid key, so avoid cache.
				return createData(portModel, modelDistanceProvder);
			}
			final String cacheKey = distanceModelVersion;

			// Caching code path - assumes long running service
			final SoftReference<IPortAndDistanceData> ref = cache.get(cacheKey);
			IPortAndDistanceData value = null;
			if (ref != null) {
				value = ref.get();
				// System.out.println("Hit for " + cacheKey);
			}
			if (value == null) {
				value = createData(portModel, modelDistanceProvder);
				cache.put(cacheKey, new SoftReference<>(value));
				// System.out.println("Miss for " + cacheKey);
			}
			return value;
		}
		return createData(portModel, modelDistanceProvder);
	}

	private @NonNull IPortAndDistanceData createData(final @NonNull PortModel portModel, final @NonNull ModelDistanceProvider modelDistanceProvider) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				install(new SharedDataModule());

				bind(PortModel.class).toInstance(portModel);

				bind(LNGSharedDataTransformer.class);
			}
		});

		final LNGSharedDataTransformer transformer = injector.getInstance(LNGSharedDataTransformer.class);

		transformer.transform(portModel, modelDistanceProvider);

		return injector.getInstance(PortAndDistanceData.class);
	}

}
