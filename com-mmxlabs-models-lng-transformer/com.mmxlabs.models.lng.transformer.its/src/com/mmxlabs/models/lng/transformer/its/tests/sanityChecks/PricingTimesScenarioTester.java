/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

/**
 * Tests {@link PricingTimesScenario} instances to make sure discharges use the correct sales price, according to a price index and a pricing date
 * 
 * @author Alex Churchill
 * 
 */
@RunWith(value = ShiroRunner.class)
public class PricingTimesScenarioTester {
	public void testingIndexingAndPriceDating(@NonNull final String name, @NonNull final LocalDate expectedPriceLookupDate, @NonNull final LocalDateTime dischargeDate,
			@Nullable final LocalDate priceDate, final double priceA, final double priceB, final double priceC) {
		final PricingTimesScenario tester = new PricingTimesScenario(3);
		System.out.println(String.format("***%s***", name));
		System.out.println(String.format("Testing contract date:%s", expectedPriceLookupDate));
		tester.initThreeCargoData(dischargeDate, priceDate);
		tester.addTestCommodityIndexes();
		tester.setScenario();
		tester.testSalesPrice(priceA, priceB, priceC);
	}

	public void testingIndexing(@NonNull final String name, @NonNull final LocalDate expectedPriceLookupDate, @NonNull final LocalDateTime dischargeDate, double priceA, final double priceB,
			final double priceC) {
		testingIndexingAndPriceDating(name, expectedPriceLookupDate, dischargeDate, null, priceA, priceB, priceC);
	}

	public void testingIndexingWithPriceDatingAndTimeZones(@NonNull final String name, @NonNull final LocalDate expectedPriceLookupDate, @NonNull final LocalDateTime dischargeDate,
			@Nullable final LocalDate pricingDate, final double priceA, @NonNull final String priceCurve, @NonNull final String portsTimeZone) {
		final PricingTimesScenario tester = new PricingTimesScenario(1, portsTimeZone);
		System.out.println("\n");
		System.out.println(String.format("***%s***", name));
		System.out.println(String.format("Testing contract date:%s", expectedPriceLookupDate));
		System.out.println(String.format("Pricing date:%s", pricingDate));
		tester.initSingleCargoData(dischargeDate, pricingDate, priceCurve);
		tester.addTestCommodityIndexes();
		tester.setScenario();
		tester.testSalesPrice(priceA);
	}

	/**
	 * Testing price of discharge 2 days away from new price index
	 */
	@Test
	public void TestPriceSaleTwoDaysAfterPriceChangeA() {
		testingIndexing("TestPriceSaleTwoDaysAfterPriceChangeA", PricingTimesScenario.createLocalDate(2014, Calendar.FEBRUARY, 1),
				PricingTimesScenario.createLocalDateTime(2014, Calendar.FEBRUARY, 3, 0), 7.5, 8.5, 5.0);
	}

	/**
	 * Testing price of discharge 2 days away from new price index
	 */
	@Test
	public void TestPriceSaleTwoDaysAfterPriceChangeB() {
		testingIndexing("TestPriceSaleTwoDaysAfterPriceChangeB", PricingTimesScenario.createLocalDate(2014, Calendar.MAY, 1), PricingTimesScenario.createLocalDateTime(2014, Calendar.MAY, 3, 0), 8.0,
				10.0, 5.0);
	}

	/**
	 * Testing price of discharge 1 day away from new price index
	 */
	@Test
	public void TestPriceSaleOneDayAfterPriceChangeA() {
		testingIndexing("TestPriceSaleOneDayAfterPriceChangeA", PricingTimesScenario.createLocalDate(2014, Calendar.JANUARY, 1),
				PricingTimesScenario.createLocalDateTime(2014, Calendar.JANUARY, 30, 0), 4.0, 6.0, 5.0);
	}

	/**
	 * Testing price of discharge 1 day away from new price index
	 */
	@Test
	public void TestPriceSaleOneDayAfterPriceChangeB() {
		testingIndexing("TestPriceSaleOneDayAfterPriceChangeB", PricingTimesScenario.createLocalDate(2014, Calendar.FEBRUARY, 1),
				PricingTimesScenario.createLocalDateTime(2014, Calendar.APRIL, 30, 0), 7.5, 8.5, 5.0);
	}

	/**
	 * Testing price of discharge 0 days away from new price index
	 */
	public void TestPriceSaleSameDayAsPriceChange() {
		testingIndexing("TestPriceSaleSameDayAsPriceChange", PricingTimesScenario.createLocalDate(2014, Calendar.FEBRUARY, 2), PricingTimesScenario.createLocalDateTime(2014, Calendar.FEBRUARY, 1, 0),
				7.5, 8.5, 5.0);

	}

	/**
	 * Testing pricing date should not affect contract
	 */
	@Test
	public void TestPriceSaleNotAffectedByPricingDate() {
		testingIndexingAndPriceDating("TestPriceSaleNotAffectedByPricingDate", PricingTimesScenario.createLocalDate(2014, Calendar.FEBRUARY, 2),
				PricingTimesScenario.createLocalDateTime(2014, Calendar.FEBRUARY, 1, 0), PricingTimesScenario.createLocalDate(2014, Calendar.FEBRUARY, 2), 7.5, 8.5, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateA() {
		testingIndexingAndPriceDating("TestPriceSaleChangedByEarlierPricingDateA", PricingTimesScenario.createLocalDate(2014, Calendar.JANUARY, 1),
				PricingTimesScenario.createLocalDateTime(2014, Calendar.FEBRUARY, 1, 0), PricingTimesScenario.createLocalDate(2014, Calendar.JANUARY, 1), 4.0, 6.0, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateB() {
		testingIndexingAndPriceDating("TestPriceSaleChangedByEarlierPricingDateB", PricingTimesScenario.createLocalDate(2014, Calendar.FEBRUARY, 1),
				PricingTimesScenario.createLocalDateTime(2014, Calendar.MAY, 1, 0), PricingTimesScenario.createLocalDate(2014, Calendar.FEBRUARY, 1), 7.5, 8.5, 5.0);
	}

}
