/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.Map;

import javax.inject.Provider;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
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
	private IVolumeAllocator cargoAllocator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@com.google.inject.Inject(optional = true)
	private IGeneratedCharterOutEvaluator generatedCharterOutEvaluator;

	@com.google.inject.Inject(optional = true)
	private IBreakEvenEvaluator breakEvenEvaluator;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject(optional = true)
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private Provider<VoyagePlanAnnotator> voyagePlanAnnotatorProvider;

	public IAnnotatedSolution calculateSchedule(final ISequences sequences, final ScheduledSequences scheduledSequences) {
		final AnnotatedSolution annotatedSolution = new AnnotatedSolution();
		annotatedSolution.setSequences(sequences);
		calculateSchedule(sequences, scheduledSequences, annotatedSolution);
		return annotatedSolution;
	}

	public void calculateSchedule(final ISequences sequences, final ScheduledSequences scheduledSequences, final IAnnotatedSolution annotatedSolution) {

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}
		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
			calculator.prepareEvaluation(sequences);
		}

		// FIXME: This should be more customisable

		// Execute custom logic to manipulate the schedule and choices
		if (breakEvenEvaluator != null) {
			breakEvenEvaluator.processSchedule(scheduledSequences);
		}

		if (generatedCharterOutEvaluator != null) {
			generatedCharterOutEvaluator.processSchedule(scheduledSequences);
		}

		if (annotatedSolution != null) {
			// Do basic voyageplan annotation
			// TODO: Roll in the other annotations!
			final VoyagePlanAnnotator annotator = voyagePlanAnnotatorProvider.get();

			for (final ScheduledSequence scheduledSequence : scheduledSequences) {
				final IResource resource = scheduledSequence.getResource();
				final ISequence sequence = sequences.getSequence(resource);

				if (sequence.size() > 0) {
					annotator.annotateFromScheduledSequence(scheduledSequence, annotatedSolution);
				}
			}
		}

		// Next we do P&L related business; first we have to assign the load volumes,
		// and then compute the resulting P&L fitness components.

		// Compute load volumes and prices
		final Map<VoyagePlan, IAllocationAnnotation> allocations = cargoAllocator.allocate(scheduledSequences);
		scheduledSequences.setAllocations(allocations);
		// Store annotations if required
		if (annotatedSolution != null) {

			// TODO: Feed into the VPA!

			annotatedSolution.setGeneralAnnotation(SchedulerConstants.G_AI_allocations, allocations);

			// now add some more data for each load slot
			final IAnnotations elementAnnotations = annotatedSolution.getElementAnnotations();
			for (final IAllocationAnnotation annotation : allocations.values()) {
				final ISequenceElement loadElement = portSlotProvider.getElement(annotation.getLoadOption());
				final ISequenceElement dischargeElement = portSlotProvider.getElement(annotation.getDischargeOption());
				elementAnnotations.setAnnotation(loadElement, SchedulerConstants.AI_volumeAllocationInfo, annotation);
				elementAnnotations.setAnnotation(dischargeElement, SchedulerConstants.AI_volumeAllocationInfo, annotation);
			}
		}

		calculateProfitAndLoss(scheduledSequences, allocations, annotatedSolution);
	}

	// TODO: Push into entity value calculator?
	private void calculateProfitAndLoss(final ScheduledSequences scheduledSequences, final Map<VoyagePlan, IAllocationAnnotation> allocations, final IAnnotatedSolution annotatedSolution) {

		if (entityValueCalculator == null) {
			return;
		}

		for (final ScheduledSequence sequence : scheduledSequences) {
			final IVessel vessel = vesselProvider.getVessel(sequence.getResource());

			int time = sequence.getStartTime();

			// FIXME : This is messy, what if iterator order is out of sync!
			// TODO Turn into a map! -- volume allocator should perform the hook up

			for (final VoyagePlan plan : sequence.getVoyagePlans()) {
				boolean cargo = false;
				if (plan.getSequence().length >= 3) {

					PortDetails firstDetails = (PortDetails) plan.getSequence()[0];
					PortDetails lastDetails = (PortDetails) plan.getSequence()[2];

					final IAllocationAnnotation currentAllocation = allocations.get(plan);

					if ((currentAllocation != null)
							&& ((firstDetails.getOptions().getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getOptions().getPortSlot() == currentAllocation.getDischargeOption()))) {
						cargo = true;
						final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, sequence.getStartTime(), annotatedSolution);
						firstDetails.setTotalGroupProfitAndLoss(cargoGroupValue);
					} else if ((vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan.getSequence().length == 4) {
						firstDetails = (PortDetails) plan.getSequence()[1];
						lastDetails = (PortDetails) plan.getSequence()[2];
						if ((currentAllocation != null)
								&& ((firstDetails.getOptions().getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getOptions().getPortSlot() == currentAllocation.getDischargeOption()))) {
							cargo = true;
							// TODO: Perhaps use the real slot time rather than always load?
							// TODO: Does it matter really?
							final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, currentAllocation.getLoadTime(), annotatedSolution);
							firstDetails.setTotalGroupProfitAndLoss(cargoGroupValue);
						}
					}
				}

				if (!cargo) {
					final long otherGroupValue = entityValueCalculator.evaluate(plan, vessel, time, sequence.getStartTime(), annotatedSolution);
					final PortDetails firstDetails = (PortDetails) plan.getSequence()[0];
					firstDetails.setTotalGroupProfitAndLoss(otherGroupValue);
				}
				time += getPlanDuration(plan);
			}
		}
	}

	private int getPlanDuration(final VoyagePlan plan) {
		return getPartialPlanDuration(plan, 0);
	}

	private int getPartialPlanDuration(final VoyagePlan plan, final int skip) {
		int planDuration = 0;
		final Object[] sequence = plan.getSequence();
		final int k = sequence.length - skip;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			planDuration += (o instanceof VoyageDetails) ? (((VoyageDetails) o).getIdleTime() + ((VoyageDetails) o).getTravelTime()) : ((PortDetails) o).getOptions().getVisitDuration();
		}
		return planDuration;
	}

}
