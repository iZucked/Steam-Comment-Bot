/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

public interface IVesselEmission {
	//Emission rates
	double getBaseFuelEmissionRate();
	double getBOGEmissionRate();
	double getPilotLightEmissionRate();
}
