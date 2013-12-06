/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import org.apache.commons.cli.Options;

/**
 * Simple class to pass into {@link Options#parseAndSet(String[], Object)}
 * 
 * @author Simon Goodall
 * 
 */

public final class SettingsOverride {
	private int iterations = 30000;
	private int seed = 1;

	private String scenario;
	private String output;

	public final String getScenario() {
		return scenario;
	}

	public final void setScenario(final String scenario) {
		this.scenario = scenario;
	}

	public final int getIterations() {
		return iterations;
	}

	public final void setIterations(final int iterations) {
		this.iterations = iterations;
	}

	public final int getSeed() {
		return seed;
	}

	public final void setSeed(final int seed) {
		this.seed = seed;
	}

	protected String getOutput() {
		return output;
	}

	protected void setOutput(String output) {
		this.output = output;
	}

}
