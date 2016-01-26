/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.lso.IThresholder;

/**
 * A version of the {@link GeometricThresholder} which uses the first epoch to set the initial temperature in order to achieve a certain acceptance rate.
 * 
 * @author hinton
 * 
 */
public class CalibratingGeometricThresholder implements IThresholder {

	private static final Logger log = LoggerFactory.getLogger(CalibratingGeometricThresholder.class);

	private static final double ACCEPTABLE_ERROR = 0.01;
	private final double initialAcceptanceRate;
	private final int epochLength;
	private final Random random;
	private GeometricThresholder delegate;
	private boolean calibrated;
	private long[] sample;
	private int samples;
	private final double alpha;
	private double calibratedTemperature;
	private final double logInitialAcceptance;
	private double sumSamples;

	public CalibratingGeometricThresholder(final Random random, final int epochLength, final double initialAcceptanceRate, final double alpha) {
		this.random = random;
		this.epochLength = epochLength;
		this.initialAcceptanceRate = initialAcceptanceRate;
		this.alpha = alpha;
		this.logInitialAcceptance = Math.log(this.initialAcceptanceRate);
	}

	@Override
	public void init() {
		// zero some stuff
		this.delegate = null;
		this.calibrated = false;
		sample = new long[epochLength];
		samples = 0;
		sumSamples = 0;
	}

	@Override
	public boolean accept(final long delta) {
		if (calibrated) {
			return delegate.accept(delta);
		} else {
			// add this to the calibration infos.
			sample[samples] = Math.abs(delta);
			sumSamples += sample[samples];
			samples++;
			if (samples >= epochLength) {
				calibrate();
			}
			return delta < 0 /*
							 * || random.nextDouble() < Math.exp(-delta / (-(sumSamples/samples)/logInitialAcceptance))
							 */;
		}
	}

	/**
	 * Calculate an initial acceptance rate using a rubbish iterative procedure and then instantiate the delegate accordingly.
	 */
	private void calibrate() {
		// we have some samples, compute an initial temperature to achieve the given IAR
		double T0 = 1;
		while (!calibrated) {
			final double ar = evaluate(T0);
			if (Math.abs(ar - initialAcceptanceRate) < ACCEPTABLE_ERROR) {
				calibrated = true;
			} else {
				if (ar < initialAcceptanceRate) {
					// increase temperature
					T0 *= 2;
				} else {
					// decrease temperature
					T0 /= 1.5;
				}
			}
		}
		sample = null;
		calibratedTemperature = T0;
		delegate = new GeometricThresholder(random, epochLength, T0, alpha);
		delegate.init();
		log.debug("Calibrated temperature: " + T0);
	}

	private double evaluate(final double temperature) {
		double result = 0;
		int realSamples = 0;
		for (final long l : sample) {
			if (l > 0) {
				realSamples++;
				result += GeometricThresholder.acceptanceProbability(l, temperature);
			}
		}
		return result / realSamples;
	}

	@Override
	public void step() {
		if (calibrated) {
			delegate.step();
		}
	}

	public boolean isCalibrated() {
		return calibrated;
	}

	public double getCalibratedTemperature() {
		return calibratedTemperature;
	}

	@Override
	public void reset() {
		assert false : "Not implemented";
	}

}
