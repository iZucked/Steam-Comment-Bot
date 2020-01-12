/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models;

/**
 * Defines how much one unit of the from is worth in to
 * 
 * @author Simon Goodall
 *
 */
public class UnitConversionFactor {

	private String from;
	private String to;
	private double factor;

	public String getFrom() {
		return from;
	}

	public void setFrom(final String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(final String to) {
		this.to = to;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(final double factor) {
		this.factor = factor;
	}
}
