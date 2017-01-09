/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;

public class InstrumentingThresholder implements IThresholder {
	final IThresholder delegate;
	private final InstrumentingMoveGenerator client;

	public InstrumentingThresholder(final IThresholder delegate, final InstrumentingMoveGenerator client) {
		this.delegate = delegate;
		this.client = client;
	}

	@Override
	public void init() {
		delegate.init();
	}

	@Override
	public boolean accept(final long delta) {
		final boolean answer = delegate.accept(delta);

		client.notifyOfThresholderDecision(delta, answer);

		return answer;
	}

	@Override
	public void step() {
		delegate.step();
	}

	@Override
	public void reset() {
		delegate.reset();
	}
}
