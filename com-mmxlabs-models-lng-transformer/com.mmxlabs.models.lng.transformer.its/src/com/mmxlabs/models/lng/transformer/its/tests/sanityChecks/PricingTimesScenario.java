/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * Creates a simple scenario for testing changes in price indexes and price dating
 * 
 * @author Alex Churchill
 */
public class PricingTimesScenario {
	private static final int cscDischargePrice = 1;

	public Port smallLoadPort;
	public Port bigLoadPort;
	public Port smallDischargePort;
	public Port bigDischargePort;

	public String dischargePriceIndexedA = "datedA";
	public String dischargePriceIndexedB = "datedB";
	public String dischargePriceNotIndexed = "5";

	public LNGScenarioModel scenario;
	public CustomScenarioCreator csc;

	public PricingTimesScenario(int numVessels, String timeZone) {
		csc = new CustomScenarioCreator(cscDischargePrice, timeZone);
		final int fuelPrice = 1;

		smallLoadPort = ScenarioTools.createPort("smallLoadPort");
		bigLoadPort = ScenarioTools.createPort("bigLoadPort");
		smallDischargePort = ScenarioTools.createPort("smallDischargePort");
		bigDischargePort = ScenarioTools.createPort("bigDischargePort");

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { smallLoadPort, bigLoadPort, smallDischargePort, bigDischargePort };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = numVessels;

		// createVessels creates and adds the vessels to the scenario.
		csc.addVesselSimple("classOne", numOfClassOne, fuelPrice, 25, 1000000, 10, 10, 0, 500, false);
	}

	public PricingTimesScenario(int numVessels) {
		this(numVessels, TimeZone.getDefault().getID());
	}

	void setScenario() {
		scenario = csc.buildScenario();
	}

	void addTestCommodityIndexes() {
		// add a commodity index to the pricing model
		CommodityIndex ci1 = csc.addCommodityIndex(dischargePriceIndexedA);
		csc.addDataToCommodity(ci1, createDate(2014, 0, 1), 4.0);
		csc.addDataToCommodity(ci1, createDate(2014, 0, 13), 4.5);
		csc.addDataToCommodity(ci1, createDate(2014, 0, 31), 7.0);
		csc.addDataToCommodity(ci1, createDate(2014, 1, 2), 7.5);
		csc.addDataToCommodity(ci1, createDate(2014, 4, 1), 8.0);

		// add a second commodity index to the pricing model
		CommodityIndex ci2 = csc.addCommodityIndex(dischargePriceIndexedB);
		csc.addDataToCommodity(ci2, createDate(2014, 0, 1), 6.0);
		csc.addDataToCommodity(ci2, createDate(2014, 0, 13), 6.5);
		csc.addDataToCommodity(ci2, createDate(2014, 0, 31), 8.0);
		csc.addDataToCommodity(ci2, createDate(2014, 1, 2), 8.5);
		csc.addDataToCommodity(ci2, createDate(2014, 4, 1), 10.0);

		// add a commodity index explicitly in UTC
		CommodityIndex ci3 = csc.addCommodityIndex("UTC");
		csc.addDataToCommodity(ci3, createDate(2014, 0, 1), 4.0);
		csc.addDataToCommodity(ci3, createDate(2014, 0, 31), 5.0);
		csc.addDataToCommodity(ci3, createDate(2014, 1, 1), 10.0);
		csc.addDataToCommodity(ci3, createDate(2014, 1, 2), 6.5);
	}

	/*
	 * Used to test different Price Indexes
	 */
	void initThreeCargoData(Date cargoStart, Date priceDate) {
		// create three identical cargoes with different price exps
		Cargo indexedCargoA = csc.addCargo("indexed-cargo-a", bigLoadPort, bigDischargePort, "1", dischargePriceIndexedA, 22, cargoStart, 50);
		Cargo indexedCargoB = csc.addCargo("indexed-cargo-b", bigLoadPort, bigDischargePort, "1", dischargePriceIndexedB, 22, cargoStart, 50);
		Cargo notIndexedCargo = csc.addCargo("not-indexed-cargo", bigLoadPort, bigDischargePort, "1", dischargePriceNotIndexed, 22, cargoStart, 50);
		if (priceDate != null) {
			setPriceDates(new Cargo[] { indexedCargoA, indexedCargoB, notIndexedCargo }, priceDate);
		}
	}

	/*
	 * Used to test different pricing dates on different price indexes
	 */
	void setPriceDates(Cargo[] cargoes, Date priceDate) {
		for (int i = 0; i < cargoes.length; i++) {
			cargoes[i].getSlots().get(1).setPricingDate(priceDate);
		}
	}

	/*
	 * Used to test timezone of pricingDate
	 */
	void initSingleCargoData(Date cargoStart, Date priceDate, String priceIndex) {
		Cargo indexedCargoA = csc.addCargo("indexed-cargo-a", bigLoadPort, bigDischargePort, "1", priceIndex, 22, cargoStart, 50);
		if (priceDate != null) {
			setPriceDates(new Cargo[] { indexedCargoA }, priceDate);
		}
	}

	/*
	 * Main testing methods to run the created scenario and compare the actual and expected price
	 */
	public void testSalesPrice(double... prices) {
		ScenarioRunner runner = new ScenarioRunner((LNGScenarioModel) this.scenario);
		try {
			runner.init();
		} catch (IncompleteScenarioException e) {
			Assert.assertTrue("Scenario runner failed to initialise simple scenario.", false);
		}
		final Schedule schedule = runner.updateScenario();
		// Workaround, would prefer to use ca.getInputCargo().getName() but hookup runner.updateScenario() not working
		String errorMsg = "Cargo %s has incorrect pricing %2f != %2f";
		for (int i = 0; i < schedule.getCargoAllocations().size(); i++) {
			CargoAllocation ca = schedule.getCargoAllocations().get(i);
			if (i == 0) {
				System.out.println("Discharge start:" + ca.getSlotAllocations().get(1).getLocalStart().getTime());
				System.out.println("Discharge end:" + ca.getSlotAllocations().get(1).getLocalEnd().getTime());
			}
			double salePrice = ca.getSlotAllocations().get(1).getPrice();
			double expectedPrice = prices[i];
			System.out.println("Sale price: " + salePrice);
			System.out.println("Expected price:" + expectedPrice);
			Assert.assertEquals(String.format(errorMsg, i, salePrice, expectedPrice), salePrice, expectedPrice, 0.0001);
		}
	}

	/*
	 * Helper date methods
	 */
	static Date createDate(int year, int month, int day, int hour, String timeZone) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	static Date createDate(int year, int month, int day) {
		return createDate(year, month, day, 0, "UTC");
	}

	static Date createDate(int year, int month, int day, String timeZone) {
		return createDate(year, month, day, 0, timeZone);
	}

}