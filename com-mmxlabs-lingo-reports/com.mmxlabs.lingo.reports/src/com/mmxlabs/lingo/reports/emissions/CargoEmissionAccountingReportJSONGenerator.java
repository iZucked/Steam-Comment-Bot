/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class CargoEmissionAccountingReportJSONGenerator{
	
	public static List<CargoEmissionAccountingReportModelV1> createReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final @NonNull ScheduleModel scheduleModel) {
		final List<CargoEmissionAccountingReportModelV1> models = new LinkedList<>();

		if (scheduleModel.getSchedule() == null) {
			return models;
		}
		
		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			final CargoEmissionAccountingReportModelV1 model = createCargoAllocationReportData(scenarioDataProvider, cargoAllocation);
			if (model != null) {
				models.add(model);
			}
		}

		return models;
	}
	
	public static CargoEmissionAccountingReportModelV1 createCargoAllocationReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final CargoAllocation cargoAllocation) {
		final CargoEmissionAccountingReportModelV1 model = new CargoEmissionAccountingReportModelV1();
		
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
		
		final Vessel vessel = ScheduleModelUtils.getVessel(cargoAllocation.getSequence());
		if (vessel == null) 
			return null;
		
		model.eventID = cargoAllocation.getName();
		model.vesselName = vessel.getName();
		model.baseFuelEmissionRate = EmissionsUtils.getBaseFuelEmissionRate(vessel);
		model.bogEmissionRate = EmissionsUtils.getBOGEmissionRate(vessel);
		model.pilotLightEmissionRate = EmissionsUtils.getPilotLightEmissionRate(vessel);
		model.baseFuelEmission = 0L;
		model.bogEmission = 0L;
		model.pilotLightEmission = 0L;
		model.totalEmission = 0L;
		LocalDateTime eventStart = null;
		
		for (final Event e : cargoAllocation.getEvents()) {
			if (eventStart == null) {
				eventStart = e.getStart().toLocalDateTime();
			}
			model.eventEnd = e.getEnd().toLocalDateTime();
			
			if (e instanceof FuelUsage fu) {
				processUsage(model, fu.getFuels());
			}
		}
		model.eventStart = eventStart;
		model.totalEmission += model.baseFuelEmission + model.bogEmission + model.pilotLightEmission;
		
		return model;
	}
	
	private static void processUsage(final CargoEmissionAccountingReportModelV1 model, List<FuelQuantity> fuelQuantity) {
		model.baseFuelEmission += EmissionsUtils.getBaseFuelEmission(model, fuelQuantity);
		model.bogEmission += EmissionsUtils.getBOGEmission(model, fuelQuantity);
		model.pilotLightEmission += EmissionsUtils.getPilotLightEmission(model, fuelQuantity);
	}

	public static File jsonOutput(final List<CargoEmissionAccountingReportModelV1> models) {
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
