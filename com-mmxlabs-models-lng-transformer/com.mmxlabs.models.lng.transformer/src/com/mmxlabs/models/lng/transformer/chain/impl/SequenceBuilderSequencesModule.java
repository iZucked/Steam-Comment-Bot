/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;

/**
 * A {@link Module} to bind a {@link ISequences} to the instance with the {@link OptimiserConstants#SEQUENCE_TYPE_INITIAL} name
 * 
 * @author Simon Goodall
 *
 */
public class SequenceBuilderSequencesModule extends AbstractModule {

	@NonNull
	private final ISequences sequences;

	public SequenceBuilderSequencesModule(@NonNull final ISequences sequences) {
		this.sequences = sequences;

	}

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	@Named(OptimiserConstants.SEQUENCE_TYPE_SEQUENCE_BUILDER)
	private ISequences provideSequences() {
		return sequences;
	}
}
