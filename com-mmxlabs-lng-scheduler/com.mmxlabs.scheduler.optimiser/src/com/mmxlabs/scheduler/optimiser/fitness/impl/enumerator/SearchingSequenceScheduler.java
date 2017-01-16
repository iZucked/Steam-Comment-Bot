/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Experimental {@link ISequenceScheduler} implementation using the {@link VoyagePlanOptimiser} to find lowest cost arrival times.
 * 
 * Currently a) very slow (but ways to address this with better caching, alternative ways to compute arrival times) and b) gets stuck in local "minima". For e.g. a 4 cargo sequence it is possible
 * cargo 4 needs to increment forward a by a large margin to get out of a minima, but doing so increases costs across the board and so will not be found.
 * 
 * This algorithm needs to change to "pull" all time windows forward (like a concertina) to overcome this (although this makes things much more complicated).
 * 
 * @author Simon Goodall
 *
 */
public class SearchingSequenceScheduler extends EnumeratingSequenceScheduler {
	// private final int seed = 0;
	// private Random random;

	@Inject
	private VoyagePlanner voyagePlanner;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private ICharterRateCalculator charterRateCalculator;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Override
	public int @Nullable [][] schedule(@NonNull final ISequences sequences) {

		setSequences(sequences);

		prepare();

		for (int index = 0; index < sequences.size(); ++index) {
			search(index);
			search(index);
		}
		synchroniseShipToShipBindings();

		return arrivalTimes;
	}

	private void synchroniseShipToShipBindings() {
		// TODO: why do we not fix up later voyages? Why do we not loop multiple times here?
		// TODO: If the above is implemented, STS slots should probably arrive as early as possible.
		for (int i = 0; i < bindings.size(); i += 4) {
			final int discharge_seq = bindings.get(i);
			final int discharge_index = bindings.get(i + 1);
			final int load_seq = bindings.get(i + 2);
			final int load_index = bindings.get(i + 3);

			// sequence elements bound by ship-to-ship transfers are effectively the same slot, so the arrival times must be synchronised
			arrivalTimes[load_seq][load_index] = arrivalTimes[discharge_seq][discharge_index];
		}

	}

	private void search(final int seq) {
		if (arrivalTimes[seq] == null) {
			return;
		}
		if (sizes[seq] > 0) {
			final ISequence sequence = sequences.getSequence(seq);
			final IResource resource = sequences.getResources().get(seq);

			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

			final int lastIndex = sequence.size();// sizes[seq];
			// First step, early arrival time

			final boolean[] breakSequence = findSequenceBreaks(sequence, isRoundTripSequence);

			boolean changedIdx[] = new boolean[lastIndex];
			for (int pos = 0; pos < lastIndex; pos++) {
				arrivalTimes[seq][pos] = getMinArrivalTime(seq, pos);
				// arrivalTimes[seq][pos] = windowStartTime[seq][pos];// getMinArrivalTime(seq, pos);
				changedIdx[pos] = true;
			}
			// arrivalTimes[seq][lastIndex] = getMinArrivalTime(seq, lastIndex);
			// arrivalTimes[seq][0] = getMaxArrivalTimeForNextArrival(seq, 0);

			boolean changed = isRoundTripSequence ? false : true;
			while (changed) {
				changed = false;
				for (int pos = 0; pos < lastIndex; pos++) {
					// If next idx has not changed, then no point updating this idx.
					if (pos + 1 < lastIndex && !changedIdx[pos + 1]) {
						continue;
					}
					changedIdx[pos] = false;
					long bestValue = getValue(seq, pos, arrivalTimes[seq][pos], breakSequence);

					int k = arrivalTimes[seq][pos];
					for (int j = arrivalTimes[seq][pos] + 1; j < getMaxArrivalTime(seq, pos); ++j) {
						// for (int j = arrivalTimes[seq][pos] + 1; j < windowEndTime[seq][pos]; ++j) {
						if (k + 12 < j) {
							// break;
						}

						// Limit search to feasible forward view
						if (j > windowEndTime[seq][pos]) {
							break;
						}
						if (j + minTimeToNextElement[seq][pos] > windowEndTime[seq][pos + 1]) {/// getMaxArrivalTimeForNextArrival(seq, pos)) {
							break;
						}
						if (j + minTimeToNextElement[seq][pos] > arrivalTimes[seq][pos + 1]) {/// getMaxArrivalTimeForNextArrival(seq, pos)) {
							break;
						}
						long value = getValue(seq, pos, j, breakSequence);
						if (value > bestValue) {
							arrivalTimes[seq][pos] = j;
							bestValue = value;
							// Mark this time window as changed
							changedIdx[pos] = true;
							// Mark global change in sequence windows.
							changed = true;
						} else if (value <= bestValue) {
							ISequenceElement thisElement = sequence.get(pos);
							IPortSlot thisSlot = portSlotProvider.getPortSlot(thisElement);
							if (thisSlot instanceof IDischargeOption) {

							} else {
								// Increasing cost, break out. (leads to local minima solutions)

								break;
							}
						}
					}
				}
			}
		}
	}

	private long getValue(int seq, int pos, int arrivalTime, boolean[] breakSequence) {
		boolean isSequenceBreak = breakSequence[pos];

		IResource resource = this.sequences.getResources().get(seq);
		@NonNull
		ISequence sequence = this.sequences.getSequence(resource);
		ISequenceElement thisElement = sequence.get(pos);
		IPortSlot thisSlot = portSlotProvider.getPortSlot(thisElement);

		@NonNull
		IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		// TODO: Handle better?
		long heelVolumeInM3 = vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
		int lastIndex = sequence.size();
		if (isSequenceBreak) {
			VoyagePlan leftPlan = null;
			VoyagePlan rightPlan = null;
			PortTimesRecord leftRecord = null;
			PortTimesRecord rightRecord = null;
			LOOP_SEARCH: for (int i = pos - 1; i >= 0; --i) {
				if (i == 0 || breakSequence[i]) {
					// GEt Left Plan
					{
						PortTimesRecord record = new PortTimesRecord();
						for (int j = i; j < pos; ++j) {
							ISequenceElement element = sequence.get(j);
							IPortSlot slot = portSlotProvider.getPortSlot(element);
							record.setSlotTime(slot, arrivalTimes[seq][j]);
							record.setSlotDuration(slot, durationProvider.getElementDuration(element, resource));
						}
						record.setReturnSlotTime(thisSlot, arrivalTime);
						long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, record.getFirstSlotTime(), record.getFirstSlotTime());
						leftPlan = voyagePlanner.makeVoyage(resource, vesselCharterInRatePerDay, record, heelVolumeInM3);
						leftRecord = record;
					}

					// Get right plan
					{
						PortTimesRecord record = new PortTimesRecord();
						record.setSlotTime(thisSlot, arrivalTime);
						record.setSlotDuration(thisSlot, durationProvider.getElementDuration(thisElement, resource));
						for (int j = pos + 1; j < lastIndex; ++j) {
							ISequenceElement element = sequence.get(j);
							IPortSlot slot = portSlotProvider.getPortSlot(element);
							if (breakSequence[j]) {
								record.setReturnSlotTime(slot, arrivalTimes[seq][j]);
								break;
							} else {
								record.setSlotTime(slot, arrivalTimes[seq][j]);
								record.setSlotDuration(slot, durationProvider.getElementDuration(element, resource));
							}
						}
						long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, record.getFirstSlotTime(), record.getFirstSlotTime());
						rightPlan = voyagePlanner.makeVoyage(resource, vesselCharterInRatePerDay, record, heelVolumeInM3);
						rightRecord = record;
					}
					break LOOP_SEARCH;
				}
			}
			long value = 0;
			if (leftPlan != null) {
				value += getValue(leftPlan, leftRecord, vesselAvailability, arrivalTime);
			}
			if (rightPlan != null) {
				value += getValue(rightPlan, rightRecord, vesselAvailability, arrivalTime);
			} else {
				assert pos + 1 == lastIndex;
			}
			return value;
		} else {
			VoyagePlan plan = null;
			PortTimesRecord record = null;
			{
				for (int i = Math.max(0, pos - 1); i >= 0; --i) {
					if (pos == 0 || breakSequence[i]) {
						record = new PortTimesRecord();

						for (int j = i; j < lastIndex; ++j) {
							ISequenceElement element = sequence.get(j);
							if (element.getName().contains("CPK-2")) {
								int ii = 90;
							}
							IPortSlot slot = portSlotProvider.getPortSlot(element);
							if (j != i && breakSequence[j]) {
								record.setReturnSlotTime(slot, arrivalTimes[seq][j]);
								break;
							} else {
								if (j == pos) {
									record.setSlotTime(slot, arrivalTime);
								} else {
									record.setSlotTime(slot, arrivalTimes[seq][j]);
								}
								record.setSlotDuration(slot, durationProvider.getElementDuration(element, resource));
							}
						}
						long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, record.getFirstSlotTime(), record.getFirstSlotTime());
						plan = voyagePlanner.makeVoyage(resource, vesselCharterInRatePerDay, record, heelVolumeInM3);
						break;
					}
				}
			}
			if (plan == null) {
				return Long.MIN_VALUE;
			}
			return getValue(plan, record, vesselAvailability, arrivalTime);
		}
	}

	@Inject
	private ShippingCostHelper h;

	long getValue(VoyagePlan plan, IPortTimesRecord record, IVesselAvailability vesselAvailability, int arrivalTIme) {

		if (record.getFirstSlot() instanceof ILoadOption) {
			long margin = 0;
			int cv = 0;
			long purchaseCost = 0;
			for (IDetailsSequenceElement e : plan.getSequence()) {
				if (e instanceof PortDetails) {
					@NonNull
					IPortSlot portSlot = ((PortDetails) e).getOptions().getPortSlot();
					if (portSlot instanceof ILoadOption) {
						ILoadOption iLoadOption = (ILoadOption) portSlot;
						cv = iLoadOption.getCargoCVValue();
						long a = iLoadOption.getLoadPriceCalculator().getEstimatedPurchasePrice(iLoadOption, (@NonNull IDischargeOption) record.getSlots().get(1), record.getSlotTime(iLoadOption));
						long lngMMBTU = Calculator.convertM3ToMMBTu(Math.min(vesselAvailability.getVessel().getCargoCapacity(), iLoadOption.getMaxLoadVolume()), cv);

						purchaseCost = Calculator.costFromConsumption(lngMMBTU, a);
					} else if (portSlot instanceof IDischargeOption) {
						IDischargeOption dischargeOption = (IDischargeOption) portSlot;
						margin += dischargeOption.getDischargePriceCalculator().getEstimatedSalesPrice((ILoadOption) record.getFirstSlot(), dischargeOption, record.getSlotTime(dischargeOption));
					}
				}
			}

			long lngMMBTU = Calculator.convertM3ToMMBTu(plan.getLNGFuelVolume(), cv);
			long lngValue = Calculator.costFromConsumption(lngMMBTU, margin);

			// Return Pair<Cost,ViolationCount>
			return -(h.getShippingCosts(plan, vesselAvailability, false, true) + lngValue + purchaseCost);
		} else {
			return -h.getShippingCosts(plan, vesselAvailability, true, true);
		}
	}
}
