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
	public void testingIndexingAndPriceDating(String name, Date testingDate, Date scenarioDate, Date priceDate, double priceA, double priceB, double priceC) {
		PricingTimesScenario tester = new PricingTimesScenario(3);
		System.out.println(String.format("***%s***", name));
		System.out.println(String.format("Testing contract date:%s", testingDate));
		tester.initThreeCargoData(scenarioDate, priceDate);
		tester.addTestCommodityIndexes();
		tester.setScenario();
		tester.testSalesPrice(priceA, priceB, priceC);
	}

	public void testingIndexing(String name, Date testingDate, Date scenarioDate, double priceA, double priceB, double priceC) {
		testingIndexingAndPriceDating(name, testingDate, scenarioDate, null, priceA, priceB, priceC);
	}

	public void testingIndexingWithPriceDatingAndTimeZones(String name, Date testingDate, Date scenarioDate, Date pricingDate, double priceA, String priceCurve, String portsTimeZone) {
		PricingTimesScenario tester = new PricingTimesScenario(1, portsTimeZone);
		System.out.println("\n");
		System.out.println(String.format("***%s***", name));
		System.out.println(String.format("Testing contract date:%s", testingDate));
		System.out.println(String.format("Pricing date:%s", pricingDate));
		tester.initSingleCargoData(scenarioDate, pricingDate, priceCurve);
		tester.addTestCommodityIndexes();
		tester.setScenario();
		tester.testSalesPrice(priceA);
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
		testingIndexingAndPriceDating("TestPriceSaleNotAffectedByPricingDate", createDate(2014, 1, 2), createDate(2014, 1, 1), createDate(2014, 1, 2), 7.5, 8.5, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateA() {
		testingIndexingAndPriceDating("TestPriceSaleChangedByEarlierPricingDateA", createDate(2014, 0, 31), createDate(2014, 1, 1), createDate(2014, 1, 1), 7.0, 8.0, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateB() {
		testingIndexingAndPriceDating("TestPriceSaleChangedByEarlierPricingDateB", createDate(2014, 0, 13), createDate(2014, 1, 1), createDate(2014, 0, 13), 4.5, 6.5, 5.0);
	}

	/**
	 * Testing pricing date points to earlier index
	 */
	@Test
	public void TestPriceSaleChangedByEarlierPricingDateC() {
		testingIndexingAndPriceDating("TestPriceSaleChangedByEarlierPricingDateC", createDate(2014, 0, 1), createDate(2014, 1, 1), createDate(2014, 0, 5), 4.0, 6.0, 5.0);
	}

	/**
	 * Testing pricing date set in UTC (at present will shift incorrectly)
	 */
	@Ignore("Pricing date is stored as UTC in the UI but the LNGTransformer assumes local time and shifts accordingly")
	@Test
	public void TestTimeZonePricingDateUTCPortEtcGMTPlus12() {
		Date pricingDate = createDate(2014, 1, 1, 0, "UTC");
		Date cargoStartDate = createDate(2014, 0, 30, "UTC");
		testingIndexingWithPriceDatingAndTimeZones("PD UTC, Port Etc/GMT+12", createDate(2014, 1, 1, "UTC"), cargoStartDate, pricingDate, 10.0, "UTC", "Etc/GMT+12");
	}

	/**
	 * Testing pricing date set in local time (will shift correctly)
	 */
	@Ignore("Pricing date is stored as UTC in the UI but the LNGTransformer assumes local time and shifts accordingly")
	@Test
	public void TestTimeZonePricingDateEtcGMTPlus12PortEtcGMTPlus12() {
		// GTM+1 means 1 hour behind
		Date pricingDate = createDate(2014, 1, 1, 0, "Etc/GMT+12");
		Date cargoStartDate = createDate(2014, 0, 30, "UTC");
		testingIndexingWithPriceDatingAndTimeZones("PD Etc/GMT+12, Port Etc/GMT+12", createDate(2014, 1, 1, "UTC"), cargoStartDate, pricingDate, 5.0, "UTC", "Etc/GMT+12");
	}
}
