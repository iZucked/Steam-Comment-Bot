/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.LinkedList;
import java.util.List;
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
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
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
//			 breakEvenEvaluator.processSchedule(scheduledSequences);
		}

		if (generatedCharterOutEvaluator != null) {
			// generatedCharterOutEvaluator.processSchedule(scheduledSequences);
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
				final List<IPortSlot> slots = annotation.getSlots();
				for (final IPortSlot portSlot : slots) {
					final ISequenceElement portElement = portSlotProvider.getElement(portSlot);
					elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_volumeAllocationInfo, annotation);
				}
			}
		}

		calculateProfitAndLoss(scheduledSequences, allocations, annotatedSolution);
	}

	private boolean detailsMatchAllocation(IAllocationAnnotation allocation, List<PortDetails> portDetails) {
		if (allocation == null) {
			return false;
		}

		if (allocation.getSlots().size() == portDetails.size() - 1) {
			for (int i = 0; i < portDetails.size() - 1; ++i) {
				if (portDetails.get(i).getOptions().getPortSlot() != allocation.getSlots().get(i)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
		// for now, only handle single load/discharge case
		// assert(allocation.getSlots().size() == 2);
		// return (firstDetails.getOptions().getPortSlot() == allocation.getSlots().get(0)) && (lastDetails.getOptions().getPortSlot() == allocation.getSlots().get(1));
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

					List<PortDetails> portDetails = new LinkedList<PortDetails>();
					for (Object obj : plan.getSequence()) {
						if (obj instanceof PortDetails) {
							portDetails.add((PortDetails) obj);
						}
					}
					PortDetails firstDetails = portDetails.get(0);
					PortDetails lastDetails = portDetails.get(portDetails.size() - 1);

					// TODO: this logic looks decidedly shaky
					boolean isDesFobCase = ((vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan.getSequence().length == 4);

					final IAllocationAnnotation currentAllocation = allocations.get(plan);

					if (detailsMatchAllocation(currentAllocation, portDetails)) {
						cargo = true;
						final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, sequence.getStartTime(), annotatedSolution);
						firstDetails.setTotalGroupProfitAndLoss(cargoGroupValue);
					} else if (isDesFobCase) {
						firstDetails = portDetails.get(1);
						lastDetails = portDetails.get(2);

						// firstDetails = (PortDetails) plan.getSequence()[1];
						// lastDetails = (PortDetails) plan.getSequence()[2];
						if (detailsMatchAllocation(currentAllocation, portDetails)) {
							cargo = true;

							// for now, only handle single load/discharge case
							assert (currentAllocation.getSlots().size() == 2);
							ILoadOption loadSlot = (ILoadOption) currentAllocation.getSlots().get(0);
							// TODO: Perhaps use the real slot time rather than always load?
							// TODO: Does it matter really?
							final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, currentAllocation.getSlotTime(loadSlot), annotatedSolution);
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
