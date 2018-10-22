/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ICargoToCargoCostCalculator;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PricingEventHelper;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselCharterInRateProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateFuelCosts;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateVoyageCalculatorHelper;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateVoyageCalculatorHelper.ApproximateFuelCostLegData;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

public class SimpleCargoToCargoCostCalculator implements ICargoToCargoCostCalculator {

	@Inject
	IDistanceProvider distanceProvider;
	
	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;
	
	@Inject
	private IRouteCostProvider routeCostProvider;
	
	@Inject
	private IElementDurationProvider elementDurationProvider;
	
	@Inject
	private IPortSlotProvider portSlotProvider;
	
	@Inject
	private IVesselProvider vesselProvider;
	
	@Inject
	IVesselCharterInRateProvider vesselCharterInRateProvider;
	
	@Inject
	PricingEventHelper pricingEventHelper;
	
	private long calculateNonCharterVariableCosts(final ILoadSlot loadA, final IDischargeSlot dischargeA, final IPortSlot vesselEventA, IPortSlot startSlotB, final IVesselAvailability vessel) {
		assert((loadA != null && dischargeA != null) || vesselEventA != null);
		final int startA = loadA != null ? loadA.getTimeWindow().getInclusiveStart() : vesselEventA.getTimeWindow().getInclusiveStart();
		final int endA = dischargeA != null ? dischargeA.getTimeWindow().getInclusiveStart() : vesselEventA.getTimeWindow().getInclusiveStart() //
				+ elementDurationProvider.getElementDuration(portSlotProvider.getElement(vesselEventA), vesselProvider.getResource(vessel));
		final int startB = startSlotB.getTimeWindow().getInclusiveStart();

		// if discharge is null use the vessel event (as it must be a vessel event)
		final IPort endAPort = getEndPort(dischargeA, vesselEventA);
		
		if (endAPort == startSlotB.getPort()) {
			return 0;
		}
		@NonNull
		final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTime(vessel.getVessel(), endAPort, //
				startSlotB.getPort(), vessel.getVessel().getMaxSpeed(),AvailableRouteChoices.OPTIMAL);
		// If the travel time is 0 no cost so shortcut
		if (quickestTravelTimeAToB.getSecond() == 0) {
			return 0L;
		}
		final int[] baseFuelPrices = vesselBaseFuelCalculator.getBaseFuelPrices(vessel.getVessel(), startA);		
		
		ApproximateFuelCosts legFuelCosts = null;
		if (loadA != null) {
			ApproximateFuelCostLegData inputData = new ApproximateFuelCostLegData();
			inputData.salesPrice = dischargeA.getDischargePriceCalculator().getEstimatedSalesPrice(loadA, dischargeA, endA);
			inputData.boiloffRateM3 = vessel.getVessel().getNBORate(VesselState.Ballast);
			inputData.vessel = vessel.getVessel();
			inputData.cv = loadA.getCargoCVValue();
			inputData.times = new int[] { endA, endA + quickestTravelTimeAToB.getSecond() };
			inputData.distance = distanceProvider.getDistance(quickestTravelTimeAToB.getFirst(), endAPort, //
					startSlotB.getPort(), vessel.getVessel());
			inputData.equivalenceFactor = vessel.getVessel().getTravelBaseFuel().getEquivalenceFactor();
			inputData.baseFuelPricesPerMT = baseFuelPrices;
			inputData.canalTransitTime = routeCostProvider.getRouteTransitTime(quickestTravelTimeAToB.getFirst(), vessel.getVessel());
			inputData.durationAtPort = 0;
			inputData.isLaden = false;
			inputData.forceBaseFuel = false;

			legFuelCosts = ApproximateVoyageCalculatorHelper.getLegFuelCosts(inputData);
		} else {
			ApproximateFuelCostLegData inputData = new ApproximateFuelCostLegData();
			inputData.salesPrice = 0;
			inputData.boiloffRateM3 = vessel.getVessel().getNBORate(VesselState.Ballast);
			inputData.vessel = vessel.getVessel();
			inputData.cv = 22_67_0000;
			inputData.times = new int[] { endA, endA + quickestTravelTimeAToB.getSecond() };
			inputData.distance = distanceProvider.getDistance(quickestTravelTimeAToB.getFirst(), endAPort, //
					startSlotB.getPort(), vessel.getVessel());
			inputData.equivalenceFactor = vessel.getVessel().getTravelBaseFuel().getEquivalenceFactor();
			inputData.baseFuelPricesPerMT = baseFuelPrices;
			inputData.canalTransitTime = routeCostProvider.getRouteTransitTime(quickestTravelTimeAToB.getFirst(), vessel.getVessel());
			inputData.durationAtPort = 0;
			inputData.isLaden = false;
			inputData.forceBaseFuel = true;
			try {
				legFuelCosts = ApproximateVoyageCalculatorHelper.getLegFuelCosts(inputData);
			} catch (Exception e) {
				ApproximateVoyageCalculatorHelper.getLegFuelCosts(inputData);
			}
		}
		return legFuelCosts.getTotalCost() + routeCostProvider
				.getRouteCost(quickestTravelTimeAToB.getFirst(), vessel.getVessel(), endA, CostType.RoundTripBallast);
	}

	private IPort getEndPort(final IDischargeSlot dischargeA, final IPortSlot vesselEventA) {
		return dischargeA != null ? dischargeA.getPort() : vesselEventA.getPort();
	}
	
	public long[][] getCargoCharterCostPerAvailability(final List<List<IPortSlot>> cargoes, final List<IVesselAvailability> vessels) {
		long[][] costs = new long[cargoes.size()][vessels.size()];
		for (int i = 0; i < cargoes.size(); i++) {
			List<IPortSlot> cargo = cargoes.get(i);
			for (int j = 0; j < vessels.size(); j++) {
				IVesselAvailability availability = vessels.get(j);
				if (SchedulerCalculationUtils.isVesselAvailabilityOptional(availability)) {
					costs[i][j] = availability.getDailyCharterInRate().getValueAtPoint(cargo.get(0).getTimeWindow().getInclusiveStart());
				}
			}
		}
		return costs;
	}

	@Override
	public long[][][] createCargoToCargoCostMatrix(final List<List<IPortSlot>> cargoes, final List<IVesselAvailability> vessels) {
		final Map<List<IPortSlot>, Integer> cargoMap = getCargoMap(cargoes);
		final Map<IVesselAvailability, Integer> vesselMap = getVesselMap(vessels);
		final long[][][] costs = new long[cargoes.size()][cargoes.size()][vessels.size()];
		for (final List<IPortSlot> cargoA : cargoes) {
			final ILoadSlot loadA = getLoadSlot(cargoA);
			final IDischargeSlot dischargeA = getDischargeSlot(cargoA);
			final IPortSlot vesselEventA = getVesselEvent(cargoA);
			if ((loadA != null && dischargeA != null) || vesselEventA != null) {
				for (final List<IPortSlot> cargoB : cargoes) {
					final ILoadSlot loadB = getLoadSlot(cargoB);
					final IPortSlot vesselEventB = getVesselEvent(cargoB);
					final IPortSlot endSlot = loadB == null ? vesselEventB : loadB;
					for (final IVesselAvailability vessel : vessels) {
						if (cargoA == cargoB) {
							costs[cargoMap.get(cargoA)][cargoMap.get(cargoB)][vesselMap.get(vessel)] = 0L;
						} else {
							long cost = -Long.MIN_VALUE;
							cost = calculateNonCharterVariableCosts(loadA, dischargeA, vesselEventA, endSlot, vessel);
							// Sanity check, should it be an exception ?
							assert(cost != -Long.MIN_VALUE);
							
							costs[cargoMap.get(cargoA)][cargoMap.get(cargoB)][vesselMap.get(vessel)] = cost;
						}
					}
				}
			}
		}
		return costs;
	}
	
	/**
	 * Note: includes duration
	 * @param cargoes
	 * @param vessels
	 * @return
	 */
	public int[][][] getMinCargoToCargoTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels) {
		Map<List<IPortSlot>, Integer> cargoMap = getCargoMap(cargoes);
		Map<IVesselAvailability, Integer> vesselMap = getVesselMap(vessels);
		int[][][] times = new int[cargoes.size()][cargoes.size()][vessels.size()];
		for (List<IPortSlot> cargoA : cargoes) {
			final IDischargeSlot dischargeA = getDischargeSlot(cargoA);
			final IPortSlot vesselEventA = getVesselEvent(cargoA);
			if (dischargeA != null || vesselEventA != null) {
				// if discharge is null use the vessel event (as it must be a vessel event)
				final IPort endAPort = getEndPort(dischargeA, vesselEventA);
				for (List<IPortSlot> cargoB : cargoes) {
					if (cargoA == cargoB) {
						continue;
					}
					for (IVesselAvailability vessel : vessels) {
						int endADuration = dischargeA != null ? elementDurationProvider.getElementDuration(portSlotProvider.getElement(dischargeA), vesselProvider.getResource(vessel)) : 0;
						IPortSlot startB = cargoB.get(0);
						@NonNull
						final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTime(vessel.getVessel(), endAPort, startB.getPort(),
								vessel.getVessel().getMaxSpeed(), AvailableRouteChoices.OPTIMAL);
						times[cargoMap.get(cargoA)][cargoMap.get(cargoB)][vesselMap.get(vessel)] = quickestTravelTimeAToB.getSecond()
								+ endADuration;
					}
				}
			}
		}
		return times;
	}
	
	/**
	 * Note: includes duration
	 * @param cargoes
	 * @param vessels
	 * @return
	 */
	public int[][] getMinCargoStartToEndSlotTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels) {
		Map<List<IPortSlot>, Integer> cargoMap = getCargoMap(cargoes);
		Map<IVesselAvailability, Integer> vesselMap = getVesselMap(vessels);
		int[][] times = new int[cargoes.size()][vessels.size()];
		for (List<IPortSlot> cargoA : cargoes) {
			ILoadSlot loadA = getLoadSlot(cargoA);
			IDischargeSlot dischargeA = getDischargeSlot(cargoA);
			final IPortSlot vesselEventA = getVesselEvent(cargoA);

			for (IVesselAvailability vessel : vessels) {
				if (vesselEventA != null) {
					times[cargoMap.get(cargoA)][vesselMap.get(vessel)] = elementDurationProvider.getElementDuration(portSlotProvider.getElement(vesselEventA), vesselProvider.getResource(vessel));
				} else {
					@NonNull
					Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTime(vessel.getVessel(), loadA.getPort(), dischargeA.getPort(),
							vessel.getVessel().getMaxSpeed(), AvailableRouteChoices.OPTIMAL);
	
					times[cargoMap.get(cargoA)][vesselMap.get(vessel)] = quickestTravelTimeAToB.getSecond()
							+ elementDurationProvider.getElementDuration(portSlotProvider.getElement(loadA), vesselProvider.getResource(vessel));
				}
			}
		}
		return times;
	}
	
	public int[] getCargoStartSlotDurations(List<List<IPortSlot>> cargoes) {
		int[] durations = new int[cargoes.size()];
		for (int i = 0; i < cargoes.size(); i++) {
			List<IPortSlot> cargo = cargoes.get(i);
			durations[i] = elementDurationProvider.getElementDuration(
					portSlotProvider.getElement(cargo.get(0)));
		}
		return durations;
	}

	public int[] getCargoEndSlotDurations(List<List<IPortSlot>> cargoes) {
		int[] durations = new int[cargoes.size()];
		for (int i = 0; i < cargoes.size(); i++) {
			List<IPortSlot> cargo = cargoes.get(i);
			IPortSlot lastSlot = cargo.get(cargo.size()-1);
			if (lastSlot.getPortType() == PortType.Load
					|| lastSlot.getPortType() == PortType.Discharge) {
				durations[i] = elementDurationProvider.getElementDuration(
						portSlotProvider.getElement(cargo.get(cargo.size()-1)));
			} else {
				// durations will be encoded on the start slot for vessel events
				durations[i] = 0;
			}
		}
		return durations;
	}

	private ILoadSlot getLoadSlot(List<IPortSlot> cargo) {
		for (IPortSlot portSlot : cargo) {
			if (portSlot instanceof ILoadSlot) {
				return (ILoadSlot) portSlot;
			}
		}
		return null;
	}
	
	private IPortSlot getVesselEvent(List<IPortSlot> cargo) {
		if (cargo.get(0).getPortType() == PortType.CharterOut
				|| cargo.get(0).getPortType() == PortType.DryDock) {
			return cargo.get(0);
		}
		return null;
	}

	private IDischargeSlot getDischargeSlot(List<IPortSlot> cargo) {
		if (cargo.get(cargo.size() - 1) instanceof IDischargeSlot) {
			return (IDischargeSlot) cargo.get(cargo.size() - 1);
		}
		return null;
	}
	
	private Map<List<IPortSlot>, Integer> getCargoMap(List<List<IPortSlot>> cargoes) {
		Map<List<IPortSlot>, Integer> cargoMap = new HashMap<>();
		for (int i = 0; i < cargoes.size(); i++) {
			List<IPortSlot> cargo = cargoes.get(i);
			cargoMap.put(cargo, i);
		}
		return cargoMap;
	}

	private Map<IVesselAvailability, Integer> getVesselMap(List<IVesselAvailability> vessels) {
		Map<IVesselAvailability, Integer> vesselMap = new HashMap<>();
		for (int i = 0; i < vessels.size(); i++) {
			IVesselAvailability vessel = vessels.get(i);
			vesselMap.put(vessel, i);
		}
		return vesselMap;
	}
	
}
