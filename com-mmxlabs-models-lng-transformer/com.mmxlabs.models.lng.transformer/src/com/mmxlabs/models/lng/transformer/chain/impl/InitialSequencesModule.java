package com.mmxlabs.models.lng.transformer.chain.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.ISequences;

public class InitialSequencesModule extends AbstractModule {

	@NonNull
	private final ISequences sequences;

	public InitialSequencesModule(@NonNull final ISequences sequences) {
		this.sequences = sequences;

	}

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	@Named("Initial")
	private ISequences provideSequences() {
		return sequences;
	}
}
