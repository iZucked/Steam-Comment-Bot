/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public interface ISharedDataModelType<T> {

	static ISharedDataModelTypeRegistry REGISTRY = new DefaultSharedDataModelTypeRegistry();

	static <T> ISharedDataModelType<T> make(@NonNull final String id, @Nullable final Function<IScenarioDataProvider, Object> makerFunction) {
		final DefaultSharedDataModelType<T> type = new DefaultSharedDataModelType<T>(id);
		registry().register(id, type, makerFunction);
		return type;
	}

	static ISharedDataModelTypeRegistry registry() {
		return REGISTRY;
	}

	String getID();
}