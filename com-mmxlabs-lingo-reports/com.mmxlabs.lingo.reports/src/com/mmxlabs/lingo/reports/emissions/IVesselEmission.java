/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

public interface IVesselEmission {
	//Emission rates
	double getBOGEmissionRate();
	double getPilotLightEmissionRate();
	double getMethaneSlipRate();
}
