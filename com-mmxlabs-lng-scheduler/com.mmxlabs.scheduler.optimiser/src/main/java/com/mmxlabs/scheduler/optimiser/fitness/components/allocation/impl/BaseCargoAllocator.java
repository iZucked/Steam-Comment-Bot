/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Base class for allocating load/discharge volumes; doesn't implement the
 * solve() method, but does do various book-keeping tasks.
 * 
 * @author hinton
 * 
 */
public abstract class BaseCargoAllocator<T> implements ICargoAllocator<T> {
	ITotalVolumeLimitProvider<T> cargoAllocationProvider;
	// TODO the following could all probably be replaced with something faster
	/**
	 * Maps from slots to cargo indices (LP variables, in the LP)
	 */
	final Map<IPortSlot, Integer> variableTable = new HashMap<IPortSlot, Integer>();
	/**
	 * Maps from slots to arrival times; subclasses need this to determine
	 * whether a slot lies in a given gas year.
	 */
	final Map<IPortSlot, Integer> slotTimes = new HashMap<IPortSlot, Integer>();

	/**
	 * Contains the quantity of LNG which <em>must</em> be loaded for a given
	 * cargo (by cargo index/LP variable, see {@link #variableTable})
	 */
	final ArrayList<Long> forcedLoadVolume = new ArrayList<Long>();
	/**
	 * Contains the capacity of the vessel carrying the cargo, by cargo index.
	 */
	final ArrayList<Long> vesselCapacity = new ArrayList<Long>();
	/**
	 * Contains the unit price (difference between sales price and purchase
	 * price) for each cargo, by cargo index.
	 */
	final ArrayList<Integer> unitPrices = new ArrayList<Integer>();

	/**
	 * The load slot for each cargo
	 */
	final ArrayList<ILoadSlot> loadSlots = new ArrayList<ILoadSlot>();
	/**
	 * The discharge slot for each cargo.
	 */
	final ArrayList<IDischargeSlot> dischargeSlots = new ArrayList<IDischargeSlot>();

	final ArrayList<Integer> loadPrices = new ArrayList<Integer>();
	final ArrayList<Integer> dischargePrices = new ArrayList<Integer>();

	int cargoCount;

	private long[] allocation;

	private long profit;

	public BaseCargoAllocator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #init()
	 */
	@Override
	public void init() {
		if (cargoAllocationProvider == null) {
			throw new RuntimeException("Cargo allocation provider must be set");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #reset()
	 */
	@Override
	public void reset() {
		cargoCount = 0;

		variableTable.clear();
		slotTimes.clear();
		unitPrices.clear();
		loadSlots.clear();
		dischargeSlots.clear();
		vesselCapacity.clear();
		forcedLoadVolume.clear();

		loadPrices.clear();
		dischargePrices.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #addCargo(com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails,
	 * com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails, int, int, long,
	 * long)
	 */
	@Override
	public void addCargo(final VoyagePlan plan, final PortDetails loadDetails,
			final VoyageDetails ladenLeg, final PortDetails dischargeDetails,
			final VoyageDetails ballastLeg, final int loadTime,
			final int dischargeTime, final long requiredLoadVolume,
			final IVesselClass vesselClass) {
		final long vesselCapacity = vesselClass.getCargoCapacity();
		final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getPortSlot();
		final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails
				.getPortSlot();

		slotTimes.put(loadSlot, loadTime);
		slotTimes.put(dischargeSlot, dischargeTime);

		loadSlots.add(loadSlot);
		dischargeSlots.add(dischargeSlot);

		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoCount;
		variableTable.put(loadSlot, ci);
		variableTable.put(dischargeSlot, ci);

		// We have to load this much LNG no matter what
		forcedLoadVolume.add(requiredLoadVolume);
		this.vesselCapacity.add(vesselCapacity);

		final int cargoCVValue = loadSlot.getCargoCVValue();

		// compute purchase price from contract
		// this is not ideal.
		final int dischargeCVPrice = dischargeSlot
				.getSalesPriceAtTime(dischargeTime);
		final long maximumDischargeVolume = Math.min(vesselCapacity
				- requiredLoadVolume, loadSlot.getMaxLoadVolume()
				- requiredLoadVolume);

		// TODO this value is incorrect for netback and profit sharing cases
		// the load CV price is the notional maximum price
		// if we load less, it might actually be worth less

		final int loadCVPrice = loadSlot.getLoadPriceCalculator()
				.calculateLoadUnitPrice(loadTime, (int) maximumDischargeVolume,
						dischargeTime, dischargeCVPrice, cargoCVValue,
						ladenLeg, ballastLeg, vesselClass);

		final int dischargeM3Price = (int) Calculator.multiply(
				dischargeCVPrice, cargoCVValue);
		final int loadM3Price = (int) Calculator.multiply(loadCVPrice,
				cargoCVValue);

		loadPrices.add(loadM3Price);
		dischargePrices.add(dischargeM3Price);

		this.unitPrices.add(dischargeM3Price - loadM3Price);

		cargoCount++;
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
		// convolve for p&l

		this.profit = 0;
		for (int i = 0; i < allocation.length; i++) {
			profit += Calculator.multiply(unitPrices.get(i), allocation[i]);
		}
	}

	public long getProfit() {
		return profit / Calculator.ScaleFactor; // why?
	}

	public long getAllocation(final IPortSlot slot) {
		final int index = variableForSlot(slot);
		if (index == -1)
			return 0;
		return allocation[index];
	}

	@Override
	public void setTotalVolumeLimitProvider(
			final ITotalVolumeLimitProvider<T> tvlp) {
		cargoAllocationProvider = tvlp;
	}

	@Override
	public Iterable<IAllocationAnnotation> getAllocations() {
		return new Iterable<IAllocationAnnotation>() {
			@Override
			public Iterator<IAllocationAnnotation> iterator() {
				return new Iterator<IAllocationAnnotation>() {
					final Iterator<ILoadSlot> loadIterator = loadSlots
							.iterator();
					final Iterator<IDischargeSlot> dischargeIterator = dischargeSlots
							.iterator();
					final Iterator<Integer> priceIterator = unitPrices
							.iterator();
					int allocationIndex;

					@Override
					public boolean hasNext() {
						return loadIterator.hasNext()
								&& dischargeIterator.hasNext()
								&& priceIterator.hasNext();
					}

					@Override
					public IAllocationAnnotation next() {
						final AllocationAnnotation annotation = new AllocationAnnotation();

						final ILoadSlot loadSlot = loadIterator.next();
						final IDischargeSlot dischargeSlot = dischargeIterator
								.next();

						annotation.setLoadSlot(loadSlot);
						annotation.setDischargeSlot(dischargeSlot);
						annotation.setFuelVolume(forcedLoadVolume
								.get(allocationIndex));

						// TODO recompute load price here; this is not necessarily right
						annotation.setLoadM3Price(loadPrices
								.get(allocationIndex));
						annotation.setDischargeM3Price(dischargePrices
								.get(allocationIndex));
						annotation.setLoadTime(slotTimes.get(loadSlot));
						annotation.setDischargeTime(slotTimes
								.get(dischargeSlot));
						annotation
								.setDischargeVolume(allocation[allocationIndex++]);

						return annotation;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}

				};
			}
		};
	}
}
