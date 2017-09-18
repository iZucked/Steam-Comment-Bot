/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;

public class LNGScenarioTransformerTest {

	@Test
	public void mapRouteOption1() {

		final Route directRoute = PortFactory.eINSTANCE.createRoute();
		directRoute.setRouteOption(RouteOption.DIRECT);

		final Route suezRoute = PortFactory.eINSTANCE.createRoute();
		suezRoute.setRouteOption(RouteOption.SUEZ);

		final Route panamaRoute = PortFactory.eINSTANCE.createRoute();
		panamaRoute.setRouteOption(RouteOption.PANAMA);

		Assert.assertEquals(ERouteOption.DIRECT, LNGScenarioTransformer.mapRouteOption(directRoute));
		Assert.assertEquals(ERouteOption.SUEZ, LNGScenarioTransformer.mapRouteOption(suezRoute));
		Assert.assertEquals(ERouteOption.PANAMA, LNGScenarioTransformer.mapRouteOption(panamaRoute));
	}

	@Test
	public void mapRouteOption2() {

		// Fall back to route default as defined in ECore
		final Route route = PortFactory.eINSTANCE.createRoute();
		Assert.assertEquals(ERouteOption.DIRECT, LNGScenarioTransformer.mapRouteOption(route));
	}

	@Test
	public void mapRouteOption3() {
		final Route route = PortFactory.eINSTANCE.createRoute();
		route.setRouteOption(RouteOption.PANAMA);
		// Seems to trigger a reset to default
		route.setRouteOption(null);
		Assert.assertEquals(ERouteOption.DIRECT, LNGScenarioTransformer.mapRouteOption(route));
	}

	@Test
	public void testPanamaCostCalculator() {

		final ISchedulerBuilder builder = Mockito.mock(ISchedulerBuilder.class);

		// Actual data as of 2016/02/01
		final PanamaCanalTariff panamaCanalTariff = PricingFactory.eINSTANCE.createPanamaCanalTariff();
		final PanamaCanalTariffBand band1 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band1.setBandEnd(60_000);
		band1.setLadenTariff(2.5);
		band1.setBallastTariff(2.23);
		band1.setBallastRoundtripTariff(2.0);

		final PanamaCanalTariffBand band2 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band2.setBandStart(60_000);
		band2.setBandEnd(90_000);
		band2.setLadenTariff(2.15);
		band2.setBallastTariff(1.88);
		band2.setBallastRoundtripTariff(1.75);

		final PanamaCanalTariffBand band3 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band3.setBandStart(90_000);
		band3.setBandEnd(120_000);
		band3.setLadenTariff(2.07);
		band3.setBallastTariff(1.8);
		band3.setBallastRoundtripTariff(1.6);

		final PanamaCanalTariffBand band4 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band4.setBandStart(120_000);
		band4.setLadenTariff(1.96);
		band4.setBallastTariff(1.71);
		band4.setBallastRoundtripTariff(1.5);

		panamaCanalTariff.getBands().add(band1);
		panamaCanalTariff.getBands().add(band2);
		panamaCanalTariff.getBands().add(band3);
		panamaCanalTariff.getBands().add(band4);

		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();

		final Vessel eVessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel2 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel3 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel4 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel5 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel6 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel7 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel8 = FleetFactory.eINSTANCE.createVessel();

		fleetModel.getVessels().add(eVessel1);
		fleetModel.getVessels().add(eVessel2);
		fleetModel.getVessels().add(eVessel3);
		fleetModel.getVessels().add(eVessel4);
		fleetModel.getVessels().add(eVessel5);
		fleetModel.getVessels().add(eVessel6);
		fleetModel.getVessels().add(eVessel7);
		fleetModel.getVessels().add(eVessel8);

		eVessel1.setName("30_000");
		eVessel2.setName("60_000");
		eVessel3.setName("60_001");
		eVessel4.setName("90_000");
		eVessel5.setName("90_001");
		eVessel6.setName("120_000");
		eVessel7.setName("120_001");
		eVessel8.setName("170_000");

		eVessel1.setCapacity(30_000);
		eVessel2.setCapacity(60_000);
		eVessel3.setCapacity(60_001);
		eVessel4.setCapacity(90_000);
		eVessel5.setCapacity(90_001);
		eVessel6.setCapacity(120_000);
		eVessel7.setCapacity(120_001);
		eVessel8.setCapacity(170_000);

		final IVessel oVessel1 = Mockito.mock(IVessel.class);
		final IVessel oVessel2 = Mockito.mock(IVessel.class);
		final IVessel oVessel3 = Mockito.mock(IVessel.class);
		final IVessel oVessel4 = Mockito.mock(IVessel.class);
		final IVessel oVessel5 = Mockito.mock(IVessel.class);
		final IVessel oVessel6 = Mockito.mock(IVessel.class);
		final IVessel oVessel7 = Mockito.mock(IVessel.class);
		final IVessel oVessel8 = Mockito.mock(IVessel.class);

		final Association<Vessel, IVessel> vesselAssociation = new Association<>();
		vesselAssociation.add(eVessel1, oVessel1);
		vesselAssociation.add(eVessel2, oVessel2);
		vesselAssociation.add(eVessel3, oVessel3);
		vesselAssociation.add(eVessel4, oVessel4);
		vesselAssociation.add(eVessel5, oVessel5);
		vesselAssociation.add(eVessel6, oVessel6);
		vesselAssociation.add(eVessel7, oVessel7);
		vesselAssociation.add(eVessel8, oVessel8);

		final List<IVessel> vesselAvailabilities = Lists.newArrayList(oVessel1, oVessel2, oVessel3, oVessel4, oVessel5, oVessel6, oVessel7, oVessel8);

		LNGScenarioTransformer.buildPanamaCosts(builder, vesselAssociation, vesselAvailabilities, panamaCanalTariff);

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 30_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 30_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 30_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 1, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 1, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 1, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel5), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 1, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel5), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 1, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel5), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 1, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel6), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel6), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel6), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel7), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel7), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel7), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel8), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 50_000)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel8), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 50_000)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel8), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 50_000)));

		Mockito.verifyNoMoreInteractions(builder);
	}

	@Test
	public void testSuezCostCalculator() {

		final ISchedulerBuilder builder = Mockito.mock(ISchedulerBuilder.class);

		final SuezCanalTariff suezCanalTariff = PricingFactory.eINSTANCE.createSuezCanalTariff();
		final SuezCanalTariffBand band1 = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band1.setBandEnd(5_000);
		band1.setLadenTariff(7.88);
		band1.setBallastTariff(6.7);

		final SuezCanalTariffBand band2 = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band2.setBandStart(5_000);
		band2.setBandEnd(10_000);
		band2.setLadenTariff(6.13);
		band2.setBallastTariff(5.21);

		final SuezCanalTariffBand band3 = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band3.setBandStart(10_000);
		band3.setBandEnd(20_000);
		band3.setLadenTariff(5.3);
		band3.setBallastTariff(4.510);

		final SuezCanalTariffBand band4 = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band4.setBandStart(20_000);
		band4.setBandEnd(40_000);
		band4.setLadenTariff(4.1);
		band4.setBallastTariff(3.49);

		final SuezCanalTariffBand band5 = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band5.setBandStart(40_000);
		band5.setBandEnd(70_000);
		band5.setLadenTariff(3.8);
		band5.setBallastTariff(3.23);

		final SuezCanalTariffBand band6 = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band6.setBandStart(70_000);
		band6.setBandEnd(120_000);
		band6.setLadenTariff(3.63);
		band6.setBallastTariff(3.090);

		final SuezCanalTariffBand band7 = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band7.setBandStart(120_000);
		band7.setLadenTariff(3.53);
		band7.setBallastTariff(3);

		suezCanalTariff.getBands().add(band1);
		suezCanalTariff.getBands().add(band2);
		suezCanalTariff.getBands().add(band3);
		suezCanalTariff.getBands().add(band4);
		suezCanalTariff.getBands().add(band5);
		suezCanalTariff.getBands().add(band6);
		suezCanalTariff.getBands().add(band7);

		final SuezCanalTugBand tugBand1 = PricingFactory.eINSTANCE.createSuezCanalTugBand();
		tugBand1.setBandEnd(90_000);
		tugBand1.setTugs(1);

		final SuezCanalTugBand tugBand2 = PricingFactory.eINSTANCE.createSuezCanalTugBand();
		tugBand2.setBandStart(90_000);
		tugBand2.setTugs(2);

		suezCanalTariff.getTugBands().add(tugBand1);
		suezCanalTariff.getTugBands().add(tugBand2);

		suezCanalTariff.setTugCost(7_000);
		suezCanalTariff.setFixedCosts(30_000 + 20_000 + 10_000);

		// TODO: Set discount factor
		// TODO: Set SDR rate
		// TODO: Add misc costs
		// TODO: Tugs!
		suezCanalTariff.setDiscountFactor(0.2);

		suezCanalTariff.setSdrToUSD("1.5");

		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();

		final Vessel eVessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel2 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel3 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel4 = FleetFactory.eINSTANCE.createVessel();

		fleetModel.getVessels().add(eVessel1);
		fleetModel.getVessels().add(eVessel2);
		fleetModel.getVessels().add(eVessel3);
		fleetModel.getVessels().add(eVessel4);

		eVessel1.setName("5_000");
		eVessel2.setName("5_001");
		eVessel3.setName("120_000");
		eVessel4.setName("120_001");

		eVessel1.setScnt(5_000);
		eVessel2.setScnt(5_001);
		eVessel3.setScnt(120_000);
		eVessel4.setScnt(120_001);

		final IVessel oVessel1 = Mockito.mock(IVessel.class);
		final IVessel oVessel2 = Mockito.mock(IVessel.class);
		final IVessel oVessel3 = Mockito.mock(IVessel.class);
		final IVessel oVessel4 = Mockito.mock(IVessel.class);

		final Association<Vessel, IVessel> vesselAssociation = new Association<>();
		vesselAssociation.add(eVessel1, oVessel1);
		vesselAssociation.add(eVessel2, oVessel2);
		vesselAssociation.add(eVessel3, oVessel3);
		vesselAssociation.add(eVessel4, oVessel4);

		final List<IVessel> vesselAvailabilities = Lists.newArrayList(oVessel1, oVessel2, oVessel3, oVessel4);
		final SeriesParser currencyIndices = new SeriesParser();
		final DateAndCurveHelper dateHelper = new DateAndCurveHelper(
				new Pair<>(ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0), ZoneId.of("UTC")), ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC"))));
		LNGScenarioTransformer.buildSuezCosts(builder, vesselAssociation, vesselAvailabilities, suezCanalTariff, currencyIndices, dateHelper);

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.Laden),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 1, IRouteCostProvider.CostType.Laden, 5_000, 0, 0, 0, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 1, IRouteCostProvider.CostType.Ballast, 5_000, 0, 0, 0, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 1, IRouteCostProvider.CostType.RoundTripBallast, 5_000, 0, 0, 0, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.Laden),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 1, IRouteCostProvider.CostType.Laden, 5_000, 1, 0, 0, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 1, IRouteCostProvider.CostType.Ballast, 5_000, 1, 0, 0, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 1, IRouteCostProvider.CostType.RoundTripBallast, 5_000, 1, 0, 0, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.Laden),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 2, IRouteCostProvider.CostType.Laden, 5_000, 5_000, 10_000, 20_000, 30_000, 50_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 2, IRouteCostProvider.CostType.Ballast, 5_000, 5_000, 10_000, 20_000, 30_000, 50_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 2, IRouteCostProvider.CostType.RoundTripBallast, 5_000, 5_000, 10_000, 20_000, 30_000, 50_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.Laden),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 2, IRouteCostProvider.CostType.Laden, 5_000, 5_000, 10_000, 20_000, 30_000, 50_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 2, IRouteCostProvider.CostType.Ballast, 5_000, 5_000, 10_000, 20_000, 30_000, 50_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.SUEZ), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				MyMatcher.eq(expectedSuezCost(suezCanalTariff, 2, IRouteCostProvider.CostType.RoundTripBallast, 5_000, 5_000, 10_000, 20_000, 30_000, 50_000, 1)));

		Mockito.verifyNoMoreInteractions(builder);
	}

	@Test
	public void testPanamaCostCalculator_WithMarkup() {

		final ISchedulerBuilder builder = Mockito.mock(ISchedulerBuilder.class);

		// Actual data as of 2016/02/01
		final PanamaCanalTariff panamaCanalTariff = PricingFactory.eINSTANCE.createPanamaCanalTariff();
		final PanamaCanalTariffBand band1 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band1.setBandEnd(60_000);
		band1.setLadenTariff(2.5);
		band1.setBallastTariff(2.23);
		band1.setBallastRoundtripTariff(2.0);

		final PanamaCanalTariffBand band2 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band2.setBandStart(60_000);
		band2.setBandEnd(90_000);
		band2.setLadenTariff(2.15);
		band2.setBallastTariff(1.88);
		band2.setBallastRoundtripTariff(1.75);

		final PanamaCanalTariffBand band3 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band3.setBandStart(90_000);
		band3.setBandEnd(120_000);
		band3.setLadenTariff(2.07);
		band3.setBallastTariff(1.8);
		band3.setBallastRoundtripTariff(1.6);

		final PanamaCanalTariffBand band4 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band4.setBandStart(120_000);
		band4.setLadenTariff(1.96);
		band4.setBallastTariff(1.71);
		band4.setBallastRoundtripTariff(1.5);

		panamaCanalTariff.getBands().add(band1);
		panamaCanalTariff.getBands().add(band2);
		panamaCanalTariff.getBands().add(band3);
		panamaCanalTariff.getBands().add(band4);

		// + 50% markup
		panamaCanalTariff.setMarkupRate(0.5);

		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();

		final Vessel eVessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel2 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel3 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel4 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel5 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel6 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel7 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel8 = FleetFactory.eINSTANCE.createVessel();

		fleetModel.getVessels().add(eVessel1);
		fleetModel.getVessels().add(eVessel2);
		fleetModel.getVessels().add(eVessel3);
		fleetModel.getVessels().add(eVessel4);
		fleetModel.getVessels().add(eVessel5);
		fleetModel.getVessels().add(eVessel6);
		fleetModel.getVessels().add(eVessel7);
		fleetModel.getVessels().add(eVessel8);

		eVessel1.setName("30_000");
		eVessel2.setName("60_000");
		eVessel3.setName("60_001");
		eVessel4.setName("90_000");
		eVessel5.setName("90_001");
		eVessel6.setName("120_000");
		eVessel7.setName("120_001");
		eVessel8.setName("170_000");

		eVessel1.setCapacity(30_000);
		eVessel2.setCapacity(60_000);
		eVessel3.setCapacity(60_001);
		eVessel4.setCapacity(90_000);
		eVessel5.setCapacity(90_001);
		eVessel6.setCapacity(120_000);
		eVessel7.setCapacity(120_001);
		eVessel8.setCapacity(170_000);

		final IVessel oVessel1 = Mockito.mock(IVessel.class);
		final IVessel oVessel2 = Mockito.mock(IVessel.class);
		final IVessel oVessel3 = Mockito.mock(IVessel.class);
		final IVessel oVessel4 = Mockito.mock(IVessel.class);
		final IVessel oVessel5 = Mockito.mock(IVessel.class);
		final IVessel oVessel6 = Mockito.mock(IVessel.class);
		final IVessel oVessel7 = Mockito.mock(IVessel.class);
		final IVessel oVessel8 = Mockito.mock(IVessel.class);

		final Association<Vessel, IVessel> vesselAssociation = new Association<>();
		vesselAssociation.add(eVessel1, oVessel1);
		vesselAssociation.add(eVessel2, oVessel2);
		vesselAssociation.add(eVessel3, oVessel3);
		vesselAssociation.add(eVessel4, oVessel4);
		vesselAssociation.add(eVessel5, oVessel5);
		vesselAssociation.add(eVessel6, oVessel6);
		vesselAssociation.add(eVessel7, oVessel7);
		vesselAssociation.add(eVessel8, oVessel8);

		final List<IVessel> vesselAvailabilities = Lists.newArrayList(oVessel1, oVessel2, oVessel3, oVessel4, oVessel5, oVessel6, oVessel7, oVessel8);

		LNGScenarioTransformer.buildPanamaCosts(builder, vesselAssociation, vesselAvailabilities, panamaCanalTariff);

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 30_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 30_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel1), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 30_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel2), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 0, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 1, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 1, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel3), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 1, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel4), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 0, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel5), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 1, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel5), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 1, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel5), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 1, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel6), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel6), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel6), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 0)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel7), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel7), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel7), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 1)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel8), Matchers.eq(IRouteCostProvider.CostType.Laden),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 50_000)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel8), Matchers.eq(IRouteCostProvider.CostType.Ballast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 50_000)));

		Mockito.verify(builder).setVesselRouteCost(Matchers.eq(ERouteOption.PANAMA), Matchers.eq(oVessel8), Matchers.eq(IRouteCostProvider.CostType.RoundTripBallast),
				Matchers.eq(expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 50_000)));

		Mockito.verifyNoMoreInteractions(builder);
	}

	@Test
	public void testPanamaCostCalculator_Examples() {

		// Examples from http://www.bakerbotts.com/ideas/publications/2015/4/lng-update

		final ISchedulerBuilder builder = Mockito.mock(ISchedulerBuilder.class);

		// Actual data as of 2016/02/01
		final PanamaCanalTariff panamaCanalTariff = PricingFactory.eINSTANCE.createPanamaCanalTariff();
		final PanamaCanalTariffBand band1 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band1.setBandEnd(60_000);
		band1.setLadenTariff(2.5);
		band1.setBallastTariff(2.23);
		band1.setBallastRoundtripTariff(2.0);

		final PanamaCanalTariffBand band2 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band2.setBandStart(60_000);
		band2.setBandEnd(90_000);
		band2.setLadenTariff(2.15);
		band2.setBallastTariff(1.88);
		band2.setBallastRoundtripTariff(1.75);

		final PanamaCanalTariffBand band3 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band3.setBandStart(90_000);
		band3.setBandEnd(120_000);
		band3.setLadenTariff(2.07);
		band3.setBallastTariff(1.8);
		band3.setBallastRoundtripTariff(1.6);

		final PanamaCanalTariffBand band4 = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
		band4.setBandStart(120_000);
		band4.setLadenTariff(1.96);
		band4.setBallastTariff(1.71);
		band4.setBallastRoundtripTariff(1.5);

		panamaCanalTariff.getBands().add(band1);
		panamaCanalTariff.getBands().add(band2);
		panamaCanalTariff.getBands().add(band3);
		panamaCanalTariff.getBands().add(band4);

		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();

		final Vessel eVessel1 = FleetFactory.eINSTANCE.createVessel();
		final Vessel eVessel2 = FleetFactory.eINSTANCE.createVessel();

		fleetModel.getVessels().add(eVessel1);
		fleetModel.getVessels().add(eVessel2);

		eVessel1.setCapacity(138_000);
		eVessel2.setCapacity(155_000);

		final IVessel oVessel1 = Mockito.mock(IVessel.class);
		final IVessel oVessel2 = Mockito.mock(IVessel.class);

		final Association<Vessel, IVessel> vesselAssociation = new Association<>();
		vesselAssociation.add(eVessel1, oVessel1);
		vesselAssociation.add(eVessel2, oVessel2);

		final List<IVessel> vesselAvailabilities = Lists.newArrayList(oVessel1, oVessel2);

		LNGScenarioTransformer.buildPanamaCosts(builder, vesselAssociation, vesselAvailabilities, panamaCanalTariff);

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Laden, new ConstantValueLongCurve(311_880_000L));
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Ballast, new ConstantValueLongCurve(274_980_000L));
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.RoundTripBallast, new ConstantValueLongCurve(247_500_000L));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Laden, new ConstantValueLongCurve(345_200_000L));
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Ballast, new ConstantValueLongCurve(304_050_000L));
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.RoundTripBallast, new ConstantValueLongCurve(273_000_000L));

		Mockito.verifyNoMoreInteractions(builder);
	}

	/**
	 * Expects bands to be sorted
	 * 
	 * @param panamaCanalTariff
	 * @param costType
	 * @param band1
	 * @param band2
	 * @param band3
	 * @param band4
	 * @return
	 */
	private ILongCurve expectedPanamaCost(final PanamaCanalTariff panamaCanalTariff, final IRouteCostProvider.CostType costType, final int band1, final int band2, final int band3, final int band4) {
		double total = 0.0;

		total += getBandPrice(panamaCanalTariff, costType, 0) * (double) band1;
		total += getBandPrice(panamaCanalTariff, costType, 1) * (double) band2;
		total += getBandPrice(panamaCanalTariff, costType, 2) * (double) band3;
		total += getBandPrice(panamaCanalTariff, costType, 3) * (double) band4;

		total *= 1.0 + panamaCanalTariff.getMarkupRate();

		return new ConstantValueLongCurve(1000L * (long) Math.round(total));
	}

	/**
	 * Expects bands to be sorted
	 * 
	 * @param panamaCanalTariff
	 * @param costType
	 * @param band1
	 * @param band2
	 * @param band3
	 * @param band4
	 * @return
	 */
	ILongCurve expectedSuezCost(final SuezCanalTariff suezCanalTariff, int tugs, final IRouteCostProvider.CostType costType, final int band1, final int band2, final int band3, final int band4,
			final int band5, final int band6, final int band7) {
		double total = 0.0;

		total += getBandPrice(suezCanalTariff, costType, 0) * (double) band1;
		total += getBandPrice(suezCanalTariff, costType, 1) * (double) band2;
		total += getBandPrice(suezCanalTariff, costType, 2) * (double) band3;
		total += getBandPrice(suezCanalTariff, costType, 3) * (double) band4;
		total += getBandPrice(suezCanalTariff, costType, 4) * (double) band5;
		total += getBandPrice(suezCanalTariff, costType, 5) * (double) band6;
		total += getBandPrice(suezCanalTariff, costType, 6) * (double) band7;

		total *= 1.0 - suezCanalTariff.getDiscountFactor();

		// Price expression is set at 1.5
		total *= 1.5;

		total += (double) tugs * suezCanalTariff.getTugCost();

		total += suezCanalTariff.getFixedCosts();

		return new ConstantValueLongCurve((long) Math.round(1000.0 * total));
	}

	double getBandPrice(final PanamaCanalTariff panamaCanalTariff, final IRouteCostProvider.CostType costType, final int band) {
		switch (costType) {
		case Ballast:
			return panamaCanalTariff.getBands().get(band).getBallastTariff();
		case Laden:
			return panamaCanalTariff.getBands().get(band).getLadenTariff();
		case RoundTripBallast:
			return panamaCanalTariff.getBands().get(band).getBallastRoundtripTariff();
		}
		Assert.fail();
		return 0.0;
	}

	double getBandPrice(final SuezCanalTariff suezCanalTariff, final IRouteCostProvider.CostType costType, final int band) {
		switch (costType) {
		case Laden:
			return suezCanalTariff.getBands().get(band).getLadenTariff();
		case RoundTripBallast:
		case Ballast:
			return suezCanalTariff.getBands().get(band).getBallastTariff();
		}
		Assert.fail();
		return 0.0;
	}

	private static class MyMatcher extends BaseMatcher<ILongCurve> {
		ILongCurve reference;

		public static ILongCurve eq(final ILongCurve reference) {
			return Matchers.argThat(new MyMatcher(reference));
		}

		public MyMatcher(final ILongCurve reference) {
			this.reference = reference;

		}

		@Override
		public boolean matches(final Object item) {
			if (item instanceof ILongCurve) {
				final ILongCurve other = (ILongCurve) item;
				long a = reference.getValueAtPoint(0);
				long b = other.getValueAtPoint(0);
				if (a != b) {
					int ii = 0;
				}
				return a == b;
			}
			return false;
		}

		@Override
		public void describeTo(final Description description) {
			description.appendText("MyMatcher: " + reference.getValueAtPoint(0));
		}

		@Override
		public void describeMismatch(Object item, Description description) {
			if (item instanceof ILongCurve) {
				final ILongCurve other = (ILongCurve) item;
				description.appendText("MyMatcher: " + other.getValueAtPoint(0));
			}
			// description.appendText("was ").appendValue(item);
		}
	};
}
