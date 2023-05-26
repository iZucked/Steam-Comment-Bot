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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class VesselEmissionAccountingReportJSONGenerator {

	public static List<VesselEmissionAccountingReportModelV1> createReportData(final @NonNull ScheduleModel scheduleModel, final boolean isPinned, final String scenarioName) {
		final List<VesselEmissionAccountingReportModelV1> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();
		
		if (schedule == null) {
			return models;
		}

		for (final var seq : schedule.getSequences()) {
			final Vessel vessel = ScheduleModelUtils.getVessel(seq);
			if (vessel != null) {
				final String vesselName = vessel.getName();
				final double baseFuelEmissionRate = EmissionsUtils.getBaseFuelEmissionRate(vessel);
				final double bogEmissionRate = EmissionsUtils.getBOGEmissionRate(vessel);
				final double pilotLightEmissionRate = EmissionsUtils.getPilotLightEmissionRate(vessel);
				for (final Event e : seq.getEvents()) {
					final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
					model.scenarioName = scenarioName;
					model.isPinned = isPinned;
					model.schedule = schedule;
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
