package com.mmxlabs.models.lng.commercial.parseutils;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.pricing.UnitConversion;

@NonNullByDefault
public class ConversionNode extends AbstractMarkedUpNode {
	private final String name;
	private final UnitConversion factor;

	public ConversionNode(final String name, final UnitConversion factor) {
		this.name = name;
		this.factor = factor;

	}

	public String getName() {
		return name;
	}

	public UnitConversion getFactor() {
		return factor;
	}
}