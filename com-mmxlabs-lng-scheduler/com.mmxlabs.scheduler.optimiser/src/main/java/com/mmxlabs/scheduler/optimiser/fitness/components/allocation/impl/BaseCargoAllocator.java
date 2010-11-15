/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocationProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * Base class for allocating load/discharge volumes; doesn't implement the solve() method,
 * but does do various book-keeping tasks.
 * 
 * @author hinton
 * 
 */
public abstract class BaseCargoAllocator<T> implements ICargoAllocator<T> {
	final double[] unitPrices = new double[] {};

	// TODO index these types
	final Map<IPortSlot, Integer> variableTable = new HashMap<IPortSlot, Integer>();

	@SuppressWarnings("unchecked")
	final Pair<ILoadSlot, IDischargeSlot> cargoes[] = new Pair[] {};

	int cargoIndex = 0;

	ICargoAllocationProvider<T> cargoAllocationProvider;
	protected long[] forcedLoadVolume;
	protected long[] vesselCapacity;

	private long[] allocation;

	private long profit;

	public BaseCargoAllocator() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#init()
	 */
	@Override
	public void init() {

		if (cargoAllocationProvider == null) {
			throw new RuntimeException("Cargo allocation provider must be set");
		}
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#reset()
	 */
	@Override
	public void reset() {
		cargoIndex = 0;
		Arrays.fill(unitPrices, 0);
		variableTable.clear();
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator#addCargo(com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails, com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails, int, int, long, long)
	 */
	@Override
	public void addCargo(final PortDetails loadDetails,
			final PortDetails dischargeDetails, final int loadTime,
			final int dischargeTime, final long requiredLoadVolume,
			final long vesselCapacity) {

		final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getPortSlot();
		final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails
				.getPortSlot();

		cargoes[cargoIndex].setBoth(loadSlot, dischargeSlot);

		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoIndex;
		variableTable.put(loadSlot, ci);
		variableTable.put(dischargeSlot, ci);

		// We have to load this much LNG no matter what
		forcedLoadVolume[cargoIndex] = requiredLoadVolume;

		this.vesselCapacity[cargoIndex] = vesselCapacity;

		final int cargoCVValue = loadSlot.getCargoCVValue();
		final int dischargeM3Price = (int) Calculator.multiply(dischargeSlot.getSalesPriceAtTime(dischargeTime),
				cargoCVValue);
		final int loadM3Price = (int) Calculator
				.multiply(loadSlot.getPurchasePriceAtTime(loadTime), cargoCVValue);
		
		unitPrices[cargoIndex] = dischargeM3Price - loadM3Price;
		
		cargoIndex++;
	}

	/**
	 * Get the variable associated with this slot (load or discharge) in the
	 * current run.
	 * 
	 * @param slot
	 * @return
	 */
	protected final int variableForSlot(final IPortSlot slot) {
		final Integer i = variableTable.get(slot);
		return i == null ? -1 : i.intValue();
	}
	
	protected abstract long[] allocateSpareVolume();
	
	public void solve() {
		this.allocation = allocateSpareVolume();
		//convolve for p&l
		
		this.profit = 0;
		for (int i = 0; i<allocation.length; i++) {
			profit += unitPrices[i] * allocation[i];
		}
	}
	
	public long getProfit() {
		return profit;
	}
	
	public long getAllocation(final IPortSlot slot) {
		final int index = variableForSlot(slot);
		if (index == -1) return 0;
		return allocation[index];
	}
}
