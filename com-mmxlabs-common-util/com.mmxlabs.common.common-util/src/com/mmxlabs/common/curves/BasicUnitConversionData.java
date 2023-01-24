/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * Equivalent to EMF UnitConversion of Pricing Model
 * @author FM
 *
 */
public class BasicUnitConversionData {
	private String from;
	private String to;
	private double factor;
	
	public BasicUnitConversionData(final String from, final String to, final double factor) {
		this.from = from;
		this.to = to;
		this.factor = factor;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public double getFactor() {
		return factor;
	}
}
