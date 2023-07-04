/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
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
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class VesselEmissionAccountingReportJSONGenerator {

	private static final long TONS_TO_GRAMS = 1_000_000;

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
				for (final Event e : seq.getEvents()) {
					final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
					model.eventID = cargoId;
					model.scenarioName = scenarioName;
					model.isPinnedFlag = isPinned;
					model.schedule = schedule;

					model.vesselName = vesselName;
					model.eventStart = e.getStart().toLocalDateTime();
					model.eventEnd = e.getEnd().toLocalDateTime();

					model.pilotLightEmission = 0L;
					model.totalEmission = 0L;
					model.methaneSlip = 0L;
					model.attainedCII = 0L;
					model.methaneSlipRate = vessel.getVesselOrDelegateMethaneSlipRate();
					int journeyDistance = 0;
					
					model.eventID = vesselName;

					if (e instanceof final SlotVisit sv) {
						model.eventType = "Slot Visit";
						final SlotAllocation sa = sv.getSlotAllocation();
						model.otherID = sa.getName();
						if (sa != null) {
							final Slot<?> slot = sa.getSlot();
							model.eventID = slot.getName();
							if (slot != null) {
								model.equivalents.add(slot);
								if (slot.getCargo() != null) {
									model.otherID = slot.getCargo().getUuid();
								}
							}
							if (slot instanceof LoadSlot) {
								model.methaneSlip += (long) (sa.getEnergyTransferred() * model.methaneSlipRate);
							}
						}
					} else if (e instanceof final StartEvent se) {
						model.eventType = "Vessel start";
						final SlotAllocation slotAllocation = se.getSlotAllocation();
						if (slotAllocation != null) {
							model.otherID = slotAllocation.getName();
							final Slot<?> slot = slotAllocation.getSlot();
							if (slot != null) {
								model.eventID = slot.getName();
							}
						}
					} else if (e instanceof final EndEvent endEvent) {
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
					} else if (e instanceof final Journey j) {
						if (j.isLaden()) {
							model.eventType = "Laden Leg";
						} else {
							model.eventType = "Ballast Leg";
						}
						if (j.getPreviousEvent() instanceof final SlotVisit sv && sv.getSlotAllocation() != null) {
							final SlotAllocation slotAllocation = sv.getSlotAllocation();
							if (slotAllocation != null) {
								model.otherID = slotAllocation.getName();
								//								final Slot<?> slot = slotAllocation.getSlot();
								//								if (slot != null) {
								//									model.eventID = slot.getName();
								//								}
							}
							model.otherID = sv.getSlotAllocation().getName();
						}
						journeyDistance = j.getDistance();
					} else if (e instanceof final Idle idleEvent) {
						if (idleEvent.isLaden()) {
							model.eventType = "Laden Idle";
						} else {
							model.eventType = "Ballast Idle";
						}
						if (idleEvent.getNextEvent() instanceof final SlotVisit sv && sv.getSlotAllocation() != null) {
							final SlotAllocation slotAllocation = sv.getSlotAllocation();
							if (slotAllocation != null) {
								model.otherID = slotAllocation.getName();
								//								final Slot<?> slot = slotAllocation.getSlot();
								//								if (slot != null) {
								//									model.eventID = slot.getName();
								//								}
							}
							model.otherID = sv.getSlotAllocation().getName();
						}
					}
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

					if (e instanceof FuelUsage fuelUsageEvent) {	

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
								model.consumedFBO += EmissionsUtils.consumedQuantityLNG(fuelQuantity);
								emissionsLNGAccumulator += model.consumedFBO * EmissionsUtils.LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
								break;
							case NBO:
								model.consumedNBO += EmissionsUtils.consumedQuantityLNG(fuelQuantity);
								emissionsLNGAccumulator += model.consumedNBO * EmissionsUtils.LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
								break;
							default:
							}
						}

						model.emissionsLNG = Math.round(emissionsLNGAccumulator);
						model.totalEmission = model.baseFuelEmissions + model.pilotLightEmission + model.emissionsLNG;
					}

					model.totalEmission += 25 * model.methaneSlip;
					final int denominatorForCIICalculation = journeyDistance * vessel.getDeadWeight();
					if (denominatorForCIICalculation == 0) {
						model.attainedCII = 0L;
					} else {
						model.attainedCII = TONS_TO_GRAMS * 1 / denominatorForCIICalculation;
					}
					models.add(model);
				}
			}
		}

		return models;
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
