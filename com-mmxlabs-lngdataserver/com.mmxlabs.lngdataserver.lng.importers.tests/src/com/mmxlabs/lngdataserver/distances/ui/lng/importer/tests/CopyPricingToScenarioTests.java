/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
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
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.pricing.model.CurvePoint;
import com.mmxlabs.lngdataserver.integration.pricing.model.CurveType;
import com.mmxlabs.lngdataserver.integration.pricing.model.DataCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.ExpressionCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.lng.exporters.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.exporters.pricing.PricingFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.distances.PortAndDistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.port.PortsToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.pricing.PricingToScenarioCopier;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.provider.PricingItemProviderAdapterFactory;

public class CopyPricingToScenarioTests {

	@Test
	public void testCleanImport() throws Exception {
		final String input = DataLoader.importData("pricing-testdata.json");

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final PricingVersion originalVersion = mapper.readerFor(PricingVersion.class).readValue(input);
		// prepareVersionModel(originalVersion);

		final String expectedResult = serialise(mapper, originalVersion);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		final EditingDomain editingDomain = createLocalEditingDomain(pricingModel);

		final Command updateCommand = PricingToScenarioCopier.getUpdateCommand(editingDomain, pricingModel, originalVersion);
		editingDomain.getCommandStack().execute(updateCommand);

		final PricingVersion derivedVersion = PricingFromScenarioCopier.generateVersion(pricingModel);
		// prepareVersionModel(derivedVersion);
		// To help ensure diff matches
		derivedVersion.setCreatedAt(originalVersion.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assert.assertEquals(expectedResult, derivedJSON);

	}

	/**
	 * Expected to fail as we use a map so clashing data will overwrite
	 * 
	 * @throws Exception
	 */

	@Test
	public void testCleanImportWithClash() throws Exception {
		final String input = DataLoader.importData("pricing-testdata-withclash.json");

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final PricingVersion originalVersion = mapper.readerFor(PricingVersion.class).readValue(input);
		// prepareVersionModel(originalVersion);

		final String expectedResult = serialise(mapper, originalVersion);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		final EditingDomain editingDomain = createLocalEditingDomain(pricingModel);

		final Command updateCommand = PricingToScenarioCopier.getUpdateCommand(editingDomain, pricingModel, originalVersion);
		editingDomain.getCommandStack().execute(updateCommand);

		Assert.assertEquals(2, pricingModel.getCommodityIndices().size());
		Assert.assertEquals(1, pricingModel.getCurrencyIndices().size());

		final PricingVersion derivedVersion = PricingFromScenarioCopier.generateVersion(pricingModel);
		// prepareVersionModel(derivedVersion);
		// To help ensure diff matches
		derivedVersion.setCreatedAt(originalVersion.getCreatedAt());

		String derivedJSON = serialise(mapper, derivedVersion);

		Assert.assertEquals(expectedResult, derivedJSON);

	}

	@Test
	public void testCopyDistances() {

		final List<CommodityIndex> commodityIndices = new ArrayList<>();
		final List<CurrencyIndex> currencyIndices = new ArrayList<>();

		final CommodityIndex c1 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityIndex();
		c1.setName("HH");
		c1.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());
		commodityIndices.add(c1);

		final CommodityIndex c2 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityIndex();
		c2.setName("II");
		c2.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());
		commodityIndices.add(c2);

		final CurrencyIndex cu1 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyIndex();
		cu1.setName("USD");
		currencyIndices.add(cu1);

		final CurrencyIndex cu2 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyIndex();
		cu2.setName("EUR");
		currencyIndices.add(cu2);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.getCommodityIndices().addAll(commodityIndices);
		pricingModel.getCurrencyIndices().addAll(currencyIndices);

		Assert.assertFalse(pricingModel.getCurrencyIndices().isEmpty());

		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assert.assertNotNull(updatePricingCommand);

		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals("USD", potentialHH.get().getCurrencyUnit());
		Assert.assertEquals("m3", potentialHH.get().getVolumeUnit());
		Assert.assertEquals(7.1, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assert.assertEquals(7.2, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 2)), 0.01);

		final Optional<CommodityIndex> potentialII = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("II")).findAny();
		Assert.assertTrue(potentialII.isPresent());
		Assert.assertEquals("USD", potentialII.get().getCurrencyUnit());
		Assert.assertEquals("m3", potentialII.get().getVolumeUnit());
		Assert.assertEquals(7.3, potentialII.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assert.assertEquals(7.4, potentialII.get().getData().getValueForMonth(YearMonth.of(2017, 2)), 0.01);

		// Currency curve is no longer present
		Assert.assertTrue(pricingModel.getCurrencyIndices().isEmpty());

	}

	@Test
	public void testPeriodInNames() {

		final List<CommodityIndex> commodityIndices = new ArrayList<>();
		final List<CurrencyIndex> currencyIndices = new ArrayList<>();

		final CommodityIndex c1 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityIndex();
		c1.setName("HH.extra");
		c1.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());
		commodityIndices.add(c1);

		final CommodityIndex c2 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityIndex();
		c2.setName("II.extra");
		c2.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());
		commodityIndices.add(c2);

		final CurrencyIndex cu1 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyIndex();
		cu1.setName("USD.extra");
		cu1.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());

		currencyIndices.add(cu1);

		final CurrencyIndex cu2 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyIndex();
		cu2.setName("EUR.extra");
		cu2.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());

		currencyIndices.add(cu2);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.getCommodityIndices().addAll(commodityIndices);
		pricingModel.getCurrencyIndices().addAll(currencyIndices);

		Assert.assertFalse(pricingModel.getCurrencyIndices().isEmpty());

		final PricingVersion derivedVersion = PricingFromScenarioCopier.generateVersion(pricingModel);

		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final PricingVersion pricingVersion = createExtraPricingVersion();

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assert.assertNotNull(updatePricingCommand);

		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH.extra")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals("USD", potentialHH.get().getCurrencyUnit());
		Assert.assertEquals("m3", potentialHH.get().getVolumeUnit());
		Assert.assertEquals(7.1, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assert.assertEquals(7.2, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 2)), 0.01);

		final Optional<CommodityIndex> potentialII = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("II.extra")).findAny();
		Assert.assertTrue(potentialII.isPresent());
		Assert.assertEquals("USD", potentialII.get().getCurrencyUnit());
		Assert.assertEquals("m3", potentialII.get().getVolumeUnit());
		Assert.assertEquals(7.3, potentialII.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assert.assertEquals(7.4, potentialII.get().getData().getValueForMonth(YearMonth.of(2017, 2)), 0.01);

		// Currency curve is no longer present
		Assert.assertTrue(pricingModel.getCurrencyIndices().isEmpty());

	}

	@Test
	public void testIndexNotPreviouslyExisting() {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals(7.1, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
	}

	@Test
	public void testExistingPointsRemoved() {
		final List<CommodityIndex> commodityIndices = new ArrayList<>();
		final List<CurrencyIndex> currencyIndices = new ArrayList<>();

		final CommodityIndex c1 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityIndex();
		c1.setName("HH");
		c1.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());
		commodityIndices.add(c1);

		final DataIndex<Double> c1Data = PricingPackage.eINSTANCE.getPricingFactory().createDataIndex();
		final IndexPoint<Double> oldData1 = PricingPackage.eINSTANCE.getPricingFactory().createIndexPoint();
		oldData1.setDate(YearMonth.of(2017, 1));
		oldData1.setValue(7.0d);
		final IndexPoint<Double> oldData2 = PricingPackage.eINSTANCE.getPricingFactory().createIndexPoint();
		oldData2.setDate(YearMonth.of(2016, 12));
		oldData2.setValue(6.9d);
		c1Data.getPoints().add(oldData2);
		c1Data.getPoints().add(oldData1);
		c1.setData(c1Data);

		final CommodityIndex c2 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityIndex();
		c2.setName("II");
		c2.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());
		commodityIndices.add(c2);

		final CurrencyIndex cu1 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyIndex();
		cu1.setName("USD");
		currencyIndices.add(cu1);

		final CurrencyIndex cu2 = PricingPackage.eINSTANCE.getPricingFactory().createCurrencyIndex();
		cu2.setName("EUR");
		currencyIndices.add(cu2);

		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		pricingModel.getCommodityIndices().addAll(commodityIndices);
		pricingModel.getCurrencyIndices().addAll(currencyIndices);

		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		final Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals(6.9, potentialHH.get().getData().getValueForMonth(YearMonth.of(2016, 12)), 0.01);

		Assert.assertNotNull(updatePricingCommand);

		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals(7.1, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assert.assertNull(potentialHH.get().getData().getValueForMonth(YearMonth.of(2016, 12)));
	}

	@Test
	public void testExpressionCurve() {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final ExpressionCurve c1 = new ExpressionCurve("REL_HH", CurveType.COMMODITY, null, null, null, "105%HH");

		pricingVersion.getCurves().put(c1.getName(), c1);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CommodityIndex> potentialRel = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("REL_HH")).findAny();
		Assert.assertTrue(potentialRel.isPresent());
		Assert.assertTrue(potentialRel.get().getData() instanceof DerivedIndex);
		Assert.assertEquals("105%HH", ((DerivedIndex) potentialRel.get().getData()).getExpression());
	}

	@Test
	public void testValues() {
		// test both double and integer values
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		final PricingVersion pricingVersion = createDefaultPricingVersion();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);

		final DataCurve c1 = new DataCurve("COOL", CurveType.CHARTER, null, "day", "USD",
				CollectionsUtil.makeArrayList(new CurvePoint(LocalDate.of(2017, 1, 1), 8000d), new CurvePoint(LocalDate.of(2017, 2, 1), 9000d)));

		pricingVersion.getCurves().put(c1.getName(), c1);

		final Command updatePricingCommand = PricingToScenarioCopier.getUpdateCommand(ed, pricingModel, pricingVersion);

		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);

		final Optional<CharterIndex> potentialCool = pricingModel.getCharterIndices().stream().filter(i -> i.getName().equals("COOL")).findAny();
		Assert.assertTrue(potentialCool.isPresent());
		Assert.assertEquals(8000, potentialCool.get().getData().getValueForMonth(YearMonth.of(2017, 1)).intValue());

		final Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals(7.1d, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)).doubleValue(), 0.01);
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
