/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Provider;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord.AllocationMode;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class VoyagePlanner {

	public static final int ROUNDING_EPSILON = 0;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private Provider<@NonNull IVoyagePlanOptimiser> voyagePlanOptimiserProvider;

	@Inject
	@NonNull
	private IRouteCostProvider routeCostProvider;

	@Inject
	private Provider<IVolumeAllocator> volumeAllocator;

	@com.google.inject.Inject(optional = true)
	private IBreakEvenEvaluator breakEvenEvaluator;

	@com.google.inject.Inject(optional = true)
	private IGeneratedCharterOutEvaluator generatedCharterOutEvaluator;

	@Inject
	private ICharterRateCalculator charterRateCalculator;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

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
	private @NonNull VoyageOptions getVoyageOptionsAndSetVpoChoices(final @NonNull IVesselAvailability vesselAvailability, final @NonNull VesselState vesselState, final int voyageStartTime,
			final int availableTime, final @NonNull IPortSlot prevPortSlot, final @NonNull IPortSlot thisPortSlot, final @Nullable VoyageOptions previousOptions,
			final @NonNull List<@NonNull IVoyagePlanChoice> vpoChoices, boolean useNBO) {

		@NonNull
		final IVessel vessel = vesselAvailability.getVessel();
		final IVesselClass vesselClass = vessel.getVesselClass();

		final IPort thisPort = thisPortSlot.getPort();
		final IPort prevPort = prevPortSlot.getPort();
		final PortType prevPortType = prevPortSlot.getPortType();

		final VoyageOptions options = new VoyageOptions(prevPortSlot, thisPortSlot);
		options.setVessel(vessel);
		options.setVesselState(vesselState);
		options.setAvailableTime(availableTime);

		// Flag to force NBO use over cost choice - e.g. for cases where there is already a heel onboard
		boolean forceNBO = false;

		final boolean isReliq = vesselClass.hasReliqCapability();

		if (prevPortType == PortType.Load) {
			useNBO = true;
			forceNBO = true;
		} else if (prevPortSlot instanceof IHeelOptionSupplierPortSlot) {
			IHeelOptionSupplierPortSlot heelOptionsPortSlot = (IHeelOptionSupplierPortSlot) prevPortSlot;
			useNBO = heelOptionsPortSlot.getHeelOptionsSupplier().getMaximumHeelAvailableInM3() > 0;
//			forceNBO = heelOptionsPortSlot.getHeelOptionsSupplier().getMinimumHeelAvailableInM3() > 0;
		}

		final int cargoCV;
		if (actualsDataProvider.hasActuals(prevPortSlot)) {
			cargoCV = actualsDataProvider.getCVValue(prevPortSlot);
		} else if (prevPortSlot instanceof IHeelOptionSupplierPortSlot) {
			final IHeelOptionSupplierPortSlot heelOptionsSlot = (IHeelOptionSupplierPortSlot) prevPortSlot;
			cargoCV = heelOptionsSlot.getHeelOptionsSupplier().getHeelCVValue();
		} else if (prevPortSlot instanceof ILoadOption) {
			final ILoadOption loadOption = (ILoadOption) prevPortSlot;
			cargoCV = loadOption.getCargoCVValue();
		} else if (prevPortType == PortType.Virtual || prevPortType == PortType.Other) {
			cargoCV = 0;
		} else if (prevPortType == PortType.DryDock || prevPortType == PortType.Maintenance) {
			cargoCV = 0;
		} else {
			assert previousOptions != null;
			cargoCV = previousOptions.getCargoCVValue();
		}

		if ((prevPortType == PortType.DryDock) || (prevPortType == PortType.Maintenance)) {
			options.setWarm(true);
		} else {
			options.setWarm(false);
		}
if (cargoCV == 0) {
	int ii = 0;
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
		// If NBO is enabled for a reliq vessel, then force FBO too
		options.setUseFBOForSupplement(useNBO && isReliq);
		// If not forced, then a choice may be added later
		options.setUseNBOForIdle((useNBO && isReliq) || forceNBO);

		if (thisPortSlot.getPortType() == PortType.Load) {
			options.setShouldBeCold(VesselTankState.MUST_BE_COLD);
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
					vpoChoices.add(new CooldownVoyagePlanChoice(options));
				}
			}
		} else if (thisPortSlot.getPortType() == PortType.Discharge) {
			options.setShouldBeCold(VesselTankState.MUST_BE_COLD);
			options.setAllowCooldown(false);

		} else if (thisPortSlot instanceof IHeelOptionConsumerPortSlot) {
			IHeelOptionConsumerPortSlot heelOptionsPortSlot = (IHeelOptionConsumerPortSlot) thisPortSlot;
			options.setShouldBeCold(heelOptionsPortSlot.getHeelOptionsConsumer().getExpectedTankState());
			options.setAllowCooldown(false);
		} else if (thisPortSlot.getPortType() == PortType.Round_Trip_Cargo_End) {
			options.setShouldBeCold(VesselTankState.MUST_BE_COLD);
			options.setAllowCooldown(false);
		} else if (thisPortSlot.getPortType() == PortType.DryDock || thisPortSlot.getPortType() == PortType.Maintenance) {
			options.setShouldBeCold(VesselTankState.MUST_BE_WARM);
		} else {
			options.setShouldBeCold(VesselTankState.MUST_BE_WARM);
		}

		// Enable choices only if NBO could be available.
		if (useNBO) {
			// Note ordering of choices is important = NBO must be set
			// before FBO and Idle choices, otherwise if NBO choice is
			// false, FBO and IdleNBO may still be true if set before
			// NBO

			if (isReliq) {
				if (vesselState == VesselState.Ballast && !forceNBO) {
					vpoChoices.add(new ReliqVoyagePlanChoice(previousOptions, options));
				}
			} else {
				if (vesselState == VesselState.Ballast && !forceNBO) {
					vpoChoices.add(new NBOTravelVoyagePlanChoice(previousOptions, options));
				}
				vpoChoices.add(new FBOVoyagePlanChoice(options));
				if (!forceNBO) {
					vpoChoices.add(new IdleNBOVoyagePlanChoice(options));
				}
			}
		}

		final List<@NonNull Pair<@NonNull ERouteOption, @NonNull Integer>> distances = distanceProvider.getDistanceValues(prevPort, thisPort, voyageStartTime, vessel);
		if (distances.isEmpty()) {
			throw new RuntimeException(String.format("No distance between %s and %s", prevPort.getName(), thisPort.getName()));
		}
		assert !distances.isEmpty();

		// Only add route choice if there is one

		if (distances.size() == 1) {
			// TODO: Make part of if instead?
			final Pair<@NonNull ERouteOption, @NonNull Integer> d = distances.get(0);
			final CostType costType;
			if (vesselState == VesselState.Laden) {
				costType = CostType.Laden;
			} else if (previousOptions != null && previousOptions.getRoute() == d.getFirst()) {
				costType = CostType.RoundTripBallast;
			} else {
				costType = CostType.Ballast;
			}

			options.setRoute(d.getFirst(), d.getSecond(), routeCostProvider.getRouteCost(d.getFirst(), vessel, voyageStartTime, costType));
		} else {
			vpoChoices.add(new RouteVoyagePlanChoice(previousOptions, options, distances, vessel, voyageStartTime, routeCostProvider));
		}

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER && thisPortSlot.getPortType() == PortType.End) {
			// The SchedulerBuilder should set options to trigger these values to be set above
			assert !options.getAllowCooldown();
			assert options.shouldBeCold() == VesselTankState.MUST_BE_COLD;
			options.setAllowCooldown(false);
			options.setShouldBeCold(VesselTankState.MUST_BE_COLD);
		} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP && thisPortSlot.getPortType() == PortType.Round_Trip_Cargo_End) {
			// The SchedulerBuilder should set options to trigger these values to be set above
			assert !options.getAllowCooldown();
			assert options.shouldBeCold() == VesselTankState.MUST_BE_COLD;
			options.setAllowCooldown(false);
			options.setShouldBeCold(VesselTankState.MUST_BE_COLD);
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
	final public List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> makeVoyagePlans(final @NonNull IResource resource, final @NonNull ISequence sequence,
			final @NonNull List<IPortTimesRecord> portTimesRecords) {

		final IVoyagePlanOptimiser voyagePlanOptimiser = voyagePlanOptimiserProvider.get();
		final List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> voyagePlansMap = new LinkedList<>();
		final List<@NonNull VoyagePlan> voyagePlansList = new LinkedList<>();

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		// Get starting heel for vessel
		final IStartRequirement startRequirement = startEndRequirementProvider.getStartRequirement(resource);
		final long[] heelInM3Range = new long[2];
		heelInM3Range[0] = startRequirement.getHeelOptions().getMinimumHeelAvailableInM3();
		heelInM3Range[1] = startRequirement.getHeelOptions().getMaximumHeelAvailableInM3();
		assert heelInM3Range[0] >= 0;
		assert heelInM3Range[1] >= 0;

		// For spot charters, start with the safety heel.
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER) {
			assert heelInM3Range[0] == vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
			assert heelInM3Range[1] == vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
			// heelVolumeInM3 = vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
			assert heelInM3Range[0] >= 0;
		}
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			assert heelInM3Range[0] == vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
			assert heelInM3Range[1] == vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
			// heelVolumeInM3 = vesselAvailability.getVessel().getVesselClass().getSafetyHeel();
			assert heelInM3Range[0] >= 0;
		}

		// Counter to find last port times record
		int idx = 0;
		int currentCargoCV = 0;
		for (final IPortTimesRecord portTimesRecord : portTimesRecords) {
			++idx;

			final List<@NonNull IOptionsSequenceElement> voyageOrPortOptions = new LinkedList<>();
			final List<@NonNull IVoyagePlanChoice> vpoChoices = new LinkedList<>();

			@Nullable
			VoyageOptions previousOptions = null;
			boolean useNBO = false;

			final int vesselStartTime = portTimesRecord.getFirstSlotTime();
			@Nullable
			IPortSlot prevPortSlot = null;

			final long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, /** FIXME: not utc */
					vesselStartTime, timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getFirstSlotTime(), portTimesRecord.getFirstSlot()));

			// Create a list of all slots including the optional (not for shipped cargoes) return slot
			final List<@NonNull IPortSlot> recordSlots = new ArrayList<>(portTimesRecord.getSlots().size() + 1);
			recordSlots.addAll(portTimesRecord.getSlots());
			final IPortSlot returnSlot = portTimesRecord.getReturnSlot();
			if (returnSlot != null) {
				recordSlots.add(returnSlot);
			}

			for (final IPortSlot thisPortSlot : recordSlots) {
				final int thisArrivalTime = portTimesRecord.getSlotTime(thisPortSlot);

				if (thisPortSlot != returnSlot) {

					// If we are a heel options slots (i.e. Start or other vessel event slot, overwrite previous heel (assume lost) and replace with a new heel value
					// TODO: Move (back?)into VPO code
					if (thisPortSlot instanceof IHeelOptionSupplierPortSlot) {
						final IHeelOptionSupplierPortSlot supplier = (IHeelOptionSupplierPortSlot) thisPortSlot;
						heelInM3Range[0] = supplier.getHeelOptionsSupplier().getMinimumHeelAvailableInM3();
						heelInM3Range[1] = supplier.getHeelOptionsSupplier().getMaximumHeelAvailableInM3();
						assert heelInM3Range[0] >= 0;
						assert heelInM3Range[1] >= 0;
					} else if (thisPortSlot.getPortType() == PortType.DryDock) {
						heelInM3Range[0] = 0;
						heelInM3Range[1] = 0;
					} else if (thisPortSlot.getPortType() == PortType.Maintenance) {
						heelInM3Range[0] = 0;
						heelInM3Range[1] = 0;
					}
				}

				// If this is the first port, then this will be null and there will
				// be no voyage to plan.
				if (prevPortSlot != null) {

					final int prevArrivalTime = portTimesRecord.getSlotTime(prevPortSlot);

					final int availableTravelTime = thisArrivalTime - prevArrivalTime - portTimesRecord.getSlotDuration(prevPortSlot);

					final int voyageStartTime = prevArrivalTime + portTimesRecord.getSlotDuration(prevPortSlot);

					final VesselState vesselState = findVesselState(portTimesRecord, prevPortSlot);
					final VoyageOptions options = getVoyageOptionsAndSetVpoChoices(vesselAvailability, vesselState, voyageStartTime, availableTravelTime, prevPortSlot, thisPortSlot, previousOptions,
							vpoChoices, useNBO);
					useNBO = options.useNBOForTravel();
					voyageOrPortOptions.add(options);
					previousOptions = options;
				}

				final PortOptions portOptions = new PortOptions(thisPortSlot);
				portOptions.setVessel(vesselAvailability.getVessel());
				if (thisPortSlot == returnSlot) {
					portOptions.setVisitDuration(0);
				} else {
					final int visitDuration = portTimesRecord.getSlotDuration(thisPortSlot);
					portOptions.setVisitDuration(visitDuration);
					assert actualsDataProvider.hasActuals(thisPortSlot) == false || actualsDataProvider.getVisitDuration(thisPortSlot) == visitDuration;
				}

				final int cargoCV;
				if (actualsDataProvider.hasActuals(thisPortSlot)) {
					cargoCV = actualsDataProvider.getCVValue(thisPortSlot);
				} else if (thisPortSlot instanceof IHeelOptionSupplierPortSlot) {
					final IHeelOptionSupplierPortSlot heelOptionsSlot = (IHeelOptionSupplierPortSlot) thisPortSlot;
					cargoCV = heelOptionsSlot.getHeelOptionsSupplier().getHeelCVValue();
				} else if (thisPortSlot instanceof ILoadOption) {
					final ILoadOption loadOption = (ILoadOption) thisPortSlot;
					cargoCV = loadOption.getCargoCVValue();
				} else {
					if (previousOptions != null) {
						cargoCV = previousOptions.getCargoCVValue();
					} else {
						cargoCV = currentCargoCV;
					}
				}
				currentCargoCV = cargoCV;
				portOptions.setCargoCVValue(cargoCV);

				voyageOrPortOptions.add(portOptions);

				// Sequence scheduler should be using the actuals time
				assert actualsDataProvider.hasActuals(thisPortSlot) == false || actualsDataProvider.getArrivalTime(thisPortSlot) == thisArrivalTime;

				prevPortSlot = thisPortSlot;
			}

			// final IPortSlot thisPortSlot = portTimesRecord.getReturnSlot();
			// final PortOptions portOptions;
			if (voyageOrPortOptions.size() > 0) {
				// Use prev slot as "thisPortSlot" is the start of a new voyage plan and thus likely a different cargo
				if (actualsDataProvider.hasActuals(recordSlots.get(0))) {
					long endHeelInM3 = generateActualsVoyagePlan(vesselAvailability, vesselStartTime, voyagePlansMap, voyagePlansList, voyageOrPortOptions, portTimesRecord, heelInM3Range);

					assert heelInM3Range[0] >= 0;
					heelInM3Range[0] = endHeelInM3;
					heelInM3Range[1] = endHeelInM3;
				} else {
					// set base fuel price in VPO
					final Triple<IVessel, IResource, Integer> vesselTriple = setVesselAndBaseFuelPrice(portTimesRecord, vesselAvailability.getVessel(), resource);

					final boolean roundTripCargoEnd = ((PortOptions) voyageOrPortOptions.get(0)).getPortSlot().getPortType() == PortType.Round_Trip_Cargo_End;

					// Special case for round trip cargo routes. There is no voyage between a Round_Trip_Cargo_End and the next load - which this current sequence will represent. However we do need to
					// model the Round_Trip_Cargo_End for the VoyagePlanIterator to work correctly. Here we strip the voyage and make this a single element sequence.
					if (!roundTripCargoEnd) {

						final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, portTimesRecord, voyagePlanOptimiser, heelInM3Range, vesselCharterInRatePerDay,
								vesselAvailability.getVesselInstanceType(), vesselTriple, vpoChoices);

						if (plan == null) {
							return null;
						}

						// Last voyage plan?
						if (isRoundTripSequence || idx == portTimesRecords.size()) {
							plan.setIgnoreEnd(false);
						}
						long endHeelInM3 = evaluateVoyagePlan(vesselAvailability, vesselStartTime, voyagePlansMap, voyagePlansList, portTimesRecord, heelInM3Range, plan);
						assert endHeelInM3 >= 0;
						heelInM3Range[0] = endHeelInM3;
						heelInM3Range[1] = endHeelInM3;
					}

					if (isRoundTripSequence) {
						voyagePlansList.get(voyagePlansList.size() - 1).setIgnoreEnd(false);
					}
				}
			}
		}

		return voyagePlansMap;

	}

	private long generateActualsVoyagePlan(final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime,
			final @NonNull List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> voyagePlansMap, final @NonNull List<@NonNull VoyagePlan> voyagePlansList,
			final @NonNull List<@NonNull IOptionsSequenceElement> voyageOrPortOptions, final @NonNull IPortTimesRecord portTimesRecord, final long[] startHeelVolumeInM3) {

		final VoyagePlan plan = new VoyagePlan();
		// Replace with actuals later if needed
		plan.setCharterInRatePerDay(charterRateCalculator.getCharterRatePerDay(vesselAvailability, /** FIXME: not utc */
				vesselStartTime, timeZoneToUtcOffsetProvider.UTC(portTimesRecord.getFirstSlotTime(), portTimesRecord.getFirstSlot())));
		assert startHeelVolumeInM3[0] == startHeelVolumeInM3[1];
		plan.setStartingHeelInM3(startHeelVolumeInM3[0]);
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

					final PortDetails portDetails = new PortDetails(portOptions);

					if (actualsDataProvider.hasActuals(portOptions.getPortSlot())) {
						portDetails.setPortCosts(actualsDataProvider.getPortCosts(portOptions.getPortSlot()));
					}

					if (idx == 0) {
						if (actualsDataProvider.hasActuals(portOptions.getPortSlot())) {

							// This should not be required in future as preceding voyages should also be actualised!
							assert startHeelVolumeInM3[0] == actualsDataProvider.getStartHeelInM3(portOptions.getPortSlot());
							assert startHeelVolumeInM3[1] == actualsDataProvider.getStartHeelInM3(portOptions.getPortSlot());
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
						portDetails.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, baseFuelConsumptionInMt);
					}
					detailedSequence[idx] = portDetails;
				} else if (element instanceof VoyageOptions) {
					final VoyageOptions voyageOptions = (VoyageOptions) element;

					final VoyageDetails voyageDetails = new VoyageDetails(voyageOptions);

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
					}

					final long consumptionInMMBTu = Calculator.convertM3ToMMBTu(lngInM3, cargoCV);
					voyageDetails.setFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu, consumptionInMMBTu);

					fuelConsumptions[FuelComponent.NBO.ordinal()] += lngInM3;
					fuelCosts[FuelComponent.NBO.ordinal()] += Calculator.costFromConsumption(consumptionInMMBTu, lngSalesPricePerMMBTu);
					lngCommitmentInM3 += lngInM3;

					// Consumption rolled into normal fuel consumption
					// voyageDetails.setRouteAdditionalConsumption(fuel, fuelUnit, consumption);

					// Route costs
					final long routeCosts = actualsDataProvider.getNextVoyageRouteCosts(voyageOptions.getFromPortSlot());
					totalRouteCost += routeCosts;

					voyageOptions.setRoute(actualsDataProvider.getNextVoyageRoute(voyageOptions.getFromPortSlot()), actualsDataProvider.getNextVoyageDistance(voyageOptions.getFromPortSlot()),
							routeCosts);

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

		final AllocationRecord allocationRecord = volumeAllocator.get().createAllocationRecord(vesselAvailability, vesselStartTime, plan, portTimesRecord);
		allocationRecord.allocationMode = AllocationMode.Actuals;
		final IAllocationAnnotation allocationAnnotation = volumeAllocator.get().allocate(allocationRecord);
		if (allocationAnnotation != null) {
			// Sanity check
			assert plan.getRemainingHeelInM3() == allocationAnnotation.getRemainingHeelVolumeInM3();
		}
		final IPortTimesRecord rec = allocationAnnotation == null ? portTimesRecord : allocationAnnotation;
		voyagePlansMap.add(new Pair<>(plan, rec));

		return plan.getRemainingHeelInM3();
	}

	// TODO: Better naming?
	private long evaluateVoyagePlan(final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime, final @NonNull List<Pair<VoyagePlan, IPortTimesRecord>> voyagePlansMap,
			final List<@NonNull VoyagePlan> voyagePlansList, final @NonNull IPortTimesRecord portTimesRecord, final long[] heelVolumeRangeInM3, final @NonNull VoyagePlan originalPlan) {

		// Take a copy so we can retain isIgnoreEnd flag later on
		final VoyagePlan plan = originalPlan; // TODO: remove
		final List<@NonNull PlanEvaluationData> plans = new ArrayList<>();

		assert heelVolumeRangeInM3[0] >= 0;
		assert heelVolumeRangeInM3[1] >= 0;
		boolean planSet = false;
		if (generatedCharterOutEvaluator != null) {
			final List<@NonNull Pair<@NonNull VoyagePlan, @NonNull IPortTimesRecord>> lp = generatedCharterOutEvaluator.processSchedule(vesselStartTime, heelVolumeRangeInM3, vesselAvailability, plan,
					portTimesRecord);
			if (lp != null) {
				for (final Pair<@NonNull VoyagePlan, @NonNull IPortTimesRecord> p : lp) {
					final PlanEvaluationData evalData = new PlanEvaluationData(portTimesRecord, p.getFirst());
					voyagePlansList.add(evalData.plan);

					if (p.getSecond() instanceof IAllocationAnnotation) {
						evalData.allocation = (IAllocationAnnotation) p.getSecond();
					}
					evalData.portTimesRecord = p.getSecond();

					if (evalData.allocation != null) {
						evalData.endHeelVolumeInM3 = evalData.allocation.getRemainingHeelVolumeInM3();
						evalData.startHeelVolumeInM3 = evalData.allocation.getStartHeelVolumeInM3();

					} else {
						evalData.endHeelVolumeInM3 = evalData.plan.getRemainingHeelInM3();
						evalData.startHeelVolumeInM3 = evalData.plan.getStartingHeelInM3();
					}
					plans.add(evalData);
					evalData.setIgnoreEndSet(true);
				}
				planSet = true;
			}
		}

		// Execute custom logic to manipulate the schedule and choices
		if (breakEvenEvaluator != null) {
			final Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> p = breakEvenEvaluator.processSchedule(vesselStartTime, vesselAvailability, plan, portTimesRecord);
			if (p != null) {
				final PlanEvaluationData evalData = new PlanEvaluationData(portTimesRecord, p.getFirst());
				voyagePlansList.add(evalData.plan);

				if (p.getSecond() instanceof IAllocationAnnotation) {
					evalData.allocation = (IAllocationAnnotation) p.getSecond();
					evalData.portTimesRecord = p.getSecond();
				} else {
					evalData.portTimesRecord = portTimesRecord;
				}

				if (evalData.allocation != null) {
					evalData.endHeelVolumeInM3 = evalData.allocation.getRemainingHeelVolumeInM3();
					evalData.startHeelVolumeInM3 = evalData.allocation.getStartHeelVolumeInM3();
				} else {
					evalData.endHeelVolumeInM3 = evalData.plan.getRemainingHeelInM3();
					evalData.startHeelVolumeInM3 = evalData.plan.getStartingHeelInM3();
				}
				plans.add(evalData);
				planSet = true;
			}
		}

		if (!planSet) {
			final PlanEvaluationData evalData = new PlanEvaluationData(portTimesRecord, plan);
			voyagePlansList.add(evalData.plan);

			evalData.allocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, plan, portTimesRecord);
			if (evalData.allocation != null) {
				evalData.portTimesRecord = evalData.allocation;
			} else {
				evalData.portTimesRecord = portTimesRecord;
			}

			if (evalData.allocation != null) {
				evalData.endHeelVolumeInM3 = evalData.allocation.getRemainingHeelVolumeInM3();
				evalData.startHeelVolumeInM3 = evalData.allocation.getStartHeelVolumeInM3();
			} else {
				evalData.endHeelVolumeInM3 = evalData.plan.getRemainingHeelInM3();
				evalData.startHeelVolumeInM3 = evalData.plan.getStartingHeelInM3();

			}
			plans.add(evalData);
		}

		for (final PlanEvaluationData planData : plans) {
			evaluateBrokenUpVoyagePlan(planData, vesselAvailability, vesselStartTime, voyagePlansMap, voyagePlansList, originalPlan);
		}

		// return the end heel of the last plan (for the generated charter out case there will be more than one plan)
		return plans.get(plans.size() - 1).getEndHeelVolumeInM3();

	}

	/**
	 * Returns a voyage plan based on the given sequence
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	@Nullable
	final public VoyagePlan makeVoyage(@NonNull final IResource resource, final long vesselCharterInRatePerDay, @NonNull final IPortTimesRecord portTimesRecord, final long[] heelVolumeRangeInM3) {
		return makeVoyage(resource, vesselCharterInRatePerDay, portTimesRecord, heelVolumeRangeInM3, voyagePlanOptimiserProvider.get());
	}

	/**
	 * Returns a voyage plan based on the given sequence
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	@Nullable
	final public VoyagePlan makeVoyage(@NonNull final IResource resource, final long vesselCharterInRatePerDay, @NonNull final IPortTimesRecord portTimesRecord, long[] initialHeelVolumeRangeInM3,
			@NonNull final IVoyagePlanOptimiser voyagePlanOptimiser) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		final List<@NonNull IOptionsSequenceElement> voyageOrPortOptions = new ArrayList<>(5);

		@NonNull
		final List<@NonNull IVoyagePlanChoice> vpoChoices = new LinkedList<>();

		VoyageOptions previousOptions = null;
		boolean useNBO = false;

		@Nullable
		IPortSlot prevPortSlot = null;

		// Create a list of all slots including the optional (not for shipped cargoes) return slot
		final List<@NonNull IPortSlot> recordSlots = new ArrayList<>(portTimesRecord.getSlots().size() + 1);
		recordSlots.addAll(portTimesRecord.getSlots());
		final IPortSlot returnSlot = portTimesRecord.getReturnSlot();
		if (returnSlot != null) {
			recordSlots.add(returnSlot);
		}
		long[] heelInM3Range = new long[2];
		heelInM3Range[0] = initialHeelVolumeRangeInM3[0];
		heelInM3Range[1] = initialHeelVolumeRangeInM3[1];
		for (final IPortSlot thisPortSlot : recordSlots) {
			final int thisArrivalTime = portTimesRecord.getSlotTime(thisPortSlot);
			// If we are a heel options slots (i.e. Start or other vessel event slot, overwrite previous heel (assume lost) and replace with a new heel value
			// TODO: Move (back?)into VPO code
			if (thisPortSlot instanceof IHeelOptionSupplierPortSlot) {
				IHeelOptionSupplierPortSlot supplier = (IHeelOptionSupplierPortSlot) thisPortSlot;
				heelInM3Range[0] = supplier.getHeelOptionsSupplier().getMinimumHeelAvailableInM3();
				heelInM3Range[1] = supplier.getHeelOptionsSupplier().getMaximumHeelAvailableInM3();
				assert heelInM3Range[0] >= 0;
				assert heelInM3Range[1] >= 0;
			}

			// If this is the first port, then this will be null and there will
			// be no voyage to plan.
			if (prevPortSlot != null) {

				final int prevArrivalTime = portTimesRecord.getSlotTime(prevPortSlot);
				final int voyageStartTime = prevArrivalTime;
				final int availableTravelTime = thisArrivalTime - prevArrivalTime - portTimesRecord.getSlotDuration(prevPortSlot);

				final VesselState vesselState = findVesselState(portTimesRecord, prevPortSlot);
				final VoyageOptions options = getVoyageOptionsAndSetVpoChoices(vesselAvailability, vesselState, voyageStartTime, availableTravelTime, prevPortSlot, thisPortSlot, previousOptions,
						vpoChoices, useNBO);
				useNBO = options.useNBOForTravel();
				voyageOrPortOptions.add(options);
				previousOptions = options;
			}

			final PortOptions portOptions = new PortOptions(thisPortSlot);
			portOptions.setVessel(vesselAvailability.getVessel());

			
			final int cargoCV;
			if (actualsDataProvider.hasActuals(thisPortSlot)) {
				cargoCV = actualsDataProvider.getCVValue(thisPortSlot);
			} else if (thisPortSlot instanceof IHeelOptionSupplierPortSlot) {
				final IHeelOptionSupplierPortSlot heelOptionsSlot = (IHeelOptionSupplierPortSlot) thisPortSlot;
				cargoCV = heelOptionsSlot.getHeelOptionsSupplier().getHeelCVValue();
			} else if (thisPortSlot instanceof ILoadOption) {
				final ILoadOption loadOption = (ILoadOption) thisPortSlot;
				cargoCV = loadOption.getCargoCVValue();
			} else {
				if (previousOptions != null) {
					cargoCV = previousOptions.getCargoCVValue();
				} else {
					cargoCV = 0;
				}
			}
			portOptions.setCargoCVValue(cargoCV);
			
			if (thisPortSlot == returnSlot) {
				portOptions.setVisitDuration(0);
			} else {
				final int visitDuration = portTimesRecord.getSlotDuration(thisPortSlot);
				portOptions.setVisitDuration(visitDuration);
			}
			voyageOrPortOptions.add(portOptions);

			// Sequence scheduler should be using the actuals time
			assert actualsDataProvider.hasActuals(thisPortSlot) == false || actualsDataProvider.getArrivalTime(thisPortSlot) == thisArrivalTime;
			// assert actualsDataProvider.hasActuals(thisPortSlot) == false || actualsDataProvider.getVisitDuration(thisPortSlot) == visitDuration;

			prevPortSlot = thisPortSlot;
		}

		// TODO: Insert actuals VoyagePlan stuff here to allow this method to be used inside main makeVoyagePlans loop

		// Populate final plan details
		if (voyageOrPortOptions.size() > 0) {
			// set base fuel price in VPO
			final Triple<@NonNull IVessel, @Nullable IResource, @NonNull Integer> vesselTriple = setVesselAndBaseFuelPrice(portTimesRecord, vesselAvailability.getVessel(), resource);
			final VoyagePlan plan = getOptimisedVoyagePlan(voyageOrPortOptions, portTimesRecord, voyagePlanOptimiser, heelInM3Range, vesselCharterInRatePerDay,
					vesselAvailability.getVesselInstanceType(), vesselTriple, vpoChoices);
			// voyagePlanOptimiser.reset();
			if (plan == null) {
				return null;
			}

			if (isRoundTripSequence) {
				plan.setIgnoreEnd(false);
			}
			return plan;
		}
		return null;
	}

	public final @NonNull VoyagePlan makeDESOrFOBVoyagePlan(final @NonNull IResource resource, final @NonNull IPortTimesRecord portTimesRecord) {

		// Virtual vessels are those operated by a third party, for FOB and DES situations.
		// Should we compute a schedule for them anyway? The arrival times don't mean much,
		// but contracts need this kind of information to make up numbers with.
		final List<@NonNull IDetailsSequenceElement> currentSequence = new ArrayList<>(2);
		final VoyagePlan currentPlan = new VoyagePlan();
		currentPlan.setIgnoreEnd(false);

		final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(resource);
		if (nominatedVessel != null) {
			// Set a start and end heel for BOG estimations
			currentPlan.setStartingHeelInM3(nominatedVessel.getVesselClass().getSafetyHeel());
			currentPlan.setRemainingHeelInM3(nominatedVessel.getVesselClass().getSafetyHeel());
		}

		boolean dischargeOptionInMMBTU = false;
		for (final IPortSlot slot : portTimesRecord.getSlots()) {

			if (slot instanceof IDischargeOption) {
				// }
				final IDischargeOption dischargeOption = (IDischargeOption) slot;
				if (dischargeOptionInMMBTU == false && !dischargeOption.isVolumeSetInM3()) {
					dischargeOptionInMMBTU = true;
				}
			}

			// Actuals Data...
			if (slot instanceof ILoadOption) {
				// overwrite with actuals if need be
				if (actualsDataProvider.hasActuals(slot)) {
					currentPlan.setStartingHeelInM3(actualsDataProvider.getStartHeelInM3(slot));
				}
			}
			// overwrite with actuals if need be
			if (actualsDataProvider.hasReturnActuals(slot)) {
				currentPlan.setRemainingHeelInM3(actualsDataProvider.getReturnHeelInM3(slot));
			}

			final PortOptions portOptions = new PortOptions(slot);
			final PortDetails portDetails = new PortDetails(portOptions);
			portOptions.setVisitDuration(0);
			// Custom scheduling code may change this to non-zero, but we need it as zero for visualisation in reports
			// portOptions.setVisitDuration(portTimesRecord.getSlotDuration(slot));
			currentSequence.add(portDetails);
		}

		currentPlan.setSequence(currentSequence.toArray(new IDetailsSequenceElement[0]));

		return currentPlan;
	}

	public final @NonNull Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> makeDESOrFOBVoyagePlanPair(final @NonNull IResource resource, final @NonNull ISequence sequence,
			final @NonNull IPortTimesRecord portTimesRecord) {

		final VoyagePlan currentPlan = makeDESOrFOBVoyagePlan(resource, portTimesRecord);

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final int vesselStartTime = portTimesRecord.getFirstSlotTime();

		// Check break-even logic
		{
			// Execute custom logic to manipulate the schedule and choices
			if (breakEvenEvaluator != null) {
				final Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> p = breakEvenEvaluator.processSchedule(vesselStartTime, vesselAvailability, currentPlan, portTimesRecord);
				if (p != null) {
					return p;
				}
			}

		}

		final IAllocationAnnotation annotation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, currentPlan, portTimesRecord);

		return new Pair<>(currentPlan, annotation);
	}

	final private @NonNull Triple<@NonNull IVessel, @Nullable IResource, @NonNull Integer> setVesselAndBaseFuelPrice(@NonNull final IPortTimesRecord portTimesRecord, @NonNull final IVessel vessel,
			@NonNull final IResource resource) {

		Triple<@NonNull IVessel, @Nullable IResource, @NonNull Integer> t = new Triple<>(vessel, resource, vesselBaseFuelCalculator.getBaseFuelPrice(vessel, portTimesRecord));
		if (portTimesRecord.getFirstSlot() instanceof ILoadOption) {
			if (actualsDataProvider.hasActuals(portTimesRecord.getFirstSlot())) {
				t = new Triple<>(vessel, resource, actualsDataProvider.getBaseFuelPricePerMT(portTimesRecord.getFirstSlot()));
			}
		}
		return t;
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
	@Nullable
	final public VoyagePlan getOptimisedVoyagePlan(final @NonNull List<@NonNull IOptionsSequenceElement> voyageOrPortOptionsSubsequence, final @NonNull IPortTimesRecord portTimesRecord,
			final @NonNull IVoyagePlanOptimiser optimiser, final long @NonNull [] heelVolumeRangeInM3, final long vesselCharterInRatePerDay, final @NonNull VesselInstanceType vesselInstanceType,
			final Triple<@NonNull IVessel, @Nullable IResource, @NonNull Integer> vesselTriple, @NonNull final List<@NonNull IVoyagePlanChoice> vpoChoices) {

		// Run sequencer evaluation
		final VoyagePlan result = optimiser.optimise(vesselTriple.getSecond(), vesselTriple.getFirst(), heelVolumeRangeInM3.clone(), vesselTriple.getThird(), vesselCharterInRatePerDay,
				portTimesRecord, voyageOrPortOptionsSubsequence, vpoChoices);

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

		// Fix up final arrival time. The VPO is permitted to change the final arrival time of certain vessels and we need to alter the arrival time array and the portTimesRecord with the new
		// arrival time.
		final IDetailsSequenceElement[] vpSequence = result.getSequence();
		if (vpSequence.length > 1) {
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
	 * Returns an array of vessel states determining, for each index of the vessel location sequence, whether the vessel arrives at that location with LNG cargo on board for resale or not
	 * 
	 * @param sequence
	 * @return
	 */
	@NonNull
	private VesselState findVesselState(final @NonNull IPortTimesRecord portTimesRecord, @NonNull final IPortSlot fromPortSlot) {

		@NonNull
		VesselState state = VesselState.Ballast;

		final @NonNull List<@NonNull IPortSlot> slots = portTimesRecord.getSlots();
		final int size = slots.size();

		for (int idx = 0; idx < size; ++idx) {
			final IPortSlot portSlot = slots.get(idx);
			final PortType portType = portSlot.getPortType();
			if (portType == PortType.Load) {
				state = VesselState.Laden;
			} else if (portType == PortType.Discharge) {
				final VesselState lstate = state;
				state = VesselState.Ballast;
				// For complex cargoes, we are laden until the last leg. Thus LDD is laden, laden, then ballast
				if (lstate == VesselState.Laden && idx + 1 < size) {
					final IPortSlot next_portSlot = slots.get(1 + idx);
					final PortType next_portType = next_portSlot.getPortType();
					if (next_portType == PortType.Discharge) {
						state = VesselState.Laden;
					}
				}
			} else {
				state = VesselState.Ballast;
			}
			if (portSlot == fromPortSlot) {
				return state;
			}
		}

		throw new IllegalArgumentException("Unknown from port slot");
	}

	/**
	 * Here, a single a voyage plan is evaluated, which may be part of what used to be a single voyage plan, e.g. if a charter out event was generated.
	 * 
	 * @return
	 */
	private void evaluateBrokenUpVoyagePlan(final @NonNull PlanEvaluationData planData, final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime,
			final List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> voyagePlansMap, final List<@NonNull VoyagePlan> voyagePlansList, final VoyagePlan originalPlan) {

		assert planData.getStartHeelVolumeInM3() >= 0;
		assert planData.getEndHeelVolumeInM3() >= 0;
		assert planData.getPlan().getStartingHeelInM3() >= 0;
		assert planData.getPlan().getLNGFuelVolume() >= 0;
		assert planData.getPlan().getRemainingHeelInM3() >= 0;

		{
			final IDetailsSequenceElement[] sequence = planData.getPlan().getSequence();
			long currentHeelInM3 = planData.getPlan().getStartingHeelInM3();
			long totalVoyageBOG = 0;

			final int adjust = planData.getPlan().isIgnoreEnd() ? 1 : 0;
			for (int i = 0; i < sequence.length - adjust; ++i) {
				final IDetailsSequenceElement e = sequence[i];
				if (e instanceof PortDetails) {
					final PortDetails portDetails = (PortDetails) e;

					final IPortSlot portSlot = portDetails.getOptions().getPortSlot();
					final long start = currentHeelInM3;
					if (portSlot.getPortType() != PortType.End) {
						if (planData.getAllocation() != null) {
							if (portSlot.getPortType() == PortType.Load) {
								currentHeelInM3 += planData.getAllocation().getPhysicalSlotVolumeInM3(portSlot);
							} else if (portSlot.getPortType() == PortType.Discharge) {
								currentHeelInM3 -= planData.getAllocation().getPhysicalSlotVolumeInM3(portSlot);
							}
						}

						totalVoyageBOG += portDetails.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3);
						if (portSlot.getPortType() == PortType.Discharge) {
							currentHeelInM3 -= portDetails.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3);
						}

						assert currentHeelInM3 + ROUNDING_EPSILON >= 0;
					} else {
						if (portSlot.getPortType() == PortType.End) {
							// final EndPortSlot endPortSlot = (EndPortSlot) portSlot;
							// Assert disabled as it is not always possible to arrive with target heel (thus capacity violation should be triggered)
							// assert currentHeelInM3 >= endPortSlot.getTargetEndHeelInM3();
						}
					}
					final long end = currentHeelInM3;
					// if (i == 0) {
					// heelLevelAnnotation.setHeelCost(planData.getPlan().getStartHeelCost());
					// }
				} else if (e instanceof VoyageDetails) {
					final VoyageDetails voyageDetails = (VoyageDetails) e;
					long voyageBOGInM3 = 0;
					for (final FuelComponent fuel : FuelComponent.getLNGFuelComponents()) {
						voyageBOGInM3 += voyageDetails.getFuelConsumption(fuel, FuelUnit.M3);
						voyageBOGInM3 += voyageDetails.getRouteAdditionalConsumption(fuel, FuelUnit.M3);
					}
					totalVoyageBOG += voyageBOGInM3;
					currentHeelInM3 -= voyageBOGInM3;
					assert currentHeelInM3 + ROUNDING_EPSILON >= 0;
				}
			}

			assert currentHeelInM3 + ROUNDING_EPSILON >= 0;

			// Sanity check these calculations match expected values
			assert totalVoyageBOG == planData.getPlan().getLNGFuelVolume();
			// Note: there may be some rounding errors when using MMBTU, so keep an eye on epsilon
			assert (Math.abs(planData.getEndHeelVolumeInM3() - currentHeelInM3) <= ROUNDING_EPSILON);
		}

		// Ensure this flag is copied across!
		if (!planData.isIgnoreEndSet()) {
			planData.getPlan().setIgnoreEnd(originalPlan.isIgnoreEnd());
		}

		voyagePlansMap.add(new Pair<>(planData.getPlan(), planData.getPortTimesRecord()));

	}
}
