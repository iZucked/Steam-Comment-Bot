package com.mmxlabs.models.lng.transformer.inject.modules;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.ISequences;

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
	@Named("Input")
	private ISequences provideSequences() {
		return inputSequences;
	}
}
