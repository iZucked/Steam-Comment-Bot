package com.mmxlabs.scheduler.optimiser.emissions.impl;

import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.emissions.IEmissionsCaclculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public class EuEtsEmissionsExporterCalculator{

	private final IEmissionsCaclculator oldCalc;
	
	// Only passing the other version as I can't figure out how to inject this calculator just for export in AnnotatedSolutionExporter
	public EuEtsEmissionsExporterCalculator(IEmissionsCaclculator oldCalc) {
		this.oldCalc = oldCalc;
	}
	
	public long getJourneyEmissions(VoyageDetails voyageDetails) {
		long scaledEmissions = oldCalc.getJourneyEmissions(voyageDetails);
		return OptimiserUnitConvertor.convertToExternalVolume(scaledEmissions);
	}
	
	public long getIdleEmissions(VoyageDetails voyageDetails) {
		long scaledEmissions = oldCalc.getIdleEmissions(voyageDetails);
		return OptimiserUnitConvertor.convertToExternalVolume(scaledEmissions);
	}
	
	public long getPortVisitEmissions(PortDetails portDetails) {
		long scaledEmissions = oldCalc.getPortVisitEmissions(portDetails);
		return OptimiserUnitConvertor.convertToExternalVolume(scaledEmissions);
	}

	public long getEmissionsCost(long totalEmissionsInTonnes, int time) {
		long scaledEmissionsCost = oldCalc.getEmissionsCost(totalEmissionsInTonnes, time);
		return (long) OptimiserUnitConvertor.convertToExternalPrice(scaledEmissionsCost);
	}

	public double getEmissionReductions(IPort startPort, IPort endPort, int time) {
		return oldCalc.getEmissionReductions(startPort, endPort, time);
	}
}
