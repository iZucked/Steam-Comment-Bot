package com.mmxlabs.models.lng.commercial.parseutils;

public class CoEff {
	private final double coeff;
	private final boolean exposed;

	public CoEff(final double coeff, final boolean exposed) {
		this.coeff = coeff;
		this.exposed = exposed;
	}

	public double getCoeff() {
		return coeff;
	}

	public boolean isExposed() {
		return exposed;
	}

}
