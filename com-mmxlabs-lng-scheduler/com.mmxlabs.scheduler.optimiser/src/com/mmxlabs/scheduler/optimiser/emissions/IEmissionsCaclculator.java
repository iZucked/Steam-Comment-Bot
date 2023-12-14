package com.mmxlabs.scheduler.optimiser.emissions;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public interface IEmissionsCaclculator {
	
	public long getEmissionsFromPortVisit(final PortDetails portDetails, final Collection<@NonNull FuelKey> travelFuelKeys, final Collection<@NonNull FuelKey> lngFuelKeys);
	public long getEmissionsFromVoyage(final VoyageDetails voyageDetails, final Collection<@NonNull FuelKey> travelFuelKeys, final Collection<@NonNull FuelKey> lngFuelKeys);
	public long getJourneyEmissions(VoyageDetails voyageDetails);
	public long getIdleEmissions(VoyageDetails voyageDetails);
	public long getPortVisitEmissions(PortDetails portDetails);
	
	public double getEmissionReductions(IPort startPort, IPort endPort, int time);
	public long getEmissionsCost(long totalEmissionsInTonnes, int time);
}
