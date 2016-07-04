/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.PortTimesPlanner;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
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
	private boolean enableCache;

	private static class Key {
		private final @NonNull IResource resource;
		private final @NonNull ISequence sequence;
		private final int[] arrivalTimes;

		public Key(final @NonNull IResource resource, final @NonNull ISequence sequence, final int[] arrivalTimes) {
			this.resource = resource;
			this.sequence = sequence;
			this.arrivalTimes = arrivalTimes;
		}

		@Override
		public int hashCode() {
			return Objects.hash(resource, sequence);
		}

		@Override
		public boolean equals(final Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof Key) {
				final Key other = (Key) obj;
				return Objects.equals(this.resource, other.resource) //
						&& Objects.equals(this.sequence, other.sequence);
			}
			return false;
		}
	}

	@Inject
	private Provider<IVolumeAllocator> volumeAllocatorProvider;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private VoyagePlanAnnotator voyagePlanAnnotator;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private VoyagePlanner voyagePlanner;

	@Inject
	private PortTimesPlanner portTimesPlanner;

	@Inject
	private CapacityViolationChecker capacityViolationChecker;

	@Inject
	private LatenessChecker latenessChecker;

	@Inject
	private IdleTimeChecker idleTimeChecker;

	private final @NonNull AbstractCache<@NonNull Key, @Nullable VolumeAllocatedSequence> cache;

	public ScheduleCalculator() {
		cache = new LHMCache<>("ScheduleCalculatorCache", (key) -> {
			return new Pair<>(key, schedule(key.resource, key.sequence, key.arrivalTimes, null));
		}, 50_000);
	}

	@Nullable
	public VolumeAllocatedSequences schedule(@NonNull final ISequences sequences, final int[][] arrivalTimes, @Nullable final IAnnotatedSolution solution) {
		final VolumeAllocatedSequences volumeAllocatedSequences = new VolumeAllocatedSequences();

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareSalesForEvaluation(sequences);
		}

		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
			calculator.preparePurchaseForEvaluation(sequences);
		}

		final List<@NonNull IResource> resources = sequences.getResources();

		for (int i = 0; i < sequences.size(); ++i) {
			final ISequence sequence = sequences.getSequence(i);
			final IResource resource = resources.get(i);
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			final VolumeAllocatedSequence volumeAllocatedSequence;
			if (solution == null && enableCache) {
				// Is this a good key? Is ISequence the same instance each time (equals will be different...)?
				final Key key = new Key(resource, sequence, arrivalTimes[i]);
				volumeAllocatedSequence = cache.get(key);

				// Verification
				if (false) {
					assert arrivalTimes[i] != null;
					final VolumeAllocatedSequence reference = schedule(resource, sequence, arrivalTimes[i], solution);

					if (volumeAllocatedSequence == null && reference == null) {
						return null;
					}

					if (reference != null && !reference.isEqual(volumeAllocatedSequence)) {
						reference.isEqual(volumeAllocatedSequence);
						throw new RuntimeException("Cache consistency error");
					}
				}
			} else {
				volumeAllocatedSequence = schedule(resource, sequence, arrivalTimes[i], solution);
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
	private VolumeAllocatedSequence schedule(final @NonNull IResource resource, final @NonNull ISequence sequence, final int @NonNull [] arrivalTimes, @Nullable final IAnnotatedSolution solution) {

		final VolumeAllocatedSequence volumeAllocatedSequence = doSchedule(resource, sequence, arrivalTimes);
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
	private VolumeAllocatedSequence doSchedule(final @NonNull IResource resource, final @NonNull ISequence sequence, final int @NonNull [] arrivalTimes) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

			@Nullable
			final IPortTimesRecord portTimesRecord = portTimesPlanner.makeDESOrFOBPortTimesRecord(resource, sequence);

			// Virtual vessels are those operated by a third party, for FOB and DES situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean much,
			// but contracts need this kind of information to make up numbers with.
			return desOrFobSchedule(resource, sequence, portTimesRecord);
		}

		if (arrivalTimes == null) {
			return new VolumeAllocatedSequence(resource, sequence, 0, Collections.<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> emptyList());
		}

		// If this a cargo round trip sequence, but we have no data (i.e. there are no cargoes), return the basic data structure to avoid any exceptions
		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
		if (isRoundTripSequence) {
			int ii = 0;
		}
		if (isRoundTripSequence && arrivalTimes.length == 0) {
			return new VolumeAllocatedSequence(resource, sequence, 0, Collections.<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> emptyList());
		}

		// Get start time
		final int startTime = arrivalTimes[0];

		final @NonNull List<@NonNull IPortTimesRecord> portTimesRecords = portTimesPlanner.makeShippedPortTimesRecords(resource, sequence, arrivalTimes);

		// Generate all the voyageplans and extra annotations for this sequence
		final List<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans = voyagePlanner.makeVoyagePlans(resource, sequence, portTimesRecords);
		if (voyagePlans == null) {
			return null;
		}

		// Put it all together and return
		final VolumeAllocatedSequence scheduledSequence = new VolumeAllocatedSequence(resource, sequence, startTime, voyagePlans);

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

		if (portTimesRecord == null) {
			return new VolumeAllocatedSequence(resource, sequence, 0, Collections.<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> emptyList());
		}
		@NonNull
		final Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> p = voyagePlanner.makeDESOrFOBVoyagePlanPair(resource, sequence, portTimesRecord);

		final int vesselStartTime = portTimesRecord.getFirstSlotTime();
		final VolumeAllocatedSequence scheduledSequence = new VolumeAllocatedSequence(resource, sequence, vesselStartTime,
				Collections.singletonList(new Triple<>(p.getFirst(), Collections.<IPortSlot, IHeelLevelAnnotation> emptyMap(), (IPortTimesRecord) p.getSecond())));

		return scheduledSequence;
	}

	private void annotate(final @NonNull ISequences sequences, final @NonNull VolumeAllocatedSequences volumeAllocatedSequences, final @NonNull IAnnotatedSolution annotatedSolution) {

		// Do basic voyageplan annotation
		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			final IResource resource = volumeAllocatedSequence.getResource();
			final ISequence sequence = sequences.getSequence(resource);

			if (sequence.size() > 0) {
				voyagePlanAnnotator.annotateFromScheduledSequence(volumeAllocatedSequence, annotatedSolution);
			}
		}

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

				final IHeelLevelAnnotation heelLevelAnnotation = scheduledSequence.getHeelLevelAnnotation(portSlot);
				if (heelLevelAnnotation != null) {
					elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_heelLevelInfo, heelLevelAnnotation);
				}
			}
		}
	}
}
