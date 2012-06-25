/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import java.util.Map;

import com.mmxlabs.common.detailtree.IDetailTree;
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

	public NetbackContract(final int marginScaled, final IMultiMatrixProvider<IPort, Integer> distanceProvider, final Map<IVesselClass, BallastParameters> ballastParameters) {
		super();
		setMarginScaled(marginScaled);
		setDistanceProvider(distanceProvider);
		this.ballastParameters = ballastParameters;
	}

	public NetbackContract(final int marginScaled) {
		super();
		setMarginScaled(marginScaled);
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

		final IVesselClass vesselClass = vessel.getVesselClass();

		final VoyageDetails ladenLeg = (VoyageDetails) plan.getSequence()[1];
		// final VoyageDetails ballastLeg = (VoyageDetails) plan.getSequence()[3];

		// get transportation costs
		// suez cost
		long totalRealTransportCosts = ladenLeg.getRouteCost();
		// fuel cost
		for (final FuelComponent c : FuelComponent.values()) {
			final long fuelConsumption = ladenLeg.getFuelConsumption(c, c.getDefaultFuelUnit()) + ladenLeg.getRouteAdditionalConsumption(c, c.getDefaultFuelUnit());
			totalRealTransportCosts += Calculator.multiply(fuelConsumption, ladenLeg.getFuelUnitPrice(c));
		}

		final BallastParameters notionalBallastParameters = ballastParameters.get(vesselClass);
		// vessel cost (don't use calculator.multiply here; hours are not
		// scaled, but price is)
		final int hireRate;
		switch (vessel.getVesselInstanceType()) {
		case SPOT_CHARTER:
			hireRate = vesselClass.getHourlyCharterInPrice();
			break;
		case TIME_CHARTER:
			hireRate = vessel.getHourlyCharterOutPrice();
			break;
		default:
			hireRate = notionalBallastParameters.getHireCost(dischargeTime);
			break;
		}

		totalRealTransportCosts += (dischargeTime - loadTime) * hireRate;

		final int notionalReturnSpeed = notionalBallastParameters.getSpeed();

		long result = Long.MAX_VALUE;
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
				totalBaseFuelCosts = Calculator.costFromConsumption(notionalBallastParameters.getBaseFuelRate() * notionalTransportTime, notionalFuelCost);
			}

			// NBO Costs
			long totalNBOCosts;
			{
				final long nboInMMBTu = Calculator.convertM3ToMMBTu(notionalBallastParameters.getNBORate(), loadSlot.getCargoCVValue());
				totalNBOCosts = Calculator.costFromConsumption(nboInMMBTu * notionalTransportTime, salesPrice);
			}

			final long notionalTransportCosts = (hireRate * notionalTransportTime) + Math.min(totalBaseFuelCosts, totalNBOCosts);

			final long transportCostPerMMBTU = Calculator.divide(notionalTransportCosts + totalRealTransportCosts, Calculator.multiply(loadSlot.getCargoCVValue(), loadVolume));

			if (transportCostPerMMBTU < result) {
				result = transportCostPerMMBTU;
			}

			if (annotations != null) {
				final IDetailTree tree = annotations.addChild("Netback - " + route, transportCostPerMMBTU);
				tree.addChild("Transport Time", notionalTransportTime);
				tree.addChild("Distance", distance);
				tree.addChild("NBO Costs", totalNBOCosts);
				tree.addChild("Base Costs", totalBaseFuelCosts);
			}
		}
		return (int) (salesPrice - result - marginScaled);
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime, final int salesPrice) {
		// TODO implement me
		throw new RuntimeException("A netback requires shipping costs - not yet handled");
	}

	public Map<IVesselClass, BallastParameters> getBallastParameters() {
		return ballastParameters;
	}

	public void setBallastParameters(final Map<IVesselClass, BallastParameters> ballastParameters) {
		this.ballastParameters = ballastParameters;
	}
}
