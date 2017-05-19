package com.mmxlabs.models.lng.transformer.shared.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.transformer.shared.IPortAndDistanceData;
import com.mmxlabs.models.lng.transformer.shared.ISharedDataTransformerService;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;

public class SharedDataTransformerService implements ISharedDataTransformerService {

	private final ConcurrentHashMap<String, IPortAndDistanceData> cache = new ConcurrentHashMap<>();

	@Override
	public IPortAndDistanceData getPortAndDistanceProvider(final PortModel portModel) {

		// return cache.computeIfAbsent(portModel.getUuid(), (k) -> {

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				install(new SharedDataModule());

				bind(PortModel.class).toInstance(portModel);

				bind(LNGSharedDataTransformer.class);
			}
		});

		final LNGSharedDataTransformer transformer = injector.getInstance(LNGSharedDataTransformer.class);

		transformer.transform(portModel);

		return injector.getInstance(PortAndDistanceData.class);
		// });
	}

}
