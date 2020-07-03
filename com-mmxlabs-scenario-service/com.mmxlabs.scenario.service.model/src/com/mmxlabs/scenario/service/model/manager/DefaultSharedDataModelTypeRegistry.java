/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.jdt.annotation.Nullable;

public class DefaultSharedDataModelTypeRegistry implements ISharedDataModelTypeRegistry {

	private final Map<String, ISharedDataModelType<?>> registry = new HashMap<>();
	private final Map<ISharedDataModelType<?>, Function<IScenarioDataProvider, Object>> makerRegistry = new HashMap<>();

	@Override
	public ISharedDataModelType<?> lookup(final String id) {
		return registry.get(id);
	}

	@Override
	public void register(final String id, final ISharedDataModelType<?> type, @Nullable final Function<IScenarioDataProvider, Object> makerFunction) {
		registry.put(id, type);
		makerRegistry.put(type,  makerFunction);
	}

	@Override
	public Function<IScenarioDataProvider, Object> getMakerFunction(final ISharedDataModelType<?> type) {
		return makerRegistry.get(type);
	}
}
