/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.IArrivalTimeScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link ScheduleCalculator} performs {@link ScheduledSequences} wide processing of a basic schedule - such as P&L calculations. This implementation also perform the basic break-even analysis,
 * charter out generation and volume allocations then finally the overall P&L calculations.
 * 
 * 
 * @author Simon Goodall
 */
public class ScheduleCalculator {

	@Inject
	@Named(SchedulerConstants.Key_VolumeAllocatedSequenceCache)
	private CacheMode cacheMode;

	@Inject
	@Named("hint-lngtransformer-disable-caches")
	private boolean hintEnableCache;

	private static class Key {
		private final @NonNull IResource resource;
		private final @NonNull ISequence sequence;
		private final @Nullable List<@NonNull IPortTimesRecord> portTimesRecords;
		private final @NonNull List<AvailableRouteChoices> voyageKeys = new LinkedList<>();
		private final @NonNull List<IRouteOptionBooking> canalBookings = new LinkedList<>();
		private final @NonNull List<Integer> slotTimes = new LinkedList<>();

		public Key(final @NonNull IResource resource, final @NonNull ISequence sequence, final @Nullable List<@NonNull IPortTimesRecord> portTimesRecords) {
			this.resource = resource;
			this.sequence = sequence;
			this.portTimesRecords = portTimesRecords;
			if (portTimesRecords != null) {
				portTimesRecords.forEach(ptr -> ptr.getSlots().forEach(slot -> voyageKeys.add(ptr.getSlotNextVoyageOptions(slot))));
				portTimesRecords.forEach(ptr -> ptr.getSlots().forEach(slot -> canalBookings.add(ptr.getRouteOptionBooking(slot))));
				portTimesRecords.forEach(ptr -> ptr.getSlots().forEach(slot -> slotTimes.add(ptr.getSlotTime(slot))));
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(resource, sequence, slotTimes, voyageKeys, canalBookings);
		}

		@Override
		public boolean equals(final Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof Key) {
				final Key other = (Key) obj;
				return Objects.equals(this.resource, other.resource) //
						&& Objects.equals(this.sequence, other.sequence) //
						&& Objects.equals(this.slotTimes, other.slotTimes) //
						&& Objects.equals(this.canalBookings, other.canalBookings) //
						&& Objects.equals(this.voyageKeys, other.voyageKeys);
			}
			return false;
		}
	}

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVoyagePlanner voyagePlanner;

	@Inject
	private IArrivalTimeScheduler arrivalTimeScheduler;

	@Inject
	private CapacityViolationChecker capacityViolationChecker;

	@Inject
	private LatenessChecker latenessChecker;

	@Inject
	private IdleTimeChecker idleTimeChecker;

	private final @NonNull AbstractCache<@NonNull Key, @Nullable VolumeAllocatedSequence> cache;

	public ScheduleCalculator() {
		cache = new LHMCache<>("ScheduleCalculatorCache", (key) -> {
			return new Pair<>(key, schedule(key.resource, key.sequence, key.portTimesRecords, null));
		}, 50_000);
	}

	@Nullable
	public VolumeAllocatedSequences schedule(@NonNull final ISequences sequences, @Nullable final IAnnotatedSolution solution) {
		final Map<IResource, List<@NonNull IPortTimesRecord>> allPortTimeRecords = arrivalTimeScheduler.schedule(sequences);
		return schedule(sequences, allPortTimeRecords, solution);
	}

	@Nullable
	public VolumeAllocatedSequences schedule(@NonNull final ISequences sequences, final Map<IResource, List<@NonNull IPortTimesRecord>> allPortTimeRecords,
			@Nullable final IAnnotatedSolution solution) {
		final VolumeAllocatedSequences volumeAllocatedSequences = new VolumeAllocatedSequences();

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareSalesForEvaluation(sequences);
		}

		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
			calculator.preparePurchaseForEvaluation(sequences);
		}

		final List<@NonNull IResource> resources = sequences.getResources();

		for (int i = 0; i < resources.size(); ++i) {
			final IResource resource = resources.get(i);
			final ISequence sequence = sequences.getSequence(resource);
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final VolumeAllocatedSequence volumeAllocatedSequence;
			final List<@NonNull IPortTimesRecord> portTimeRecords = allPortTimeRecords.get(resource);
			if (solution == null && cacheMode != CacheMode.Off && hintEnableCache) {

				final Key key = new Key(resource, sequence, portTimeRecords);
				volumeAllocatedSequence = cache.get(key);

				// Verification
				if (cacheMode == CacheMode.Verify) {
					final VolumeAllocatedSequence reference = schedule(resource, sequence, portTimeRecords, solution);

					if (volumeAllocatedSequence == null) {
						return null;
					}

					if (reference == null) {
						return null;
					}

					if (reference != null && !reference.isEqual(volumeAllocatedSequence)) {
						reference.isEqual(volumeAllocatedSequence);
						throw new RuntimeException("Cache consistency error");
					}
				}
			} else {
				volumeAllocatedSequence = schedule(resource, sequence, portTimeRecords, solution);
			}

			if (volumeAllocatedSequence == null) {
				return null;
			}
			volumeAllocatedSequences.add(vesselAvailability, volumeAllocatedSequence);
		}

		if (solution != null) {
			annotate(sequences, volumeAllocatedSequences, solution);
		}

		return volumeAllocatedSequences;

	}

	/**
	 * Schedule an {@link ISequence} using the given array of arrivalTimes, indexed according to sequence elements. These times will be used as the base arrival time. However is some cases the time
	 * between elements may be too short (i.e. because the vessel is already travelling at max speed). In such cases, if adjustArrivals is true, then arrival times will be adjusted in the
	 * {@link VoyagePlan}s. Otherwise null will be returned.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 *            Array of arrival times at each {@link ISequenceElement} in the {@link ISequence}
	 * @return
	 * @throws InfeasibleVoyageException
	 */
	@Nullable
	private VolumeAllocatedSequence schedule(final @NonNull IResource resource, final @NonNull ISequence sequence, final List<@NonNull IPortTimesRecord> records,
			@Nullable final IAnnotatedSolution solution) {

		final VolumeAllocatedSequence volumeAllocatedSequence = doSchedule(resource, sequence, records);
		if (volumeAllocatedSequence != null) {

			// Perform capacity violations analysis
			capacityViolationChecker.calculateCapacityViolations(volumeAllocatedSequence, solution);

			// Perform capacity violations analysis
			latenessChecker.calculateLateness(volumeAllocatedSequence, solution);

			// Idle time checker
			idleTimeChecker.calculateIdleTime(volumeAllocatedSequence, solution);
		}
		return volumeAllocatedSequence;
	}

	@Nullable
	private VolumeAllocatedSequence doSchedule(final @NonNull IResource resource, final @NonNull ISequence sequence, final List<@NonNull IPortTimesRecord> records) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

			@Nullable
			IPortTimesRecord portTimesRecord = null;
			if (records != null && !records.isEmpty()) {
				portTimesRecord = records.get(0);
			}

			// Virtual vessels are those operated by a third party, for FOB and DES situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean much,
			// but contracts need this kind of information to make up numbers with.
			return desOrFobSchedule(resource, sequence, portTimesRecord);
		}

		if (records == null) {
			return new VolumeAllocatedSequence(resource, vesselAvailability, sequence, 0, Collections.<@NonNull Pair<VoyagePlan, IPortTimesRecord>> emptyList());
		}

		// If this a cargo round trip sequence, but we have no data (i.e. there are no cargoes), return the basic data structure to avoid any exceptions
		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		if (isRoundTripSequence && records.size() == 0) {
			return new VolumeAllocatedSequence(resource, vesselAvailability, sequence, 0, Collections.<@NonNull Pair<VoyagePlan, IPortTimesRecord>> emptyList());
		}

		// Generate all the voyageplans and extra annotations for this sequence
		final List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> voyagePlans = voyagePlanner.makeVoyagePlans(resource, sequence, records);
		if (voyagePlans == null) {
			return null;
		}
		final int startTime = records.isEmpty() ? 0 : records.get(0).getFirstSlotTime();
		// Put it all together and return
		final VolumeAllocatedSequence scheduledSequence = new VolumeAllocatedSequence(resource, vesselAvailability, sequence, startTime, voyagePlans);

		return scheduledSequence;
	}

	/**
	 * This method replaces the normal shipped cargo calculation path with one specific to DES purchase or FOB sale cargoes. However this currently merges in behaviour from other classes - such as
	 * scheduling and volume allocation - which should really stay in those other classes.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */
	@NonNull
	private VolumeAllocatedSequence desOrFobSchedule(final @NonNull IResource resource, final @NonNull ISequence sequence, final @Nullable IPortTimesRecord portTimesRecord) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		if (portTimesRecord == null) {
			return new VolumeAllocatedSequence(resource, vesselAvailability, sequence, 0, Collections.<@NonNull Pair<VoyagePlan, IPortTimesRecord>> emptyList());
		}
		@NonNull
		final Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> p = voyagePlanner.makeDESOrFOBVoyagePlanPair(resource, sequence, portTimesRecord);

		final int vesselStartTime = portTimesRecord.getFirstSlotTime();
		final VolumeAllocatedSequence scheduledSequence = new VolumeAllocatedSequence(resource, vesselAvailability, sequence, vesselStartTime,
				Collections.singletonList(new Pair<>(p.getFirst(), (IPortTimesRecord) p.getSecond())));

		return scheduledSequence;
	}

	private void annotate(final @NonNull ISequences sequences, final @NonNull VolumeAllocatedSequences volumeAllocatedSequences, final @NonNull IAnnotatedSolution annotatedSolution) {

		// now add some more data for each load slot
		final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
		for (final VolumeAllocatedSequence scheduledSequence : volumeAllocatedSequences) {
			for (final IPortSlot portSlot : scheduledSequence.getSequenceSlots()) {
				assert portSlot != null;

				final ISequenceElement portElement = portSlotProvider.getElement(portSlot);

				assert portElement != null;

				final IAllocationAnnotation allocationAnnotation = scheduledSequence.getAllocationAnnotation(portSlot);
				if (allocationAnnotation != null) {
					elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);
				}
			}
		}
	}
}
