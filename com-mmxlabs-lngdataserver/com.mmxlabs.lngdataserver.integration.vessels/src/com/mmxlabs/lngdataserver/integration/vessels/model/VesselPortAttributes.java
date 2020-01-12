/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.model;

import java.util.OptionalDouble;

public class VesselPortAttributes {

	private OptionalDouble inPortBaseRate = OptionalDouble.empty();
	private OptionalDouble inPortNBORate = OptionalDouble.empty();

	public OptionalDouble getInPortBaseRate() {
		return inPortBaseRate;
	}

	public void setInPortBaseRate(OptionalDouble inPortBaseRate) {
		this.inPortBaseRate = inPortBaseRate;
	}

	public OptionalDouble getInPortNBORate() {
		return inPortNBORate;
	}

	public void setInPortNBORate(OptionalDouble inPortNBORate) {
		this.inPortNBORate = inPortNBORate;
	}

}
