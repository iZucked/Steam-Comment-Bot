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
import com.mmxlabs.lngdataserver.integration.reports.cargo.CargoReportModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class CargoesPerContractJSONGenerator {

	public static List<CargoesPerContractReportModel> createLongShortData(ScheduleModel scheduleModel) {
		Map<YearMonth, List<ContractReportModel>> purchaseContractPerMonths = new HashMap<>(); 
		Map<YearMonth, List<ContractReportModel>> sellContractPerMonths = new HashMap<>();

		List<LoadSlot> loadSlots = new ArrayList<>();
		List<DischargeSlot> dischargeSlots = new ArrayList<>();

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

			if (loadOptional.isPresent()) {
				loadSlots.add(loadOptional.get());
			}

			if (dischargeOptional.isPresent()) {
				dischargeSlots.add(dischargeOptional.get());
			}
		}

		for (OpenSlotAllocation openSlotAllocation: scheduleModel.getSchedule().getOpenSlotAllocations()) {
			Slot slot = openSlotAllocation.getSlot();

			// Long
			if (slot instanceof DischargeSlot) {
				DischargeSlot discharge = (DischargeSlot) slot;
				dischargeSlots.add(discharge);
				continue;
			} 

			// Short
			if (slot instanceof LoadSlot) {
				LoadSlot load = (LoadSlot) slot;
				loadSlots.add(load);
			}
		}

		for (LoadSlot load: loadSlots) {
			if (load.getContract() != null) {
				String name = load.getContract().getName();
				ContractType type = load.getContract().getContractType();

				LocalDate loadDate = load.getWindowStart();
				YearMonth key = YearMonth.of(loadDate.getYear(), loadDate.getMonth());

				purchaseContractPerMonths.computeIfPresent(key, (k, v) -> {
					ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});

				purchaseContractPerMonths.computeIfAbsent(key, k -> {
					List<ContractReportModel> v = new ArrayList<>();
					ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});
			}
		}

		for (DischargeSlot discharge: dischargeSlots) {
			if (discharge.getContract() != null) {
				String name = discharge.getContract().getName();
				ContractType type = discharge.getContract().getContractType();

				LocalDate dischargeDate = discharge.getWindowStart();
				YearMonth key = YearMonth.of(dischargeDate.getYear(), dischargeDate.getMonth());

				sellContractPerMonths.computeIfPresent(key, (k, v) -> {
					ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});

				sellContractPerMonths.computeIfAbsent(key, k -> {
					List<ContractReportModel> v = new ArrayList<>();
					ContractReportModel contract = new ContractReportModel();
					contract.setName(name);
					contract.setNb(1);
					v.add(contract);
					return v;
				});
			}
		}

		YearMonth min = findMinYearMonth(purchaseContractPerMonths.keySet(), sellContractPerMonths.keySet());
		YearMonth max = findMaxYearMonth(purchaseContractPerMonths.keySet(), sellContractPerMonths.keySet());

		if (min == null || max == null) {
			return null;
		}

		List<CargoesPerContractReportModel> cargoesPerContractReportModels = fillLongShortReportModelRange(min, max, purchaseContractPerMonths, sellContractPerMonths);
//		jsonOutput(cargoesPerContractReportModels);
		return cargoesPerContractReportModels;
	}


	private static List<CargoesPerContractReportModel> fillLongShortReportModelRange(YearMonth min, YearMonth max, Map<YearMonth, List<ContractReportModel>> purchaseContractPerMonths, Map<YearMonth, List<ContractReportModel>> sellContractPerMonths) {

		List<YearMonth> months = Stream.iterate(min, date -> date.plusMonths(1)).limit(ChronoUnit.MONTHS.between(min, max) + 1).collect(Collectors.toList());
		List<CargoesPerContractReportModel> cargoesPerContractReportModels = new ArrayList<>();

		for(YearMonth month: months) {
			CargoesPerContractReportModel cargoesPerContractReportModel = new CargoesPerContractReportModel();
			cargoesPerContractReportModel.month = month.getMonthValue();
			cargoesPerContractReportModel.year = month.getYear();
			cargoesPerContractReportModel.contracts = getSumContracts(purchaseContractPerMonths.get(month));
			//cargoesPerContractReportModel.sellContracts = getSumContracts(sellContractPerMonths.get(month));

			cargoesPerContractReportModels.add(cargoesPerContractReportModel);
			
		}

		return cargoesPerContractReportModels;
	}

	private static List<ContractReportModel> getSumContracts(List<ContractReportModel> contracts) {
		if (contracts == null) {
			return new ArrayList<>();
		}
		
		Map<String, ContractReportModel> sums = new HashMap<>();

		for(ContractReportModel contract: contracts) {
			sums.computeIfPresent(contract.getName(), (k, v) -> {
				v.setNb(v.getNb() + 1);
				return v;
			});

			sums.computeIfAbsent(contract.getName(), (k) -> {
				ContractReportModel c = new ContractReportModel();
				c.setName(k);
				c.setNb(1);
				return c;	
			});
		}
		return new ArrayList<>(sums.values());
	}

	private static YearMonth findMinYearMonth(Collection<YearMonth> l1, Collection<YearMonth> l2) {
		YearMonth min = null;
		Optional<YearMonth> minL1 = l1.stream().min(YearMonth::compareTo);
		Optional<YearMonth> minL2 = l2.stream().min(YearMonth::compareTo);

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

	private static YearMonth findMaxYearMonth(Collection<YearMonth> l1, Collection<YearMonth> l2) {
		YearMonth max;
		Optional<YearMonth> maxL1 = l1.stream().max(YearMonth::compareTo);
		Optional<YearMonth> maxL2 = l2.stream().max(YearMonth::compareTo);

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

	private static void jsonOutput(List<CargoesPerContractReportModel> cargoesPerContractReportModels) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), cargoesPerContractReportModels);
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
