/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package summary;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class KSummaryJSONGenerator {


	public static KSummaryModel createScheduleData(ScheduleModel scheduleModel) {
		Map<String, List<SlotAllocation>> contractGroups = new HashMap<>();

		for(CargoAllocation cargoAllocation: scheduleModel.getSchedule().getCargoAllocations()) {
			for (SlotAllocation slotAllocation: cargoAllocation.getSlotAllocations()) {

				if (slotAllocation.getSlot() instanceof DischargeSlot) {
					Contract contract = slotAllocation.getContract();
					if(contract != null) {
						contractGroups.computeIfPresent(contract.getName(), (k,v) -> {
							v.add(slotAllocation);
							return v;
						});
						contractGroups.computeIfAbsent(contract.getName(), (k) -> {
							List<SlotAllocation> l = new ArrayList<>();
							l.add(slotAllocation);
							return l;
						});
					}
				}
			}
		}
		
		LocalDate now = LocalDate.now();
		
		LocalDate adpCalendarStart = LocalDate.of(now.getYear(), 4, 1);
		LocalDate adpCalendarEnd = LocalDate.of(now.getYear() + 1, 4, 1);
		
		LocalDate yearlyCalendarStart = LocalDate.of(now.getYear(), 1, 1);
		LocalDate yearlyCalendarEnd = LocalDate.of(now.getYear() + 1, 1, 1);
		
		List<KSummaryReportModel> res = contractGroups.entrySet().stream().map(x -> contractStats(x.getValue(), false, yearlyCalendarStart, yearlyCalendarEnd)).collect(Collectors.toList());
		res.addAll(contractGroups.entrySet().stream().map(x -> contractStats(x.getValue(), true, adpCalendarStart, adpCalendarEnd)).collect(Collectors.toList()));
		res = res.stream().filter((x) -> x != null).collect(Collectors.toList());
		
		KSummaryModel output = new KSummaryModel();
		
		output.setSummaryData(res);
		output.setAdpCalendarStart(adpCalendarStart);
		output.setAdpCalendarEnd(adpCalendarEnd);
		
		output.setYearlyCalendarStart(yearlyCalendarStart);
		output.setYearlyCalendarEnd(yearlyCalendarEnd);
		
		jsonOutput(output);
		return output;
	}

	private static KSummaryReportModel contractStats(Collection<SlotAllocation> collection, boolean isADP, LocalDate start, LocalDate end) {
		KSummaryReportModel summary = new KSummaryReportModel();
		summary.isADP = isADP;

		List<SlotAllocation> slots = collection.stream().filter(slot -> slot.getSlot().getWindowStart().isAfter(start) && slot.getSlot().getWindowStart().isBefore(end)).collect(Collectors.toList());
		
		if (slots.isEmpty()) {
			return null;
		}
		
		slots.stream().forEach(slotAllocation -> {
			summary.counterparty = slotAllocation.getContract().getName();
			summary.cargoes++;
			summary.tonnes += slotAllocation.getVolumeTransferred();
			if (slotAllocation.getSlot().getWindowStart().isBefore(LocalDate.now())) {
				summary.delivered++;
			}
			summary.avgPrice += slotAllocation.getPrice();
		});
		summary.avgCargoSize = summary.getTonnes() / summary.getCargoes();
		summary.avgPrice = summary.getAvgPrice() / summary.getCargoes();
		summary.currency = "USD";
		summary.sizeUnit = "M3";
		return summary;
	}

	private static void jsonOutput(KSummaryModel kSummaryModel) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), kSummaryModel);
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
