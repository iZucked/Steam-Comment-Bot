/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.io.File;
import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.cii.ModelUtilsCII;
import com.mmxlabs.models.lng.schedule.emissions.EmissionModelUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class VesselEmissionAccountingReportJSONGenerator {
	
	private VesselEmissionAccountingReportJSONGenerator() {
	}

	public static List<VesselEmissionAccountingReportModelV1> createReportData(final @NonNull ScheduleModel scheduleModel, final boolean isPinned, final String scenarioName) {
		final List<VesselEmissionAccountingReportModelV1> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();

		if (schedule == null) {
			return models;
		}

		for (final Sequence seq : schedule.getSequences()) {
			final String cargoId = seq.getEvents().get(0).name();
			final Vessel vessel = ScheduleModelUtils.getVessel(seq);
			if (vessel != null) {
				final String vesselName = vessel.getName();
				for (final Event event : seq.getEvents()) {
					final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
					ModelUtilsCII.processAccumulatableEventModelForCII(model, vessel, event);
					model.eventID = cargoId;
					model.scenarioName = scenarioName;
					model.isPinnedFlag = isPinned;
					model.schedule = schedule;
					
					model.vesselName = vesselName;
					model.eventStart = event.getStart().toLocalDateTime();
					model.eventEnd = event.getEnd().toLocalDateTime();

					model.pilotLightEmission = 0L;
					model.totalEmission = 0L;
					model.methaneSlip = 0L;
					model.methaneSlipRate = vessel.getVesselOrDelegateMethaneSlipRate();
					model.eventID = vesselName;

					final int journeyDistance = processEvent(event, model);
					if (model.eventType == null) {
						continue;
					}

					model.baseFuelEmissions = 0L;
					model.pilotLightEmission = 0L;
					model.emissionsLNG = 0L;
					model.consumedNBO = 0L;
					model.consumedFBO = 0L;
					model.baseFuelType = "-";
					model.pilotLightFuelType = "-";
					model.pilotLightFuelConsumption = 0L;
					model.baseFuelConsumed = 0L;
					model.ciiValue = 0L;

					if (event instanceof FuelUsage fuelUsageEvent) {	
						processFuelUsageEvent(model, fuelUsageEvent);
					}
					
					model.setCII(vessel, model.totalEmission, journeyDistance, Year.from(event.getStart().toLocalDate()));
					model.totalEmission += EmissionModelUtils.METHANE_CO2_EQUIVALENT * model.methaneSlip;
					models.add(model);
				}
			}
		}

		return models;
	}

	private static void processFuelUsageEvent(final VesselEmissionAccountingReportModelV1 model, FuelUsage fuelUsageEvent) {
		double emissionsLNGAccumulator = 0.0;
		
		for (final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {

			final Fuel fuel = fuelQuantity.getFuel();
			final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
			final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);


			switch (fuel) {
			case BASE_FUEL:
				if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
					throw new IllegalStateException();
				}
				model.baseFuelType = baseFuel.getName();
				model.baseFuelConsumed = Math.round(fuelAmount.getQuantity());
				model.baseFuelEmissions = Math.round(model.baseFuelConsumed * baseFuel.getEmissionRate());
				break;
			case PILOT_LIGHT:
				if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
					throw new IllegalStateException();
				}
				model.pilotLightFuelType = baseFuel.getName();
				model.pilotLightFuelConsumption = Math.round(fuelAmount.getQuantity());
				model.pilotLightEmission = Math.round(model.pilotLightFuelConsumption * baseFuel.getEmissionRate());
				break;
			case FBO:
				model.consumedFBO += EmissionModelUtils.consumedQuantityLNG(fuelQuantity);
				emissionsLNGAccumulator += model.consumedFBO * EmissionModelUtils.LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
				break;
			case NBO:
				model.consumedNBO += EmissionModelUtils.consumedQuantityLNG(fuelQuantity);
				emissionsLNGAccumulator += model.consumedNBO * EmissionModelUtils.LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
				break;
			default:
			}
		}

		model.emissionsLNG = Math.round(emissionsLNGAccumulator);
		model.totalEmission += model.baseFuelEmissions + model.pilotLightEmission + model.emissionsLNG;
	}

	private static int processEvent(final Event event, final VesselEmissionAccountingReportModelV1 model) {
		if (event instanceof final SlotVisit slotVisit) {
			processSlotVisit(model, slotVisit);
		} else if (event instanceof final StartEvent startEvent) {
			return processStartEvent(model, startEvent);
		} else if (event instanceof final EndEvent endEvent) {
			processEndEvent(model, endEvent);
		} else if (event instanceof final Journey journeyEvent) {
			return processJourneyEvent(model, journeyEvent);
		} else if (event instanceof final Idle idleEvent) {
			processIdleEvent(model, idleEvent);
		}
		return 0;
	}

	private static void processIdleEvent(final VesselEmissionAccountingReportModelV1 model, Idle idleEvent) {
		if (idleEvent.isLaden()) {
			model.eventType = "Laden Idle";
		} else {
			model.eventType = "Ballast Idle";
		}
		if (idleEvent.getNextEvent() instanceof final SlotVisit sv && sv.getSlotAllocation() != null) {
			final SlotAllocation slotAllocation = sv.getSlotAllocation();
			if (slotAllocation != null) {
				model.otherID = slotAllocation.getName();
			}
			model.otherID = sv.getSlotAllocation().getName();
		}
	}

	private static int processJourneyEvent(final VesselEmissionAccountingReportModelV1 model, Journey journeyEvent) {
		int journeyDistance;
		if (journeyEvent.isLaden()) {
			model.eventType = "Laden Leg";
		} else {
			model.eventType = "Ballast Leg";
		}
		if (journeyEvent.getPreviousEvent() instanceof final SlotVisit sv && sv.getSlotAllocation() != null) {
			final SlotAllocation slotAllocation = sv.getSlotAllocation();
			if (slotAllocation != null) {
				model.otherID = slotAllocation.getName();
			}
			model.otherID = sv.getSlotAllocation().getName();
		}
		journeyDistance = journeyEvent.getDistance();
		return journeyDistance;
	}

	private static void processEndEvent(final VesselEmissionAccountingReportModelV1 model, EndEvent endEvent) {
		model.eventType = "Vessel end";
		if (endEvent.getSlotAllocation() != null) {
			model.otherID = endEvent.getSlotAllocation().getName();
		}
		final SlotAllocation slotAllocation = endEvent.getSlotAllocation();
		if (slotAllocation != null) {
			model.otherID = slotAllocation.getName();
			final Slot<?> slot = slotAllocation.getSlot();
			if (slot != null) {
				model.eventID = slot.getName();
			}
		}
	}

	private static int processStartEvent(final VesselEmissionAccountingReportModelV1 model, StartEvent startEvent) {
		model.eventType = "Vessel start";
		final SlotAllocation slotAllocation = startEvent.getSlotAllocation();
		if (slotAllocation != null) {
			model.otherID = slotAllocation.getName();
			final Slot<?> slot = slotAllocation.getSlot();
			if (slot != null) {
				model.eventID = slot.getName();
			}
		}
		final VesselCharter vesselCharter = startEvent.getSequence().getVesselCharter();
		if (vesselCharter != null) {
			final CIIStartOptions ciiStartOptions = vesselCharter.getCiiStartOptions();
			if (ciiStartOptions != null) {
				model.totalEmission += ciiStartOptions.getYearTodayEmissions();
				return ciiStartOptions.getYearTodayDistance();
			}
		}
		return 0;
	}

	private static void processSlotVisit(final VesselEmissionAccountingReportModelV1 model, SlotVisit slotVisit) {
		model.eventType = "Slot Visit";
		final @Nullable SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
		if (slotAllocation != null) {
			model.otherID = slotAllocation.getName();
			final Slot<?> slot = slotAllocation.getSlot();
			if (slot != null) {
				model.eventID = slot.getName();
				model.equivalents.add(slot);
				if (slot.getCargo() != null) {
					model.otherID = slot.getCargo().getUuid();
				}
			}
			if (slot instanceof LoadSlot) {
				model.methaneSlip += Math.round(EmissionModelUtils.GRAMS_TO_TONS * slotAllocation.getPhysicalEnergyTransferred() * model.methaneSlipRate);
			}
		}
	}

	public static File jsonOutput(final List<VesselEmissionAccountingReportModelV1> models) {
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
