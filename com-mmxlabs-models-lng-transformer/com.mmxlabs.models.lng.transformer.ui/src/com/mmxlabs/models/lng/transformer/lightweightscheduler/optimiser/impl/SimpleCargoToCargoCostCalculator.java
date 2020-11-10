/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateFuelCosts;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateVoyageCalculatorHelper;
import com.mmxlabs.scheduler.optimiser.voyage.util.ApproximateVoyageCalculatorHelper.ApproximateFuelCostLegData;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

public class SimpleCargoToCargoCostCalculator implements ICargoToCargoCostCalculator {

	@Inject
	private IDistanceProvider distanceProvider;

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

	private long calculateNonCharterVariableCosts(final ILoadSlot loadA, final IDischargeSlot dischargeA, final IPortSlot vesselEventA, final IPortSlot startSlotB, final IVesselAvailability vessel) {
		assert ((loadA != null && dischargeA != null) || vesselEventA != null);
		final int startA = loadA != null ? loadA.getTimeWindow().getInclusiveStart() : vesselEventA.getTimeWindow().getInclusiveStart();
		final int endA = dischargeA != null ? dischargeA.getTimeWindow().getInclusiveStart()
				: vesselEventA.getTimeWindow().getInclusiveStart() //
						+ elementDurationProvider.getElementDuration(portSlotProvider.getElement(vesselEventA), vesselProvider.getResource(vessel));

		// if discharge is null use the vessel event (as it must be a vessel event)
		final IPort endAPort = getEndPort(dischargeA, vesselEventA);

		if (endAPort == startSlotB.getPort()) {
			return 0;
		}
		@NonNull
		final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTime(vessel.getVessel(), endAPort, //
				startSlotB.getPort(), vessel.getVessel().getMaxSpeed(), AvailableRouteChoices.OPTIMAL);
		// If the travel time is 0 no cost so shortcut
		if (quickestTravelTimeAToB.getSecond() == 0) {
			return 0L;
		}
		final int[] baseFuelPrices = vesselBaseFuelCalculator.getBaseFuelPrices(vessel.getVessel(), startA);

		ApproximateFuelCosts legFuelCosts = null;
		if (loadA != null) {
			assert dischargeA != null;
			final ApproximateFuelCostLegData inputData = new ApproximateFuelCostLegData();
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
			inputData.includeIdleBunkerCosts = true;

			legFuelCosts = ApproximateVoyageCalculatorHelper.getLegFuelCosts(inputData);
		} else {
			final ApproximateFuelCostLegData inputData = new ApproximateFuelCostLegData();
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
			inputData.includeIdleBunkerCosts = true;
			try {
				legFuelCosts = ApproximateVoyageCalculatorHelper.getLegFuelCosts(inputData);
			} catch (final Exception e) {
				ApproximateVoyageCalculatorHelper.getLegFuelCosts(inputData);
			}
		}

		assert legFuelCosts != null;

		return legFuelCosts.getTotalCost() + routeCostProvider.getRouteCost(quickestTravelTimeAToB.getFirst(), vessel.getVessel(), endA, CostType.RoundTripBallast);
	}

	private IPort getEndPort(final IDischargeSlot dischargeA, final IPortSlot vesselEventA) {
		return dischargeA != null ? dischargeA.getPort() : vesselEventA.getPort();
	}

	private IPortSlot getEndPortSlot(final IDischargeSlot dischargeA, final IPortSlot vesselEventA) {
		return dischargeA != null ? dischargeA : vesselEventA;
	}

	public long[][] getCargoCharterCostPerAvailability(final List<List<IPortSlot>> cargoes, final List<IVesselAvailability> vessels) {
		final long[][] costs = new long[cargoes.size()][vessels.size()];
		for (int i = 0; i < cargoes.size(); i++) {
			final List<IPortSlot> cargo = cargoes.get(i);
			for (int j = 0; j < vessels.size(); j++) {
				final IVesselAvailability availability = vessels.get(j);
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
							long cost = calculateNonCharterVariableCosts(loadA, dischargeA, vesselEventA, endSlot, vessel);
							// Sanity check, should it be an exception ?
							assert (cost != Long.MAX_VALUE);

							costs[cargoMap.get(cargoA)][cargoMap.get(cargoB)][vesselMap.get(vessel)] = cost;
						}
					}
				}
			}
		}
		return costs;
	}

	/**
	 * Note: includes duration. Between two consecutive cargoes. Including the contingency.
	 * 
	 * @param cargoes
	 * @param vessels
	 * @return
	 */
	public int[][][] getMinCargoToCargoTravelTimesPerVessel(final List<List<IPortSlot>> cargoes, final List<IVesselAvailability> vessels) {
		final Map<List<IPortSlot>, Integer> cargoMap = getCargoMap(cargoes);
		final Map<IVesselAvailability, Integer> vesselMap = getVesselMap(vessels);
		final int[][][] times = new int[cargoes.size()][cargoes.size()][vessels.size()];
		for (final List<IPortSlot> cargoA : cargoes) {
			final IDischargeSlot dischargeA = getDischargeSlot(cargoA);
			final IPortSlot vesselEventA = getVesselEvent(cargoA);
			if (dischargeA != null || vesselEventA != null) {
				// if discharge is null use the vessel event (as it must be a vessel event)

				final IPortSlot endAPortSlot = getEndPortSlot(dischargeA, vesselEventA);
				for (final List<IPortSlot> cargoB : cargoes) {
					if (cargoA == cargoB) {
						continue;
					}
					for (final IVesselAvailability vessel : vessels) {
						final int endADuration = dischargeA != null ? elementDurationProvider.getElementDuration(portSlotProvider.getElement(dischargeA), vesselProvider.getResource(vessel)) : 0;
						IPortSlot startB = cargoB.get(0);

						if (startB instanceof IVesselEventPortSlot) {
							final IVesselEventPortSlot iVesselEventPortSlot = (IVesselEventPortSlot) startB;

							startB = iVesselEventPortSlot.getEventPortSlots().get(0);
						}

						@NonNull
						final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTimeWithContingency(vessel.getVessel(), endAPortSlot, startB,
								vessel.getVessel().getMaxSpeed(), AvailableRouteChoices.OPTIMAL);

						times[cargoMap.get(cargoA)][cargoMap.get(cargoB)][vesselMap.get(vessel)] = quickestTravelTimeAToB.getSecond() + endADuration;
					}
				}
			}
		}
		return times;
	}

	/**
	 * Note: includes duration. Between load and discharge slots within the cargo. Including the contingency.
	 * 
	 * @param cargoes
	 * @param vessels
	 * @return
	 */
	public int[][] getMinCargoStartToEndSlotTravelTimesPerVessel(final List<List<IPortSlot>> cargoes, final List<IVesselAvailability> vessels) {
		final Map<List<IPortSlot>, Integer> cargoMap = getCargoMap(cargoes);
		final Map<IVesselAvailability, Integer> vesselMap = getVesselMap(vessels);
		final int[][] times = new int[cargoes.size()][vessels.size()];
		for (final List<IPortSlot> cargoA : cargoes) {
			final ILoadSlot loadA = getLoadSlot(cargoA);
			final IDischargeSlot dischargeA = getDischargeSlot(cargoA);
			final IPortSlot vesselEventA = getVesselEvent(cargoA);

			for (final IVesselAvailability vessel : vessels) {
				if (vesselEventA != null) {
					final int duration = elementDurationProvider.getElementDuration(portSlotProvider.getElement(vesselEventA), vesselProvider.getResource(vessel));
					times[cargoMap.get(cargoA)][vesselMap.get(vessel)] = duration;
				} else {
					assert loadA != null;
					assert dischargeA != null;
					final @NonNull Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTimeWithContingency(vessel.getVessel(), loadA, dischargeA,
							vessel.getVessel().getMaxSpeed(), AvailableRouteChoices.OPTIMAL);

					times[cargoMap.get(cargoA)][vesselMap.get(vessel)] = quickestTravelTimeAToB.getSecond()
							+ elementDurationProvider.getElementDuration(portSlotProvider.getElement(loadA), vesselProvider.getResource(vessel));
				}
			}
		}
		return times;
	}

	public int[] getCargoStartSlotDurations(final List<List<IPortSlot>> cargoes) {
		final int[] durations = new int[cargoes.size()];
		for (int i = 0; i < cargoes.size(); i++) {
			final List<IPortSlot> cargo = cargoes.get(i);
			durations[i] = elementDurationProvider.getElementDuration(portSlotProvider.getElement(cargo.get(0)));
		}
		return durations;
	}

	public int[] getCargoEndSlotDurations(final List<List<IPortSlot>> cargoes) {
		final int[] durations = new int[cargoes.size()];
		for (int i = 0; i < cargoes.size(); i++) {
			final List<IPortSlot> cargo = cargoes.get(i);
			final IPortSlot lastSlot = cargo.get(cargo.size() - 1);
			if (lastSlot.getPortType() == PortType.Load || lastSlot.getPortType() == PortType.Discharge) {
				durations[i] = elementDurationProvider.getElementDuration(portSlotProvider.getElement(cargo.get(cargo.size() - 1)));
			} else {
				// durations will be encoded on the start slot for vessel events
				durations[i] = 0;
			}
		}
		return durations;
	}

	private ILoadSlot getLoadSlot(final List<IPortSlot> cargo) {
		for (final IPortSlot portSlot : cargo) {
			if (portSlot instanceof ILoadSlot) {
				return (ILoadSlot) portSlot;
			}
		}
		return null;
	}

	private IPortSlot getVesselEvent(final List<IPortSlot> cargo) {

		for (int i = 0; i < cargo.size(); ++i) {
			if (cargo.get(i).getPortType() == PortType.CharterOut //
					|| cargo.get(i).getPortType() == PortType.DryDock //
					|| cargo.get(i).getPortType() == PortType.Maintenance) {
				return cargo.get(i);
			}
		}
		return null;
	}

	private IDischargeSlot getDischargeSlot(final List<IPortSlot> cargo) {
		if (cargo.get(cargo.size() - 1) instanceof IDischargeSlot) {
			return (IDischargeSlot) cargo.get(cargo.size() - 1);
		}
		return null;
	}

	private Map<List<IPortSlot>, Integer> getCargoMap(final List<List<IPortSlot>> cargoes) {
		final Map<List<IPortSlot>, Integer> cargoMap = new HashMap<>();
		for (int i = 0; i < cargoes.size(); i++) {
			final List<IPortSlot> cargo = cargoes.get(i);
			cargoMap.put(cargo, i);
		}
		return cargoMap;
	}

	private Map<IVesselAvailability, Integer> getVesselMap(final List<IVesselAvailability> vessels) {
		final Map<IVesselAvailability, Integer> vesselMap = new HashMap<>();
		for (int i = 0; i < vessels.size(); i++) {
			final IVesselAvailability vessel = vessels.get(i);
			vesselMap.put(vessel, i);
		}
		return vesselMap;
	}

}
