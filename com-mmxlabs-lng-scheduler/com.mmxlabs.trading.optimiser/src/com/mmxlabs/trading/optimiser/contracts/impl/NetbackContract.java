/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import java.util.Map;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.DurationDetailElement;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
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

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int salesPricePerMMBTu,
			final long loadVolumeInM3, final long dischargeVolumeInM3, final IVessel vessel, final VoyagePlan plan, final IDetailTree annotations) {

		return calculateLoadUnitPrice(loadSlot, dischargeSlot, loadTime, dischargeTime, salesPricePerMMBTu, loadVolumeInM3, vessel, plan, annotations, true);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int transferTime, final int salesPricePerMMBTu, final long transferVolumeInM3,
			final IDetailTree annotations) {

		return calculateLoadUnitPrice(loadOption, dischargeOption, transferTime, transferTime, salesPricePerMMBTu, transferVolumeInM3, null, null, annotations, false);
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

	public void setFloorPriceScaled(final int floorPriceScaled) {
		this.floorPriceScaled = floorPriceScaled;
	}

	private int calculateLoadUnitPrice(final ILoadOption loadSlot, final IDischargeOption dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePricePerMMBTu,
			final long loadVolumeInM3, final IVessel vessel, final VoyagePlan plan, final IDetailTree annotations, final boolean includeShipping) {

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
			final ICurve hireRatePerHour;
			switch (vessel.getVesselInstanceType()) {
			case SPOT_CHARTER:
				hireRatePerHour = vessel.getHourlyCharterInPrice();
				break;
			case TIME_CHARTER:
				hireRatePerHour = vessel.getHourlyCharterInPrice();
				break;
			default:
				hireRatePerHour = new ConstantValueCurve(notionalBallastParameters.getHireCost(dischargeTime));
				break;
			}

			if (hireRatePerHour != null) {
				totalRealTransportCosts += Calculator.quantityFromRateTime(hireRatePerHour.getValueAtPoint(dischargeTime), dischargeTime - loadTime);
			}
			final int notionalReturnSpeed = notionalBallastParameters.getSpeed();

			for (final String route : notionalBallastParameters.getRoutes()) {

				final Integer distance = distanceProvider.get(route).get(ladenLeg.getOptions().getToPortSlot().getPort(), ladenLeg.getOptions().getFromPortSlot().getPort());
				if (distance == null) {
					continue;
				}

				final int notionalTransportTimeInHours = Calculator.getTimeFromSpeedDistance(notionalReturnSpeed, distance);

				// TODO: Add in canal costs etc

				// Base Fuel Costs
				long totalBaseFuelCosts;
				{
					final long notionalFuelCost = vesselClass.getBaseFuelUnitPrice();
					final long totalFuelInMT = Calculator.quantityFromRateTime(notionalBallastParameters.getBaseFuelRate(), notionalTransportTimeInHours);
					totalBaseFuelCosts = Calculator.costFromConsumption(totalFuelInMT, notionalFuelCost);
				}

				// NBO Costs
				long totalNBOCosts;
				{
					final long nboInMMBTuPerDay = Calculator.convertM3ToMMBTu(notionalBallastParameters.getNBORate(), loadSlot.getCargoCVValue());
					final long totalNBOInMMBtu = Calculator.quantityFromRateTime(nboInMMBTuPerDay, notionalTransportTimeInHours);
					totalNBOCosts = Calculator.costFromConsumption(totalNBOInMMBtu, dischargePricePerMMBTu);
				}

				long notionalTransportCosts = Math.min(totalBaseFuelCosts, totalNBOCosts);
				if (hireRatePerHour != null) {
					notionalTransportCosts += Calculator.quantityFromRateTime(hireRatePerHour.getValueAtPoint(dischargeTime), notionalTransportTimeInHours);
				}

				final long transportCostPerMMBTU = Calculator.getPerMMBTuFromTotalAndVolumeInM3(notionalTransportCosts + totalRealTransportCosts, loadVolumeInM3, loadSlot.getCargoCVValue());

				if (transportCostPerMMBTU < result) {
					result = transportCostPerMMBTU;
				}

				if (annotations != null) {
					final IDetailTree tree = annotations.addChild("Netback - " + route, transportCostPerMMBTU);
					tree.addChild("Transport Time", new DurationDetailElement(notionalTransportTimeInHours));
					tree.addChild("Distance", distance);
					tree.addChild("NBO Costs", new TotalCostDetailElement(totalNBOCosts));
					tree.addChild("Base Costs", new TotalCostDetailElement(totalBaseFuelCosts));
				}
			}

		} else {
			result = 0;
		}
		final int rawPricePerMMBTu = (int) (dischargePricePerMMBTu - result - marginScaled);
		if (rawPricePerMMBTu < floorPriceScaled) {
			return floorPriceScaled;
		}
		return rawPricePerMMBTu;
	}
}
