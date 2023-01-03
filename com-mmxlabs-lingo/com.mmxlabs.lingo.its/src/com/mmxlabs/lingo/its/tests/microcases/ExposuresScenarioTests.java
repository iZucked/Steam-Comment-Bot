/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.inject.name.Names;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.ITSCSVImporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@SuppressWarnings("unused")
@ExtendWith(value = ShiroRunner.class)
@RequireFeature(value = {KnownFeatures.FEATURE_GENERATED_PAPER_DEALS, KnownFeatures.FEATURE_PAPER_DEALS, KnownFeatures.FEATURE_EXPOSURES})
public class ExposuresScenarioTests extends AbstractMicroTestCase {
	
	@NonNull
	@Override
	public IScenarioDataProvider importReferenceData() throws Exception {
		return importReferenceDataForExposures("/referencedata/hedging-test/");
	}
	
	@NonNull
	public static IScenarioDataProvider importReferenceDataForExposures(final String url) throws Exception {

		final @NonNull String urlRoot = AbstractMicroTestCase.class.getResource(url).toString();
		final ITSCSVImporter importer = new ITSCSVImporter();
		importer.importPortData(urlRoot);
		importer.importCostData(urlRoot);
		importer.importEntityData(urlRoot);
		importer.importFleetData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPromptData(urlRoot);
		importer.importContractData(urlRoot);
		importer.importPorfolioData(urlRoot);

		return importer.doImport();
	}
	
	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity("EntityA");
	}

	private static final double delta = 0.01;
	
	/**
	 * Test the initial case with no restrictions
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGeneratedHedgesComputation() throws Exception {
		
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS));

		final CargoModel cm = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		cm.getPaperDeals().clear();
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		pricingModel.getMarketIndices().forEach(mi -> mi.setAutoHedgeEnabled(true));
		
		IOptimiserInjectorService localOverrides = OptimiserInjectorServiceMaker.begin() //
				.makeModule() //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.OPTIMISE_PAPER_PNL)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.RE_HEDGE_WITH_PAPERS)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)).toInstance(Boolean.TRUE)) //
				.buildOverride(ModuleType.Module_LNGTransformerModule)//
				.make();
		
		evaluateWithOverrides(localOverrides, null, scenarioRunner -> {
			Assertions.assertNotNull(scenarioRunner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = scenarioRunner.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertNotNull(schedule.getGeneratedPaperDeals());
			Assertions.assertFalse(schedule.getGeneratedPaperDeals().isEmpty());
			Assertions.assertTrue(schedule.getGeneratedPaperDeals().size() == 4);
			
			PaperDeal brentBuyJan = null;
			PaperDeal brentSellFeb = null;
			PaperDeal jkmBuyJan = null;
			PaperDeal jkmSellFeb = null;
			for (final PaperDeal paperDeal : schedule.getGeneratedPaperDeals()) {
				if ("brent_2017-01_buy".equalsIgnoreCase(paperDeal.getName())) {
					brentBuyJan = paperDeal;
				} else if ("brent_2017-02_sell".equalsIgnoreCase(paperDeal.getName())) {
					brentSellFeb = paperDeal;
				} else if ("jkm_2017-01_buy".equalsIgnoreCase(paperDeal.getName())) {
					jkmBuyJan = paperDeal;
				} else if ("jkm_2017-02_sell".equalsIgnoreCase(paperDeal.getName())) {
					jkmSellFeb = paperDeal;
				}
			}
			Assertions.assertNotNull(brentBuyJan);
			Assertions.assertEquals(42724.0, brentBuyJan.getQuantity(),delta);
			Assertions.assertNotNull(brentSellFeb);
			Assertions.assertEquals(108321.0, brentSellFeb.getQuantity(), delta);
			Assertions.assertNotNull(jkmBuyJan);
			Assertions.assertEquals(3826848.0, jkmBuyJan.getQuantity(), delta);
			Assertions.assertNotNull(jkmSellFeb);
			Assertions.assertEquals(3789894.0, jkmSellFeb.getQuantity(), delta);
		});
	}
	
	/**
	 * Test that some hedges are generated
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGenerateSomeHedges() throws Exception {
		
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS));

		generateTestPapers2();
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		pricingModel.getMarketIndices().forEach(mi -> mi.setAutoHedgeEnabled(true));
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);
		commercialModel.getEntities().forEach(e -> e.setThirdParty(false));
		
		IOptimiserInjectorService localOverrides = OptimiserInjectorServiceMaker.begin() //
				.makeModule() //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.OPTIMISE_PAPER_PNL)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.RE_HEDGE_WITH_PAPERS)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)).toInstance(Boolean.TRUE)) //
				.buildOverride(ModuleType.Module_LNGTransformerModule)//
				.make();
		
		evaluateWithOverrides(localOverrides, null, scenarioRunner -> {
			Assertions.assertNotNull(scenarioRunner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = scenarioRunner.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertNotNull(schedule.getGeneratedPaperDeals());
			Assertions.assertFalse(schedule.getGeneratedPaperDeals().isEmpty());
			Assertions.assertTrue(schedule.getGeneratedPaperDeals().size() == 2);
			
			PaperDeal brentBuyFeb = null;
			PaperDeal jkmBuyJan = null;
			PaperDeal jkmSellFeb = null;
			for (final PaperDeal paperDeal : schedule.getGeneratedPaperDeals()) {
				if ("brent_2017-02_buy".equalsIgnoreCase(paperDeal.getName())) {
					brentBuyFeb = paperDeal;
				} else if ("jkm_2017-02_sell".equalsIgnoreCase(paperDeal.getName())) {
					jkmSellFeb = paperDeal;
				}
			}
			Assertions.assertNotNull(brentBuyFeb);
			Assertions.assertEquals(493007.0, brentBuyFeb.getQuantity(), delta);
			Assertions.assertNotNull(jkmSellFeb);
			Assertions.assertEquals(3789894.0, jkmSellFeb.getQuantity(), delta);
		});
	}
	
	/**
	 * Test that for some paper auto-hedge flag works hedges are generated
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGenerateNoHedges() throws Exception {
		
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS));

		generateTestPapers2();
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		pricingModel.getMarketIndices().forEach(mi -> mi.setAutoHedgeEnabled(false));
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);
		commercialModel.getEntities().forEach(e -> e.setThirdParty(false));
		
		IOptimiserInjectorService localOverrides = OptimiserInjectorServiceMaker.begin() //
				.makeModule() //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.OPTIMISE_PAPER_PNL)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.RE_HEDGE_WITH_PAPERS)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)).toInstance(Boolean.TRUE)) //
				.buildOverride(ModuleType.Module_LNGTransformerModule)//
				.make();
		
		evaluateWithOverrides(localOverrides, null, scenarioRunner -> {
			Assertions.assertNotNull(scenarioRunner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = scenarioRunner.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertNotNull(schedule.getGeneratedPaperDeals());
			Assertions.assertTrue(schedule.getGeneratedPaperDeals().isEmpty());
		});
	}
	
	/**
	 * Test the initial case with third-party entities restrictions
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testGeneratedHedgesComputationWithThirdPartyEntities() throws Exception {
		
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS));
		Assertions.assertTrue(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS));

		final CargoModel cm = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		cm.getPaperDeals().clear();
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioDataProvider);
		commercialModel.getEntities().forEach(e -> e.setThirdParty(true));
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		pricingModel.getMarketIndices().forEach(mi -> mi.setAutoHedgeEnabled(true));
		
		IOptimiserInjectorService localOverrides = OptimiserInjectorServiceMaker.begin() //
				.makeModule() //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.OPTIMISE_PAPER_PNL)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.RE_HEDGE_WITH_PAPERS)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)).toInstance(Boolean.TRUE)) //
				.buildOverride(ModuleType.Module_LNGTransformerModule)//
				.make();
		
		evaluateWithOverrides(localOverrides, null, scenarioRunner -> {
			Assertions.assertNotNull(scenarioRunner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = scenarioRunner.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertNotNull(schedule.getGeneratedPaperDeals());
			Assertions.assertTrue(schedule.getGeneratedPaperDeals().isEmpty());
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
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		pricingModel.getMarketIndices().forEach(mi -> mi.setAutoHedgeEnabled(true));
		cm.getPaperDeals().add(makePaperDeal(pricingModel,"BRENT-Buy-2017-Jan", true, "BRENT_ICE", "Brent_momental_swap", YearMonth.of(2017, 1), 237177.00, 1.00));
		cm.getPaperDeals().add(makePaperDeal(pricingModel,"BRENT-Sell-2017-Feb", false, "BRENT_ICE", "Brent_momental_swap", YearMonth.of(2017, 2), 601329.00, 1.00));
		cm.getPaperDeals().add(makePaperDeal(pricingModel,"JKM-Buy-2017-Jan", true, "jkm", "Jkm_momental_swap", YearMonth.of(2017, 1), 36954.00, 1.00));
	}
	
	private void generateTestPapers2() {
		final CargoModel cm = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		pricingModel.getMarketIndices().forEach(mi -> mi.setAutoHedgeEnabled(true));
		cm.getPaperDeals().add(makePaperDeal(pricingModel,"BRENT-Buy-2017-Jan", true, "BRENT_ICE", "Brent_momental_swap", YearMonth.of(2017, 1), 42724.00, 1.00));
		cm.getPaperDeals().add(makePaperDeal(pricingModel,"BRENT-Sell-2017-Feb", false, "BRENT_ICE", "Brent_momental_swap", YearMonth.of(2017, 2), 601329.00, 1.00));
		cm.getPaperDeals().add(makePaperDeal(pricingModel,"JKM-Buy-2017-Jan", true, "jkm", "Jkm_momental_swap", YearMonth.of(2017, 1), 3826848.00, 1.00));
	}
	
	private PaperDeal makePaperDeal(final PricingModel pricingModel, final String name, final boolean isPurchase, final String curveName, final String instrument, final YearMonth month, final double quantity, final double price) {
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