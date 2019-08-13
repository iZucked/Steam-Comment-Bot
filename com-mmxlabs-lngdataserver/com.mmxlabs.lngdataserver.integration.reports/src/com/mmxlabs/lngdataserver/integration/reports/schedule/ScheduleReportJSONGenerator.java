/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.schedule;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.util.AssignmentLabelProvider;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.impl.JourneyImpl;

public class ScheduleReportJSONGenerator {

	static private ScheduleReportModel createScheduleReportModel(Event e, String type, String vesselName, int count) {
		ScheduleReportModel scheduleReportModel = new ScheduleReportModel();
		scheduleReportModel.startDate = e.getStart().toLocalDate();
		scheduleReportModel.endDate = e.getEnd().toLocalDate();
		scheduleReportModel.type = type;
		scheduleReportModel.vesselName = vesselName;
		scheduleReportModel.name = "" + e.name();

		if (type.equals("laden") || type.equals("ballast")) {

			if (e.getPort() != null) {
				scheduleReportModel.departurePort = e.getPort().getName();
			}

			if (e.getNextEvent() != null && e.getNextEvent().getPort() != null) {
				scheduleReportModel.arrivalPort = e.getNextEvent().getPort().getName();
			}
		} else {
			if (e.getPort() != null) {
				scheduleReportModel.departurePort = e.getPort().getName();
			}
			scheduleReportModel.arrivalPort = null;
		}

		return scheduleReportModel;
	}

	public static List<ScheduleReportModel> createScheduleData(ScheduleModel scheduleModel) {
		VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
		List<ScheduleReportModel> schedulesReportModels = new LinkedList<>();
		int count = 0;
		for (Sequence sequence : scheduleModel.getSchedule().getSequences()) {
			String vesselName;

			if (sequence.isSpotVessel()) {
				vesselName = AssignmentLabelProvider.getLabelFor(sequence.getCharterInMarket(), sequence.getSpotIndex(), true);
				// Skip nominal cargoes as they render badly on the hub
				if (sequence.getSpotIndex() < 0) {
					continue;
				}
			} else {
				if (sequence.getVesselAvailability() != null) {
					vesselName = AssignmentLabelProvider.getLabelFor(sequence.getVesselAvailability());
				} else {
					continue;
				}
			}

			List<Event> ladenJourneys = sequence.getEvents().stream().filter(x -> x instanceof Journey).filter(x -> ((JourneyImpl) x).isLaden()).collect(Collectors.toList());
			List<Event> ballastJourneys = sequence.getEvents().stream().filter(x -> x instanceof Journey).filter(x -> !((JourneyImpl) x).isLaden()).collect(Collectors.toList());
			List<Event> idleJourneys = sequence.getEvents().stream().filter(x -> x instanceof Idle).collect(Collectors.toList());
			List<Event> visitJourneys = sequence.getEvents().stream().filter(x -> x instanceof SlotVisit).collect(Collectors.toList());
			List<Event> charterJourneys = sequence.getEvents().stream().filter(x -> x instanceof GeneratedCharterOut).collect(Collectors.toList());
			List<VesselEventVisit> vesselEvents = sequence.getEvents().stream().filter(x -> x instanceof VesselEventVisit).map(x -> (VesselEventVisit) x).collect(Collectors.toList());

			for (Event ladenJourney : ladenJourneys) {
				schedulesReportModels.add(createScheduleReportModel(ladenJourney, "laden", vesselName, count++));
			}

			for (Event ballastJourney : ballastJourneys) {
				schedulesReportModels.add(createScheduleReportModel(ballastJourney, "ballast", vesselName, count++));
			}

			for (Event idleJourney : idleJourneys) {
				schedulesReportModels.add(createScheduleReportModel(idleJourney, "idle", vesselName, count++));
			}

			for (Event visitJourney : visitJourneys) {
				schedulesReportModels.add(createScheduleReportModel(visitJourney, "visit", vesselName, count++));
			}

			for (Event charterJourney : charterJourneys) {
				schedulesReportModels.add(createScheduleReportModel(charterJourney, "charter", vesselName, count++));
			}
			for (VesselEventVisit eventVisit : vesselEvents) {
				VesselEvent event = eventVisit.getVesselEvent();
				String type = "unknown";
				if (event instanceof CharterOutEvent) {
					type = "charter";
				} else if (event instanceof DryDockEvent) {
					type = "drydock";
				} else if (event instanceof MaintenanceEvent) {
					type = "maintenance";
				}
				schedulesReportModels.add(createScheduleReportModel(eventVisit, type, vesselName, count++));
			}
		}

		return schedulesReportModels;
	}

	private static void jsonOutput(List<ScheduleReportModel> scheduleReportModels) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), scheduleReportModels);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}