/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.transformer.shared.IPortAndDistanceData;
import com.mmxlabs.models.lng.transformer.shared.ISharedDataTransformerService;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;

public class SharedDataTransformerService implements ISharedDataTransformerService {

	private final ConcurrentHashMap<String, SoftReference<IPortAndDistanceData>> cache = new ConcurrentHashMap<>();

	@Override
	public IPortAndDistanceData getPortAndDistanceProvider(final PortModel portModel) {

		if (false) {

			// Caching code path - assumes long running service

			final String key = getKey(portModel);
			final SoftReference<IPortAndDistanceData> ref = cache.get(key);
			IPortAndDistanceData value = null;
			if (ref != null) {
				value = ref.get();
			}
			if (value == null) {
				value = createData(portModel);
				cache.put(key, new SoftReference<>(value));
			}
			return value;
		}
		return createData(portModel);
	}

	private @NonNull String getKey(final @NonNull PortModel portModel) {

		// return String.format("%s-%s", portModel.getPortDataVersion(), portModel.getDistanceDataVersion());

		return portModel.getUuid();
	}

	private @NonNull IPortAndDistanceData createData(final @NonNull PortModel portModel) {
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
	}

}
