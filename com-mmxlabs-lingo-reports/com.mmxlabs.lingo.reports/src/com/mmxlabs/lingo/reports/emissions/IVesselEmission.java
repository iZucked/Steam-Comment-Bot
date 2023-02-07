package com.mmxlabs.lingo.reports.emissions;

public interface IVesselEmission {
	double getBaseFuelEmissionRate();
	double getBOGEmissionRate();
	double getPilotLightEmissionRate();
}
