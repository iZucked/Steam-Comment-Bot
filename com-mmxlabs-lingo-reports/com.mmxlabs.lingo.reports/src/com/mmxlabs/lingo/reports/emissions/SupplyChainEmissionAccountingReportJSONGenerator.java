/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.io.File;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class SupplyChainEmissionAccountingReportJSONGenerator{
	
	public static List<SupplyChainEmissionAccountingReportModelV1> createReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final @NonNull ScheduleModel scheduleModel) {
		final List<SupplyChainEmissionAccountingReportModelV1> models = new LinkedList<>();

		if (scheduleModel.getSchedule() == null) {
			return models;
		}
		
		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			final SupplyChainEmissionAccountingReportModelV1 model = createCargoAllocationReportData(scenarioDataProvider, cargoAllocation);
			if (model != null) {
				models.add(model);
			}
		}

		return models;
	}
	
	public static SupplyChainEmissionAccountingReportModelV1 createCargoAllocationReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final CargoAllocation cargoAllocation) {
		final SupplyChainEmissionAccountingReportModelV1 model = new SupplyChainEmissionAccountingReportModelV1();
		final EList<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();

		final Optional<SlotAllocation> loadOptional = slotAllocations //
				.stream() //
				.filter(s -> (s.getSlot() instanceof LoadSlot)) //
				.findFirst();

		if (!loadOptional.isPresent()) {
			return null;
		}

		model.equivalents.add(cargoAllocation);
		model.equivalents.addAll(cargoAllocation.getEvents());
		for (SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
			model.equivalents.add(sa);
			if (sa.getSlot() != null) {
				model.equivalents.add(sa.getSlot());
			}
			
		}
		model.liquefactionEmission = 0.0;
		model.pipelineEmission = 0.0;
		model.shippingEmission = 0.0;
		model.upstreamEmission = 0.0;
		model.totalEmission = 0.0;
		final LoadSlot loadSlot = ScheduleModelUtils.getLoadSlot(cargoAllocation);
		
       if(loadSlot != null) {
    	   Port port = loadSlot.getPort();
    	   final PurchaseContract purchaseContract=loadSlot.getContract();
    	   
    	   if(purchaseContract != null) {
    		   model.pipelineEmissionRate = purchaseContract.getPipelineEmissionRate();
    		   model.upstreamEmissionRate = purchaseContract.getUpstreamEmissionRate();
    		   int physicalEnergyTransferred = ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred();
    		   model.upstreamEmission = physicalEnergyTransferred * model.upstreamEmissionRate;
    		   model.pipelineEmission = physicalEnergyTransferred * model.pipelineEmissionRate;
    		   model.totalEmission += model.upstreamEmission;
    		   model.totalEmission += model.pipelineEmission;
    	   }
    	   
    	   if(port != null) {
    		   if(purchaseContract == null) {
    			   model.pipelineEmissionRate = port.getPipelineEmissionRate();
    			   model.upstreamEmissionRate = port.getUpstreamEmissionRate();
    			   int physicalEnergyTransferred = ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred();
        		   model.upstreamEmission = physicalEnergyTransferred * model.upstreamEmissionRate;
        		   model.pipelineEmission = physicalEnergyTransferred * model.pipelineEmissionRate;
        		   model.totalEmission += model.upstreamEmission;
        		   model.totalEmission += model.pipelineEmission;
    		   }
    		   model.liquefactionEmissionRate = port.getLiquefactionEmissionRate();
    		   model.liquefactionEmission = model.liquefactionEmissionRate * ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred();
    		   model.totalEmission += model.liquefactionEmission;
    	   }


       }
		final Vessel vessel = ScheduleModelUtils.getVessel(cargoAllocation.getSequence());
		
		model.eventID = cargoAllocation.getName();
		//if vesel is not null assign the emission rates and process the usage for each event
		if(vessel != null) {
			model.vesselName = vessel.getName();
			model.baseFuelEmissionRate = vessel.getBaseFuelEmissionRate();
			model.bogEmissionRate = vessel.getBogEmissionRate();
			model.pilotLightEmissionRate = vessel.getPilotLightEmissionRate();
			for (final Event e : cargoAllocation.getEvents()) {	
				if (e instanceof FuelUsage fu) {
					processUsage(model, fu.getFuels());
				}
			}
			model.totalEmission += model.shippingEmission;
		} 

        // getting the start and the end of the event
		LocalDateTime eventStart = null;
		
		for (final Event e : cargoAllocation.getEvents()) {
			if (eventStart == null) {
				eventStart = e.getStart().toLocalDateTime();
			}
			model.eventEnd = e.getEnd().toLocalDateTime();
			
		}
		model.eventStart = eventStart;
		
		return model;
	}
	
	private static void processUsage(final SupplyChainEmissionAccountingReportModelV1 model, List<FuelQuantity> fuelQuantity) {
		for (final FuelQuantity fq : fuelQuantity) {
			switch (fq.getFuel()) {
			case BASE_FUEL: 
				fq.getAmounts().forEach(fa -> model.shippingEmission += fa.getQuantity() * model.baseFuelEmissionRate);
				break;
			case FBO, NBO:
				fq.getAmounts().forEach(fa -> model.shippingEmission += fa.getQuantity() * model.bogEmissionRate);
				break;
			case PILOT_LIGHT:
				fq.getAmounts().forEach(fa -> model.shippingEmission += fa.getQuantity() * model.pilotLightEmissionRate);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + fq.getFuel());
			}
		}
		
	}

	public static File jsonOutput(final List<SupplyChainEmissionAccountingReportModelV1> models) {
		final ObjectMapper objectMapper = new ObjectMapper();
		final File file = new File("/temp/emissions.json");
		try {
			objectMapper.writeValue(file, models);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return file;
	}
}
