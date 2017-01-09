/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

public class ExposureValue {

	private double multipler;

	public ExposureValue(double multipler) {
		this.multipler = multipler;

	}

	public double getMultiplier() {
		return multipler;
	}
	// getUnits();

	// getCurrency();

	public double getExposure(double volume) {
		return getMultiplier() * volume;
	}
}
