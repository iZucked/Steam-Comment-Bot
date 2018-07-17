/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargo;

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
import com.mmxlabs.common.time.Days;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class CargoReportJSONGenerator {

	public static List<CargoReportModel> createILPData(ScheduleModel scheduleModel) {
		VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
		List<CargoReportModel> ilpModels = new LinkedList<>();
		for (CargoAllocation cargoAllocation: scheduleModel.getSchedule().getCargoAllocations()) {
			CargoReportModel ilpModel = new CargoReportModel();
			EList<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
			List<SlotAllocation> collect = slotAllocations //
											.stream() //
											.filter(s->(s.getSlot() != null && (s.getSlot() instanceof LoadSlot))) //
											.collect(Collectors.toList());
			Optional<LoadSlot> loadOptional = slotAllocations //
												.stream() //
												.filter(s->(s.getSlot() != null && (s.getSlot() instanceof LoadSlot))) //
												.map(s->(LoadSlot) s.getSlot()) //
												.findFirst();
			Optional<DischargeSlot> dischargeOptional = slotAllocations.stream().filter(s->(s.getSlot() != null && s.getSlot() instanceof DischargeSlot)).map(s->(DischargeSlot) s.getSlot()).findFirst();
			if (!loadOptional.isPresent()) {
				continue;
			}
			ilpModel.loadingWindowDate = loadOptional.get().getWindowStart();
			ilpModel.loadingWindowSizeInHours = loadOptional.get().getWindowSizeInHours();
			ilpModel.sourcePortName = loadOptional.get().getPort().getName();
			ilpModel.type = "";
			if (dischargeOptional.isPresent()) {
				ilpModel.endBuyer = CargoReportUtils.getEndBuyerText(dischargeOptional.get());
				ilpModel.endBuyerWindowDate = dischargeOptional.get().getWindowStart();
				ilpModel.endBuyerWindowSizeInHours = dischargeOptional.get().getWindowSizeInHours();
				ilpModel.receivingPortName = dischargeOptional.get().getPort().getName();
			}
			String vesselName = vesselAssignmentFormatter.render(cargoAllocation);
			if (vesselName.equals("")) {
				ilpModel.vesselName = null;
			} else {
				ilpModel.vesselName = vesselName;
			}
			LocalDate nextLoadPortDate = CargoReportUtils.getNextLoadPortDate(cargoAllocation);
			ilpModel.nextLoadPortDate = nextLoadPortDate;
			if (nextLoadPortDate != null) {
				ilpModel.rtv = Days.between(loadOptional.get().getWindowStart(), nextLoadPortDate);
			} else {
				ilpModel.rtv = null;
			}
			Optional<SlotAllocation> slotAllocation = slotAllocations.stream().filter(s->s.getSlot() == loadOptional.get()).findFirst();
			ilpModel.loadableVolume = slotAllocation.get().getVolumeTransferred();
			ilpModel.comments = loadOptional.get().getNotes();
			ilpModels.add(ilpModel);
		}
//		jsonOutput(ilpModels);
		return ilpModels;
	}
	
	private static void jsonOutput(List<CargoReportModel> ilpModels) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), ilpModels);
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
