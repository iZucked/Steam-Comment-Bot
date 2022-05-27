/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.RoundTripCargoEnd;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

@NonNullByDefault
public class PNLBasedWindowTrimmerUtils {
	public static final String ENABLE_BACKWARDS_NBO_CHECK = "ENABLE_BACKWARDS_NBO_CHECK";

	public static class TimeChoice implements Comparable<TimeChoice> {
		public final int time;
		public final boolean important;

		public TimeChoice(final int time, final boolean important) {
			this.time = time;
			this.important = important;
		}

		public static TimeChoice forImportant(final int time) {
			return new TimeChoice(time, true);
		}

		public static TimeChoice forNormal(final int time) {
			return new TimeChoice(time, false);
		}

		@Override
		public boolean equals(@Nullable final Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof TimeChoice) {
				final TimeChoice other = (TimeChoice) obj;
				return this.time == other.time && this.important == other.important;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return time;
		}

		@Override
		public int compareTo(final TimeChoice o) {
			int c = Integer.compare(this.time, o.time);
			if (c == 0) {
				c = Boolean.compare(important, o.important);
			}
			return c;
		}

		/**
		 * Filter out unimportant times if there exists the same time as an important
		 * option.
		 */
		public static void filter(final Collection<TimeChoice> times) {
			final Collection<Integer> importantTimes = new TreeSet<>();
			for (final TimeChoice t : times) {
				if (t.important) {
					importantTimes.add(t.time);
				}
			}
			times.removeIf(t -> !t.important && importantTimes.contains(t.time));
		}
	}

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IPriceIntervalProducer priceIntervalProducer;

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	@Named(SchedulerConstants.MIDNIGHT_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve midnightIntervalsInHoursCurve;

	@Inject
	private IPanamaBookingsProvider panamaBookingsProvider;

	// Compute best NBO based arrival time in initial interval computation
	private static final boolean PRE_FORWARDS_NBO_TIME = true; // True fixes P case 1 issue (fixes 6k loss and gains 12k overall)

	// Compute best NBO based departure time in initial interval computation (see
	// next flags)
	@Inject(optional = true)
	@Named(ENABLE_BACKWARDS_NBO_CHECK)
	private final boolean PRE_BACKWARDS_NBO_TIME = true;

	// If PRE_BACKWARDS_NBO_TIME is true, then compute for laden or ballast legs?
	private static final boolean BACKWARDS_NBO_TIME_LADEN = true;
	private static final boolean BACKWARDS_NBO_TIME_BALLAST = true;

	private final boolean SPEED_STEP_ENABLE = false;
	private final int SPEED_STEP_RETAIN_COUNT = 5;
	private final int SPEED_STEP_COST_DIFF = 30_000_000;
	private final int SPEED_STEP_KNOTS_INCREMENT = OptimiserUnitConvertor.convertToInternalSpeed(.1);
	private final int DEFAULT_CV = OptimiserUnitConvertor.convertToInternalConversionFactor(22.67);

	public List<TimeChoice> computeBasicIntervalsForSlot(final IPortTimeWindowsRecord portTimeWindowsRecord, final IPortSlot slot) {
		if (slot instanceof ILoadOption) {
			final ILoadOption load = (ILoadOption) slot;
			List<int[]> loadPriceIntervals = null;

			if (load.getLoadPriceCalculator() instanceof IPriceIntervalProvider) {
				if (priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowsRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowsRecord))) {

					// Loading intervals
					loadPriceIntervals = getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowsRecord, load);
				} else {
					loadPriceIntervals = getLoadPriceIntervalsBasedOnDischarge(portTimeWindowsRecord, load);
				}
			}
			if (loadPriceIntervals != null) {
				final List<TimeChoice> arrivalTimeIntervals = new LinkedList<>();
				for (int j = 0; j < loadPriceIntervals.size(); ++j) {
					// Each interval is the start of the next price...
					int t = loadPriceIntervals.get(j)[0];
					if (j == loadPriceIntervals.size() - 1) {
						// .. this means the last interval is an exclusive time
						--t;
					}
					arrivalTimeIntervals.add(TimeChoice.forImportant(t));
				}
				return arrivalTimeIntervals;
			}
		} else if (slot instanceof IDischargeOption) {
			final IDischargeOption discharge = (IDischargeOption) slot;
			List<int[]> dischargePriceIntervals = null;
			if (discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
				if (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowsRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowsRecord))) {
					dischargePriceIntervals = getDischargePriceIntervalsIndependentOfLoad(portTimeWindowsRecord, discharge);
				} else {
					dischargePriceIntervals = getDischargePriceIntervalsBasedOnLoad(portTimeWindowsRecord, discharge);
				}
			}
			if (dischargePriceIntervals != null) {
				final List<TimeChoice> arrivalTimeIntervals = new LinkedList<>();
				for (int j = 0; j < dischargePriceIntervals.size(); ++j) {
					// Each interval is the start of the next price...
					int t = dischargePriceIntervals.get(j)[0];
					if (j == dischargePriceIntervals.size() - 1) {
						// .. this means the last interval is an exclusive time
						--t;
					}
					arrivalTimeIntervals.add(TimeChoice.forImportant(t));
				}

				return arrivalTimeIntervals;
			}

		}
		if (slot instanceof IEndPortSlot) {
			final ITimeWindow tw = portTimeWindowsRecord.getSlotFeasibleTimeWindow(slot);
			final List<TimeChoice> arrivalTimeIntervals = new LinkedList<>();
			arrivalTimeIntervals.add(TimeChoice.forImportant(tw.getInclusiveStart()));
			if (tw.getExclusiveEnd() != Integer.MAX_VALUE) {
				arrivalTimeIntervals.add(TimeChoice.forImportant(tw.getExclusiveEnd() - 1));
			}
			return arrivalTimeIntervals;
		} else if (slot instanceof RoundTripCargoEnd) {
			final ITimeWindow tw = portTimeWindowsRecord.getSlotFeasibleTimeWindow(slot);
			final List<TimeChoice> arrivalTimeIntervals = new LinkedList<>();
			arrivalTimeIntervals.add(TimeChoice.forImportant(tw.getInclusiveStart()));
			if (tw.getExclusiveEnd() != Integer.MAX_VALUE) {
				arrivalTimeIntervals.add(TimeChoice.forImportant(tw.getExclusiveEnd() - 1));
			}
			return arrivalTimeIntervals;

		} else {
			final ITimeWindow tw = portTimeWindowsRecord.getSlotFeasibleTimeWindow(slot);

			final List<TimeChoice> arrivalTimeIntervals = new LinkedList<>();
			arrivalTimeIntervals.add(TimeChoice.forImportant(tw.getInclusiveStart()));
			arrivalTimeIntervals.add(TimeChoice.forImportant(tw.getExclusiveEnd() - 1));
			return arrivalTimeIntervals;
		}
	}

	public List<int[]> getLoadPriceIntervalsIndependentOfDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getInclusiveStart(), loadTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getLoadIntervalsIndependentOfDischarge(load, portTimeWindowRecord));
	}

	public List<int[]> getDischargePriceIntervalsIndependentOfLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getInclusiveStart(), dischargeTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getDischargeWindowIndependentOfLoad(discharge, portTimeWindowRecord));
	}

	public List<int[]> getLoadPriceIntervalsBasedOnDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getInclusiveStart(), loadTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getLoadIntervalsBasedOnDischarge(load, portTimeWindowRecord));
	}

	public List<int[]> getDischargePriceIntervalsBasedOnLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getInclusiveStart(), dischargeTimeWindow.getExclusiveEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord));
	}

	public void computeIntervalsForSlot(final IPortSlot toPortSlot, final IVesselCharter vesselCharter, final int vesselStartTime, final PortTimesRecord _record,
			final IPortTimeWindowsRecord ptwr, final List<IPortSlot> slots, final int toSlotIdxInRecordArray, final MinTravelTimeData minTimeData, final int lastSlotArrivalTime,
			final Set<TimeChoice> times) {
		// This index in the index in the record slots array
		final IPortSlot fromPortSlot = slots.get(toSlotIdxInRecordArray - 1);

		// This index is the index in the entire sequence - for use in travel time array
		final int fromElementIndexInSequence = ptwr.getIndex(fromPortSlot);

		ITimeWindow slotFeasibleTimeWindow = ptwr.getSlotFeasibleTimeWindow(toPortSlot);
		if (slotFeasibleTimeWindow == null) {
			slotFeasibleTimeWindow = toPortSlot.getTimeWindow();
		}
		int twStart = slotFeasibleTimeWindow.getInclusiveStart();
		int twEnd = slotFeasibleTimeWindow.getExclusiveEnd() - 1;

		{
			final IVessel vessel = vesselCharter.getVessel();
			int calculationCV;
			AvailableRouteChoices lastRouteChoices = null;
			lastRouteChoices = AvailableRouteChoices.OPTIMAL;
			if (fromElementIndexInSequence >= 0 && fromPortSlot != null) {
				final VesselState vesselState = fromPortSlot instanceof ILoadOption ? VesselState.Laden : VesselState.Ballast;
				if (fromPortSlot instanceof ILoadOption) {
					final ILoadOption iLoadOption = (ILoadOption) fromPortSlot;
					calculationCV = iLoadOption.getCargoCVValue();
				} else {
					calculationCV = DEFAULT_CV;
				}
				final int nboSpeed = Math.min(Math.max(getNBOSpeed(vessel, vesselState, calculationCV), vessel.getMinSpeed()), vessel.getMaxSpeed());

				final List<@NonNull DistanceMatrixEntry> allDistanceValues = distanceProvider.getDistanceValues(fromPortSlot.getPort(), toPortSlot.getPort(), vessel, lastRouteChoices);

				final List<RouteCostRecord> extraTimes = new LinkedList<>();
				for (final DistanceMatrixEntry dme : allDistanceValues) {
					if (dme.getDistance() == Integer.MAX_VALUE) {
						continue;
					}
					if (dme.getDistance() == 0) {
						continue;
					}

					if (SPEED_STEP_ENABLE && toPortSlot instanceof IEndPortSlot) {
						int minSpeed = nboSpeed;// vessel.getMinSpeed();
						// round min and max speeds to allow better speed stepping
						minSpeed = roundUpToNearest(minSpeed, 100);
						final int maxSpeed = vessel.getMaxSpeed();

						// min speed needs to be bounded!
						minSpeed = Math.min(minSpeed, maxSpeed);

						// loop through speeds and canals
						int speed = minSpeed;

						final int baseTime = lastSlotArrivalTime + ptwr.getSlotDuration(fromPortSlot) //
								+ ptwr.getSlotTotalExtraIdleTime(fromPortSlot) //
								+ routeCostProvider.getRouteTransitTime(dme.getRoute(), vessel);

						// Window bounds will be taken care of later.
						final PortTimesRecord copy = _record.copy();
						final int[] baseFuelPrices = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, copy.getFirstSlotTime());
						while (speed <= maxSpeed) {
							final int vesselTravelTimeInHours = Calculator.getTimeFromSpeedDistance(speed, dme.getDistance());
							final int t = baseTime + vesselTravelTimeInHours;
							// This is ok! only on a temporary copy of the data for this method
							copy.setSlotTime(toPortSlot, t);

							if (slotFeasibleTimeWindow.contains(t) && fromPortSlot instanceof IDischargeOption) {

								final IDischargeOption iDischargeOption = (IDischargeOption) fromPortSlot;
								final int p = iDischargeOption.getDischargePriceCalculator().estimateSalesUnitPrice(iDischargeOption, copy, null);
								final RouteCostRecord rr = new RouteCostRecord();
								rr.time = t;
								// Add in canal cost
								rr.cost += routeCostProvider.getRouteCost(dme.getRoute(), fromPortSlot.getPort(), toPortSlot.getPort(), vessel,
										lastSlotArrivalTime + ptwr.getSlotDuration(fromPortSlot), CostType.Ballast);

								// Add charter cost
								rr.cost += vesselCharter.getCharterCostCalculator().getCharterCost(lastSlotArrivalTime, lastSlotArrivalTime, t - lastSlotArrivalTime);
								// Assume NBO + FBO
								{
									// estimate speed and rate
									final long rateVoyage = vessel.getConsumptionRate(vesselState).getRate(speed);

									final long requiredTotalFuelMT = (rateVoyage * vesselTravelTimeInHours) / 24L;
									final long requiredTotalFuelM3 = Calculator.convertMTToM3(requiredTotalFuelMT, calculationCV, vessel.getTravelBaseFuel().getEquivalenceFactor());
									final long requiredTotalFuelMMBTu = Calculator.convertM3ToMMBTu(requiredTotalFuelM3, calculationCV);

									rr.cost += Calculator.costFromConsumption(requiredTotalFuelMMBTu, p);
								}

								extraTimes.add(rr);
							}
							if (slotFeasibleTimeWindow.contains(t)) {

								final RouteCostRecord rr = new RouteCostRecord();
								rr.time = t;
								// Add in canal cost
								rr.cost += routeCostProvider.getRouteCost(dme.getRoute(), fromPortSlot.getPort(), toPortSlot.getPort(), vessel,
										lastSlotArrivalTime + ptwr.getSlotDuration(fromPortSlot), CostType.Ballast);

								// Add charter cost
								rr.cost += vesselCharter.getCharterCostCalculator().getCharterCost(copy.getFirstSlotTime(), lastSlotArrivalTime, t - lastSlotArrivalTime);
								// Assume NBO + FBO
								{
									// estimate speed and rate
									final long rateVoyage = vessel.getConsumptionRate(vesselState).getRate(speed);

									final long requiredTotalFuelMT = (rateVoyage * vesselTravelTimeInHours) / 24L;

									rr.cost += Calculator.costFromConsumption(requiredTotalFuelMT, baseFuelPrices[vessel.getTravelBaseFuel().getIndex()]);
								}

								extraTimes.add(rr);
							}

							// Termination condition.
							if (speed >= maxSpeed) {
								break;
							}
							// Prepare for next iteration
							speed += SPEED_STEP_KNOTS_INCREMENT;

							// Clamp to max speed
							if (speed > maxSpeed) {
								speed = maxSpeed;
							}
						}
					} else {

						final int nbotravelTime = ptwr.getSlotDuration(fromPortSlot) //
								+ Calculator.getTimeFromSpeedDistance(nboSpeed, dme.getDistance()) //
								+ routeCostProvider.getRouteTransitTime(dme.getRoute(), vessel) //
								+ ptwr.getSlotTotalExtraIdleTime(fromPortSlot) //
						;

						final int t = lastSlotArrivalTime;
						{
							final int fastestTime = t + minTimeData.getMinTravelTime(fromElementIndexInSequence);
							times.add(TimeChoice.forNormal(Math.max(twStart, fastestTime)));

							final int nboTime = t + Math.max(nbotravelTime, minTimeData.getTravelTime(dme.getRoute(), fromElementIndexInSequence));
							if (nboTime < slotFeasibleTimeWindow.getExclusiveEnd()) {
								times.add(TimeChoice.forNormal(Math.max(twStart, nboTime)));
							}
						}
					}
				}

				if (SPEED_STEP_ENABLE && !extraTimes.isEmpty()) {
					// Sort cheapest cost first
					extraTimes.sort((a, b) -> Long.compare(a.cost, b.cost));
					// Filter out similar results.
					RouteCostRecord last = null;
					final Iterator<RouteCostRecord> itr = extraTimes.iterator();
					while (itr.hasNext()) {

						final RouteCostRecord next = itr.next();
						if (last == null) {
							last = next;
							continue;
						}
						if (
						// (Math.abs(next.time - last.time) > 6) ||
						(next.cost - last.cost > SPEED_STEP_COST_DIFF)) {
							last = next;
						} else {
							itr.remove();
						}
					}
					while (extraTimes.size() > SPEED_STEP_RETAIN_COUNT) {
						extraTimes.remove(SPEED_STEP_RETAIN_COUNT);
					}
					// Add remaining times to the array
					extraTimes.forEach(r -> times.add(TimeChoice.forNormal(r.time)));
				}

			}
		}

		alignTimeChoicesToMidnightLocalTime(times, 24, toPortSlot);
		if (toPortSlot instanceof IEndPortSlot) {

			final IResource resource = vesselProvider.getResource(vesselCharter);
			final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
			if (endRequirement != null) {
				// Flag this sequence up as a min / max duration case
				final boolean durationBasedSchedule = endRequirement.isMinDurationSet() || endRequirement.isMaxDurationSet();
				if (durationBasedSchedule) {
					// Case 1. We have not used up our min duration.
					// First try to pad out the end.
					// Then pad out start.
					// Then finish at the end.
					if (endRequirement.isMinDurationSet()) {
						final int minEndTime = vesselStartTime + endRequirement.getMinDurationInHours();
						times.add(TimeChoice.forImportant(minEndTime));
						if (minEndTime > twStart) {
							// Adjust time time windows
							twStart = Math.max(minEndTime, twStart);
							twEnd = Math.max(twStart, twEnd);
						}
					}

					if (endRequirement.isMaxDurationSet()) {
						final int maxEndTime = vesselStartTime + endRequirement.getMaxDurationInHours();
						times.add(TimeChoice.forImportant(maxEndTime));
						if (maxEndTime < twEnd) {
							// Reduce end time
							twEnd = Math.min(maxEndTime, twEnd);
							// But increase it if needed to meet the start.
							twEnd = Math.max(twEnd, twStart);
						}
					}
				}
			}
		}

		final int pTWStart = twStart;
		final int pTWEnd = twEnd;
		{
			// Remove anything too early or late
			times.removeIf(t -> t.time > pTWEnd);
			times.removeIf(t -> t.time < pTWStart);
		}
		// Ensure the quickest time is added and remove anything too early
		{
			int time = lastSlotArrivalTime + minTimeData.getMinTravelTime(fromElementIndexInSequence);
			time = Math.max(time, pTWStart);
			times.add(TimeChoice.forImportant(time));
			final int pTime = time;
			// Remove anything too early.
			times.removeIf(t -> t.time < pTime);
		}
	}

	public ImmutableMap<IPortSlot, ImmutableList<TimeChoice>> computeDefaultIntervals(final List<IPortTimeWindowsRecord> records, final IResource resource, final MinTravelTimeData travelTimeData) {
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);

		final IVessel vessel = vesselCharter.getVessel();
		final boolean roundTripCargo = vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		IPortSlot lastSlot = null;
		Collection<TimeChoice> lastIntervals = null;
		AvailableRouteChoices lastRouteChoices = null;

		final Map<IPortSlot, Collection<TimeChoice>> intervalMap = new HashMap<>();

		final List<IPortSlot> slots = new LinkedList<>();
		final Map<IPortSlot, IPortTimeWindowsRecord> slotToRecords = new HashMap<>();

		// Forward loop. Collect basic arrival intervals and the NBO based speed to next
		// slot (if within window)
		for (final IPortTimeWindowsRecord record : records) {
			int calculationCV = DEFAULT_CV;
			if (roundTripCargo) {
				lastSlot = null;
				lastRouteChoices = AvailableRouteChoices.OPTIMAL;
			}

			List<IPortSlot> recordSlots = new LinkedList<>(record.getSlots());
			IPortSlot returnSlot = record.getReturnSlot();
			if (roundTripCargo && returnSlot != null) {
				recordSlots.add(returnSlot);
			}
			for (final IPortSlot slot : recordSlots) {
				slotToRecords.put(slot, record);
				slots.add(slot);

				final int elementIndex = record.getIndex(slot);

				final Collection<TimeChoice> intervals = new TreeSet<>();
				for (final TimeChoice t : computeBasicIntervalsForSlot(record, slot)) {
					intervals.add(t);
				}

				final ITimeWindow tw = record.getSlotFeasibleTimeWindow(slot);
				// Look at NBO speed from the previous slot to this one.
				if (elementIndex > 0 && lastSlot != null) {
					if (PRE_FORWARDS_NBO_TIME && lastSlot instanceof ILoadOption) {
						final VesselState vesselState = lastSlot instanceof ILoadOption ? VesselState.Laden : VesselState.Ballast;
						if (lastSlot instanceof ILoadOption) {
							final ILoadOption iLoadOption = (ILoadOption) lastSlot;
							calculationCV = iLoadOption.getCargoCVValue();
						}
						final int nboSpeed = Math.min(Math.max(getNBOSpeed(vessel, vesselState, calculationCV), vessel.getMinSpeed()), vessel.getMaxSpeed());

						final List<@NonNull DistanceMatrixEntry> allDistanceValues = distanceProvider.getDistanceValues(lastSlot.getPort(), slot.getPort(), vessel, lastRouteChoices);

						for (final DistanceMatrixEntry dme : allDistanceValues) {
							if (dme.getDistance() == Integer.MAX_VALUE) {
								continue;
							}

							// If we have a Panama booking allocated, the booking date constrains the
							// departure and arrival times. The min travel time data is unable to accurately
							// encode this for
							// Panama.
							if (dme.getRoute() == ERouteOption.PANAMA //
									&& record.getSlotNextVoyageOptions(lastSlot) == AvailableRouteChoices.PANAMA_ONLY //
									&& record.getRouteOptionBooking(lastSlot) != null //
							) {

								final int bookingDate = record.getRouteOptionBooking(lastSlot).getBookingDate();

								final ECanalEntry exit = distanceProvider.getRouteOptionCanalEntrance(slot.getPort(), ERouteOption.PANAMA);
								final IPort exitPort = distanceProvider.getRouteOptionEntryPort(ERouteOption.PANAMA, exit);
								final int distanceFromCanal = distanceProvider.getDistance(ERouteOption.DIRECT, exitPort, slot.getPort(), vessel);

								// Time from canal booking.
								final int nboTravelTime = (distanceFromCanal == 0) ? 0 : Calculator.getTimeFromSpeedDistance(nboSpeed, distanceFromCanal);
								final int maxSpeedTravelTime = (distanceFromCanal == 0) ? 0 : Calculator.getTimeFromSpeedDistance(vessel.getMaxSpeed(), distanceFromCanal);

								final int transitTime = routeCostProvider.getRouteTransitTime(dme.getRoute(), vessel);
								final int extraIdleTime = record.getSlotTotalExtraIdleTime(lastSlot);

								// Feasible trimmer should have made sure this is valid.
								// assert lastSlotArrivalTime + visitDuration + timeToCanal < bookingDate -
								// arrivalMargin;

								// This is the fixed time components to add on to the booking date before we add
								// on the speed based elements.
								final int baseTime = bookingDate + transitTime + extraIdleTime;

								final int fastestArrivalTime = baseTime + maxSpeedTravelTime;
								if (fastestArrivalTime < tw.getExclusiveEnd()) {
									intervals.add(TimeChoice.forNormal(Math.max(tw.getInclusiveStart(), fastestArrivalTime)));
								}
								final int nboBasedArrivalTime = baseTime + nboTravelTime;
								if (nboBasedArrivalTime < tw.getExclusiveEnd()) {
									intervals.add(TimeChoice.forNormal(Math.max(tw.getInclusiveStart(), nboBasedArrivalTime)));
								}

							} else {
								final int nboTravelTime = (dme.getDistance() == 0) ? 0
										: Calculator.getTimeFromSpeedDistance(nboSpeed, dme.getDistance()) + routeCostProvider.getRouteTransitTime(dme.getRoute(), vessel);

								final int totalTravelTime = record.getSlotDuration(lastSlot) + nboTravelTime + record.getSlotTotalExtraIdleTime(lastSlot);

								for (final TimeChoice lastSlotArrivalTime : lastIntervals) {
									final int fastestArrivalTime = lastSlotArrivalTime.time + travelTimeData.getTravelTime(dme.getRoute(), elementIndex - 1);
									if (fastestArrivalTime < tw.getExclusiveEnd()) {
										intervals.add(TimeChoice.forNormal(Math.max(tw.getInclusiveStart(), fastestArrivalTime)));
									}
									final int nboBasedArrivalTime = lastSlotArrivalTime.time + totalTravelTime;
									if (nboBasedArrivalTime < tw.getExclusiveEnd()) {
										intervals.add(TimeChoice.forNormal(Math.max(tw.getInclusiveStart(), nboBasedArrivalTime)));
									}
								}
							}
						}
					} else {
						final List<@NonNull DistanceMatrixEntry> allDistanceValues = distanceProvider.getDistanceValues(lastSlot.getPort(), slot.getPort(), vessel, lastRouteChoices);
						for (final DistanceMatrixEntry dme : allDistanceValues) {
							if (dme.getDistance() == Integer.MAX_VALUE) {
								continue;
							}

							for (final TimeChoice lastSlotArrivalTime : lastIntervals) {
								final int fastestArrivalTime = lastSlotArrivalTime.time + travelTimeData.getTravelTime(dme.getRoute(), elementIndex - 1);
								if (fastestArrivalTime < tw.getExclusiveEnd()) {
									intervals.add(TimeChoice.forNormal(Math.max(tw.getInclusiveStart(), fastestArrivalTime)));
								}
							}
						}
					}
				}
				intervalMap.put(slot, new HashSet<>(intervals));

				// Update for next iteration.
				lastRouteChoices = record.getSlotNextVoyageOptions(slot);
				lastIntervals = intervals;

				lastSlot = slot;
			}
		}

		// Re-align to midnight, local time
		intervalMap.entrySet().forEach(e -> alignTimeChoicesToMidnightLocalTime(e.getValue(), 0, e.getKey()));

		if (PRE_BACKWARDS_NBO_TIME) {

			final IStartRequirement startRequirement = startEndRequirementProvider.getStartRequirement(resource);

			// Reverse pass, given each arrival time, work out when we would need to arrive
			// at the previous slot if we wanted to use NBO speed.
			// Work backwards for NBO times
			for (int i = slots.size() - 1; i > 0; --i) {

				lastSlot = slots.get(i - 1);

				if (lastSlot instanceof RoundTripCargoEnd) {
					continue;
				}

				final IPortSlot slot = slots.get(i);
				final IPortTimeWindowsRecord record = slotToRecords.get(lastSlot);
				lastRouteChoices = record.getSlotNextVoyageOptions(lastSlot);
				final ITimeWindow lTW = record.getSlotFeasibleTimeWindow(lastSlot);

				int calculationCV;
				final VesselState vesselState = lastSlot instanceof ILoadOption ? VesselState.Laden : VesselState.Ballast;
				if (lastSlot instanceof ILoadOption) {
					final ILoadOption iLoadOption = (ILoadOption) lastSlot;
					calculationCV = iLoadOption.getCargoCVValue();
					if (!BACKWARDS_NBO_TIME_LADEN) {
						continue;
					}
				} else {

					calculationCV = DEFAULT_CV;
					if (!BACKWARDS_NBO_TIME_BALLAST) {
						continue;
					}
				}

				if (i == 1 || (roundTripCargo && slot instanceof ILoadSlot)) {
					// Remove the start time of 0 as it is typically a default value that is not
					// much use
					if (startRequirement != null && !startRequirement.hasTimeRequirement()) {
						final Collection<TimeChoice> choices = intervalMap.get(lastSlot);
						if (choices.size() > 1) {
							choices.removeIf(t -> t.time == 0);
						}
					}
				}

				final List<@NonNull DistanceMatrixEntry> allDistanceValues = distanceProvider.getDistanceValues(lastSlot.getPort(), slot.getPort(), vessel, lastRouteChoices);
				for (final DistanceMatrixEntry dme : allDistanceValues) {
					if (dme.getDistance() == Integer.MAX_VALUE) {
						continue;
					}
					final int nboSpeed = Math.min(Math.max(getNBOSpeed(vessel, vesselState, calculationCV), vessel.getMinSpeed()), vessel.getMaxSpeed());

					// If we have a Panama booking allocated, the booking date constrains the
					// departure and arrival times. The min travel time data is unable to accurately
					// encode this for
					// Panama.
					if (dme.getRoute() == ERouteOption.PANAMA //
							&& record.getSlotNextVoyageOptions(lastSlot) == AvailableRouteChoices.PANAMA_ONLY //
							&& record.getRouteOptionBooking(lastSlot) != null //
					) {

						// Arrival time of the booking
						final int bookingDate = record.getRouteOptionBooking(lastSlot).getBookingDate();

						// Extra arrival time margin
						final int arrivalMargin = panamaBookingsProvider.getMarginInHours();

						// Get distance from origin to canal
						final ECanalEntry entrance = distanceProvider.getRouteOptionCanalEntrance(lastSlot.getPort(), ERouteOption.PANAMA);
						final IPort entrancePort = distanceProvider.getRouteOptionEntryPort(ERouteOption.PANAMA, entrance);
						final int distanceToCanal = distanceProvider.getDistance(ERouteOption.DIRECT, lastSlot.getPort(), entrancePort, vessel);

						// Travel time to the canal NBO speed and max speed.
						final int nboTravelTime = (distanceToCanal == 0) ? 0 : Calculator.getTimeFromSpeedDistance(nboSpeed, distanceToCanal);
						final int maxSpeedTravelTime = (distanceToCanal == 0) ? 0 : Calculator.getTimeFromSpeedDistance(vessel.getMaxSpeed(), distanceToCanal);

						final int visitDuration = record.getSlotDuration(lastSlot);

						// Calculate the arrival time of the from slot - work backwards from the booking
						// date. These are the fixed components, travel time added next
						final int baseTime = bookingDate - arrivalMargin - visitDuration;
						{
							// NBO based arrival time
							final int t = baseTime - nboTravelTime;
							if (lastSlot.getTimeWindow() == null || (t >= lTW.getInclusiveStart() && t < lTW.getExclusiveEnd())) {
								intervalMap.get(lastSlot).add(TimeChoice.forNormal(t));
							}
						}
						{
							// Max speed based arrival time
							final int t = baseTime - maxSpeedTravelTime;
							if (lastSlot.getTimeWindow() == null || (t >= lTW.getInclusiveStart() && t < lTW.getExclusiveEnd())) {
								intervalMap.get(lastSlot).add(TimeChoice.forNormal(t));
							}
						}
					} else {

						final int nboTravelTime = dme.getDistance() == 0 ? 0
								: Calculator.getTimeFromSpeedDistance(nboSpeed, dme.getDistance()) + routeCostProvider.getRouteTransitTime(dme.getRoute(), vessel);

						final int totalTravelTime = record.getSlotDuration(lastSlot) + nboTravelTime + record.getSlotTotalExtraIdleTime(lastSlot);
						for (final TimeChoice t2 : intervalMap.get(slot)) {
							final int t = Math.max(0, t2.time - totalTravelTime);
							if (lastSlot.getTimeWindow() == null || (t >= lTW.getInclusiveStart() && t < lTW.getExclusiveEnd())) {
								intervalMap.get(lastSlot).add(TimeChoice.forNormal(t));
							}
						}
					}
				}
			}
		}

		// Re-align to midnight, local time
		intervalMap.entrySet().forEach(e -> alignTimeChoicesToMidnightLocalTime(e.getValue(), 0, e.getKey()));

		{

			final IStartRequirement startRequirement = startEndRequirementProvider.getStartRequirement(resource);
			final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);

			if (endRequirement != null) {

				BiConsumer<IPortTimeWindowsRecord, IPortTimeWindowsRecord> action = (firstRecord, endRecord) -> {

					final IPortSlot firstSlot = firstRecord.getFirstSlot();
					final IPortSlot endSlot = roundTripCargo ? endRecord.getReturnSlot() : endRecord.getFirstSlot();
					final ITimeWindow endFeasibleTW = endRecord.getSlotFeasibleTimeWindow(endSlot);

					//
					final int twEndSlotEnd = endFeasibleTW.getExclusiveEnd() - 1;
					//
					int newStartTime = -1;
					if (endRequirement.isMinDurationSet()) {
						final ITimeWindow startEventFeasibleTimeWindow = firstRecord.getFirstSlotFeasibleTimeWindow();
						final int earliestStartTime = startEventFeasibleTimeWindow.getInclusiveStart();

						// Add in valid start times to cover min duration to all end dates.
						for (final TimeChoice t : intervalMap.get(endSlot)) {
							final int newT = t.time - endRequirement.getMinDurationInHours();
							if (startEventFeasibleTimeWindow.contains(newT)) {
								intervalMap.get(firstSlot).add(TimeChoice.forNormal(newT));
							}
						}
						// Make sure there is an end time for the min duration from all the start times.
						for (final TimeChoice t : intervalMap.get(firstSlot)) {
							intervalMap.get(endSlot).add(TimeChoice.forImportant(t.time + endRequirement.getMinDurationInHours()));
							intervalMap.get(endSlot).add(TimeChoice.forImportant(t.time + endRequirement.getMaxDurationInHours()));
						}

						final int minEndTime = earliestStartTime + endRequirement.getMinDurationInHours();
						if (minEndTime > twEndSlotEnd) {
							// Too much time for given end window. Do we have some padding left at start?
							newStartTime = twEndSlotEnd - endRequirement.getMinDurationInHours();

							if (startRequirement != null && startRequirement.hasTimeRequirement()) {
								final ITimeWindow startTW = startRequirement.getTimeWindow();
								if (startTW != null) {
									final int inclusiveStart = startTW.getInclusiveStart();
									// Do not start before we are allowed to pick up the vessel
									newStartTime = Math.max(newStartTime, inclusiveStart);
								}
							}
							newStartTime = Math.max(newStartTime, 0);

							final int pNewStartTime = newStartTime;
							final Collection<TimeChoice> collection = intervalMap.get(firstSlot);
							collection.removeIf(t -> t.time > pNewStartTime);

							// Update start window
							// Sg: Is this a bug? Probably shouldn't be doing this?
							final ITimeWindow tw = firstRecord.getFirstSlotFeasibleTimeWindow();
							final MutableTimeWindow ftw = new MutableTimeWindow(Math.min(tw.getInclusiveStart(), pNewStartTime), Math.max(pNewStartTime + 1, tw.getExclusiveEnd()));
							firstRecord.setSlotFeasibleTimeWindow(firstSlot, ftw);
						}
					}
				};

				if (roundTripCargo) {
					// Each ptwr needs to be adjusted separately
					for (IPortTimeWindowsRecord ptwr : records) {
						action.accept(ptwr, ptwr);
					}
				} else {
					final IPortTimeWindowsRecord firstRecord = records.get(0);
					final IPortTimeWindowsRecord endRecord = records.get(records.size() - 1);
					action.accept(firstRecord, endRecord);
				}
			}
		}

		// Final re-alignment pass
		intervalMap.entrySet().forEach(e -> alignTimeChoicesToMidnightLocalTime(e.getValue(), 0, e.getKey()));

		final ImmutableMap.Builder<IPortSlot, ImmutableList<TimeChoice>> subMap = new ImmutableMap.Builder<>();
		for (final Map.Entry<IPortSlot, Collection<TimeChoice>> e : intervalMap.entrySet()) {
			subMap.put(e.getKey(), ImmutableList.copyOf(e.getValue()));
		}
		return subMap.build();
	}

	private int getNBOSpeed(final IVessel vessel, final VesselState vesselState, final int cv) {
		final long nboRateInM3PerHour = vessel.getNBORate(vesselState);
		final long nboProvidedInMT = Calculator.convertM3ToMT(nboRateInM3PerHour, cv, vessel.getTravelBaseFuel().getEquivalenceFactor());
		return vessel.getConsumptionRate(vesselState).getSpeed(nboProvidedInMT);
	}

	private static int roundUpToNearest(final int input, final int rounding) {
		return ((input + (rounding - 1)) / rounding) * rounding;
	}

	/**
	 * For each time option, add in midnight localtime before and after the option.
	 * Important options are retained, otherwise discarded. The intention of this
	 * code is to regularise arrival times (better for caching, better for users)
	 * and avoid tring 1am, 2am, 3am etc.
	 * 
	 * @param input
	 * @param x
	 * @param port
	 */
	private void alignTimeChoicesToMidnightLocalTime(final Collection<TimeChoice> input, final int x, final @Nullable IPortSlot port) {
		// Disabled for now as this typically will *increase* the number of intervals
		// found, rather than reduce them. In practice (with current code) more often
		// than not there are many options spread across the windows, but tend to be one
		// option a day rather than many on the same day.
		if (true) {
			TimeChoice.filter(input);
			return;
		}

		if (input.size() <= 2) {
			return;
		}

		TimeChoice lowerBound = null;
		TimeChoice upperBound = null;
		final Collection<TimeChoice> output = new LinkedHashSet<>();
		for (final TimeChoice tc : input) {

			if (lowerBound == null || tc.time < lowerBound.time) {
				lowerBound = tc;
			}
			if (upperBound == null || tc.time > upperBound.time) {
				upperBound = tc;
			}
			final int independentTime = timeZoneToUtcOffsetProvider.UTC(tc.time, port);
			try {
				final int lowerTime = midnightIntervalsInHoursCurve.getPreviousInterval(independentTime);
				final int localTime = timeZoneToUtcOffsetProvider.localTime(lowerTime, port);
				// Check we still have a valid time.
				if (localTime >= 0) {
					output.add(TimeChoice.forNormal(localTime));
				}
			} catch (final NullPointerException e) {
				// Ignore - assuming out of bounds
			}
			if (tc.important) {
				output.add(tc);
			}
			try {
				final int upperTime = midnightIntervalsInHoursCurve.getNextInterval(independentTime);
				output.add(TimeChoice.forNormal(timeZoneToUtcOffsetProvider.localTime(upperTime, port)));
			} catch (final NullPointerException e) {
				// Ignore - assuming out of bounds
			}
		}

		assert lowerBound != null;
		assert upperBound != null;

		input.clear();

		input.add(lowerBound);
		input.addAll(output);
		input.add(upperBound);

		TimeChoice.filter(input);
	}

}
