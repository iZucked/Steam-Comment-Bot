/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.HeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord.AllocationMode;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class VoyagePlanner {
	@Inject
	private IElementDurationProvider durationsProvider;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IVoyagePlanOptimiser voyagePlanOptimiser;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IVolumeAllocator volumeAllocator;

	@com.google.inject.Inject(optional = true)
	private IBreakEvenEvaluator breakEvenEvaluator;

	@com.google.inject.Inject(optional = true)
	private IGeneratedCharterOutEvaluator generatedCharterOutEvaluator;

	@Inject
	private ICharterRateCalculator charterRateCalculator;
	
	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	/**
	 * Returns a voyage options object and extends the current VPO with appropriate choices for a particular journey. TODO: refactor this if possible to simplify it and make it stateless (it currently
	 * messes with the VPO).
	 * 
	 * @param vessel
	 * @param vesselState
	 * @param availableTime
	 * @param element
	 * @param prevElement
	 * @param previousOptions
	 * @param useNBO
	 * @return
	 */
	private VoyageOptions getVoyageOptionsAndSetVpoChoices(final IVesselAvailability vesselAvailability, final VesselState vesselState, final int availableTime, final ISequenceElement element,
			final ISequenceElement prevElement, final VoyageOptions previousOptions, final IVoyagePlanOptimiser optimiser, boolean useNBO) {

		final IVesselClass vesselClass = vesselAvailability.getVessel().getVesselClass();

		final IPort thisPort = portProvider.getPortForElement(element);
		final IPort prevPort = portProvider.getPortForElement(prevElement);
		final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
		final IPortSlot prevPortSlot = portSlotProvider.getPortSlot(prevElement);
		final PortType prevPortType = portTypeProvider.getPortType(prevElement);

		final VoyageOptions options = new VoyageOptions();

		options.setVessel(vesselAvailability.getVessel());
		options.setFromPortSlot(prevPortSlot);
		options.setToPortSlot(thisPortSlot);
		options.setVesselState(vesselState);
		options.setAvailableTime(availableTime);

		// Flag to force NBO use over cost choice - e.g. for cases where there is already a heel onboard
		boolean forceNBO = false;

		if ((prevPortType == PortType.Load) || (prevPortType == PortType.CharterOut)) {
			useNBO = true;
		}

		final int cargoCV;
		if (actualsDataProvider.hasActuals(prevPortSlot)) {
			cargoCV = actualsDataProvider.getCVValue(prevPortSlot);
		} else if (prevPortSlot instanceof IHeelOptionsPortSlot) {
			final IHeelOptionsPortSlot heelOptionsSlot = (IHeelOptionsPortSlot) prevPortSlot;
			if (heelOptionsSlot.getHeelOptions().getHeelLimit() > 0) {
				useNBO = true;
				forceNBO = false;
			} else {
				useNBO = false;
				forceNBO = false;
				// FIXME: Setting 0 here breaks most unit tests....
				// cargoCV = 0;
			}
			cargoCV = heelOptionsSlot.getHeelOptions().getHeelCVValue();
		} else if (prevPortSlot instanceof ILoadOption) {
			final ILoadOption loadOption = (ILoadOption) prevPortSlot;
			cargoCV = loadOption.getCargoCVValue();
		} else {
			cargoCV = previousOptions.getCargoCVValue();
		}

		if ((prevPortType == PortType.DryDock) || (prevPortType == PortType.Maintenance)) {
			options.setWarm(true);
		} else {
			options.setWarm(false);
		}

		options.setCargoCVValue(cargoCV);

		// Convert rate to MT equivalent per day
		final int nboRateInMTPerDay = (int) Calculator.convertM3ToMT(vesselClass.getNBORate(vesselState), cargoCV, vesselClass.getBaseFuel().getEquivalenceFactor());
		if (nboRateInMTPerDay > 0) {
			final int nboSpeed = vesselClass.getConsumptionRate(vesselState).getSpeed(nboRateInMTPerDay);
			options.setNBOSpeed(nboSpeed);
		}
		// Determined by voyage plan optimiser
		options.setUseNBOForTravel(useNBO);
		options.setUseFBOForSupplement(false);
		// If not forced, then a choice may be added later
		options.setUseNBOForIdle(forceNBO);

		if (thisPortSlot.getPortType() == PortType.Load) {
			options.setShouldBeCold(true);
			final ILoadSlot thisLoadSlot = (ILoadSlot) thisPortSlot;

			if (thisLoadSlot.isCooldownSet()) {
				if (thisLoadSlot.isCooldownForbidden()) {
					// cooldown may still happen if circumstances allow
					// no alternative.
					options.setAllowCooldown(false);
				} else {
					options.setAllowCooldown(true);
				}
			} else {
				if (thisPort.shouldVesselsArriveCold()) {
					// we don't want to use cooldown ever
					options.setAllowCooldown(false);
				} else {
					// we have a choice
					options.setAllowCooldown(false);
					optimiser.addChoice(new CooldownVoyagePlanChoice(options));
				}
			}
		} else if (thisPortSlot.getPortType() == PortType.End) {
			final IResource resource = vesselProvider.getResource(vesselAvailability);
			final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
			options.setShouldBeCold(endRequirement.isEndCold());
			options.setAllowCooldown(false);
		} else {
			options.setShouldBeCold(false);
		}

		// Enable choices only if NBO could be available.
		if (useNBO) {
			// Note ordering of choices is important = NBO must be set
			// before FBO and Idle choices, otherwise if NBO choice is
			// false, FBO and IdleNBO may still be true if set before
			// NBO

			if (vesselState == VesselState.Ballast && !forceNBO) {
				optimiser.addChoice(new NBOTravelVoyagePlanChoice(previousOptions, options));
			}

			optimiser.addChoice(new FBOVoyagePlanChoice(options));

			if (!forceNBO) {
				optimiser.addChoice(new IdleNBOVoyagePlanChoice(options));
			}
		}

		final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(prevPort, thisPort));
		// Only add route choice if there is one
		if (distances.size() == 1) {
			final MatrixEntry<IPort, Integer> d = distances.get(0);
			options.setDistance(d.getValue());
			options.setRoute(d.getKey());
		} else {
			optimiser.addChoice(new RouteVoyagePlanChoice(options, distances));
		}

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER && thisPortSlot.getPortType() == PortType.End) {
			// The SchedulerBuilder should set options to trigger these values to be set above
			assert !options.getAllowCooldown();
			assert options.shouldBeCold();
			options.setAllowCooldown(false);
			options.setShouldBeCold(true);
		}

		return options;
	}

	/**
	 * Returns a list of voyage plans based on breaking up a sequence of vessel real or virtual destinations into single conceptual cargo voyages.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	final public List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IAllocationAnnotation>> makeVoyagePlans(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {

		// TODO: Handle FOB/DES cargoes also

		// IF FOB/DES
		// customFOBDESModeller.createVoyagePlan()
		final int vesselStartTime = arrivalTimes[0];

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		final boolean isShortsSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IAllocationAnnotation>> voyagePlansMap = new LinkedList<>();
		final List<VoyagePlan> voyagePlansList = new LinkedList<>();

		final List<IOptionsSequenceElement> voyageOrPortOptions = new ArrayList<IOptionsSequenceElement>(5);
		// final List<Integer> currentTimes = new ArrayList<Integer>(3);
		// final List<Integer> currentDurations = new ArrayList<Integer>(3);

		PortTimesRecord portTimesRecord = new PortTimesRecord();
		final boolean[] breakSequence = findSequenceBreaks(sequence);
		final VesselState[] states = findVesselStates(sequence);

		ISequenceElement prevElement = null;
		IPort prevPort = null;
		IPort prev2Port = null;
		IPortSlot prevPortSlot = null;
		// Used for end of sequence checks
		IPortSlot prevPrevPortSlot = null;

		VoyageOptions previousOptions = null;
		boolean useNBO = false;

		int prevVisitDuration = 0;
		final Iterator<ISequenceElement> itr = sequence.iterator();

		long heelVolumeInM3 = 0;
		// For spot charters, start with the safety heel.
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER) {
			heelVolumeInM3 = vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
			assert heelVolumeInM3 >= 0;
		}

		for (int idx = 0; itr.hasNext(); ++idx) {
			final ISequenceElement element = itr.next();

			final IPort thisPort = portProvider.getPortForElement(element);
			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			final PortType portType = portTypeProvider.getPortType(element);

			// If this is the first port, then this will be null and there will
			// be no voyage to plan.
			int shortCargoReturnArrivalTime = 0;
			if (prevPort != null) {
				final int availableTime;
				final boolean isShortCargoEnd = isShortsSequence && portType == PortType.Short_Cargo_End;

				if (!isShortCargoEnd) {
					// Available time, as determined by inputs.
					availableTime = arrivalTimes[idx] - arrivalTimes[idx - 1] - prevVisitDuration;
				} else { // shorts cargo end on shorts sequence
					int minTravelTime = Integer.MAX_VALUE;
					for (final MatrixEntry<IPort, Integer> entry : distanceProvider.getValues(prevPort, prev2Port)) {
						final int distance = entry.getValue();
						final int extraTime = routeCostProvider.getRouteTransitTime(entry.getKey(), vesselAvailability.getVessel().getVesselClass());
						final int minByRoute = Calculator.getTimeFromSpeedDistance(vesselAvailability.getVessel().getVesselClass().getMaxSpeed(), distance) + extraTime;
						minTravelTime = Math.min(minTravelTime, minByRoute);
					}
					availableTime = minTravelTime;

					shortCargoReturnArrivalTime = arrivalTimes[idx - 1] + prevVisitDuration + availableTime;
				}

				final VoyageOptions options = getVoyageOptionsAndSetVpoChoices(vesselAvailability, states[idx], availableTime, element, prevElement, previousOptions, voyagePlanOptimiser, useNBO);
				useNBO = options.useNBOForTravel();
				voyageOrPortOptions.add(options);
				previousOptions = options;
			}

			final int visitDuration = actualsDataProvider.hasActuals(thisPortSlot) ? actualsDataProvider.getVisitDuration(thisPortSlot) : durationsProvider.getElementDuration(element, resource);

			final PortOptions portOptions = new PortOptions();
			portOptions.setVisitDuration(visitDuration);
			portOptions.setPortSlot(thisPortSlot);
			portOptions.setVessel(vesselAvailability.getVessel());
			voyageOrPortOptions.add(portOptions);

			// Sequence scheduler should be using the actuals time
			assert actualsDataProvider.hasActuals(thisPortSlot) == false || actualsDataProvider.getArrivalTime(thisPortSlot) == arrivalTimes[idx];
			if (breakSequence[idx]) {
				if (isShortsSequence && portType == PortType.Short_Cargo_End) {
					portTimesRecord.setReturnSlotTime(thisPortSlot, shortCargoReturnArrivalTime);
				} else {
					portTimesRecord.setReturnSlotTime(thisPortSlot, arrivalTimes[idx]);
				}
			} else {
				if (isShortsSequence && portType == PortType.Short_Cargo_End) {
					portTimesRecord.setSlotTime(thisPortSlot, shortCargoReturnArrivalTime);
				} else {
					portTimesRecord.setSlotTime(thisPortSlot, arrivalTimes[idx]);
				}
				portTimesRecord.setSlotDuration(thisPortSlot, visitDuration);
			}
			if (breakSequence[idx]) {
				// Use prev slot as "thisPortSlot" is the start of a new voyage plan and thus likely a different cargo
				if (actualsDataProvider.hasActuals(prevPortSlot)) {
					heelVolumeInM3 = generateActualsVoyagePlan(vesselAvailability, vesselStartTime, voyagePlansMap, voyagePlansList, voyageOrPortOptions, portTimesRecord, heelVolumeInM3);
					assert heelVolumeInM3 >= 0;
					// Reset VPO ready for next iteration - some data may have been added
					voyagePlanOptimiser.reset();
				} else {
					// set base fuel price in VPO
					setVesselAndBaseFuelPrice(voyagePlanOptimiser, portTimesRecord, vesselAvailability.getVessel(), resource);

					final boolean shortCargoEnd = ((PortOptions) voyageOrPortOptions.get(0)).getPortSlot().getPortType() == PortType.Short_Cargo_End;

					// Special case for cargo shorts routes. There is no voyage between a Short_Cargo_End and the next load - which this current sequence will represent. However we do need to model
					// the
					// Short_Cargo_End for the VoyagePlanIterator to work correctly. Here we strip the voyage and make this a single element sequence.
					if (!shortCargoEnd) {
						final int vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, /** FIXME: not utc */
						vesselStartTime, timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getFirstSlotTime(), portTimesRecord.getFirstSlot()));

						final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, portTimesRecord, voyagePlanOptimiser, heelVolumeInM3, vesselCharterInRatePerDay);
						if (plan == null) {
							return null;
						}
						{
							final IDetailsSequenceElement[] vpSequence = plan.getSequence();
							final VoyageDetails lastVoyage = (VoyageDetails) vpSequence[vpSequence.length - 2];
							if (lastVoyage.getOptions().getToPortSlot().getPortType() != PortType.End) {
								assert arrivalTimes[idx] == portTimesRecord.getSlotTime(portTimesRecord.getReturnSlot());

							}
						}
						arrivalTimes[idx] = portTimesRecord.getSlotTime(portTimesRecord.getReturnSlot());

						heelVolumeInM3 = evaluateVoyagePlan(vesselAvailability, vesselStartTime, voyagePlansMap, voyagePlansList, portTimesRecord, heelVolumeInM3, plan);
						assert heelVolumeInM3 >= 0;
					}

					if (isShortsSequence) {
						voyagePlansList.get(voyagePlansList.size() - 1).setIgnoreEnd(false);
					}
				}

				// Reset useNBO flag
				useNBO = false;
				previousOptions = null;

				voyageOrPortOptions.clear();
				voyageOrPortOptions.add(portOptions);

				// Reset object ref
				portTimesRecord = new PortTimesRecord();
				portTimesRecord.setSlotTime(thisPortSlot, arrivalTimes[idx]);
				portTimesRecord.setSlotDuration(thisPortSlot, visitDuration);
			}

			// If we are a heel options slots (i.e. Start or other vessel event slot, overwrite previous heel (assume lost) and replace with a new heel value
			// TODO: Move (back?)into VPO code
			if (thisPortSlot instanceof IHeelOptionsPortSlot) {
				heelVolumeInM3 = ((IHeelOptionsPortSlot) thisPortSlot).getHeelOptions().getHeelLimit();
				assert heelVolumeInM3 >= 0;
			}

			// Setup for next iteration
			prev2Port = prevPort;

			prevPort = thisPort;
			prevVisitDuration = visitDuration;
			prevElement = element;
			prevPrevPortSlot = prevPortSlot;
			prevPortSlot = thisPortSlot;
		}

		// TODO: Do we need to run optimiser when we only have a load port here?

		// Populate final plan details
		if (voyageOrPortOptions.size() > 1) {
			if (actualsDataProvider.hasActuals(prevPrevPortSlot)) {
				heelVolumeInM3 = generateActualsVoyagePlan(vesselAvailability, vesselStartTime, voyagePlansMap, voyagePlansList, voyageOrPortOptions, portTimesRecord, heelVolumeInM3);
				assert heelVolumeInM3 >= 0;
				// Reset VPO ready for next iteration - some data may have been added
				voyagePlanOptimiser.reset();
			} else {
				final int vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, /** FIXME: not utc */
				vesselStartTime, timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getFirstSlotTime(), portTimesRecord.getFirstSlot()));
				// set vessel and fuel price here
				setVesselAndBaseFuelPrice(voyagePlanOptimiser, portTimesRecord, vesselAvailability.getVessel(), resource);
				final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, portTimesRecord, voyagePlanOptimiser, heelVolumeInM3, vesselCharterInRatePerDay);
				if (plan == null) {
					return null;
				}

				// Fix up final arrival time. The VPO is permitted to change the final arrival time of certain vessels and we need to alter the arrival time array and the portTimesRecord with the
				// new arrival time.
				if (!isShortsSequence) {
					final int idx = arrivalTimes.length - 1;
					arrivalTimes[idx] = portTimesRecord.getSlotTime(portTimesRecord.getReturnSlot());
				}

				plan.setIgnoreEnd(false);
				heelVolumeInM3 = evaluateVoyagePlan(vesselAvailability, vesselStartTime, voyagePlansMap, voyagePlansList, portTimesRecord, heelVolumeInM3, plan);
				assert heelVolumeInM3 >= 0;

			}
		}

		return voyagePlansMap;
	}

	private long generateActualsVoyagePlan(final IVesselAvailability vesselAvailability, final int vesselStartTime,
			final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IAllocationAnnotation>> voyagePlansMap, final List<VoyagePlan> voyagePlansList,
			final List<IOptionsSequenceElement> voyageOrPortOptions, final IPortTimesRecord portTimesRecord, final long startHeelVolumeInM3) {
		final Map<IPortSlot, IHeelLevelAnnotation> heelLevelAnnotations = new HashMap<IPortSlot, IHeelLevelAnnotation>();

		final VoyagePlan plan = new VoyagePlan();
		// Replace with actuals later if needed
		plan.setCharterInRatePerDay(charterRateCalculator.getCharterRatePerDay(vesselAvailability, /** FIXME: not utc */
		vesselStartTime, timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getFirstSlotTime(), portTimesRecord.getFirstSlot())));
		plan.setStartingHeelInM3(startHeelVolumeInM3);
		{

			// Pass 1, get CV and sales price
			int idx = -1;
			int lngSalesPricePerMMBTu = 0;
			int cargoCV = 0;
			{
				for (final IOptionsSequenceElement element : voyageOrPortOptions) {
					++idx;
					if (element instanceof PortOptions) {
						final PortOptions portOptions = (PortOptions) element;

						if (portOptions.getPortSlot() instanceof ILoadOption && idx != voyageOrPortOptions.size() - 1) {
							cargoCV = actualsDataProvider.getCVValue(portOptions.getPortSlot());
							plan.setCharterInRatePerDay(actualsDataProvider.getCharterRatePerDay(portOptions.getPortSlot()));
						}

						else if (portOptions.getPortSlot() instanceof IDischargeOption) {
							lngSalesPricePerMMBTu = actualsDataProvider.getLNGPricePerMMBTu(portOptions.getPortSlot());
						}
					}
				}
			}

			// Pass 2, work out everything else

			// Totals for voyage plan
			final long[] fuelConsumptions = new long[FuelComponent.values().length];
			final long[] fuelCosts = new long[FuelComponent.values().length];
			int totalRouteCost = 0;

			long lngCommitmentInM3 = 0;
			long endHeelInM3 = 0;

			int baseFuelPricePerMT = 0;
			idx = -1;
			final IDetailsSequenceElement[] detailedSequence = new IDetailsSequenceElement[voyageOrPortOptions.size()];
			for (final IOptionsSequenceElement element : voyageOrPortOptions) {
				++idx;
				if (element instanceof PortOptions) {
					final PortOptions portOptions = (PortOptions) element;

					final PortDetails portDetails = new PortDetails();
					portDetails.setOptions(portOptions);

					if (actualsDataProvider.hasActuals(portOptions.getPortSlot())) {
						portDetails.setPortCosts(actualsDataProvider.getPortCosts(portOptions.getPortSlot()));
					}

					if (idx == 0) {
						if (actualsDataProvider.hasActuals(portOptions.getPortSlot())) {

							// This should not be required in future as preceeding voyages should also be actualised!
							assert startHeelVolumeInM3 == actualsDataProvider.getStartHeelInM3(portOptions.getPortSlot());
							// plan.setStartingHeelInM3(startHeelVolumeInM3);

							baseFuelPricePerMT = actualsDataProvider.getBaseFuelPricePerMT(portOptions.getPortSlot());

						}
					}
					portDetails.setFuelUnitPrice(FuelComponent.Base, baseFuelPricePerMT);
					portDetails.setFuelUnitPrice(FuelComponent.IdleBase, baseFuelPricePerMT);
					portDetails.setFuelUnitPrice(FuelComponent.Base_Supplemental, baseFuelPricePerMT);
					portDetails.setFuelUnitPrice(FuelComponent.PilotLight, baseFuelPricePerMT);
					portDetails.setFuelUnitPrice(FuelComponent.IdlePilotLight, baseFuelPricePerMT);

					// Port Fuel Consumption
					if (idx < voyageOrPortOptions.size() - 1) {
						final long baseFuelConsumptionInMt = actualsDataProvider.getPortBaseFuelConsumptionInMT(portOptions.getPortSlot());
						fuelConsumptions[FuelComponent.Base.ordinal()] += baseFuelConsumptionInMt;
						fuelCosts[FuelComponent.Base.ordinal()] += Calculator.costFromConsumption(baseFuelConsumptionInMt, baseFuelPricePerMT);
						portDetails.setFuelConsumption(FuelComponent.Base, baseFuelConsumptionInMt);
					}
					detailedSequence[idx] = portDetails;
				} else if (element instanceof VoyageOptions) {
					final VoyageOptions voyageOptions = (VoyageOptions) element;

					final VoyageDetails voyageDetails = new VoyageDetails();
					voyageDetails.setOptions(voyageOptions);

					// No distinction between travel and idle
					voyageDetails.setTravelTime(voyageOptions.getAvailableTime());
					voyageDetails.setIdleTime(0);
					// Not known
					voyageDetails.setSpeed(10);

					// Base Fuel

					voyageDetails.setFuelUnitPrice(FuelComponent.Base, baseFuelPricePerMT);
					voyageDetails.setFuelUnitPrice(FuelComponent.IdleBase, baseFuelPricePerMT);
					voyageDetails.setFuelUnitPrice(FuelComponent.Base_Supplemental, baseFuelPricePerMT);
					voyageDetails.setFuelUnitPrice(FuelComponent.PilotLight, baseFuelPricePerMT);
					voyageDetails.setFuelUnitPrice(FuelComponent.IdlePilotLight, baseFuelPricePerMT);

					final long baseFuelConsumptionInMt = actualsDataProvider.getNextVoyageBaseFuelConsumptionInMT(voyageOptions.getFromPortSlot());
					voyageDetails.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, baseFuelConsumptionInMt);

					fuelConsumptions[FuelComponent.Base.ordinal()] += baseFuelConsumptionInMt;
					fuelCosts[FuelComponent.Base.ordinal()] += Calculator.costFromConsumption(baseFuelConsumptionInMt, baseFuelPricePerMT);

					// LNG
					long lngInM3;
					if (voyageOptions.getVesselState() == VesselState.Laden) {
						// Volume after loading
						lngInM3 = actualsDataProvider.getStartHeelInM3(voyageOptions.getFromPortSlot()) + actualsDataProvider.getVolumeInM3(voyageOptions.getFromPortSlot());
						// Volume Left after discharge. This leave BOG + remaining heel
						lngInM3 -= actualsDataProvider.getVolumeInM3(voyageOptions.getToPortSlot());

						// Take off heel left at end of discharge. This is now our laden BOG quantity;
						lngInM3 -= actualsDataProvider.getEndHeelInM3(voyageOptions.getToPortSlot());

						voyageDetails.setFuelUnitPrice(FuelComponent.NBO, lngSalesPricePerMMBTu);

						voyageDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, lngInM3);

						final HeelLevelAnnotation heelLevelAnnotation = new HeelLevelAnnotation(actualsDataProvider.getStartHeelInM3(voyageOptions.getFromPortSlot()),
								actualsDataProvider.getStartHeelInM3(voyageOptions.getFromPortSlot()) + actualsDataProvider.getVolumeInM3(voyageOptions.getFromPortSlot()));
						heelLevelAnnotations.put(voyageOptions.getFromPortSlot(), heelLevelAnnotation);

					} else {
						assert voyageOptions.getVesselState() == VesselState.Ballast;
						// Volume after discharging
						lngInM3 = actualsDataProvider.getEndHeelInM3(voyageOptions.getFromPortSlot());

						if (actualsDataProvider.hasReturnActuals(voyageOptions.getFromPortSlot())) {
							// Take off heel left at start of next event.
							endHeelInM3 = actualsDataProvider.getReturnHeelInM3(voyageOptions.getFromPortSlot());
						} else if (actualsDataProvider.hasActuals(voyageOptions.getToPortSlot())) {
							// Take off heel left at start of next load.
							endHeelInM3 = actualsDataProvider.getStartHeelInM3(voyageOptions.getToPortSlot());
						} else if (actualsDataProvider.hasActuals(voyageOptions.getFromPortSlot())) {
							final long heelAfterDischarge = actualsDataProvider.getEndHeelInM3(voyageOptions.getFromPortSlot());
							// end heel cannot be larger than discharge heel
							endHeelInM3 = Math.min(heelAfterDischarge, vesselAvailability.getVessel().getVesselClass().getSafetyHeel());
						} else {
							// Assume we arrive with safety heel at next destination.
							endHeelInM3 = vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
						}
						// Take of end heel, this is now our laden BOG quantity;
						lngInM3 -= endHeelInM3;
						voyageDetails.setFuelUnitPrice(FuelComponent.NBO, lngSalesPricePerMMBTu);
						voyageDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, lngInM3);

						final HeelLevelAnnotation heelLevelAnnotation = new HeelLevelAnnotation(actualsDataProvider.getEndHeelInM3(voyageOptions.getFromPortSlot())
								+ actualsDataProvider.getVolumeInM3(voyageOptions.getFromPortSlot()), actualsDataProvider.getEndHeelInM3(voyageOptions.getFromPortSlot()));
						heelLevelAnnotations.put(voyageOptions.getFromPortSlot(), heelLevelAnnotation);

					}

					final long consumptionInMMBTu = Calculator.convertM3ToMMBTu(lngInM3, cargoCV);
					voyageDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu, consumptionInMMBTu);

					fuelConsumptions[FuelComponent.NBO.ordinal()] += lngInM3;
					fuelCosts[FuelComponent.NBO.ordinal()] += Calculator.costFromConsumption(consumptionInMMBTu, lngSalesPricePerMMBTu);
					lngCommitmentInM3 += lngInM3;

					// Consumption rolled into normal fuel consumption
					// Route costs
					voyageOptions.setRoute(actualsDataProvider.getNextVoyageRoute(voyageOptions.getFromPortSlot()));

					final long routeCosts = actualsDataProvider.getNextVoyageRouteCosts(voyageOptions.getFromPortSlot());
					// voyageDetails.setRouteAdditionalConsumption(fuel, fuelUnit, consumption);
					voyageDetails.setRouteCost(routeCosts);
					totalRouteCost += routeCosts;

					voyageOptions.setDistance(actualsDataProvider.getNextVoyageDistance(voyageOptions.getFromPortSlot()));

					detailedSequence[idx] = voyageDetails;
				}
			}

			// Store results in plan
			plan.setRemainingHeelInM3(endHeelInM3);
			plan.setSequence(detailedSequence);

			plan.setLNGFuelVolume(lngCommitmentInM3);

			// Set the totals
			for (final FuelComponent fc : FuelComponent.values()) {
				plan.setFuelConsumption(fc, fuelConsumptions[fc.ordinal()]);
				plan.setTotalFuelCost(fc, fuelCosts[fc.ordinal()]);
			}

			plan.setTotalRouteCost(totalRouteCost);

		}

		voyagePlansList.add(plan);

		final AllocationRecord allocationRecord = volumeAllocator.createAllocationRecord(vesselAvailability, vesselStartTime, plan, portTimesRecord);
		allocationRecord.allocationMode = AllocationMode.Actuals;
		final IAllocationAnnotation allocationAnnotation = volumeAllocator.allocate(allocationRecord);

		// Sanity check
		assert plan.getRemainingHeelInM3() == allocationAnnotation.getRemainingHeelVolumeInM3();

		voyagePlansMap.add(new Triple<>(plan, heelLevelAnnotations, allocationAnnotation));

		return plan.getRemainingHeelInM3();
	}

	// TODO: Better naming?
	private long evaluateVoyagePlan(final IVesselAvailability vesselAvailability, final int vesselStartTime,
			final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IAllocationAnnotation>> voyagePlansMap, final List<VoyagePlan> voyagePlansList, final IPortTimesRecord portTimesRecord,
			final long startHeelVolumeInM3, final VoyagePlan originalPlan) {

		// Take a copy so we can retain isIgnoreEnd flag later on
		VoyagePlan plan = originalPlan;
		assert startHeelVolumeInM3 >= 0;
		// TODO: Handle LNG at end of charter out
		long endHeelVolumeInM3 = 0;
		boolean planSet = false;
		IAllocationAnnotation allocationAnnotation = null;
		if (generatedCharterOutEvaluator != null) {
			final Pair<VoyagePlan, IAllocationAnnotation> p = generatedCharterOutEvaluator.processSchedule(vesselStartTime, vesselAvailability, plan, portTimesRecord);
			if (p != null) {
				plan = p.getFirst();
				voyagePlansList.add(p.getFirst());
				allocationAnnotation = p.getSecond();

				if (allocationAnnotation != null) {
					endHeelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
				} else {
					endHeelVolumeInM3 = plan.getRemainingHeelInM3();
				}
				planSet = true;
			}
		}

		// FIXME: This should be more customisable

		// Execute custom logic to manipulate the schedule and choices
		if (breakEvenEvaluator != null) {
			final Pair<VoyagePlan, IAllocationAnnotation> p = breakEvenEvaluator.processSchedule(vesselStartTime, vesselAvailability, plan, portTimesRecord);
			if (p != null) {
				plan = p.getFirst();
				voyagePlansList.add(p.getFirst());
				allocationAnnotation = p.getSecond();

				if (allocationAnnotation != null) {
					endHeelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
				} else {
					endHeelVolumeInM3 = plan.getRemainingHeelInM3();
				}
				planSet = true;
			}
		}

		if (!planSet) {
			voyagePlansList.add(plan);
			// TODO: Non-cargo cases?
			allocationAnnotation = volumeAllocator.allocate(vesselAvailability, vesselStartTime, plan, portTimesRecord);
			if (allocationAnnotation == null) {
				// not a cargo plan?
				endHeelVolumeInM3 = plan.getRemainingHeelInM3();
			} else {
				endHeelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
			}
		}

		assert endHeelVolumeInM3 >= 0;

		// Generate heel level annotations
		final Map<IPortSlot, IHeelLevelAnnotation> heelLevelAnnotations = new HashMap<IPortSlot, IHeelLevelAnnotation>();
		{
			final IDetailsSequenceElement[] sequence = plan.getSequence();
			long currentHeelInM3 = startHeelVolumeInM3;
			long totalVoyageBOG = 0;
			int voyageTime = 0;
			IPortSlot optionalHeelUsePortSlot = null;
			final int adjust = plan.isIgnoreEnd() ? 1 : 0;
			for (int i = 0; i < sequence.length - adjust; ++i) {
				final IDetailsSequenceElement e = sequence[i];
				if (e instanceof PortDetails) {
					final PortDetails portDetails = (PortDetails) e;
					final IPortSlot portSlot = portDetails.getOptions().getPortSlot();
					final long start = currentHeelInM3;
					if (portSlot.getPortType() != PortType.End) {
						optionalHeelUsePortSlot = null;
						if (allocationAnnotation != null) {
							if (portSlot.getPortType() == PortType.Load) {
								currentHeelInM3 += allocationAnnotation.getSlotVolumeInM3(portSlot);
							} else if (portSlot.getPortType() == PortType.Discharge) {
								currentHeelInM3 -= allocationAnnotation.getSlotVolumeInM3(portSlot);
							}
						} else {
							if (portSlot instanceof IHeelOptionsPortSlot) {
								optionalHeelUsePortSlot = portSlot;
								// FIXME: This volume is optional use
								final IHeelOptionsPortSlot heelOptionsPortSlot = (IHeelOptionsPortSlot) portSlot;
								currentHeelInM3 = heelOptionsPortSlot.getHeelOptions().getHeelLimit();
							} else {
								currentHeelInM3 = 0;
							}

						}
						assert currentHeelInM3 >= 0;

					} else {
						if (portSlot instanceof EndPortSlot) {
							final EndPortSlot endPortSlot = (EndPortSlot) portSlot;
							// Assert disabled as it is not always possible to arrive with target heel (thus capacity violation should be triggered)
							// assert currentHeelInM3 >= endPortSlot.getTargetEndHeelInM3();
						}
					}
					final long end = currentHeelInM3;

					final HeelLevelAnnotation heelLevelAnnotation = new HeelLevelAnnotation(start, end);
					heelLevelAnnotations.put(portSlot, heelLevelAnnotation);
				} else if (e instanceof VoyageDetails) {
					final VoyageDetails voyageDetails = (VoyageDetails) e;
					long voyageBOGInM3 = 0;
					for (final FuelComponent fuel : FuelComponent.getLNGFuelComponents()) {
						voyageBOGInM3 += voyageDetails.getFuelConsumption(fuel, FuelUnit.M3);
						voyageBOGInM3 += voyageDetails.getRouteAdditionalConsumption(fuel, FuelUnit.M3);
					}
					totalVoyageBOG += voyageBOGInM3;
					currentHeelInM3 -= voyageBOGInM3;
					assert currentHeelInM3 >= 0;
					voyageTime += voyageDetails.getTravelTime();
					voyageTime += voyageDetails.getIdleTime();

				}
			}
			// The optional heel use port slot has heel on board which may or may not have been used.
			// The default code path assumes it has been used. However, if there is no NBO at all, we assume it did not exist,
			// thus we need to update the data to accommodate.
			if (optionalHeelUsePortSlot != null && voyageTime > 0 && totalVoyageBOG == 0) {
				// Replace heel level annotation
				heelLevelAnnotations.put(optionalHeelUsePortSlot, new HeelLevelAnnotation(0, 0));
				// Update current heel - this will still be the start heel value as there was no boil-off
				currentHeelInM3 = 0;
			}
			assert currentHeelInM3 >= 0;

			// Sanity check these calculations match expected values
			assert totalVoyageBOG == plan.getLNGFuelVolume();
			assert endHeelVolumeInM3 == currentHeelInM3;
		}

		// Ensure this flag is copied across!
		plan.setIgnoreEnd(originalPlan.isIgnoreEnd());
		voyagePlansMap.add(new Triple<>(plan, heelLevelAnnotations, allocationAnnotation));

		return endHeelVolumeInM3;

	}

	/**
	 * Returns a voyage plan based on the given sequence
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	final public VoyagePlan makeVoyage(final IResource resource, final List<ISequenceElement> sequenceElements, final int vesselCharterInRatePerDay, final IPortTimesRecord portTimesRecord,
			long heelVolumeInM3) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final boolean isShortsSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final List<IOptionsSequenceElement> voyageOrPortOptions = new ArrayList<IOptionsSequenceElement>(5);
		final List<Integer> currentTimes = new ArrayList<Integer>(3);
		final VesselState[] states = findVesselStates(sequenceElements);

		ISequenceElement prevElement = null;
		IPort prevPort = null;
		IPort prev2Port = null;

		VoyageOptions previousOptions = null;
		boolean useNBO = false;

		int prevVisitDuration = 0;
		int prevSlotTime = 0;
		IPortSlot prevPortSlot = null;
		for (int idx = 0; idx < sequenceElements.size(); ++idx) {
			final ISequenceElement element = sequenceElements.get(idx);

			final IPort thisPort = portProvider.getPortForElement(element);
			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			final PortType portType = portTypeProvider.getPortType(element);
			final int thisSlotTime = portTimesRecord.getSlotTime(thisPortSlot);

			// If we are a heel options slots (i.e. Start or other vessel event slot, overwrite previous heel (assume lost) and replace with a new heel value
			if (thisPortSlot instanceof IHeelOptionsPortSlot) {
				heelVolumeInM3 = ((IHeelOptionsPortSlot) thisPortSlot).getHeelOptions().getHeelLimit();
			}

			// If this is the first port, then this will be null and there will
			// be no voyage to plan.
			int shortCargoReturnArrivalTime = 0;
			if (prevPort != null) {
				final int availableTime;
				final boolean isShortCargoEnd = isShortsSequence && portType == PortType.Short_Cargo_End;

				if (!isShortCargoEnd) {
					// Available time, as determined by inputs.
					availableTime = portTimesRecord.getSlotTime(thisPortSlot) - portTimesRecord.getSlotTime(prevPortSlot) - prevVisitDuration;
				} else { // shorts cargo end on shorts sequence
					int minTravelTime = Integer.MAX_VALUE;
					for (final MatrixEntry<IPort, Integer> entry : distanceProvider.getValues(prevPort, prev2Port)) {
						final int distance = entry.getValue();
						final int extraTime = routeCostProvider.getRouteTransitTime(entry.getKey(), vesselAvailability.getVessel().getVesselClass());
						final int minByRoute = Calculator.getTimeFromSpeedDistance(vesselAvailability.getVessel().getVesselClass().getMaxSpeed(), distance) + extraTime;
						minTravelTime = Math.min(minTravelTime, minByRoute);
					}
					availableTime = minTravelTime;

					shortCargoReturnArrivalTime = prevSlotTime + prevVisitDuration + availableTime;
				}

				final VoyageOptions options = getVoyageOptionsAndSetVpoChoices(vesselAvailability, states[idx], availableTime, element, prevElement, previousOptions, voyagePlanOptimiser, useNBO);
				useNBO = options.useNBOForTravel();

				voyageOrPortOptions.add(options);
				previousOptions = options;
			}

			final int visitDuration = portTimesRecord.getSlotDuration(thisPortSlot);
			assert visitDuration == durationsProvider.getElementDuration(element, resource);

			final PortOptions portOptions = new PortOptions();
			portOptions.setVisitDuration(visitDuration);
			portOptions.setPortSlot(thisPortSlot);
			portOptions.setVessel(vesselAvailability.getVessel());

			if (isShortsSequence && portType == PortType.Short_Cargo_End) {
				currentTimes.add(shortCargoReturnArrivalTime);
			} else {
				currentTimes.add(thisSlotTime);
			}
			voyageOrPortOptions.add(portOptions);

			// Setup for next iteration
			prev2Port = prevPort;

			prevPort = thisPort;
			prevVisitDuration = visitDuration;
			prevSlotTime = thisSlotTime;
			prevPortSlot = thisPortSlot;
			prevElement = element;
		}

		// TODO: Do we need to run optimiser when we only have a load port here?

		// Populate final plan details
		if (voyageOrPortOptions.size() > 1) {
			setVesselAndBaseFuelPrice(voyagePlanOptimiser, portTimesRecord, vesselAvailability.getVessel(), resource);
			return getOptimisedVoyagePlan(voyageOrPortOptions, portTimesRecord, voyagePlanOptimiser, heelVolumeInM3, vesselCharterInRatePerDay);
		}
		return null;
	}

	final private void setVesselAndBaseFuelPrice(IVoyagePlanOptimiser voyagePlanOptimiser, IPortTimesRecord portTimesRecord, IVessel vessel, IResource resource) {
		voyagePlanOptimiser.setVessel(vessel, resource, vesselBaseFuelCalculator.getBaseFuelPrice(vessel, portTimesRecord));
		if (portTimesRecord.getFirstSlot() instanceof ILoadOption) {
			if (actualsDataProvider.hasActuals(portTimesRecord.getFirstSlot())) {
				voyagePlanOptimiser.setVessel(vessel, resource, actualsDataProvider.getBaseFuelPricePerMT(portTimesRecord.getFirstSlot()));
			}
		}
	}
	
	/**
	 * Returns a VoyagePlan produced by the optimiser from a cargo itinerary.
	 * 
	 * @param voyageOrPortOptionsSubsequence
	 *            An alternating list of PortOptions and VoyageOptions objects
	 * @param arrivalTimes
	 * @param optimiser
	 * @param startHeelVolumeInM3
	 * @return An optimised VoyagePlan
	 */
	final public VoyagePlan getOptimisedVoyagePlan(final List<IOptionsSequenceElement> voyageOrPortOptionsSubsequence, final IPortTimesRecord portTimesRecord, final IVoyagePlanOptimiser optimiser,
			final long startHeelVolumeInM3, final int vesselCharterInRatePerDay) {
		// Run sequencer evaluation
		optimiser.setVesselCharterInRatePerDay(vesselCharterInRatePerDay);
		optimiser.setBasicSequence(voyageOrPortOptionsSubsequence);
		optimiser.setPortTimesRecord(portTimesRecord);
		optimiser.setStartHeel(startHeelVolumeInM3);
		optimiser.init();
		final VoyagePlan result = optimiser.optimise();
		if (result == null) {
			return null;
		}

		final Object[] sequence = result.getSequence();
		for (int i = 0; i < sequence.length; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				final int availableTime = details.getOptions().getAvailableTime();

				// Take voyage details time as this can be larger than
				// available time e.g. due to reaching max speed.
				final int duration = details.getTravelTime() + details.getIdleTime();

				// assert duration >= (availableTime - 1) :
				// "Duration should exceed available time less one, but is "
				// + duration + " vs " + availableTime; // hack
				if (duration > availableTime) {
					// TODO: replace by throwing an exception, since if any one subsequence is infeasible, the whole sequence is infeasible
					return null;
				}
			}
		}

		// Reset VPO ready for next iteration
		optimiser.reset();

		// Fix up final arrival time. The VPO is permitted to change the final arrival time of certain vessels and we need to alter the arrival time array and the portTimesRecord with the new
		// arrival time.
		{
			final IDetailsSequenceElement[] vpSequence = result.getSequence();
			final VoyageDetails lastVoyage = (VoyageDetails) vpSequence[vpSequence.length - 2];
			if (lastVoyage.getOptions().getToPortSlot().getPortType() == PortType.End) {
				// New arrival time = Previous element arrival time + visit duration + travel time + idle time.

				final IPortSlot fromSlot = lastVoyage.getOptions().getFromPortSlot();
				final int newTime = portTimesRecord.getSlotTime(fromSlot) + portTimesRecord.getSlotDuration(fromSlot) + lastVoyage.getTravelTime() + lastVoyage.getIdleTime();
				portTimesRecord.setSlotTime(portTimesRecord.getReturnSlot(), newTime);

			}
		}

		return result;

	}

	/**
	 * Returns an array of boolean values indicating whether, for each index of the vessel location sequence, a sequence break occurs at that location (separating one cargo from the next one).
	 * 
	 * @param sequence
	 * @return
	 */
	private boolean[] findSequenceBreaks(final ISequence sequence) {
		final boolean[] result = new boolean[sequence.size()];

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			final PortType portType = portTypeProvider.getPortType(element);
			switch (portType) {
			case Load:
				result[idx] = (idx > 0); // don't break on first load port
				break;
			case CharterOut:
			case DryDock:
			case Other:
			case Maintenance:
			case Short_Cargo_End:
				result[idx] = true;
				break;
			default:
				result[idx] = false;
				break;
			}
			idx++;
		}

		return result;
	}

	/**
	 * Returns an array of vessel states determining, for each index of the vessel location sequence, whether the vessel arrives at that location with LNG cargo on board for resale or not
	 * 
	 * @param sequence
	 * @return
	 */
	private VesselState[] findVesselStates(final ISequence sequence) {
		final VesselState[] result = new VesselState[sequence.size()];

		VesselState state = VesselState.Ballast;
		int possiblePartialDischargeIndex = -1;

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			result[idx] = state;
			// Determine vessel state changes from this location
			switch (portTypeProvider.getPortType(element)) {
			case Load:
				state = VesselState.Laden;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			case Discharge:
				// we'll assume this is a full discharge
				state = VesselState.Ballast;
				// but the last discharge which might have been partial *was* a partial discharge
				if (possiblePartialDischargeIndex > -1) {
					result[possiblePartialDischargeIndex + 1] = VesselState.Laden;
				}
				// and this one might be too
				possiblePartialDischargeIndex = idx;
				break;
			case CharterOut:
			case DryDock:
			case Maintenance:
			case Short_Cargo_End:
				state = VesselState.Ballast;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			default:
				break;
			}

			idx++;
		}
		return result;
	}

	/**
	 * Returns an array of vessel states determining, for each index of the vessel location sequence, whether the vessel arrives at that location with LNG cargo on board for resale or not
	 * 
	 * @param sequence
	 * @return
	 */
	private VesselState[] findVesselStates(final List<ISequenceElement> elements) {
		final VesselState[] result = new VesselState[elements.size()];

		VesselState state = VesselState.Ballast;
		int possiblePartialDischargeIndex = -1;

		int idx = 0;
		for (final ISequenceElement element : elements) {
			result[idx] = state;
			// Determine vessel state changes from this location
			switch (portTypeProvider.getPortType(element)) {
			case Load:
				state = VesselState.Laden;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			case Discharge:
				// we'll assume this is a full discharge
				state = VesselState.Ballast;
				// but the last discharge which might have been partial *was* a partial discharge
				if (possiblePartialDischargeIndex > -1) {
					result[possiblePartialDischargeIndex + 1] = VesselState.Laden;
				}
				// and this one might be too
				possiblePartialDischargeIndex = idx;
				break;
			case CharterOut:
			case DryDock:
			case Maintenance:
			case Short_Cargo_End:
				state = VesselState.Ballast;
				possiblePartialDischargeIndex = -1; // forget about any previous discharge, it was correctly set to a full discharge
				break;
			default:
				break;
			}

			idx++;
		}

		return result;
	}

}
