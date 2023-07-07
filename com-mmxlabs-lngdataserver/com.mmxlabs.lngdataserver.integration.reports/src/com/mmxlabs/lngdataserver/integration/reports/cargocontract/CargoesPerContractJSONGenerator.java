/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargocontract;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class CargoesPerContractJSONGenerator {

	public static List<CargoesPerContractReportModel> createReportData(final ScheduleModel scheduleModel) {
		final Map<YearMonth, Map<String, Integer>> purchaseContractPerMonths = new HashMap<>();
		final Map<YearMonth, Map<String, Integer>> sellContractPerMonths = new HashMap<>();

		final List<LoadSlot> loadSlots = new ArrayList<>();
		final List<DischargeSlot> dischargeSlots = new ArrayList<>();

		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {

			for (final SlotAllocation slot : cargoAllocation.getSlotAllocations()) {
				if (slot.getSlot() instanceof final LoadSlot s) {
					loadSlots.add(s);
				} else if (slot.getSlot() instanceof final DischargeSlot s) {
					dischargeSlots.add(s);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : scheduleModel.getSchedule().getOpenSlotAllocations()) {
			final Slot<?> slot = openSlotAllocation.getSlot();

			if (slot instanceof final DischargeSlot discharge) {
				// Long
				dischargeSlots.add(discharge);
				// Short
			} else if (slot instanceof final LoadSlot load) {
				loadSlots.add(load);
			}
		}

		YearMonth min = null;
		YearMonth max = null;
		for (final LoadSlot load : loadSlots) {
			if (load.getContract() != null) {
				final String name = load.getContract().getName();
				final ContractType type = load.getContract().getContractType();

				final LocalDate loadDate = load.getWindowStart();
				final YearMonth key = YearMonth.of(loadDate.getYear(), loadDate.getMonth());

				purchaseContractPerMonths.computeIfAbsent(key, k -> new HashMap<>()).merge(name, 1, Integer::sum);
				if (min == null || key.isBefore(min)) {
					min = key;
				}
				if (max == null || key.isAfter(max)) {
					max = key;
				}
			}
		}

		for (final DischargeSlot discharge : dischargeSlots) {
			if (discharge.getContract() != null) {
				final String name = discharge.getContract().getName();
				final ContractType type = discharge.getContract().getContractType();

				final LocalDate dischargeDate = discharge.getWindowStart();
				final YearMonth key = YearMonth.of(dischargeDate.getYear(), dischargeDate.getMonth());
				sellContractPerMonths.computeIfAbsent(key, k -> new HashMap<>()).merge(name, 1, Integer::sum);
				if (min == null || key.isBefore(min)) {
					min = key;
				}
				if (max == null || key.isAfter(max)) {
					max = key;
				}
			}
		}

		if (min == null || max == null) {
			return Collections.emptyList();
		}

		YearMonth month = min;
		final List<CargoesPerContractReportModel> cargoesPerContractReportModels = new ArrayList<>();
		while (!month.isAfter(max)) {
			final CargoesPerContractReportModel cargoesPerContractReportModel = new CargoesPerContractReportModel();
			cargoesPerContractReportModel.month = month.getMonthValue();
			cargoesPerContractReportModel.year = month.getYear();
			cargoesPerContractReportModel.contracts = new LinkedList<>();
			
			final Map<String, Integer> sums = purchaseContractPerMonths.get(month);
			if (sums != null) {
				for (final var e : sums.entrySet()) {
					final ContractReportModel c = new ContractReportModel();
					c.setName(e.getKey());
					c.setNb(e.getValue());
					cargoesPerContractReportModel.contracts.add(c);
				}
			}
			// Only reporting purchase contracts?
			// cargoesPerContractReportModel.sellContracts = getSumContracts(sellContractPerMonths.get(month));
			month = month.plusMonths(1);
		}

		return cargoesPerContractReportModels;
	}

	private static void jsonOutput(final List<CargoesPerContractReportModel> cargoesPerContractReportModels) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), cargoesPerContractReportModels);
		} catch (final JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
