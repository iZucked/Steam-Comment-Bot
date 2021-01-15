/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation;

import org.eclipse.jdt.annotation.NonNull;

public interface IEvaluationState {

	void setData(@NonNull String key, @NonNull Object data);

	<T> T getData(@NonNull String key, Class<T> cls);

}
