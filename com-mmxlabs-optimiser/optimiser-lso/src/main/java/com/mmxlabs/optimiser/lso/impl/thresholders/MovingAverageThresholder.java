/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso.impl.thresholders;

import java.util.Random;

import com.mmxlabs.optimiser.lso.IThresholder;

public class MovingAverageThresholder implements IThresholder {
	private long[] window;
	private double sum = 0;
	private int front = 0;
	private double logAlpha;
	final double alpha0;
	final double cooling;
	final private Random random;
	int tick = 0;
	int epoch = 0;
	private final int epochLength;
	private double lastT;
	
	public MovingAverageThresholder(Random random, double initialAcceptance, double cooling, int epochLength, int window) {
		this.random = random;
		this.window = new long[window];
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
	public boolean accept(long delta) {
		if (delta <= 0) return true;
		
		sum -= window[front];
		sum += delta;
		window[front] = delta;
		front = (front+1)%window.length;
		
		final double mean = sum/window.length;
		
		final double T = -mean/logAlpha;
		lastT = T;
		
		return random.nextDouble() < Math.exp(-delta/T);
	}

	@Override
	public void step() {
		if (tick == epochLength) {
			tick = 0;
			epoch++;
		} else {
			if (tick == 0) {
				logAlpha = Math.log(alpha0 * Math.pow(cooling, epoch));
				System.err.println("Epoch " + epoch + ", alpha = " + alpha0 *Math.pow(cooling, epoch) + " T=" + lastT);
			}
			tick++;
		}
	}
}
