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

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
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

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	protected ITotalVolumeLimitProvider cargoAllocationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private Provider<VoyagePlanIterator> voyagePlanIteratorProvider;

	/**
	 * All the constraints to take into account, indexed bu cargo
	 */
	protected final ArrayList<AllocationRecord> constraints = new ArrayList<AllocationRecord>();

	/**
	 * Maps from slots to arrival times; subclasses need this to determine whether a slot lies in a given gas year.
	 */
	private final Map<IPortSlot, Integer> slotTimes = new HashMap<IPortSlot, Integer>();

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
		slotTimes.clear();
		constraints.clear();
	}

	/**
	 * @param initialLngVolume
	 * @return
	 * @since 5.0
	 */
	public final AllocationRecord addCargo(final VoyagePlan plan, final IVessel vessel, int vesselStartTime, final ArrayList<PortDetails> ports, final ArrayList<VoyageDetails> voyages,
			final ArrayList<Integer> slotTimes, long initialLngVolume) {
		final PortDetails[] portDetails = ports.toArray(new PortDetails[ports.size()]);
		final VoyageDetails[] voyageDetails = voyages.toArray(new VoyageDetails[voyages.size()]);
		final Integer[] times = slotTimes.toArray(new Integer[slotTimes.size()]);
		return addCargo(plan, portDetails, voyageDetails, times, plan.getLNGFuelVolume(), vessel, vesselStartTime, initialLngVolume);
	}

	@Override
	public Map<VoyagePlan, IAllocationAnnotation> allocate(final ScheduledSequences sequences) {
		reset();

		final VoyagePlanIterator planIterator = voyagePlanIteratorProvider.get();
		for (final ScheduledSequence sequence : sequences) {
			planIterator.setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), sequence.getArrivalTimes());
			final IVessel vessel = vesselProvider.getVessel(sequence.getResource());
			int vesselStartTime = sequence.getStartTime();
			PortDetails loadDetails = null;
			PortDetails dischargeDetails = null;
			VoyagePlan plan = null;
			long lngRolledOver = 0; // the lng volume rolled over from the previous voyage plan in m3

			// TODO: All this info should be accessible from ScheduledSequences
			
			final ArrayList<PortDetails> cargoPortDetails = new ArrayList<PortDetails>();
			final ArrayList<Integer> slotTimes = new ArrayList<Integer>();
			final ArrayList<VoyageDetails> voyages = new ArrayList<VoyageDetails>();
			IPortSlot lastSlot = null;
			plan = planIterator.getCurrentPlan();

			while (planIterator.hasNextObject()) {
				assert plan != null;
				final Object object;
				if (planIterator.nextObjectIsStartOfPlan()) {

					// The AbstractSequencesScheduler breaks up the VoyagePlans in the desired fashion, make use of that information here.
					// Previously this code attempted to replicate that logic - and then map to a voyage plan. This was not very reliable.

					// Ensure voyages size is greater than zero - otherwise this is really a special/virtual cargo - such as FOB/DES which is handled separately
					if (plan != null && cargoPortDetails.size() >= 2 && voyages.size() > 0) {
						AllocationRecord cargoAllocation = addCargo(plan, vessel, vesselStartTime, cargoPortDetails, voyages, slotTimes, lngRolledOver);//
						lngRolledOver = cargoAllocation.minEndVolumeInM3;
					}
					// Clear the lists
					slotTimes.clear();
					cargoPortDetails.clear();
					voyages.clear();

					lngRolledOver = plan.getRemainingHeelInM3();

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
				addCargo(plan, vessel, vesselStartTime, cargoPortDetails, voyages, slotTimes, lngRolledOver);
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
	 * Allocate method designed for a SIMPLE cargo TODO: Duplicate in this method
	 * 
	 * @since 5.0
	 */
	@Override
	public IAllocationAnnotation allocate(final IVessel vessel, int vesselStartTime, final VoyagePlan plan, final List<Integer> arrivalTimes) {

		PortDetails loadDetails = null;
		PortDetails dischargeDetails = null;

		ILoadOption loadOption = null;
		IDischargeOption dischargeOption = null;

		long forcedLoadVolumeInM3 = plan.getLNGFuelVolume();
		int loadTime = 0, dischargeTime = 0;

		int dischargePricePerM3 = 0;
		int loadPricePerM3 = 0;
		int dischargePricePerMMBTu = 0;
		int loadPricePerMMBTu = 0;
		long maximumDischargeVolumeInM3 = 0;
		// Rough estimate of required array size
		IDetailsSequenceElement[] sequence = plan.getSequence();
		List<IPortSlot> slots = new ArrayList<>(sequence.length / 2 + 1);

		for (int i = 0; i < sequence.length - 1; ++i) {
			IDetailsSequenceElement element = sequence[i];
			if (element instanceof PortDetails) {
				final PortDetails pd = (PortDetails) element;
				final IPortSlot slot = pd.getOptions().getPortSlot();
				slots.add(slot);
			}
		}

		int idx = -1;
		for (final Object object : sequence) {
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
					// Nothing to do
				} else if ((dischargeDetails != null) && (loadDetails != null)) {
					assert plan != null;

					{
						final long vesselCapacityInM3 = vessel.getCargoCapacity();
						loadOption = (ILoadSlot) loadDetails.getOptions().getPortSlot();
						dischargeOption = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

						// We have to load this much LNG no matter what

						final int cargoCVValue = loadOption.getCargoCVValue();

						// compute purchase price from contract
						// this is not ideal.
						dischargePricePerMMBTu = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeOption, dischargeTime, null);
						long maxLoadVolumeInM3 = loadOption.getMaxLoadVolume();
						if (maxLoadVolumeInM3 == 0) {
							maxLoadVolumeInM3 = vesselCapacityInM3;
						}
						maximumDischargeVolumeInM3 = Math.min(vesselCapacityInM3 - forcedLoadVolumeInM3, maxLoadVolumeInM3 - forcedLoadVolumeInM3);

						// TODO this value is incorrect for netback and profit sharing cases where FOB price changes
						// the load CV price is the notional maximum price
						// if we load less, it might actually be worth less

						final long loadVolumeInM3 = maximumDischargeVolumeInM3 + forcedLoadVolumeInM3;
						loadPricePerMMBTu = loadOption.getLoadPriceCalculator().calculateFOBPricePerMMBTu((ILoadSlot) loadOption, (IDischargeSlot) dischargeOption, loadTime, dischargeTime,
								dischargePricePerMMBTu, loadVolumeInM3, maximumDischargeVolumeInM3, vessel, vesselStartTime, plan, null);

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

				loadOption = (ILoadOption) loadDetails.getOptions().getPortSlot();
				dischargeOption = (IDischargeOption) dischargeDetails.getOptions().getPortSlot();

				if (loadOption instanceof ILoadSlot) {
					isFOB = true;
				}
				if (dischargeOption instanceof IDischargeSlot) {
					isDES = true;
				}

				assert isDES != isFOB;

				final int time;

				if (isFOB) {
					// Pick start of window.
					time = ((ILoadSlot) loadOption).getTimeWindow().getStart();
				} else {
					time = ((IDischargeSlot) dischargeOption).getTimeWindow().getStart();
				}

				forcedLoadVolumeInM3 = 0;

				final int cargoCVValue = loadOption.getCargoCVValue();

				// compute purchase price from contract
				// this is not ideal.
				dischargePricePerMMBTu = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeOption, time, null);

				// TODO this value is incorrect for netback and profit sharing cases
				// the load price per MMBTu is the notional maximum price
				// if we load less, it might actually be worth less
				maximumDischargeVolumeInM3 = Math.max(loadOption.getMaxLoadVolume(), dischargeOption.getMaxDischargeVolume());
				final long loadVolumeInM3 = maximumDischargeVolumeInM3;

				if (isFOB) {
					loadPricePerMMBTu = loadOption.getLoadPriceCalculator().calculatePriceForFOBSalePerMMBTu((ILoadSlot) loadOption, dischargeOption, time, dischargePricePerMMBTu, loadVolumeInM3,
							null);
					loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
				} else {
					assert isDES;
					loadPricePerMMBTu = loadOption.getLoadPriceCalculator().calculateDESPurchasePricePerMMBTu(loadOption, (IDischargeSlot) dischargeOption, time, dischargePricePerMMBTu,
							loadVolumeInM3, null);
					loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
				}
				dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBTu, cargoCVValue);
			}
		}

		final AllocationAnnotation annotation = new AllocationAnnotation();

		annotation.getSlots().clear();
		annotation.getSlots().add(loadOption);
		annotation.getSlots().add(dischargeOption);
		annotation.setFuelVolumeInM3(forcedLoadVolumeInM3);

		// TODO recompute load price here; this is not necessarily right
		annotation.setSlotPricePerM3(loadOption, loadPricePerM3);
		annotation.setSlotPricePerM3(loadOption, loadPricePerMMBTu);
		annotation.setSlotPricePerM3(dischargeOption, dischargePricePerM3);
		annotation.setSlotPricePerM3(dischargeOption, dischargePricePerMMBTu);
		annotation.setSlotTime(loadOption, loadTime);
		annotation.setSlotTime(dischargeOption, dischargeTime);
		annotation.setSlotVolumeInM3(dischargeOption, maximumDischargeVolumeInM3);

		return annotation;
	}

	@Override
	public void dispose() {
		reset();
	}

	/**
	 * @param initialLngVolume
	 * @return
	 * @since 5.0
	 */
	public AllocationRecord addCargo(final VoyagePlan plan, final PortDetails[] portDetails, final VoyageDetails[] legs, final Integer[] times, final long requiredFuelVolumeInM3,
			final IVessel vessel, int vesselStartTime, long initialLngVolume) {
		// TODO: REMOVE HACK!
		if (portDetails.length == 2) {
			final PortDetails loadDetails = portDetails[0];
			final PortDetails dischargeDetails = portDetails[1];
			final VoyageDetails ladenLeg = legs.length > 0 ? legs[0] : null;
			final VoyageDetails ballastLeg = legs.length > 1 ? legs[1] : null;

			return addLoadDischargeCargo(plan, loadDetails, ladenLeg, dischargeDetails, ballastLeg, times[0], times[1], requiredFuelVolumeInM3, vessel, vesselStartTime, initialLngVolume);
		} else {
			return addComplexCargo(plan, portDetails, legs, times, requiredFuelVolumeInM3, vessel, vesselStartTime, initialLngVolume);
		}
	}

	/**
	 * @return
	 * @since 5.0
	 */
	public AllocationRecord addLoadDischargeCargo(final VoyagePlan plan, final PortDetails loadDetails, final VoyageDetails ladenLeg, final PortDetails dischargeDetails,
			final VoyageDetails ballastLeg, final int loadTime, final int dischargeTime, final long requiredFuelVolumeInM3, final IVessel vessel, int vesselStartTime, final long startHeel) {

		final long vesselCapacityInM3 = vessel.getCargoCapacity();
		final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
		final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

		slotTimes.put(loadSlot, loadTime);
		slotTimes.put(dischargeSlot, dischargeTime);

		final List<IPortSlot> slots = Lists.newArrayList(loadSlot, dischargeSlot);

		final long endHeelRequired = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0l;
		final long dischargeHeelRequired = plan.getRemainingHeelType() == HeelType.DISCHARGE ? plan.getRemainingHeelInM3() : 0l;

		final int cargoCVValue = loadSlot.getCargoCVValue();

		// compute purchase price from contract
		// this is not ideal.
		final int dischargePricePerMMBtu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, dischargeTime, null);
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
		final int loadPricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateFOBPricePerMMBTu(loadSlot, dischargeSlot, loadTime, dischargeTime, dischargePricePerMMBtu, loadVolumeInM3,
				maximumDischargeVolumeInM3, vessel, vesselStartTime, plan, null);
		final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBtu, cargoCVValue);
		final int loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);

		final int[] pricesPerM3 = { loadPricePerM3, dischargePricePerM3 };
		final int[] pricesPerMMBTu = { loadPricePerMMBTu, dischargePricePerMMBtu };
		final int[] times = { loadTime, dischargeTime };

		final AllocationRecord result = new AllocationRecord(vessel, vesselCapacityInM3, requiredFuelVolumeInM3, startHeel, endHeelRequired, dischargeHeelRequired, slots.toArray(new IPortSlot[2]),
				pricesPerM3, pricesPerMMBTu, times, plan);
		constraints.add(result);

		return result;
	}

	public void addVirtualCargo(final VoyagePlan plan, final PortDetails loadDetails, final PortDetails dischargeDetails) {

		final ILoadOption loadSlot = (ILoadOption) loadDetails.getOptions().getPortSlot();
		final IDischargeOption dischargeSlot = (IDischargeOption) dischargeDetails.getOptions().getPortSlot();

		addVirtualCargo(plan, loadSlot, dischargeSlot);
	}

	public void addVirtualCargo(final VoyagePlan plan, final ILoadOption loadOption, final IDischargeOption dischargeOption) {
		boolean isFOB = false;
		boolean isDES = false;

		if (loadOption instanceof ILoadSlot) {
			isFOB = true;
		}
		if (dischargeOption instanceof IDischargeSlot) {
			isDES = true;
		}

		assert isDES != isFOB;

		final int time;

		if (isFOB) {
			// Pick start of window.
			time = ((ILoadSlot) loadOption).getTimeWindow().getStart();
		} else {
			time = ((IDischargeSlot) dischargeOption).getTimeWindow().getStart();
		}

		slotTimes.put(loadOption, time);
		slotTimes.put(dischargeOption, time);

		final IPortSlot[] slots = { loadOption, dischargeOption };

		final int cargoCVValue = loadOption.getCargoCVValue();

		// compute purchase price from contract
		// this is not ideal.
		final int dischargePricePerMMBTu = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeOption, time, null);

		// If there is a nominated vessel, then use it's capacity to clamp transfer volume.
		// TODO: Note BOG may in reality further restrict the transfer volume
		long vesselCapacityInM3 = Long.MAX_VALUE;
		IVessel nominatedVessel = null;
		{
			// DES Purchase?
			if (!(loadOption instanceof LoadSlot)) {
				nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(loadOption));
				if (nominatedVessel != null) {
					vesselCapacityInM3 = nominatedVessel.getCargoCapacity();
				}
			}
			// FOB Sale?
			if (!(dischargeOption instanceof DischargeSlot)) {
				nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(dischargeOption));
				if (nominatedVessel != null) {
					vesselCapacityInM3 = nominatedVessel.getCargoCapacity();
				}
			}
		}
		// TODO this value is incorrect for netback and profit sharing cases
		// the load CV price is the notional maximum price
		// if we load less, it might actually be worth less
		// TODO: Fix this check - should be min with a zero (unspecified) check
		final long loadVolumeInM3 = Math.min(vesselCapacityInM3, Math.max(loadOption.getMaxLoadVolume(), dischargeOption.getMaxDischargeVolume()));

		final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBTu, cargoCVValue);
		final int loadPricePerMMBTu;
		final int loadPricePerM3;
		if (isFOB) {
			loadPricePerMMBTu = loadOption.getLoadPriceCalculator().calculatePriceForFOBSalePerMMBTu((ILoadSlot) loadOption, dischargeOption, time, dischargePricePerMMBTu, loadVolumeInM3, null);
			loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
		} else {
			assert isDES;
			loadPricePerMMBTu = loadOption.getLoadPriceCalculator().calculateDESPurchasePricePerMMBTu(loadOption, (IDischargeSlot) dischargeOption, time, dischargePricePerMMBTu, loadVolumeInM3, null);
			loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
		}

		final int[] pricesPerM3 = { loadPricePerM3, dischargePricePerM3 };
		final int[] pricesPerMMBTu = { loadPricePerMMBTu, dischargePricePerMMBTu };
		final int[] times = { time, time };

		constraints.add(new AllocationRecord(nominatedVessel, vesselCapacityInM3, slots, pricesPerM3, pricesPerMMBTu, times, plan));
	}

	/**
	 * @param initialLngVolume
	 * @return
	 * @since 5.0
	 */
	public AllocationRecord addComplexCargo(final VoyagePlan plan, final PortDetails[] portDetails, final VoyageDetails[] legs, final Integer[] times, final long requiredFuelVolumeInM3,
			final IVessel vessel, int vesselStartTime, long startHeel) {

		final long vesselCapacityInM3 = vessel.getCargoCapacity();
		final IPortSlot[] slots = new IPortSlot[portDetails.length];

		final int[] slotTimes = new int[slots.length];

		for (int i = 0; i < slots.length; i++) {
			slots[i] = portDetails[i].getOptions().getPortSlot();
			slotTimes[i] = times[i];
		}

		final long endHeelRequired = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0l;
		final long dischargeHeelRequired = plan.getRemainingHeelType() == HeelType.DISCHARGE ? plan.getRemainingHeelInM3() : 0l;

		final ILoadSlot firstLoadSlot = (ILoadSlot) slots[0];
		final int cargoCVValue = firstLoadSlot.getCargoCVValue();

		final int[] pricesPerM3 = new int[slots.length];
		final int[] pricesPerMMBTu = new int[slots.length];
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

				final int dischargePricePerMMBtu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, times[i], null);
				final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBtu, cargoCVValue);

				pricesPerM3[i] = dischargePricePerM3;
				pricesPerMMBTu[i] = dischargePricePerMMBtu;
			}

			final ILoadSlot loadSlot = (ILoadSlot) slots[0];

			final ILoadPriceCalculator calculator = loadSlot.getLoadPriceCalculator();
			assert (calculator instanceof SimpleContract);

			final int loadPricePerMMBTu = calculator.calculateFOBPricePerMMBTu(null, null, times[0], 0, 0, 0, 0, null, vesselStartTime, null, null);

			final int loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
			final long loadVolumeInM3 = totalDischargeVolume + requiredFuelVolumeInM3;

			volumesInM3[0] = loadVolumeInM3;
			pricesPerM3[0] = loadPricePerM3;
			pricesPerMMBTu[0] = loadPricePerMMBTu;
		}

		final AllocationRecord result = new AllocationRecord(vessel, vesselCapacityInM3, requiredFuelVolumeInM3, startHeel, endHeelRequired, dischargeHeelRequired, slots, pricesPerM3, pricesPerMMBTu,
				slotTimes, plan);
		constraints.add(result);
		return result;
	}

	protected abstract void allocateSpareVolume();

	public void solve() {
		allocateSpareVolume();
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

					@Override
					public boolean hasNext() {
						return constraintsIterator.hasNext();
					}

					@Override
					public Pair<VoyagePlan, IAllocationAnnotation> next() {
						final AllocationRecord constraint = constraintsIterator.next();
						final AllocationAnnotation annotation = constraint.createAllocationAnnotation();

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
