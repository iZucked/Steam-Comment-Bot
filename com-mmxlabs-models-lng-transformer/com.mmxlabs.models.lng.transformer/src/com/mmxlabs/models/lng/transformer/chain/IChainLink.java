package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

public interface IChainLink {

	int getProgressTicks();

	void init(@NonNull IMultiStateResult inputState);

	@NonNull
	IMultiStateResult getInputState();

	@NonNull
	IMultiStateResult run(@NonNull IProgressMonitor monitor);

}
