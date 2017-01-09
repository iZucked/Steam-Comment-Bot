/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;

public class InputSequencesModule extends AbstractModule {

	@NonNull
	private final ISequences inputSequences;

	public InputSequencesModule(@NonNull final ISequences inputSequences) {
		this.inputSequences = inputSequences;

	}

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	@Named(OptimiserConstants.SEQUENCE_TYPE_INPUT)
	private ISequences provideSequences() {
		return inputSequences;
	}
}
