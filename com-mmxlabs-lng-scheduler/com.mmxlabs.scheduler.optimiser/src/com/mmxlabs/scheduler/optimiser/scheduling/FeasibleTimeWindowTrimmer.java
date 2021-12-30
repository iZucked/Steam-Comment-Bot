/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.optimiser.core.exceptions.InfeasibleSolutionException;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.RoundTripCargoEnd;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IExtraIdleTimeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IScheduledPurgeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.PanamaBookingHelper;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.IVoyageSpecificationProvider;
import com.mmxlabs.scheduler.optimiser.sequenceproviders.VoyageSpecificationProviderImpl;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;

/**
 * A sequence scheduler which enumerates possible combinations of arrival times
 * explicitly, rather than using the GA byte array decoding method. This should
 * be subclassable into a random sequence scheduler as well, with reduced
 * decoding overhead
 * 
 * @author hinton
 * 
 */
public class FeasibleTimeWindowTrimmer {

	/**
	 * How long to let empty time windows be. Since these mostly happen at the end
	 * of sequences we make this zero.
	 */
	private static final int EMPTY_WINDOW_SIZE = 0;

	private static class ElementRecord {

		/**
		 * Flag indicating a round trip end element. The next element should be
		 * considered to be the start of a sequence. (E.g. for the
		 * DirectRandomSequenceScheduler it should reset the seed).
		 */
		boolean isRoundTripEnd;
		/**
		 * The start times of each window, appropriately `clipped' to deal with
		 * infeasible choices or null time windows.
		 */
		int windowStartTime;
		/**
		 * The end times of each window, similar to start times.
		 */
		int windowEndTime = Integer.MAX_VALUE;
		/**
		 * Worst case tracker
		 */
		int worstWaitingTime;

		int visitDuration;
		int[] extraIdleTimes = new int[ExplicitIdleTime.values().length];

		/**
		 * Whether or not the {@link PortType} of any {@link PortSlot} associated with
		 * each sequence element has {@link PortType} Virtual. If a sequence element is
		 * virtual, the travel times should be held at zero and the virtual element and
		 * its neighbors should have the same time window.
		 * 
		 * Thus if we see A -> virtual -> B, the arrival time at B should be clamped to
		 * the arrival time at A. This is done by checks in the
		 * {@link #getMinArrivalTime(int, int)} and {@link #getMaxArrivalTime(int, int)}
		 * methods.
		 */
		boolean isVirtual;

		/**
		 * A flag to indicate that we should just use the timewindow rather than include
		 * the previous journey time. Intended for use with the cargo shorts where each
		 * cargo is indepenedent of the others on the route. Also used for actuals where
		 * forcast travel time is irrelevant.
		 */
		boolean useTimeWindow;
		/**
		 * Boolean indicating time is actualised and cannot be changed.
		 */
		boolean actualisedTimeWindow;

		PortTimeWindowsRecord ptr;

		/**
		 * Break points in the sequence for the start of each voyage plan
		 */
		boolean breakSequence;

	}

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IElementPortProvider portProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IScheduledPurgeProvider scheduledPurgeProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private Injector injector;

	@Inject
	private IExtraIdleTimeProvider extraIdleTimeProvider;

	@Inject
	private IPanamaBookingsProvider panamaBookingsProvider;

	@Inject
	private PanamaBookingHelper panamaBookingsHelper;

	@Inject
	@Named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)
	private boolean checkPanamaCanalBookings = false;

	@Inject
	@Named(SchedulerConstants.Key_UseBestPanamaCanalIdleDaysWindowTrimming)
	private boolean useBestCanalIdleDays = false;

	@Inject
	@Named(SchedulerConstants.Key_SchedulePurges)
	private boolean purgeSchedulingEnabled;

	private final TimeWindow defaultStartWindow = new TimeWindow(0, Integer.MAX_VALUE);

	private ElementRecord[] records;

	public FeasibleTimeWindowTrimmer() {
		super();
	}

	public final List<IPortTimeWindowsRecord> generateTrimmedWindows(final @NonNull IResource resource, final @NonNull ISequence sequence, final @NonNull MinTravelTimeData travelTimeData,
			final @NonNull CurrentBookingData currentBookingData, ISequencesAttributesProvider sequencesAttributesProvider) {

		final LinkedList<IPortTimeWindowsRecord> portTimeWindowRecords = new LinkedList<>();
		trimBasedOnMaxSpeed(resource, sequence, portTimeWindowRecords, travelTimeData, sequencesAttributesProvider);

		if (checkPanamaCanalBookings) {
			trimBasedOnPanamaCanal(resource, sequence, travelTimeData, currentBookingData);
		}

		for (int idx = 0; idx < portTimeWindowRecords.size(); idx++) {
			final IPortTimeWindowsRecord portTimeWindowsRecord = portTimeWindowRecords.get(idx);
			if (isSequentialVessel(resource)) {
				final boolean isLastRecord = idx == portTimeWindowRecords.size() - 1;
				setFeasibleTimeWindowsUsingPrevious(portTimeWindowsRecord, travelTimeData, isLastRecord);
			} else {
				setFeasibleTimeWindowsRoundTrip(portTimeWindowsRecord, travelTimeData);
			}

		}
		return portTimeWindowRecords;

	}

	/**
	 * Returns an array of boolean values indicating whether, for each index of the
	 * vessel location sequence, a sequence break occurs at that location
	 * (separating one cargo from the next one).
	 * 
	 * @param sequence
	 * @return
	 */
	private void findSequenceBreaks(final ISequence sequence, final boolean isRoundTripSequence, ElementRecord[] records) {

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			final PortType portType = portTypeProvider.getPortType(element);
			switch (portType) {
			case Load -> records[idx].breakSequence = !isRoundTripSequence && (idx > 0); // don't break on first load port
			case CharterOut, DryDock, Other, End, Round_Trip_Cargo_End -> records[idx].breakSequence = true;
			default -> records[idx].breakSequence = false;
			}
			idx++;
		}
	}

	protected final void trimBasedOnMaxSpeed(final @NonNull IResource resource, final @NonNull ISequence sequence, final List<IPortTimeWindowsRecord> portTimeWindowRecords,
			final MinTravelTimeData travelTimeData, ISequencesAttributesProvider sequencesAttributesProvider) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			// TODO: Implement something here rather than rely on VoyagePlanner
			return;
		}

		final int size = sequence.size();
		// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
		if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence, vesselAvailability)) {
			return;
		}

		resizeAll(size);

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		findSequenceBreaks(sequence, isRoundTripSequence, records);

		final int vesselMaxSpeed = vesselAvailability.getVessel().getMaxSpeed();
		final int vesselPurgeTime = vesselAvailability.getVessel().getPurgeTime();

		int index = 0;
		ISequenceElement prevElement = null;
		IPortSlot prevPortSlot = null;
		// Used for end of sequence checks
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();

		// first pass, collecting start time windows
		for (final ISequenceElement element : sequence) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			records[index].visitDuration = actualsDataProvider.hasActuals(thisPortSlot) ? actualsDataProvider.getVisitDuration(thisPortSlot) : durationProvider.getElementDuration(element, resource);

			final ITimeWindow window;

			IVoyageSpecificationProvider voyageSpecProvider = sequencesAttributesProvider.getProvider(IVoyageSpecificationProvider.class);
			// If there is an override, then we set the other travel times to MAX_VALUE
			Integer timeOverride = null;
			if (voyageSpecProvider != null) {
				timeOverride = voyageSpecProvider.getArrivalTime(thisPortSlot);
			}
			if (timeOverride != null) {
				window = new TimeWindow(timeOverride, timeOverride + 1);
			} else if (portTypeProvider.getPortType(element) == PortType.Start) {
				// Take element start window into account
				final IStartEndRequirement startRequirement = startEndRequirementProvider.getStartRequirement(resource);

				// "windows" defaults to the default start window
				ITimeWindow timeWindow = defaultStartWindow;

				// but can be overridden by a specified start requirement
				if (startRequirement != null && startRequirement.hasTimeRequirement()) {
					timeWindow = startRequirement.getTimeWindow();
				}

				window = timeWindow;
			} else if (portTypeProvider.getPortType(element) == PortType.End) {
				final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
				window = endRequirement.getTimeWindow();
				assert window != null;// End requirements should always have a window.
			} else if (thisPortSlot instanceof RoundTripCargoEnd) {
				records[index].isRoundTripEnd = true;
				// If this is null, the we will force discharge arrival as early as possible,
				// regardless of cost (or booking availability)
				window = null;// new TimeWindow(0, Integer.MAX_VALUE);
			} else {

				final IPortSlot prevSlot = prevElement == null ? null : portSlotProvider.getPortSlot(prevElement);
				if (prevSlot != null && actualsDataProvider.hasReturnActuals(prevSlot)) {
					window = actualsDataProvider.getReturnTimeAsTimeWindow(prevSlot);
					if (actualsDataProvider.hasActuals(portSlot)) {
						final int a = actualsDataProvider.getArrivalTime(portSlot);
						final int b = actualsDataProvider.getReturnTime(prevSlot);
						assert a == b;

					}
					records[index].actualisedTimeWindow = true;
				} else if (actualsDataProvider.hasActuals(portSlot)) {
					window = actualsDataProvider.getArrivalTimeWindow(portSlot);
					records[index].actualisedTimeWindow = true;
				} else {
					window = portSlot.getTimeWindow();
				}
			}

			if (prevElement != null) {
				assert prevPortSlot != null;

				records[index - 1].extraIdleTimes[ExplicitIdleTime.CONTINGENCY.ordinal()] = actualsDataProvider.hasActuals(thisPortSlot) ? 0
						: extraIdleTimeProvider.getContingencyIdleTimeInHours(prevPortSlot, thisPortSlot);
				records[index - 1].extraIdleTimes[ExplicitIdleTime.MARKET_BUFFER.ordinal()] = actualsDataProvider.hasActuals(thisPortSlot) ? 0
						: extraIdleTimeProvider.getBufferIdleTimeInHours(thisPortSlot);
				if (purgeSchedulingEnabled) {
					if (portTypeProvider.getPortType(prevElement) == PortType.DryDock) {
						records[index - 1].extraIdleTimes[ExplicitIdleTime.PURGE.ordinal()] = vesselPurgeTime;
					} else if (portTypeProvider.getPortType(element) == PortType.Load) {
						if (scheduledPurgeProvider.isPurgeScheduled(thisPortSlot)) {
							records[index - 1].extraIdleTimes[ExplicitIdleTime.PURGE.ordinal()] = vesselPurgeTime;
						}
					}
				}
				for (var type : ExplicitIdleTime.values()) {
					portTimeWindowsRecord.setSlotExtraIdleTime(prevPortSlot, type, records[index - 1].extraIdleTimes[type.ordinal()]);
				}
			}

			records[index].isRoundTripEnd = false;
			if (records[index].breakSequence) {
				assert prevPortSlot != null;

				// last slot in plan, set return

				portTimeWindowsRecord.setReturnSlot(thisPortSlot, null, records[index].visitDuration, index);
				// finalise record
				portTimeWindowRecords.add(portTimeWindowsRecord);
				// create new record
				if (!(thisPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setSlot(thisPortSlot, null, records[index].visitDuration, index);
				} else {
					records[index].isRoundTripEnd = true;
				}
			} else {
				if (!(prevPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord.setSlot(thisPortSlot, null, records[index].visitDuration, index);
				} else {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setSlot(thisPortSlot, null, records[index].visitDuration, index);

					records[index].isRoundTripEnd = true;
				}
			}

			records[index].ptr = portTimeWindowsRecord;

			records[index].isVirtual = portTypeProvider.getPortType(element) == PortType.Virtual;
			records[index].useTimeWindow = prevElement == null ? false : portTypeProvider.getPortType(prevElement) == PortType.Round_Trip_Cargo_End;

			// Calculate minimum inter-element durations
			travelTimeData.setMinTravelTime(index, records[index].visitDuration);
			int directTravelTime;
			int suezTravelTime;
			int panamaTravelTime;
			if (prevElement != null) {
				assert prevPortSlot != null;

				final IPort prevPort = portProvider.getPortForElement(prevElement);
				final IPort port = portProvider.getPortForElement(element);

				final int prevVisitDuration = records[index - 1].visitDuration;

				directTravelTime = distanceProvider.getTravelTime(ERouteOption.DIRECT, vesselAvailability.getVessel(), prevPort, port, vesselMaxSpeed);
				suezTravelTime = distanceProvider.getTravelTime(ERouteOption.SUEZ, vesselAvailability.getVessel(), prevPort, port, vesselMaxSpeed);
				panamaTravelTime = distanceProvider.getTravelTime(ERouteOption.PANAMA, vesselAvailability.getVessel(), prevPort, port, vesselMaxSpeed);

				// Can we build this into the distance provider api?
				// If there is an override, then we set the other travel times to MAX_VALUE
				if (voyageSpecProvider != null) {
					ERouteOption routeOverride = voyageSpecProvider.getVoyageRouteOption(prevPortSlot, thisPortSlot);
					if (routeOverride != null) {
						switch (routeOverride) {
						case DIRECT:
							suezTravelTime = Integer.MAX_VALUE;
							panamaTravelTime = Integer.MAX_VALUE;
							break;
						case PANAMA:
							suezTravelTime = Integer.MAX_VALUE;
							directTravelTime = Integer.MAX_VALUE;
							break;
						case SUEZ:
							directTravelTime = Integer.MAX_VALUE;
							panamaTravelTime = Integer.MAX_VALUE;
							break;
						default:
							break;
						}
					}
				}
				
				int extraIdleTime = 0;
				for (var type : ExplicitIdleTime.values()) {
					extraIdleTime += records[index - 1].extraIdleTimes[type.ordinal()];
				}

				if (directTravelTime != Integer.MAX_VALUE) {
					directTravelTime += prevVisitDuration + extraIdleTime;
				}
				if (suezTravelTime != Integer.MAX_VALUE) {
					suezTravelTime += prevVisitDuration + extraIdleTime;
				}
				if (panamaTravelTime != Integer.MAX_VALUE) {
					panamaTravelTime += prevVisitDuration + extraIdleTime;
				}

				final int minTravelTime = Math.min(directTravelTime, Math.min(panamaTravelTime, suezTravelTime));
				if (minTravelTime == Integer.MAX_VALUE) {
					throw new InfeasibleSolutionException("No valid distance");
				}

				travelTimeData.setMinTravelTime(index - 1, minTravelTime);
				travelTimeData.setTravelTime(ERouteOption.DIRECT, index - 1, directTravelTime);
				travelTimeData.setTravelTime(ERouteOption.SUEZ, index - 1, suezTravelTime);
				travelTimeData.setTravelTime(ERouteOption.PANAMA, index - 1, panamaTravelTime);
			}

			// Handle time windows
			if (window == null) { // empty time windows are made to be the
									// biggest reasonable gap
				if (index > 0) {
					// clip start of time window
					records[index].windowStartTime = records[index - 1].windowStartTime + travelTimeData.getMinTravelTime(index - 1);
					assert records[index].windowEndTime > records[index].windowStartTime;
					// backwards pass will fix this.
					if (records[index].isRoundTripEnd) {
						// FIXME: This forces the previous discharge to be as early as possible. Pick a
						// suitable window size! (records[index-1].windowEndTime + minTravelTime should
						// work as
						// PTRMaker
						// selects
						// quickest travel time)
						records[index].windowEndTime = records[index].windowStartTime + 1 + EMPTY_WINDOW_SIZE;
						assert records[index].windowEndTime > records[index].windowStartTime;
						// The fix! Note price schedule needs updating otherwise we slow down too much,
						// records[index].windowEndTime = records[index - 1].windowEndTime + 2 *
						// travelTimeData.getMinTravelTime(index - 1);
					} else {
						// We may get here for e.g. relocatable charter out events.
						records[index].windowEndTime = records[index].windowStartTime + 1 + EMPTY_WINDOW_SIZE;
						assert records[index].windowEndTime > records[index].windowStartTime;
					}
				} else {
					records[index].windowStartTime = 0;
					records[index].windowEndTime = 1 + sequence.size() == 1 ? 0 : EMPTY_WINDOW_SIZE;
					assert records[index].windowEndTime > records[index].windowStartTime;
				}
			} else {
				if (index == 0) {// first time window is special
					// It is possible the first window is a spot market slot with a negative start
					// window due to timezones
					records[index].windowStartTime = Math.max(0, window.getInclusiveStart());
					records[index].windowEndTime = Math.max(1, window.getExclusiveEnd());
					assert records[index].windowEndTime > records[index].windowStartTime;
				} else {

					// subsequent time windows have their start time clipped, so
					// they don't start any earlier
					// than you could get to them without being late.
					records[index].windowEndTime = window.getExclusiveEnd();
					assert records[index].windowEndTime > records[index].windowStartTime;
					if (records[index].useTimeWindow || records[index].actualisedTimeWindow) {
						// Cargo shorts - pretend this is a start element
						// Actuals - use window directly
						records[index].windowStartTime = window.getInclusiveStart();
						assert records[index].windowEndTime > records[index].windowStartTime;
					} else {
						assert prevElement != null;
						records[index].windowStartTime = Math.max(window.getInclusiveStart(), records[index - 1].windowStartTime + travelTimeData.getMinTravelTime(index - 1));
						records[index].windowEndTime = Math.max(records[index].windowEndTime, records[index].windowStartTime + 1);
						assert records[index].windowEndTime > records[index].windowStartTime;

					}

				}
			}

			assert records[index].windowEndTime > records[index].windowStartTime;

			index++;
			prevElement = element;
			prevPortSlot = thisPortSlot;
		}

		// Do this bit twice, first time to correctly align the start/end boundaries of
		// the time windows, then to run again after max duration trimming
		for (int pass = 0; pass < 2; ++pass) {

			// add the last time window
			// portTimeWindowsRecords.get(sequenceIndex).add(portTimeWindowsRecord);
			// now perform reverse-pass to trim any overly late end times
			// (that is end times which would make us late at the next element)
			for (index = size - 2; index >= 0; index--) {
				// trim the end of this time window so that the next element is
				// reachable without lateness
				// (but never so that the end time is before the start time)

				if (records[index].actualisedTimeWindow && records[index + 1].actualisedTimeWindow) {
					// Skip, windows should already match.
				} else if (records[index + 1].actualisedTimeWindow && !records[index].actualisedTimeWindow) {
					// Current window if flexible, next window is fixed, bring end window back
					records[index].windowEndTime = records[index + 1].windowEndTime - travelTimeData.getMinTravelTime(index);
					assert records[index].windowEndTime > records[index].windowStartTime;
				} else if (!records[index + 1].useTimeWindow) {
					records[index].windowEndTime = Math.max(records[index].windowStartTime + 1,
							Math.min(records[index].windowEndTime, records[index + 1].windowEndTime - travelTimeData.getMinTravelTime(index)));
					assert records[index].windowEndTime > records[index].windowStartTime;
				}

				// Make sure end if >= start - this may shift the end forward again violating
				// min travel time.
				records[index].windowEndTime = Math.max(records[index].windowStartTime + 1, records[index].windowEndTime);
				assert records[index].windowEndTime > records[index].windowStartTime;
			}
			if (pass == 0) {
				if (sequence.size() > 0) {
					// Apply max duration cutoff to the last element of the sequence
					trimBasedOnMaxDuration(resource, size - 1);
					// Try to trim down the start event upper bound given the minimal duration if it
					// is set
					trimBasedOnMinDuration(resource, size - 1);
				}
			}

		}

		for (index = 1; index < size; ++index) {
			assert records[index].windowStartTime < records[index].windowEndTime;
			if (!records[index - 1].isRoundTripEnd) {
				assert records[index - 1].windowStartTime <= records[index].windowStartTime;
				assert records[index - 1].windowEndTime <= records[index].windowEndTime;
			}
		}

		// For charter outs where event, virtual, other, copy the event window forward
		for (index = 1; index < size; ++index) {
			if (records[index].isVirtual || records[index - 1].isVirtual) {
				records[index].windowStartTime = records[index - 1].windowStartTime;
				records[index].windowEndTime = records[index - 1].windowEndTime;
				assert records[index].windowEndTime > records[index].windowStartTime;
			}
		}
	}

	protected final void trimBasedOnPanamaCanal(final @NonNull IResource resource, final @NonNull ISequence sequence, final MinTravelTimeData travelTimeData,
			final CurrentBookingData currentBookings) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			// No shipping
			return;
		}

		final int size = sequence.size();
		// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
		if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence, vesselAvailability)) {
			return;
		}

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
		// if (isRoundTripSequence) {
		// // No Panama bookings.
		// return;
		// }

		// Make sure we can schedule stuff on Panama
		final IVessel vessel = vesselAvailability.getVessel();
		if (!distanceProvider.isRouteAvailable(ERouteOption.PANAMA, vessel)) {
			return;
		}

		boolean changed = false;
		// Two phases
		// - Phase 0 allocate required Panama voyages. Allocated boooking and voyages
		// where Panama (with booking or idle time) is faster.
		// - Phase 1 force direct/suez where possible
		for (int phase = 0; phase < 2; ++phase) {
			if (changed) {
				// Keep in the current phase until we stop changing stuff.
				--phase;
				changed = false;
			}

			changed |= trimPanamaForwardsPass(sequence, travelTimeData, currentBookings, isRoundTripSequence, vessel, phase);
			changed |= trimPanamaBackwardsPass(resource, sequence, travelTimeData, currentBookings, size, isRoundTripSequence, vessel);
		}

	}

	private boolean trimPanamaForwardsPass(final ISequence sequence, final MinTravelTimeData travelTimeData, final CurrentBookingData currentBookings, final boolean isRoundTripSequence,
			final @NonNull IVessel vessel, final int phase) {

		boolean changed = false;
		int index = 0;
		ISequenceElement fromElement = null;
		IPortSlot fromPortSlot = null;

		// first pass, collecting start time windows
		for (final ISequenceElement toElement : sequence) {

			final IPortSlot toPortSlot = portSlotProvider.getPortSlot(toElement);

			final int fromElementIndex = index - 1;
			final int toElementIndex = index;

			if (toElementIndex > 0 && /* !records[toElementIndex].isRoundTripEnd && */ !records[toElementIndex].useTimeWindow) {
				assert fromElement != null;
				assert fromPortSlot != null;

				records[toElementIndex].windowStartTime = Math.max(records[toElementIndex].windowStartTime,
						records[fromElementIndex].windowStartTime + travelTimeData.getMinTravelTime(fromElementIndex));
				records[toElementIndex].windowEndTime = Math.max(records[toElementIndex].windowStartTime + 1, records[toElementIndex].windowEndTime);
				assert records[toElementIndex].windowEndTime > records[toElementIndex].windowStartTime;
				if (checkPanamaCanalBookings) {

					final PortTimeWindowsRecord currentPortTimeWindowsRecord = records[fromElementIndex].ptr;

					// Already processed?
					AvailableRouteChoices currentRouteOptions = currentPortTimeWindowsRecord.getSlotNextVoyageOptions(fromPortSlot);
					if (currentRouteOptions != AvailableRouteChoices.OPTIMAL || currentPortTimeWindowsRecord.getSlotIsNextVoyageConstrainedPanama(fromPortSlot)) {

						records[toElementIndex].windowStartTime = Math.max(records[toElementIndex].windowStartTime,
								records[fromElementIndex].windowStartTime + travelTimeData.getMinTravelTime(fromElementIndex));
						records[toElementIndex].windowEndTime = Math.max(records[toElementIndex].windowEndTime, records[toElementIndex].windowStartTime + 1);
						assert records[toElementIndex].windowEndTime > records[toElementIndex].windowStartTime;
						// Choice already allocation, skip
						index++;
						fromElement = toElement;
						fromPortSlot = toPortSlot;

						continue;
					}

					final int directTravelTime = travelTimeData.getTravelTime(ERouteOption.DIRECT, fromElementIndex);
					final int suezTravelTime = travelTimeData.getTravelTime(ERouteOption.SUEZ, fromElementIndex);
					final int panamaTravelTime = travelTimeData.getTravelTime(ERouteOption.PANAMA, fromElementIndex);

					if (!travelTimeData.isRouteValid(ERouteOption.PANAMA, fromElementIndex, currentRouteOptions)) {
						// No Panama route, so an easy decision
						travelTimeData.setMinTravelTime(fromElementIndex, Math.min(suezTravelTime, directTravelTime));
						currentPortTimeWindowsRecord.setSlotNextVoyageOptions(fromPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA);
						currentPortTimeWindowsRecord.setSlotAdditionalPanamaDetails(fromPortSlot, false, 0);
						changed = true;
					} else {
						final @Nullable ECanalEntry routeOptionEntry = distanceProvider.getRouteOptionCanalEntrance(fromPortSlot.getPort(), ERouteOption.PANAMA);
						if (routeOptionEntry != null) {

							final PanamaSegments panamaSegments = getPanamaTravelSegments(toElementIndex, vessel, fromPortSlot, toPortSlot);

							final ECanalEntry panamaEntry = routeOptionEntry;
							boolean bookingAllocated = false;
							// TODO: Is this still a valid decision?
							// Only check for actual bookings once.
							if (phase == 0) {
								// Only apply bookings if Panama is normally the faster option.
								if (!isRoundTripSequence && isBetterThroughPanama(records[fromElementIndex].windowStartTime, records[toElementIndex].windowStartTime, panamaTravelTime,
										Math.min(directTravelTime, suezTravelTime))) {

									final List<IRouteOptionBooking> vesselBookings = currentBookings.getUnusedBookingsForSpecificVessel(vessel, panamaEntry);
									bookingAllocated = applyFirstFeasibleBooking(travelTimeData, currentBookings, fromPortSlot, fromElementIndex, toElementIndex, currentPortTimeWindowsRecord,

											panamaSegments, records[toElementIndex].windowEndTime, vesselBookings);
								}
							}

							if (bookingAllocated) {
								// Booking allocated, time windows should have been updated, no need to check
								// for other bookings or idle times.
								changed = true;
							} else if (records[fromElementIndex].windowStartTime + directTravelTime < records[toElementIndex].windowEndTime //
									|| (records[fromElementIndex].windowStartTime + suezTravelTime < records[toElementIndex].windowEndTime && suezTravelTime != Integer.MAX_VALUE) //
									|| directTravelTime <= panamaTravelTime) {

								if (phase != 0) {
									// In the first phase, do not set the voyage choice as this may cause problems
									// further down the line (however, northbound Panama may be allowed)

									final int earliestPanamaArrivalTime = records[fromElementIndex].windowStartTime + panamaSegments.travelTimeToCanalWithoutMargin;
									final int latestPanamaArrivalTime = Math.max(earliestPanamaArrivalTime + 1, records[fromElementIndex + 1].windowEndTime - panamaSegments.travelTimeFromCanal);

									final int waitingTime = getMaxIdleHours(vessel, panamaSegments.isNorthbound, //
											earliestPanamaArrivalTime, latestPanamaArrivalTime);

									travelTimeData.setMinTravelTime(fromElementIndex, Math.min(Math.min(suezTravelTime, directTravelTime), panamaTravelTime + waitingTime));

									currentPortTimeWindowsRecord.setSlotNextVoyageOptions(fromPortSlot, AvailableRouteChoices.OPTIMAL);
									currentPortTimeWindowsRecord.setSlotAdditionalPanamaDetails(fromPortSlot, true, waitingTime);

									changed = true;
								}

								// TODO: We could perform an additional pass and allocate a spare booking slot
								// and set the route choice to optimal and let the schedule determine best
								// route.
								// TODO: Note this would require the MinTravelTimeData object to be passed
								// around for time calculations rather than use the distance provider to get
								// non-booking
								// time.
								// TODO: Or we could re-calculate the travel time using the booking. (Maybe add
								// API to distance provider to get travel time via booking?).
							} else {
								// go through panama, figure out if there is an unassigned booking
								// TODO: this is a bit optimistic in case there is no booking ;-)
								travelTimeData.setMinTravelTime(fromElementIndex, panamaTravelTime);
								changed = true;

								boolean foundBooking = false;
								assert fromElement != null;

								// Nominal vessels should not use the Panama bookings
								if (!isRoundTripSequence) {
									// Check flexible bookings for a match.
									final List<IRouteOptionBooking> flexibleBookings = currentBookings.getUnusedBookingsForMultipleVessels(vessel, panamaEntry);
									foundBooking = applyFirstFeasibleBooking(travelTimeData, currentBookings, fromPortSlot, fromElementIndex, toElementIndex, currentPortTimeWindowsRecord,
											panamaSegments, records[toElementIndex].windowEndTime, flexibleBookings);

									if (!foundBooking) {
										// No feasible match. Windows are initially trimmed based on distance/max_speed.
										// However if we cannot find a booking, then Panama will have the waiting days
										// added.
										// If Panama + wait days is still faster and it would delay the next event, lets
										// recheck the bookings with the new upper bound. We may be able to use a
										// booking now
										// with less lateness than we would have if we just used wait days.
										final int earliestPanamaArrivalTime = records[fromElementIndex].windowStartTime + panamaSegments.travelTimeToCanalWithoutMargin;
										final int latestPanamaArrivalTime = records[fromElementIndex + 1].windowEndTime - panamaSegments.travelTimeFromCanal;
										final int waitingTime = getMaxIdleHours(vessel, panamaSegments.isNorthbound, earliestPanamaArrivalTime, latestPanamaArrivalTime);
										final int delayedPanamaTravelTime = panamaTravelTime + waitingTime;
										if (delayedPanamaTravelTime < Math.min(directTravelTime, suezTravelTime)) {
											int earliestArrivalAtTo = records[fromElementIndex].windowStartTime + delayedPanamaTravelTime;
											if (earliestArrivalAtTo >= records[toElementIndex].windowEndTime) {
												// We will be late either way, so recheck bookings
												{
													// Re-check the specific bookings first
													final List<IRouteOptionBooking> vesselBookings = currentBookings.getUnusedBookingsForSpecificVessel(vessel, panamaEntry);
													foundBooking = applyFirstFeasibleBooking(travelTimeData, currentBookings, fromPortSlot, fromElementIndex, toElementIndex,
															currentPortTimeWindowsRecord, panamaSegments, earliestArrivalAtTo + 1, vesselBookings);
												}
												if (!foundBooking) {
													// ... then recheck the flexible bookings
													foundBooking = applyFirstFeasibleBooking(travelTimeData, currentBookings, fromPortSlot, fromElementIndex, toElementIndex,
															currentPortTimeWindowsRecord, panamaSegments, earliestArrivalAtTo + 1, flexibleBookings);
												}
											}

										}
									}
								}

								if (!foundBooking) {
									// Applying the waiting time with no booking
									changed |= updateRouteOptionsAndMinTravelTimesBasedOnNoBookingFound(vessel, travelTimeData, fromElementIndex, fromPortSlot, currentPortTimeWindowsRecord,
											directTravelTime, suezTravelTime, panamaTravelTime, panamaSegments);
								} else {
									// Booking has been applied, mark as changed.
									changed = true;
								}
							}
						}
					}
				}
				records[toElementIndex].windowStartTime = Math.max(records[toElementIndex].windowStartTime,
						records[fromElementIndex].windowStartTime + travelTimeData.getMinTravelTime(fromElementIndex));
				records[toElementIndex].windowEndTime = Math.max(records[toElementIndex].windowEndTime, records[toElementIndex].windowStartTime + 1);
				assert records[toElementIndex].windowStartTime < records[toElementIndex].windowEndTime;
			}
			index++;
			fromElement = toElement;
			fromPortSlot = toPortSlot;
		}
		return changed;
	}

	private boolean applyFirstFeasibleBooking(final MinTravelTimeData travelTimeData, final CurrentBookingData currentBookings, IPortSlot fromPortSlot, final int fromElementIndex,
			final int toElementIndex, final PortTimeWindowsRecord currentPortTimeWindowsRecord, final PanamaSegments panamaSegments, int toWindowEndTime,
			final List<IRouteOptionBooking> vesselBookings) {

		boolean foundBooking = false;
		for (final IRouteOptionBooking potentialBooking : vesselBookings) {

			if (currentBookings.isBookingFeasible(potentialBooking, records[fromElementIndex].windowStartTime, toWindowEndTime, panamaSegments)) {

				trimWindowsForBooking(travelTimeData, currentBookings, toElementIndex, fromPortSlot, currentPortTimeWindowsRecord, panamaSegments, potentialBooking);

				foundBooking = true;
				break; // No need to look further, as found a match.
			}
		}
		return foundBooking;
	}

	private boolean trimPanamaBackwardsPass(final IResource resource, final ISequence sequence, final MinTravelTimeData travelTimeData, final CurrentBookingData currentBookings, final int size,
			final boolean isRoundTripSequence, final IVessel vessel) {

		boolean changed = false;

		ISequenceElement fromElement;
		IPortSlot fromPortSlot;
		// Do this bit twice, first time to correctly align the start/end boundaries of
		// the time windows, then to run again after max duration trimming
		for (int end_pass = 0; end_pass < 2; ++end_pass) {
			// now perform reverse-pass to trim any overly late end times
			// (that is end times which would make us late at the next element)
			for (int index = size - 2; index >= 0; index--) {

				int fromElementIndex = index;
				int toElementIndex = index + 1;

				// Trim window ends taking into account potential delays for using the panama
				// canal. E.g. Northbound may introduce 5 day delay.
				// Consider voyage A then voyage B. Neither voyage needs panama from the windows
				// alone. If A goes via panama, B could go direct or via panama with booking.
				// However going direct on
				// voyage A can force B to go via panama miss the booking and pick up 5 day
				// delay.
				// We can't easily manipulate the min travel time array as travel time is
				// dependent on the actual departure time. Instead we can adjust the upper bound
				// of departure time to take
				// delays
				// into account.

				if (true) {
					// first pass, collecting start time windows
					fromElement = sequence.get(fromElementIndex);
					fromPortSlot = portSlotProvider.getPortSlot(fromElement);

					final ISequenceElement toElement = sequence.get(toElementIndex);
					final IPortSlot toPortSlot = portSlotProvider.getPortSlot(toElement);
					final PortTimeWindowsRecord currentPortTimeWindowsRecord = records[fromElementIndex].ptr;

					// If we have already picked the route choice, travel time should have been
					// updated.
					final AvailableRouteChoices slotNextVoyageOptions = currentPortTimeWindowsRecord.getSlotNextVoyageOptions(fromPortSlot);
					if (slotNextVoyageOptions == AvailableRouteChoices.OPTIMAL && !records[toElementIndex].useTimeWindow) {
						assert fromElement != null;

						final int directTravelTime = travelTimeData.getTravelTime(ERouteOption.DIRECT, fromElementIndex);
						final int suezTravelTime = travelTimeData.getTravelTime(ERouteOption.SUEZ, fromElementIndex);

						if (travelTimeData.isRouteValid(ERouteOption.PANAMA, fromElementIndex, slotNextVoyageOptions)) {
							final PanamaSegments panamaSegments = getPanamaTravelSegments(toElementIndex, vessel, fromPortSlot, toPortSlot);
							final int endTime = records[toElementIndex].windowEndTime;

							if (endTime != Integer.MAX_VALUE && panamaSegments.travelTimeFromCanal != Integer.MAX_VALUE) {
								// Calculate latest Panama arrival time. This includes excess delays. Can be
								// overwritten later if there is a suitable booking
								int latestPanamaTime = endTime - panamaSegments.travelTimeFromCanal;
								// Removing for now to account for the season end points
								final int earliestPanamaArrivalTime = records[toElementIndex - 1].windowStartTime + panamaSegments.travelTimeToCanalWithoutMargin;
								final int latestPanamaArrivalTime = Math.max(earliestPanamaArrivalTime + 1, records[toElementIndex].windowEndTime - panamaSegments.travelTimeFromCanal);
								final int waitingTime = getMaxIdleHours(vessel, panamaSegments.isNorthbound, earliestPanamaArrivalTime, latestPanamaArrivalTime);
								latestPanamaTime -= waitingTime;

								// TODO: get the best season change point based on the waiting time PLUS
								// earliest/latest Panama time

								if (!isRoundTripSequence) {
									// (for reverse pass)
									// Next pass should allocate the booking during the forward trimming phase, if
									// suitable one available.
									final List<IRouteOptionBooking> set = currentBookings.getUnusedBookingsForMultipleVessels(vessel, panamaSegments.canalEntranceUsed);

									for (final IRouteOptionBooking booking : set) {

										if (currentBookings.isBookingFeasible(booking, records[fromElementIndex].windowStartTime, endTime, panamaSegments)) {
											final int time = booking.getBookingDate() - panamaBookingsProvider.getMarginInHours();
											// // Find the latest possible booking date.
											if (time > latestPanamaTime) {
												latestPanamaTime = time;
											}
										}
									}
								} else {
									final int panamaTravelTime = travelTimeData.getTravelTime(ERouteOption.PANAMA, fromElementIndex);
									changed |= updateRouteOptionsAndMinTravelTimesBasedOnNoBookingFound(vessel, travelTimeData, fromElementIndex, fromPortSlot, currentPortTimeWindowsRecord,
											directTravelTime, suezTravelTime, panamaTravelTime, panamaSegments);
								}
								// If the new panama time is the fastest, update the window.
								final int newPanamaTime = endTime - (latestPanamaTime - panamaSegments.travelTimeToCanalWithoutMargin);
								if (newPanamaTime < Math.min(directTravelTime, suezTravelTime)) {
									final int newFromWindowEndTime = endTime - newPanamaTime;
									if (newFromWindowEndTime > records[fromElementIndex].windowStartTime && newFromWindowEndTime < records[fromElementIndex].windowEndTime) {
										records[fromElementIndex].windowEndTime = newFromWindowEndTime;
										// Mark current pass as changed. If pass 0 then we may have caused more forced
										// voyages.
										changed = true;
									}
								}
							}
						}
					}
				}

				// trim the end of this time window so that the next element is
				// reachable without lateness
				// (but never so that the end time is before the start time)
				if (records[fromElementIndex].actualisedTimeWindow && records[toElementIndex].actualisedTimeWindow) {
					// Skip, windows should already match.
				} else if (records[toElementIndex].actualisedTimeWindow && !records[fromElementIndex].actualisedTimeWindow) {
					// Current window if flexible, next window is fixed, bring end window back
					records[fromElementIndex].windowEndTime = records[toElementIndex].windowEndTime - travelTimeData.getMinTravelTime(fromElementIndex);
				} else if (!records[toElementIndex].useTimeWindow) {
					records[fromElementIndex].windowEndTime = Math.max(records[fromElementIndex].windowStartTime + 1,
							Math.min(records[fromElementIndex].windowEndTime, records[toElementIndex].windowEndTime - travelTimeData.getMinTravelTime(fromElementIndex)));
				}

				// Make sure end if >= start - this may shift the end forward again violating
				// min travel time.
				records[fromElementIndex].windowEndTime = Math.max(records[fromElementIndex].windowStartTime + 1, records[fromElementIndex].windowEndTime);
				assert records[fromElementIndex].windowStartTime < records[fromElementIndex].windowEndTime;
			}
			if (end_pass == 0) {
				if (sequence.size() > 0) {
					// Apply max duration cutoff to the last element of the sequence
					trimBasedOnMaxDuration(resource, size - 1);
					// Try to trim down the start event upper bound given the minimal duration if it
					// is set
					trimBasedOnMinDuration(resource, size - 1);
				}
			}
		}
		// For charter outs where event, virtual, other, copy the event window forward
		for (int toIndex = 1; toIndex < size; ++toIndex) {
			if (records[toIndex].isVirtual || records[toIndex - 1].isVirtual) {
				records[toIndex].windowStartTime = records[toIndex - 1].windowStartTime;
				records[toIndex].windowEndTime = records[toIndex - 1].windowEndTime;
				assert records[toIndex].windowEndTime > records[toIndex].windowStartTime;
			}
		}
		return changed;
	}

	private void trimWindowsBasedOnMatchedPanamaBooking(final IRouteOptionBooking potentialBooking, final int toElementIndex, final int toCanal, final int fromEntryPoint) {

		int fromElementIndex = toElementIndex - 1;
		assert fromElementIndex >= 0;

		// Adjust origin window end
		records[fromElementIndex].windowEndTime = Math.min(potentialBooking.getBookingDate() + 1 - toCanal, records[fromElementIndex].windowEndTime);
		assert records[fromElementIndex].windowStartTime < records[fromElementIndex].windowEndTime;

		// Adjust destination window start
		records[toElementIndex].windowStartTime = Math.max(records[toElementIndex].windowStartTime, potentialBooking.getBookingDate() + fromEntryPoint);
		// It is possible we introduce lateness by using a booking (as initial mintime
		// is based on simple panama distance/time logic)
		records[toElementIndex].windowEndTime = Math.max(records[toElementIndex].windowEndTime, records[toElementIndex].windowStartTime + 1);
		assert records[toElementIndex].windowEndTime > records[toElementIndex].windowStartTime;

	}

	/**
	 * Update the time windows based on the given booking. This will trim both the
	 * earliest arrival at the destination but ALSO the latest departure from the
	 * origin. Elements prior to the origin will need to be re-checked after this
	 * method has been called.
	 * 
	 * @param travelTimeData
	 * @param currentBookings
	 * @param toElementIndex
	 * @param fromPortSlot
	 * @param currentPortTimeWindowsRecord
	 * @param segments
	 * @param booking
	 */
	private void trimWindowsForBooking(final MinTravelTimeData travelTimeData, final CurrentBookingData currentBookings, final int toElementIndex, final IPortSlot fromPortSlot,
			final PortTimeWindowsRecord currentPortTimeWindowsRecord, final PanamaSegments segments, final IRouteOptionBooking booking) {
		int fromElementIndex = toElementIndex - 1;
		assert fromElementIndex >= 0;
		// destination can be reached from slot
		assert records[fromElementIndex].windowStartTime < records[toElementIndex - 1].windowEndTime;

		currentPortTimeWindowsRecord.setSlotNextVoyageOptions(fromPortSlot, AvailableRouteChoices.PANAMA_ONLY);
		currentPortTimeWindowsRecord.setRouteOptionBooking(fromPortSlot, booking);
		currentPortTimeWindowsRecord.setSlotAdditionalPanamaDetails(fromPortSlot, true, 0);

		trimWindowsBasedOnMatchedPanamaBooking(booking, toElementIndex, segments.travelTimeToCanalWithMargin, segments.travelTimeFromCanal);

		final int travelTime = segments.travelTimeToCanalWithMargin + segments.travelTimeFromCanal;
		// TODO: This is probably a better time to use....
		// final int travelTime = records[toElementIndex].windowStartTime -
		// (records[fromElementIndex].windowEndTime - 1);
		// assert travelTime >= segments.travelTimeToCanalWithMargin +
		// segments.travelTimeFromCanal;

		travelTimeData.setMinTravelTime(fromElementIndex, travelTime);

		currentBookings.setUsed(segments.canalEntranceUsed, booking);
	}

	private boolean updateRouteOptionsAndMinTravelTimesBasedOnNoBookingFound(final IVessel vessel, final MinTravelTimeData travelTimeData, final int fromElementIndex, final IPortSlot prevPortSlot,
			final PortTimeWindowsRecord currentPortTimeWindowsRecord, final int directTravelTime, final int suezTravelTime, final int panamaTravelTime, final PanamaSegments panamaSegments) {
		boolean changed = false;

		// Loop over until waiting time is not increasing.

		// Note: panamaTravelTime include any previous extra idle time and does not need
		// to be re-added here, unlike in the booking code path
		// always assume the worst case:
		final int earliestPanamaArrivalTime = records[fromElementIndex].windowStartTime + panamaSegments.travelTimeToCanalWithoutMargin;
		final int latestPanamaArrivalTime = Math.max(earliestPanamaArrivalTime + 1, records[fromElementIndex + 1].windowEndTime - panamaSegments.travelTimeFromCanal);
		assert earliestPanamaArrivalTime < latestPanamaArrivalTime;
		final int waitingTime = getMaxIdleHours(vessel, panamaSegments.isNorthbound, earliestPanamaArrivalTime, latestPanamaArrivalTime);
		final int delayedPanamaTravelTime = panamaTravelTime + waitingTime;
		if (delayedPanamaTravelTime < Math.min(directTravelTime, suezTravelTime)) {
			travelTimeData.setMinTravelTime(fromElementIndex, delayedPanamaTravelTime);
			currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.PANAMA_ONLY);
			currentPortTimeWindowsRecord.setSlotAdditionalPanamaDetails(prevPortSlot, true, waitingTime);
			changed = true;
		} else {
			travelTimeData.setMinTravelTime(fromElementIndex, Math.min(directTravelTime, suezTravelTime));
			currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA);
			currentPortTimeWindowsRecord.setSlotAdditionalPanamaDetails(prevPortSlot, false, 0);
			changed = true;
		}

		return changed;
	}

	private int getMaxIdleHours(final IVessel vessel, final boolean northbound, int startDateInclusive, int endDateExclusive) {
		if (useBestCanalIdleDays) {
			return panamaBookingsProvider.getBestIdleHours(vessel, startDateInclusive, endDateExclusive, northbound);
		}
		return panamaBookingsProvider.getWorstIdleHours(vessel, startDateInclusive, endDateExclusive, northbound);
	}

	/**
	 * Check whether we should go through Panama if we've made a booking. For
	 * example, we shouldn't go through if we will be late through Panama and can
	 * arrive on time direct or suez.
	 * 
	 * @param start
	 * @param end
	 * @param panamaTravelTime
	 * @param minOtherTravel
	 * @return
	 */
	private boolean isBetterThroughPanama(final int start, final int end, final int panamaTravelTime, final int minOtherTravel) {
		if (minOtherTravel == Integer.MAX_VALUE) {
			return true;
		} else if (panamaTravelTime == Integer.MAX_VALUE) {
			return false;
		} else if ((start + panamaTravelTime) < end) {
			return true;
		} else if (panamaTravelTime <= minOtherTravel) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Resize all the integer buffers for a given route
	 * 
	 * @param arrayIndex
	 * @param size
	 */
	private final void resizeAll(final int size) {
		if (records == null || records.length < size) {
			records = new ElementRecord[size];
		}
		for (int i = 0; i < size; ++i) {
			records[i] = new ElementRecord();
		}
	}

	/**
	 * For round trip (nominal) cargoes, we don't care what else has happened on
	 * this vessel
	 * 
	 * @param resource
	 * 
	 * @param portTimeWindowsRecord
	 * @param seqIndex
	 */
	private void setFeasibleTimeWindowsRoundTrip(final IPortTimeWindowsRecord portTimeWindowsRecord, final MinTravelTimeData travelTimeData) {
		int prevFeasibleWindowStart = IPortSlot.NO_PRICING_DATE;
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			TimeWindow timeWindow;
			int feasibleWindowStart;
			int feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot)) {
				// first load
				feasibleWindowStart = records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime;
				feasibleWindowEnd = records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime;
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				try {
					feasibleWindowStart = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime,
							prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
					feasibleWindowEnd = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime, feasibleWindowStart + 1);
					timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
				} catch (final Exception e) {
					feasibleWindowStart = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime,
							prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
					feasibleWindowEnd = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime, feasibleWindowStart + 1);
					timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
				}
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
			prevFeasibleWindowStart = feasibleWindowStart;
		}
		final IPortSlot portSlot = portTimeWindowsRecord.getReturnSlot();
		if (portSlot != null) {
			TimeWindow timeWindow;
			int feasibleWindowStart;
			int feasibleWindowEnd;

			try {
				feasibleWindowStart = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime,
						prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
				feasibleWindowEnd = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime, feasibleWindowStart + 1);
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} catch (final Exception e) {
				feasibleWindowStart = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime,
						prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
				feasibleWindowEnd = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime, feasibleWindowStart + 1);
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
		}
	}

	/**
	 * The previous cargo will have changed the constraints on the time windows, so
	 * we must find the new feasible time windows before choosing an arrival time
	 * 
	 * @param portTimeWindowsRecord
	 * @param seqIndex
	 */
	private void setFeasibleTimeWindowsUsingPrevious(final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull MinTravelTimeData travelTimeData, final boolean isLast) {
		int prevFeasibleWindowStart = Integer.MIN_VALUE;
		boolean lastSlotWasVirtual = false;
		TimeWindow rollingWindow = null;
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {

			final TimeWindow timeWindow;
			int feasibleWindowStart;
			int feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot) && portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) > 0) {
				// first load
				feasibleWindowStart = getFeasibleWindowStart(portTimeWindowsRecord, travelTimeData);
				feasibleWindowEnd = getFeasibleWindowEnd(portTimeWindowsRecord, portSlot, feasibleWindowStart);
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				if (portSlot.getPortType() == PortType.Virtual || lastSlotWasVirtual) {
					timeWindow = rollingWindow;
					assert timeWindow != null;
					feasibleWindowStart = timeWindow.getInclusiveStart();
					feasibleWindowEnd = timeWindow.getExclusiveEnd();
				} else {

					if (prevFeasibleWindowStart == Integer.MIN_VALUE) {
						feasibleWindowStart = records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime;
						feasibleWindowEnd = records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime;
					} else {
						feasibleWindowStart = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime,
								prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
						feasibleWindowEnd = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime, feasibleWindowStart + 1);
					}
					timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
				}
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
			assert records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime == feasibleWindowStart;
			assert records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime == feasibleWindowEnd;
			assert records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime > records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime;
			prevFeasibleWindowStart = feasibleWindowStart;

			rollingWindow = timeWindow;
			lastSlotWasVirtual = portSlot.getPortType() == PortType.Virtual;

		}
		if (isLast && portTimeWindowsRecord.getReturnSlot() != null) {
			final IPortSlot portSlot = portTimeWindowsRecord.getReturnSlot();
			assert portSlot != null;
			int feasibleWindowStart;
			int feasibleWindowEnd;
			if (prevFeasibleWindowStart == Integer.MIN_VALUE) {
				feasibleWindowStart = records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime;
				feasibleWindowEnd = records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime;
			} else {
				feasibleWindowStart = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowStartTime,
						prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
				feasibleWindowEnd = Math.max(records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime, feasibleWindowStart + 1);
			}

			final TimeWindow timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
		}
	}

	private int getFeasibleWindowEnd(final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull IPortSlot portSlot, final int feasibleWindowStart) {
		return Math.max(feasibleWindowStart + 1, records[portTimeWindowsRecord.getIndex(portSlot)].windowEndTime);
	}

	private int getFeasibleWindowStart(final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull MinTravelTimeData travelTimeData) {
		final int prevIndex = portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1;
		return Math.max(records[prevIndex].windowStartTime + travelTimeData.getMinTravelTime(prevIndex), records[portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot())].windowStartTime);
	}

	private boolean isSequentialVessel(final @NonNull IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return false;
		}
		return true;
	}

	public void setTrimByPanamaCanalBookings(final boolean value) {
		checkPanamaCanalBookings = value;
	}

	protected final void trimBasedOnMinDuration(final @NonNull IResource resource, final int index) {
		final IEndRequirement requirement = startEndRequirementProvider.getEndRequirement(resource);
		if (requirement != null) {
			if (requirement.isMinDurationSet()) {
				final int minDurationLowerBound = getTrimmedLowerBoundBasedOnMinDuration(requirement, index);
				records[index].windowStartTime = Math.max(records[index].windowStartTime, minDurationLowerBound);
				records[index].windowEndTime = Math.max(records[index].windowEndTime, records[index].windowStartTime + 1);
				assert records[index].windowEndTime > records[index].windowStartTime;
			}
		}
	}

	protected final void trimBasedOnMaxDuration(final @NonNull IResource resource, final int index) {
		final IEndRequirement requirement = startEndRequirementProvider.getEndRequirement(resource);

		if (requirement != null) {
			if (requirement.isMaxDurationSet()) {
				final int maxDurationUpperBound = getTrimmedUpperBoundBasedOnMaxDuration(requirement, index);
				records[index].windowEndTime = Math.max(records[index].windowStartTime + 1, Math.min(records[index].windowEndTime, maxDurationUpperBound));
				assert records[index].windowEndTime > records[index].windowStartTime;
			}
		}
	}

	protected final int getTrimmedUpperBoundBasedOnMaxDuration(final IEndRequirement endRequirement, final int index) {

		final int upperBound = records[index].windowEndTime;
		final int maxUpperBound = records[0].windowEndTime + endRequirement.getMaxDurationInHours();
		return Math.min(upperBound, maxUpperBound);
	}

	protected final int getTrimmedLowerBoundBasedOnMinDuration(final IEndRequirement requirement, final int index) {
		int lowerBound = records[index].windowStartTime;

		final int minLowerBound = records[0].windowStartTime + requirement.getMinDurationInHours();

		assert lowerBound != Integer.MIN_VALUE : "Missing lower bound when trimming with min duration";

		lowerBound = Math.max(lowerBound, minLowerBound);

		return lowerBound;
	}

	/**
	 * Internal "record" class to hold panama travel time calcs
	 */
	class PanamaSegments {
		int travelTimeToCanalWithMargin; // Includes visit duration. For use with a booking
		int travelTimeToCanalWithoutMargin; // Includes visit duration. For use with the idle days padding.
		int travelTimeFromCanal;
		boolean isNorthbound;
		ECanalEntry canalEntranceUsed;
	}

	/**
	 * Compute and store the different the panama canal travel segments needed in
	 * various stages of the logic.
	 */
	private PanamaSegments getPanamaTravelSegments(final int toElementIndex, final IVessel vessel, final IPortSlot fromPortSlot, final IPortSlot toPortSlot) {

		final PanamaSegments segments = new PanamaSegments();
		segments.isNorthbound = distanceProvider.getRouteOptionDirection(fromPortSlot.getPort(), ERouteOption.PANAMA) == IDistanceProvider.RouteOptionDirection.NORTHBOUND;

		segments.canalEntranceUsed = distanceProvider.getRouteOptionCanalEntrance(fromPortSlot.getPort(), ERouteOption.PANAMA);

		segments.travelTimeToCanalWithMargin = records[toElementIndex - 1].visitDuration + panamaBookingsHelper.getTravelTimeToCanal(vessel, fromPortSlot.getPort(), true);
		segments.travelTimeToCanalWithoutMargin = records[toElementIndex - 1].visitDuration + panamaBookingsHelper.getTravelTimeToCanal(vessel, fromPortSlot.getPort(), false);

		int extraIdleTime = 0;
		for (var type : ExplicitIdleTime.values()) {
			extraIdleTime += records[toElementIndex - 1].extraIdleTimes[type.ordinal()];
		}
		segments.travelTimeFromCanal = panamaBookingsHelper.getTravelTimeFromCanalEntry(vessel, segments.canalEntranceUsed, toPortSlot.getPort()) //
				+ extraIdleTime; // make sure purge, contingency, spot market idle time etc is still included.

		return segments;
	}

}
