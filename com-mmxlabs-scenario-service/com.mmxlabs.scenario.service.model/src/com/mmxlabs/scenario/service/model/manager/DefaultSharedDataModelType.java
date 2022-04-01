/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.jdt.annotation.NonNull;

public class DefaultSharedDataModelType<T> implements ISharedDataModelType<T> {

	private final String id;

	public DefaultSharedDataModelType(@NonNull final String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}
}
