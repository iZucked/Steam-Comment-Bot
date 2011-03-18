/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.pandl;

import scenario.Scenario;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.contract.Contract;
import scenario.contract.Entity;
import scenario.contract.SalesContract;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;

/**
 * A device which takes a {@link Schedule}, and does some additional computation
 * to compute the profit and loss for the different corporate entities involved.
 * 
 * TODO allow this to be included in the main optimisation loop.
 * 
 * @author hinton
 * 
 */
public class ProfitAndLossCalculator {
	public ProfitAndLossCalculator() {

	}

	public void addProfitAndLoss(final Scenario scenario,
			final Schedule schedule) {
		for (final CargoAllocation cargo : schedule.getCargoAllocations()) {
			final LoadSlot load = cargo.getLoadSlot();
			final Slot discharge = cargo.getDischargeSlot();

			final Contract loadContract = load.getContract();
			final SalesContract dischargeContract = (SalesContract) discharge
					.getContract();

			final Entity loadEntity = loadContract.getEntity();
			final Entity dischargeEntity = dischargeContract.getEntity();

			final Entity shippingEntity = scenario.getContractModel()
					.getShippingEntity();

			// now compute P&L values

			final long loadVolume = cargo.getLoadVolume();
			final long transportCost = cargo.getTotalCost();

			final float salesPriceToMarket = cargo.getDischargePriceM3(); // convert
																			// to
																			// CV
																			// value

			final long dischargeVolume = cargo.getDischargeVolume();
			// how much is left after regas
			final long dischargeVolumeSold = (long) (dischargeVolume * dischargeContract
					.getRegasEfficiency());
			final float revenueToShippingEntity = cargo.getDischargePriceM3()
					* cargo.getDischargeVolume();
			final float revenueToDischargeEntity = dischargeVolumeSold
					* salesPriceToMarket;

			final float profitToDischargeEntity = revenueToDischargeEntity
					- revenueToShippingEntity;

			final float purchaseUnitPrice = 0; // calculate load revenue here

			final float revenueToLoadEntity = loadVolume * purchaseUnitPrice;
			// all load revenue is booked as profit.
			final float profitToShippingEntity = revenueToShippingEntity
					- (transportCost + revenueToLoadEntity);
		}
	}
}
