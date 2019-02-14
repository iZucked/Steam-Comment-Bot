/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.commandproviders;

import java.time.YearMonth;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
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
	enum ExpectedChange {
		NONE, MARKET, SETTLED
	}

	class VersionData {
		String market;

		public VersionData(final PricingModel m) {
			market = m.getMarketCurveDataVersion();

			Assert.assertNotNull(market);
		}
	}

	protected void checkAndUpdate(final PricingModel pricingModel, final VersionData currentVersion, final ExpectedChange... changeTypes) {
		// None is just to make the API call easier.
		final Set<ExpectedChange> changeTypesSet = EnumSet.of(ExpectedChange.NONE, changeTypes);
		if (changeTypesSet.contains(ExpectedChange.MARKET)) {
			Assert.assertNotEquals(currentVersion.market, pricingModel.getMarketCurveDataVersion());
			currentVersion.market = pricingModel.getMarketCurveDataVersion();
		}

		// Lazy duplicated check here
		Assert.assertEquals(currentVersion.market, pricingModel.getMarketCurveDataVersion());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeOtherData_SettledPrices() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		VersionData currentVersion = new VersionData(pricingModel);

		List<DatePointContainer> settledPrices = new LinkedList<>();
		DatePointContainer curve1 = PricingFactory.eINSTANCE.createDatePointContainer();
		curve1.setName("name");
		settledPrices.add(curve1);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES, settledPrices)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.SETTLED);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.SETTLED);
		DatePoint dp = PricingFactory.eINSTANCE.createDatePoint();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, curve1, PricingPackage.Literals.DATE_POINT_CONTAINER__POINTS, dp)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.SETTLED);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, dp, PricingPackage.Literals.DATE_POINT__VALUE, 1.0)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.SETTLED);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, dp)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.SETTLED);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.SETTLED);

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeOtherData_ConversionFactors() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		VersionData currentVersion = new VersionData(pricingModel);

		UnitConversion factor = PricingFactory.eINSTANCE.createUnitConversion();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS, factor)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, factor, PricingPackage.Literals.UNIT_CONVERSION__FROM, "from")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, factor, PricingPackage.Literals.UNIT_CONVERSION__TO, "to")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, factor, PricingPackage.Literals.UNIT_CONVERSION__FACTOR, 1.0)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.NONE);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, factor)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.NONE);
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCommodityIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		VersionData currentVersion = new VersionData(pricingModel);

		CommodityCurve curve1 = PricingFactory.eINSTANCE.createCommodityCurve();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION, "5")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCommodityIndex_Data() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		VersionData currentVersion = new VersionData(pricingModel);

		CommodityCurve curve1 = PricingFactory.eINSTANCE.createCommodityCurve();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		YearMonthPoint pt1 = PricingFactory.eINSTANCE.createYearMonthPoint();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, curve1, PricingPackage.Literals.YEAR_MONTH_POINT_CONTAINER__POINTS, pt1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, pt1, PricingPackage.Literals.YEAR_MONTH_POINT__VALUE, 1.0)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, pt1, PricingPackage.Literals.YEAR_MONTH_POINT__DATE, YearMonth.now())));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, pt1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCurrencyIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		VersionData currentVersion = new VersionData(pricingModel);

		CurrencyCurve curve1 = PricingFactory.eINSTANCE.createCurrencyCurve();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__CURRENCY_CURVES, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION, "5")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeBaseFuelIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		VersionData currentVersion = new VersionData(pricingModel);

		BunkerFuelCurve curve1 = PricingFactory.eINSTANCE.createBunkerFuelCurve();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__BUNKER_FUEL_CURVES, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION, "5")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeCharterIndex_Expression() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		VersionData currentVersion = new VersionData(pricingModel);

		CharterCurve curve1 = PricingFactory.eINSTANCE.createCharterCurve();

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(AddCommand.create(domain, pricingModel, PricingPackage.Literals.PRICING_MODEL__CHARTER_CURVES, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, MMXCorePackage.Literals.NAMED_OBJECT__NAME, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT, "name2")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, curve1, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION, "5")));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(DeleteCommand.create(domain, curve1)));
		checkAndUpdate(pricingModel, currentVersion, ExpectedChange.MARKET);
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