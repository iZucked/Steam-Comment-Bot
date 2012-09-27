/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VirtualCargo;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Base class for allocating load/discharge volumes; doesn't implement the solve() method, but does do various book-keeping tasks.
 * 
 * @author hinton
 * 
 */
public abstract class BaseCargoAllocator implements ICargoAllocator {
	
	@Inject
	ITotalVolumeLimitProvider cargoAllocationProvider;
	// TODO the following could all probably be replaced with something faster
	/**
	 * Maps from slots to cargo indices (LP variables, in the LP)
	 */
	final Map<IPortSlot, Integer> variableTable = new HashMap<IPortSlot, Integer>();
	/**
	 * Maps from slots to arrival times; subclasses need this to determine whether a slot lies in a given gas year.
	 */
	final Map<IPortSlot, Integer> slotTimes = new HashMap<IPortSlot, Integer>();

	/**
	 * Contains the quantity of LNG which <em>must</em> be loaded for a given cargo (by cargo index/LP variable, see {@link #variableTable})
	 */
	final ArrayList<Long> forcedLoadVolume = new ArrayList<Long>();
	/**
	 * Contains the capacity of the vessel carrying the cargo, by cargo index.
	 */
	final ArrayList<Long> vesselCapacity = new ArrayList<Long>();
	/**
	 * Contains the unit price (difference between sales price and purchase price) for each cargo, by cargo index.
	 */
	final ArrayList<Integer> unitPrices = new ArrayList<Integer>();

	/**
	 * The load slot for each cargo
	 */
	final ArrayList<ILoadOption> loadSlots = new ArrayList<ILoadOption>();
	/**
	 * The discharge slot for each cargo.
	 */
	final ArrayList<IDischargeOption> dischargeSlots = new ArrayList<IDischargeOption>();

	final ArrayList<Integer> loadPrices = new ArrayList<Integer>();
	final ArrayList<Integer> dischargePrices = new ArrayList<Integer>();

	int cargoCount;

	private long[] allocation;

	private long profit;
	
	@Inject
	private IVesselProvider vesselProvider;

	public BaseCargoAllocator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator #init()
	 */
	@Override
	public void init() {
		if (cargoAllocationProvider == null) {
			throw new RuntimeException("Cargo allocation provider must be set");
		}
	}

	@Override
	public void setVesselProvider(final IVesselProvider dataComponentProvider) {
		this.vesselProvider = dataComponentProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator #reset()
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

	@Override
	public Collection<IAllocationAnnotation> allocate(final ScheduledSequences sequences) {
		reset();

		final VoyagePlanIterator planIterator = new VoyagePlanIterator();
		for (final ScheduledSequence sequence : sequences) {
			planIterator.setVoyagePlans(sequence.getVoyagePlans(), sequence.getStartTime());
			final IVessel vessel = vesselProvider.getVessel(sequence.getResource());

			PortDetails loadDetails = null;
			PortDetails dischargeDetails = null;
			VoyageDetails ladenVoyage = null;
			VoyageDetails ballastVoyage = null;
			VoyagePlan plan = null;

			int loadTime = 0, dischargeTime = 0;

			while (planIterator.hasNextObject()) {
				final Object object;
				if (planIterator.nextObjectIsStartOfPlan()) {
					object = planIterator.nextObject();
					plan = planIterator.getCurrentPlan();
				} else {
					object = planIterator.nextObject();
				}
				if (object instanceof PortDetails) {
					final PortDetails pd = (PortDetails) object;
					if (pd.getPortSlot() instanceof ILoadOption) {
						loadDetails = pd;
						loadTime = planIterator.getCurrentTime();
					} else if (pd.getPortSlot() instanceof IDischargeOption) {
						dischargeDetails = pd;
						dischargeTime = planIterator.getCurrentTime();
					}
				} else if (object instanceof VoyageDetails) {
					if ((dischargeDetails == null) && (loadDetails != null)) {
						ladenVoyage = (VoyageDetails) object;
					} else if ((dischargeDetails != null) && (loadDetails != null)) {
						ballastVoyage = (VoyageDetails) object;
						assert plan != null;
						addCargo(plan, loadDetails, ladenVoyage, dischargeDetails, ballastVoyage, loadTime, dischargeTime, plan.getLNGFuelVolume(), vessel);
						loadDetails = null;
						dischargeDetails = null;
					}
				}
			}
			if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
				if (loadDetails != null && dischargeDetails != null) {
					addVirtualCargo(loadDetails, dischargeDetails);
				}
			}

		}

		// TODO: Do we use this!
		for (final VirtualCargo virtual : sequences.getVirtualCargoes()) {
			// add virtual cargo, somehow; contracts need to know how to price this, because we need the load price.
			addVirtualCargo(virtual);

		}

		solve();
		final LinkedList<IAllocationAnnotation> result = new LinkedList<IAllocationAnnotation>();
		for (final IAllocationAnnotation aa : getAllocations()) {
			result.add(aa);
		}
		return result;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public IAllocationAnnotation allocate(final IVessel vessel, final VoyagePlan plan, int[] arrivalTimes) {

		PortDetails loadDetails = null;
		PortDetails dischargeDetails = null;
		VoyageDetails ladenVoyage = null;
		VoyageDetails ballastVoyage = null;

		ILoadOption loadSlot = null;
		IDischargeOption dischargeSlot = null;

		long forcedLoadVolume = plan.getLNGFuelVolume();
		int loadTime = 0, dischargeTime = 0;

		int dischargeM3Price = 0;
		int loadM3Price = 0;
		long maximumDischargeVolume = 0;
		for (Object object : plan.getSequence()) {
			if (object instanceof PortDetails) {
				final PortDetails pd = (PortDetails) object;
				if (pd.getPortSlot() instanceof ILoadOption) {
					loadDetails = pd;
					loadTime = arrivalTimes[0];
				} else if (pd.getPortSlot() instanceof IDischargeOption) {
					dischargeDetails = pd;
					dischargeTime = arrivalTimes[3];
				}
			} else if (object instanceof VoyageDetails) {
				if ((dischargeDetails == null) && (loadDetails != null)) {
					ladenVoyage = (VoyageDetails) object;
				} else if ((dischargeDetails != null) && (loadDetails != null)) {
					ballastVoyage = (VoyageDetails) object;
					assert plan != null;

					{

						IVesselClass vesselClass = vessel.getVesselClass();
						final long vesselCapacity = vesselClass.getCargoCapacity();
						loadSlot = (ILoadSlot) loadDetails.getPortSlot();
						dischargeSlot = (IDischargeSlot) dischargeDetails.getPortSlot();

						// We have to load this much LNG no matter what

						final int cargoCVValue = loadSlot.getCargoCVValue();

						// compute purchase price from contract
						// this is not ideal.
						final int dischargeCVPrice = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, dischargeTime);
						long maxLoadVolume = loadSlot.getMaxLoadVolume();
						if (maxLoadVolume == 0) {
							maxLoadVolume = vesselCapacity;
						}
						maximumDischargeVolume = Math.min(vesselCapacity - forcedLoadVolume, maxLoadVolume - forcedLoadVolume);

						// TODO this value is incorrect for netback and profit sharing cases
						// the load CV price is the notional maximum price
						// if we load less, it might actually be worth less

						final int loadCVPrice = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice((ILoadSlot) loadSlot, (IDischargeSlot) dischargeSlot, loadTime, dischargeTime,
								dischargeCVPrice, (int) maximumDischargeVolume, vessel, plan, null);

						dischargeM3Price = (int) Calculator.multiply(dischargeCVPrice, cargoCVValue);
						loadM3Price = (int) Calculator.multiply(loadCVPrice, cargoCVValue);

					}
					addCargo(plan, loadDetails, ladenVoyage, dischargeDetails, ballastVoyage, loadTime, dischargeTime, plan.getLNGFuelVolume(), vessel);
					loadDetails = null;
					dischargeDetails = null;

				}
			}
		}

		if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
			if (loadDetails != null && dischargeDetails != null) {
				boolean isFOB = false;
				boolean isDES = false;

				loadSlot = (ILoadOption) loadDetails.getPortSlot();
				dischargeSlot = (IDischargeOption) dischargeDetails.getPortSlot();

				if (loadSlot instanceof ILoadSlot) {
					isFOB = true;
				}
				if (dischargeSlot instanceof IDischargeSlot) {
					isDES = true;
				}

				assert isDES != isFOB;

				final int time;

				if (isFOB) {
					// Pick start of window.
					time = ((ILoadSlot) loadSlot).getTimeWindow().getStart();
				} else {
					time = ((IDischargeSlot) dischargeSlot).getTimeWindow().getStart();
				}

				forcedLoadVolume = 0;

				final int cargoCVValue = loadSlot.getCargoCVValue();

				// compute purchase price from contract
				// this is not ideal.
				final int dischargeCVPrice = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, time);

				// TODO this value is incorrect for netback and profit sharing cases
				// the load CV price is the notional maximum price
				// if we load less, it might actually be worth less

				final int loadCVPrice = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice(loadSlot, dischargeSlot, time, time, dischargeCVPrice, null);
				dischargeM3Price = (int) Calculator.multiply(dischargeCVPrice, cargoCVValue);
				loadM3Price = (int) Calculator.multiply(loadCVPrice, cargoCVValue);

				maximumDischargeVolume = Math.max(loadSlot.getMaxLoadVolume(), dischargeSlot.getMaxDischargeVolume());

			}
		}

		final AllocationAnnotation annotation = new AllocationAnnotation();

		annotation.setLoadSlot(loadSlot);
		annotation.setDischargeSlot(dischargeSlot);
		annotation.setFuelVolume(forcedLoadVolume);

		// TODO recompute load price here; this is not necessarily right
		annotation.setLoadM3Price(loadM3Price);
		annotation.setDischargeM3Price(dischargeM3Price);
		annotation.setLoadTime(loadTime);
		annotation.setDischargeTime(dischargeTime);
		annotation.setDischargeVolume(maximumDischargeVolume);

		return annotation;
	}

	@Override
	public void dispose() {
		reset();
	}

	public void addCargo(final VoyagePlan plan, final PortDetails loadDetails, final VoyageDetails ladenLeg, final PortDetails dischargeDetails, final VoyageDetails ballastLeg, final int loadTime,
			final int dischargeTime, final long requiredLoadVolume, final IVessel vessel) {
		// if (requiredLoadVolume > vesselClass.getCargoCapacity() / 10) {
		// System.err.println("Using a whole lot of gas for fuel here");
		// }

		IVesselClass vesselClass = vessel.getVesselClass();
		final long vesselCapacity = vesselClass.getCargoCapacity();
		final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getPortSlot();
		final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getPortSlot();

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
		final int dischargeCVPrice = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, dischargeTime);
		long maxLoadVolume = loadSlot.getMaxLoadVolume();
		if (maxLoadVolume == 0) {
			maxLoadVolume = vesselCapacity;
		}
		final long maximumDischargeVolume = Math.min(vesselCapacity - requiredLoadVolume, maxLoadVolume - requiredLoadVolume);

		// TODO this value is incorrect for netback and profit sharing cases
		// the load CV price is the notional maximum price
		// if we load less, it might actually be worth less

		final int loadCVPrice = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice(loadSlot, dischargeSlot, loadTime, dischargeTime, dischargeCVPrice, (int) maximumDischargeVolume, vessel,
				plan, null);

		final int dischargeM3Price = (int) Calculator.multiply(dischargeCVPrice, cargoCVValue);
		final int loadM3Price = (int) Calculator.multiply(loadCVPrice, cargoCVValue);

		loadPrices.add(loadM3Price);
		dischargePrices.add(dischargeM3Price);

		this.unitPrices.add(dischargeM3Price - loadM3Price);

		cargoCount++;
	}

	public void addVirtualCargo(final PortDetails loadDetails, PortDetails dischargeDetails) {

		final ILoadOption loadSlot = (ILoadOption) loadDetails.getPortSlot();
		final IDischargeOption dischargeSlot = (IDischargeOption) dischargeDetails.getPortSlot();

		addVirtualCargo(loadSlot, dischargeSlot);
	}

	public void addVirtualCargo(final VirtualCargo cargo) {

		final ILoadOption loadSlot = cargo.getLoadOption();
		final IDischargeOption dischargeSlot = cargo.getDischargeOption();
		addVirtualCargo(loadSlot, dischargeSlot);
	}

	public void addVirtualCargo(final ILoadOption loadSlot, final IDischargeOption dischargeSlot) {
		boolean isFOB = false;
		boolean isDES = false;

		if (loadSlot instanceof ILoadSlot) {
			isFOB = true;
		}
		if (dischargeSlot instanceof IDischargeSlot) {
			isDES = true;
		}

		assert isDES != isFOB;

		final int time;

		if (isFOB) {
			// Pick start of window.
			time = ((ILoadSlot) loadSlot).getTimeWindow().getStart();
		} else {
			time = ((IDischargeSlot) dischargeSlot).getTimeWindow().getStart();
		}

		slotTimes.put(loadSlot, time);
		slotTimes.put(dischargeSlot, time);

		loadSlots.add(loadSlot);
		dischargeSlots.add(dischargeSlot);

		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoCount;
		variableTable.put(loadSlot, ci);
		variableTable.put(dischargeSlot, ci);

		// We have to load this much LNG no matter what
		forcedLoadVolume.add(0l);
		this.vesselCapacity.add(Long.MAX_VALUE);

		final int cargoCVValue = loadSlot.getCargoCVValue();

		// compute purchase price from contract
		// this is not ideal.
		final int dischargeCVPrice = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, time);

		// TODO this value is incorrect for netback and profit sharing cases
		// the load CV price is the notional maximum price
		// if we load less, it might actually be worth less

		final int loadCVPrice = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice(loadSlot, dischargeSlot, time, time, dischargeCVPrice, null);

		final int dischargeM3Price = (int) Calculator.multiply(dischargeCVPrice, cargoCVValue);
		final int loadM3Price = (int) Calculator.multiply(loadCVPrice, cargoCVValue);

		loadPrices.add(loadM3Price);
		dischargePrices.add(dischargeM3Price);

		this.unitPrices.add(dischargeM3Price - loadM3Price);

		cargoCount++;
	}

	/**
	 * Get the variable associated with this slot (load or discharge) in the current run.
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
		if (index == -1) {
			return 0;
		}
		return allocation[index];
	}

	@Override
	public void setTotalVolumeLimitProvider(final ITotalVolumeLimitProvider tvlp) {
		cargoAllocationProvider = tvlp;
	}

	public Iterable<IAllocationAnnotation> getAllocations() {
		return new Iterable<IAllocationAnnotation>() {
			@Override
			public Iterator<IAllocationAnnotation> iterator() {
				return new Iterator<IAllocationAnnotation>() {
					final Iterator<ILoadOption> loadIterator = loadSlots.iterator();
					final Iterator<IDischargeOption> dischargeIterator = dischargeSlots.iterator();
					final Iterator<Integer> priceIterator = unitPrices.iterator();
					int allocationIndex;

					@Override
					public boolean hasNext() {
						return loadIterator.hasNext() && dischargeIterator.hasNext() && priceIterator.hasNext();
					}

					@Override
					public IAllocationAnnotation next() {
						final AllocationAnnotation annotation = new AllocationAnnotation();

						final ILoadOption loadSlot = loadIterator.next();
						final IDischargeOption dischargeSlot = dischargeIterator.next();

						annotation.setLoadSlot(loadSlot);
						annotation.setDischargeSlot(dischargeSlot);
						annotation.setFuelVolume(forcedLoadVolume.get(allocationIndex));

						// TODO recompute load price here; this is not necessarily right
						annotation.setLoadM3Price(loadPrices.get(allocationIndex));
						annotation.setDischargeM3Price(dischargePrices.get(allocationIndex));
						annotation.setLoadTime(slotTimes.get(loadSlot));
						annotation.setDischargeTime(slotTimes.get(dischargeSlot));
						annotation.setDischargeVolume(allocation[allocationIndex++]);

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
