package com.mmxlabs.models.lng.transformer.extensions.euets;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.EmissionsUtil;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PricingEventHelper;
import com.mmxlabs.scheduler.optimiser.providers.IEuEtsProvider;

public class EuEtsExporterExtension implements IExporterExtension {
	
	@Inject
	private IEuEtsProvider euEtsProvider;

	@Nullable
	private Schedule output;

	@Nullable
	private ModelEntityMap modelEntityMap;

	@Nullable
	private IAnnotatedSolution annotatedSolution;

	@Inject
	private PricingEventHelper pricingEventHelper;
	
	@Inject
	private DateAndCurveHelper curveHelper;
	
	@Override
	public void startExporting(@NonNull Schedule output, @NonNull ModelEntityMap modelEntityMap, @NonNull IAnnotatedSolution annotatedSolution) {
		this.output = output;
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
	}

	@Override
	public void finishExporting() {
		if (output == null //
				|| modelEntityMap == null //
				|| annotatedSolution == null) {
			throw new IllegalStateException("Exporter not initialised correctly");
		}
		
		final List<Event> events = output.getSequences().stream().flatMap(s -> s.getEvents().stream()).collect(Collectors.toList());
		
		for(Event e : events) {
			double emissions = 0;
			double emissionsFactor = 1.0;

			// Check if event is Port visit, idle, dry-dock or maintenance and not in EU port 
			if(!isPortInPortGroup(e.getPort())) {
				emissionsFactor *= 0;
			}
			
			// Check if event is journey
			if(e instanceof Journey journeyEvent) {
				// From EU port -> EU port?
				if(isPortInPortGroup(journeyEvent.getPort()) && isPortInPortGroup(journeyEvent.getDestination())) {
					emissionsFactor *= 1.0;
				}
				// From EU port -> non EU port
				else if(isPortInPortGroup(journeyEvent.getPort()) && !isPortInPortGroup(journeyEvent.getDestination())) {
					emissionsFactor *= 0.5;
				}
				// From non EU port -> non EU port
				else if(!isPortInPortGroup(journeyEvent.getPort()) && !isPortInPortGroup(journeyEvent.getDestination())){
					emissionsFactor *= 0;
				}
			}
			
			// Event at EU port?
			if(isPortInPortGroup(e.getPort())) {
				emissionsFactor *= 1.0;
			}
			
			// Check event year against ramp up table
			emissionsFactor *= euEtsProvider.getSeasonalityCurve().getEmissionsCoveredForYear(e.getStart().getYear());
			
			// Calculate emissions
			if(e instanceof FuelUsage fuelUsageEvent) {
				emissions = getEmissionsFromFuelUsage(fuelUsageEvent);
			}
			
			
			// Calculate cost
			int costPerTonOfEmissions = euEtsProvider.getPriceCurve().getValueAtPoint(curveHelper.convertTime(e.getStart()), Collections.emptyMap());
			
			// Add cost to event
			
		}
	}
	
	private double getEmissionsFromFuelUsage(FuelUsage fuelUsageEvent) {
		double totalEmissions = 0;
		for(final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {
			final Fuel fuel = fuelQuantity.getFuel();
			final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);
			
			final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
			double emissionsRate = 0;
			if(baseFuel != null && baseFuel.getEmissionReference() != null) {
				emissionsRate = baseFuel.getEmissionReference().getCf();
			}
			
			// Calculate emissions rate for fuel used
			switch(fuel) {
			case BASE_FUEL, PILOT_LIGHT:
				// Base Fuel is set to Pilot Light Base Fuel when fuel is Pilot Light
				totalEmissions += fuelAmount.getQuantity() * emissionsRate;
				break;
			case FBO, NBO:
				// Need to define this function for optimiser
				final long quantity = EmissionsUtil.consumedQuantityLNG(fuelQuantity);
				emissionsRate = 0;
				totalEmissions +=  quantity * emissionsRate;
				break;
			default:
			}
		}
		
		return totalEmissions;
	}

	private boolean isPortInPortGroup(Port port) {
		return euEtsProvider.getEuPorts().stream().anyMatch(p -> p.getName().equals(port.getName()));
	}
}
