package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;

public interface IChainRunner {

	@NonNull
	LNGDataTransformer getDataTransformer();

	@NonNull
	IMultiStateResult getInitialState();

	@NonNull
	IMultiStateResult run(@NonNull final IProgressMonitor monitor);
}