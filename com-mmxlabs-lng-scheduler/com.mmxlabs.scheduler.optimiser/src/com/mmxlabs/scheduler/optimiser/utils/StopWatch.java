package com.mmxlabs.scheduler.optimiser.utils;

/**
 * Simple class for timing code.
 */
public class StopWatch {

	private long prevTimeInNanos = 0;
	
	public void start() {
		prevTimeInNanos = System.nanoTime();
	}
	
	public void print(String opName) {
		long currTimeInNanos = System.nanoTime();
		if (prevTimeInNanos == 0) prevTimeInNanos = currTimeInNanos;
		double timeTaken = ((currTimeInNanos - prevTimeInNanos)/1000.0);
		if (timeTaken > 1_000_000) {
			System.out.println(opName + timeTaken / 1_000_000.0 + " seconds elapsed.");			
		}
		else if (timeTaken > 1000) {
			System.out.println(opName + timeTaken / 1000.0 + " milliseconds elapsed.");			
		}
		else {
			System.out.println(opName + timeTaken + " microseconds elapsed.");
		}
		prevTimeInNanos = currTimeInNanos;
	}
}
