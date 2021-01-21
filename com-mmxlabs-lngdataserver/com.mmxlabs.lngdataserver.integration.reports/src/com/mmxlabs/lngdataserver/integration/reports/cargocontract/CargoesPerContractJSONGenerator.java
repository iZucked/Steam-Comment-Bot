/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;

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

	public static List<CargoesPerContractReportModel> createLongShortData(final ScheduleModel scheduleModel) {
		final Map<YearMonth, List<ContractReportModel>> purchaseContractPerMonths = new HashMap<>();
		final Map<YearMonth, List<ContractReportModel>> sellContractPerMonths = new HashMap<>();

		final List<LoadSlot> loadSlots = new ArrayList<>();
		final List<DischargeSlot> dischargeSlots = new ArrayList<>();

		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			final EList<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();

			final Optional<LoadSlot> loadOptional = slotAllocations //
					.stream() //
					.filter(s -> (s.getSlot() != null && (s.getSlot() instanceof LoadSlot))) //
					.map(s -> (LoadSlot) s.getSlot()) //
					.findFirst();
			final Optional<DischargeSlot> dischargeOptional = slotAllocations.stream().filter(s -> (s.getSlot() != null && s.getSlot() instanceof DischargeSlot)).map(s -> (DischargeSlot) s.getSlot())
					.findFirst();

			if (loadOptional.isPresent()) {
				loadSlots.add(loadOptional.get());
			}

			if (dischargeOptional.isPresent()) {
				dischargeSlots.add(dischargeOptional.get());
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : scheduleModel.getSchedule().getOpenSlotAllocations()) {
			final Slot slot = openSlotAllocation.getSlot();

			// Long
			if (slot instanceof DischargeSlot) {
				final DischargeSlot discharge = (DischargeSlot) slot;
				dischargeSlots.add(discharge);
				continue;
			}

			// Short
			if (slot instanceof LoadSlot) {
				final LoadSlot load = (LoadSlot) slot;
				loadSlots.add(load);
			}
		}

		for (final LoadSlot load : loadSlots) {
			if (load.getContract() != null) {
				final String name = load.getContract().getName();
				final ContractType type = load.getContract().getContractType();

				final LocalDate loadDate = load.getWindowStart();
				final YearMonth key = YearMonth.of(loadDate.getYear(), loadDate.getMonth());

				purchaseContractPerMonths.computeIfPresent(key, (k, v) -> {
					final ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});

				purchaseContractPerMonths.computeIfAbsent(key, k -> {
					final List<ContractReportModel> v = new ArrayList<>();
					final ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});
			}
		}

		for (final DischargeSlot discharge : dischargeSlots) {
			if (discharge.getContract() != null) {
				final String name = discharge.getContract().getName();
				final ContractType type = discharge.getContract().getContractType();

				final LocalDate dischargeDate = discharge.getWindowStart();
				final YearMonth key = YearMonth.of(dischargeDate.getYear(), dischargeDate.getMonth());

				sellContractPerMonths.computeIfPresent(key, (k, v) -> {
					final ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});

				sellContractPerMonths.computeIfAbsent(key, k -> {
					final List<ContractReportModel> v = new ArrayList<>();
					final ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});
			}
		}

		final YearMonth min = findMinYearMonth(purchaseContractPerMonths.keySet(), sellContractPerMonths.keySet());
		final YearMonth max = findMaxYearMonth(purchaseContractPerMonths.keySet(), sellContractPerMonths.keySet());

		if (min == null || max == null) {
			return null;
		}

		final List<CargoesPerContractReportModel> cargoesPerContractReportModels = fillLongShortReportModelRange(min, max, purchaseContractPerMonths, sellContractPerMonths);

		return cargoesPerContractReportModels;
	}

	private static List<CargoesPerContractReportModel> fillLongShortReportModelRange(final YearMonth min, final YearMonth max,
			final Map<YearMonth, List<ContractReportModel>> purchaseContractPerMonths, final Map<YearMonth, List<ContractReportModel>> sellContractPerMonths) {

		final List<YearMonth> months = Stream.iterate(min, date -> date.plusMonths(1)).limit(ChronoUnit.MONTHS.between(min, max) + 1).collect(Collectors.toList());
		final List<CargoesPerContractReportModel> cargoesPerContractReportModels = new ArrayList<>();

		for (final YearMonth month : months) {
			final CargoesPerContractReportModel cargoesPerContractReportModel = new CargoesPerContractReportModel();
			cargoesPerContractReportModel.month = month.getMonthValue();
			cargoesPerContractReportModel.year = month.getYear();
			cargoesPerContractReportModel.contracts = getSumContracts(purchaseContractPerMonths.get(month));
			// cargoesPerContractReportModel.sellContracts = getSumContracts(sellContractPerMonths.get(month));

			cargoesPerContractReportModels.add(cargoesPerContractReportModel);

		}

		return cargoesPerContractReportModels;
	}

	private static List<ContractReportModel> getSumContracts(final List<ContractReportModel> contracts) {
		if (contracts == null) {
			return new ArrayList<>();
		}

		final Map<String, ContractReportModel> sums = new HashMap<>();

		for (final ContractReportModel contract : contracts) {
			sums.computeIfPresent(contract.getName(), (k, v) -> {
				v.setNb(v.getNb() + 1);
				return v;
			});

			sums.computeIfAbsent(contract.getName(), (k) -> {
				final ContractReportModel c = new ContractReportModel();
				c.setName(k);
				c.setNb(1);
				return c;
			});
		}
		return new ArrayList<>(sums.values());
	}

	private static YearMonth findMinYearMonth(final Collection<YearMonth> l1, final Collection<YearMonth> l2) {
		YearMonth min = null;
		final Optional<YearMonth> minL1 = l1.stream().min(YearMonth::compareTo);
		final Optional<YearMonth> minL2 = l2.stream().min(YearMonth::compareTo);

		if (!minL1.isPresent() && !minL2.isPresent()) {
			return null;
		}

		if (minL2.isPresent() && minL1.isPresent()) {
			if (minL1.get().isBefore(minL2.get())) {
				min = minL1.get();
			} else {
				min = minL2.get();
			}
		} else if (!minL1.isPresent()) {
			min = minL2.get();
		} else {
			min = minL1.get();
		}
		return min;
	}

	private static YearMonth findMaxYearMonth(final Collection<YearMonth> l1, final Collection<YearMonth> l2) {
		YearMonth max;
		final Optional<YearMonth> maxL1 = l1.stream().max(YearMonth::compareTo);
		final Optional<YearMonth> maxL2 = l2.stream().max(YearMonth::compareTo);

		if (!maxL1.isPresent() && !maxL2.isPresent()) {
			return null;
		}

		if (maxL2.isPresent() && maxL1.isPresent()) {
			if (maxL1.get().isAfter(maxL2.get())) {
				max = maxL1.get();
			} else {
				max = maxL2.get();
			}
		} else if (!maxL1.isPresent()) {
			max = maxL2.get();
		} else {
			max = maxL1.get();
		}
		return max;
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
