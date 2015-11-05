package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;

public interface ILNGStateTransformerUnit {

	@NonNull
	Injector getInjector();

	@NonNull
	LNGDataTransformer getDataTransformer();

	@NonNull
	IMultiStateResult getInputState();

	@NonNull
	IMultiStateResult run(@NonNull IProgressMonitor monitor);
}
