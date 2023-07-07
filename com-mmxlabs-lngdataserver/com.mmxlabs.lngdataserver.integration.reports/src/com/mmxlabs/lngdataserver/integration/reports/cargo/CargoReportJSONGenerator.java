/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargo;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class CargoReportJSONGenerator {

	public static String createSchemaAndReportData(final Mode mode, final @NonNull ScheduleModel scheduleModel) throws JsonProcessingException {

		final StringBuilder sb = new StringBuilder();
		sb.append("{ \"coldef\": ");

		final SchemaGenerator generator = new SchemaGenerator();
		final String generateHubSchema = generator.generateHubSchema(CargoReportModel.class, mode);
		sb.append(generateHubSchema);

		sb.append(", \"data\": ");
		final List<CargoReportModel> models = CargoReportJSONGenerator.createReportData(scheduleModel);
		final ObjectMapper objectMapper = new ObjectMapper();
		sb.append(objectMapper.writeValueAsString(models));
		sb.append("}");

		return sb.toString();
	}

	public static List<CargoReportModel> createReportData(final @NonNull ScheduleModel scheduleModel) {

		final VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
		final List<CargoReportModel> models = new LinkedList<>();

		int rowGroupCounter = 1;
		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {

			int rowGroup = rowGroupCounter++;
			// Build up list of slots assigned to cargo, sorting into loads and discharges
			final List<SlotAllocation> loadSlots = new ArrayList<>();
			final List<SlotAllocation> dischargeSlots = new ArrayList<>();
			for (final SlotAllocation slot : cargoAllocation.getSlotAllocations()) {
				if (slot.getSlot() instanceof LoadSlot) {
					loadSlots.add(slot);
				} else if (slot.getSlot() instanceof DischargeSlot) {
					dischargeSlots.add(slot);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}
			}

			// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {
				final CargoReportModel model = new CargoReportModel();
				model.rowGroup = rowGroup;
				model.cargoType = cargoAllocation.getCargoType().toString();

				if (i == 0) {
					final String vesselName = vesselAssignmentFormatter.render(cargoAllocation);
					if (vesselName != null && !vesselName.equals("")) {
						model.vesselName = vesselName;
					}
				}

				if (i < loadSlots.size()) {
					updateLoadAttributes(model, loadSlots.get(i));
				}
				if (i < dischargeSlots.size()) {
					updateDischargeAttributes(model, dischargeSlots.get(i));
				}
				models.add(model);
			}

		}

		for (final OpenSlotAllocation openSlotAllocation : scheduleModel.getSchedule().getOpenSlotAllocations()) {

			final CargoReportModel model = new CargoReportModel();

			Slot<?> slot = openSlotAllocation.getSlot();
			if (slot == null) {
				continue;
			}
			boolean isLong = slot instanceof LoadSlot;
			model.cargoType = isLong ? "Long" : "Short";

			if (isLong) {
				model.loadName = slot.getName();
				model.loadComment = slot.getNotes();
				model.purchaseContract = slot.getContract() == null ? null : slot.getContract().getName();
				model.purchaseCounterparty = slot.getSlotOrDelegateCounterparty();
				model.loadScheduledDate = slot.getSchedulingTimeWindow().getStart().toLocalDateTime();
				model.loadPortName = slot.getPort().getName();
			} else {
				model.dischargeName = slot.getName();
				model.dischargeComment = slot.getNotes();
				model.saleContract = slot.getContract() == null ? null : slot.getContract().getName();
				model.saleCounterparty = slot.getSlotOrDelegateCounterparty();
				model.dischargeScheduledDate = slot.getSchedulingTimeWindow().getStart().toLocalDateTime();
				model.dischargePortName = slot.getPort().getName();
			}

			models.add(model);
		}

		return models;
	}

	private static void updateLoadAttributes(final @NonNull CargoReportModel model, final @NonNull SlotAllocation slotAllocation) {
		final Slot slot = slotAllocation.getSlot();
		if (slot != null) {
			model.loadName = slot.getName();
			model.loadComment = slot.getNotes();
			model.purchaseContract = slot.getContract() == null ? null : slot.getContract().getName();
			model.purchaseCounterparty = slot.getSlotOrDelegateCounterparty();
		}
		final SlotVisit slotVisit = slotAllocation.getSlotVisit();
		if (slotVisit != null) {
			model.loadScheduledDate = slotVisit.getStart().toLocalDateTime();
			model.loadPortName = slotVisit.getPort().getName();
		}
		model.loadPrice = slotAllocation.getPrice();
		model.loadVolumeM3 = (double) slotAllocation.getVolumeTransferred();
		model.loadVolumeMMBTU = (double) slotAllocation.getEnergyTransferred();
	}

	private static void updateDischargeAttributes(final @NonNull CargoReportModel model, final @NonNull SlotAllocation slotAllocation) {
		final Slot slot = slotAllocation.getSlot();
		if (slot != null) {
			model.dischargeName = slot.getName();
			model.dischargeComment = slot.getNotes();
			model.saleContract = slot.getContract() == null ? null : slot.getContract().getName();
			model.saleCounterparty = slot.getSlotOrDelegateCounterparty();
		}
		final SlotVisit slotVisit = slotAllocation.getSlotVisit();
		if (slotVisit != null) {
			model.dischargeScheduledDate = slotVisit.getStart().toLocalDateTime();
			model.dischargePortName = slotVisit.getPort().getName();
		}
		model.dischargePrice = slotAllocation.getPrice();
		model.dischargeVolumeM3 = (double) slotAllocation.getVolumeTransferred();
		model.dischargeVolumeMMBTU = (double) slotAllocation.getEnergyTransferred();
	}

	private static void jsonOutput(final List<CargoReportModel> models) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), models);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}
