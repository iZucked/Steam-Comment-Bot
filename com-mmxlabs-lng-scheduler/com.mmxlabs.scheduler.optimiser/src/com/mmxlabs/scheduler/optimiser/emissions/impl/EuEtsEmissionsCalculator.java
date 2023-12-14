package com.mmxlabs.scheduler.optimiser.emissions.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.ToLongFunction;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.emissions.IEmissionsCaclculator;
import com.mmxlabs.scheduler.optimiser.providers.IEuEtsProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

public class EuEtsEmissionsCalculator implements IEmissionsCaclculator {

	// Constants for unit conversions
	public static final double LNG_DENSITY_TON_PER_M3 = 0.450;

	@Inject
	public IEuEtsProvider euEtsProvider;
	
	@Override
	public long getEmissionsFromPortVisit(final PortDetails portDetails, final Collection<@NonNull FuelKey> travelFuelKeys, final Collection<@NonNull FuelKey> lngFuelKeys) {
		long totalEmissions= 0;
		List<PortType> allowedPortTypes = List.of(PortType.Load, PortType.Discharge);
		
		if (allowedPortTypes.contains(portDetails.getOptions().getPortSlot().getPortType())){
			for(FuelKey fuel : travelFuelKeys) {
				totalEmissions += getFuelEmissionsInTonnes(fuel, portDetails::getFuelConsumption);
			}
			
			for(FuelKey fuel : lngFuelKeys) {
				totalEmissions += getFuelEmissionsInTonnes(fuel, f -> Calculator.multiply(portDetails.getFuelConsumption(f), LNG_DENSITY_TON_PER_M3));
			}
		}
		return totalEmissions;
	}

	@Override
	public long getEmissionsFromVoyage(final VoyageDetails voyageDetails, final Collection<@NonNull FuelKey> travelFuelKeys, final Collection<@NonNull FuelKey> lngFuelKeys) {
		long totalEmissions = 0;
		
		for(FuelKey fuel : travelFuelKeys) {
			totalEmissions += getFuelEmissionsInTonnes(fuel, voyageDetails::getFuelConsumption);
			totalEmissions += getFuelEmissionsInTonnes(fuel, voyageDetails::getRouteAdditionalConsumption);
		}
		
		for(FuelKey fuel : lngFuelKeys) {
			totalEmissions += getFuelEmissionsInTonnes(fuel, f -> Calculator.multiply(voyageDetails.getFuelConsumption(f), LNG_DENSITY_TON_PER_M3));
			totalEmissions += getFuelEmissionsInTonnes(fuel, f -> Calculator.multiply(voyageDetails.getRouteAdditionalConsumption(f), LNG_DENSITY_TON_PER_M3));
		}
		return totalEmissions;
	}

	@Override
	public double getEmissionReductions(IPort startPort, IPort endPort, int time) {
		double reductionFactor = 1.0;
		
		// If only one port in EU
		if((euEtsProvider.getEuPorts().contains(startPort) ^ euEtsProvider.getEuPorts().contains(endPort))) {
			// Check if only one port selected
			if(startPort == null ^ endPort == null) {
				reductionFactor *= 1.0;
			}
			else {
				reductionFactor *= 0.5;
			}
		}
		// If neither in EU
		else if(!euEtsProvider.getEuPorts().contains(startPort) && !euEtsProvider.getEuPorts().contains(endPort)) {
			reductionFactor *= 0;
		}
		// Both in EU
		else {
			reductionFactor *= 1.0;
		}
		
		// Apply reduction from ramp up table
		reductionFactor *= (euEtsProvider.getSeasonalityCurve().getEmissionsCoveredForYear(time) / 100.0);
		
		return reductionFactor;
	}

	@Override
	public long getEmissionsCost(long totalEmissionsInTonnes, int time) {
		double euaPrice = euEtsProvider.getPriceCurve().getValueAtPoint(time, Collections.emptyMap());
		long emissionsCost = Calculator.multiply(totalEmissionsInTonnes, euaPrice);
		
		return emissionsCost;
	}
	
	protected long getFuelEmissionsInTonnes(@NonNull final FuelKey fuel, ToLongFunction<@NonNull FuelKey> getFuelConsumption) {
		// Get fuel quantity
		final long fuelQuantityInMT = getFuelConsumption.applyAsLong(fuel);
		
		if(fuelQuantityInMT == 0)
			return 0;
		
		// Get fuel emissions
		final String fuelName = fuel.getBaseFuel().getName();
		final double emissionRateInTonnePerTonne = euEtsProvider.getFuelEmissionRates().getOrDefault(fuelName, 0.0);
		
		final long emissionsInTonnes = Calculator.multiply(fuelQuantityInMT, emissionRateInTonnePerTonne);
		
		return emissionsInTonnes;
	}

	@Override
	public long getJourneyEmissions(VoyageDetails voyageDetails) {
		IVessel currVessel = voyageDetails.getOptions().getVessel();
		List<FuelKey> lngFuelKeys = new ArrayList<>(Arrays.asList(LNGFuelKeys.LNG_In_m3));
		lngFuelKeys.remove(LNGFuelKeys.IdleNBO_In_m3);
		return getEmissionsFromVoyage(voyageDetails, currVessel.getTravelFuelKeys(), lngFuelKeys);
	}

	@Override
	public long getIdleEmissions(VoyageDetails voyageDetails) {
		IVessel currVessel = voyageDetails.getOptions().getVessel();
		return getEmissionsFromVoyage(voyageDetails, currVessel.getIdleFuelKeys(), List.of(LNGFuelKeys.IdleNBO_In_m3));
	}

	@Override
	public long getPortVisitEmissions(PortDetails portDetails) {
		IVessel currVessel = portDetails.getOptions().getVessel();
		
		if(currVessel == null) {
			return 0;
		}

		List<FuelKey> lngFuelKeys = new ArrayList<>(Arrays.asList(LNGFuelKeys.LNG_In_m3));
		lngFuelKeys.remove(LNGFuelKeys.IdleNBO_In_m3);
		
		return getEmissionsFromPortVisit(portDetails, currVessel.getPortFuelKeys(), lngFuelKeys);
	}

}
