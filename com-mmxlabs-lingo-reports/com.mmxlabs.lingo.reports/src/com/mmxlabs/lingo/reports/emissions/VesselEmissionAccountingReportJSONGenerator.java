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

public class VesselEmissionAccountingReportJSONGenerator {

	public static List<VesselEmissionAccountingReportModelV1> createReportData(final @NonNull IScenarioDataProvider scenarioDataProvider, final @NonNull ScheduleModel scheduleModel) {
		final List<VesselEmissionAccountingReportModelV1> models = new LinkedList<>();

		if (scheduleModel.getSchedule() == null) {
			return models;
		}

		for (final var seq : scheduleModel.getSchedule().getSequences()) {
			final Vessel vessel = ScheduleModelUtils.getVessel(seq);
			if (vessel != null) {
				final String vesselName = vessel.getName();
				final double baseFuelEmissionRate = EmissionsUtils.getBaseFuelEmissionRate(vessel);
				final double bogEmissionRate = EmissionsUtils.getBOGEmissionRate(vessel);
				final double pilotLightEmissionRate = EmissionsUtils.getPilotLightEmissionRate(vessel);
				for (final Event e : seq.getEvents()) {
					final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
					model.vesselName = vesselName;
					model.baseFuelEmissionRate = baseFuelEmissionRate;
					model.bogEmissionRate = bogEmissionRate;
					model.pilotLightEmissionRate = pilotLightEmissionRate;
					model.eventStart = e.getStart().toLocalDateTime();
					model.eventEnd = e.getEnd().toLocalDateTime();

					model.baseFuelEmission = 0L;
					model.bogEmission = 0L;
					model.pilotLightEmission = 0L;
					model.totalEmission = 0L;

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
					model.totalEmission += model.baseFuelEmission + model.bogEmission + model.pilotLightEmission;
					models.add(model);
				}
			}
		}

		return models;
	}

	private static void processUsage(final VesselEmissionAccountingReportModelV1 model, List<FuelQuantity> fuelQuantity) {
		model.baseFuelEmission += EmissionsUtils.getBaseFuelEmission(model, fuelQuantity);
		model.bogEmission += EmissionsUtils.getBOGEmission(model, fuelQuantity);
		model.pilotLightEmission += EmissionsUtils.getPilotLightEmission(model, fuelQuantity);
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
