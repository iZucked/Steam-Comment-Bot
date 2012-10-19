/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import java.util.Map;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.CurrencyDetailElement;
import com.mmxlabs.common.detailtree.impl.DurationDetailElement;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Computes sales price with netbacks.
 * 
 * Netback purchase price = actual sales price - (real transportation costs from laden leg + notional transport cost from return ballast leg) - margin per mmbtu
 * 
 * @author hinton
 * 
 */
public class NetbackContract implements ILoadPriceCalculator {
	private int marginScaled;
	private int floorPriceScaled;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	private Map<IVesselClass, BallastParameters> ballastParameters;

	public final int getMarginScaled() {
		return marginScaled;
	}

	public final void setMarginScaled(final int marginScaled) {
		this.marginScaled = marginScaled;
	}

	public final IMultiMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public final void setDistanceProvider(final IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public NetbackContract(final int marginScaled, final int floorPriceScaled, final IMultiMatrixProvider<IPort, Integer> distanceProvider, final Map<IVesselClass, BallastParameters> ballastParameters) {
		super();
		setMarginScaled(marginScaled);
		setFloorPriceScaled(floorPriceScaled);
		setDistanceProvider(distanceProvider);
		this.ballastParameters = ballastParameters;
	}

	public NetbackContract(final int marginScaled, final int floorPriceScaled) {
		super();
		setMarginScaled(marginScaled);
		setFloorPriceScaled(floorPriceScaled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2#prepareEvaluation(com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences)
	 */
	@Override
	public void prepareEvaluation(final ScheduledSequences sequences) {

	}

	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int salesPrice, final int loadVolume,
			final IVessel vessel, final VoyagePlan plan, final IDetailTree annotations) {

		return calculateLoadUnitPrice(loadSlot, dischargeSlot, loadTime, dischargeTime, salesPrice, loadVolume, vessel, plan, annotations, true);
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime, final int salesPrice, IDetailTree annotations) {

		return calculateLoadUnitPrice(loadOption, dischargeOption, loadTime, dischargeTime, salesPrice, -1, null, null, annotations, false);
	}

	public Map<IVesselClass, BallastParameters> getBallastParameters() {
		return ballastParameters;
	}

	public void setBallastParameters(final Map<IVesselClass, BallastParameters> ballastParameters) {
		this.ballastParameters = ballastParameters;
	}

	public int getFloorPriceScaled() {
		return floorPriceScaled;
	}

	public void setFloorPriceScaled(int floorPriceScaled) {
		this.floorPriceScaled = floorPriceScaled;
	}

	private int calculateLoadUnitPrice(final ILoadOption loadSlot, final IDischargeOption dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePricePerM3,
			final int loadVolume, final IVessel vessel, final VoyagePlan plan, final IDetailTree annotations, boolean includeShipping) {

		long result = Long.MAX_VALUE;

		if (includeShipping) {
			final IVesselClass vesselClass = vessel.getVesselClass();

			final VoyageDetails ladenLeg = (VoyageDetails) plan.getSequence()[1];
			// final VoyageDetails ballastLeg = (VoyageDetails) plan.getSequence()[3];

			// get transportation costs
			// suez cost
			long totalRealTransportCosts = ladenLeg.getRouteCost();
			// fuel cost
			for (final FuelComponent c : FuelComponent.values()) {
				final long fuelConsumption = ladenLeg.getFuelConsumption(c, c.getDefaultFuelUnit()) + ladenLeg.getRouteAdditionalConsumption(c, c.getDefaultFuelUnit());
				totalRealTransportCosts += Calculator.costFromConsumption(fuelConsumption, ladenLeg.getFuelUnitPrice(c));
			}

			final BallastParameters notionalBallastParameters = ballastParameters.get(vesselClass);
			// vessel cost (don't use calculator.multiply here; hours are not
			// scaled, but price is)
			final ICurve hireRate;
			switch (vessel.getVesselInstanceType()) {
			case SPOT_CHARTER:
				hireRate = vessel.getHourlyCharterInPrice();
				break;
			case TIME_CHARTER:
				hireRate = vessel.getHourlyCharterInPrice();
				break;
			default:
				hireRate = new ConstantValueCurve(notionalBallastParameters.getHireCost(dischargeTime));
				break;
			}

			if (hireRate != null) {
				totalRealTransportCosts += Calculator.quantityFromRateTime(hireRate.getValueAtPoint(dischargeTime), dischargeTime - loadTime);
			}
			final int notionalReturnSpeed = notionalBallastParameters.getSpeed();

			for (final String route : notionalBallastParameters.getRoutes()) {

				final Integer distance = distanceProvider.get(route).get(ladenLeg.getOptions().getToPortSlot().getPort(), ladenLeg.getOptions().getFromPortSlot().getPort());
				if (distance == null) {
					continue;
				}

				final int notionalTransportTime = Calculator.getTimeFromSpeedDistance(notionalReturnSpeed, distance);

				// TODO: Add in canal costs etc

				// Base Fuel Costs
				long totalBaseFuelCosts;
				{
					final long notionalFuelCost = vesselClass.getBaseFuelUnitPrice();
					final long totalFuelInMT = Calculator.quantityFromRateTime(notionalBallastParameters.getBaseFuelRate(), notionalTransportTime);
					totalBaseFuelCosts = Calculator.costFromConsumption(totalFuelInMT, notionalFuelCost);
				}

				// NBO Costs
				long totalNBOCosts;
				{
					final long nboInMMBTuPerDay = Calculator.convertM3ToMMBTu(notionalBallastParameters.getNBORate(), loadSlot.getCargoCVValue());
					final long totalNBOInMMBtu = Calculator.quantityFromRateTime(nboInMMBTuPerDay, notionalTransportTime);
					totalNBOCosts = Calculator.costFromConsumption(totalNBOInMMBtu, dischargePricePerM3);
				}

				long notionalTransportCosts = Math.min(totalBaseFuelCosts, totalNBOCosts);
				if (hireRate != null) {
					notionalTransportCosts += Calculator.quantityFromRateTime(hireRate.getValueAtPoint(dischargeTime), notionalTransportTime);
				}

				final long transportCostPerMMBTU = Calculator.getPerMMBTuFromTotalAndVolumeInM3(notionalTransportCosts + totalRealTransportCosts, loadVolume, loadSlot.getCargoCVValue());

				if (transportCostPerMMBTU < result) {
					result = transportCostPerMMBTU;
				}

				if (annotations != null) {
					final IDetailTree tree = annotations.addChild("Netback - " + route, transportCostPerMMBTU);
					tree.addChild("Transport Time", new DurationDetailElement(notionalTransportTime));
					tree.addChild("Distance", distance);
					tree.addChild("NBO Costs", new CurrencyDetailElement(totalNBOCosts));
					tree.addChild("Base Costs", new CurrencyDetailElement(totalBaseFuelCosts));
				}
			}

		} else {
			result = 0;
		}
		int rawPrice = (int) (dischargePricePerM3 - result - marginScaled);
		if (rawPrice < floorPriceScaled) {
			return floorPriceScaled;
		}
		return rawPrice;
	}
}
