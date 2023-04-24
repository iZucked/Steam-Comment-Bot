/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.schedule;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.util.AssignmentLabelProvider;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class ScheduleReportJSONGeneratorV2 {

	private static ScheduleReportModelV2 createScheduleReportModel(Event e, String type, String subType, String vesselName) {
		ScheduleReportModelV2 scheduleReportModel = new ScheduleReportModelV2();
		scheduleReportModel.setStartDate(e.getStart().toLocalDate());
		scheduleReportModel.setEndDate(e.getEnd().toLocalDate());
		scheduleReportModel.setType(type);
		scheduleReportModel.setSubType(subType);
		scheduleReportModel.setVesselName(vesselName);
		scheduleReportModel.setName("" + e.name());
		scheduleReportModel.setLabel("");

		if (e instanceof Journey) {
			Journey journey = (Journey) e;
			if (journey.getRouteOption() == RouteOption.PANAMA) {
				scheduleReportModel.setLabel("Panama");
			} else if (journey.getRouteOption() == RouteOption.SUEZ) {
				scheduleReportModel.setLabel("Suez");
			}
		}

		if (type.equals("laden") || type.equals("ballast")) {

			if (e.getPort() != null) {
				scheduleReportModel.setDeparturePort(e.getPort().getName());
			}

			if (e.getNextEvent() != null && e.getNextEvent().getPort() != null) {
				scheduleReportModel.setArrivalPort(e.getNextEvent().getPort().getName());
			}
		} else {
			if (e.getPort() != null) {
				scheduleReportModel.setDeparturePort(e.getPort().getName());
			}
			scheduleReportModel.setArrivalPort(null);
		}

		return scheduleReportModel;
	}

	public static List<ScheduleReportModelV2> createScheduleData(ScheduleModel scheduleModel) {

		List<ScheduleReportModelV2> schedulesReportModels = new LinkedList<>();

		for (Sequence sequence : scheduleModel.getSchedule().getSequences()) {
			String vesselName;

			if (sequence.isSpotVessel()) {
				vesselName = AssignmentLabelProvider.getLabelFor(sequence.getCharterInMarket(), sequence.getSpotIndex(), true);
				// Skip nominal cargoes as they render badly on the hub
				if (sequence.getSpotIndex() < 0) {
					continue;
				}
			} else {
				if (sequence.getVesselCharter() != null) {
					vesselName = AssignmentLabelProvider.getLabelFor(sequence.getVesselCharter());
				} else {
					continue;
				}
			}

			List<Event> ladenJourneys = sequence.getEvents().stream().filter(x -> x instanceof Journey).filter(x -> ((Journey) x).isLaden()).collect(Collectors.toList());
			List<Event> ballastJourneys = sequence.getEvents().stream().filter(x -> x instanceof Journey).filter(x -> !((Journey) x).isLaden()).collect(Collectors.toList());
			List<Event> ladenIdleJourneys = sequence.getEvents().stream().filter(x -> x instanceof Idle).filter(x -> ((Idle) x).isLaden()).collect(Collectors.toList());
			List<Event> ballastIdleJourneys = sequence.getEvents().stream().filter(x -> x instanceof Idle).filter(x -> !((Idle) x).isLaden()).collect(Collectors.toList());

			List<Event> visitJourneys = sequence.getEvents().stream().filter(x -> x instanceof SlotVisit).collect(Collectors.toList());
			List<Event> charterJourneys = sequence.getEvents().stream().filter(x -> x instanceof GeneratedCharterOut).collect(Collectors.toList());
			List<Event> charterLengthJourneys = sequence.getEvents().stream().filter(x -> x instanceof GeneratedCharterLengthEvent).collect(Collectors.toList());
			List<VesselEventVisit> vesselEvents = sequence.getEvents().stream().filter(x -> x instanceof VesselEventVisit).map(x -> (VesselEventVisit) x).collect(Collectors.toList());

			for (Event ladenJourney : ladenJourneys) {
				schedulesReportModels.add(createScheduleReportModel(ladenJourney, "laden", "", vesselName));
			}

			for (Event ballastJourney : ballastJourneys) {
				schedulesReportModels.add(createScheduleReportModel(ballastJourney, "ballast", "", vesselName));
			}

			for (Event idleJourney : ladenIdleJourneys) {
				schedulesReportModels.add(createScheduleReportModel(idleJourney, "idle", "ladenidle", vesselName));
			}
			for (Event idleJourney : ballastIdleJourneys) {
				schedulesReportModels.add(createScheduleReportModel(idleJourney, "idle", "ballastidle", vesselName));
			}

			for (Event visitJourney : visitJourneys) {
				String subType = "";
				if (visitJourney instanceof SlotVisit) {
					SlotVisit slotVisit = (SlotVisit) visitJourney;
					if (slotVisit.getSlotAllocation().getSlotAllocationType() == SlotAllocationType.PURCHASE) {
						subType = "load";
					} else if (slotVisit.getSlotAllocation().getSlotAllocationType() == SlotAllocationType.SALE) {
						subType = "discharge";
					}
				}
				schedulesReportModels.add(createScheduleReportModel(visitJourney, "visit", subType, vesselName));
			}

			for (Event charterJourney : charterJourneys) {
				schedulesReportModels.add(createScheduleReportModel(charterJourney, "generatedcharterout", "", vesselName));
			}
			for (Event charterJourney : charterLengthJourneys) {
				schedulesReportModels.add(createScheduleReportModel(charterJourney, "charterlength", "", vesselName));
			}
			for (VesselEventVisit eventVisit : vesselEvents) {
				VesselEvent event = eventVisit.getVesselEvent();
				String type = "unknown";
				if (event instanceof CharterOutEvent) {
					type = "charterout";
				} else if (event instanceof DryDockEvent) {
					type = "drydock";
				} else if (event instanceof MaintenanceEvent) {
					type = "maintenance";
				}
				schedulesReportModels.add(createScheduleReportModel(eventVisit, type, "", vesselName));
			}
		}

		return schedulesReportModels;
	}

}