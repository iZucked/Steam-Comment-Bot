/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures;

public class Constant implements IExposureNode {

	private final long constant;
	private final String newVolumeUnit;

	public Constant(final long constant, final String newVolumeUnit) {
		this.constant = constant;
		this.newVolumeUnit = newVolumeUnit;
	}

	public String getNewVolumeUnit() {
		return newVolumeUnit;
	}

	public long getConstant() {
		return constant;
	}

}
