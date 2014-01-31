/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
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
	@Inject(optional = true)
	private ICustomVolumeAllocator allocationRecordModifier;
	@Inject
	private INominatedVesselProvider nominatedVesselProvider;
	//
	@Inject
	private IPortSlotProvider portSlotProvider;

	//
	// @Inject
	// protected ITotalVolumeLimitProvider cargoAllocationProvider;
	//
	// @Inject
	// private IVesselProvider vesselProvider;
	//
	// @Inject
	// private Provider<VoyagePlanIterator> voyagePlanIteratorProvider;
	//
	// /**
	// * All the constraints to take into account, indexed by cargo
	// */
	// protected final ArrayList<AllocationRecord> constraints = new ArrayList<AllocationRecord>();
	//
	// /**
	// * Maps from slots to arrival times; subclasses need this to determine whether a slot lies in a given gas year.
	// */
	// private final Map<IPortSlot, Integer> slotTimes = new HashMap<IPortSlot, Integer>();

	public BaseVolumeAllocator() {
		super();
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator #init()
	// */
	// @Override
	// public void init() {
	// if (cargoAllocationProvider == null) {
	// throw new RuntimeException("Cargo allocation provider must be set");
	// }
	// }
	//
	// @Override
	// public void setVesselProvider(final IVesselProvider dataComponentProvider) {
	// this.vesselProvider = dataComponentProvider;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator #reset()
	// */
	// @Override
	// public void reset() {
	// slotTimes.clear();
	// constraints.clear();
	// }
	//
	// /**
	// * @param initialLngVolume
	// * @return
	// * @since 5.0
	// */
	// public final AllocationRecord addCargo(final VoyagePlan plan, final IVessel vessel, int vesselStartTime, final ArrayList<PortDetails> ports, final ArrayList<VoyageDetails> voyages,
	// final ArrayList<Integer> slotTimes, long initialLngVolume) {
	// final PortDetails[] portDetails = ports.toArray(new PortDetails[ports.size()]);
	// final VoyageDetails[] voyageDetails = voyages.toArray(new VoyageDetails[voyages.size()]);
	// final Integer[] times = slotTimes.toArray(new Integer[slotTimes.size()]);
	// return addCargo(plan, portDetails, voyageDetails, times, plan.getLNGFuelVolume(), vessel, vesselStartTime, initialLngVolume);
	// }
	//
	// @Override
	// public Map<VoyagePlan, IAllocationAnnotation> allocate(final ScheduledSequences sequences) {
	// reset();
	//
	// final VoyagePlanIterator planIterator = voyagePlanIteratorProvider.get();
	// for (final ScheduledSequence sequence : sequences) {
	// planIterator.setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), sequence.getArrivalTimes());
	// final IVessel vessel = vesselProvider.getVessel(sequence.getResource());
	// int vesselStartTime = sequence.getStartTime();
	// PortDetails loadDetails = null;
	// PortDetails dischargeDetails = null;
	// VoyagePlan plan = null;
	//
	// // TODO: All this info should be accessible from ScheduledSequences
	//
	// final ArrayList<PortDetails> cargoPortDetails = new ArrayList<PortDetails>();
	// final ArrayList<Integer> slotTimes = new ArrayList<Integer>();
	// final ArrayList<VoyageDetails> voyages = new ArrayList<VoyageDetails>();
	// IPortSlot lastSlot = null;
	// plan = planIterator.getCurrentPlan();
	// // long lngRolledOver = 0; // the lng volume rolled over from the previous voyage plan in m3
	// long lngRolledOver = plan.getStartingHeelInM3();
	//
	// while (planIterator.hasNextObject()) {
	// assert plan != null;
	// final Object object;
	// if (planIterator.nextObjectIsStartOfPlan()) {
	// // The AbstractSequencesScheduler breaks up the VoyagePlans in the desired fashion, make use of that information here.
	// // Previously this code attempted to replicate that logic - and then map to a voyage plan. This was not very reliable.
	//
	// // Ensure voyages size is greater than zero - otherwise this is really a special/virtual cargo - such as FOB/DES which is handled separately
	// if (plan != null && cargoPortDetails.size() >= 2 && voyages.size() > 0) {
	// addCargo(plan, vessel, vesselStartTime, cargoPortDetails, voyages, slotTimes, lngRolledOver);//
	// }
	// // Clear the lists
	// slotTimes.clear();
	// cargoPortDetails.clear();
	// voyages.clear();
	//
	// // FIXME: This is only the min heel rollover, not any excess load volume
	// // Update LNG rollover quantity
	// if (plan.getRemainingHeelType() == HeelType.END) {
	// lngRolledOver = plan.getRemainingHeelInM3();
	// } else {
	// lngRolledOver = 0;
	// }
	//
	// object = planIterator.nextObject();
	// plan = planIterator.getCurrentPlan();
	// } else {
	// object = planIterator.nextObject();
	// }
	// if (object instanceof PortDetails) {
	// final PortDetails pd = (PortDetails) object;
	// final IPortSlot slot = pd.getOptions().getPortSlot();
	//
	// if (slot instanceof ILoadOption || slot instanceof IDischargeOption) {
	// cargoPortDetails.add(pd);
	// slotTimes.add(planIterator.getCurrentTime());
	// }
	//
	// if (slot instanceof ILoadOption) {
	// loadDetails = pd;
	// } else if (slot instanceof IDischargeOption) {
	// dischargeDetails = pd;
	// }
	//
	// lastSlot = slot;
	//
	// } else if (object instanceof VoyageDetails) {
	// if (lastSlot instanceof ILoadSlot || lastSlot instanceof IDischargeSlot) {
	// voyages.add((VoyageDetails) object);
	// }
	// }
	// }
	// if (vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
	// if (loadDetails != null && dischargeDetails != null) {
	// addVirtualCargo(plan, loadDetails, dischargeDetails);
	// // Clear fields
	// cargoPortDetails.clear();
	// slotTimes.clear();
	// voyages.clear();
	// }
	// }
	//
	// // Handle any left over bits
	// if (plan != null && cargoPortDetails.size() >= 2 && voyages.size() > 0) {
	// addCargo(plan, vessel, vesselStartTime, cargoPortDetails, voyages, slotTimes, lngRolledOver);
	// }
	//
	// }
	//
	// solve();
	// final Map<VoyagePlan, IAllocationAnnotation> result = new HashMap<VoyagePlan, IAllocationAnnotation>();
	// for (final Pair<VoyagePlan, IAllocationAnnotation> p : getAllocations()) {
	// result.put(p.getFirst(), p.getSecond());
	// }
	// return result;
	// }

	@Override
	@Nullable
	public AllocationRecord createAllocationRecord(final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final List<Integer> arrivalTimes) {

		// if (not cargo plan) {
		// return null;
		// }

		// TODO: Assume VoyagePlanner has done this for us
		// IVessel nominatedVessel = null;
		// {
		// // DES Purchase?
		// if (!(loadOption instanceof LoadSlot)) {
		// nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(loadOption));
		// }
		// // FOB Sale?
		// if (!(dischargeOption instanceof DischargeSlot)) {
		// nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(dischargeOption));
		// }
		// }

		final long startVolumeInM3 = plan.getStartingHeelInM3();
		final long requiredFuelVolumeInM3 = plan.getLNGFuelVolume();
		final long minEndVolumeInM3 = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0;
		final long dischargeHeelInM3 = plan.getRemainingHeelType() == HeelType.DISCHARGE ? plan.getRemainingHeelInM3() : 0;

		// Rough estimate of required array size
		final IDetailsSequenceElement[] sequence = plan.getSequence();
		final int numElements = sequence.length / 2 + 1;
		final List<IPortSlot> slots = new ArrayList<>(numElements);
		// List<Integer> slotTimes = new ArrayList<>(numElements);
		final List<Long> minVolumes = new ArrayList<>(numElements);
		final List<Long> maxVolumes = new ArrayList<>(numElements);

		boolean foundALoad = false;

		// These handle current special cases around FOB/DES
		Integer transferTime = null;
		Long transferVolume = null;
		IVessel nominatedVessel = null;
		for (int i = 0; i < sequence.length - 1; ++i) {
			final IDetailsSequenceElement element = sequence[i];

			if (element instanceof PortDetails) {
				final PortDetails pd = (PortDetails) element;
				final IPortSlot slot = pd.getOptions().getPortSlot();
				// Special case for FOB/DES
				// Need better bit#
				if (slot instanceof StartPortSlot) {
					continue;
				}
				slots.add(slot);
				if (slot instanceof ILoadOption) {
					final ILoadOption loadOption = (ILoadOption) slot;
					minVolumes.add(loadOption.getMinLoadVolume());
					maxVolumes.add(loadOption.getMaxLoadVolume());
					foundALoad = true;
					if (!(loadOption instanceof ILoadSlot)) {
						transferTime = arrivalTimes.get(i);
						if (transferVolume == null) {
							transferVolume = loadOption.getMaxLoadVolume();
						} else {
							transferVolume = Math.min(transferVolume, loadOption.getMaxLoadVolume());
						}
						nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(loadOption));
					}
				} else if (slot instanceof IDischargeOption) {
					final IDischargeOption dischargeOption = (IDischargeOption) slot;
					minVolumes.add(dischargeOption.getMinDischargeVolume());
					maxVolumes.add(dischargeOption.getMaxDischargeVolume());
					if (!(dischargeOption instanceof IDischargeSlot)) {
						transferTime = arrivalTimes.get(i);
						if (transferVolume == null) {
							transferVolume = dischargeOption.getMaxDischargeVolume();
						} else {
							transferVolume = Math.min(transferVolume, dischargeOption.getMaxDischargeVolume());
						}
						nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(dischargeOption));
					}
				} else if (slot instanceof IHeelOptionsPortSlot) {
					final IHeelOptionsPortSlot heelOptionsPortSlot = (IHeelOptionsPortSlot) slot;
					final IHeelOptions heelOptions = heelOptionsPortSlot.getHeelOptions();
					minVolumes.add(heelOptions.getHeelLimit());
					maxVolumes.add(heelOptions.getHeelLimit());
				} else {
					minVolumes.add(0l);
					maxVolumes.add(0l);
				}
			}
		}

		// Only handle cargoes for now
		if (!foundALoad) {
			return null;
		}

		final List<Integer> slotTimes;
		final IVessel slotVessel = nominatedVessel != null ? nominatedVessel : vessel;
		if (transferTime != null) {
			slotTimes = new ArrayList<Integer>(sequence.length - 1);
			for (int i = 0; i < sequence.length - 1; ++i) {
				slotTimes.add(transferTime);
			}
		} else {
			slotTimes = new ArrayList<Integer>(arrivalTimes);
		}

		final AllocationRecord allocationRecord = new AllocationRecord(slotVessel, startVolumeInM3, requiredFuelVolumeInM3, minEndVolumeInM3, dischargeHeelInM3, slots, slotTimes, minVolumes,
				maxVolumes);
		if (allocationRecordModifier != null) {
			allocationRecordModifier.handle(allocationRecord);
		}

		return allocationRecord;
	}

	// @Override
	// public void dispose() {
	// reset();
	// }
	//
	// /**
	// * @param initialLngVolume
	// * @return
	// * @since 5.0
	// */
	// public AllocationRecord addCargo(final VoyagePlan plan, final PortDetails[] portDetails, final VoyageDetails[] legs, final Integer[] times, final long requiredFuelVolumeInM3,
	// final IVessel vessel, int vesselStartTime, long initialLngVolume) {
	// // TODO: REMOVE HACK!
	// if (portDetails.length == 2) {
	// final PortDetails loadDetails = portDetails[0];
	// final PortDetails dischargeDetails = portDetails[1];
	// final VoyageDetails ladenLeg = legs.length > 0 ? legs[0] : null;
	// final VoyageDetails ballastLeg = legs.length > 1 ? legs[1] : null;
	//
	// return addLoadDischargeCargo(plan, loadDetails, ladenLeg, dischargeDetails, ballastLeg, times[0], times[1], requiredFuelVolumeInM3, vessel, vesselStartTime, initialLngVolume);
	// } else {
	// return addComplexCargo(plan, portDetails, legs, times, requiredFuelVolumeInM3, vessel, vesselStartTime, initialLngVolume);
	// }
	// }

	// /**
	// * @return
	// * @since 5.0
	// */
	// public AllocationRecord addLoadDischargeCargo(final VoyagePlan plan, final PortDetails loadDetails, final VoyageDetails ladenLeg, final PortDetails dischargeDetails,
	// final VoyageDetails ballastLeg, final int loadTime, final int dischargeTime, final long requiredFuelVolumeInM3, final IVessel vessel, int vesselStartTime, final long startHeel) {
	//
	// final long vesselCapacityInM3 = vessel.getCargoCapacity();
	// final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
	// final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();
	//
	// slotTimes.put(loadSlot, loadTime);
	// slotTimes.put(dischargeSlot, dischargeTime);
	//
	// final List<IPortSlot> slots = Lists.newArrayList(loadSlot, dischargeSlot);
	//
	// final long endHeelRequired = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0l;
	// final long dischargeHeelRequired = plan.getRemainingHeelType() == HeelType.DISCHARGE ? plan.getRemainingHeelInM3() : 0l;
	//
	// final int cargoCVValue = loadSlot.getCargoCVValue();
	//
	// // compute purchase price from contract
	// // this is not ideal.
	// final int dischargePricePerMMBtu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, dischargeTime, null);
	// long maxLoadVolumeInM3 = loadSlot.getMaxLoadVolume();
	// if (maxLoadVolumeInM3 == 0) {
	// maxLoadVolumeInM3 = vesselCapacityInM3;
	// }
	// final long maximumDischargeVolumeInM3 = Math.min(vesselCapacityInM3 - requiredFuelVolumeInM3, maxLoadVolumeInM3 - requiredFuelVolumeInM3);
	//
	// // TODO this value is incorrect for netback and profit sharing cases
	// // the load CV price is the notional maximum price
	// // if we load less, it might actually be worth less
	//
	// // FIXME: Why do we pass in max discharge value?
	// final long loadVolumeInM3 = maximumDischargeVolumeInM3 + requiredFuelVolumeInM3;
	// final int loadPricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateFOBPricePerMMBTu(loadSlot, dischargeSlot, loadTime, dischargeTime, dischargePricePerMMBtu, loadVolumeInM3,
	// maximumDischargeVolumeInM3, vessel, vesselStartTime, plan, null);
	// final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBtu, cargoCVValue);
	// final int loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
	//
	// final int[] pricesPerM3 = { loadPricePerM3, dischargePricePerM3 };
	// final int[] pricesPerMMBTu = { loadPricePerMMBTu, dischargePricePerMMBtu };
	// final int[] times = { loadTime, dischargeTime };
	//
	// final AllocationRecord result = new AllocationRecord(vessel, vesselCapacityInM3, requiredFuelVolumeInM3, startHeel, endHeelRequired, dischargeHeelRequired, slots.toArray(new IPortSlot[2]),
	// pricesPerM3, pricesPerMMBTu, times, plan);
	// constraints.add(result);
	//
	// return result;
	// }

	// public void addVirtualCargo(final VoyagePlan plan, final PortDetails loadDetails, final PortDetails dischargeDetails) {
	//
	// final ILoadOption loadSlot = (ILoadOption) loadDetails.getOptions().getPortSlot();
	// final IDischargeOption dischargeSlot = (IDischargeOption) dischargeDetails.getOptions().getPortSlot();
	//
	// addVirtualCargo(plan, loadSlot, dischargeSlot);
	// }
	//
	// public void addVirtualCargo(final VoyagePlan plan, final ILoadOption loadOption, final IDischargeOption dischargeOption) {
	// boolean isFOB = false;
	// boolean isDES = false;
	//
	// if (loadOption instanceof ILoadSlot) {
	// isFOB = true;
	// }
	// if (dischargeOption instanceof IDischargeSlot) {
	// isDES = true;
	// }
	//
	// assert isDES != isFOB;
	//
	// final int time;
	//
	// if (isFOB) {
	// // Pick start of window.
	// time = ((ILoadSlot) loadOption).getTimeWindow().getStart();
	// } else {
	// time = ((IDischargeSlot) dischargeOption).getTimeWindow().getStart();
	// }
	//
	// slotTimes.put(loadOption, time);
	// slotTimes.put(dischargeOption, time);
	//
	// final IPortSlot[] slots = { loadOption, dischargeOption };
	//
	// final int cargoCVValue = loadOption.getCargoCVValue();
	//
	// // compute purchase price from contract
	// // this is not ideal.
	// final int dischargePricePerMMBTu = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeOption, time, null);
	//
	// // If there is a nominated vessel, then use it's capacity to clamp transfer volume.
	// // TODO: Note BOG may in reality further restrict the transfer volume
	// long vesselCapacityInM3 = Long.MAX_VALUE;
	// ` // TODO this value is incorrect for netback and profit sharing cases
	// // the load CV price is the notional maximum price
	// // if we load less, it might actually be worth less
	// // TODO: Fix this check - should be min with a zero (unspecified) check
	// final long loadVolumeInM3 = Math.min(vesselCapacityInM3, Math.max(loadOption.getMaxLoadVolume(), dischargeOption.getMaxDischargeVolume()));
	//
	// final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBTu, cargoCVValue);
	// final int loadPricePerMMBTu;
	// final int loadPricePerM3;
	// if (isFOB) {
	// loadPricePerMMBTu = loadOption.getLoadPriceCalculator().calculatePriceForFOBSalePerMMBTu((ILoadSlot) loadOption, dischargeOption, time, dischargePricePerMMBTu, loadVolumeInM3, null);
	// loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
	// } else {
	// assert isDES;
	// loadPricePerMMBTu = loadOption.getLoadPriceCalculator().calculateDESPurchasePricePerMMBTu(loadOption, (IDischargeSlot) dischargeOption, time, dischargePricePerMMBTu, loadVolumeInM3, null);
	// loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
	// }
	//
	// final int[] pricesPerM3 = { loadPricePerM3, dischargePricePerM3 };
	// final int[] pricesPerMMBTu = { loadPricePerMMBTu, dischargePricePerMMBTu };
	// final int[] times = { time, time };
	//
	// constraints.add(new AllocationRecord(nominatedVessel, vesselCapacityInM3, slots, pricesPerM3, pricesPerMMBTu, times, plan));
	// }

	// /**
	// * @param initialLngVolume
	// * @return
	// * @since 5.0
	// */
	// public AllocationRecord addComplexCargo(final VoyagePlan plan, final PortDetails[] portDetails, final VoyageDetails[] legs, final Integer[] times, final long requiredFuelVolumeInM3,
	// final IVessel vessel, int vesselStartTime, long startHeel) {
	//
	// final long vesselCapacityInM3 = vessel.getCargoCapacity();
	// final IPortSlot[] slots = new IPortSlot[portDetails.length];
	//
	// final int[] slotTimes = new int[slots.length];
	//
	// for (int i = 0; i < slots.length; i++) {
	// slots[i] = portDetails[i].getOptions().getPortSlot();
	// slotTimes[i] = times[i];
	// }
	//
	// final long endHeelRequired = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0l;
	// final long dischargeHeelRequired = plan.getRemainingHeelType() == HeelType.DISCHARGE ? plan.getRemainingHeelInM3() : 0l;
	//
	// final ILoadSlot firstLoadSlot = (ILoadSlot) slots[0];
	// final int cargoCVValue = firstLoadSlot.getCargoCVValue();
	//
	// final int[] pricesPerM3 = new int[slots.length];
	// final int[] pricesPerMMBTu = new int[slots.length];
	// final long[] volumesInM3 = new long[slots.length];
	//
	// {
	// long totalDischargeVolume = 0;
	//
	// // TODO: this is a complete hack
	// // assumes (for a demo tomorrow) that the first slot is a load slot
	// // and all others are discharge with fixed volumes
	// for (int i = 1; i < slots.length; i++) {
	// final IDischargeSlot dischargeSlot = (IDischargeSlot) slots[i];
	//
	// // HACK! assume fixed specified volume
	// assert (dischargeSlot.getMinDischargeVolume() == dischargeSlot.getMaxDischargeVolume());
	// final long volume = dischargeSlot.getMinDischargeVolume();
	// volumesInM3[i] = volume;
	//
	// totalDischargeVolume += volume;
	//
	// final int dischargePricePerMMBtu = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, times[i], null);
	// final int dischargePricePerM3 = Calculator.costPerM3FromMMBTu(dischargePricePerMMBtu, cargoCVValue);
	//
	// pricesPerM3[i] = dischargePricePerM3;
	// pricesPerMMBTu[i] = dischargePricePerMMBtu;
	// }
	//
	// final ILoadSlot loadSlot = (ILoadSlot) slots[0];
	//
	// final ILoadPriceCalculator calculator = loadSlot.getLoadPriceCalculator();
	// assert (calculator instanceof SimpleContract);
	//
	// final int loadPricePerMMBTu = calculator.calculateFOBPricePerMMBTu(null, null, times[0], 0, 0, 0, 0, null, vesselStartTime, null, null);
	//
	// final int loadPricePerM3 = Calculator.costPerM3FromMMBTu(loadPricePerMMBTu, cargoCVValue);
	// final long loadVolumeInM3 = totalDischargeVolume + requiredFuelVolumeInM3;
	//
	// volumesInM3[0] = loadVolumeInM3;
	// pricesPerM3[0] = loadPricePerM3;
	// pricesPerMMBTu[0] = loadPricePerMMBTu;
	// }
	//
	// long startVolumeInM3;
	// final AllocationRecord result = new AllocationRecord(vessel, startVolumeInM3, requiredFuelVolumeInM3, endHeelRequired, dischargeHeelRequired, slots, pricesPerM3, pricesPerMMBTu, slotTimes);
	// constraints.add(result);
	// return result;
	// }

	// protected abstract void allocateSpareVolume();
	//
	// public void solve() {
	// allocateSpareVolume();
	// }
	//
	// @Override
	// public void setTotalVolumeLimitProvider(final ITotalVolumeLimitProvider tvlp) {
	// cargoAllocationProvider = tvlp;
	// }

	// public Iterable<Pair<VoyagePlan, IAllocationAnnotation>> getAllocations() {
	// return new Iterable<Pair<VoyagePlan, IAllocationAnnotation>>() {
	// @Override
	// public Iterator<Pair<VoyagePlan, IAllocationAnnotation>> iterator() {
	// return new Iterator<Pair<VoyagePlan, IAllocationAnnotation>>() {
	// final Iterator<AllocationRecord> constraintsIterator = constraints.iterator();
	//
	// @Override
	// public boolean hasNext() {
	// return constraintsIterator.hasNext();
	// }
	//
	// @Override
	// public Pair<VoyagePlan, IAllocationAnnotation> next() {
	// final AllocationRecord constraint = constraintsIterator.next();
	// final AllocationAnnotation annotation = constraint.createAllocationAnnotation();
	//
	// return new Pair<VoyagePlan, IAllocationAnnotation>(constraint.voyagePlan, annotation);
	// }
	//
	// @Override
	// public void remove() {
	// throw new UnsupportedOperationException();
	// }
	//
	// };
	// }
	// };
	// }
}
