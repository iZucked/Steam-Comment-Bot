/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.BitSet;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.lso.IThresholder;

public class MovingAverageThresholder implements IThresholder {

	private static final Logger log = LoggerFactory.getLogger(MovingAverageThresholder.class);

	@NonNull
	private final Random random;

	private final BitSet accepts;
	private final long[] window;
	private double sum = 0;
	private int acceptCount = 0;
	private int front = 0;
	private double logAlpha;
	final double alpha0;
	final double cooling;
	int tick = 0;
	int epoch = 0;
	private final int epochLength;
	private double lastT;

	public MovingAverageThresholder(@NonNull final Random random, final double initialAcceptance, final double cooling, final int epochLength, final int window) {
		this.random = random;
		this.window = new long[window];
		accepts = new BitSet(window);
		this.alpha0 = initialAcceptance;
		this.cooling = cooling;
		this.epochLength = epochLength;
	}

	@Override
	public void init() {
		tick = 0;
		epoch = 0;
	}

	@Override
	public boolean accept(final long delta) {
		if (delta < 0) {
			return true;
		}

		sum -= window[front];
		sum += delta;
		window[front] = delta;

		final double mean = sum / window.length;

		final double T = -mean / logAlpha;
		lastT = T;

		final boolean accepted = random.nextDouble() < Math.exp(-delta / T);
		accepts.set(front, accepted);
		if (accepted) {
			acceptCount++;
		}
		front = (front + 1) % window.length;
		if (accepts.get(front)) {
			acceptCount--;
		}
		return random.nextDouble() < Math.exp(-delta / T);

	}

	@Override
	public void step() {
		if (tick == epochLength) {
			tick = 0;
			epoch++;
		} else {
			if (tick == 0) {
				logAlpha = Math.log(alpha0 * Math.pow(cooling, epoch));
				log.debug("Epoch " + epoch + ", alpha = " + (alpha0 * Math.pow(cooling, epoch)) + " T=" + lastT);
			}
			tick++;
		}
	}
	
	@Override
	public void reset() {
		assert false : "Not implemented";
	}

}
