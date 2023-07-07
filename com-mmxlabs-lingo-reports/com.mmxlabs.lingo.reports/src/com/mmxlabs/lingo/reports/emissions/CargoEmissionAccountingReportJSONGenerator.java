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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class CargoEmissionAccountingReportJSONGenerator {

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

		for (final Event event : cargoAllocation.getEvents()) {
			if (eventStart == null) {
				eventStart = event.getStart().toLocalDateTime();
			}
			model.eventEnd = event.getEnd().toLocalDateTime();
		}

		calculateFuelEmissions(cargoAllocation, model);
		calculateMethaneSlip(cargoAllocation, model, vessel);
		calculateCII(cargoAllocation, model, vessel);

		model.eventStart = eventStart;
		model.totalEmission += model.nbo + model.fbo + model.baseFuelEmission + model.pilotLightEmission + model.upstreamEmission + model.liquefactionEmission + model.methaneSlipEmissionsCO2
				+ model.pipelineEmission;
		return model;
	}

	private static void calculateFuelEmissions(final CargoAllocation cargoAllocation, final CargoEmissionAccountingReportModelV1 model) {
		double baseFuelAccumulator = 0.0;
		double nboAccumulator = 0.0;
		double fboAccumulator = 0.0;
		double pilotLightAccumulator = 0.0;
		for (final Event event : cargoAllocation.getEvents()) {
			if (event instanceof final FuelUsage fuelUsageEvent) {

				for (final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {

					final Fuel fuel = fuelQuantity.getFuel();
					final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
					final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);

					switch (fuel) {
					case BASE_FUEL:
						if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
							throw new IllegalStateException();
						}
						baseFuelAccumulator += fuelAmount.getQuantity() * baseFuel.getEmissionRate();
						break;
					case PILOT_LIGHT:
						if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
							throw new IllegalStateException();
						}
						pilotLightAccumulator += fuelAmount.getQuantity() * baseFuel.getEmissionRate();
						break;
					case FBO:
						fboAccumulator += EmissionsUtils.consumedQuantityLNG(fuelQuantity) * EmissionsUtils.LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
						break;
					case NBO:
						nboAccumulator += EmissionsUtils.consumedQuantityLNG(fuelQuantity) * EmissionsUtils.LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
						break;
					default:
					}
				}
			}
		}
		model.baseFuelEmission = Math.round(baseFuelAccumulator);
		model.pilotLightEmission = Math.round(pilotLightAccumulator);
		model.nbo = Math.round(nboAccumulator);
		model.fbo = Math.round(fboAccumulator);
	}

	private static void calculateMethaneSlip(final CargoAllocation cargoAllocation, final CargoEmissionAccountingReportModelV1 model, final Vessel vessel) {
		double methaneSlipAccumulator = 0.0;
		for (final Event event : cargoAllocation.getEvents()) {
			if (event instanceof final SlotVisit sv) {
				final SlotAllocation sa = sv.getSlotAllocation();
				if (sa != null) {
					final Slot<?> slot = sa.getSlot();
					if (slot instanceof LoadSlot) {
						methaneSlipAccumulator += sa.getPhysicalEnergyTransferred() * vessel.getVesselOrDelegateMethaneSlipRate();
					}
				}
			}
		}
		model.methaneSlip = Math.round(methaneSlipAccumulator);
		model.methaneSlipEmissionsCO2 = EmissionsUtils.METHANE_CO2_EQUIVALENT * model.methaneSlip;
	}

	private static void calculateCII(final CargoAllocation cargoAllocation, final CargoEmissionAccountingReportModelV1 model, final Vessel vessel) {
		double distanceAccumulator = 0.0;
		for (final Event event : cargoAllocation.getEvents()) {
			if (event instanceof final Journey journeyEvent) {
				distanceAccumulator += journeyEvent.getDistance();
			}
		}
		final double distance = Math.round(distanceAccumulator);
		final double denominatorCII = distance * vessel.getVesselOrDelegateDeadWeight();
		if (denominatorCII == 0) {
			model.ciiValue = null;
			model.ciiGrade = "-";
		} else {
			final double numeratorCII = (double) model.nbo + model.fbo + model.baseFuelEmission + model.pilotLightEmission + model.upstreamEmission + model.liquefactionEmission
					+ model.pipelineEmission;
			model.ciiValue = Math.round(EmissionsUtils.MT_TO_GRAMS * numeratorCII / denominatorCII);
			model.ciiGrade = UtilsCII.getLetterGrade(vessel, model.ciiValue);
		}
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
