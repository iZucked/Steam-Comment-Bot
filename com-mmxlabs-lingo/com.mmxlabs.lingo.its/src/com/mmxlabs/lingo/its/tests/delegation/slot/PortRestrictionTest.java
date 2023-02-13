/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.util.SetUtils;

public class PortRestrictionTest extends AbstractMicroTestCase {
	@NonNull
	private static final String LOAD_PORT_CHOSEN = InternalDataConstants.PORT_CAMERON;
	@NonNull
	private static final String DISCHARGE_PORT_CHOSEN = InternalDataConstants.PORT_HIMEJI;

	@NonNull
	private static final String OTHER_LOAD_PORT_A = InternalDataConstants.PORT_COVE_POINT;
	@NonNull
	private static final String OTHER_LOAD_PORT_B = InternalDataConstants.PORT_DARWIN;
	@NonNull
	private static final String OTHER_LOAD_PORT_C = InternalDataConstants.PORT_ONSLOW;

	@NonNull
	private static final String OTHER_DISCHARGE_PORT_A = InternalDataConstants.PORT_CHITA;
	@NonNull
	private static final String OTHER_DISCHARGE_PORT_B = InternalDataConstants.PORT_FUTTSU;
	@NonNull
	private static final String OTHER_DISCHARGE_PORT_C = InternalDataConstants.PORT_BORYEONG;

	private @NonNull Set<@NonNull Port> buildPortsSetFromNames(final String... portNames) {
		return Arrays.stream(portNames).map(portFinder::findPortById).collect(Collectors.toSet());
	}

	private static <T extends Contract> void noRestrictionsAnywhereTest(final Supplier<@NonNull T> contractMaker, final Function<@NonNull T, @NonNull Slot<T>> slotMaker) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(slot.getSlotOrDelegatePortRestrictions());
		Assertions.assertFalse(slot.getSlotOrDelegatePortRestrictionsArePermissive());
		Assertions.assertTrue(slotOrDelegatePortRestrictions.isEmpty());
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotNoRestrictionsAnywhereTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegatePortRestrictions());
		Assertions.assertFalse(spotSlot.getSlotOrDelegatePortRestrictionsArePermissive());
		Assertions.assertTrue(slotOrDelegatePortRestrictions.isEmpty());
	}

	private static <T extends Contract> void noRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<@NonNull T> contractMaker, final Function<@NonNull T, @NonNull Slot<T>> slotMaker,
			final boolean delegateRestrictionsArePermissive, @NonNull final Set<@NonNull Port> delegatePortRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		contract.setRestrictedPortsArePermissive(delegateRestrictionsArePermissive);
		contract.getRestrictedPorts().addAll(delegatePortRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegatePortRestrictionsArePermissive();
		if (delegateRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(slot.getSlotOrDelegatePortRestrictions());
		Assertions.assertEquals(delegatePortRestrictions.size(), slotOrDelegatePortRestrictions.size());
		Assertions.assertTrue(delegatePortRestrictions.containsAll(slotOrDelegatePortRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotNoRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean delegateRestrictionsArePermissive, @NonNull final Set<@NonNull Port> delegatePortRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		spotMarket.setRestrictedPortsArePermissive(delegateRestrictionsArePermissive);
		spotMarket.getRestrictedPorts().addAll(delegatePortRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegatePortRestrictionsArePermissive();
		if (delegateRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegatePortRestrictions());
		Assertions.assertEquals(delegatePortRestrictions.size(), slotOrDelegatePortRestrictions.size());
		Assertions.assertTrue(delegatePortRestrictions.containsAll(slotOrDelegatePortRestrictions));
	}

	private static <T extends Contract> void restrictionsOverrideWithNoDelegateRestrictionsTest(final Supplier<@NonNull T> contractMaker, final Function<@NonNull T, @NonNull Slot<T>> slotMaker,
			final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Port> slotPortRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		slot.setRestrictedPortsOverride(true);
		slot.setRestrictedPortsArePermissive(slotRestrictionsArePermissive);
		slot.getRestrictedPorts().addAll(slotPortRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegatePortRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(slot.getSlotOrDelegatePortRestrictions());
		Assertions.assertEquals(slotPortRestrictions.size(), slotOrDelegatePortRestrictions.size());
		Assertions.assertTrue(slotPortRestrictions.containsAll(slotOrDelegatePortRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotRestrictionsOverrideWithNoDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Port> slotPortRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		spotSlot.setRestrictedPortsOverride(true);
		spotSlot.setRestrictedPortsArePermissive(slotRestrictionsArePermissive);
		spotSlot.getRestrictedPorts().addAll(slotPortRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegatePortRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegatePortRestrictions());
		Assertions.assertEquals(slotPortRestrictions.size(), slotOrDelegatePortRestrictions.size());
		Assertions.assertTrue(slotPortRestrictions.containsAll(slotOrDelegatePortRestrictions));
	}

	private static <T extends Contract> void restrictionsOverrideWithDelegateRestrictionsTest(@NonNull final Supplier<@NonNull T> contractMaker,
			@NonNull final Function<@NonNull T, @NonNull Slot<T>> slotMaker, final boolean delegateRestrictionsArePermissive, final boolean slotRestrictionsArePermissive,
			@NonNull final Set<@NonNull Port> delegatePortRestrictions, @NonNull final Set<@NonNull Port> slotPortRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final T contract = contractMaker.get();
		@SuppressWarnings("null")
		@NonNull
		final Slot<@NonNull T> slot = slotMaker.apply(contract);

		Assertions.assertFalse(slotPortRestrictions.equals(delegatePortRestrictions));

		contract.setRestrictedPortsArePermissive(delegateRestrictionsArePermissive);
		contract.getRestrictedPorts().addAll(delegatePortRestrictions);
		slot.setRestrictedPortsOverride(true);
		slot.setRestrictedPortsArePermissive(slotRestrictionsArePermissive);
		slot.getRestrictedPorts().addAll(slotPortRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegatePortRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(slot.getSlotOrDelegatePortRestrictions());
		Assertions.assertEquals(slotPortRestrictions.size(), slotOrDelegatePortRestrictions.size());
		Assertions.assertTrue(slotPortRestrictions.containsAll(slotOrDelegatePortRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean delegateRestrictionsArePermissive, final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Port> delegatePortRestrictions,
			@NonNull final Set<@NonNull Port> slotPortRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		Assertions.assertFalse(slotPortRestrictions.equals(delegatePortRestrictions));

		spotMarket.setRestrictedPortsArePermissive(delegateRestrictionsArePermissive);
		spotMarket.getRestrictedPorts().addAll(delegatePortRestrictions);
		spotSlot.setRestrictedPortsOverride(true);
		spotSlot.setRestrictedPortsArePermissive(slotRestrictionsArePermissive);
		spotSlot.getRestrictedPorts().addAll(slotPortRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegatePortRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final Set<Port> slotOrDelegatePortRestrictions = SetUtils.getObjects(spotSlot.getSlotOrDelegatePortRestrictions());
		Assertions.assertEquals(slotPortRestrictions.size(), slotOrDelegatePortRestrictions.size());
		Assertions.assertTrue(slotPortRestrictions.containsAll(slotOrDelegatePortRestrictions));
	}

	private @NonNull PurchaseContract buildBasicPurchaseContract() {
		final PurchaseContract pc = commercialModelBuilder.makeExpressionPurchaseContract("purchaseContract", entity, "1");
		// Sanity checks
		Assertions.assertFalse(pc.isRestrictedPortsArePermissive());
		Assertions.assertTrue(pc.getRestrictedPorts().isEmpty());
		return pc;
	}

	private @NonNull LoadSlot buildBasicLoadSlot(@NonNull final PurchaseContract pc) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT_CHOSEN);
		final LocalDate loadStart = LocalDate.of(2021, 1, 20);
		final LoadSlot loadSlot = cargoModelBuilder.createFOBPurchase("loadSlot", loadStart, loadPort, pc, entity, null, 23.0);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedPortsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegatePortRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull SalesContract buildBasicSalesContract() {
		final SalesContract sc = commercialModelBuilder.makeExpressionSalesContract("salesContract", entity, "1");
		// Sanity checks
		Assertions.assertFalse(sc.isRestrictedPortsArePermissive());
		Assertions.assertTrue(sc.getRestrictedPorts().isEmpty());
		return sc;
	}

	private @NonNull DischargeSlot buildBasicDischargeSlot(@NonNull final SalesContract sc) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT_CHOSEN);
		final LocalDate dischargeStart = LocalDate.of(2021, 2, 1);
		final DischargeSlot dischargeSlot = cargoModelBuilder.createDESSale("dischargeSlot", dischargeStart, dischargePort, sc, entity, null);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedPortsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegatePortRestrictionsArePermissive());
		return dischargeSlot;
	}

	private @NonNull FOBPurchasesMarket buildBasicFobPurchaseMarket() {
		final Port loadPort = portFinder.findPortById(LOAD_PORT_CHOSEN);
		final FOBPurchasesMarket pm = spotMarketsModelBuilder.makeFOBPurchaseMarket("fobPurchaseMarket", loadPort, entity, "1", 23.0).withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedPortsArePermissive());
		Assertions.assertTrue(pm.getRestrictedPorts().isEmpty());
		return pm;
	}

	private @NonNull DESPurchaseMarket buildBasicDesPurchaseMarket() {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT_CHOSEN);
		final DESPurchaseMarket pm = spotMarketsModelBuilder.makeDESPurchaseMarket("desPurchaseMarket", Collections.singletonList(dischargePort), entity, "1", 23.0).withEnabled(true)
				.withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedPortsArePermissive());
		Assertions.assertTrue(pm.getRestrictedPorts().isEmpty());
		return pm;
	}

	private @NonNull SpotLoadSlot buildBasicSpotLoadSlot(@NonNull final FOBPurchasesMarket pm) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT_CHOSEN);
		final SpotLoadSlot loadSlot = cargoModelBuilder.createSpotFOBPurchase("spotLoadSlot", pm, YearMonth.of(2021, 1), loadPort);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedPortsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegatePortRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull SpotLoadSlot buildBasicSpotLoadSlot(@NonNull final DESPurchaseMarket pm) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT_CHOSEN);
		final SpotLoadSlot loadSlot = cargoModelBuilder.createSpotDESPurchase("spotLoadSlot", pm, YearMonth.of(2021, 1), dischargePort);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedPortsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegatePortRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull DESSalesMarket buildBasicDesSalesMarket() {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT_CHOSEN);
		final DESSalesMarket pm = spotMarketsModelBuilder.makeDESSaleMarket("desSalesMarket", dischargePort, entity, "1").withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedPortsArePermissive());
		Assertions.assertTrue(pm.getRestrictedPorts().isEmpty());
		return pm;
	}

	private @NonNull FOBSalesMarket buildBasicFobSalesMarket() {
		final Port loadPort = portFinder.findPortById(LOAD_PORT_CHOSEN);
		final FOBSalesMarket pm = spotMarketsModelBuilder.makeFOBSaleMarket("fobSalesMarket", Collections.singletonList(loadPort), entity, "1").withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedPortsArePermissive());
		Assertions.assertTrue(pm.getRestrictedPorts().isEmpty());
		return pm;
	}

	private @NonNull SpotDischargeSlot buildBasicSpotDischargeSlot(@NonNull final DESSalesMarket sm) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT_CHOSEN);
		final SpotDischargeSlot dischargeSlot = cargoModelBuilder.createSpotDESSale("spotDischargeSlot", sm, YearMonth.of(2021, 2), dischargePort);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedPortsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegatePortRestrictionsArePermissive());
		return dischargeSlot;
	}

	private @NonNull SpotDischargeSlot buildBasicSpotDischargeSlot(@NonNull final FOBSalesMarket sm) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT_CHOSEN);
		final SpotDischargeSlot dischargeSlot = cargoModelBuilder.createSpotFOBSale("spotDischargeSlot", sm, YearMonth.of(2021, 2), loadPort);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedPortsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegatePortRestrictionsArePermissive());
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
		final Set<@NonNull Port> delegateLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> delegateDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, delegateDischargePortRestrictions);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, delegateLoadPortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateDischargePortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateDischargePortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateLoadPortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateLoadPortRestrictions);
	}

	/*
	 * [Override: None], [Delegate: Disallow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testNoRestrictionsOverrideWithDisallowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Port> delegateLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> delegateDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, delegateDischargePortRestrictions);
		noRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, delegateLoadPortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateDischargePortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateDischargePortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateLoadPortRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, delegateLoadPortRestrictions);
	}

	/*
	 * [Override: Allow], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testAllowRestrictionsOverrideWithNoRestrictionsOnDelegate() {
		final boolean slotRestrictionsArePermissive = true;
		@NonNull
		final Set<@NonNull Port> slotLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> slotDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, slotRestrictionsArePermissive, slotDischargePortRestrictions);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, slotRestrictionsArePermissive, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotLoadPortRestrictions);
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
		final Set<@NonNull Port> delegateLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> delegateDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		@NonNull
		final Set<@NonNull Port> slotLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_B, OTHER_LOAD_PORT_C);
		@NonNull
		final Set<@NonNull Port> slotDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_B, OTHER_DISCHARGE_PORT_C);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
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
		final Set<@NonNull Port> delegateLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> delegateDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		@NonNull
		final Set<@NonNull Port> slotLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_B, OTHER_LOAD_PORT_C);
		@NonNull
		final Set<@NonNull Port> slotDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_B, OTHER_DISCHARGE_PORT_C);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
	}

	/*
	 * [Override: Disallow], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testDisallowRestrictionsOverrideWithNoRestrictionsOnDelegate() {
		final boolean slotRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Port> slotLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> slotDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, slotRestrictionsArePermissive, slotDischargePortRestrictions);
		restrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, slotRestrictionsArePermissive, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotLoadPortRestrictions);
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
		final Set<@NonNull Port> delegateLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> delegateDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		@NonNull
		final Set<@NonNull Port> slotLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_B, OTHER_LOAD_PORT_C);
		@NonNull
		final Set<@NonNull Port> slotDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_B, OTHER_DISCHARGE_PORT_C);

		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
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
		final Set<@NonNull Port> delegateLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_A);
		@NonNull
		final Set<@NonNull Port> delegateDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_A);
		@NonNull
		final Set<@NonNull Port> slotLoadPortRestrictions = buildPortsSetFromNames(LOAD_PORT_CHOSEN, OTHER_LOAD_PORT_B, OTHER_LOAD_PORT_C);
		@NonNull
		final Set<@NonNull Port> slotDischargePortRestrictions = buildPortsSetFromNames(DISCHARGE_PORT_CHOSEN, OTHER_DISCHARGE_PORT_B, OTHER_DISCHARGE_PORT_C);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicPurchaseContract, this::buildBasicLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicSalesContract, this::buildBasicDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateDischargePortRestrictions, slotDischargePortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateLoadPortRestrictions, slotLoadPortRestrictions);
	}
}
