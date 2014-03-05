package com.mmxlabs.optimiser.lso.modules;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;

public class MoveGeneratorModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	private InstrumentingMoveGenerator provideInstrumentingMoveGenerator(final IMoveGenerator moveGenerator) {

		final InstrumentingMoveGenerator instrumentingMoveGenerator = LocalSearchOptimiserModule.instrumenting ? new InstrumentingMoveGenerator(moveGenerator, true // profile moves (true) or just rate
				// (false)
				, false // don't log moves to file
		)
				: null;
		return instrumentingMoveGenerator;

	}

}
