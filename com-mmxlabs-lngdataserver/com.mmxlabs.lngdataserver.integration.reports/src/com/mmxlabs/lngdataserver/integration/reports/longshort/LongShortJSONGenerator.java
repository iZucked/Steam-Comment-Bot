/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.longshort;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class LongShortJSONGenerator {

	public static List<LongShortReportModel> createLongShortData(ScheduleModel scheduleModel) {
		Map<YearMonth, Integer> shortsPerMonths = new HashMap<>(); 
		Map<YearMonth, Integer> longsPerMonths = new HashMap<>();
		
		for (CargoAllocation cargoAllocation: scheduleModel.getSchedule().getCargoAllocations()) {
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
			Optional<DischargeSlot> dischargeOptional = slotAllocations.stream() //
					.filter(s->(s.getSlot() != null && s.getSlot() instanceof DischargeSlot)) //
					.map(s->(DischargeSlot) s.getSlot()) //
					.findFirst();
			
			// Long
			if (!loadOptional.isPresent() && dischargeOptional.isPresent()) {
				LocalDate dischargeDate = dischargeOptional.get().getWindowStart();
				YearMonth key = YearMonth.of(dischargeDate.getYear(), dischargeDate.getMonth());
				longsPerMonths.computeIfAbsent(key, (k) -> 1);
				longsPerMonths.computeIfPresent(key, (k, v) -> v++);
				continue;
			}
			
			// Short
			if (loadOptional.isPresent() && !dischargeOptional.isPresent()) {
				LocalDate loadDate = loadOptional.get().getWindowStart();
				LocalDate dischargeDate = loadOptional.get().getWindowStart();
				YearMonth key = YearMonth.of(loadDate.getYear(), loadDate.getMonth());
				longsPerMonths.computeIfAbsent(key, (k) -> 1);
				longsPerMonths.computeIfPresent(key, (k, v) ->longsPerMonths.get(key) + 1);
			}
		}
		
		YearMonth min = findMinYearMonth(shortsPerMonths.keySet(), longsPerMonths.keySet());
		YearMonth max = findMaxYearMonth(shortsPerMonths.keySet(), longsPerMonths.keySet());

		if (min == null || max == null) {
			return null;
		}
		
		List<LongShortReportModel> longShortReportModels = fillLongShortReportModelRange(min, max, shortsPerMonths, longsPerMonths);
		jsonOutput(longShortReportModels);
		return longShortReportModels;
	}
	

	private static List<LongShortReportModel> fillLongShortReportModelRange(YearMonth min, YearMonth max, Map<YearMonth, Integer> shortsPerMonths, Map<YearMonth, Integer>longsPerMonths) {
		
		List<YearMonth> months = Stream.iterate(min, date -> date.plusMonths(1)).limit(ChronoUnit.MONTHS.between(min, max)).collect(Collectors.toList());
		List<LongShortReportModel> longShortReportModels = new ArrayList<>();
		
		for(YearMonth month: months) {
			LongShortReportModel shortReportModel = new LongShortReportModel();
			shortReportModel.month = month.getMonthValue();
			shortReportModel.year = month.getMonthValue();
			shortReportModel.cargoType = "short";
			shortReportModel.count = shortsPerMonths.computeIfAbsent(month, key -> 0);
			
			longShortReportModels.add(shortReportModel);
		}
		
		for(YearMonth month: months) {
			LongShortReportModel longReportModel = new LongShortReportModel();
			longReportModel.month = month.getMonthValue();
			longReportModel.year = month.getMonthValue();
			longReportModel.cargoType = "long";
			longReportModel.count = longsPerMonths.computeIfAbsent(month, key -> 0);
			
			longShortReportModels.add(longReportModel);
		}
		return longShortReportModels;
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
	
	private static void jsonOutput(List<LongShortReportModel> longShortReportModels) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), longShortReportModels);
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
