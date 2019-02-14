/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.longshort;
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


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

public class LongShortJSONGenerator {

	public static List<LongShortReportModel> createLongShortData(ScheduleModel scheduleModel) {
		Map<YearMonth, Integer> shortsPerMonths = new HashMap<>(); 
		Map<YearMonth, Integer> longsPerMonths = new HashMap<>();
		
		for (OpenSlotAllocation openSlotAllocation: scheduleModel.getSchedule().getOpenSlotAllocations()) {
			Slot slot = openSlotAllocation.getSlot();
			
			// Long
			if (slot instanceof DischargeSlot) {
				DischargeSlot discharge = (DischargeSlot) slot;

				LocalDate dischargeDate = discharge.getWindowStart();
				YearMonth key = YearMonth.of(dischargeDate.getYear(), dischargeDate.getMonth());
				longsPerMonths.computeIfPresent(key, (k, v) -> ++v);
				longsPerMonths.computeIfAbsent(key, (k) -> 1);
				continue;
			} 

			// Short
			if (slot instanceof LoadSlot) {
				LoadSlot load = (LoadSlot) slot;
				
				LocalDate loadDate = load.getWindowStart();
				YearMonth key = YearMonth.of(loadDate.getYear(), loadDate.getMonth());
				shortsPerMonths.computeIfPresent(key, (k, v) -> ++v);
				shortsPerMonths.computeIfAbsent(key, (k) -> 1);
			} 
		}
		
		YearMonth min = findMinYearMonth(shortsPerMonths.keySet(), longsPerMonths.keySet());
		YearMonth max = findMaxYearMonth(shortsPerMonths.keySet(), longsPerMonths.keySet());

		if (min == null || max == null) {
			return null;
		}
		
		List<LongShortReportModel> longShortReportModels = fillLongShortReportModelRange(min, max, shortsPerMonths, longsPerMonths);
//		jsonOutput(longShortReportModels);
		return longShortReportModels;
	}
	

	private static List<LongShortReportModel> fillLongShortReportModelRange(YearMonth min, YearMonth max, Map<YearMonth, Integer> shortsPerMonths, Map<YearMonth, Integer>longsPerMonths) {
		
		List<YearMonth> months = Stream.iterate(min, date -> date.plusMonths(1)).limit(ChronoUnit.MONTHS.between(min, max) + 1).collect(Collectors.toList());
		List<LongShortReportModel> longShortReportModels = new ArrayList<>();
		
		for(YearMonth month: months) {
			LongShortReportModel longShortReportModel = new LongShortReportModel();
			longShortReportModel.month = month.getMonthValue();
			longShortReportModel.year = month.getYear();
			longShortReportModel.longs = longsPerMonths.computeIfAbsent(month, key -> 0);
			longShortReportModel.shorts = shortsPerMonths.computeIfAbsent(month, key -> 0);
			
			longShortReportModels.add(longShortReportModel);
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
