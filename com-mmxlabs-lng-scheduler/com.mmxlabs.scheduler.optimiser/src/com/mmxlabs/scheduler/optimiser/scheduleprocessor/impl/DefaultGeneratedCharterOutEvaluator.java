/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.FBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IdleNBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.NBOTravelVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider.CharterMarketOptions;
import com.mmxlabs.scheduler.optimiser.providers.IGeneratedCharterOutSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
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
public class DefaultGeneratedCharterOutEvaluator implements IGeneratedCharterOutEvaluator {
	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IVolumeAllocator cargoAllocator;

	@Inject
	private ICharterMarketProvider charterMarketProvider;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IVoyagePlanOptimiser vpo;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IGeneratedCharterOutSlotProviderEditor generatedCharterOutSlotProviderEditor;

	@Inject
	private IVesselProvider vesselProvider;
	
	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	private static final double canalChoiceThreshold = 0.1; //percentage improvement required to choose a canal route
	
	@Override
	public List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(final int vesselStartTime, final long startHeelVolumeInM3, final IVesselAvailability vesselAvailability, final VoyagePlan vp, final IPortTimesRecord portTimesRecord) {
		if (!(vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		final long startHeelInM3 = startHeelVolumeInM3;

		// First step, find a ballast leg which is long enough to charter-out
		final Object[] currentSequence = vp.getSequence();
		int ballastIdx = -1;
		int ballastStartTime = -1;

		for (int idx = 0; idx < currentSequence.length; ++idx) {
			final Object obj = currentSequence[idx];
			if (obj instanceof PortDetails) {
				// Pass
			} else if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				// record last ballast leg
				if (details.getOptions().getVesselState() == VesselState.Ballast) {
					ballastIdx = idx;
					ballastStartTime = portTimesRecord.getSlotTime(details.getOptions().getFromPortSlot()) + portTimesRecord.getSlotDuration(details.getOptions().getFromPortSlot());
				}
			}
		}

		assert ((ballastIdx < 0) == (ballastStartTime < 0));
		if (ballastIdx == -1 || ballastStartTime == -1) {

			// no ballast leg?
			return null;
		}

		final VoyageDetails ballastDetails = (VoyageDetails) currentSequence[ballastIdx];
		final int availableTime = ballastDetails.getOptions().getAvailableTime();
		
		final GeneratedCharterOutOption gcoMarket = getCharterOutOption(ballastStartTime, availableTime, ballastDetails.getOptions().getFromPortSlot(), ballastDetails.getOptions().getToPortSlot(), vesselAvailability);

		// Have we found a market?
		if (gcoMarket.getOption() == null) {
			return null;
		}

		final ExtendedCharterOutSequence bigSequence = constructNewRawSequenceWithCharterOuts(currentSequence, gcoMarket, portTimesRecord, ballastIdx, ballastStartTime);

		final VoyagePlan bigVoyagePlan = runVPOOnBigSequence(vesselAvailability.getVessel(), vp, startHeelInM3, bigSequence);

		final long remainingHeelInM3 = bigVoyagePlan.getRemainingHeelInM3();

		// now split into two
		final Pair<VoyagePlan, VoyagePlan> vPlans = splitVoyagePlan(bigVoyagePlan);
		final VoyagePlan upToCharterPlan = vPlans.getFirst();
		final VoyagePlan charterToEndPlan = vPlans.getSecond();

		// split the port times records in two...
		final Pair<IPortTimesRecord, IPortTimesRecord> splitPortTimesRecords = splitPortTimesRecord(bigSequence.getPortTimesRecord()); // -----> move up
		final IPortTimesRecord preCharteringTimes = splitPortTimesRecords.getFirst();
		final IPortTimesRecord postCharteringTimes = splitPortTimesRecords.getSecond();

		// set parameters such as heels and ends to ignore
		setPlanParameters(bigVoyagePlan, charterToEndPlan, upToCharterPlan, vp, remainingHeelInM3);
		final long firstPlanRemainingHeel = upToCharterPlan.getRemainingHeelInM3();
		// calculate pre-charter plan
		voyageCalculator.calculateVoyagePlan(upToCharterPlan, vesselAvailability.getVessel(), bigVoyagePlan.getStartingHeelInM3(),
				vesselBaseFuelCalculator.getBaseFuelPrice(vesselAvailability.getVessel(), preCharteringTimes), preCharteringTimes, upToCharterPlan.getSequence());
		// remaining heel may have been overwritten
		upToCharterPlan.setRemainingHeelInM3(firstPlanRemainingHeel);
		
		final IAllocationAnnotation preCharterAllocation = cargoAllocator.allocate(vesselAvailability, vesselStartTime, upToCharterPlan, preCharteringTimes);

		// add on delta to starting heel and remaining heel
		if (preCharterAllocation != null) {
			final long delta = preCharterAllocation.getRemainingHeelVolumeInM3() - charterToEndPlan.getStartingHeelInM3();
			charterToEndPlan.setStartingHeelInM3(charterToEndPlan.getStartingHeelInM3() + delta);
			charterToEndPlan.setRemainingHeelInM3(charterToEndPlan.getRemainingHeelInM3() + delta);
			// now we set the heel options on the charter out event as it will be need when the second voyage plan is calculated
			final IGeneratedCharterOutVesselEventPortSlot charter = (IGeneratedCharterOutVesselEventPortSlot) ((PortDetails) charterToEndPlan.getSequence()[0]).getOptions().getPortSlot();
			setHeelOptions(charter.getVesselEvent(), preCharteringTimes, upToCharterPlan.getSequence(), charterToEndPlan.getStartingHeelInM3());
		}
		
		final long secondPlanRemainingHeel = charterToEndPlan.getRemainingHeelInM3();
		// calculate post-charter plan
		voyageCalculator.calculateVoyagePlan(charterToEndPlan, vesselAvailability.getVessel(), charterToEndPlan.getStartingHeelInM3(),
				vesselBaseFuelCalculator.getBaseFuelPrice(vesselAvailability.getVessel(), postCharteringTimes), postCharteringTimes, charterToEndPlan.getSequence());
		// remaining heel may have been overwritten
		charterToEndPlan.setRemainingHeelInM3(secondPlanRemainingHeel);
		
		final List<Pair<VoyagePlan, IPortTimesRecord>> charterPlans = new LinkedList<Pair<VoyagePlan, IPortTimesRecord>>();
		charterPlans.add(new Pair<VoyagePlan, IPortTimesRecord>(upToCharterPlan, preCharterAllocation != null ? preCharterAllocation : preCharteringTimes));
		charterPlans.add(new Pair<VoyagePlan, IPortTimesRecord>(charterToEndPlan, postCharteringTimes));

		return charterPlans;
	}

	private GeneratedCharterOutOption getCharterOutOption(final int ballastStartTime, final int availableTime, final IPortSlot dischargeSlot, final IPortSlot nextLoadSlot, final IVesselAvailability vesselAvailability) {
		final GeneratedCharterOutOption gcoo = new GeneratedCharterOutOption();
		final IPort discharge = dischargeSlot.getPort();
		final IPort nextLoad = nextLoadSlot.getPort();
		gcoo.setMaxCharteringRevenue(-1);
		
		// set end ports
		boolean goingToEnd = false;
		final Set<IPort> endPorts = new HashSet<IPort>();
		if (nextLoadSlot instanceof EndPortSlot) {
			goingToEnd = true;
			endPorts.add(nextLoad);
		}
		// Scan all the markets for a match
		for (final CharterMarketOptions option : charterMarketProvider.getCharterOutOptions(vesselAvailability.getVessel().getVesselClass(), ballastStartTime)) {
			Set<IPort> ports = option.getAllowedPorts();
			if (ports.isEmpty()) {
				// If no ports, charter out at next load port (this is primarily for ITS cases)
				ports = new HashSet<IPort>();
				ports.add(nextLoad);
			} else {
				// end slot exceptions
				if (goingToEnd) {
					if (ports.contains(nextLoad)) {
						// only going to charter at the end port
						ports = endPorts;
					} else {
						// end port not in this option so move on to the next
						continue;
					}
				}
			}
			for (final IPort charterOutPort : ports) {
				final Triple<Integer, String, Integer> toCharterPort = calculateShortestTimeToPort(discharge, charterOutPort, vesselAvailability.getVessel().getVesselClass());
				final Triple<Integer, String, Integer> fromCharterPort = calculateShortestTimeToPort(charterOutPort, nextLoad, vesselAvailability.getVessel().getVesselClass());
				final int availableCharteringTime = availableTime - toCharterPort.getThird() - fromCharterPort.getThird();
				final int charterStartTime = ballastStartTime + toCharterPort.getThird();
				final long dailyPrice = (long) option.getCharterPrice(charterStartTime);
				if (charterStartTime < charterMarketProvider.getCharterOutStartTime()) {
					continue;
				}
				final long charteringRevenue = Calculator.quantityFromRateTime(dailyPrice, availableCharteringTime) / 24L;
				if (availableCharteringTime >= option.getMinDuration() && charteringRevenue > gcoo.getMaxCharteringRevenue()) {
					gcoo.setToCharterPort(toCharterPort);
					gcoo.setFromCharterPort(fromCharterPort);
					gcoo.setOption(option);
					gcoo.setCharterDuration(availableCharteringTime);
					gcoo.setPort(charterOutPort);
					gcoo.setMaxCharteringRevenue(charteringRevenue);
					gcoo.setCharterStartTime(charterStartTime);
				}
			}
		}
		return gcoo;
	}

	private Triple<Integer, String, Integer> calculateShortestTimeToPort(final IPort slotPort, final IPort charterPort, final IVesselClass vesselClass) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		String route = "";

		final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(slotPort, charterPort));
		int directTime = Integer.MAX_VALUE;
		MatrixEntry<IPort, Integer> directEntry = null;
		for (final MatrixEntry<IPort, Integer> d : distances) {
			final int travelTime = Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), d.getValue()) + routeCostProvider.getRouteTransitTime(d.getKey(), vesselClass);
			if (d.getKey().equals("Direct")) {
				directTime = travelTime;
				directEntry = d;
			}
			if (travelTime < shortestTime) {
				distance = d.getValue();
				route = d.getKey();
				shortestTime = travelTime;
			}
		}
		// heuristic to only use canal if a big improvement
		if (!route.equals("Direct") && directEntry != null) {
			final double improvement = ((double) directTime - (double) shortestTime)/ (double) directTime;
			if (directTime == 0 || improvement < canalChoiceThreshold) {
				distance = directEntry.getValue();
				route = "Direct";
				shortestTime = directTime;
			}
		}
		return new Triple(distance, route, shortestTime);
	}

	private ExtendedCharterOutSequence constructNewRawSequenceWithCharterOuts(final Object[] currentSequence, final GeneratedCharterOutOption charterOutOption, final IPortTimesRecord portTimesRecord, final int ballastIdx, final int ballastStartTime) {
		final List<IOptionsSequenceElement> newRawSequence = new ArrayList<IOptionsSequenceElement>(currentSequence.length);
		final ExtendedCharterOutSequence bigSequence = new ExtendedCharterOutSequence();

		// build new sequence up to and not including last ballast leg
		for (int i = 0; i < ballastIdx; i++) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				newRawSequence.add(((PortDetails) o).getOptions().clone());
			} else if (o instanceof VoyageDetails) {
				newRawSequence.add(((VoyageDetails) o).getOptions().clone());
				if (((VoyageDetails) o).getOptions().getVesselState() == VesselState.Laden) {
					bigSequence.setLaden(((VoyageDetails) o).getOptions().clone());
				}
			}
		}

		// Now insert elements from the charter out option
		final VoyageDetails originalBallast = (VoyageDetails) currentSequence[ballastIdx];
		final IPortSlot existingSlotUsedToGenerateCharterOut = originalBallast.getOptions().getFromPortSlot();

		final IGeneratedCharterOutVesselEventPortSlot charterOutPortSlot = generatedCharterOutSlotProviderEditor.getPortSlotGeneratedByPortSlot(existingSlotUsedToGenerateCharterOut);
		assert (charterOutOption != null);

		// copy port times record
		final Triple<IPortSlot, Integer, Integer> charterOutTimesRecord = new Triple<IPortSlot, Integer, Integer>(charterOutPortSlot, charterOutOption.getCharterStartTime(), charterOutOption.getCharterDuration());
		final IPortTimesRecord bigPlanPortTimesRecord = createPortTimesRecordForExtendedPlan(portTimesRecord, charterOutTimesRecord);

		// (1) ballast to charter out

		final VoyageOptions dischargeToCharterPortVoyageOptions = new VoyageOptions();
		dischargeToCharterPortVoyageOptions.setDistance(charterOutOption.getToCharterPort().getFirst());
		dischargeToCharterPortVoyageOptions.setRoute(charterOutOption.getToCharterPort().getSecond());
		dischargeToCharterPortVoyageOptions.setAvailableTime(charterOutOption.getToCharterPort().getThird());
		dischargeToCharterPortVoyageOptions.setVesselState(VesselState.Ballast);
		dischargeToCharterPortVoyageOptions.setVessel(originalBallast.getOptions().getVessel());
		dischargeToCharterPortVoyageOptions.setFromPortSlot(originalBallast.getOptions().getFromPortSlot());
		dischargeToCharterPortVoyageOptions.setToPortSlot(charterOutPortSlot);
		dischargeToCharterPortVoyageOptions.setShouldBeCold(originalBallast.getOptions().shouldBeCold());
		dischargeToCharterPortVoyageOptions.setWarm(originalBallast.getOptions().isWarm());
		dischargeToCharterPortVoyageOptions.setAllowCooldown(false);
		dischargeToCharterPortVoyageOptions.setCargoCVValue(originalBallast.getOptions().getCargoCVValue());

		// (2) charter out

		final PortOptions generatedCharterPortOptions = new PortOptions();
		generatedCharterPortOptions.setVessel(originalBallast.getOptions().getVessel());
		generatedCharterPortOptions.setVisitDuration(charterOutOption.getCharterDuration());
		// now update port slot
		charterOutPortSlot.setId(String.format("gco-%s-%s", originalBallast.getOptions().getFromPortSlot().getPort(), originalBallast.getOptions().getToPortSlot().getPort()));
		charterOutPortSlot.setPort(charterOutOption.getPort());

		final IGeneratedCharterOutVesselEvent charterOutEvent = charterOutPortSlot.getVesselEvent();
		charterOutEvent.setStartPort(charterOutOption.getPort());
		charterOutEvent.setEndPort(charterOutOption.getPort());
		charterOutEvent.setHireOutRevenue(charterOutOption.getMaxCharteringRevenue());
		charterOutEvent.setRepositioning(0);
		charterOutEvent.setDurationHours(charterOutOption.getCharterDuration());

		generatedCharterPortOptions.setPortSlot(charterOutPortSlot);

		// (3) ballast to return port
		
		final VoyageOptions charterToReturnPortVoyageOptions = new VoyageOptions();
		charterToReturnPortVoyageOptions.setDistance(charterOutOption.getFromCharterPort().getFirst());
		charterToReturnPortVoyageOptions.setRoute(charterOutOption.getFromCharterPort().getSecond());
		charterToReturnPortVoyageOptions.setAvailableTime(charterOutOption.getFromCharterPort().getThird());
		charterToReturnPortVoyageOptions.setVesselState(VesselState.Ballast);
		charterToReturnPortVoyageOptions.setVessel(originalBallast.getOptions().getVessel());
		charterToReturnPortVoyageOptions.setFromPortSlot(charterOutPortSlot);
		charterToReturnPortVoyageOptions.setToPortSlot(originalBallast.getOptions().getToPortSlot());
		charterToReturnPortVoyageOptions.setShouldBeCold(originalBallast.getOptions().shouldBeCold());
		charterToReturnPortVoyageOptions.setWarm(originalBallast.getOptions().isWarm());
		charterToReturnPortVoyageOptions.setAllowCooldown(originalBallast.getOptions().getAllowCooldown());
		charterToReturnPortVoyageOptions.setCargoCVValue(originalBallast.getOptions().getCargoCVValue());
		
		// add options to new sequence
		newRawSequence.add(dischargeToCharterPortVoyageOptions);
		newRawSequence.add(generatedCharterPortOptions);
		newRawSequence.add(charterToReturnPortVoyageOptions);
		newRawSequence.add(((PortDetails) currentSequence[currentSequence.length - 1]).getOptions().clone());

		// store data in extended sequence data structure
		setExtendedSequence(newRawSequence, bigSequence, bigPlanPortTimesRecord, dischargeToCharterPortVoyageOptions, generatedCharterPortOptions, charterToReturnPortVoyageOptions);

		return bigSequence;
	}

	private void setExtendedSequence(final List<IOptionsSequenceElement> newRawSequence, final ExtendedCharterOutSequence bigSequence, final IPortTimesRecord bigPlanPortTimesRecord,
			final VoyageOptions dischargeToCharterPortVoyageOptions, final PortOptions generatedCharterPortOptions, final VoyageOptions charterToReturnPortVoyageOptions) {
		bigSequence.setToCharter(dischargeToCharterPortVoyageOptions);
		bigSequence.setFromCharter(charterToReturnPortVoyageOptions);
		bigSequence.setCharter(generatedCharterPortOptions);
		bigSequence.setSequence(newRawSequence);
		bigSequence.setPortTimesRecord(bigPlanPortTimesRecord);
	}

	private IPortTimesRecord createPortTimesRecordForExtendedPlan(final IPortTimesRecord existing, final Triple<IPortSlot, Integer, Integer>... slotsToAdd) {
		final PortTimesRecord newPortsTimeRecord = new PortTimesRecord();
		// existing
		for (int i = 0; i < existing.getSlots().size(); i++) {
			newPortsTimeRecord.setSlotTime(existing.getSlots().get(i), existing.getSlotTime(existing.getSlots().get(i)));
			newPortsTimeRecord.setSlotDuration(existing.getSlots().get(i), existing.getSlotDuration(existing.getSlots().get(i)));
		}
		// new
		for (final Triple<IPortSlot, Integer, Integer> slotToAdd : slotsToAdd) {
			newPortsTimeRecord.setSlotTime(slotToAdd.getFirst(), slotToAdd.getSecond());
			newPortsTimeRecord.setSlotDuration(slotToAdd.getFirst(), slotToAdd.getThird());
		}
		// existing return
		final IPortSlot returnSlot = existing.getReturnSlot();
		newPortsTimeRecord.setReturnSlotTime(returnSlot, existing.getSlotTime(returnSlot));
		return newPortsTimeRecord;
	}

	private Pair<IPortTimesRecord, IPortTimesRecord> splitPortTimesRecord(final IPortTimesRecord existing) {
		final PortTimesRecord firstPortsTimeRecord = new PortTimesRecord();
		final PortTimesRecord secondPortsTimeRecord = new PortTimesRecord();
		// first (... D -- CO)
		for (int i = 0; i < existing.getSlots().size() - 1; i++) {
			firstPortsTimeRecord.setSlotTime(existing.getSlots().get(i), existing.getSlotTime(existing.getSlots().get(i)));
			firstPortsTimeRecord.setSlotDuration(existing.getSlots().get(i), existing.getSlotDuration(existing.getSlots().get(i)));
		}
		firstPortsTimeRecord.setReturnSlotTime(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		// second (... CO -- NL)
		secondPortsTimeRecord.setSlotTime(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		secondPortsTimeRecord.setSlotDuration(existing.getSlots().get(existing.getSlots().size() - 1), existing.getSlotTime(existing.getSlots().get(existing.getSlots().size() - 1)));
		secondPortsTimeRecord.setReturnSlotTime(existing.getReturnSlot(), existing.getSlotTime(existing.getReturnSlot()));
		return new Pair<IPortTimesRecord, IPortTimesRecord>(firstPortsTimeRecord, secondPortsTimeRecord);
	}

	private VoyagePlan runVPOOnBigSequence(final IVessel vessel, final VoyagePlan originalVoyagePlan, final long startHeelInM3, final ExtendedCharterOutSequence bigSequence) {
		// We will use the VPO to optimise fuel and route choices
		vpo.reset();

		vpo.setVessel(vessel, null, vesselBaseFuelCalculator.getBaseFuelPrice(vessel, bigSequence.getPortTimesRecord()));
		vpo.setVesselCharterInRatePerDay(originalVoyagePlan.getCharterInRatePerDay());

		// Install our new alternative sequence
		vpo.setBasicSequence(bigSequence.getSequence());
		vpo.setStartHeel(startHeelInM3);

		// Rebuilt the arrival times list
		vpo.setPortTimesRecord(bigSequence.getPortTimesRecord());

		// Add in NBO etc choices (ballast 1)
		vpo.addChoice(new NBOTravelVoyagePlanChoice(bigSequence.getLaden() == null ? null : bigSequence.getLaden(), bigSequence.getToCharter()));
		vpo.addChoice(new FBOVoyagePlanChoice(bigSequence.getToCharter()));
		vpo.addChoice(new IdleNBOVoyagePlanChoice(bigSequence.getToCharter()));

		// Add in NBO etc choices (ballast 2)
		vpo.addChoice(new NBOTravelVoyagePlanChoice(bigSequence.getToCharter(), bigSequence.getFromCharter()));
		vpo.addChoice(new FBOVoyagePlanChoice(bigSequence.getFromCharter()));
		vpo.addChoice(new IdleNBOVoyagePlanChoice(bigSequence.getFromCharter()));

		// Calculate our new plan
		final VoyagePlan newVoyagePlan = vpo.optimise();

		return newVoyagePlan;
	}

	private Pair<IDetailsSequenceElement[], IDetailsSequenceElement[]> splitSequenceDetails(final VoyagePlan vp) {
		final int elementsUpToCO = vp.getSequence().length - 2;
		final IDetailsSequenceElement[] first = new IDetailsSequenceElement[elementsUpToCO];
		final IDetailsSequenceElement[] second = new IDetailsSequenceElement[3];
		// .... up to Ballast -- CO
		for (int i = 0; i < elementsUpToCO; i++) {
			first[i] = vp.getSequence()[i];
		}
		// CO -- Ballast -- Next Load
		for (int i = elementsUpToCO - 1; i < vp.getSequence().length; i++) {
			second[i - (elementsUpToCO - 1)] = vp.getSequence()[i];
		}
		return new Pair<IDetailsSequenceElement[], IDetailsSequenceElement[]>(first, second);
	}

	private Pair<VoyagePlan, VoyagePlan> splitVoyagePlan(final VoyagePlan bigVoyagePlan) {
		// now split into two
		final VoyagePlan upToCharterPlan = new VoyagePlan();
		final VoyagePlan charterToEndPlan = new VoyagePlan();

		final Pair<IDetailsSequenceElement[], IDetailsSequenceElement[]> splitSequenceDetails = splitSequenceDetails(bigVoyagePlan);
		upToCharterPlan.setSequence(splitSequenceDetails.getFirst());
		charterToEndPlan.setSequence(splitSequenceDetails.getSecond());

		return new Pair<VoyagePlan, VoyagePlan>(upToCharterPlan, charterToEndPlan);
	}

	private void setPlanParameters(final VoyagePlan bigVoyagePlan, final VoyagePlan charterToEndPlan, final VoyagePlan upToCharterPlan, final VoyagePlan originalVP, final long remainingHeelInM3) {
		final long totalBoilOffPostCharter = getTotalBoilOffInSequence(charterToEndPlan.getSequence());

		charterToEndPlan.setRemainingHeelInM3(remainingHeelInM3);
		charterToEndPlan.setStartingHeelInM3(remainingHeelInM3 + totalBoilOffPostCharter);

		upToCharterPlan.setStartingHeelInM3(bigVoyagePlan.getStartingHeelInM3());
		upToCharterPlan.setRemainingHeelInM3(remainingHeelInM3 + totalBoilOffPostCharter);

		upToCharterPlan.setCharterInRatePerDay(originalVP.getCharterInRatePerDay());
		charterToEndPlan.setCharterInRatePerDay(originalVP.getCharterInRatePerDay());

		upToCharterPlan.setIgnoreEnd(true);
		charterToEndPlan.setIgnoreEnd(originalVP.isIgnoreEnd());
	}

	private long getTotalBoilOffInSequence(final IDetailsSequenceElement[] sequence) {
		long totalBoilOff = 0;
		for (final IDetailsSequenceElement sequenceElement : sequence) {
			if (sequenceElement instanceof VoyageDetails) {
				totalBoilOff += getTotalBoilOffForVoyage((VoyageDetails) sequenceElement);
			}
		}
		return totalBoilOff;
	}

	private long getTotalBoilOffForVoyage(final VoyageDetails voyageDetails) {
		long totalBoilOffM3 = 0;
		for (final FuelComponent fc : FuelComponent.getLNGFuelComponents()) {
			totalBoilOffM3 += voyageDetails.getFuelConsumption(fc, FuelUnit.M3);
			totalBoilOffM3 += voyageDetails.getRouteAdditionalConsumption(fc, FuelUnit.M3);
		}
		return totalBoilOffM3;
	}

	/**
	 * Loop through the first voyage plan (up to the GCO) and find an element to extract heel options information
	 * 
	 * @param generatedCharterOutVesselEvent
	 * @param sequence
	 * @param heelVolume
	 * @param portTimesRecord
	 * @return
	 */
	private void setHeelOptions(final IGeneratedCharterOutVesselEvent generatedCharterOutVesselEvent, final IPortTimesRecord portTimesRecord, final IDetailsSequenceElement[] sequence, final long heelVolume) {
		int pricePerMBTU = 0;
		int cv = 0;

		final IDetailsSequenceElement startOfSequence = sequence[0];
		if (startOfSequence instanceof PortDetails) {
			final IPortSlot portSlot = ((PortDetails) startOfSequence).getOptions().getPortSlot();
			if (portSlot instanceof IHeelOptionsPortSlot) {
				pricePerMBTU = ((IHeelOptionsPortSlot) portSlot).getHeelOptions().getHeelUnitPrice();
				cv = ((IHeelOptionsPortSlot) portSlot).getHeelOptions().getHeelCVValue();
			} else if (portSlot instanceof ILoadSlot) {
				IDischargeSlot discharge = null;
				for (int i = sequence.length - 1; i >= 0; i--) {
					if (sequence[i] instanceof PortDetails) {
						if (((PortDetails) sequence[i]).getOptions().getPortSlot() instanceof IDischargeSlot) {
							discharge = (IDischargeSlot) ((PortDetails) sequence[i]).getOptions().getPortSlot();
							break;
						}
					}
				}
				if (discharge != null) {
					pricePerMBTU = discharge.getDischargePriceCalculator().estimateSalesUnitPrice(discharge, portTimesRecord, null);
				}
				cv = ((ILoadSlot) portSlot).getCargoCVValue();
			}
		}
		generatedCharterOutVesselEvent.setHeelOptions(pricePerMBTU, cv, heelVolume);
	}
}