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
import scenario.schedule.BookedRevenue;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;

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
	final SchedulePackage schedulePackage = SchedulePackage.eINSTANCE;
	final ScheduleFactory scheduleFactory = schedulePackage
			.getScheduleFactory();

	public ProfitAndLossCalculator() {

	}

	public void addProfitAndLoss(final Scenario scenario,
			final Schedule schedule) {
		for (final CargoAllocation allocation : schedule.getCargoAllocations()) {
			final LoadSlot loadSlot = allocation.getLoadSlot();
			final Slot dischargeSlot = allocation.getDischargeSlot();

			final Contract loadContract = loadSlot.getContract();
			final SalesContract dischargeContract = (SalesContract) dischargeSlot
					.getContract();

			final Entity loadEntity = loadContract.getEntity();
			final Entity dischargeEntity = dischargeContract.getEntity();

			final Entity shippingEntity = scenario.getContractModel()
					.getShippingEntity();

			final BookedRevenue dischargeRevenue = scheduleFactory
					.createBookedRevenue();
			final BookedRevenue shippingRevenue = scheduleFactory
					.createBookedRevenue();
			final BookedRevenue loadRevenue = scheduleFactory
					.createBookedRevenue();

			dischargeRevenue.setEntity(dischargeEntity);
			shippingRevenue.setEntity(shippingEntity);
			loadRevenue.setEntity(loadEntity);

			schedule.getRevenue().add(loadRevenue);
			schedule.getRevenue().add(shippingRevenue);
			schedule.getRevenue().add(dischargeRevenue);

			// item 1; value of selling LNG
			final long dischargedM3 = allocation.getDischargeVolume();
			final long saleableM3 = (long) Math.floor(allocation
					.getDischargeVolume()
					* dischargeContract.getRegasEfficiency());

			// divide by CV value to get mmbtu
			final long saleableMMBTU = (long) Math.floor(saleableM3
					/ loadSlot.getCargoCVvalue());

			// need base date to get sales value.
			final long salesValue = dischargeContract.getMarket()
					.getPriceCurve().getValueAtDate(something);
		}
	}
}
