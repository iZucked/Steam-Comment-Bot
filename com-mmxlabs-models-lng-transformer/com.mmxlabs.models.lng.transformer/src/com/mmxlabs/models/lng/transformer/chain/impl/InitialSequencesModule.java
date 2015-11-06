package com.mmxlabs.models.lng.transformer.chain.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * A {@link Module} to bind a {@link ISequences} to the instance with the "Initial" name
 * 
 * @author Simon Goodall
 *
 */
public class InitialSequencesModule extends AbstractModule {

	@NonNull
	private final ISequences sequences;

	public InitialSequencesModule(@NonNull final ISequences sequences) {
		this.sequences = sequences;

	}

	@Override
	protected void configure() {

	}

	// TODO: Declare this as a constant somewhere
	@Provides
	@Singleton
	@Named("Initial")
	private ISequences provideSequences() {
		return sequences;
	}
}
