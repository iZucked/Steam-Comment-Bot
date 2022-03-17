/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.delegation.slot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.util.SetUtils;

public class VesselRestrictionTest extends AbstractMicroTestCase {
	@NonNull
	private static final String VESSEL_SMALL = InternalDataConstants.REF_VESSEL_STEAM_138;
	@NonNull
	private static final String VESSEL_MEDIUM = InternalDataConstants.REF_VESSEL_STEAM_145;
	@NonNull
	private static final String VESSEL_LARGE = InternalDataConstants.REF_VESSEL_MEGI_176;

	@NonNull
	private static final String LOAD_PORT = InternalDataConstants.PORT_CAMERON;
	@NonNull
	private static final String DISCHARGE_PORT = InternalDataConstants.PORT_HIMEJI;

	private @NonNull Set<@NonNull Vessel> buildVesselsSetFromNames(final String... vesselNames) {
		return Arrays.stream(vesselNames).map(fleetModelFinder::findVessel).collect(Collectors.toSet());
	}

	private static <T extends Contract> void noRestrictionsAnywhereTest(final Supplier<@NonNull T> contractMaker, final Function<@NonNull T, @NonNull Slot<T>> slotMaker) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertFalse(slot.getSlotOrDelegateVesselRestrictionsArePermissive());
		Assertions.assertTrue(slotOrDelegateVesselRestrictions.isEmpty());
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotNoRestrictionsAnywhereTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertFalse(spotSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
		Assertions.assertTrue(slotOrDelegateVesselRestrictions.isEmpty());
	}

	private static <T extends Contract> void noRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<@NonNull T> contractMaker, final Function<@NonNull T, @NonNull Slot<T>> slotMaker,
			final boolean delegateRestrictionsArePermissive, @NonNull final Set<@NonNull Vessel> delegateVesselRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		contract.setRestrictedVesselsArePermissive(delegateRestrictionsArePermissive);
		contract.getRestrictedVessels().addAll(delegateVesselRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegateVesselRestrictionsArePermissive();
		if (delegateRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertEquals(delegateVesselRestrictions.size(), slotOrDelegateVesselRestrictions.size());
		Assertions.assertTrue(delegateVesselRestrictions.containsAll(slotOrDelegateVesselRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotNoRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean delegateRestrictionsArePermissive, @NonNull final Set<@NonNull Vessel> delegateVesselRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		spotMarket.setRestrictedVesselsArePermissive(delegateRestrictionsArePermissive);
		spotMarket.getRestrictedVessels().addAll(delegateVesselRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegateVesselRestrictionsArePermissive();
		if (delegateRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertEquals(delegateVesselRestrictions.size(), slotOrDelegateVesselRestrictions.size());
		Assertions.assertTrue(delegateVesselRestrictions.containsAll(slotOrDelegateVesselRestrictions));
	}

	private static <T extends Contract> void restrictionsOverrideWithNoDelegateRestrictionsTest(final Supplier<@NonNull T> contractMaker, final Function<@NonNull T, @NonNull Slot<T>> slotMaker,
			final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Vessel> slotVesselRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		slot.setRestrictedVesselsOverride(true);
		slot.setRestrictedVesselsArePermissive(slotRestrictionsArePermissive);
		slot.getRestrictedVessels().addAll(slotVesselRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegateVesselRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertEquals(slotVesselRestrictions.size(), slotOrDelegateVesselRestrictions.size());
		Assertions.assertTrue(slotVesselRestrictions.containsAll(slotOrDelegateVesselRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotRestrictionsOverrideWithNoDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Vessel> slotVesselRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		spotSlot.setRestrictedVesselsOverride(true);
		spotSlot.setRestrictedVesselsArePermissive(slotRestrictionsArePermissive);
		spotSlot.getRestrictedVessels().addAll(slotVesselRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegateVesselRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertEquals(slotVesselRestrictions.size(), slotOrDelegateVesselRestrictions.size());
		Assertions.assertTrue(slotVesselRestrictions.containsAll(slotOrDelegateVesselRestrictions));
	}

	private static <T extends Contract> void restrictionsOverrideWithDelegateRestrictionsTest(@NonNull final Supplier<@NonNull T> contractMaker,
			@NonNull final Function<@NonNull T, @NonNull Slot<T>> slotMaker, final boolean delegateRestrictionsArePermissive, final boolean slotRestrictionsArePermissive,
			@NonNull final Set<@NonNull Vessel> delegateVesselRestrictions, @NonNull final Set<@NonNull Vessel> slotVesselRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<@NonNull T> slot = slotMaker.apply(contract);

		Assertions.assertFalse(slotVesselRestrictions.equals(delegateVesselRestrictions));

		contract.setRestrictedVesselsArePermissive(delegateRestrictionsArePermissive);
		contract.getRestrictedVessels().addAll(delegateVesselRestrictions);
		slot.setRestrictedVesselsOverride(true);
		slot.setRestrictedVesselsArePermissive(slotRestrictionsArePermissive);
		slot.getRestrictedVessels().addAll(slotVesselRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegateVesselRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertEquals(slotVesselRestrictions.size(), slotOrDelegateVesselRestrictions.size());
		Assertions.assertTrue(slotVesselRestrictions.containsAll(slotOrDelegateVesselRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean delegateRestrictionsArePermissive, final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Vessel> delegateVesselRestrictions,
			@NonNull final Set<@NonNull Vessel> slotVesselRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		Assertions.assertFalse(slotVesselRestrictions.equals(delegateVesselRestrictions));

		spotMarket.setRestrictedVesselsArePermissive(delegateRestrictionsArePermissive);
		spotMarket.getRestrictedVessels().addAll(delegateVesselRestrictions);
		spotSlot.setRestrictedVesselsOverride(true);
		spotSlot.setRestrictedVesselsArePermissive(slotRestrictionsArePermissive);
		spotSlot.getRestrictedVessels().addAll(slotVesselRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegateVesselRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Vessel> slotOrDelegateVesselRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegateVesselRestrictions());
		Assertions.assertEquals(slotVesselRestrictions.size(), slotOrDelegateVesselRestrictions.size());
		Assertions.assertTrue(slotVesselRestrictions.containsAll(slotOrDelegateVesselRestrictions));
	}

	private @NonNull PurchaseContract buildBasicPurchaseContract() {
		final PurchaseContract pc = commercialModelBuilder.makeExpressionPurchaseContract("purchaseContract", entity, "1");
		// Sanity checks
		Assertions.assertFalse(pc.isRestrictedVesselsArePermissive());
		Assertions.assertTrue(pc.getRestrictedVessels().isEmpty());
		return pc;
	}

	private @NonNull LoadSlot buildBasicLoadSlot(@NonNull final PurchaseContract pc) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final LocalDate loadStart = LocalDate.of(2021, 1, 20);
		final LoadSlot loadSlot = cargoModelBuilder.createFOBPurchase("loadSlot", loadStart, loadPort, pc, entity, null, 23.0);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedVesselsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull SalesContract buildBasicSalesContract() {
		final SalesContract sc = commercialModelBuilder.makeExpressionSalesContract("salesContract", entity, "1");
		// Sanity checks
		Assertions.assertFalse(sc.isRestrictedVesselsArePermissive());
		Assertions.assertTrue(sc.getRestrictedVessels().isEmpty());
		return sc;
	}

	private @NonNull DischargeSlot buildBasicDischargeSlot(@NonNull final SalesContract sc) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final LocalDate dischargeStart = LocalDate.of(2021, 2, 1);
		final DischargeSlot dischargeSlot = cargoModelBuilder.createDESSale("dischargeSlot", dischargeStart, dischargePort, sc, entity, null);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedVesselsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
		return dischargeSlot;
	}

	private @NonNull FOBPurchasesMarket buildBasicFobPurchaseMarket() {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final FOBPurchasesMarket pm = spotMarketsModelBuilder.makeFOBPurchaseMarket("fobPurchaseMarket", loadPort, entity, "1", 23.0).withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedVesselsArePermissive());
		Assertions.assertTrue(pm.getRestrictedVessels().isEmpty());
		return pm;
	}

	private @NonNull DESPurchaseMarket buildBasicDesPurchaseMarket() {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final DESPurchaseMarket pm = spotMarketsModelBuilder.makeDESPurchaseMarket("desPurchaseMarket", Collections.singletonList(dischargePort), entity, "1", 23.0).withEnabled(true)
				.withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedVesselsArePermissive());
		Assertions.assertTrue(pm.getRestrictedVessels().isEmpty());
		return pm;
	}

	private @NonNull SpotLoadSlot buildBasicSpotLoadSlot(@NonNull final FOBPurchasesMarket pm) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final SpotLoadSlot loadSlot = cargoModelBuilder.createSpotFOBPurchase("spotLoadSlot", pm, YearMonth.of(2021, 1), loadPort);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedVesselsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull SpotLoadSlot buildBasicSpotLoadSlot(@NonNull final DESPurchaseMarket pm) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final SpotLoadSlot loadSlot = cargoModelBuilder.createSpotDESPurchase("spotLoadSlot", pm, YearMonth.of(2021, 1), dischargePort);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedVesselsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull DESSalesMarket buildBasicDesSalesMarket() {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final DESSalesMarket pm = spotMarketsModelBuilder.makeDESSaleMarket("desSalesMarket", dischargePort, entity, "1").withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedVesselsArePermissive());
		Assertions.assertTrue(pm.getRestrictedVessels().isEmpty());
		return pm;
	}

	private @NonNull FOBSalesMarket buildBasicFobSalesMarket() {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final FOBSalesMarket pm = spotMarketsModelBuilder.makeFOBSaleMarket("fobSalesMarket", Collections.singletonList(loadPort), entity, "1").withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedVesselsArePermissive());
		Assertions.assertTrue(pm.getRestrictedVessels().isEmpty());
		return pm;
	}

	private @NonNull SpotDischargeSlot buildBasicSpotDischargeSlot(@NonNull final DESSalesMarket sm) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final SpotDischargeSlot dischargeSlot = cargoModelBuilder.createSpotDESSale("spotDischargeSlot", sm, YearMonth.of(2021, 2), dischargePort);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedVesselsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
		return dischargeSlot;
	}

	private @NonNull SpotDischargeSlot buildBasicSpotDischargeSlot(@NonNull final FOBSalesMarket sm) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final SpotDischargeSlot dischargeSlot = cargoModelBuilder.createSpotFOBSale("spotDischargeSlot", sm, YearMonth.of(2021, 2), loadPort);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedVesselsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegateVesselRestrictionsArePermissive());
		return dischargeSlot;
	}

	/*
	 * [Override: None], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testNoRestrictionsAnywhere() {
		noRestrictionsAnywhereTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot);
		noRestrictionsAnywhereTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot);
		spotNoRestrictionsAnywhereTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot);
		spotNoRestrictionsAnywhereTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot);
		spotNoRestrictionsAnywhereTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot);
		spotNoRestrictionsAnywhereTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot);
	}

	/*
	 * [Override: None], [Delegate: Allow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testNoRestrictionsOverrideWithAllowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = true;
		@NonNull
		final Set<@NonNull Vessel> delegateVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
	}

	/*
	 * [Override: None], [Delegate: Disallow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testNoRestrictionsOverrideWithDisallowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Vessel> delegateVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateVesselRestrictions);
	}

	/*
	 * [Override: Allow], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testAllowRestrictionsOverrideWithNoRestrictionsOnDelegate() {
		final boolean slotRestrictionsArePermissive = true;
		@NonNull
		final Set<@NonNull Vessel> slotVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
	}

	/*
	 * [Override: Allow type], [Delegate: Allow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testAllowRestrictionsOverrideWithAllowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = true;
		final boolean slotRestrictionsArePermissive = true;
		@NonNull
		final Set<@NonNull Vessel> delegateVesselRestrictions = buildVesselsSetFromNames(VESSEL_MEDIUM, VESSEL_LARGE);
		@NonNull
		final Set<@NonNull Vessel> slotVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
	}

	/*
	 * [Override: Allow], [Delegate: Disallow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testAllowRestrictionsOverrideWithDisallowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = false;
		final boolean slotRestrictionsArePermissive = true;
		@NonNull
		final Set<@NonNull Vessel> delegateVesselRestrictions = buildVesselsSetFromNames(VESSEL_MEDIUM, VESSEL_LARGE);
		@NonNull
		final Set<@NonNull Vessel> slotVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
	}

	/*
	 * [Override: Disallow], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testDisallowRestrictionsOverrideWithNoRestrictionsOnDelegate() {
		final boolean slotRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Vessel> slotVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotVesselRestrictions);
	}

	/*
	 * [Override: Disallow], [Delegate: Allow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testDisallowRestrictionsOverrideWithAllowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = true;
		final boolean slotRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Vessel> delegateVesselRestrictions = buildVesselsSetFromNames(VESSEL_MEDIUM, VESSEL_LARGE);
		@NonNull
		final Set<@NonNull Vessel> slotVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);

		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
	}

	/*
	 * [Override: Disallow], [Delegate: Disallow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testDisallowRestrictionsOverrideWithDisallowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = false;
		final boolean slotRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Vessel> delegateVesselRestrictions = buildVesselsSetFromNames(VESSEL_MEDIUM, VESSEL_LARGE);
		@NonNull
		final Set<@NonNull Vessel> slotVesselRestrictions = buildVesselsSetFromNames(VESSEL_SMALL);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateVesselRestrictions, slotVesselRestrictions);
	}
}
