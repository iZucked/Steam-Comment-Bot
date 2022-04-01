/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.model;

import java.util.OptionalDouble;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
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
