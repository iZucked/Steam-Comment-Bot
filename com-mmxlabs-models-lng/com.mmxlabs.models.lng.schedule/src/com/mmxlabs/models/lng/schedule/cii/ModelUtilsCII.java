/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.cii;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.emissions.EmissionModelUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class ModelUtilsCII {
	
	private ModelUtilsCII() {
	}

	public static void processAccumulatableEventModelForCII(final CIIAccumulatableEventModel model, final Vessel vessel, final Event event) {
		model.setCIIVessel(vessel);
		model.setCIIEvent(event);
		model.setCIIStartDate(event.getStart().toLocalDate());
		model.setCIIEndDate(event.getEnd().toLocalDate());
		if (event instanceof FuelUsage fuelUsageEvent) {
			processFuelUsageEvent(model, fuelUsageEvent);
		}
	}

	private static void processFuelUsageEvent(final CIIAccumulatableEventModel model, FuelUsage fuelUsageEvent) {
		for (final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {
			final Fuel fuel = fuelQuantity.getFuel();
			final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
			final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);
			switch (fuel) {
			case BASE_FUEL, PILOT_LIGHT:
				if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
					throw new IllegalStateException();
				}
				model.addToTotalEmissionForCII(Math.round(fuelAmount.getQuantity() * baseFuel.getEmissionRate()));
				break;
			case FBO, NBO:
				model.addToTotalEmissionForCII(EmissionModelUtils.consumedCarbonEquivalentEmissionLNG(fuelQuantity));
				break;
			default:
			}
		}
	}

	public static List<CIIAccumulatableEventModel> createCIIDataForVessel(ScheduleModel scheduleModel, Vessel vessel) {
		final List<CIIAccumulatableEventModel> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();

		if (schedule == null) {
			return models;
		}

		for (final Sequence seq : schedule.getSequences()) {
			final Vessel sequenceVessel = ScheduleModelUtils.getVessel(seq);
			if (sequenceVessel != null && sequenceVessel == vessel) {
				for (final Event event : seq.getEvents()) {
					final CIIAccumulatableEventModel model = new VerySImpleCIIAccumulatableModel();
					processAccumulatableEventModelForCII(model, vessel, event);
					models.add(model);
				}
			}
		}

		return models;
	}
}
