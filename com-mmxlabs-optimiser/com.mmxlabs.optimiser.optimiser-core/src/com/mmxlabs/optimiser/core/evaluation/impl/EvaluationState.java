/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

public final class EvaluationState implements IEvaluationState {

	private final Map<String, Object> dataMap = new HashMap<>();

	@Override
	public void setData(@NonNull final String key, @NonNull final Object data) {
		dataMap.put(key, data);

	}

	@Override
	public <T> T getData(@NonNull final String key, final Class<T> cls) {
		return cls.cast(dataMap.get(key));
	}

}
