/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Provider;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
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
	private IVolumeAllocator volumeAllocator;

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

	@Inject(optional = true)
	private IMarkToMarketProvider markToMarketProvider;

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
		final Map<VoyagePlan, IAllocationAnnotation> allocations = volumeAllocator.allocate(scheduledSequences);
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

		calculateProfitAndLoss(sequences, scheduledSequences, allocations, annotatedSolution);
	}

	// TODO: Push into entity value calculator?
	private void calculateProfitAndLoss(final ISequences sequences, final ScheduledSequences scheduledSequences, final Map<VoyagePlan, IAllocationAnnotation> allocations,
			final IAnnotatedSolution annotatedSolution) {

		if (entityValueCalculator == null) {
			return;
		}

		for (final ScheduledSequence sequence : scheduledSequences) {
			final IVessel vessel = vesselProvider.getVessel(sequence.getResource());

			int time = sequence.getStartTime();

			for (final VoyagePlan plan : sequence.getVoyagePlans()) {
				boolean cargo = false;
				if (plan.getSequence().length >= 3) {

					// Extract list of all the PortDetails encountered
					final List<PortDetails> portDetails = new LinkedList<PortDetails>();
					for (final Object obj : plan.getSequence()) {
						if (obj instanceof PortDetails) {
							portDetails.add((PortDetails) obj);
						}
					}

					// TODO: this logic looks decidedly shaky - plan sequence length could change with logic changes
					final boolean isDesFobCase = ((vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan
							.getSequence().length == 4);

					final IAllocationAnnotation currentAllocation = allocations.get(plan);
					if (currentAllocation != null) {
						if (isDesFobCase) {
							final PortDetails firstDetails = portDetails.get(1);

							cargo = true;

							// for now, only handle single load/discharge case
							assert (currentAllocation.getSlots().size() == 2);
							final ILoadOption loadSlot = (ILoadOption) currentAllocation.getSlots().get(0);
							// TODO: Perhaps use the real slot time rather than always load?
							// TODO: Does it matter really?
							final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, currentAllocation.getSlotTime(loadSlot), annotatedSolution);
							firstDetails.setTotalGroupProfitAndLoss(cargoGroupValue);

						} else {
							final PortDetails firstDetails = portDetails.get(0);
							cargo = true;
							final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, sequence.getStartTime(), annotatedSolution);
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

		if (annotatedSolution != null && markToMarketProvider != null) {
			// Mark-to-Market Calculations

			for (final ISequenceElement element : sequences.getUnusedElements()) {
				if (element == null) {
					continue;
				}
				final IMarkToMarket market = markToMarketProvider.getMarketForElement(element);
				if (market == null) {
					continue;
				}

				final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

				final ILoadOption loadOption;
				final IDischargeOption dischargeOption;
				final int time;

				final IVessel vessel;
				if (portSlot instanceof ILoadSlot) {
					loadOption = (ILoadOption) portSlot;
					dischargeOption = new MarkToMarketDischargeOption(market, loadOption);
					time = loadOption.getTimeWindow().getStart();
					vessel = new MarkToMarketVessel(market, loadOption);
				} else if (portSlot instanceof IDischargeSlot) {
					dischargeOption = (IDischargeOption) portSlot;
					loadOption = new MarkToMarketLoadOption(market, dischargeOption);
					time = dischargeOption.getTimeWindow().getStart();
					vessel = new MarkToMarketVessel(market, dischargeOption);
				} else {
					continue;
				}

				// Create voyage plan
				final VoyagePlan voyagePlan = new VoyagePlan();
				{
					final PortOptions loadOptions = new PortOptions();
					final PortDetails loadDetails = new PortDetails();
					loadDetails.setOptions(loadOptions);
					loadOptions.setVisitDuration(0);
					loadOptions.setPortSlot(loadOption);

					final PortOptions dischargeOptions = new PortOptions();
					final PortDetails dischargeDetails = new PortDetails();
					dischargeDetails.setOptions(dischargeOptions);
					dischargeOptions.setVisitDuration(0);
					dischargeOptions.setPortSlot(dischargeOption);

					voyagePlan.setSequence(new Object[] { loadDetails, dischargeDetails });
				}

				// Create an allocation annotation.
				IAllocationAnnotation allocationAnnotation = volumeAllocator.allocate(vessel, voyagePlan, Lists.newArrayList(Integer.valueOf(time), Integer.valueOf(time)));

				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);

				// Calculate P&L
				entityValueCalculator.evaluate(voyagePlan, allocationAnnotation, vessel, time, annotatedSolution);
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
