/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	private static final double LNG_DENSITY_TON_PER_M3 = 0.450;
	private static final double LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL = 2.750;
	
	private static final long TONS_TO_GRAMS = 1_000_000;

	public static List<VesselEmissionAccountingReportModelV1> createReportData(final @NonNull ScheduleModel scheduleModel, final boolean isPinned, final String scenarioName) {
		final List<VesselEmissionAccountingReportModelV1> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();
		
		if (schedule == null) {
			return models;
		}

		for (final Sequence seq : schedule.getSequences()) {
			final Vessel vessel = ScheduleModelUtils.getVessel(seq);
			if (vessel != null) {
				final String vesselName = vessel.getName();
				for (final Event e : seq.getEvents()) {
					final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
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
					int journeyDistance = 0;
					
					if (e instanceof final SlotVisit sv) {
						final SlotAllocation sa = sv.getSlotAllocation();
						model.otherID = sa.getName();
						if (sa != null) {
							final Slot<?> slot = sa.getSlot();
							if (slot != null) {
								model.equivalents.add(slot);
								model.eventID = slot.getName();
								if (slot.getCargo() != null) {
									model.otherID = slot.getCargo().getUuid();
								}
							}
							if (slot instanceof LoadSlot) {
								model.methaneSlip += (long) (sa.getEnergyTransferred() * model.methaneSlipRate);
							}
						}
					} else if (e instanceof final StartEvent se) {
						model.eventID = "Vessel start";
						if (se.getSlotAllocation() != null) {
							model.otherID = se.getSlotAllocation().getName();
						}
					} else if (e instanceof final EndEvent ee) {
						model.eventID = "Vessel end";
						if (ee.getSlotAllocation() != null) {
							model.otherID = ee.getSlotAllocation().getName();
						}
					} else if (e instanceof final Journey j) {
						if (j.isLaden()) {
							model.eventID = "Laden Leg";
						} else {
							model.eventID = "Ballast Leg";
						}
						if (j.getPreviousEvent() instanceof final SlotVisit sv && sv.getSlotAllocation() != null) {
							model.otherID = sv.getSlotAllocation().getName();
						}
						journeyDistance = j.getDistance();
					} else if (e instanceof final Idle i) {
						if (i.isLaden()) {
							model.eventID = "Laden Idle";
						} else {
							model.eventID = "Ballast Idle";
						}
						if (i.getNextEvent() instanceof final SlotVisit sv && sv.getSlotAllocation() != null) {
							model.otherID = sv.getSlotAllocation().getName();
						}
					}
					if (model.eventID == null) {
						continue; 
					}
					
					if (e instanceof FuelUsage fuelUsageEvent) {
						
						model.baseFuelEmissions = 0L;
						model.pilotLightEmission = 0L;
						model.emissionsLNG = 0L;
						model.consumedNBO = 0L;
						model.consumedFBO = 0L;
						
						for (final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {

							final Fuel fuel = fuelQuantity.getFuel();
							final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
							final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);
							
							switch (fuel) {
							case BASE_FUEL:
								model.baseFuelType = baseFuel.getName();
								model.baseFuelConsumed = Math.round(fuelAmount.getQuantity());
								model.baseFuelEmissions = Math.round(baseFuel.getEquivalenceFactor() * model.baseFuelConsumed * baseFuel.getEmissionRate());
								break;
							case PILOT_LIGHT:
								model.pilotLightFuelType = baseFuel.getName();
								model.pilotLightFuelConsumption = Math.round(fuelAmount.getQuantity());
								model.pilotLightEmission = Math.round(baseFuel.getEquivalenceFactor() * model.pilotLightFuelConsumption * baseFuel.getEmissionRate());
								break;
							case FBO:
								model.consumedFBO += consumedQuantityLNG(fuelQuantity, FuelUnit.MMBTU);
								model.emissionsLNG += Math.round(model.consumedFBO * LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL);
								break;
							case NBO:
								model.consumedNBO += consumedQuantityLNG(fuelQuantity, FuelUnit.MMBTU);
								model.emissionsLNG += Math.round(model.consumedNBO * LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL);
								break;
							default:
							}
						}
						
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

	private static Long consumedQuantityLNG(final FuelQuantity fuelQuantity, final FuelUnit desiredFuelUnit) {
		
		final Set<FuelUnit> uniqueUnits = new HashSet<>();
		for (final FuelAmount fuelAmount : fuelQuantity.getAmounts()) {
			uniqueUnits.add(fuelAmount.getUnit());
		}
		
		final boolean unitsAreTheSame = uniqueUnits.size() == 1;
		double quantity = 0.0;
				
		if (unitsAreTheSame) {
			final FuelUnit unitItSelf = uniqueUnits.stream().findAny().orElseThrow();
			quantity = fuelQuantity.getAmounts().stream().map(amount -> amount.getQuantity()).reduce(Double::sum).orElse(0.0);
			
			// Convert whatever to MT
			quantity = switch (unitItSelf) {
				case MMBTU -> throw new IllegalStateException("Bad");
				case M3 -> quantity * LNG_DENSITY_TON_PER_M3;
				case MT -> quantity;
			};
		} else {
			for (final FuelAmount amount : fuelQuantity.getAmounts()) {
				if (amount.getUnit() == FuelUnit.M3) {
					quantity = amount.getQuantity();
				}
			}
			quantity *= LNG_DENSITY_TON_PER_M3;
		}
		return Math.round(quantity);
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
