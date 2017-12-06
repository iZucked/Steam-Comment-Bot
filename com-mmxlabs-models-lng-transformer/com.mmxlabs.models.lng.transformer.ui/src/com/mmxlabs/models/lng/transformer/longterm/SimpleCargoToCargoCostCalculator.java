/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public class SimpleCargoToCargoCostCalculator implements ICargoToCargoCostCalculator {

	@Inject
	IDistanceProvider distanceProvider;
	
	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;
	
	@Inject
	private IRouteCostProvider routeCostProvider;
	
	@Inject
	private IPortVisitDurationProvider portVisitDurationProvider;
	
	@Inject
	private IElementDurationProvider elementDurationProvider;
	
	@Inject
	private IPortSlotProvider portSlotProvider;
	
	@Inject
	private IVesselProvider vesselProvider;
	
	@Override
	public long calculateNonCharterVariableCosts(final ILoadSlot loadA, final IDischargeSlot dischargeA, final ILoadSlot loadB, final IDischargeSlot dischargeB, final IVessel vessel) {
		final ITimeWindow startA = loadA.getTimeWindow();
		final ITimeWindow endA = dischargeA.getTimeWindow();
		final ITimeWindow startB = loadB.getTimeWindow();
		final ITimeWindow endB = dischargeB.getTimeWindow();

		@NonNull
		final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTime(vessel, dischargeA.getPort(), loadB.getPort(), vessel.getMaxSpeed(),
				AvailableRouteChoices.OPTIMAL);
		final int salesPrice = dischargeA.getDischargePriceCalculator().getEstimatedSalesPrice(loadA, dischargeB, endA.getInclusiveStart());
		final int[] baseFuelPrices = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, startA.getInclusiveStart());		
		final long[] legFuelCosts = PriceIntervalProviderHelper.getLegFuelCosts(salesPrice, vessel.getNBORate(VesselState.Ballast), vessel, loadA.getCargoCVValue(),
				new int[] { endA.getInclusiveStart(), endA.getInclusiveStart() + quickestTravelTimeAToB.getSecond() },
				distanceProvider.getDistance(quickestTravelTimeAToB.getFirst(), dischargeA.getPort(), loadB.getPort(), vessel), vessel.getTravelBaseFuel().getEquivalenceFactor(),
				baseFuelPrices, routeCostProvider.getRouteTransitTime(quickestTravelTimeAToB.getFirst(), vessel),
				portVisitDurationProvider.getVisitDuration(dischargeA.getPort(), PortType.Discharge), false);

		return LongStream.of(legFuelCosts).sum() + routeCostProvider.getRouteCost(quickestTravelTimeAToB.getFirst(), vessel, endA.getInclusiveStart(), CostType.RoundTripBallast);
	}

	@Override
	public Long[][][] createCargoToCargoCostMatrix(final List<List<IPortSlot>> cargoes, final List<IVesselAvailability> vessels) {
		final Map<List<IPortSlot>, Integer> cargoMap = getCargoMap(cargoes);
		final Map<IVesselAvailability, Integer> vesselMap = getVesselMap(vessels);
		final Long[][][] costs = new Long[cargoes.size()][cargoes.size()][vessels.size()];
		for (final List<IPortSlot> cargoA : cargoes) {
			final ILoadSlot loadA = getLoadSlot(cargoA);
			final IDischargeSlot dischargeA = getDischargeSlot(cargoA);
			if (loadA != null && dischargeA != null) {
				for (final List<IPortSlot> cargoB : cargoes) {
					for (final IVesselAvailability vessel : vessels) {
						final ILoadSlot loadB = getLoadSlot(cargoB);
						final IDischargeSlot dischargeB = getDischargeSlot(cargoB);
						final long cost = calculateNonCharterVariableCosts(loadA, dischargeA, loadB, dischargeB, vessel.getVessel());
						costs[cargoMap.get(cargoA)][cargoMap.get(cargoB)][vesselMap.get(vessel)] = cost;
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
			ILoadSlot loadA = getLoadSlot(cargoA);
			IDischargeSlot dischargeA = getDischargeSlot(cargoA);
			if (loadA != null && dischargeA != null) {
				for (List<IPortSlot> cargoB : cargoes) {
					for (IVesselAvailability vessel : vessels) {
						ILoadSlot loadB = getLoadSlot(cargoB);
						@NonNull
						final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTime(vessel.getVessel(), dischargeA.getPort(), loadB.getPort(),
								vessel.getVessel().getMaxSpeed(), AvailableRouteChoices.OPTIMAL);
						times[cargoMap.get(cargoA)][cargoMap.get(cargoB)][vesselMap.get(vessel)] = quickestTravelTimeAToB.getSecond()
								+ elementDurationProvider.getElementDuration(portSlotProvider.getElement(dischargeA), vesselProvider.getResource(vessel));
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
			for (IVesselAvailability vessel : vessels) {
				@NonNull
				Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTimeAToB = distanceProvider.getQuickestTravelTime(vessel.getVessel(), loadA.getPort(), dischargeA.getPort(),
						vessel.getVessel().getMaxSpeed(), AvailableRouteChoices.OPTIMAL);

				times[cargoMap.get(cargoA)][vesselMap.get(vessel)] = quickestTravelTimeAToB.getSecond()
						+ elementDurationProvider.getElementDuration(portSlotProvider.getElement(loadA), vesselProvider.getResource(vessel));
			}
		}
		return times;
	}

	private ILoadSlot getLoadSlot(List<IPortSlot> cargo) {
		for (IPortSlot portSlot : cargo) {
			if (portSlot instanceof ILoadSlot) {
				return (ILoadSlot) portSlot;
			}
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
