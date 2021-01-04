/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossRecorder;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotCountConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * Data structures for performing a long term optimisation
 * 
 * @author achurchill
 *
 */
public class PairingOptimisationData<P,C> implements ProfitAndLossRecorder {
	@Inject
	private IPhaseOptimisationData phaseOptimisationData;
	@Inject
	private IPortSlotProvider portSlotProvider;
	@Inject
	private IMaxSlotCountConstraintDataProvider maxSlotCountConstraint;

	Long[][] profit;
	private boolean[][] valid;
	private List<ILoadOption> sortedLoads;
	private List<IDischargeOption> sortedDischarges;
	Map<ILoadOption, Integer> loadMap = new HashMap<>();
	Map<IDischargeOption, Integer> dischargeMap = new HashMap<>();
	private boolean[] optionalLoads;
	private boolean[] optionalDischarges;

	private List<Map<String, List<Integer>>> maxDischargeGroupCount;
	private List<Map<String, List<Integer>>> minDischargeGroupCount;
	private List<Map<String, List<Integer>>> maxLoadGroupCount;
	private List<Map<String, List<Integer>>> minLoadGroupCount;

	public PairingOptimisationData() {

	}

	public void init(final List<ILoadOption> loadsUnsorted, final List<IDischargeOption> dischargesUnsorted) {
		// sort slots
		this.setSortedLoads(new LinkedList<>(loadsUnsorted));
		this.setSortedDischarges(new LinkedList<>(dischargesUnsorted));
		{
			int index = 0;
			for (final ILoadOption iLoadOption : getSortedLoads()) {
				loadMap.put(iLoadOption, index++);
			}
		}
		{
			int index = 0;
			for (final IDischargeOption iDischargeOption : getSortedDischarges()) {
				dischargeMap.put(iDischargeOption, index++);
			}
		}
		this.profit = new Long[getSortedLoads().size()][getSortedDischarges().size()];
		this.valid = new boolean[getSortedLoads().size()][getSortedDischarges().size()];

		setOptionalLoads();
		setOptionalDischarges();
		setContraints();
		setMaxDischargeGroupCount();
		setMinDischargeGroupCount();
		setMaxLoadGroupCount();
		setMinLoadGroupCount();
	}
	
	@Override
	public void record(@NonNull final ILoadOption load, @NonNull final IDischargeOption discharge, final long profitAndLoss) {
		profit[loadMap.get(load)][dischargeMap.get(discharge)] = profitAndLoss;
	}

	/**
	 * Finds the allocated discharge for a given load
	 * 
	 * @param load
	 * @param allocations
	 * @return
	 */
	public IDischargeOption getPairedDischarge(ILoadOption load, boolean[][] allocations) {
		for (int i = 0; i < allocations[loadMap.get(load)].length; i++) {
			if (allocations[loadMap.get(load)][i] == true) {
				return getSortedDischarges().get(i);
			}
		}
		return null;
	}

	public Long[][] getProfit() {
		return profit;
	}

	public boolean[] getOptionalLoads() {
		return optionalLoads;
	}

	public void setOptionalLoads(boolean[] optionalLoads) {
		this.optionalLoads = optionalLoads;
	}

	public boolean[] getOptionalDischarges() {
		return optionalDischarges;
	}

	private void setOptionalLoads() {
		optionalLoads = new boolean[this.getSortedLoads().size()];
		for (int i = 0; i < getSortedLoads().size(); i++) {
			optionalLoads[i] = phaseOptimisationData.isElementOptional(portSlotProvider.getElement(getSortedLoads().get(i)))
					&& !phaseOptimisationData.getSoftRequiredElements().contains(portSlotProvider.getElement(getSortedLoads().get(i)));
		}
	}

	private void setOptionalDischarges() {
		optionalDischarges = new boolean[this.getSortedDischarges().size()];
		for (int i = 0; i < getSortedDischarges().size(); i++) {
			optionalDischarges[i] = phaseOptimisationData.isElementOptional(portSlotProvider.getElement(getSortedDischarges().get(i)))
					&& !phaseOptimisationData.getSoftRequiredElements().contains(portSlotProvider.getElement(getSortedDischarges().get(i)));
		}
	}

	public void setOptionalDischarges(boolean[] optionalDischarges) {
		this.optionalDischarges = optionalDischarges;
	}

	/**
	 * For a given profit array, create a constraints array from null values
	 */
	public void setContraints() {
		for (int i = 0; i < profit.length; i++) {
			for (int j = 0; j < profit[i].length; j++) {
				if (profit[i][j] == null) {
					valid[i][j] = false;
				} else {
					valid[i][j] = true;
				}
			}
		}
	}

	public long[][] getProfitAsPrimitive() {
		if (profit.length == 0) {
			throw new UserFeedbackException("Please generate slots for purchase and sales contracts before doing ADP optimisation.");
		}
		long[][] profitL = new long[profit.length][profit[0].length];
		for (int i = 0; i < profit.length; i++) {
			for (int j = 0; j < profit[i].length; j++) {
				profitL[i][j] = profit[i][j] != null ? profit[i][j] : 0L;
			}
		}
		return profitL;
	}

	public boolean[][] getValid() {
		setContraints();
		return valid;
	}

	public void setValid(boolean[][] valid) {
		this.valid = valid;
	}

	public int getIndex(ILoadOption load) {
		return loadMap.get(load);
	}

	public int getIndex(IDischargeOption discharge) {
		return dischargeMap.get(discharge);
	}

	public List<Map<String, List<Integer>>> getMaxDischargeGroupCount() {
		return maxDischargeGroupCount;
	}

	public void setMaxDischargeGroupCount() {
		List<ConstraintInfo<P,C,IDischargeOption>> allMaxDischargeGroupCounts = maxSlotCountConstraint.getAllMaxDischargeGroupCounts();
		maxDischargeGroupCount = createJSONMapFromConstraint(allMaxDischargeGroupCounts);
	}
	
	public List<ConstraintInfo<P,C,IDischargeOption>> getMaxDischargeGroupCounts() {
		return maxSlotCountConstraint.getAllMaxDischargeGroupCounts();
	}

	public List<ConstraintInfo<P,C,IDischargeOption>> getMinDischargeGroupCounts() {
		return maxSlotCountConstraint.getAllMinDischargeGroupCounts();
	}

	public List<ConstraintInfo<P,C,ILoadOption>> getMinLoadGroupCounts() {
		return maxSlotCountConstraint.getAllMinLoadGroupCounts();
	}

	public List<ConstraintInfo<P,C,ILoadOption>> getMaxLoadGroupCounts() {
		return maxSlotCountConstraint.getAllMaxLoadGroupCounts();
	}

	public List<Map<String, List<Integer>>> getMinDischargeGroupCount() {
		return minDischargeGroupCount;
	}

	public void setMinDischargeGroupCount() {
		List<ConstraintInfo<P,C,IDischargeOption>> allMinDischargeGroupCounts = maxSlotCountConstraint.getAllMinDischargeGroupCounts();
		minDischargeGroupCount = createJSONMapFromConstraint(allMinDischargeGroupCounts);
	}

	private List<Map<String, List<Integer>>> createJSONMapFromConstraint(List<ConstraintInfo<P,C,IDischargeOption>> allMaxDischargeGroupCounts) {
		List<Map<String, List<Integer>>> dischargeCountConstraintJSON = new LinkedList<>();
		for (ConstraintInfo<P,C,IDischargeOption> entry : allMaxDischargeGroupCounts) {
			List<Integer> slots = entry.getSlots().stream().map(dis -> (Integer) getIndex(dis)).collect(Collectors.toList());
			List<Integer> count = CollectionsUtil.makeLinkedList(entry.getBound());
			
			Map<String, List<Integer>> map = new LinkedHashMap<>();
			map.put("discharges", slots);
			map.put("count", count);
			dischargeCountConstraintJSON.add(map);
		}
		return dischargeCountConstraintJSON;
	}

	public List<Map<String, List<Integer>>> getMaxLoadGroupCount() {
		return maxLoadGroupCount;
	}

	public void setMaxLoadGroupCount() {
		List<ConstraintInfo<P,C,ILoadOption>> allMaxLoadGroupCounts = maxSlotCountConstraint.getAllMaxLoadGroupCounts();
		maxLoadGroupCount = createLoadJSONMapFromConstraint(allMaxLoadGroupCounts);
	}

	public List<Map<String, List<Integer>>> getMinLoadGroupCount() {
		return minLoadGroupCount;
	}

	public void setMinLoadGroupCount() {
		List<ConstraintInfo<P,C,ILoadOption>> allMinLoadGroupCounts = maxSlotCountConstraint.getAllMinLoadGroupCounts();
		minLoadGroupCount = createLoadJSONMapFromConstraint(allMinLoadGroupCounts);
	}

	private List<Map<String, List<Integer>>> createLoadJSONMapFromConstraint(List<ConstraintInfo<P,C,ILoadOption>> allMaxLoadGroupCounts) {
		List<Map<String, List<Integer>>> dischargeCountConstraintJSON = new LinkedList<>();
		for (ConstraintInfo<P,C,ILoadOption> entry : allMaxLoadGroupCounts) {
			List<Integer> slots = entry.getSlots().stream().map(dis -> (Integer) getIndex(dis)).collect(Collectors.toList());
			List<Integer> count = CollectionsUtil.makeLinkedList(entry.getBound());
			Map<String, List<Integer>> map = new LinkedHashMap<>();
			map.put("loads", slots);
			map.put("count", count);
			dischargeCountConstraintJSON.add(map);
		}
		return dischargeCountConstraintJSON;
	}

	public List<ILoadOption> getSortedLoads() {
		return sortedLoads;
	}

	public void setSortedLoads(List<ILoadOption> sortedLoads) {
		this.sortedLoads = sortedLoads;
		this.sortedLoads.sort((a, b) -> a.getId().compareTo(b.getId()));
	}

	public List<IDischargeOption> getSortedDischarges() {
		return sortedDischarges;
	}

	public void setSortedDischarges(List<IDischargeOption> sortedDischarges) {
		this.sortedDischarges = sortedDischarges;
		this.sortedDischarges.sort((a, b) -> a.getId().compareTo(b.getId()));
	}

}
