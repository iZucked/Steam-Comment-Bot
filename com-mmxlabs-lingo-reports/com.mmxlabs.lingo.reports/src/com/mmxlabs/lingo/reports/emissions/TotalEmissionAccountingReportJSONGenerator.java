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
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class TotalEmissionAccountingReportJSONGenerator {

	public static List<TotalEmissionAccountingReportModelV1> createReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final @NonNull ScheduleModel scheduleModel) {
		final List<TotalEmissionAccountingReportModelV1> models = new LinkedList<>();

		if (scheduleModel.getSchedule() == null) {
			return models;
		}

		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			final TotalEmissionAccountingReportModelV1 model = createCargoAllocationReportData(scenarioDataProvider, cargoAllocation);
			if (model != null) {
				models.add(model);
			}
		}

		return models;
	}

	public static TotalEmissionAccountingReportModelV1 createCargoAllocationReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final CargoAllocation cargoAllocation) {
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

		if (loadSlot != null) {
			Port port = loadSlot.getPort();
			final PurchaseContract purchaseContract = loadSlot.getContract();

			if (purchaseContract != null) {
				model.pipelineEmissionRate = purchaseContract.getContractOrDelegatePipelineEmissionRate();
				model.upstreamEmissionRate = purchaseContract.getContractOrDelegateUpstreamEmissionRate();
				int physicalEnergyTransferred = ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred();
				model.upstreamEmission = (long) (physicalEnergyTransferred * model.upstreamEmissionRate);
				model.pipelineEmission = (long) (physicalEnergyTransferred * model.pipelineEmissionRate);
				model.totalEmission += model.upstreamEmission;
				model.totalEmission += model.pipelineEmission;
			}

			if (port != null) {
				if (purchaseContract == null) {
					model.pipelineEmissionRate = port.getPipelineEmissionRate();
					model.upstreamEmissionRate = port.getUpstreamEmissionRate();
					int physicalEnergyTransferred = ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred();
					model.upstreamEmission = (long) (physicalEnergyTransferred * model.upstreamEmissionRate);
					model.pipelineEmission = (long) (physicalEnergyTransferred * model.pipelineEmissionRate);
					model.totalEmission += model.upstreamEmission;
					model.totalEmission += model.pipelineEmission;
				}
				model.liquefactionEmissionRate = port.getLiquefactionEmissionRate();
				model.liquefactionEmission = (long) (model.liquefactionEmissionRate * ScheduleModelUtils.getLoadAllocation(cargoAllocation).getPhysicalEnergyTransferred());
				model.totalEmission += model.liquefactionEmission;
			}

		}
		final Vessel vessel = ScheduleModelUtils.getVessel(cargoAllocation.getSequence());

		model.eventID = cargoAllocation.getName();
		// if vesel is not null assign the emission rates and process the usage for each
		// event
		if (vessel != null) {
			model.vesselName = vessel.getName();
			model.baseFuelEmissionRate = vessel.getVesselOrDelegateBaseFuelEmissionRate();
			model.bogEmissionRate = vessel.getVesselOrDelegateBogEmissionRate();
			model.pilotLightEmissionRate = vessel.getVesselOrDelegatePilotLightEmissionRate();
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

	private static void processUsage(final TotalEmissionAccountingReportModelV1 model, List<FuelQuantity> fuelQuantity) {
		for (final FuelQuantity fq : fuelQuantity) {
			switch (fq.getFuel()) {
			case BASE_FUEL: {
				final Optional<FuelAmount> optMtFuelAmount = fq.getAmounts().stream() //
						.filter(fa -> fa.getUnit() == FuelUnit.MT) //
						.findFirst();
				if (optMtFuelAmount.isPresent()) {
					model.shippingEmission += (long) (optMtFuelAmount.get().getQuantity() * model.baseFuelEmissionRate);
				}
				break;
			}
			case FBO, NBO: {
				// Want m3
				final Optional<FuelAmount> optM3FuelAmount = fq.getAmounts().stream() //
						.filter(fa -> fa.getUnit() == FuelUnit.M3) //
						.findFirst();
				if (optM3FuelAmount.isPresent()) {
					model.shippingEmission += (long) (optM3FuelAmount.get().getQuantity() * model.bogEmissionRate);
				}
				break;
			}
			case PILOT_LIGHT: {
				final Optional<FuelAmount> optMtFuelAmount = fq.getAmounts().stream() //
				.filter(fa -> fa.getUnit() == FuelUnit.MT)//
				.findFirst();
				if (optMtFuelAmount.isPresent()) {
					model.shippingEmission += (long) (optMtFuelAmount.get().getQuantity() * model.pilotLightEmissionRate);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + fq.getFuel());
			}
		}

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
