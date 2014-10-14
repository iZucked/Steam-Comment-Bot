package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.Date;

import org.junit.Test;

import static com.mmxlabs.models.lng.transformer.its.tests.sanityChecks.PricingTimesScenario.createDate;

/**
 * Tests {@link PricingTimesScenario} instances to make sure discharges use the correct sales price, according to a price index and a pricing date
 * 
 * @author Alex Churchill
 * 
 */
public class PricingTimesScenarioTester {
	public void testingIndexing(String name, Date testingDate, Date scenarioDate, double priceA, double priceB, double priceC) {
		PricingTimesScenario tester = new PricingTimesScenario();
		System.out.println(String.format("***%s***", name));
		System.out.println(String.format("Testing contract date:%s", testingDate));
		tester.addCargo(scenarioDate);
		tester.addTestCommodityIndexes();
		tester.setScenario();
		tester.testSalesPrice(priceA, priceB, priceC);
	}

	public void testingIndexingWithPriceDating(String name, Date testingDate, Date scenarioDate, Date priceDate, double priceA, double priceB, double priceC) {
		PricingTimesScenario tester = new PricingTimesScenario();
		System.out.println(String.format("***%s***", name));
		System.out.println(String.format("Testing contract date:%s", testingDate));
		tester.addCargo(scenarioDate);
		tester.addTestCommodityIndexes();
		tester.setPriceDate(scenarioDate, priceDate);
		tester.setScenario();
		tester.testSalesPrice(priceA, priceB, priceC);
	}

	/**
	 * Testing price of discharge 2 days away from new price index
	 */
	@Test
	public void TestPriceSaleTwoDaysAfterPriceChangeA() {
		testingIndexing("TestPriceSaleTwoDaysAfterPriceChangeA", createDate(2014, 0, 1), createDate(2014, 0, 1), 4.0, 6.0, 5.0);
	}

	/**
	 * Testing price of discharge 2 days away from new price index
	 */
	@Test
	public void TestPriceSaleTwoDaysAfterPriceChangeB() {
		testingIndexing("TestPriceSaleTwoDaysAfterPriceChangeB", createDate(2014, 0, 13), createDate(2014, 0, 13), 4.5, 6.5, 5.0);
	}

	/**
	 * Testing price of discharge 1 day away from new price index
	 */
	@Test
	public void TestPriceSaleOneDayAfterPriceChangeA() {
		testingIndexing("TestPriceSaleOneDayAfterPriceChangeA", createDate(2014, 0, 31), createDate(2014, 0, 30), 7.0, 8.0, 5.0);
	}

	/**
	 * Testing price of discharge 1 day away from new price index
	 */
	@Test
	public void TestPriceSaleOneDayAfterPriceChangeB() {
		testingIndexing("TestPriceSaleOneDayAfterPriceChangeB", createDate(2014, 1, 2), createDate(2014, 1, 1), 7.5, 8.5, 5.0);
	}

	/**
	 * Testing price of discharge 0 days away from new price index
	 */
	public void TestPriceSaleSameDayAsPriceChange() {
		testingIndexing("TestPriceSaleSameDayAsPriceChange", createDate(2014, 1, 2), createDate(2014, 0, 31), 7.5, 8.5, 5.0);

	}

	/**
	 * Testing pricing date should not affect contract
	 */
	@Test
	public void TestPriceSaleNotAffectedByPricingDate() {
		testingIndexingWithPriceDating("TestPriceSaleNotAffectedByPricingDate", createDate(2014, 1, 2), createDate(2014, 1, 1), createDate(2014, 1, 2), 7.5, 8.5, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateA() {
		testingIndexingWithPriceDating("TestPriceSaleChangedByEarlierPricingDateA", createDate(2014, 0, 31), createDate(2014, 1, 1), createDate(2014, 1, 1), 7.0, 8.0, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateB() {
		testingIndexingWithPriceDating("TestPriceSaleChangedByEarlierPricingDateB", createDate(2014, 0, 13), createDate(2014, 1, 1), createDate(2014, 0, 13), 4.5, 6.5, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateC() {
		testingIndexingWithPriceDating("TestPriceSaleChangedByEarlierPricingDateC", createDate(2014, 0, 1), createDate(2014, 1, 1), createDate(2014, 0, 5), 4.0, 6.0, 5.0);
	}
}
