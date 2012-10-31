/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Redirection contract. A combination of both the {@link NetbackContract} and {@link ProfitSharingContract}.
 * 
 * @authosr Simon Goodall
 * 
 * @since 2.0
 */
public class RedirectionContract implements ILoadPriceCalculator {

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	private final ICurve purchasePriceCurve;
	private final ICurve salesPriceCurve;
	private final int notionalSpeed;
	private final IPort baseMarketPort;

	/**
	 * @since 2.0
	 */
	public RedirectionContract(final ICurve purchasePriceCurve, final ICurve salesPriceCurve, final int notionalSpeed, final IPort baseMarketPort) {
		super();
		this.purchasePriceCurve = purchasePriceCurve;
		this.salesPriceCurve = salesPriceCurve;
		this.notionalSpeed = notionalSpeed;
		this.baseMarketPort = baseMarketPort;
	}

	@Override
	public void prepareEvaluation(final ScheduledSequences sequences) {
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int actualSalesPricePerMMBTu,
			final int loadVolumeInM3, final IVessel vessel, final VoyagePlan plan, final IDetailTree annotation) {
		final int marketPurchasePricePerMMBTu = purchasePriceCurve.getValueAtPoint(loadTime);
		final int cargoCVValue = loadSlot.getCargoCVValue();
		final int actualSalesPricePerM3 = Calculator.costPerM3FromMMBTu(actualSalesPricePerMMBTu, cargoCVValue);
		final int marketPurchasePricePerM3 = Calculator.costPerM3FromMMBTu(marketPurchasePricePerMMBTu, cargoCVValue);

		if (baseMarketPort.equals(dischargeSlot.getPort())) {
			return marketPurchasePricePerMMBTu;
		} else {

			// Notional Costs
			long totalNotionalPNL = 0;
			{
				final VoyagePlan notionalPlan = new VoyagePlan();

				// TODO: Iterate over route choices
				// Direct distance/
				// TODO: Iterate over routes - however this changes the travelTime!
				final String route = "Direct";
				final Integer distance = distanceProvider.get(route).get(loadSlot.getPort(), baseMarketPort);

				// Generate a notional voyage plan
				final int travelTime = Calculator.getTimeFromSpeedDistance(notionalSpeed, distance);
				final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
				final IResource resource = vesselProvider.getResource(vessel);
				final int loadDuration = durationProvider.getElementDuration(loadElement, resource);

				// TODO: Extend API to contain port default
				final int notionalDischargeDuration = durationProvider.getDefaultValue();

				// Determine notional port visit times.
				final int notionalDischargeTime = loadTime + loadDuration + travelTime;
				final int notionalReturnTime = notionalDischargeTime + notionalDischargeDuration + travelTime;
				final int[] arrivalTimes = new int[] { loadTime, notionalDischargeTime, notionalReturnTime };

				final DischargeSlot notionalDischargeSlot = new DischargeSlot();
				notionalDischargeSlot.setPort(baseMarketPort);
				notionalDischargeSlot.setTimeWindow(new TimeWindow(notionalDischargeTime, notionalDischargeTime));
				notionalDischargeSlot.setDischargePriceCalculator(new MarketPriceContract(salesPriceCurve, 0, OptimiserUnitConvertor.convertToInternalConversionFactor(1)));

				final PortSlot notionalReturnSlot = new EndPortSlot();
				notionalReturnSlot.setPort(loadSlot.getPort());
				notionalReturnSlot.setTimeWindow(new TimeWindow(notionalReturnTime, notionalReturnTime));

				// Calculate new voyage requirements
				// TODO: Optimise over fuel choices using a VPO?
				{
					final VoyageDetails ladenDetails = new VoyageDetails();
					{
						final VoyageOptions ladenOptions = new VoyageOptions();
						ladenOptions.setAvailableTime(travelTime);
						ladenOptions.setAllowCooldown(false);
						ladenOptions.setAvailableLNG(travelTime);
						ladenOptions.setDistance(distance);
						ladenOptions.setFromPortSlot(loadSlot);
						ladenOptions.setNBOSpeed(notionalSpeed);
						ladenOptions.setRoute(route);
						ladenOptions.setShouldBeCold(true);
						ladenOptions.setToPortSlot(notionalDischargeSlot);
						ladenOptions.setUseFBOForSupplement(true);
						ladenOptions.setUseNBOForIdle(true);
						ladenOptions.setUseNBOForTravel(true);
						ladenOptions.setVessel(vessel);
						ladenOptions.setVesselState(VesselState.Laden);
						ladenOptions.setWarm(false);

						voyageCalculator.calculateVoyageFuelRequirements(ladenOptions, ladenDetails);
					}
					final VoyageDetails ballastDetails = new VoyageDetails();
					{
						final VoyageOptions ballastOptions = new VoyageOptions();
						ballastOptions.setAvailableTime(travelTime);
						ballastOptions.setAllowCooldown(false);
						ballastOptions.setAvailableLNG(travelTime);
						ballastOptions.setDistance(distance);
						ballastOptions.setFromPortSlot(notionalDischargeSlot);
						ballastOptions.setNBOSpeed(notionalSpeed);
						ballastOptions.setRoute(route);
						ballastOptions.setShouldBeCold(true);
						ballastOptions.setToPortSlot(notionalReturnSlot);
						ballastOptions.setUseFBOForSupplement(true);
						ballastOptions.setUseNBOForIdle(true);
						ballastOptions.setUseNBOForTravel(true);
						ballastOptions.setVessel(vessel);
						ballastOptions.setVesselState(VesselState.Ballast);
						ballastOptions.setWarm(false);

						voyageCalculator.calculateVoyageFuelRequirements(ballastOptions, ballastDetails);
					}
					final PortDetails loadDetails = new PortDetails();
					loadDetails.setPortSlot(loadSlot);
					loadDetails.setVisitDuration(loadDuration);
					final PortDetails dischargeDetails = new PortDetails();
					dischargeDetails.setPortSlot(notionalDischargeSlot);
					dischargeDetails.setVisitDuration(notionalDischargeDuration);
					final PortDetails returnDetails = new PortDetails();
					returnDetails.setPortSlot(notionalReturnSlot);
					returnDetails.setVisitDuration(0);

					final Object[] sequence = new Object[] { loadDetails, ladenDetails, dischargeDetails, ballastDetails, returnDetails };
					notionalPlan.setSequence(sequence);
					voyageCalculator.calculateVoyagePlan(notionalPlan, vessel, arrivalTimes, sequence);
				}
				final long totalShippingCost = entityValueCalculator.getShippingCosts(notionalPlan, vessel, false, loadTime, null);

				// TODO: We are using actual load - so we should ensure this is still positive as the consumed LNG volume may be different.
				final long dischargeVolumeInM3 = loadVolumeInM3 - notionalPlan.getLNGFuelVolume();
				final int notionalSalesPricePerMMBTu = salesPriceCurve.getValueAtPoint(notionalDischargeTime);
				final int notionalSalesPricePerM3 = Calculator.costPerM3FromMMBTu(notionalSalesPricePerMMBTu, cargoCVValue);

				final long totalSalesRevenue = Calculator.costFromConsumption(dischargeVolumeInM3, notionalSalesPricePerM3);
				final long totalPurchaseCosts = Calculator.costFromConsumption(loadVolumeInM3, marketPurchasePricePerM3);

				totalNotionalPNL = totalSalesRevenue - totalShippingCost - totalPurchaseCosts;
			}
			long totalActualPNL = 0;
			{
				final long totalShippingCost = entityValueCalculator.getShippingCosts(plan, vessel, false, loadTime, null);
				final long dischargeVolumeInM3 = loadVolumeInM3 - plan.getLNGFuelVolume();
				final long totalSalesRevenue = Calculator.costFromConsumption(dischargeVolumeInM3, actualSalesPricePerM3);
				final long totalPurchaseCosts = Calculator.costFromConsumption(loadVolumeInM3, marketPurchasePricePerM3);

				totalActualPNL = totalSalesRevenue - totalShippingCost - totalPurchaseCosts;
			}

			// Profit Share
			int profitSharePerMMBTu = 0;
			// Clamp to [0, infinity)
			final long diff = Math.max(0, totalActualPNL - totalNotionalPNL);
			if (diff > 0) {
				profitSharePerMMBTu = Calculator.getPerMMBTuFromTotalAndVolumeInM3(diff, loadVolumeInM3, cargoCVValue);
			}

			return marketPurchasePricePerMMBTu + profitSharePerMMBTu;
		}
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime, final int actualSalesPrice,
			final IDetailTree annotations) {
		return 0;
	}
}
