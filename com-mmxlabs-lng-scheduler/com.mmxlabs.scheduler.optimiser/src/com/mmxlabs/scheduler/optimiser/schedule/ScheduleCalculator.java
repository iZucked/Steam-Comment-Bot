/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
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
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
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

	@Inject(optional = true)
	private ScheduledDataLookupProvider scheduledDataLookupProvider;

	@Inject
	private CapacityViolationChecker capacityViolationChecker;

	@Inject
	private IVolumeAllocator volumeAllocator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject(optional = true)
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private Provider<VoyagePlanAnnotator> voyagePlanAnnotatorProvider;

	@Inject(optional = true)
	private IMarkToMarketProvider markToMarketProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private VoyagePlanner voyagePlanner;

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
	private ScheduledSequence schedule(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {
		final IVessel vessel = vesselProvider.getVessel(resource);

		if (vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			// Virtual vessels are those operated by a third party, for FOB and DES situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean much,
			// but contracts need this kind of information to make up numbers with.
			return desOrFobSchedule(resource, sequence);
		}

		final boolean isShortsSequence = vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		// TODO: document this code path
		if (isShortsSequence && arrivalTimes.length == 0) {
			return new ScheduledSequence(resource, 0, Collections.<VoyagePlan> emptyList(), new int[] { 0 });
		}

		// Get start time
		final int startTime = arrivalTimes[0];

		final Map<VoyagePlan, IAllocationAnnotation> voyagePlans = voyagePlanner.makeVoyagePlans(resource, sequence, arrivalTimes);
		if (voyagePlans == null) {
			return null;
		}

		final ScheduledSequence scheduledSequence = new ScheduledSequence(resource, startTime, new ArrayList<>(voyagePlans.keySet()), arrivalTimes);
		scheduledSequence.getAllocations().putAll(voyagePlans);
		return scheduledSequence;
	}

	private ScheduledSequence desOrFobSchedule(final IResource resource, final ISequence sequence) {
		// Virtual vessels are those operated by a third party, for FOB and DES situations.
		// Should we compute a schedule for them anyway? The arrival times don't mean much,
		// but contracts need this kind of information to make up numbers with.
		final List<IDetailsSequenceElement> currentSequence = new ArrayList<IDetailsSequenceElement>(5);
		final VoyagePlan currentPlan = new VoyagePlan();

		boolean startSet = false;
		int startTime = 0;
		for (final ISequenceElement element : sequence) {

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);

			// Determine transfer time
			// TODO: This might need updating when we complete FOB/DES work - the load slot may not have a real time window
			if (!startSet && !(thisPortSlot instanceof StartPortSlot)) {

				if (thisPortSlot instanceof ILoadSlot) {
					startTime = thisPortSlot.getTimeWindow().getStart();
					startSet = true;
				}
				if (thisPortSlot instanceof IDischargeSlot) {
					startTime = thisPortSlot.getTimeWindow().getStart();
					startSet = true;
				}
			}

			final PortOptions portOptions = new PortOptions();
			final PortDetails portDetails = new PortDetails();
			portDetails.setOptions(portOptions);
			portOptions.setVisitDuration(0);
			portOptions.setPortSlot(thisPortSlot);
			currentSequence.add(portDetails);

		}
		// TODO: This should come from the ISequencesScheduler
		final int times[] = new int[sequence.size()];
		Arrays.fill(times, startTime);
		currentPlan.setSequence(currentSequence.toArray(new IDetailsSequenceElement[0]));
		ScheduledSequence scheduledSequence = new ScheduledSequence(resource, startTime, Collections.singletonList(currentPlan), times);

		IVessel vessel = vesselProvider.getVessel(resource);
		int vesselStartTime = startTime;

		// TODO: This is not the place!
		final IAllocationAnnotation annotation = volumeAllocator.allocate(vessel, vesselStartTime, currentPlan, CollectionsUtil.toArrayList(times));
		scheduledSequence.getAllocations().put(currentPlan, annotation);

		return scheduledSequence;
	}

	public ScheduledSequences schedule(@NonNull final ISequences sequences, @NonNull final int[][] arrivalTimes, @Nullable IAnnotatedSolution solution) {
		final ScheduledSequences result = new ScheduledSequences();

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}
		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
			calculator.prepareEvaluation(sequences);
		}

		final List<IResource> resources = sequences.getResources();

		for (int i = 0; i < sequences.size(); i++) {
			final ISequence sequence = sequences.getSequence(i);
			final IResource resource = resources.get(i);

			final ScheduledSequence scheduledSequence = schedule(resource, sequence, arrivalTimes[i]);
			if (scheduledSequence == null) {
				return null;
			}
			result.add(scheduledSequence);
		}

		calculateSchedule(sequences, result, solution);

		return result;
	}

	private void calculateSchedule(final ISequences sequences, final ScheduledSequences scheduledSequences, final IAnnotatedSolution annotatedSolution) {

		if (scheduledDataLookupProvider != null) {
			scheduledDataLookupProvider.reset();
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
		final Map<VoyagePlan, IAllocationAnnotation> allocations = new HashMap<>();
		for (ScheduledSequence seq : scheduledSequences) {
			if (seq.getAllocations() != null) {
				for (Map.Entry<VoyagePlan, IAllocationAnnotation> e : seq.getAllocations().entrySet()) {
					if (e.getValue() != null) {
						allocations.put(e.getKey(), e.getValue());
					}
				}
			}
		}
		scheduledSequences.getAllocations();
		scheduledSequences.setAllocations(allocations);
		// Store annotations if required
		if (annotatedSolution != null) {

			// TODO: Feed into the VPA!

			// annotatedSolution.setGeneralAnnotation(SchedulerConstants.G_AI_allocations, allocations);

			// now add some more data for each load slot
			final IAnnotations elementAnnotations = annotatedSolution.getElementAnnotations();
			for (final IAllocationAnnotation annotation : allocations.values()) {
				if (annotation != null) {
					final List<IPortSlot> slots = annotation.getSlots();
					for (final IPortSlot portSlot : slots) {
						final ISequenceElement portElement = portSlotProvider.getElement(portSlot);
						elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_volumeAllocationInfo, annotation);
					}
				}
			}
		}

		if (scheduledDataLookupProvider != null) {
			scheduledDataLookupProvider.setInputs(sequences, scheduledSequences);
		}

		calculateProfitAndLoss(sequences, scheduledSequences, allocations, annotatedSolution);

		// Perform capacity violations analysis
		capacityViolationChecker.calculateCapacityViolations(sequences, scheduledSequences, allocations, annotatedSolution);
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
			calculateMarkToMarketPNL(sequences, annotatedSolution);

		}
	}

	/**
	 * @since 6.0
	 */
	protected void calculateMarkToMarketPNL(final ISequences sequences, final IAnnotatedSolution annotatedSolution) {
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
			if (portSlot instanceof ILoadOption) {
				loadOption = (ILoadOption) portSlot;
				if (loadOption instanceof ILoadSlot) {
					dischargeOption = new MarkToMarketDischargeOption(market, loadOption);
				} else {
					dischargeOption = new MarkToMarketDischargeSlot(market, loadOption);
				}
				time = loadOption.getTimeWindow().getStart();
				vessel = new MarkToMarketVessel(market, loadOption);
			} else if (portSlot instanceof IDischargeOption) {
				dischargeOption = (IDischargeOption) portSlot;
				if (dischargeOption instanceof IDischargeSlot) {
					loadOption = new MarkToMarketLoadOption(market, dischargeOption);
				} else {
					loadOption = new MarkToMarketLoadSlot(market, dischargeOption);
				}
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

				voyagePlan.setSequence(new IDetailsSequenceElement[] { loadDetails, dischargeDetails });
			}

			// Create an allocation annotation.
			final IAllocationAnnotation allocationAnnotation = volumeAllocator.allocate(vessel, time, voyagePlan, Lists.newArrayList(Integer.valueOf(time), Integer.valueOf(time)));
			if (allocationAnnotation != null) {
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
