/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.mmxlabs.common.Pair;
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
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan.HeelType;

/**
 * Base class for allocating load/discharge volumes; doesn't implement the solve() method, but does do various book-keeping tasks.
 * 
 * @author hinton
 * 
 */
public abstract class BaseCargoAllocator implements IVolumeAllocator {

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
	final ArrayList<Long> forcedLoadVolumeInM3 = new ArrayList<Long>();
	
	final ArrayList<Long> remainingHeelVolumeInM3 = new ArrayList<Long>();

	/**
	 * Contains the capacity of the vessel carrying the cargo, by cargo index.
	 */
	final ArrayList<Long> vesselCapacityInM3 = new ArrayList<Long>();
	/**
	 * Contains the unit price (difference between sales price and purchase price) for each cargo, by cargo index.
	 */
	final ArrayList<Integer> unitPricesPerM3 = new ArrayList<Integer>();

	/**
	 * The load slot for each cargo
	 */
	final ArrayList<ILoadOption> loadSlots = new ArrayList<ILoadOption>();
	/**
	 * The discharge slot for each cargo.
	 */
	final ArrayList<IDischargeOption> dischargeSlots = new ArrayList<IDischargeOption>();
	
	/**
	 * The list of slots for each cargo.
	 */
	final ArrayList<List<IPortSlot>> slotLists = new ArrayList<List<IPortSlot>>(); 
	
	final Map<IPortSlot, Integer> slotPricesPerM3 = new HashMap<IPortSlot, Integer>();

	final ArrayList<VoyagePlan> voyagePlans = new ArrayList<VoyagePlan>();

	//final ArrayList<Integer> loadPricesPerM3 = new ArrayList<Integer>();
	//final ArrayList<Integer> dischargePricesPerM3 = new ArrayList<Integer>();

	int cargoCount;

	private long[] allocation;

	// private long profit;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private Provider<VoyagePlanIterator> voyagePlanIteratorProvider;

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
		unitPricesPerM3.clear();
		loadSlots.clear();
		dischargeSlots.clear();
		slotLists.clear();
		vesselCapacityInM3.clear();
		forcedLoadVolumeInM3.clear();
		remainingHeelVolumeInM3.clear();

		//loadPricesPerM3.clear();
		//dischargePricesPerM3.clear();
		slotPricesPerM3.clear();
		voyagePlans.clear();
	}

	@Override
	public Map<VoyagePlan, IAllocationAnnotation> allocate(final ScheduledSequences sequences) {
		reset();

		final VoyagePlanIterator planIterator = voyagePlanIteratorProvider.get();
		for (final ScheduledSequence sequence : sequences) {
			planIterator.setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), sequence.getArrivalTimes());
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
					final IPortSlot slot = pd.getOptions().getPortSlot();
					if (slot instanceof ILoadOption) {
						loadDetails = pd;
						loadTime = planIterator.getCurrentTime();
					} else if (slot instanceof IDischargeOption) {
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
						voyagePlans.add(plan);
						loadDetails = null;
						dischargeDetails = null;
					}
				}
			}
			if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
				if (loadDetails != null && dischargeDetails != null) {
					addVirtualCargo(loadDetails, dischargeDetails);
					voyagePlans.add(plan);
				}
			}

		}

		solve();
		final Map<VoyagePlan, IAllocationAnnotation> result = new HashMap<VoyagePlan, IAllocationAnnotation>();
		for (final Pair<VoyagePlan, IAllocationAnnotation> p : getAllocations()) {
			result.put(p.getFirst(), p.getSecond());
		}
		return result;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public IAllocationAnnotation allocate(final IVessel vessel, final VoyagePlan plan, final int[] arrivalTimes) {

		PortDetails loadDetails = null;
		PortDetails dischargeDetails = null;
		VoyageDetails ladenVoyage = null;
		VoyageDetails ballastVoyage = null;

		ILoadOption loadSlot = null;
		IDischargeOption dischargeSlot = null;

		long forcedLoadVolumeInM3 = plan.getLNGFuelVolume();
		int loadTime = 0, dischargeTime = 0;

		int dischargePricePerM3 = 0;
		int loadPricePerM3 = 0;
		long maximumDischargeVolumeInM3 = 0;
		for (final Object object : plan.getSequence()) {
			if (object instanceof PortDetails) {
				final PortDetails pd = (PortDetails) object;
				final IPortSlot slot = pd.getOptions().getPortSlot();
				if (slot instanceof ILoadOption) {
					loadDetails = pd;
					loadTime = arrivalTimes[0];
				} else if (slot instanceof IDischargeOption) {
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

						final IVesselClass vesselClass = vessel.getVesselClass();
						final long vesselCapacityInM3 = vesselClass.getCargoCapacity();
						loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
						dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

						// We have to load this much LNG no matter what

						final int cargoCVValue = loadSlot.getCargoCVValue();

						// compute purchase price from contract
						// this is not ideal.
						final int dischargePricePerMMBTu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, dischargeTime);
						long maxLoadVolumeInM3 = loadSlot.getMaxLoadVolume();
						if (maxLoadVolumeInM3 == 0) {
							maxLoadVolumeInM3 = vesselCapacityInM3;
						}
						maximumDischargeVolumeInM3 = Math.min(vesselCapacityInM3 - forcedLoadVolumeInM3, maxLoadVolumeInM3 - forcedLoadVolumeInM3);

						// TODO this value is incorrect for netback and profit sharing cases
						// the load CV price is the notional maximum price
						// if we load less, it might actually be worth less

						final long loadVolumeInM3 = maximumDischargeVolumeInM3 + forcedLoadVolumeInM3;
						final int loadPricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice((ILoadSlot) loadSlot, (IDischargeSlot) dischargeSlot, loadTime, dischargeTime,
								dischargePricePerMMBTu, loadVolumeInM3, maximumDischargeVolumeInM3, vessel, plan, null);

						dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBTu, cargoCVValue);
						loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);

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

				loadSlot = (ILoadOption) loadDetails.getOptions().getPortSlot();
				dischargeSlot = (IDischargeOption) dischargeDetails.getOptions().getPortSlot();

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

				forcedLoadVolumeInM3 = 0;

				final int cargoCVValue = loadSlot.getCargoCVValue();

				// compute purchase price from contract
				// this is not ideal.
				final int dischargePricePerMMBTu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, time);

				// TODO this value is incorrect for netback and profit sharing cases
				// the load price per MMBTu is the notional maximum price
				// if we load less, it might actually be worth less
				maximumDischargeVolumeInM3 = Math.max(loadSlot.getMaxLoadVolume(), dischargeSlot.getMaxDischargeVolume());
				final long loadVolumeInM3 = maximumDischargeVolumeInM3;
				final int loadPricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice(loadSlot, dischargeSlot, time, dischargePricePerMMBTu, loadVolumeInM3, null);
				dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBTu, cargoCVValue);
				loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);

			}
		}

		final AllocationAnnotation annotation = new AllocationAnnotation();

		annotation.getSlots().clear();
		annotation.getSlots().add(loadSlot);
		annotation.getSlots().add(dischargeSlot);
		//annotation.setLoadSlot(loadSlot);
		//annotation.setDischargeSlot(dischargeSlot);
		annotation.setFuelVolumeInM3(forcedLoadVolumeInM3);

		// TODO recompute load price here; this is not necessarily right
		annotation.setSlotPricePerM3(loadSlot, loadPricePerM3);
		annotation.setSlotPricePerM3(dischargeSlot, dischargePricePerM3);
		annotation.setSlotTime(loadSlot, loadTime);
		annotation.setSlotTime(dischargeSlot, dischargeTime);
		annotation.setSlotVolumeInM3(dischargeSlot, maximumDischargeVolumeInM3);
		
		//annotation.setLoadPricePerM3(loadPricePerM3);
		//annotation.setDischargePricePerM3(dischargePricePerM3);
		//annotation.setLoadTime(loadTime);
		//annotation.setDischargeTime(dischargeTime);
		//annotation.setDischargeVolumeInM3(maximumDischargeVolumeInM3);

		return annotation;
	}

	@Override
	public void dispose() {
		reset();
	}

	public void addCargo(final VoyagePlan plan, final PortDetails loadDetails, final VoyageDetails ladenLeg, final PortDetails dischargeDetails, final VoyageDetails ballastLeg, final int loadTime,
			final int dischargeTime, final long requiredFuelVolumeInM3, final IVessel vessel) {

		final IVesselClass vesselClass = vessel.getVesselClass();
		final long vesselCapacityInM3 = vesselClass.getCargoCapacity();
		final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
		final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

		slotTimes.put(loadSlot, loadTime);
		slotTimes.put(dischargeSlot, dischargeTime);

		
		loadSlots.add(loadSlot);
		dischargeSlots.add(dischargeSlot);

		ArrayList<IPortSlot> slotList = new ArrayList<IPortSlot>();
		slotList.add(loadSlot);
		slotList.add(dischargeSlot);
		slotLists.add(slotList);		
		
		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoCount;
		variableTable.put(loadSlot, ci);
		variableTable.put(dischargeSlot, ci);

		// We have to load this much LNG no matter what
		forcedLoadVolumeInM3.add(requiredFuelVolumeInM3);
		this.vesselCapacityInM3.add(vesselCapacityInM3);

		final int cargoCVValue = loadSlot.getCargoCVValue();

		long remainingHeelInM3 = plan.getRemainingHeelInM3();
		
		// We need to include this as part of the load volume, but it is otherwise discarded.
		// Note: We need to model the other heel type here for min quantities, however the current allocators do not take this into account.
		if (plan.getRemainingHeelType() == HeelType.END) {
			remainingHeelVolumeInM3.add(remainingHeelInM3);
		} else {
			remainingHeelVolumeInM3.add(0l);
		}
		
		// compute purchase price from contract
		// this is not ideal.
		final int dischargePricePerMMBtu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, dischargeTime);
		long maxLoadVolumeInM3 = loadSlot.getMaxLoadVolume();
		if (maxLoadVolumeInM3 == 0) {
			maxLoadVolumeInM3 = vesselCapacityInM3;
		}
		final long maximumDischargeVolumeInM3 = Math.min(vesselCapacityInM3 - requiredFuelVolumeInM3, maxLoadVolumeInM3 - requiredFuelVolumeInM3);

		// TODO this value is incorrect for netback and profit sharing cases
		// the load CV price is the notional maximum price
		// if we load less, it might actually be worth less

		// FIXME: Why do we pass in max discharge value?
		final long loadVolumeInM3 = maximumDischargeVolumeInM3 + requiredFuelVolumeInM3;
		final int loadPricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice(loadSlot, dischargeSlot, loadTime, dischargeTime, dischargePricePerMMBtu, loadVolumeInM3,
				maximumDischargeVolumeInM3, vessel, plan, null);

		final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBtu, cargoCVValue);
		final int loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);

		slotPricesPerM3.put(loadSlot, loadPricePerM3);
		slotPricesPerM3.put(dischargeSlot, dischargePricePerM3);
		//loadPricesPerM3.add(loadPricePerM3);
		//dischargePricesPerM3.add(dischargePricePerM3);

		this.unitPricesPerM3.add(dischargePricePerM3 - loadPricePerM3);

		cargoCount++;
	}

	public void addVirtualCargo(final PortDetails loadDetails, final PortDetails dischargeDetails) {

		final ILoadOption loadSlot = (ILoadOption) loadDetails.getOptions().getPortSlot();
		final IDischargeOption dischargeSlot = (IDischargeOption) dischargeDetails.getOptions().getPortSlot();

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
		forcedLoadVolumeInM3.add(0l);
		this.vesselCapacityInM3.add(Long.MAX_VALUE);
		remainingHeelVolumeInM3.add(0l);

		final int cargoCVValue = loadSlot.getCargoCVValue();

		// compute purchase price from contract
		// this is not ideal.
		final int dischargePricePerMMBTu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, time);

		// TODO this value is incorrect for netback and profit sharing cases
		// the load CV price is the notional maximum price
		// if we load less, it might actually be worth less
		// TODO: Fix this check - should be min with a zero (unspecified) check
		final long loadVolumeInM3 = Math.max(loadSlot.getMaxLoadVolume(), dischargeSlot.getMaxDischargeVolume());

		final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBTu, cargoCVValue);
		final int loadPricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateLoadUnitPrice(loadSlot, dischargeSlot, time, dischargePricePerMMBTu, loadVolumeInM3, null);
		final int loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);

		slotPricesPerM3.put(loadSlot, loadPricePerM3);
		slotPricesPerM3.put(dischargeSlot, dischargePricePerM3);
		//loadPricesPerM3.add(loadPricePerM3);
		//dischargePricesPerM3.add(dischargePricePerM3);

		this.unitPricesPerM3.add(dischargePricePerM3 - loadPricePerM3);

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

	/**
	 * Returns a 
	 * @return
	 */
	protected abstract long[] allocateSpareVolume();

	public void solve() {
		this.allocation = allocateSpareVolume();
		// convolve for p&l

		// this.profit = 0;
		// for (int i = 0; i < allocation.length; i++) {
		// profit += Calculator.convertM3ToM3Price(allocation[i], unitPrices.get(i));
		// }
	}

	// public long getProfit() {
	// return profit / Calculator.ScaleFactor; // why?
	// }

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

	public Iterable<Pair<VoyagePlan, IAllocationAnnotation>> getAllocations() {
		return new Iterable<Pair<VoyagePlan, IAllocationAnnotation>>() {
			@Override
			public Iterator<Pair<VoyagePlan, IAllocationAnnotation>> iterator() {
				return new Iterator< Pair<VoyagePlan, IAllocationAnnotation>>() {
					final Iterator<ILoadOption> loadIterator = loadSlots.iterator();
					final Iterator<IDischargeOption> dischargeIterator = dischargeSlots.iterator();
					final Iterator<List<IPortSlot>> slotsIterator = slotLists.iterator();
					final Iterator<Integer> priceIterator = unitPricesPerM3.iterator();
					final Iterator<VoyagePlan> voyagePlansIterator = voyagePlans.iterator();
					int allocationIndex;

					@Override
					public boolean hasNext() {
						return loadIterator.hasNext() && dischargeIterator.hasNext() && priceIterator.hasNext() && voyagePlansIterator.hasNext();
					}

					@Override
					public Pair<VoyagePlan, IAllocationAnnotation> next() {
						final AllocationAnnotation annotation = new AllocationAnnotation();

						final ILoadOption loadSlot = loadIterator.next();
						final IDischargeOption dischargeSlot = dischargeIterator.next();
						final List<IPortSlot> slotList = slotsIterator.next(); 

						annotation.getSlots().clear();
						annotation.getSlots().addAll(slotList);
						
						//annotation.setLoadSlot(loadSlot);
						//annotation.setDischargeSlot(dischargeSlot);
						annotation.setFuelVolumeInM3(forcedLoadVolumeInM3.get(allocationIndex));
						annotation.setRemainingHeelVolumeInM3(remainingHeelVolumeInM3.get(allocationIndex));

						// TODO recompute load price here; this is not necessarily right
						annotation.setSlotPricePerM3(loadSlot, slotPricesPerM3.get(loadSlot));
						annotation.setSlotPricePerM3(dischargeSlot, slotPricesPerM3.get(dischargeSlot));
						//annotation.setLoadPricePerM3(loadPricesPerM3.get(allocationIndex));
						//annotation.setDischargePricePerM3(dischargePricesPerM3.get(allocationIndex));
						annotation.setSlotTime(loadSlot, slotTimes.get(loadSlot));
						annotation.setSlotTime(dischargeSlot, slotTimes.get(dischargeSlot));
						
						//annotation.setLoadTime(slotTimes.get(loadSlot));
						//annotation.setDischargeTime(slotTimes.get(dischargeSlot));
						
						annotation.setSlotVolumeInM3(dischargeSlot, allocation[allocationIndex++]);
						//annotation.setDischargeVolumeInM3(allocation[allocationIndex++]);

						return new Pair<VoyagePlan, IAllocationAnnotation>(voyagePlansIterator.next(), annotation);
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
