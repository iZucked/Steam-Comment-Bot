/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
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
	private Provider<ScheduledDataLookupProvider> scheduledDataLookupProviderProvider;

	@Inject
	private CapacityViolationChecker capacityViolationChecker;

	@Inject
	private LatenessChecker latenessChecker;

	@Inject
	private IdleTimeChecker idleTimeChecker;

	@Inject
	private IVolumeAllocator volumeAllocator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject(optional = true)
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private VoyagePlanAnnotator voyagePlanAnnotator;

	@Inject(optional = true)
	private IMarkToMarketProvider markToMarketProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private VoyagePlanner voyagePlanner;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject(optional = true)
	private ICustomNonShippedScheduler customNonShippedScheduler;
	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

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
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			// Virtual vessels are those operated by a third party, for FOB and DES situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean much,
			// but contracts need this kind of information to make up numbers with.
			return desOrFobSchedule(resource, sequence);
		}

		// If this is the cargo shorts sequence, but we have no data (i.e. there are no short cargoes), return the basic data structure to avoid any exceptions
		final boolean isShortsSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;
		if (isShortsSequence && arrivalTimes.length == 0) {
			return new ScheduledSequence(resource, sequence, 0, Collections.<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> emptyList());
		}

		// Get start time
		final int startTime = arrivalTimes[0];

		// Generate all the voyageplans and extra annotations for this sequence
		final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans = voyagePlanner.makeVoyagePlans(resource, sequence, arrivalTimes);
		if (voyagePlans == null) {
			return null;
		}

		// Put it all together and return
		final ScheduledSequence scheduledSequence = new ScheduledSequence(resource, sequence, startTime, voyagePlans);

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
	private ScheduledSequence desOrFobSchedule(final IResource resource, final ISequence sequence) {
		// Virtual vessels are those operated by a third party, for FOB and DES situations.
		// Should we compute a schedule for them anyway? The arrival times don't mean much,
		// but contracts need this kind of information to make up numbers with.
		final List<IDetailsSequenceElement> currentSequence = new ArrayList<IDetailsSequenceElement>(5);
		final VoyagePlan currentPlan = new VoyagePlan();
		currentPlan.setIgnoreEnd(false);
		boolean dischargeOptionInMMBTU = false;
		final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(resource);
		if (nominatedVessel != null) {
			// Set a start and end heel for BOG estimations

			currentPlan.setStartingHeelInM3(nominatedVessel.getVesselClass().getSafetyHeel());
			currentPlan.setRemainingHeelInM3(nominatedVessel.getVesselClass().getSafetyHeel());
		}
		boolean startSet = false;
		int startTime = 0;
		for (final ISequenceElement element : sequence) {

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			if (thisPortSlot instanceof StartPortSlot) {
				continue;
			}
			if (thisPortSlot instanceof EndPortSlot) {
				continue;
			}

			// Determine transfer time
			if (!startSet && !(thisPortSlot instanceof StartPortSlot)) {

				// Find latest window start for all slots in FOB/DES combo. However if DES divertable, ignore.
				if (thisPortSlot instanceof ILoadOption) {
					// Divertible DES has real time window.
					if (!shippingHoursRestrictionProvider.isDivertable(element)) {
						if (actualsDataProvider.hasActuals(thisPortSlot)) {
							startTime = actualsDataProvider.getArrivalTime(thisPortSlot);
						} else {
							final int windowStart = thisPortSlot.getTimeWindow().getStart();
							startTime = Math.max(windowStart, startTime);
						}
					}
				}
				if (thisPortSlot instanceof IDischargeOption) {
					if (actualsDataProvider.hasActuals(thisPortSlot)) {
						startTime = actualsDataProvider.getArrivalTime(thisPortSlot);
					} else {
						// Divertible FOB has sales time window
						// if (!shippingHoursRestrictionProvider.isDivertable(element)) {
						final int windowStart = thisPortSlot.getTimeWindow().getStart();
						startTime = Math.max(windowStart, startTime);
					}
					// }
					if (dischargeOptionInMMBTU == false && !(((IDischargeOption) thisPortSlot).isVolumeSetInM3())) {
						dischargeOptionInMMBTU = true;
					}
				}

				// Actuals Data...
				if (thisPortSlot instanceof ILoadOption) {
					// overwrite with actuals if need be
					if (actualsDataProvider.hasActuals(thisPortSlot)) {
						currentPlan.setStartingHeelInM3(actualsDataProvider.getStartHeelInM3(thisPortSlot));
					}
				}
				// overwrite with actuals if need be
				if (actualsDataProvider.hasReturnActuals(thisPortSlot)) {
					currentPlan.setRemainingHeelInM3(actualsDataProvider.getReturnHeelInM3(thisPortSlot));
				}
			}

			if (thisPortSlot instanceof IDischargeSlot) {
				// Break here to avoid processing further
				startSet = true;
			}

			final PortOptions portOptions = new PortOptions();
			final PortDetails portDetails = new PortDetails();
			portDetails.setOptions(portOptions);
			portOptions.setVisitDuration(0);
			portOptions.setPortSlot(thisPortSlot);
			currentSequence.add(portDetails);

		}

		if (currentSequence.size() == 0) {
			return new ScheduledSequence(resource, sequence, 0, Collections.<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> emptyList());
		}

		// TODO: This should come from the ISequencesScheduler
		final int times[] = new int[sequence.size()];
		Arrays.fill(times, startTime);
		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		for (final ISequenceElement element : sequence) {
			final IPortSlot slot = portSlotProvider.getPortSlot(element);
			portTimesRecord.setSlotTime(slot, startTime);
			portTimesRecord.setSlotDuration(slot, 0);
		}
		currentPlan.setSequence(currentSequence.toArray(new IDetailsSequenceElement[0]));

		if (customNonShippedScheduler != null) {
			customNonShippedScheduler.modifyArrivalTimes(resource, startTime, currentPlan, portTimesRecord);

			// Back apply any modified times to times array
			int idx = 0;
			for (final ISequenceElement element : sequence) {
				final IPortSlot slot = portSlotProvider.getPortSlot(element);
				times[idx++] = portTimesRecord.getSlotTime(slot);
			}
		}

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final int vesselStartTime = startTime;

		if (dischargeOptionInMMBTU) {
			// now we have the load and discharge pair, if an mmbtu discharge, convert to m3
			voyagePlanner.setDesPurchaseOrFobPurchaseM3VolumeDetails(currentSequence);
		}
		// TODO: This is not the place!
		final IAllocationAnnotation annotation = volumeAllocator.allocate(vesselAvailability, vesselStartTime, currentPlan, portTimesRecord);

		final ScheduledSequence scheduledSequence = new ScheduledSequence(resource, sequence, startTime,
				Collections.singletonList(new Triple<>(currentPlan, Collections.<IPortSlot, IHeelLevelAnnotation> emptyMap(), (IPortTimesRecord) annotation)));

		return scheduledSequence;
	}

	public ScheduledSequences schedule(@NonNull final ISequences sequences, final int @NonNull [][] arrivalTimes, @Nullable final IAnnotatedSolution solution) {
		final ScheduledSequences result = new ScheduledSequences();

		if (scheduledDataLookupProviderProvider != null) {
			ScheduledDataLookupProvider scheduledDataLookupProvider = scheduledDataLookupProviderProvider.get();
			scheduledDataLookupProvider.reset();
		}

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

		if (scheduledDataLookupProviderProvider != null) {
			ScheduledDataLookupProvider scheduledDataLookupProvider = scheduledDataLookupProviderProvider.get();
			scheduledDataLookupProvider.reset();
		}

		if (annotatedSolution != null) {
			// Do basic voyageplan annotation
			// TODO: Roll in the other annotations!

			for (final ScheduledSequence scheduledSequence : scheduledSequences) {
				final IResource resource = scheduledSequence.getResource();
				assert resource != null;
				final ISequence sequence = sequences.getSequence(resource);
				assert sequence != null;

				if (sequence.size() > 0) {
					voyagePlanAnnotator.annotateFromScheduledSequence(scheduledSequence, annotatedSolution);
				}
			}
		}

		// Next we do P&L related business; first we have to assign the load volumes,
		// and then compute the resulting P&L fitness components.

		// Store annotations if required
		if (annotatedSolution != null) {
			// TODO: Feed into the VPA!

			// now add some more data for each load slot
			final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
			for (final ScheduledSequence scheduledSequence : scheduledSequences) {
				for (final IPortSlot portSlot : scheduledSequence.getSequenceSlots()) {
					assert portSlot != null;


					final IAllocationAnnotation allocationAnnotation = scheduledSequence.getAllocationAnnotation(portSlot);
					final IHeelLevelAnnotation heelLevelAnnotation = scheduledSequence.getHeelLevelAnnotation(portSlot);
					final ISequenceElement  portElement		= portSlotProvider.getElement(portSlot);
						
			
					
//					 = portSlotProvider.getElement(portSlot);
					if (portElement == null) {
						int ii = 0;
					}
					assert portElement != null;
					if (allocationAnnotation != null) {
						elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);
					}

					if (heelLevelAnnotation != null) {
						elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_heelLevelInfo, heelLevelAnnotation);
					}
				}
			}
		}

		if (scheduledDataLookupProviderProvider != null) {
			ScheduledDataLookupProvider scheduledDataLookupProvider = scheduledDataLookupProviderProvider.get();
			scheduledDataLookupProvider.setInputs(scheduledSequences);
		}

		calculateProfitAndLoss(sequences, scheduledSequences, annotatedSolution);

		// Perform capacity violations analysis
		capacityViolationChecker.calculateCapacityViolations(scheduledSequences, annotatedSolution);

		// Perform capacity violations analysis
		latenessChecker.calculateLateness(scheduledSequences, annotatedSolution);
		
		// Idle time checker
		idleTimeChecker.calculateIdleTime(scheduledSequences, annotatedSolution);
	}

	// TODO: Push into entity value calculator?
	private void calculateProfitAndLoss(final ISequences sequences, final ScheduledSequences scheduledSequences, // final Map<VoyagePlan, IAllocationAnnotation> allocations,
			final IAnnotatedSolution annotatedSolution) {

		if (entityValueCalculator == null) {
			return;
		}

		// 2014-02-12 - SG
		// Back hack to allow a custom contract to reset cached data AFTER any charter out generation / break -even code has run.
		// TODO: Need a better phase system to notify components where we are in the process.
		{
			for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
				shippingCalculator.prepareRealPNL();
			}
			for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
				calculator.prepareRealPNL();
			}
		}
		for (final ScheduledSequence sequence : scheduledSequences) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(sequence.getResource());
			assert vesselAvailability != null;

			int time = sequence.getStartTime();

			// for (final VoyagePlan plan : sequence.getVoyagePlans()) {
			for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> entry : sequence.getVoyagePlans()) {
				boolean cargo = false;
				final VoyagePlan plan = entry.getFirst();
				final IPortTimesRecord portTimesRecord = entry.getThird();
				final IAllocationAnnotation currentAllocation = (portTimesRecord instanceof IAllocationAnnotation) ? (IAllocationAnnotation) portTimesRecord : null;
				if (plan.getSequence().length >= 2) {

					// Extract list of all the PortDetails encountered
					final List<PortDetails> portDetails = new LinkedList<PortDetails>();
					for (final Object obj : plan.getSequence()) {
						if (obj instanceof PortDetails) {
							portDetails.add((PortDetails) obj);
						}
					}

					// TODO: this logic looks decidedly shaky - plan sequence length could change with logic changes
					final boolean isDesFobCase = ((vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE
							|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan.getSequence().length == 2);
					if (currentAllocation != null) {
						final CargoValueAnnotation cargoValueAnnotation;// = new CargoValueAnnotation(currentAllocation);
						cargo = true;
						if (isDesFobCase) {
							// for now, only handle single load/discharge case
							assert (currentAllocation.getSlots().size() == 2);
							final ILoadOption loadSlot = (ILoadOption) currentAllocation.getSlots().get(0);
							Pair<CargoValueAnnotation, Long> p = entityValueCalculator.evaluate(plan, currentAllocation, vesselAvailability, currentAllocation.getSlotTime(loadSlot),
									annotatedSolution);
							cargoValueAnnotation = p.getFirst();
							final long cargoGroupValue = p.getSecond();
							scheduledSequences.setVoyagePlanGroupValue(plan, cargoGroupValue);

						} else {
							Pair<CargoValueAnnotation, Long> p = entityValueCalculator.evaluate(plan, currentAllocation, vesselAvailability, sequence.getStartTime(), annotatedSolution);
							cargoValueAnnotation = p.getFirst();
							final long cargoGroupValue = p.getSecond();
							scheduledSequences.setVoyagePlanGroupValue(plan, cargoGroupValue);
						}

						// Store annotations if required
						if (annotatedSolution != null) {
							// now add some more data for each load slot
							final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
							final List<IPortSlot> slots = currentAllocation.getSlots();
							for (final IPortSlot portSlot : slots) {
								assert portSlot != null;
								final ISequenceElement portElement = portSlotProvider.getElement(portSlot);
								assert portElement != null;
								elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_cargoValueAllocationInfo, cargoValueAnnotation);
							}
						}
					}
				}

				if (!cargo) {
					final long otherGroupValue = entityValueCalculator.evaluate(plan, vesselAvailability, time, sequence.getStartTime(), annotatedSolution);
					scheduledSequences.setVoyagePlanGroupValue(plan, otherGroupValue);
				}
				time += getPlanDuration(plan);
			}

		}

		calculateUnusedSlotPNL(sequences, scheduledSequences, annotatedSolution);

		if (annotatedSolution != null && markToMarketProvider != null) {
			// calculateMarkToMarketPNL(sequences, annotatedSolution);
		}
	}

	protected void calculateUnusedSlotPNL(final ISequences sequences, final ScheduledSequences scheduledSequences, @Nullable final IAnnotatedSolution annotatedSolution) {

		for (final ISequenceElement element : sequences.getUnusedElements()) {
			if (element == null) {
				continue;
			}
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			if (portSlot instanceof ILoadOption || portSlot instanceof IDischargeOption) {
				// Calculate P&L
				final long groupValue = entityValueCalculator.evaluateUnusedSlot(portSlot, annotatedSolution);
				scheduledSequences.setUnusedSlotGroupValue(portSlot, groupValue);
			}
		}
	}

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

			final IVesselAvailability vesselAvailability;
			if (portSlot instanceof ILoadOption) {
				loadOption = (ILoadOption) portSlot;
				if (loadOption instanceof ILoadSlot) {
					dischargeOption = new MarkToMarketDischargeOption(market, loadOption);
				} else {
					dischargeOption = new MarkToMarketDischargeSlot(market, loadOption);
				}
				time = loadOption.getTimeWindow().getStart();
				vesselAvailability = new MarkToMarketVesselAvailability(market, loadOption);
			} else if (portSlot instanceof IDischargeOption) {
				dischargeOption = (IDischargeOption) portSlot;
				if (dischargeOption instanceof IDischargeSlot) {
					loadOption = new MarkToMarketLoadOption(market, dischargeOption);
				} else {
					loadOption = new MarkToMarketLoadSlot(market, dischargeOption);
				}
				time = dischargeOption.getTimeWindow().getStart();
				vesselAvailability = new MarkToMarketVesselAvailability(market, dischargeOption);
			} else {
				continue;
			}

			final PortTimesRecord portTimesRecord = new PortTimesRecord();
			portTimesRecord.setSlotTime(loadOption, time);
			portTimesRecord.setSlotTime(dischargeOption, time);
			portTimesRecord.setSlotDuration(loadOption, 0);
			portTimesRecord.setSlotDuration(dischargeOption, 0);

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
			voyagePlan.setIgnoreEnd(false);
			// Create an allocation annotation.
			final IAllocationAnnotation allocationAnnotation = volumeAllocator.allocate(vesselAvailability, time, voyagePlan, portTimesRecord);
			if (allocationAnnotation != null) {
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);
				final CargoValueAnnotation cargoValueAnnotation = new CargoValueAnnotation(allocationAnnotation);
				// Calculate P&L
				entityValueCalculator.evaluate(voyagePlan, cargoValueAnnotation, vesselAvailability, time, annotatedSolution);
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_cargoValueAllocationInfo, cargoValueAnnotation);
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
