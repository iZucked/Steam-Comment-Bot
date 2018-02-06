package com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
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
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.pricing.CurveType;
import com.mmxlabs.lngdataserver.integration.pricing.IPricingProvider;
import com.mmxlabs.lngdataserver.lng.importers.pricing.PricingToScenarioCopier;
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
	public void testCopyDistances() throws IOException {
		
		Assert.fail("Not yet implemented");

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
		
		
		final EditingDomain ed = createLocalEditingDomain(pricingModel);
		
		final IPricingProvider pp = getDefaultPP();

		PricingToScenarioCopier copier = new PricingToScenarioCopier();
		Command updatePricingCommand = copier.getUpdatePricingCommand(ed, pp, pricingModel);

		Assert.assertNotNull(updatePricingCommand);
		
		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);
		
		Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals("USD", potentialHH.get().getCurrencyUnit());
		Assert.assertEquals("m3", potentialHH.get().getVolumeUnit());
		Assert.assertEquals(7.1, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assert.assertEquals(7.2, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 2)), 0.01);
		
		Optional<CommodityIndex> potentialII = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("II")).findAny();
		Assert.assertTrue(potentialII.isPresent());
		Assert.assertEquals("USD", potentialII.get().getCurrencyUnit());
		Assert.assertEquals("m3", potentialII.get().getVolumeUnit());
		Assert.assertEquals(7.3, potentialII.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
		Assert.assertEquals(7.4, potentialII.get().getData().getValueForMonth(YearMonth.of(2017, 2)), 0.01);
	}
	
	@Test
	public void testIndexNotPreviouslyExisting() throws IOException {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		IPricingProvider defaultPP = getDefaultPP();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);
		
		PricingToScenarioCopier copier = new PricingToScenarioCopier();
		Command updatePricingCommand = copier.getUpdatePricingCommand(ed, defaultPP, pricingModel);
		ed.getCommandStack().execute(updatePricingCommand);

		Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals(7.1, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)), 0.01);
	}
	
	@Test
	public void testExistingPointsRemoved() throws IOException {
		final List<CommodityIndex> commodityIndices = new ArrayList<>();
		final List<CurrencyIndex> currencyIndices = new ArrayList<>();
		
		final CommodityIndex c1 = PricingPackage.eINSTANCE.getPricingFactory().createCommodityIndex();
		c1.setName("HH");
		c1.setData(PricingPackage.eINSTANCE.getPricingFactory().createDataIndex());
		commodityIndices.add(c1);

		
		DataIndex<Double> c1Data = PricingPackage.eINSTANCE.getPricingFactory().createDataIndex();
		IndexPoint<Double> oldData1 = PricingPackage.eINSTANCE.getPricingFactory().createIndexPoint();
		oldData1.setDate(YearMonth.of(2017, 1));
		oldData1.setValue(7.0d);
		IndexPoint<Double> oldData2 = PricingPackage.eINSTANCE.getPricingFactory().createIndexPoint();
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
		
		IPricingProvider defaultPP = getDefaultPP();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);
		
		PricingToScenarioCopier copier = new PricingToScenarioCopier();
		Command updatePricingCommand = copier.getUpdatePricingCommand(ed, defaultPP, pricingModel);

		Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
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
	public void testExpressionCurve() throws IOException {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		IPricingProvider defaultPP = getDefaultPP();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);
		defaultPP.getCommodityCurves().add("REL_HH");
		when(defaultPP.getCurveType("REL_HH")).thenReturn(CurveType.ExpressionCurve);
		when(defaultPP.getExpression("REL_HH")).thenReturn("105%HH");
		
		PricingToScenarioCopier copier = new PricingToScenarioCopier();
		Command updatePricingCommand = copier.getUpdatePricingCommand(ed, defaultPP, pricingModel);
		
		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);
		
		Optional<CommodityIndex> potentialRel = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("REL_HH")).findAny();
		Assert.assertTrue(potentialRel.isPresent());
		Assert.assertTrue(potentialRel.get().getData() instanceof DerivedIndex);
		Assert.assertEquals("105%HH", ((DerivedIndex)potentialRel.get().getData()).getExpression());
	}
	
	@Test
	public void testValues() throws IOException {
		// test both double and integer values
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		IPricingProvider defaultPP = getDefaultPP();
		final EditingDomain ed = createLocalEditingDomain(pricingModel);
		
		
		final List<String> charterCurves = CollectionsUtil.makeArrayList("COOL");
		when(defaultPP.getCharterCurves()).thenReturn(charterCurves);
		when(defaultPP.getCurveType("COOL")).thenReturn(CurveType.DataCurve);
		when(defaultPP.getData("COOL")).thenReturn(CollectionsUtil.makeArrayList(new Pair<LocalDate, Double>(LocalDate.of(2017, 1, 1), 8000d), new Pair<LocalDate, Double>(LocalDate.of(2017, 2, 1), 9000d)));
		when(defaultPP.getCurrencyUnit("COOL")).thenReturn("USD");
		when(defaultPP.getVolumeUnit("COOL")).thenReturn("day");
		
		PricingToScenarioCopier copier = new PricingToScenarioCopier();
		Command updatePricingCommand = copier.getUpdatePricingCommand(ed, defaultPP, pricingModel);
		
		Assert.assertTrue(updatePricingCommand.canExecute());
		ed.getCommandStack().execute(updatePricingCommand);
		
		Optional<CharterIndex> potentialCool = pricingModel.getCharterIndices().stream().filter(i -> i.getName().equals("COOL")).findAny();
		Assert.assertTrue(potentialCool.isPresent());
		Assert.assertEquals(8000, potentialCool.get().getData().getValueForMonth(YearMonth.of(2017, 1)).intValue());
		
		Optional<CommodityIndex> potentialHH = pricingModel.getCommodityIndices().stream().filter(i -> i.getName().equals("HH")).findAny();
		Assert.assertTrue(potentialHH.isPresent());
		Assert.assertEquals(7.1d, potentialHH.get().getData().getValueForMonth(YearMonth.of(2017, 1)).doubleValue(), 0.01);
	}
	

	private static IPricingProvider getDefaultPP() throws IOException {
		final IPricingProvider pp = Mockito.mock(IPricingProvider.class);
		final List<String> commodityCurves = CollectionsUtil.makeArrayList("HH", "II");
		
		when(pp.getCommodityCurves()).thenReturn(commodityCurves);
		when(pp.getCurveType("HH")).thenReturn(CurveType.DataCurve);
		when(pp.getCurveType("II")).thenReturn(CurveType.DataCurve);

		when(pp.getData("HH")).thenReturn(CollectionsUtil.makeArrayList(new Pair<LocalDate, Double>(LocalDate.of(2017, 1, 1), 7.1d), new Pair<LocalDate, Double>(LocalDate.of(2017, 2, 1), 7.2d)));
		when(pp.getCurrencyUnit("HH")).thenReturn("USD");
		when(pp.getVolumeUnit("HH")).thenReturn("m3");
		
		when(pp.getData("II")).thenReturn(CollectionsUtil.makeArrayList(new Pair<LocalDate, Double>(LocalDate.of(2017, 1, 1), 7.3d), new Pair<LocalDate, Double>(LocalDate.of(2017, 2, 1), 7.4d)));
		when(pp.getCurrencyUnit("II")).thenReturn("USD");
		when(pp.getVolumeUnit("II")).thenReturn("m3");
		
		return pp;
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
}
