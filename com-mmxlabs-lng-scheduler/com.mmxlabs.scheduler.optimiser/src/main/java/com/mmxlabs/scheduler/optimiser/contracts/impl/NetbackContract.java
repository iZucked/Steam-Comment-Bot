/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
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
public class NetbackContract implements ILoadPriceCalculator2 {
	private int marginScaled;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	public final int getMarginScaled() {
		return marginScaled;
	}

	public final void setMarginScaled(int marginScaled) {
		this.marginScaled = marginScaled;
	}

	public final IMultiMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public final void setDistanceProvider(IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public NetbackContract(int marginScaled, final IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		super();
		setMarginScaled(marginScaled);
		setDistanceProvider(distanceProvider);
	}

	/**
	 * create a blank netback contract
	 */
	public NetbackContract() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2#prepareEvaluation(com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences)
	 */
	@Override
	public void prepareEvaluation(ScheduledSequences sequences) {

	}

	@Override
	public int calculateLoadUnitPrice(ILoadSlot loadSlot, IDischargeSlot dischargeSlot, int loadTime, int dischargeTime, int salesPrice, int loadVolume, IVesselClass vesselClass, VoyagePlan plan) {
		final VoyageDetails ladenLeg = (VoyageDetails) plan.getSequence()[1];
//		final VoyageDetails ballastLeg = (VoyageDetails) plan.getSequence()[3];

		// get transportation costs
		// suez cost
		long totalRealTransportCosts = ladenLeg.getRouteCost();
		// fuel cost
		for (final FuelComponent c : FuelComponent.values()) {
			totalRealTransportCosts += Calculator.multiply(ladenLeg.getFuelConsumption(c, c.getDefaultFuelUnit()), ladenLeg.getFuelUnitPrice(c));
		}
		// vessel cost (don't use calculator.multiply here; hours are not
		// scaled, but price is)
		totalRealTransportCosts += (dischargeTime - loadTime) * vesselClass.getHourlyCharterInPrice();

		final int notionalReturnSpeed = vesselClass.getMaxSpeed(); // fair?

		// notional transport time is for maximum distance.

		// TODO cache this; it is determined by (from port, to port, vessel class,
		// load unit price)
		// TODO should notional return ever go via a canal?
		final int notionalTransportTime = Calculator.getTimeFromSpeedDistance(notionalReturnSpeed,
				distanceProvider.getMaximumValue(ladenLeg.getOptions().getToPortSlot().getPort(), ladenLeg.getOptions().getFromPortSlot().getPort()));

		/**
		 * the notional cost of one MT equivalent of the cheapest fuel.
		 */
		final long notionalFuelCost = Math.min(vesselClass.getBaseFuelUnitPrice(), Calculator.multiply(salesPrice, vesselClass.getBaseFuelConversionFactor()));
		/**
		 * The notional cost of fuel = cost of one MT equivalent of cheapest fuel * hourly fuel usage at return speed * transport time
		 */
		final long totalNotionalFuelCost = Calculator.costFromConsumption(vesselClass.getConsumptionRate(VesselState.Ballast).getRate(notionalReturnSpeed) * notionalTransportTime, notionalFuelCost);

		final long notionalTransportCosts = (vesselClass.getHourlyCharterInPrice() * notionalTransportTime) + totalNotionalFuelCost;

		final long transportCostPerMMBTU = Calculator.divide(notionalTransportCosts + totalRealTransportCosts, Calculator.multiply(loadSlot.getCargoCVValue(), loadVolume));

		int result = (int) (salesPrice - transportCostPerMMBTU - marginScaled);
		// System.err.println("netback cost " + actualSalesPrice + " => " + result);
		return result;
	}
}
