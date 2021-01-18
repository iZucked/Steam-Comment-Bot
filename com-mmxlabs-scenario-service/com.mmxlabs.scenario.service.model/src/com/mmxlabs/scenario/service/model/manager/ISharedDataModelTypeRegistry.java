/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.function.Function;

import org.eclipse.jdt.annotation.Nullable;

public interface ISharedDataModelTypeRegistry {

	ISharedDataModelType<?> lookup(String key);

	void register(String id, ISharedDataModelType<?> type, @Nullable Function<IScenarioDataProvider, Object> makerFunction);

	Function<IScenarioDataProvider, Object> getMakerFunction(ISharedDataModelType<?> type);
}