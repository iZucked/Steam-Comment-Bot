/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class VesselEmissionAccountingReportJSONGenerator{
	
	public static List<VesselEmissionAccountingReportModelV1> createReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final @NonNull ScheduleModel scheduleModel) {
		final List<VesselEmissionAccountingReportModelV1> models = new LinkedList<>();

		if (scheduleModel.getSchedule() == null) {
			return models;
		}
		
		for (final var seq : scheduleModel.getSchedule().getSequences()) {
			final Vessel vessel = ScheduleModelUtils.getVessel(seq);
			if (vessel != null) {
				final String vesselName = vessel.getName();
				final double baseFuelEmissionRate = vessel.getBaseFuelEmissionRate();
				final double bogEmissionRate = vessel.getBogEmissionRate();
				final double pilotLightEmissionRate = vessel.getPilotLightEmissionRate();
				for (final Event e : seq.getEvents()) {
					final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
					model.vesselName = vesselName;
					model.baseFuelEmissionRate = baseFuelEmissionRate;
					model.bogEmissionRate = bogEmissionRate;
					model.pilotLightEmissionRate = pilotLightEmissionRate;
					model.eventStart = e.getStart().toLocalDateTime();
					model.eventEnd = e.getEnd().toLocalDateTime();
					
					model.baseFuelEmission = 0;
					model.bogEmission = 0;
					model.pilotLightEmission = 0;
					
					if (e instanceof FuelUsage fu) {
						processUsage(model, fu.getFuels());
					}
					if (e instanceof SlotVisit sv) {
						final SlotAllocation sa = sv.getSlotAllocation();
						if (sa != null) {
							final Slot<?> slot = sa.getSlot(); 
							if (slot != null) {
								model.equivalents.add(slot);
								model.eventID = slot.getName();
							}
						}
					} else if (e instanceof StartEvent se) {
						model.eventID = "Vessel start";
					} else if (e instanceof EndEvent) {
						model.eventID = "Vessel end";
					} else if (e instanceof Journey j) {
						if (j.isLaden()) {
							model.eventID = "Laden Leg";
						} else {
							model.eventID = "Ballast Leg";
						}
					} else if (e instanceof Idle i) {
						if (i.isLaden()) {
							model.eventID = "Laden Idle";
						} else {
							model.eventID = "Ballast Idle";
						}
					}
					models.add(model);
				}
			}
		}

		return models;
	}
	
	private static void processUsage(final VesselEmissionAccountingReportModelV1 model, List<FuelQuantity> fuelQuantity) {
		for (final FuelQuantity fq : fuelQuantity) {
			switch (fq.getFuel()) {
			case BASE_FUEL: 
				fq.getAmounts().forEach(fa -> model.baseFuelEmission += (int) (fa.getQuantity() * model.baseFuelEmissionRate));
				break;
			case FBO, NBO:
				fq.getAmounts().forEach(fa -> model.bogEmission += (int) (fa.getQuantity() * model.bogEmissionRate));
				break;
			case PILOT_LIGHT:
				fq.getAmounts().forEach(fa -> model.pilotLightEmission += (int) (fa.getQuantity() * model.pilotLightEmissionRate));
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + fq.getFuel());
			}
		}
		model.totalEmission = model.baseFuelEmission + model.bogEmission + model.pilotLightEmission;
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
