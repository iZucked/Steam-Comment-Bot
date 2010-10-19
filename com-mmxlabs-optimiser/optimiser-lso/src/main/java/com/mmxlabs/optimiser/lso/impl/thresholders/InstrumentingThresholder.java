/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso.impl.thresholders;

import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;

public class InstrumentingThresholder implements IThresholder {
	final IThresholder delegate;
	private InstrumentingMoveGenerator<?> client;
	
	public InstrumentingThresholder(IThresholder delegate, InstrumentingMoveGenerator<?> client) {
		this.delegate = delegate;
		this.client = client;
	}
	
	@Override
	public void init() {
		delegate.init();
	}

	@Override
	public boolean accept(long delta) {
		final boolean answer = delegate.accept(delta);
		
		client.notifyOfThresholderDecision(delta, answer);
		
		return answer;
	}

	@Override
	public void step() {
		delegate.step();
	}	
}
