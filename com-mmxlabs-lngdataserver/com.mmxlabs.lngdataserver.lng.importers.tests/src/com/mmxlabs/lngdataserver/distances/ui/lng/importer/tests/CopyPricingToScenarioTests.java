/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.pricing.model.CurvePoint;
import com.mmxlabs.lngdataserver.integration.pricing.model.CurveType;
import com.mmxlabs.lngdataserver.integration.pricing.model.DataCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.ExpressionCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.lng.io.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.pricing.PricingToScenarioCopier;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.provider.PricingItemProviderAdapterFactory;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;

public class CopyPricingToScenarioTests {

	@Test
	public void testCleanImport() throws Exception {
		final String input = DataLoader.importData("pricing-testdata.json");

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final PricingVersion originalVersion = mapper.readerFor(PricingVersion.class).readValue(input);

		prepareVersionModel(originalVersion);

		final String expectedResult = serialise(mapper, originalVersion);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.setMarketCurvesVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());
		final EditingDomain editingDomain = createLocalEditingDomain(pricingModel);

		final Command updateCommand = PricingToScenarioCopier.getUpdateCommand(editingDomain, pricingModel, originalVersion);
		Assertions.assertTrue(updateCommand.canExecute());
		editingDomain.getCommandStack().execute(updateCommand);

		final PricingVersion derivedVersion = PricingFromScenarioCopier.generateVersion(pricingModel);
		// prepareVersionModel(derivedVersion);
		// To help ensure diff matches
		derivedVersion.setCreatedAt(originalVersion.getCreatedAt());
		// Deprecated!
		derivedVersion.getCurves().clear();
		prepareVersionModel(derivedVersion);

		String derivedJSON = serialise(mapper, derivedVersion);

		Assertions.assertEquals(expectedResult, derivedJSON);

	}

	private void prepareVersionModel(PricingVersion originalVersion) {
		Collections.sort(originalVersion.getCurvesList(), (a, b) -> {
			int c = a.getName().compareTo(b.getName());
			if (c == 0) {
				c = a.getType().compareTo(b.getType());
			}
			return c;
		});
	}

	/**
	 * Expected to fail as we use a map so clashing data will overwrite
	 * 
	 * @throws Exception
	 */

	@Test
	public void testCopyDistances() {

		final List<CommodityCurve> commodityIndices = new ArrayList<>();
		final List<CurrencyCurve> currencyIndices = new ArrayList<>();

		final CommodityCurve c1 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityCurve();
		c1.setName("HH");
		commodityIndices.add(c1);

		final CommodityCurve c2 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityCurve();
		c2.setName("II");
		commodityIndices.add(c2);

		final CurrencyCurve cu1 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyCurve();
		cu1.setName("USD");
		currencyIndices.add(cu1);

		final CurrencyCurve cu2 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyCurve();
		cu2.setName("EUR");
		currencyIndices.add(cu2);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.setMarketCurvesVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());

		pricingModel.getCommodityCurves().addAll(commodityIndices);
		pricingModel.getCurrencyCurves().addAll(currencyIndices);

		Assertions.assertFalse(pricingModel.getCurrencyCurves().isEmpty());

		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assertions.assertNotNull(updatePricingCommand);

		Assertions.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityCurve> potentialHH = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assertions.assertTrue(potentialHH.isPresent());
		Assertions.assertEquals("USD", potentialHH.get().getCurrencyUnit());
		Assertions.assertEquals("m3", potentialHH.get().getVolumeUnit());
		Assertions.assertEquals(7.1, potentialHH.get().valueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assertions.assertEquals(7.2, potentialHH.get().valueForMonth(YearMonth.of(2017, 2)), 0.01);

		final Optional<CommodityCurve> potentialII = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("II")).findAny();
		Assertions.assertTrue(potentialII.isPresent());
		Assertions.assertEquals("USD", potentialII.get().getCurrencyUnit());
		Assertions.assertEquals("m3", potentialII.get().getVolumeUnit());
		Assertions.assertEquals(7.3, potentialII.get().valueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assertions.assertEquals(7.4, potentialII.get().valueForMonth(YearMonth.of(2017, 2)), 0.01);

		// Currency curve is no longer present
		Assertions.assertTrue(pricingModel.getCurrencyCurves().isEmpty());

	}

	@Test
	public void testPeriodInNames() {

		final List<CommodityCurve> commodityIndices = new ArrayList<>();
		final List<CurrencyCurve> currencyIndices = new ArrayList<>();

		final CommodityCurve c1 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityCurve();
		c1.setName("HH.extra");
		commodityIndices.add(c1);

		final CommodityCurve c2 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityCurve();
		c2.setName("II.extra");
		commodityIndices.add(c2);

		final CurrencyCurve cu1 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyCurve();
		cu1.setName("USD.extra");

		currencyIndices.add(cu1);

		final CurrencyCurve cu2 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyCurve();
		cu2.setName("EUR.extra");

		currencyIndices.add(cu2);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.setMarketCurvesVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());

		pricingModel.getCommodityCurves().addAll(commodityIndices);
		pricingModel.getCurrencyCurves().addAll(currencyIndices);

		Assertions.assertFalse(pricingModel.getCurrencyCurves().isEmpty());

		final PricingVersion derivedVersion = PricingFromScenarioCopier.generateVersion(pricingModel);

		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final PricingVersion pricingVersion = createExtraPricingVersion();

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assertions.assertNotNull(updatePricingCommand);

		Assertions.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityCurve> potentialHH = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("HH.extra")).findAny();
		Assertions.assertTrue(potentialHH.isPresent());
		Assertions.assertEquals("USD", potentialHH.get().getCurrencyUnit());
		Assertions.assertEquals("m3", potentialHH.get().getVolumeUnit());
		Assertions.assertEquals(7.1, potentialHH.get().valueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assertions.assertEquals(7.2, potentialHH.get().valueForMonth(YearMonth.of(2017, 2)), 0.01);

		final Optional<CommodityCurve> potentialII = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("II.extra")).findAny();
		Assertions.assertTrue(potentialII.isPresent());
		Assertions.assertEquals("USD", potentialII.get().getCurrencyUnit());
		Assertions.assertEquals("m3", potentialII.get().getVolumeUnit());
		Assertions.assertEquals(7.3, potentialII.get().valueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assertions.assertEquals(7.4, potentialII.get().valueForMonth(YearMonth.of(2017, 2)), 0.01);

		// Currency curve is no longer present
		Assertions.assertTrue(pricingModel.getCurrencyCurves().isEmpty());

	}

	@Test
	public void testIndexNotPreviouslyExisting() {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.setMarketCurvesVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());

		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityCurve> potentialHH = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assertions.assertTrue(potentialHH.isPresent());
		Assertions.assertEquals(7.1, potentialHH.get().valueForMonth(YearMonth.of(2017, 1)), 0.01);
	}

	@Test
	public void testExistingPointsRemoved() {
		final List<CommodityCurve> commodityIndices = new ArrayList<>();
		final List<CurrencyCurve> currencyIndices = new ArrayList<>();

		final CommodityCurve c1 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityCurve();
		c1.setName("HH");
		commodityIndices.add(c1);

		final YearMonthPoint oldData1 = PricingPackage.eINSTANCE.getPricingFactory().createYearMonthPoint();
		oldData1.setDate(YearMonth.of(2017, 1));
		oldData1.setValue(7.0d);

		final YearMonthPoint oldData2 = PricingPackage.eINSTANCE.getPricingFactory().createYearMonthPoint();
		oldData2.setDate(YearMonth.of(2016, 12));
		oldData2.setValue(6.9d);

		c1.getPoints().add(oldData2);
		c1.getPoints().add(oldData1);

		final CommodityCurve c2 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityCurve();
		c2.setName("II");
		commodityIndices.add(c2);

		final CurrencyCurve cu1 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyCurve();
		cu1.setName("USD");
		currencyIndices.add(cu1);

		final CurrencyCurve cu2 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyCurve();
		cu2.setName("EUR");
		currencyIndices.add(cu2);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.setMarketCurvesVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());

		pricingModel.getCommodityCurves().addAll(commodityIndices);
		pricingModel.getCurrencyCurves().addAll(currencyIndices);

		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		final Optional<CommodityCurve> potentialHH = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assertions.assertTrue(potentialHH.isPresent());
		Assertions.assertEquals(6.9, potentialHH.get().valueForMonth(YearMonth.of(2016, 12)), 0.01);

		Assertions.assertNotNull(updatePricingCommand);

		Assertions.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		Assertions.assertTrue(potentialHH.isPresent());
		Assertions.assertEquals(7.1, potentialHH.get().valueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assertions.assertNull(potentialHH.get().valueForMonth(YearMonth.of(2016, 12)));
	}

	@Test
	public void testExpressionCurve() {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.setMarketCurvesVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());

		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final ExpressionCurve c1 = new ExpressionCurve("REL_HH", CurveType.COMMODITY, null, null, null, "105%HH");

		pricingVersion.getCurves().put(c1.getName(), c1);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assertions.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityCurve> potentialRel = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("REL_HH")).findAny();
		Assertions.assertTrue(potentialRel.isPresent());
		Assertions.assertEquals("105%HH", potentialRel.get().getExpression());
	}

	@Test
	public void testValues() {
		// test both double and integer values
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.setMarketCurvesVersionRecord(MMXCoreFactory.eINSTANCE.createVersionRecord());

		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final DataCurve c1 = new DataCurve("COOL", CurveType.CHARTER, null, "day", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 8000d), new CurvePoint(LocalDate.of(2017, 2, 1), 9000d)));

		pricingVersion.getCurves().put(c1.getName(), c1);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assertions.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CharterCurve> potentialCool = pricingModel.getCharterCurves().stream().filter(i -> i.getName().equals("COOL")).findAny();
		Assertions.assertTrue(potentialCool.isPresent());
		Assertions.assertEquals(8000, potentialCool.get().valueForMonth(YearMonth.of(2017, 1)).intValue());

		final Optional<CommodityCurve> potentialHH = pricingModel.getCommodityCurves().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assertions.assertTrue(potentialHH.isPresent());
		Assertions.assertEquals(7.1d, potentialHH.get().valueForMonth(YearMonth.of(2017, 1)).doubleValue(), 0.01);
	}

	private static PricingVersion createDefaultPricingVersion() {

		final PricingVersion version = new PricingVersion();
		final Map<String, Curve> m = new HashMap<>();

		final DataCurve c1 = new DataCurve("HH", CurveType.COMMODITY, null, "m3", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 7.1d), new CurvePoint(LocalDate.of(2017, 2, 1), 7.2d)));
		final DataCurve c2 = new DataCurve("II", CurveType.COMMODITY, null, "m3", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 7.3d), new CurvePoint(LocalDate.of(2017, 2, 1), 7.4d)));

		m.put(c1.getName(), c1);
		m.put(c2.getName(), c2);

		version.setCurves(m);

		return version;
	}

	private static PricingVersion createClashingPricingVersion() {

		final PricingVersion version = new PricingVersion();
		final Map<String, Curve> m = new HashMap<>();

		final DataCurve c1 = new DataCurve("HH", CurveType.COMMODITY, null, "m3", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 7.1d), new CurvePoint(LocalDate.of(2017, 2, 1), 7.2d)));
		final DataCurve c2 = new DataCurve("II", CurveType.COMMODITY, null, "m3", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 7.3d), new CurvePoint(LocalDate.of(2017, 2, 1), 7.4d)));

		m.put(c1.getName(), c1);
		m.put(c2.getName(), c2);

		version.setCurves(m);

		return version;
	}

	private static PricingVersion createExtraPricingVersion() {

		final PricingVersion version = new PricingVersion();
		final Map<String, Curve> m = new HashMap<>();

		final DataCurve c1 = new DataCurve("HH.extra", CurveType.COMMODITY, null, "m3", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 7.1d), new CurvePoint(LocalDate.of(2017, 2, 1), 7.2d)));
		final DataCurve c2 = new DataCurve("II.extra", CurveType.COMMODITY, null, "m3", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 7.3d), new CurvePoint(LocalDate.of(2017, 2, 1), 7.4d)));

		m.put(Curve.encodedName(c1.getName()), c1);
		m.put(Curve.encodedName(c2.getName()), c2);

		version.setCurves(m);

		return version;
	}

	@NonNull
	public static EditingDomain createLocalEditingDomain(final EObject rootObject) {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new PricingItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
		final ResourceImpl r = new ResourceImpl();

		ed.getResourceSet().getResources().add(r);
		r.getContents().add(rootObject);
		return ed;
	}

	private String serialise(final ObjectMapper mapper, final PricingVersion version) throws IOException, JsonGenerationException, JsonMappingException {
		final StringWriter writer = new StringWriter();
		mapper.writerWithDefaultPrettyPrinter().writeValue(writer, version);
		final String result = writer.toString();
		return result;
	}

}
