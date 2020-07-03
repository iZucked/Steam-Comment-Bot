/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.IVoyagePlanEvaluator;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.ScheduledVoyagePlanResult;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.IArrivalTimeScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link ScheduleCalculator} performs {@link ScheduledSequences} wide processing of a basic schedule - such as P&L calculations. This implementation also perform the basic break-even analysis,
 * charter out generation and volume allocations then finally the overall P&L calculations.
 * 
 * 
 * @author Simon Goodall
 */
@NonNullByDefault
public class ScheduleCalculator {

	@Inject
	private IVoyagePlanEvaluator voyagePlanEvaluator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IArrivalTimeScheduler arrivalTimeScheduler;

	@Inject
	private LatenessChecker latenessChecker;

	@Nullable
	public ProfitAndLossSequences schedule(final ISequences sequences, @Nullable final IAnnotatedSolution solution) {
		final Map<IResource, List<IPortTimesRecord>> allPortTimeRecords = arrivalTimeScheduler.schedule(sequences);
		return schedule(sequences, allPortTimeRecords, solution);
	}

	@Nullable
	public ProfitAndLossSequences schedule(final ISequences sequences, final Map<IResource, List<IPortTimesRecord>> allPortTimeRecords, @Nullable final IAnnotatedSolution solution) {
		final ProfitAndLossSequences volumeAllocatedSequences = new ProfitAndLossSequences();

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

			final List<@NonNull IPortTimesRecord> portTimeRecords = allPortTimeRecords.get(resource);
			final VolumeAllocatedSequence volumeAllocatedSequence = doSchedule(resource, sequence, portTimeRecords, solution);

			if (volumeAllocatedSequence == null) {
				return null;
			}
			// Check for max duration lateness
			if (!volumeAllocatedSequence.getVoyagePlanRecords().isEmpty()) {
				latenessChecker.calculateLateness(volumeAllocatedSequence, solution);
			}

			volumeAllocatedSequences.add(vesselAvailability, volumeAllocatedSequence);
		}

		if (solution != null) {
			annotate(volumeAllocatedSequences, solution);
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

	private @Nullable VolumeAllocatedSequence doSchedule(final IResource resource, final ISequence sequence, final @Nullable List<IPortTimesRecord> records,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

			@Nullable
			IPortTimesRecord portTimesRecord = null;
			if (records != null && !records.isEmpty()) {
				portTimesRecord = records.get(0);
			}

			// Virtual vessels are those operated by a third party, for FOB and DES
			// situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean
			// much,
			// but contracts need this kind of information to make up numbers with.
			return desOrFobSchedule(resource, vesselAvailability, sequence, portTimesRecord, annotatedSolution);
		}

		return shippedSchedule(resource, vesselAvailability, sequence, records, annotatedSolution);
	}

	private VolumeAllocatedSequence shippedSchedule(final IResource resource, final IVesselAvailability vesselAvailability, final ISequence sequence, final @Nullable List<IPortTimesRecord> records,
			@Nullable final IAnnotatedSolution annotatedSolution) {
		if (records == null || records.isEmpty()) {
			return VolumeAllocatedSequence.empty(resource, sequence);
		}

		final int vesselStartTime = records.get(0).getFirstSlotTime();

		// Generate all the voyage plans and extra annotations for this sequence
		final List<VoyagePlanRecord> voyagePlans = new LinkedList<>();

		ScheduledVoyagePlanResult lastResult = null;

		for (int idx = 0; idx < records.size(); idx++) {

			final IPortTimesRecord portTimeWindowsRecord = records.get(idx);
			final boolean lastPlan = idx == records.size() - 1;

			final PreviousHeelRecord previousHeelRecord = new PreviousHeelRecord();

			previousHeelRecord.heelVolumeInM3 = lastResult == null ? 0 : lastResult.lastHeelVolumeInM3;
			previousHeelRecord.lastHeelPricePerMMBTU = lastResult == null ? 0 : lastResult.lastHeelPricePerMMBTU;
			previousHeelRecord.lastCV = lastResult == null ? 0 : lastResult.lastCV;
			previousHeelRecord.forcedCooldown = lastResult == null ? false : lastResult.forcedCooldown;

			final List<ScheduledVoyagePlanResult> results = voyagePlanEvaluator.evaluateShipped(resource, vesselAvailability, //
					vesselAvailability.getCharterCostCalculator(), //
					vesselStartTime, //
					previousHeelRecord, portTimeWindowsRecord, //
					lastPlan, //
					false, // Return best
					true, // annotatedSolution != null, // Keep solutions for export
					annotatedSolution);

			assert !results.isEmpty();
			final ScheduledVoyagePlanResult result = results.get(0);
			voyagePlans.addAll(result.voyagePlans);

			lastResult = result;

		}

		return new VolumeAllocatedSequence(resource, sequence, vesselStartTime, voyagePlans);
	}

	/**
	 * This method replaces the normal shipped cargo calculation path with one specific to DES purchase or FOB sale cargoes. However this currently merges in behaviour from other classes - such as
	 * scheduling and volume allocation - which should really stay in those other classes.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */

	private VolumeAllocatedSequence desOrFobSchedule(final IResource resource, final IVesselAvailability vesselAvailability, final ISequence sequence, final @Nullable IPortTimesRecord portTimesRecord,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		if (portTimesRecord == null) {
			return VolumeAllocatedSequence.empty(resource, sequence);
		}
		final int vesselStartTime = portTimesRecord.getFirstSlotTime();

		final ScheduledVoyagePlanResult result = voyagePlanEvaluator.evaluateNonShipped(resource, vesselAvailability, vesselStartTime, //
				portTimesRecord, //
				true, //annotatedSolution != null, // Keep solutions for export
				annotatedSolution);

		return new VolumeAllocatedSequence(resource, sequence, vesselStartTime, result.voyagePlans);
	}

	private void annotate(final ProfitAndLossSequences profitAndLossSequences, final IAnnotatedSolution annotatedSolution) {

		// now add some more data for each load slot
		final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
		for (final VolumeAllocatedSequence scheduledSequence : profitAndLossSequences) {

			for (final VoyagePlanRecord vpr : scheduledSequence.getVoyagePlanRecords()) {
				final IAllocationAnnotation allocationAnnotation = vpr.getAllocationAnnotation();
				final ICargoValueAnnotation cargoValueAnnotation = vpr.getCargoValueAnnotation();

				for (final IPortSlot portSlot : vpr.getPortTimesRecord().getSlots()) {
					assert portSlot != null;

					final ISequenceElement portElement = portSlotProvider.getElement(portSlot);
					assert portElement != null;

					if (allocationAnnotation != null) {
						elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);
					}
					if (cargoValueAnnotation != null) {
						elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_cargoValueAllocationInfo, cargoValueAnnotation);
					}
				}
			}
		}
	}
}
