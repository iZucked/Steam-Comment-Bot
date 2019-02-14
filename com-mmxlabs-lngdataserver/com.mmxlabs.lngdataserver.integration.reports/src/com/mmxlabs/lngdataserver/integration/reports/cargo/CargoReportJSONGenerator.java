/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargo;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
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

	public static List<CargoReportModel> createReportData(final @NonNull ScheduleModel scheduleModel) {

		final VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
		final List<CargoReportModel> models = new LinkedList<>();

		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {

			final CargoReportModel model = new CargoReportModel();
			final EList<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();

			final Optional<SlotAllocation> loadOptional = slotAllocations //
					.stream() //
					.filter(s -> (s.getSlot() != null && (s.getSlot() instanceof LoadSlot))) //
					.findFirst();

			final Optional<SlotAllocation> dischargeOptional = slotAllocations.stream()//
					.filter(s -> (s.getSlot() != null && s.getSlot() instanceof DischargeSlot)) //
					.findFirst();

			if (!loadOptional.isPresent()) {
				continue;
			}

			model.cargoType = cargoAllocation.getCargoType().toString();

			final String vesselName = vesselAssignmentFormatter.render(cargoAllocation);
			if (!vesselName.equals("")) {
				model.vesselName = vesselName;
			}

			if (loadOptional.isPresent()) {
				updateLoadAttributes(model, loadOptional.get());
			}
			if (dischargeOptional.isPresent()) {
				updateDischargeAttributes(model, dischargeOptional.get());
			}

			models.add(model);
		}

		for (final OpenSlotAllocation openSlotAllocation : scheduleModel.getSchedule().getOpenSlotAllocations()) {

			final CargoReportModel model = new CargoReportModel();

			Slot slot = openSlotAllocation.getSlot();
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
				model.loadScheduledDate = slot.getWindowStartWithSlotOrPortTime().toLocalDateTime();
				model.loadPortName = slot.getPort().getName();
			} else {
				model.dischargeName = slot.getName();
				model.dischargeComment = slot.getNotes();
				model.saleContract = slot.getContract() == null ? null : slot.getContract().getName();
				model.saleCounterparty = slot.getSlotOrDelegateCounterparty();
				model.dischargeScheduledDate = slot.getWindowStartWithSlotOrPortTime().toLocalDateTime();
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
		model.loadVolumeM3 = slotAllocation.getVolumeTransferred();
		model.loadVolumeMMBTU = slotAllocation.getEnergyTransferred();
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
		model.dischargeVolumeM3 = slotAllocation.getVolumeTransferred();
		model.dischargeVolumeMMBTU = slotAllocation.getEnergyTransferred();
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
