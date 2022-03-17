/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.bunkerfuels;

public class BunkerFuelDefinition {

	private String name;
	private double equivalenceFactor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getEquivalenceFactor() {
		return equivalenceFactor;
	}

	public void setEquivalenceFactor(double equivalenceFactor) {
		this.equivalenceFactor = equivalenceFactor;
	}
}
