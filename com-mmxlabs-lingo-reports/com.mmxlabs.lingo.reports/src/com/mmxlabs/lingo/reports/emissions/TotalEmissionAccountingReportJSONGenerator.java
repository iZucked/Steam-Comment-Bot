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
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class TotalEmissionAccountingReportJSONGenerator {

	public static List<TotalEmissionAccountingReportModelV1> createReportData(final @NonNull ScheduleModel scheduleModel, final boolean isPinned, final String scenarioName) {
		final List<TotalEmissionAccountingReportModelV1> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();
		if (schedule == null) {
			return models;
		}

		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			final TotalEmissionAccountingReportModelV1 model = createCargoAllocationReportData(cargoAllocation);
			if (model != null) {
				model.scenarioName = scenarioName;
				model.schedule = schedule;
				model.isPinned = isPinned;
				model.otherID = cargoAllocation.getName();
				models.add(model);
			}
		}

		return models;
	}

	public static TotalEmissionAccountingReportModelV1 createCargoAllocationReportData(final CargoAllocation cargoAllocation) {
		final TotalEmissionAccountingReportModelV1 model = new TotalEmissionAccountingReportModelV1();
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
		model.liquefactionEmission = 0L;
		model.pipelineEmission = 0L;
		model.shippingEmission = 0L;
		model.upstreamEmission = 0L;
		model.totalEmission = 0L;
		final LoadSlot loadSlot = ScheduleModelUtils.getLoadSlot(cargoAllocation);
		int physicalEnergyTransferred = ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred();

		if (loadSlot != null) {
			Port port = loadSlot.getPort();
			final PurchaseContract purchaseContract = loadSlot.getContract();

			if (purchaseContract != null) {
				model.pipelineEmissionRate = purchaseContract.getContractOrDelegatePipelineEmissionRate();
				model.upstreamEmissionRate = purchaseContract.getContractOrDelegateUpstreamEmissionRate();
			}

			if (port != null) {
				if (purchaseContract == null) {
					model.pipelineEmissionRate = port.getPipelineEmissionRate();
					model.upstreamEmissionRate = port.getUpstreamEmissionRate();
				}
				model.liquefactionEmissionRate = port.getLiquefactionEmissionRate();
				model.liquefactionEmission = (long) (model.liquefactionEmissionRate * physicalEnergyTransferred);
				model.totalEmission += model.liquefactionEmission;
			}

			if (model.pipelineEmissionRate != 0.0) {
				model.pipelineEmission = (long) (physicalEnergyTransferred * model.pipelineEmissionRate);
				model.totalEmission += model.pipelineEmission;
			}
			if (model.upstreamEmissionRate != 0.0) {
				model.upstreamEmission = (long) (physicalEnergyTransferred * model.upstreamEmissionRate);
				model.totalEmission += model.upstreamEmission;
			}
		}
		final Vessel vessel = ScheduleModelUtils.getVessel(cargoAllocation.getSequence());

		model.eventID = cargoAllocation.getName();
		// if vesel is not null assign the emission rates and process the usage for each
		// event
		if (vessel != null) {
			model.vesselName = vessel.getName();
			model.baseFuelEmissionRate = EmissionsUtils.getBaseFuelEmissionRate(vessel);
			model.bogEmissionRate = EmissionsUtils.getBOGEmissionRate(vessel);
			model.pilotLightEmissionRate = EmissionsUtils.getPilotLightEmissionRate(vessel);
			for (final Event e : cargoAllocation.getEvents()) {
				if (e instanceof FuelUsage fu) {
					model.shippingEmission += EmissionsUtils.getBaseFuelEmission(model, fu.getFuels());
					model.shippingEmission += EmissionsUtils.getBOGEmission(model, fu.getFuels());
					model.shippingEmission += EmissionsUtils.getPilotLightEmission(model, fu.getFuels());
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

	public static File jsonOutput(final List<TotalEmissionAccountingReportModelV1> models) {
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
