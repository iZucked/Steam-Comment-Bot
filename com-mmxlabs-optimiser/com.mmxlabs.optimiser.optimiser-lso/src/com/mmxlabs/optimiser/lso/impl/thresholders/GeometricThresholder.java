/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.lso.IThresholder;

/**
 * An implementation of the classic SA thresholder. This has a "temperature", which is used to probabilistically accept/reject moves. A move which causes change delta is accepted with probability
 * exp(-delta/temperature). The temperature is reduced periodically, with the period being known as the "epoch length", by multiplying the current temperature by a cooling value which is less than 1.
 * Longer epochs and larger cooling values cause slower convergence, but increase the quality of the result (given enough steps).
 * 
 * @author hinton
 */
public class GeometricThresholder implements IThresholder {
	private static final Logger log = LoggerFactory.getLogger(GeometricThresholder.class);
	private final double fractionPerEpoch;
	private final int epochLength;
	private int ticks;
	private double temperature;
	private final double T0;
	private final Random random;

	/**
	 * Create a new geometric thresholder
	 * 
	 * @param random
	 *            the random number generator
	 * @param epochLength
	 *            the length of one epoch
	 * @param T0
	 *            the initial temperature
	 * @param alpha
	 *            the cooling parameter (<1)
	 */
	public GeometricThresholder(final Random random, final int epochLength, final double T0, final double alpha) {
		this.random = random;
		this.epochLength = epochLength;
		this.T0 = T0;
		this.fractionPerEpoch = alpha;
		if ((alpha >= 1) || (alpha <= 0)) {
			throw new IllegalArgumentException("Alpha must be strictly between 1 and 0. You provided " + alpha);
		}
	}

	public double getTemperature() {
		return temperature;
	}

	@Override
	public void init() {
		reset();
	}

	@Override
	public boolean accept(final long delta) {
		if (delta < 0) {
			return true;
		} else {
			return random.nextDouble() < acceptanceProbability(delta, temperature);
		}
	}

	protected final static double acceptanceProbability(final long delta, final double T) {
		return Math.exp(-delta / T);
	}

	@Override
	public void step() {
		ticks++;
		if (ticks >= epochLength) {
			ticks = 0;
			temperature *= fractionPerEpoch;
			log.debug("Next epoch, temperature = " + temperature);
//			System.out.println("Next epoch, temperature = " + temperature);

		}
	}

	@Override
	public void reset() {
		ticks = 0;
		temperature = T0;
	}

}
