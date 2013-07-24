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
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;
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
 * @since 6.0
 * 
 */
public abstract class BaseVolumeAllocator implements IVolumeAllocator {
	
	/**
	 * Record class for allocation constraints per  
	 * @author Simon McGregor
	 */
	static final class AllocationRecord {
		/** The LNG volume which the vessel starts with (the start heel) */
		final long startVolumeInM3 = 0;
		
		/** The capacity of the vessel carrying the cargo */		
		final long vesselCapacityInM3;
		
		/** The quantity of LNG which <em>must</em> be loaded for a given cargo (for fuel) */
		final long requiredFuelVolumeInM3;

		/** The LNG volume which must remain at the end of the voyage (the remaining heel) */
		final long minEndVolumeInM3;
		
		/** Prices of LNG at each load / discharge slot in the cargo */
		final int [] slotPricesPerM3;
		
		/** Slots in the cargo */
		final IPortSlot [] slots;

		final VoyagePlan voyagePlan;
		
		public AllocationRecord(long capacity, long forced, long heel, int [] prices, IPortSlot [] slots, VoyagePlan plan) {
			vesselCapacityInM3 = capacity;
			requiredFuelVolumeInM3 = forced;
			minEndVolumeInM3 = heel;
			slotPricesPerM3 = prices;			
			this.slots = slots;
			voyagePlan = plan;
		}
	}
	
	/**
	 * All the constraints to take into account, indexed bu cargo
	 */
	final ArrayList<AllocationRecord> constraints = new ArrayList<AllocationRecord>();
	
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

	int cargoCount;

	private long[] allocation;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private Provider<VoyagePlanIterator> voyagePlanIteratorProvider;

	public BaseVolumeAllocator() {
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
		constraints.clear();
	}

	/**
	 * @since 5.0
	 */
	public final void addCargo(final VoyagePlan plan, final IVessel vessel, final ArrayList<PortDetails> ports, final ArrayList<VoyageDetails> voyages, final ArrayList<Integer> slotTimes) {
		final PortDetails[] portDetails = ports.toArray(new PortDetails[ports.size()]);
		final VoyageDetails[] voyageDetails = voyages.toArray(new VoyageDetails[voyages.size()]);
		final Integer[] times = slotTimes.toArray(new Integer[slotTimes.size()]);
		addCargo(plan, portDetails, voyageDetails, times, plan.getLNGFuelVolume(), vessel);
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
			VoyagePlan plan = null;

			final ArrayList<PortDetails> cargoPortDetails = new ArrayList<PortDetails>();
			final ArrayList<Integer> slotTimes = new ArrayList<Integer>();
			final ArrayList<VoyageDetails> voyages = new ArrayList<VoyageDetails>();
			IPortSlot lastSlot = null;
			plan = planIterator.getCurrentPlan();

			while (planIterator.hasNextObject()) {
				final Object object;
				if (planIterator.nextObjectIsStartOfPlan()) {

					// The AbstractSequencesScheduler breaks up the VoyagePlans in the desired fashion, make use of that information here.
					// Previously this code attempted to replicate that logic - and then map to a voyage plan. This was not very reliable.

					// Ensure voyages size is greater than zero - otherwise this is really a special/virtual cargo - such as FOB/DES which is handled separately
					if (plan != null && cargoPortDetails.size() >= 2 && voyages.size() > 0) {
						addCargo(plan, vessel, cargoPortDetails, voyages, slotTimes);//
					}
					// Clear the lists
					slotTimes.clear();
					cargoPortDetails.clear();
					voyages.clear();

					object = planIterator.nextObject();
					plan = planIterator.getCurrentPlan();
				} else {
					object = planIterator.nextObject();
				}
				if (object instanceof PortDetails) {
					final PortDetails pd = (PortDetails) object;
					final IPortSlot slot = pd.getOptions().getPortSlot();

					if (slot instanceof ILoadOption || slot instanceof IDischargeOption) {
						cargoPortDetails.add(pd);
						slotTimes.add(planIterator.getCurrentTime());
					}

					if (slot instanceof ILoadOption) {
						loadDetails = pd;
					} else if (slot instanceof IDischargeOption) {
						dischargeDetails = pd;
					}

					lastSlot = slot;

				} else if (object instanceof VoyageDetails) {
					if (lastSlot instanceof ILoadSlot || lastSlot instanceof IDischargeSlot) {
						voyages.add((VoyageDetails) object);
					}
				}
			}
			if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
				if (loadDetails != null && dischargeDetails != null) {
					addVirtualCargo(plan, loadDetails, dischargeDetails);
					// Clear fields
					cargoPortDetails.clear();
					slotTimes.clear();
					voyages.clear();
				}
			}

			// Handle any left over bits
			if (plan != null && cargoPortDetails.size() >= 2 && voyages.size() > 0) {
				addCargo(plan, vessel, cargoPortDetails, voyages, slotTimes);
			}

		}

		solve();
		final Map<VoyagePlan, IAllocationAnnotation> result = new HashMap<VoyagePlan, IAllocationAnnotation>();
		for (final Pair<VoyagePlan, IAllocationAnnotation> p : getAllocations()) {
			result.put(p.getFirst(), p.getSecond());
		}
		return result;
	}

	// TODO REVIEW THIS METHOD IN LIGHT OF COMPLEX CARGOES
	/**
	 * Allocate method designed for a SIMPLE cargo
	 * 
	 * @since 5.0
	 */
	@Override
	public IAllocationAnnotation allocate(final IVessel vessel, final VoyagePlan plan, final List<Integer> arrivalTimes) {

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
		int idx = -1;
		for (final Object object : plan.getSequence()) {
			if (object instanceof PortDetails) {
				++idx;
				final PortDetails pd = (PortDetails) object;
				final IPortSlot slot = pd.getOptions().getPortSlot();
				if (slot instanceof ILoadOption) {
					loadDetails = pd;
					loadTime = arrivalTimes.get(idx);
				} else if (slot instanceof IDischargeOption) {
					dischargeDetails = pd;
					dischargeTime = arrivalTimes.get(idx);
				}
			} else if (object instanceof VoyageDetails) {
				if ((dischargeDetails == null) && (loadDetails != null)) {
					ladenVoyage = (VoyageDetails) object;
				} else if ((dischargeDetails != null) && (loadDetails != null)) {
					ballastVoyage = (VoyageDetails) object;
					assert plan != null;

					{
						final long vesselCapacityInM3 = vessel.getCargoCapacity();
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
		annotation.setFuelVolumeInM3(forcedLoadVolumeInM3);

		// TODO recompute load price here; this is not necessarily right
		annotation.setSlotPricePerM3(loadSlot, loadPricePerM3);
		annotation.setSlotPricePerM3(dischargeSlot, dischargePricePerM3);
		annotation.setSlotTime(loadSlot, loadTime);
		annotation.setSlotTime(dischargeSlot, dischargeTime);
		annotation.setSlotVolumeInM3(dischargeSlot, maximumDischargeVolumeInM3);

		return annotation;
	}

	@Override
	public void dispose() {
		reset();
	}

	/**
	 * @since 5.0
	 */
	public void addCargo(final VoyagePlan plan, final PortDetails[] portDetails, final VoyageDetails[] legs, final Integer[] times, final long requiredFuelVolumeInM3, final IVessel vessel) {
		// TODO: REMOVE HACK!
		if (portDetails.length == 2) {
			PortDetails loadDetails = portDetails[0];
			PortDetails dischargeDetails = portDetails[1];
			VoyageDetails ladenLeg = legs.length > 0 ? legs[0] : null;
			VoyageDetails ballastLeg = legs.length > 1 ? legs[1] : null;

			addLoadDischargeCargo(plan, loadDetails, ladenLeg, dischargeDetails, ballastLeg, times[0], times[1], requiredFuelVolumeInM3, vessel);
		} else {
			addComplexCargo(plan, portDetails, legs, times, requiredFuelVolumeInM3, vessel);
		}
	}

	/**
	 * @since 5.0
	 */
	public void addLoadDischargeCargo(final VoyagePlan plan, final PortDetails loadDetails, final VoyageDetails ladenLeg, final PortDetails dischargeDetails, final VoyageDetails ballastLeg,
			final int loadTime, final int dischargeTime, final long requiredFuelVolumeInM3, final IVessel vessel) {

		final long vesselCapacityInM3 = vessel.getCargoCapacity();
		final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
		final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

		slotTimes.put(loadSlot, loadTime);
		slotTimes.put(dischargeSlot, dischargeTime);

		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoCount;
		variableTable.put(loadSlot, ci);
		variableTable.put(dischargeSlot, ci);		
		
		final IPortSlot[] slots = { loadSlot, dischargeSlot };


		final long heelRequired = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0l; 

		final int cargoCVValue = loadSlot.getCargoCVValue();

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

		final int[] prices = { loadPricePerM3, dischargePricePerM3 };

		constraints.add(new AllocationRecord(vesselCapacityInM3, requiredFuelVolumeInM3, heelRequired, prices, slots, plan));
		
		cargoCount++;
	}

	public void addVirtualCargo(final VoyagePlan plan, final PortDetails loadDetails, final PortDetails dischargeDetails) {

		final ILoadOption loadSlot = (ILoadOption) loadDetails.getOptions().getPortSlot();
		final IDischargeOption dischargeSlot = (IDischargeOption) dischargeDetails.getOptions().getPortSlot();

		addVirtualCargo(plan, loadSlot, dischargeSlot);
	}

	public void addVirtualCargo(final VoyagePlan plan, final ILoadOption loadSlot, final IDischargeOption dischargeSlot) {
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

		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoCount;
		variableTable.put(loadSlot, ci);
		variableTable.put(dischargeSlot, ci);

		final IPortSlot[] slots = { loadSlot, dischargeSlot };
		
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

		final int[] prices = { loadPricePerM3, dischargePricePerM3 };
		
		constraints.add(new AllocationRecord(Long.MAX_VALUE, 0l, 0l, prices, slots, plan));

		cargoCount++;
	}

	/**
	 * @since 5.0
	 */
	public void addComplexCargo(final VoyagePlan plan, final PortDetails[] portDetails, final VoyageDetails[] legs, final Integer[] times, final long requiredFuelVolumeInM3, final IVessel vessel) {

		final long vesselCapacityInM3 = vessel.getCargoCapacity();
		final IPortSlot[] slots = new IPortSlot[portDetails.length];

		for (int i = 0; i < slots.length; i++) {
			slots[i] = portDetails[i].getOptions().getPortSlot();
			slotTimes.put(slots[i], times[i]);
		}

		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoCount;
		for (final IPortSlot slot : slots) {
			variableTable.put(slot, ci);
		}

		final long heelRequired = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0l; 

		
		final ILoadSlot firstLoadSlot = (ILoadSlot) slots[0];
		final int cargoCVValue = firstLoadSlot.getCargoCVValue();

		final int[] pricesPerM3 = new int[slots.length];
		final long[] volumesInM3 = new long[slots.length];

		{
			long totalDischargeVolume = 0;

			// TODO: this is a complete hack
			// assumes (for a demo tomorrow) that the first slot is a load slot
			// and all others are discharge with fixed volumes
			for (int i = 1; i < slots.length; i++) {
				final IDischargeSlot dischargeSlot = (IDischargeSlot) slots[i];

				// HACK! assume fixed specified volume
				assert (dischargeSlot.getMinDischargeVolume() == dischargeSlot.getMaxDischargeVolume());
				final long volume = dischargeSlot.getMinDischargeVolume();
				volumesInM3[i] = volume;

				totalDischargeVolume += volume;

				final int dischargePricePerMMBtu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, times[i]);
				final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBtu, cargoCVValue);

				pricesPerM3[i] = dischargePricePerM3;
			}

			final ILoadSlot loadSlot = (ILoadSlot) slots[0];

			final ILoadPriceCalculator calculator = loadSlot.getLoadPriceCalculator();
			assert (calculator instanceof SimpleContract);

			final int loadPricePerMMBTu = calculator.calculateLoadUnitPrice(null, null, times[0], 0, 0, 0, 0, null, null, null);

			final int loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
			final long loadVolumeInM3 = totalDischargeVolume + requiredFuelVolumeInM3;

			volumesInM3[0] = loadVolumeInM3;
			pricesPerM3[0] = loadPricePerM3;
		}

		constraints.add(new AllocationRecord(vesselCapacityInM3, requiredFuelVolumeInM3, heelRequired, pricesPerM3, slots, plan));
		cargoCount++;
	}

	protected abstract long[] allocateSpareVolume();

	public void solve() {
		this.allocation = allocateSpareVolume();
	}

	@Override
	public void setTotalVolumeLimitProvider(final ITotalVolumeLimitProvider tvlp) {
		cargoAllocationProvider = tvlp;
	}

	public Iterable<Pair<VoyagePlan, IAllocationAnnotation>> getAllocations() {
		return new Iterable<Pair<VoyagePlan, IAllocationAnnotation>>() {
			@Override
			public Iterator<Pair<VoyagePlan, IAllocationAnnotation>> iterator() {
				return new Iterator<Pair<VoyagePlan, IAllocationAnnotation>>() {
					final Iterator<AllocationRecord> constraintsIterator = constraints.iterator();
					int allocationIndex;

					@Override
					public boolean hasNext() {
						return constraintsIterator.hasNext();
					}

					@Override
					public Pair<VoyagePlan, IAllocationAnnotation> next() {
						final AllocationAnnotation annotation = new AllocationAnnotation();
						AllocationRecord constraint = constraintsIterator.next();

						final IPortSlot[] slots = constraint.slots;

						annotation.getSlots().clear();
						for (final IPortSlot slot : slots) {
							annotation.getSlots().add(slot);
						}

						annotation.setFuelVolumeInM3(constraint.requiredFuelVolumeInM3);
						annotation.setRemainingHeelVolumeInM3(constraint.minEndVolumeInM3);

						// TODO recompute load price here; this is not necessarily right
						// final int[] prices = priceIterator.next();
						final int[] prices = constraint.slotPricesPerM3;

						assert slots.length == prices.length;
						for (int i = 0; i < slots.length; i++) {
							annotation.setSlotPricePerM3(slots[i], prices[i]);
							annotation.setSlotTime(slots[i], slotTimes.get(slots[i]));
						}

						// load/discharge case
						if (slots.length == 2) {
							annotation.setSlotVolumeInM3(slots[1], allocation[allocationIndex++]);
						}
						// LDD case
						else {
							for (int j = 1; j < slots.length; j++) {
								final IDischargeOption discharge = (IDischargeOption) slots[j];
								annotation.setSlotVolumeInM3(discharge, discharge.getMaxDischargeVolume());
							}
						}

						return new Pair<VoyagePlan, IAllocationAnnotation>(constraint.voyagePlan, annotation);
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
