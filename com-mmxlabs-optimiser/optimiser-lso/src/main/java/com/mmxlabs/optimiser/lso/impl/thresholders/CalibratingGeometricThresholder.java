package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import com.mmxlabs.optimiser.lso.IThresholder;

/**
 * A version of the {@link GeometricThresholder} which uses the first epoch to set the initial temperature
 * in order to achieve a certain acceptance rate.
 * @author hinton
 *
 */
public class CalibratingGeometricThresholder implements IThresholder {
	private static final double ACCEPTABLE_ERROR = 0.01;
	private double initialAcceptanceRate;
	private int epochLength;
	private Random random;
	private GeometricThresholder delegate;
	private boolean calibrated;
	private long[] sample;
	private int samples;
	private double alpha;
	private double calibratedTemperature;

	public CalibratingGeometricThresholder(Random random, int epochLength, double initialAcceptanceRate, double alpha) {
		this.random = random;
		this.epochLength = epochLength;
		this.initialAcceptanceRate = initialAcceptanceRate;
		this.alpha = alpha;
	}
	
	@Override
	public void init() {
		//zero some stuff
		this.delegate = null;
		this.calibrated = false;
		sample = new long[epochLength];
		samples = 0;
	}

	@Override
	public boolean accept(long delta) {
		if (calibrated) {
			return delegate.accept(delta);
		} else {
			//add this to the calibration infos.
			sample[samples] = Math.abs(delta);
			samples++;
			if (samples >= epochLength) {
				calibrate();
			}
			return delta < 0;
		}
	}

	/**
	 * Calculate an initial acceptance rate using a rubbish iterative procedure and then instantiate
	 * the delegate accordingly.
	 */
	private void calibrate() {
		// we have some samples, compute an initial temperature to achieve the given IAR
		double T0 = 1;
		while (!calibrated) {
			double ar = evaluate(T0);
			if (Math.abs(ar - initialAcceptanceRate) < ACCEPTABLE_ERROR) {
				calibrated = true;
			} else {
				if (ar < initialAcceptanceRate) {
					//increase temperature
					T0 *= 2;
				} else {
					//decrease temperature
					T0 /= 1.5;
				}
			}
		}
		sample = null;
		calibratedTemperature = T0;
		delegate = new GeometricThresholder(random, epochLength, T0, alpha);
		delegate.init();
		System.err.println("Calibrated temperature: " + T0);
	}
	
	private double evaluate(final double temperature) {
		double result = 0;
		int realSamples = 0;
		for (long l : sample) {
			if (l > 0) {
				realSamples ++;
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

}
