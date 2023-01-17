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

import org.eclipse.e4.ui.model.internal.ModelUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class SecondCargoEmissionAccountingReportJSONGenerator{
	
	public static List<SecondCargoEmissionAccountingReportModelV1> createReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final @NonNull ScheduleModel scheduleModel) {
		final List<SecondCargoEmissionAccountingReportModelV1> models = new LinkedList<>();

		if (scheduleModel.getSchedule() == null) {
			return models;
		}
		
		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			final SecondCargoEmissionAccountingReportModelV1 model = createCargoAllocationReportData(scenarioDataProvider, cargoAllocation);
			if (model != null) {
				models.add(model);
			}
		}

		return models;
	}
	
	public static SecondCargoEmissionAccountingReportModelV1 createCargoAllocationReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final CargoAllocation cargoAllocation) {
		final SecondCargoEmissionAccountingReportModelV1 model = new SecondCargoEmissionAccountingReportModelV1();
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
		model.shipping = 0.0;
		model.upstream = 0.0;
		model.totalEmission = 0.0;
		final LoadSlot loadSlot = ScheduleModelUtils.getLoadSlot(cargoAllocation);
		
       if(loadSlot != null) {
    	   final PurchaseContract purchaseContract=loadSlot.getContract();
    	   if(purchaseContract != null) {
    		   model.upstreamEmissionRate = purchaseContract.getUpstreamEmissionRate();
    		   model.upstream = ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred() * model.upstreamEmissionRate;
    		   model.totalEmission += model.upstream;
    	   }
       }
		final Vessel vessel = ScheduleModelUtils.getVessel(cargoAllocation.getSequence());
		
		model.eventID = cargoAllocation.getName();
		//TODO comment
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
			model.totalEmission += model.shipping;
		} 

// TODO comment
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
	
	private static void processUsage(final SecondCargoEmissionAccountingReportModelV1 model, List<FuelQuantity> fuelQuantity) {
		for (final FuelQuantity fq : fuelQuantity) {
			switch (fq.getFuel()) {
			case BASE_FUEL: 
				fq.getAmounts().forEach(fa -> model.shipping += fa.getQuantity() * model.baseFuelEmissionRate);
				break;
			case FBO, NBO:
				fq.getAmounts().forEach(fa -> model.shipping += fa.getQuantity() * model.bogEmissionRate);
				break;
			case PILOT_LIGHT:
				fq.getAmounts().forEach(fa -> model.shipping += fa.getQuantity() * model.pilotLightEmissionRate);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + fq.getFuel());
			}
		}
		
	}

	public static File jsonOutput(final List<SecondCargoEmissionAccountingReportModelV1> models) {
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
