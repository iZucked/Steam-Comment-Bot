/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Association;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
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

		final VesselClass eVesselClass = FleetFactory.eINSTANCE.createVesselClass();
		fleetModel.getVesselClasses().add(eVesselClass);

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

		eVessel1.setVesselClass(eVesselClass);
		eVessel2.setVesselClass(eVesselClass);
		eVessel3.setVesselClass(eVesselClass);
		eVessel4.setVesselClass(eVesselClass);
		eVessel5.setVesselClass(eVesselClass);
		eVessel6.setVesselClass(eVesselClass);
		eVessel7.setVesselClass(eVesselClass);
		eVessel8.setVesselClass(eVesselClass);

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

		final IVesselClass oVesselClass1 = Mockito.mock(IVesselClass.class);

		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<>();
		vesselClassAssociation.add(eVesselClass, oVesselClass1);

		Mockito.when(oVessel1.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel2.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel3.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel4.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel5.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel6.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel7.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel8.getVesselClass()).thenReturn(oVesselClass1);

		final List<IVessel> vesselAvailabilities = Lists.newArrayList(oVessel1, oVessel2, oVessel3, oVessel4, oVessel5, oVessel6, oVessel7, oVessel8);

		LNGScenarioTransformer.buildPanamaCosts(builder, vesselAssociation, vesselClassAssociation, vesselAvailabilities, panamaCanalTariff);

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 30_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 30_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 30_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 1, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 1, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 1, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel4, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel4, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel4, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel5, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 1, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel5, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 1, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel5, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 1, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel6, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel6, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel6, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel7, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 1));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel7, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 1));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel7, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 1));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel8, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 50_000));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel8, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 50_000));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel8, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 50_000));

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

		final VesselClass eVesselClass = FleetFactory.eINSTANCE.createVesselClass();
		fleetModel.getVesselClasses().add(eVesselClass);

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

		eVessel1.setVesselClass(eVesselClass);
		eVessel2.setVesselClass(eVesselClass);
		eVessel3.setVesselClass(eVesselClass);
		eVessel4.setVesselClass(eVesselClass);
		eVessel5.setVesselClass(eVesselClass);
		eVessel6.setVesselClass(eVesselClass);
		eVessel7.setVesselClass(eVesselClass);
		eVessel8.setVesselClass(eVesselClass);

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

		final IVesselClass oVesselClass1 = Mockito.mock(IVesselClass.class);

		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<>();
		vesselClassAssociation.add(eVesselClass, oVesselClass1);

		Mockito.when(oVessel1.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel2.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel3.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel4.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel5.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel6.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel7.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel8.getVesselClass()).thenReturn(oVesselClass1);

		final List<IVessel> vesselAvailabilities = Lists.newArrayList(oVessel1, oVessel2, oVessel3, oVessel4, oVessel5, oVessel6, oVessel7, oVessel8);

		LNGScenarioTransformer.buildPanamaCosts(builder, vesselAssociation, vesselClassAssociation, vesselAvailabilities, panamaCanalTariff);

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 30_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 30_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 30_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 0, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 1, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 1, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 1, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel4, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel4, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel4, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 0, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel5, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 1, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel5, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 1, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel5, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 1, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel6, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel6, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel6, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 0));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel7, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 1));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel7, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 1));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel7, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 1));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel8, IRouteCostProvider.CostType.Laden,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Laden, 60_000, 30_000, 30_000, 50_000));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel8, IRouteCostProvider.CostType.Ballast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.Ballast, 60_000, 30_000, 30_000, 50_000));

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel8, IRouteCostProvider.CostType.RoundTripBallast,
				expectedPanamaCost(panamaCanalTariff, IRouteCostProvider.CostType.RoundTripBallast, 60_000, 30_000, 30_000, 50_000));

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

		final VesselClass eVesselClass1 = FleetFactory.eINSTANCE.createVesselClass();
		final VesselClass eVesselClass2 = FleetFactory.eINSTANCE.createVesselClass();
		final VesselClass eVesselClass3 = FleetFactory.eINSTANCE.createVesselClass();

		fleetModel.getVesselClasses().add(eVesselClass1);
		fleetModel.getVesselClasses().add(eVesselClass2);
		fleetModel.getVesselClasses().add(eVesselClass3);

		eVesselClass1.setCapacity(138_000);
		eVesselClass2.setCapacity(155_000);
		eVesselClass3.setCapacity(177_000);

		eVessel1.setCapacity(138_000);
		eVessel2.setCapacity(155_000);

		final IVessel oVessel1 = Mockito.mock(IVessel.class);
		final IVessel oVessel2 = Mockito.mock(IVessel.class);
		// Spot vessel
		final IVessel oVessel3 = Mockito.mock(IVessel.class);

		final Association<Vessel, IVessel> vesselAssociation = new Association<>();
		vesselAssociation.add(eVessel1, oVessel1);
		vesselAssociation.add(eVessel2, oVessel2);

		final IVesselClass oVesselClass1 = Mockito.mock(IVesselClass.class);
		final IVesselClass oVesselClass2 = Mockito.mock(IVesselClass.class);
		final IVesselClass oVesselClass3 = Mockito.mock(IVesselClass.class);

		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<>();
		vesselClassAssociation.add(eVesselClass1, oVesselClass1);
		vesselClassAssociation.add(eVesselClass2, oVesselClass2);
		vesselClassAssociation.add(eVesselClass3, oVesselClass3);

		Mockito.when(oVessel1.getVesselClass()).thenReturn(oVesselClass1);
		Mockito.when(oVessel2.getVesselClass()).thenReturn(oVesselClass2);
		Mockito.when(oVessel3.getVesselClass()).thenReturn(oVesselClass3);

		final List<IVessel> vesselAvailabilities = Lists.newArrayList(oVessel1, oVessel2, oVessel3);

		LNGScenarioTransformer.buildPanamaCosts(builder, vesselAssociation, vesselClassAssociation, vesselAvailabilities, panamaCanalTariff);

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Laden, 311_880_000L);
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.Ballast, 274_980_000L);
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel1, IRouteCostProvider.CostType.RoundTripBallast, 247_500_000L);

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Laden, 345_200_000L);
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.Ballast, 304_050_000L);
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel2, IRouteCostProvider.CostType.RoundTripBallast, 273_000_000L);

		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.Laden, 388_320_000L);
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.Ballast, 341_670_000L);
		Mockito.verify(builder).setVesselRouteCost(ERouteOption.PANAMA, oVessel3, IRouteCostProvider.CostType.RoundTripBallast, 306_000_000L);

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
	long expectedPanamaCost(final PanamaCanalTariff panamaCanalTariff, final IRouteCostProvider.CostType costType, final int band1, final int band2, final int band3, final int band4) {
		double total = 0.0;

		total += getBandPrice(panamaCanalTariff, costType, 0) * (double) band1;
		total += getBandPrice(panamaCanalTariff, costType, 1) * (double) band2;
		total += getBandPrice(panamaCanalTariff, costType, 2) * (double) band3;
		total += getBandPrice(panamaCanalTariff, costType, 3) * (double) band4;

		total *= 1.0 + panamaCanalTariff.getMarkupRate();

		return 1000L * (long) Math.round(total);
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
}
