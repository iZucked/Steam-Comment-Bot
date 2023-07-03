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
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class CargoEmissionAccountingReportJSONGenerator{
	
	public static List<CargoEmissionAccountingReportModelV1> createReportData(final @NonNull ScheduleModel scheduleModel, final boolean isPinned, final String scenarioName) {
		final List<CargoEmissionAccountingReportModelV1> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();
		if (schedule == null) {
			return models;
		}
		
		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			final CargoEmissionAccountingReportModelV1 model = createCargoAllocationReportData(cargoAllocation);
			if (model != null) {
				model.scenarioName = scenarioName;
				model.schedule = schedule;
				model.isPinnedFlag = isPinned;
				model.otherID = cargoAllocation.getName();
				models.add(model);
			}
		}

		return models;
	}
	
	public static CargoEmissionAccountingReportModelV1 createCargoAllocationReportData(final CargoAllocation cargoAllocation) {
		final CargoEmissionAccountingReportModelV1 model = new CargoEmissionAccountingReportModelV1();
		
		final EList<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();

		final Optional<SlotAllocation> loadOptional = slotAllocations //
				.stream() //
				.filter(s -> (s.getSlot() instanceof LoadSlot)) //
				.findFirst();
		
		if (loadOptional.isEmpty()) {
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
		model.baseFuelEmission = 0L;
		model.pilotLightEmission = 0L;
		model.totalEmission = 0L;
		model.methaneSlip = 0L;
		LocalDateTime eventStart = null;
		
		calculatePortEmissions(cargoAllocation, model);
		
		
		for (final Event e : cargoAllocation.getEvents()) {
			if (eventStart == null) {
				eventStart = e.getStart().toLocalDateTime();
			}
			model.eventEnd = e.getEnd().toLocalDateTime();
		}
		slotAllocations.stream().filter(s -> s.getSlot() instanceof LoadSlot).forEach(sa -> {
			model.methaneSlip += (long) (sa.getEnergyTransferred() * model.methaneSlipRate);
		});
		model.eventStart = eventStart;
		model.totalEmission += model.baseFuelEmission + model.pilotLightEmission + 25 * model.methaneSlip;
		return model;
	}

	private static void calculatePortEmissions(final CargoAllocation cargoAllocation, final CargoEmissionAccountingReportModelV1 model) {
		model.upstreamEmission = 0L;
		model.pipelineEmission = 0L;
		model.liquefactionEmission = 0L;
		final Slot<?> slot = cargoAllocation.getSlotAllocations().stream().map(it -> it.getSlot()).findAny().orElse(null);
		if (slot != null) {			
			final Port port = ScheduleModelUtils.getPortFromSlot(slot);
			if (port != null) {
				final SlotAllocation loadSlotAllocation = ScheduleModelUtils.getLoadAllocation(cargoAllocation);
				if (loadSlotAllocation != null) {
					model.upstreamEmission = Math.round(port.getUpstreamEmissionRate() * loadSlotAllocation.getPhysicalVolumeTransferred());
					model.pipelineEmission = Math.round(port.getPipelineEmissionRate() * loadSlotAllocation.getPhysicalVolumeTransferred());
					model.liquefactionEmission = Math.round(port.getLiquefactionEmissionRate() * loadSlotAllocation.getPhysicalVolumeTransferred());
				}
			}
		}
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
