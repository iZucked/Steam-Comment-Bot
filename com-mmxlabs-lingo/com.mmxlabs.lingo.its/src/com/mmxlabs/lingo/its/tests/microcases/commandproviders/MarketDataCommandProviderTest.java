/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.commandproviders;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * These test the AssignableElementCommandProvider to ensure the SequenceHint is reset to 0 when data that could influence it's value has changed.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class MarketDataCommandProviderTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeOtherData_SettledPrices() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		String currentVersion = pricingModel.getMarketCurveDataVersion();
		Assert.assertNotNull(currentVersion);

		List<DatePointContainer> settledPrices = new LinkedList<>();
		DatePointContainer curve1 = PricingFactory.eINSTANCE.createDatePointContainer();
		curve1.setName("name");
		settledPrices.add(curve1);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES, settledPrices)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		DatePoint dp = PricingFactory.eINSTANCE.createDatePoint();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, curve1, PricingPackage.Literals.DATE_POINT_CONTAINER__POINTS, dp)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, dp, PricingPackage.Literals.DATE_POINT__VALUE, 1.0)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, dp)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeOtherData_ConversionFactors() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		String currentVersion = pricingModel.getMarketCurveDataVersion();
		Assert.assertNotNull(currentVersion);

		UnitConversion factor = PricingFactory.eINSTANCE.createUnitConversion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS, factor)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, factor, PricingPackage.Literals.UNIT_CONVERSION__FROM, "from")));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, factor, PricingPackage.Literals.UNIT_CONVERSION__TO, "to")));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, factor, PricingPackage.Literals.UNIT_CONVERSION__FACTOR, 1.0)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, factor)));
		Assert.assertEquals(currentVersion, pricingModel.getMarketCurveDataVersion());

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCommodityIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		String currentVersion = pricingModel.getMarketCurveDataVersion();
		Assert.assertNotNull(currentVersion);

		CommodityIndex curve1 = PricingFactory.eINSTANCE.createCommodityIndex();
		DerivedIndex<Double> dp = PricingFactory.eINSTANCE.createDerivedIndex();
		curve1.setData(dp);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, dp, PricingPackage.Literals.DERIVED_INDEX__EXPRESSION, "5")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCommodityIndex_Data() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		String currentVersion = pricingModel.getMarketCurveDataVersion();
		Assert.assertNotNull(currentVersion);

		CommodityIndex curve1 = PricingFactory.eINSTANCE.createCommodityIndex();
		DataIndex<Double> dp = PricingFactory.eINSTANCE.createDataIndex();
		curve1.setData(dp);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		IndexPoint<Double> pt1 = PricingFactory.eINSTANCE.createIndexPoint();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, dp, PricingPackage.Literals.DATA_INDEX__POINTS, pt1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, pt1, PricingPackage.Literals.INDEX_POINT__VALUE, 1.0)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, pt1, PricingPackage.Literals.INDEX_POINT__DATE, YearMonth.now())));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, pt1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCurrencyIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		String currentVersion = pricingModel.getMarketCurveDataVersion();
		Assert.assertNotNull(currentVersion);

		CurrencyIndex curve1 = PricingFactory.eINSTANCE.createCurrencyIndex();
		DerivedIndex<Double> dp = PricingFactory.eINSTANCE.createDerivedIndex();
		curve1.setData(dp);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, dp, PricingPackage.Literals.DERIVED_INDEX__EXPRESSION, "5")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeBaseFuelIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		String currentVersion = pricingModel.getMarketCurveDataVersion();
		Assert.assertNotNull(currentVersion);

		BaseFuelIndex curve1 = PricingFactory.eINSTANCE.createBaseFuelIndex();
		DerivedIndex<Double> dp = PricingFactory.eINSTANCE.createDerivedIndex();
		curve1.setData(dp);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, dp, PricingPackage.Literals.DERIVED_INDEX__EXPRESSION, "5")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCharterIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		String currentVersion = pricingModel.getMarketCurveDataVersion();
		Assert.assertNotNull(currentVersion);

		CharterIndex curve1 = PricingFactory.eINSTANCE.createCharterIndex();
		DerivedIndex<Integer> dp = PricingFactory.eINSTANCE.createDerivedIndex();
		curve1.setData(dp);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT, "name2")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, dp, PricingPackage.Literals.DERIVED_INDEX__EXPRESSION, "5")));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		Assert.assertNotEquals(currentVersion, pricingModel.getMarketCurveDataVersion());
		currentVersion = pricingModel.getMarketCurveDataVersion();
	}

	private EditingDomain createEditingDomain(final LNGScenarioModel scenarioModel) {

		// Create the editing domain with a special command stack.
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource r = new ResourceImpl();
		r.getContents().add(scenarioModel);
		resourceSet.getResources().add(r);

		return ScenarioStorageUtil.initEditingDomain(null, resourceSet, scenarioModel).getFirst();
	}

}