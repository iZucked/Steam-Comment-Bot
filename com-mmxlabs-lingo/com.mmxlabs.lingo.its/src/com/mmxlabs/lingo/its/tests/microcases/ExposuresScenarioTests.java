/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@SuppressWarnings("unused")
@ExtendWith(value = ShiroRunner.class)
public class ExposuresScenarioTests extends AbstractMicroTestCase {
	
	@NonNull
	@Override
	public IScenarioDataProvider importReferenceData() throws Exception {
		return importReferenceData("/referencedata/hedging-test/");
	}
	
	private PricingModel pricingModel;
	
	@NonNull
	public static IScenarioDataProvider importReferenceData(final String url) throws Exception {

		final @NonNull String urlRoot = AbstractMicroTestCase.class.getResource(url).toString();
		final CSVImporter importer = new CSVImporter();
		importer.importPortData(urlRoot);
		importer.importCostData(urlRoot);
		importer.importEntityData(urlRoot);
		importer.importFleetData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPromptData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPorfolioData(urlRoot);

		return importer.doImport();
	}
	


	private static List<String> requiredFeatures = Lists.newArrayList();
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterAll
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	private static final double delta = 0.01;
	
	/**
	 * Test the initial case with no restrictions
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGeneratedHedgesComputation() throws Exception {

		evaluateWithLSOTest(scenarioRunner -> {
			Assertions.assertNotNull(scenarioRunner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = scenarioRunner.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertNotNull(schedule.getGeneratedPaperDeals());
			Assertions.assertFalse(schedule.getGeneratedPaperDeals().isEmpty());
			Assertions.assertTrue(schedule.getGeneratedPaperDeals().size() == 3);
			
			PaperDeal brentBuyJan = null;
			PaperDeal brentSellFeb = null;
			PaperDeal jkmBuyJan = null;
			for (final PaperDeal paperDeal : schedule.getGeneratedPaperDeals()) {
				if ("brent-ice-2017-01_buy".equalsIgnoreCase(paperDeal.getName())) {
					brentBuyJan = paperDeal;
				} else if ("brent-ice_2017-02_sell".equalsIgnoreCase(paperDeal.getName())) {
					brentSellFeb = paperDeal;
				} else if ("jkm_2017-02_buy".equalsIgnoreCase(paperDeal.getName())) {
					jkmBuyJan = paperDeal;
				}
			}
			Assertions.assertNotNull(brentBuyJan);
			Assertions.assertEquals(brentBuyJan.getQuantity(), 237160.00, delta);
			Assertions.assertNotNull(brentSellFeb);
			Assertions.assertEquals(brentSellFeb.getQuantity(), -601320.00, delta);
			Assertions.assertNotNull(jkmBuyJan);
			Assertions.assertEquals(jkmBuyJan.getQuantity(), 36938.00, delta);
		});
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNoGeneratedHedges() throws Exception {

		generateTestPapers();
		
		evaluateWithLSOTest(scenarioRunner -> {
			Assertions.assertNotNull(scenarioRunner);
			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = scenarioRunner.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertNotNull(schedule.getGeneratedPaperDeals());
			Assertions.assertTrue(schedule.getGeneratedPaperDeals().isEmpty());
		});
	}
	
	private void generateTestPapers() {
		final CargoModel cm = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);
		entity = commercialModel.getEntities().get(0);
		cm.getPaperDeals().add(makePaperDeal("BRENT-Buy-2017-Jan", true, "BRENT_ICE", "Brent_momental_swap", YearMonth.of(2017, 1), 237177.00, 1.00));
		cm.getPaperDeals().add(makePaperDeal("BRENT-Sell-2017-Feb", false, "BRENT_ICE", "Brent_momental_swap", YearMonth.of(2017, 2), 601329.00, 1.00));
		cm.getPaperDeals().add(makePaperDeal("JKM-Buy-2017-Jan", true, "jkm", "Brent_momental_swap", YearMonth.of(2017, 1), 36954.00, 1.00));
	}
	
	private PaperDeal makePaperDeal(final String name, final boolean isPurchase, final String curveName, final String instrument, final YearMonth month, final double quantity, final double price) {
		PaperDeal paperDeal = null;

		if (isPurchase) {
			paperDeal = CargoFactory.eINSTANCE.createBuyPaperDeal();
		} else {
			paperDeal = CargoFactory.eINSTANCE.createSellPaperDeal();
		}
		paperDeal.setName(name);
		paperDeal.setQuantity(quantity);
		paperDeal.setPrice(price);
		paperDeal.setPricingMonth(month);
		paperDeal.setStartDate(month.atDay(1)); 
		paperDeal.setEndDate(month.atEndOfMonth());
		
		SettleStrategy settleStrategy = null;
		for (final SettleStrategy ss : pricingModel.getSettleStrategies()) {
			if (instrument.equalsIgnoreCase(ss.getName())) {
				settleStrategy = ss;
				break;
			}
		}

		paperDeal.setPricingType(PaperPricingType.INSTRUMENT);
		paperDeal.setInstrument(settleStrategy);
		paperDeal.setIndex(curveName);
		paperDeal.setEntity(entity);
		paperDeal.setYear(month.getYear());
		paperDeal.setComment("");
		
		return paperDeal;
	}
}