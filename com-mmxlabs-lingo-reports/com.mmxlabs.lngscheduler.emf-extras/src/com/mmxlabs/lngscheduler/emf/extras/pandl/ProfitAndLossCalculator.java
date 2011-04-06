/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.pandl;

import scenario.Scenario;
import scenario.cargo.CargoType;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.contract.Contract;
import scenario.contract.Entity;
import scenario.contract.SalesContract;
import scenario.schedule.BookedRevenue;
import scenario.schedule.CargoAllocation;
import scenario.schedule.CargoRevenue;
import scenario.schedule.CharterOutRevenue;
import scenario.schedule.LineItem;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleetallocation.FleetVessel;

import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.Calculator;

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
			final Schedule schedule, final ModelEntityMap entities) {
		for (final Sequence seq : schedule.getSequences()) {
			boolean getNextLeg = false;
			BookedRevenue lastRevenue = null;
			for (final ScheduledEvent evt : seq.getEvents()) {
				if (evt instanceof CharterOutVisit) {
					final CharterOutVisit visit = (CharterOutVisit) evt;

					// add revenue for charter out
					final CharterOutRevenue revenue = scheduleFactory
							.createCharterOutRevenue();
					
					visit.setRevenue(revenue);
					
					revenue.setCharterOut(visit);
					
					revenue.setEntity(scenario.getContractModel()
							.getShippingEntity());
					revenue.setDate(visit.getEndTime());
					
					final LineItem li = scheduleFactory.createLineItem();
					li.setName("Charter out revenue");
					li.setParty(null);
					li.setValue((evt.getDuration()
							* ((FleetVessel) seq.getVessel()).getVessel()
									.getClass_().getDailyCharterPrice())/24);
					
					// TODO onward ballast leg costs - we pay or renter pays?
					// at the moment I'm saying we pay.
					revenue.getLineItems().add(li);
					
					schedule.getRevenue().add(revenue);
					
					getNextLeg = true;
					lastRevenue = revenue;
				} else if (getNextLeg && evt instanceof Journey) {
					final Journey journey = (Journey) evt;
					final LineItem li = scheduleFactory.createLineItem();
					li.setName("Onward journey cost");
					li.setParty(null);
					li.setValue(- (int) journey.getTotalCost());
					lastRevenue.getLineItems().add(li);
				} else if (getNextLeg && evt instanceof Idle) {
					final Idle idle = (Idle) evt;
					final LineItem li = scheduleFactory.createLineItem();
					li.setName("Onward idle cost");
					li.setParty(null);
					li.setValue(- (int) idle.getTotalCost());
					lastRevenue.getLineItems().add(li);
					getNextLeg = false;
				}
			}
		}

		for (final CargoAllocation allocation : schedule.getCargoAllocations()) {
			final LoadSlot loadSlot = allocation.getLoadSlot();
			final Slot dischargeSlot = allocation.getDischargeSlot();

			final Contract loadContract = loadSlot.getSlotOrPortContract();
			final SalesContract dischargeContract = (SalesContract) dischargeSlot
					.getSlotOrPortContract();

			final Entity loadEntity = loadContract.getEntity();
			final Entity dischargeEntity = dischargeContract.getEntity();

			final Entity shippingEntity = scenario.getContractModel()
					.getShippingEntity();

			final CargoRevenue dischargeRevenue = scheduleFactory
					.createCargoRevenue();
			final CargoRevenue shippingRevenue = scheduleFactory
					.createCargoRevenue();
			final CargoRevenue loadRevenue = scheduleFactory
					.createCargoRevenue();

			dischargeRevenue.setEntity(dischargeEntity);
			shippingRevenue.setEntity(shippingEntity);
			loadRevenue.setEntity(loadEntity);

			// item 1; value of selling LNG
			final long dischargedM3 = allocation.getDischargeVolume();
			final long saleableM3 = (long) Math.floor(allocation
					.getDischargeVolume()
					* dischargeContract.getRegasEfficiency());

			// divide by CV value to get mmbtu
			final long saleableMMBTU = (long) Math.floor(saleableM3
					* loadSlot.getCargoCVvalue());
			final long lostMMBTU = (long) (dischargedM3 * loadSlot
					.getCargoCVvalue()) - saleableMMBTU;

			// lookup the final market sales price
			final float salesPricePerMMBTU = dischargeContract.getMarket()
					.getPriceCurve()
					.getValueAtDate(allocation.getDischargeDate());

			// compute how much money the discharge entity will have made
			final int salesRevenueToShippingEntity = (int) Math
					.floor(salesPricePerMMBTU * saleableMMBTU);

			final int salesRevenueToDischargeEntity = (int) Math
					.floor(salesRevenueToShippingEntity
							* dischargeContract.getMarkup());

			final int potentialSalesRevenueToShippingEntity = (int) Math
					.floor(salesPricePerMMBTU * (saleableMMBTU + lostMMBTU));
			final int regasLossesToShippingEntity = potentialSalesRevenueToShippingEntity
					- salesRevenueToShippingEntity;

			// set revenues and costs
			{
				dischargeRevenue.getLineItems().add(
						createLineItem("Sale to market", null,
								salesRevenueToDischargeEntity));

				dischargeRevenue.getLineItems().add(
						createLineItem("Purchase from shipping",
								shippingEntity, -salesRevenueToShippingEntity));

				shippingRevenue.getLineItems().add(
						createLineItem("Value of discharged cargo",
								dischargeEntity, salesRevenueToShippingEntity
										+ regasLossesToShippingEntity));

				if (CargoType.FLEET.equals(allocation.getCargoType())) {
					shippingRevenue.getLineItems().add(
							createLineItem("Regas losses", dischargeEntity,
									-regasLossesToShippingEntity));
				}
			}

			// now add transport costs to shipping revenue
			if (CargoType.FLEET.equals(allocation.getCargoType())) {
				// TODO here I am taking laden & ballast costs - should it be
				// laden costs only, and ballast costs are booked at a later
				// date (not associated with a cargo?)

				final int totalNonLNGFuelCost = getTotalNonLNGFuelCost(allocation
						.getLadenLeg())
						+ getTotalNonLNGFuelCost(allocation.getBallastLeg())
						+ getTotalNonLNGFuelCost(allocation.getLadenIdle())
						+ getTotalNonLNGFuelCost(allocation.getBallastIdle());

				if (totalNonLNGFuelCost != 0)
					shippingRevenue.getLineItems().add(
							createLineItem("Base fuel cost", null,
									-totalNonLNGFuelCost));

				final int canalCost = (int) (allocation.getLadenLeg()
						.getRouteCost() + allocation.getBallastLeg()
						.getRouteCost());

				if (canalCost != 0)
					shippingRevenue.getLineItems().add(
							createLineItem("Canal cost", null, -canalCost));

				final int hireCost = (int) (allocation.getLadenLeg()
						.getHireCost()
						+ allocation.getBallastLeg().getHireCost()
						+ allocation.getBallastIdle().getHireCost() + allocation
						.getLadenIdle().getHireCost());
				if (hireCost != 0)
					shippingRevenue.getLineItems().add(
							createLineItem("Hire cost", null, -hireCost));
			}

			// now do load-side stuff
			{
				// TODO convert to floats in VisitEventExporter rather than
				// un-scaling here

				final int loadValue = (int) ((allocation.getLoadPriceM3() * allocation
						.getLoadVolume()) / Calculator.ScaleFactor);
				loadRevenue.getLineItems().add(
						createLineItem("Sales to shipping", shippingEntity,
								loadValue));
				shippingRevenue.getLineItems().add(
						createLineItem("Purchase of cargo", loadEntity,
								-loadValue));
			}

			// don't forget to set the dates at which revenue is booked,
			// and thus taxed

			shippingRevenue.setDate(allocation.getDischargeDate());
			loadRevenue.setDate(allocation.getLoadDate());
			dischargeRevenue.setDate(allocation.getDischargeDate());

			shippingRevenue.setCargo(allocation);
			loadRevenue.setCargo(allocation);
			dischargeRevenue.setCargo(allocation);

			allocation.setLoadRevenue(loadRevenue);
			allocation.setShippingRevenue(shippingRevenue);
			allocation.setDischargeRevenue(dischargeRevenue);

			// put revenue in book
			schedule.getRevenue().add(loadRevenue);
			schedule.getRevenue().add(shippingRevenue);
			schedule.getRevenue().add(dischargeRevenue);
		}
	}

	private int getTotalNonLNGFuelCost(final FuelMixture mix) {
		int total = 0;
		for (final FuelQuantity fq : mix.getFuelUsage()) {
			if (fq.getFuelType().equals(FuelType.FBO)
					|| fq.getFuelType().equals(FuelType.NBO)) {
				total += fq.getTotalPrice();
			}
		}
		return total;
	}

	private LineItem createLineItem(final String name, final Entity party,
			final int value) {
		final LineItem item = scheduleFactory.createLineItem();
		item.setName(name);
		item.setParty(party);
		item.setValue(value);
		return item;
	}
}
