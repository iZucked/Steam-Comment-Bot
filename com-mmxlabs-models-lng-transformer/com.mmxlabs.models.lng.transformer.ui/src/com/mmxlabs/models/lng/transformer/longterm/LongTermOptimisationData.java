/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossRecorder;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * Data structures for performing a long term optimisation
 * @author achurchill
 *
 */
public class LongTermOptimisationData implements ProfitAndLossRecorder {
	@Inject
	IOptionalElementsProvider optionalElementsProvider;
	@Inject
	IPortSlotProvider portSlotProvider;
	
	Long[][] profit;
	private boolean[][] valid;
	@NonNull private ILoadOption[] sortedLoads;
	@NonNull private IDischargeOption[] sortedDischarges;
	Map<ILoadOption, Integer> loadMap = new HashMap<>();
	Map<IDischargeOption, Integer> dischargeMap = new HashMap<>();
	private boolean[] optionalLoads;
	private boolean[] optionalDischarges;
	
	public LongTermOptimisationData() {
		
	}
	
	public void init(final List<ILoadOption> loadsUnsorted, final List<IDischargeOption> dischargesUnsorted) {

		loadsUnsorted.sort((a,b)->a.getId().compareTo(b.getId()));
		this.sortedLoads = loadsUnsorted.toArray(new ILoadOption[0]);
		dischargesUnsorted.sort((a,b)->a.getId().compareTo(b.getId()));
		this.sortedDischarges = dischargesUnsorted.toArray(new IDischargeOption[0]);
		{
			int index = 0;
			for (final ILoadOption iLoadOption : sortedLoads) {
				loadMap.put(iLoadOption, index++);
			}
		}
		{
			int index = 0;
			for (final IDischargeOption iDischargeOption : sortedDischarges) {
				dischargeMap.put(iDischargeOption, index++);
			}
		}
		this.profit = new Long[sortedLoads.length][sortedDischarges.length];
		this.valid = new boolean[sortedLoads.length][sortedDischarges.length];
		this.setOptionalLoads(new boolean[this.sortedLoads.length]);
		this.setOptionalDischarges(new boolean[this.sortedDischarges.length]);
		setOptionalLoads();
		setOptionalDischarges();
		setContraints();
	}
	
	@Override
	public void record(@NonNull final ILoadOption load, @NonNull final IDischargeOption discharge, final long profitAndLoss) {
		profit[loadMap.get(load)][dischargeMap.get(discharge)] = profitAndLoss;
	}
	
	/**
	 * Finds the allocated discharge for a given load 
	 * @param load
	 * @param allocations
	 * @return
	 */
	public IDischargeOption getPairedDischarge(ILoadOption load, boolean[][] allocations) {
		for (int i = 0; i < allocations[loadMap.get(load)].length; i++) {
			if (allocations[loadMap.get(load)][i] == true) {
				return sortedDischarges[i];
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
		for (int i = 0; i < sortedLoads.length; i++) {
			optionalLoads[i] = optionalElementsProvider.isElementOptional(portSlotProvider.getElement(sortedLoads[i])) && !optionalElementsProvider.getSoftRequiredElements().contains(portSlotProvider.getElement(sortedLoads[i]));
		}
	}

	private void setOptionalDischarges() {
		for (int i = 0; i < sortedDischarges.length; i++) {
			optionalDischarges[i] = optionalElementsProvider.isElementOptional(portSlotProvider.getElement(sortedDischarges[i])) && !optionalElementsProvider.getSoftRequiredElements().contains(portSlotProvider.getElement(sortedDischarges[i]));
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
		long[][] profitL= new long[profit.length][profit[0].length];
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
}
