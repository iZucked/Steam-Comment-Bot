/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.schedule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.util.AssignmentLabelProvider;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.impl.IdleImpl;
import com.mmxlabs.models.lng.schedule.impl.JourneyImpl;

public class ScheduleReportJSONGenerator {
	
	static private ScheduleReportModel createScheduleReportModel(Event e, String type, String vesselName, int count) {
				ScheduleReportModel scheduleReportModel = new ScheduleReportModel();
				scheduleReportModel.startDate = e.getStart().toLocalDate();
				scheduleReportModel.endDate = e.getEnd().toLocalDate();
				scheduleReportModel.type = type;

				scheduleReportModel.vesselName = vesselName;
				scheduleReportModel.name = "" + count;
				
				return scheduleReportModel;
	}

	public static List<ScheduleReportModel> createScheduleData(ScheduleModel scheduleModel) {
		VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
		List<ScheduleReportModel> schedulesReportModels = new LinkedList<>();
		int count = 0;
		for(Sequence sequence: scheduleModel.getSchedule().getSequences()) {
			String vesselName;
			
			if (sequence.isSpotVessel()) {
				vesselName = AssignmentLabelProvider.getLabelFor(sequence.getCharterInMarket(), sequence.getSpotIndex());
			} else {
				if (sequence.getVesselAvailability() != null) {
				vesselName = AssignmentLabelProvider.getLabelFor(sequence.getVesselAvailability());
				} else {
					continue;
				}
			}
			
			List<Event> ladenJourneys = sequence.getEvents().stream().filter(x -> x instanceof JourneyImpl).filter(x -> ((JourneyImpl) x).isLaden()).collect(Collectors.toList());
			List<Event> ballastJourneys = sequence.getEvents().stream().filter(x -> x instanceof JourneyImpl).filter(x -> !((JourneyImpl) x).isLaden()).collect(Collectors.toList());
			List<Event> idleJourneys = sequence.getEvents().stream().filter(x -> x instanceof IdleImpl).collect(Collectors.toList());
			
			
			for(Event ladenJourney: ladenJourneys) {
				schedulesReportModels.add(createScheduleReportModel(ladenJourney, "laden", vesselName, count++));
			}
			
			for(Event ballastJourney: ballastJourneys) {
				schedulesReportModels.add(createScheduleReportModel(ballastJourney, "ballast", vesselName, count++));
			}

			for(Event idleJourney: idleJourneys) {
				schedulesReportModels.add(createScheduleReportModel(idleJourney, "idle", vesselName, count++));
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
