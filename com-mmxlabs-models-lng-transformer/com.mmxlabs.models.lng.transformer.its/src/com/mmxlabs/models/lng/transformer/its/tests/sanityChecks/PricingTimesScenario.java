/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;

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

	public PricingTimesScenario(final int numVessels, final String timeZone) {
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

	public PricingTimesScenario(final int numVessels) {
		this(numVessels, "UTC");
	}

	void setScenario() {
		scenario = csc.buildScenario();
	}

	void addTestCommodityIndexes() {
		// add a commodity index to the pricing model
		final CommodityIndex ci1 = csc.addCommodityIndex(dischargePriceIndexedA);
		csc.addDataToCommodity(ci1, createYearMonth(2014, Calendar.JANUARY), 4.0);
		// csc.addDataToCommodity(ci1, createYearMonth(2014, 0, 13), 4.5);
		// csc.addDataToCommodity(ci1, createYearMonth(2014, 0, 31), 7.0);
		csc.addDataToCommodity(ci1, createYearMonth(2014, Calendar.FEBRUARY), 7.5);
		csc.addDataToCommodity(ci1, createYearMonth(2014, Calendar.MAY), 8.0);

		// add a second commodity index to the pricing model
		final CommodityIndex ci2 = csc.addCommodityIndex(dischargePriceIndexedB);
		csc.addDataToCommodity(ci2, createYearMonth(2014, Calendar.JANUARY), 6.0);
		// csc.addDataToCommodity(ci2, createYearMonth(2014, 0, 13), 6.5);
		// csc.addDataToCommodity(ci2, createYearMonth(2014, 0, 31), 8.0);
		csc.addDataToCommodity(ci2, createYearMonth(2014, Calendar.FEBRUARY), 8.5);
		csc.addDataToCommodity(ci2, createYearMonth(2014, Calendar.MAY), 10.0);

		// add a commodity index explicitly in UTC
		final CommodityIndex ci3 = csc.addCommodityIndex("UTC");
		csc.addDataToCommodity(ci3, createYearMonth(2014, Calendar.JANUARY), 4.0);
		// csc.addDataToCommodity(ci3, createYearMonth(2014, 0, 31), 5.0);
		csc.addDataToCommodity(ci3, createYearMonth(2014, Calendar.FEBRUARY), 10.0);
		// csc.addDataToCommodity(ci3, createYearMonth(2014, 1, 2), 6.5);
	}

	/*
	 * Used to test different Price Indexes
	 */
	void initThreeCargoData(@NonNull final LocalDateTime dischargeDate, @Nullable final LocalDate priceDate) {
		final int travelTime = 48;
		// create three identical cargoes with different price exps
		final Cargo indexedCargoA = csc.addCargo("indexed-cargo-a", bigLoadPort, bigDischargePort, "1", dischargePriceIndexedA, 22, dischargeDate.minusHours(travelTime), travelTime);
		final Cargo indexedCargoB = csc.addCargo("indexed-cargo-b", bigLoadPort, bigDischargePort, "1", dischargePriceIndexedB, 22, dischargeDate.minusHours(travelTime), travelTime);
		final Cargo notIndexedCargo = csc.addCargo("not-indexed-cargo", bigLoadPort, bigDischargePort, "1", dischargePriceNotIndexed, 22, dischargeDate.minusHours(travelTime), travelTime);
		if (priceDate != null) {
			setPriceDates(new Cargo[] { indexedCargoA, indexedCargoB, notIndexedCargo }, priceDate);
		}
	}

	/*
	 * Used to test different pricing dates on different price indexes
	 */
	void setPriceDates(@NonNull final Cargo[] cargoes, @Nullable final LocalDate priceDate) {
		for (int i = 0; i < cargoes.length; i++) {
			cargoes[i].getSlots().get(1).setPricingDate(priceDate);
		}
	}

	/*
	 * Used to test timezone of pricingDate
	 */
	void initSingleCargoData(@NonNull final LocalDateTime cargoStart, @Nullable final LocalDate priceDate, @NonNull final String priceIndex) {
		int travelTime = 48;
		final Cargo indexedCargoA = csc.addCargo("indexed-cargo-a", bigLoadPort, bigDischargePort, "1", priceIndex, 22, cargoStart.minusHours(travelTime), travelTime);
		if (priceDate != null) {
			setPriceDates(new Cargo[] { indexedCargoA }, priceDate);
		}
	}

	/*
	 * Main testing methods to run the created scenario and compare the actual and expected price
	 */
	public void testSalesPrice(double... prices) {

		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, (LNGScenarioModel) this.scenario, LNGScenarioRunnerUtils.createDefaultSettings(),
					new TransformerExtensionTestBootstrapModule(), null, true);
			runner.evaluateInitialState();

			final Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);

			// Workaround, would prefer to use ca.getInputCargo().getName() but hookup runner.updateScenario() not working
			String errorMsg = "Cargo %s has incorrect pricing %2f != %2f";
			for (int i = 0; i < schedule.getCargoAllocations().size(); i++) {
				CargoAllocation ca = schedule.getCargoAllocations().get(i);
				if (i == 0) {
					System.out.println("Discharge start:" + ca.getSlotAllocations().get(1).getSlotVisit().getStart().toString());
					System.out.println("Discharge end:" + ca.getSlotAllocations().get(1).getSlotVisit().getEnd().toString());
				}
				double salePrice = ca.getSlotAllocations().get(1).getPrice();
				double expectedPrice = prices[i];
				System.out.println("Sale price: " + salePrice);
				System.out.println("Expected price:" + expectedPrice);
				Assert.assertEquals(String.format(errorMsg, i, expectedPrice, salePrice), expectedPrice, salePrice, 0.0001);
			}

		} finally {
			executorService.shutdownNow();
		}
	}

	/*
	 * Helper date methods
	 */
	@NonNull
	static ZonedDateTime createDateTime(final int year, final int month, final int day, final int hour, final String timeZone) {
		return ZonedDateTime.of(year, 1 + month, day, hour, 0, 0, 0, ZoneId.of(timeZone));
	}

	@NonNull
	static LocalDateTime createLocalDateTime(final int year, final int month, final int day, final int hour) {
		return LocalDateTime.of(year, 1 + month, day, hour, 0);
	}

	@NonNull
	static LocalDate createLocalDate(final int year, final int month, final int day) {
		return LocalDate.of(year, 1 + month, day);
	}

	@NonNull
	static YearMonth createYearMonth(final int year, final int month) {
		return YearMonth.of(year, 1 + month);
	}

}