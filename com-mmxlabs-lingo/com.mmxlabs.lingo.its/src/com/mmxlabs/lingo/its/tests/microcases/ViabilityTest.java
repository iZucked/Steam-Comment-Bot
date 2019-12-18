/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ViabilityResult;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.analytics.ui.views.viability.ViabilitySandboxEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.viability.ViabilityUtils;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Test viability sandbox evaluator
 * 
 * @author FM
 *
 */
@SuppressWarnings("unused")
@ExtendWith(value = ShiroRunner.class)
public class ViabilityTest extends AbstractMicroTestCase {
	
	@Override
	@NonNull
	public IScenarioDataProvider importReferenceData() throws MalformedURLException {
		return importReferenceData("/referencedata/viability/full-micro-scenario/");
	}
	
	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity("Entity");
	}
	
	@NonNull
	public static IScenarioDataProvider importReferenceData(final String url) throws MalformedURLException {

		final @NonNull String urlRoot = AbstractMicroTestCase.class.getResource(url).toString();
		final CSVImporter importer = new CSVImporter();
		importer.importPortData(urlRoot);
		importer.importCostData(urlRoot);
		importer.importEntityData(urlRoot);
		importer.importFleetData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPromptData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importSpotMarkets(urlRoot);
		importer.importPorfolioData(urlRoot);
		importer.importStandardComponents(urlRoot);

		return importer.doImport();
	}
	
	@Test
	@Tag(TestCategories.MICRO_TEST)	public void testFullScenario() throws Exception {
		
		evaluateTest(null, null, scenarioRunner -> {
		});
		
		ViabilityModel model = ViabilityUtils.createModelFromScenario(lngScenarioModel, "test");
		
		ViabilitySandboxEvaluator.evaluate(scenarioDataProvider, null, model);
		
		final Map<Pair<LoadSlot,VesselAvailability>, List<ViabilityResult>> checks = getResultingModel();
		
		for (ViabilityRow vr : model.getRows()) {
			final ShippingOption so = vr.getShipping();
			Assertions.assertNotNull(so);
			VesselAvailability va = null;
			
			if (so instanceof ExistingVesselCharterOption) {
				va = ((ExistingVesselCharterOption)so).getVesselCharter();
			}
			
			Assertions.assertNotNull(va);
			
			final BuyOption bo = vr.getBuyOption();
			if (bo instanceof BuyReference) {
				final BuyReference br = (BuyReference) bo;
				final LoadSlot ls = br.getSlot();
				Assertions.assertNotNull(ls);
				
				Pair<LoadSlot,VesselAvailability> one = new Pair<LoadSlot, VesselAvailability>(ls, va);
				
				final List<ViabilityResult> actualResults = vr.getLhsResults();
				final List<ViabilityResult> inputResults = checks.get(one);
				
				if (actualResults == null || inputResults == null) {
					continue;
				}
				
				Assertions.assertTrue( actualResults.size() == inputResults.size());
				Assertions.assertTrue( doCheck(actualResults, inputResults));
			}
		}
	}
	
	/*
	 * do check
	 */
	private boolean doCheck(final List<ViabilityResult> list1, final List<ViabilityResult> list2) {
		if (list1.size() == 0 && list2.size() ==0) {
			return true;
		}
		boolean result = false;
		
		for (ViabilityResult cvr1 : list1) {
			result = false;
			for (ViabilityResult cvr2 : list2) {
				if (cvr2.getEarliestETA().getDayOfYear() == cvr1.getEarliestETA().getDayOfYear()) {
					result = true;
					break;
				}
			}
		}

		return result;
	}
	
	/*
	 * Creating results. Need shipping!
	 */
	private Map<Pair<LoadSlot,VesselAvailability>, List<ViabilityResult>>  getResultingModel(){
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
		
		List<ViabilityResult> vls = new ArrayList<ViabilityResult>();
		final Map<Pair<LoadSlot,VesselAvailability>, List<ViabilityResult>> results2 = 
				new HashMap<Pair<LoadSlot,VesselAvailability>, List<ViabilityResult>>();
		
		//PF_1
		vls.add(createResult(findMarketByName(spotModel, "DES_Argentina"), //
				3100000,3100000,
				6.414517, 6.471318,
				LocalDate.of(2017,5,18),LocalDate.of(2017,5,26)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Egypt"), //
				3532074,3431177,
				6.289512, 6.369667,
				LocalDate.of(2017,5,22),LocalDate.of(2017,5,31)));
		vls.add(createResult(findMarketByName(spotModel, "DES_China"), //
				3491128,3431177,
				6.249257, 6.13059,
				LocalDate.of(2017,6,2),LocalDate.of(2017,6,11)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Pakistan"), //
				3509821,3431177,
				6.290509, 6.18566,
				LocalDate.of(2017,5,28),LocalDate.of(2017,6,6)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Brazil"), //
				3565900,3431919,
				6.067916, 6.198547,
				LocalDate.of(2017,5,12),LocalDate.of(2017,5,25)));
		vls.add(createResult(findMarketByName(spotModel, "DES_NWE"), //
				3434589,3431177,
				6.166371, 6.310748,
				LocalDate.of(2017,5,17),LocalDate.of(2017,5,22)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Japan"), //
				3501958,3431177,
				6.269977, 6.243184,
				LocalDate.of(2017,5,30),LocalDate.of(2017,6,9)));
		//PF_1
		Pair<LoadSlot, VesselAvailability> one = new Pair<LoadSlot, VesselAvailability>(findSlot(cargoModel, "PF_1"),
				findVesselAvailability(cargoModel, "TFDE A"));
		results2.put(one, vls);
		vls.clear();
		
		//BO_2
		vls.add(createResult(findMarketByName(spotModel, "DES_Argentina"), //
				3100000,3100000,
				6.414517, 6.471318,
				LocalDate.of(2017,5,18),LocalDate.of(2017,5,26)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Egypt"), //
				3532074,3431177,
				6.289512, 6.369667,
				LocalDate.of(2017,5,22),LocalDate.of(2017,5,31)));
		vls.add(createResult(findMarketByName(spotModel, "DES_China"), //
				3491128,3431177,
				6.249257, 6.13059,
				LocalDate.of(2017,6,2),LocalDate.of(2017,6,11)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Pakistan"), //
				3509821,3431177,
				6.290509, 6.18566,
				LocalDate.of(2017,5,28),LocalDate.of(2017,6,6)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Brazil"), //
				3565900,3431919,
				6.067916, 6.198547,
				LocalDate.of(2017,5,12),LocalDate.of(2017,5,25)));
		vls.add(createResult(findMarketByName(spotModel, "DES_NWE"), //
				3434589,3431177,
				6.166371, 6.310748,
				LocalDate.of(2017,5,17),LocalDate.of(2017,5,22)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Japan"), //
				3501958,3431177,
				6.269977, 6.243184,
				LocalDate.of(2017,5,30),LocalDate.of(2017,6,9)));
		//BO_2
		one = new Pair<LoadSlot, VesselAvailability>(findSlot(cargoModel, "BO_2"),
				findVesselAvailability(cargoModel, "TFDE A"));
		results2.put(one, vls);
		vls.clear();
		
		
		//PF_1
		vls.add(createResult(findMarketByName(spotModel, "DES_Pakistan"), //
				3356880,3301523,
				5.716716, 5.823119,
				LocalDate.of(2017,5,28),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_China"), //
				3332058,3301523,
				5.781993, 5.786772,
				LocalDate.of(2017,6,2),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Argentina"), //
				3100000,3100000,
				5.524911, 6.003594,
				LocalDate.of(2017,5,18),LocalDate.of(2017,6,8)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Egypt"), //
				3386430,3301523,
				5.592419, 5.850525,
				LocalDate.of(2017,5,22),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Brazil"), //
				3431346,3305069,
				5.197951, 5.799277,
				LocalDate.of(2017,5,12),LocalDate.of(2017,6,8)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Japan"), //
				3346439,3301523,
				5.750681, 5.8489,
				LocalDate.of(2017,5,30),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_NWE"), //
				3407115,3304084,
				5.285645, 5.774048,
				LocalDate.of(2017,5,17),LocalDate.of(2017,6,9)));
		//PF_1
		one = new Pair<LoadSlot, VesselAvailability>(findSlot(cargoModel, "PF_1"),
				findVesselAvailability(cargoModel, "Charter A"));
		results2.put(one, vls);
		vls.clear();
		
		//BO_2
		vls.add(createResult(findMarketByName(spotModel, "DES_Pakistan"), //
				3356880,3301523,
				5.716716, 5.823119,
				LocalDate.of(2017,5,28),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_China"), //
				3332058,3301523,
				5.781993, 5.786772,
				LocalDate.of(2017,6,2),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Argentina"), //
				3100000,3100000,
				5.524911, 6.003594,
				LocalDate.of(2017,5,18),LocalDate.of(2017,6,8)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Egypt"), //
				3386430,3301523,
				5.592419, 5.850525,
				LocalDate.of(2017,5,22),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Brazil"), //
				3431346,3305069,
				5.197951, 5.799277,
				LocalDate.of(2017,5,12),LocalDate.of(2017,6,8)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Japan"), //
				3346439,3301523,
				5.750681, 5.8489,
				LocalDate.of(2017,5,30),LocalDate.of(2017,6,9)));
		vls.add(createResult(findMarketByName(spotModel, "DES_NWE"), //
				3407115,3304084,
				5.285645, 5.774048,
				LocalDate.of(2017,5,17),LocalDate.of(2017,6,9)));
		//BO_2
		one = new Pair<LoadSlot, VesselAvailability>(findSlot(cargoModel, "BO_2"),
				findVesselAvailability(cargoModel, "Charter A"));
		results2.put(one, vls);
		vls.clear();
		
		//PF_1
		vls.add(createResult(findMarketByName(spotModel, "DES_China"), //
				3622547,3509079,
				5.752335, 5.867221,
				LocalDate.of(2017,3,6),LocalDate.of(2017,3,25)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Pakistan"), //
				3601152,3508024,
				5.859914, 5.92596,
				LocalDate.of(2017,3,11),LocalDate.of(2017,3,19)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Japan"), //
				3615466,3508024,
				5.804819, 5.914164,
				LocalDate.of(2017,3,8),LocalDate.of(2017,3,26)));
		//PF_1
		one = new Pair<LoadSlot, VesselAvailability>(findSlot(cargoModel, "PF_1"),
				findVesselAvailability(cargoModel, "TFDE B"));
		results2.put(one, vls);
		vls.clear();
		
		//BO_2
		vls.add(createResult(findMarketByName(spotModel, "DES_China"), //
				3622547,3509079,
				5.752335, 5.867221,
				LocalDate.of(2017,3,6),LocalDate.of(2017,3,25)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Pakistan"), //
				3601152,3508024,
				5.859914, 5.92596,
				LocalDate.of(2017,3,11),LocalDate.of(2017,3,19)));
		vls.add(createResult(findMarketByName(spotModel, "DES_Japan"), //
				3615466,3508024,
				5.804819, 5.914164,
				LocalDate.of(2017,3,8),LocalDate.of(2017,3,26)));
		//BO_2
		one = new Pair<LoadSlot, VesselAvailability>(findSlot(cargoModel, "BO_2"),
				findVesselAvailability(cargoModel, "TFDE B"));
		results2.put(one, vls);
		vls.clear();
		
		return results2;
	}
	
	/* 
	 * find load slot by name
	 * 
	 */
	private LoadSlot findSlot(final CargoModel cargoModel, final @NonNull String name) {
		for (final LoadSlot load : cargoModel.getLoadSlots()) {
			if (load.getName().contains(name)) {
				return load;
			}
		}
		return null;
	}
	
	
	/* 
	 * find vessel availability slot by name
	 * 
	 */
	private VesselAvailability findVesselAvailability(final CargoModel cargoModel, final @NonNull String name) {
		for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
			if (va == null) continue;
			final Vessel v = va.getVessel();
			if (v == null) continue;
			
			if (v.getName().contains(name)) {
				return va;
			}
		}
		return null;
	}
	
	/* 
	 * find spot market by name
	 * returns first found
	 */
	private SpotMarket findMarketByName(final SpotMarketsModel spotModel, final @NonNull String name) {
		final SpotMarketGroup smgDS = spotModel.getDesSalesSpotMarket();
		if (smgDS != null) {
			for (final SpotMarket spotMarket : smgDS.getMarkets()) {
				if (spotMarket != null) {
					if (spotMarket.getName().contains(name)) {
						return spotMarket;
					}
				}
			}
		}
		final SpotMarketGroup smgFS = spotModel.getFobSalesSpotMarket();
		if (smgFS != null) {
			for (final SpotMarket spotMarket : smgFS.getMarkets()) {
				if (spotMarket != null) {
					if (spotMarket.getName().contains(name)) {
						return spotMarket;
					}
				}
			}
		}
		return null;
	}
	/*
	 * Assembling results
	 */
	private ViabilityResult createResult(final SpotMarket target, 
			final int ev, 
			final int lv,
			final double ep, 
			final double lp,
			final LocalDate ea,
			final LocalDate la) {
		final ViabilityResult result = AnalyticsFactory.eINSTANCE.createViabilityResult();
		
		result.setTarget(target);
		result.setEarliestETA(ea);
		result.setEarliestPrice(ep);
		result.setEarliestVolume(ev);
		result.setLatestETA(la);
		result.setLatestPrice(lp);
		result.setEarliestVolume(lv);
		
		return result;
	}
	
}
