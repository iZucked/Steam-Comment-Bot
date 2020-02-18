/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.common.paperdeals.BasicPaperDealAllocationEntry;
import com.mmxlabs.common.paperdeals.BasicPaperDealData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Represents a solution undergoing evaluation as a list of lists of voyageplans, start times, resources etc.
 * 
 * Also stores information about cargo allocations and load prices, once they have been filled in and updated.
 * 
 * @author hinton, modified by FM
 * 
 */
public class ProfitAndLossSequences {

	private final Map<IPortSlot, HeelValueRecord> heelValueRecords = new HashMap<>();
	private final Map<IPortSlot, OptimiserExposureRecords> exposureRecords = new HashMap<>();
	private final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealRecords = new HashMap<>();
	private final Map<IPortSlot, Long> unusedSlotGroupValue = new HashMap<>();
	private final Map<VoyagePlan, Long> voyagePlanGroupValue = new HashMap<>();

	private final @NonNull VolumeAllocatedSequences volumeAllocatedSequences;
	private final Map<VoyagePlan, CargoValueAnnotation> planToValueAnnotation = new HashMap<>();

	public static class HeelValueRecord {
		private final long heelRevenue;
		private final long heelCost;
		private final int revenueUnitPrice;
		private final int costUnitPrice;

		public HeelValueRecord(final long cost, final int costUnitPrice, final long revenue, final int revenueUnitPrice) {
			this.heelCost = cost;
			this.costUnitPrice = costUnitPrice;
			this.heelRevenue = revenue;
			this.revenueUnitPrice = revenueUnitPrice;
		}

		public int getRevenueUnitPrice() {
			return revenueUnitPrice;
		}

		public int getCostUnitPrice() {
			return costUnitPrice;
		}

		public long getHeelRevenue() {
			return heelRevenue;
		}

		public long getHeelCost() {
			return heelCost;
		}

		public static HeelValueRecord withRevenue(final long revenue, final int unitPrice) {
			return new HeelValueRecord(0, 0, revenue, unitPrice);

		}

		public static HeelValueRecord withCost(final long cost, final int unitPrice) {
			return new HeelValueRecord(cost, unitPrice, 0, 0);
		}

		public static HeelValueRecord merge(final HeelValueRecord a, final HeelValueRecord b) {
			final long cost = a.getHeelCost() + b.getHeelCost();
			final long revenue = a.getHeelRevenue() + b.getHeelRevenue();
			final int costUnitPrice = a.getCostUnitPrice() + b.getCostUnitPrice();
			final int revenueUnitPrice = a.getRevenueUnitPrice() + b.getRevenueUnitPrice();

			// Only expect one heel price to be present at a time.
			assert a.getCostUnitPrice() == costUnitPrice || b.getCostUnitPrice() == costUnitPrice;
			assert a.getRevenueUnitPrice() == revenueUnitPrice || b.getRevenueUnitPrice() == revenueUnitPrice;

			return new HeelValueRecord(cost, costUnitPrice, revenue, revenueUnitPrice);
		}
	}
	
	public static class OptimiserExposureRecords {
		public final List<BasicExposureRecord> records = new ArrayList<>();
	}

	public ProfitAndLossSequences(final @NonNull VolumeAllocatedSequences volumeAllocatedSequences) {
		this.volumeAllocatedSequences = volumeAllocatedSequences;
	}

	public void setUnusedSlotGroupValue(@NonNull final IPortSlot portSlot, final long groupValue) {
		unusedSlotGroupValue.put(portSlot, groupValue);
	}

	public long getUnusedSlotGroupValue(@NonNull final IPortSlot portSlot) {
		if (unusedSlotGroupValue.containsKey(portSlot)) {
			return unusedSlotGroupValue.get(portSlot);
		}
		return 0L;
	}

	public void setVoyagePlanGroupValue(@NonNull final VoyagePlan plan, final long groupValue) {
		voyagePlanGroupValue.put(plan, groupValue);
	}

	public long getVoyagePlanGroupValue(@NonNull final VoyagePlan plan) {
		if (voyagePlanGroupValue.containsKey(plan)) {
			return voyagePlanGroupValue.get(plan);
		}
		return 0L;

	}

	public @NonNull VolumeAllocatedSequences getVolumeAllocatedSequences() {
		return volumeAllocatedSequences;
	}

	public void setCargoValueAnnotation(final VoyagePlan plan, final CargoValueAnnotation cargoValueAnnotation) {
		planToValueAnnotation.put(plan, cargoValueAnnotation);
	}

	public CargoValueAnnotation getCargoValueAnnotation(final VoyagePlan plan) {
		return planToValueAnnotation.get(plan);
	}

	public void mergeHeelValueRecord(final IPortSlot slot, final HeelValueRecord record) {
		heelValueRecords.merge(slot, record, HeelValueRecord::merge);
	}

	public HeelValueRecord getPortHeelRecord(final IPortSlot slot) {
		return heelValueRecords.getOrDefault(slot, new HeelValueRecord(0, 0, 0, 0));
	}
	
	public void addPortExposureRecord(final IPortSlot slot, final List<BasicExposureRecord> records) {
		if (exposureRecords.get(slot) != null) {
			final OptimiserExposureRecords existingRecords = exposureRecords.get(slot);
			existingRecords.records.addAll(records);
		} else {
			final OptimiserExposureRecords newRecords = new OptimiserExposureRecords();
			newRecords.records.addAll(records);
			exposureRecords.put(slot, newRecords);
		}
	}
	
	public OptimiserExposureRecords getPortExposureRecord(final IPortSlot slot) {
		return exposureRecords.get(slot);
	}
	
	public Map<IPortSlot, OptimiserExposureRecords> getPortExposureRecords(){
		return exposureRecords;
	}
	
	public void setPaperDealRecords(Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealRecords) {
		this.paperDealRecords.clear();
		this.paperDealRecords.putAll(paperDealRecords);
	}
	
	public  List<BasicPaperDealAllocationEntry> getPaperDealAllocationEntries(final BasicPaperDealData paperDeal){
		return paperDealRecords.get(paperDeal);
	}
	
	public  Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> getPaperDealRecords(){
		return paperDealRecords;
	}
}
