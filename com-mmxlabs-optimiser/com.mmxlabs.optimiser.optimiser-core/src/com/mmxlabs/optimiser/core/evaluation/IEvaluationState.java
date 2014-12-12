package com.mmxlabs.optimiser.core.evaluation;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IEvaluationState {

	void setData(@NonNull String key, @NonNull Object data);

	@Nullable
	<T> T getData(@NonNull String key, @NonNull Class<T> cls);

}
