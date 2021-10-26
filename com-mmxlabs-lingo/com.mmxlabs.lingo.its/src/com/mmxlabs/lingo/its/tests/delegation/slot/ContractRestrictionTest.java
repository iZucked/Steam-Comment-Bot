/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.delegation.slot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class ContractRestrictionTest extends AbstractMicroTestCase {

	@NonNull
	private static final String PURCHASE_CONTRACT_CHOSEN = "purchaseContractChosen";
	@NonNull
	private static final String PURCHASE_CONTRACT_A = "purchaseContractA";
	@NonNull
	private static final String PURCHASE_CONTRACT_B = "purchaseContractB";
	@NonNull
	private static final String PURCHASE_CONTRACT_C = "purchaseContractC";

	@NonNull
	private static final String SALES_CONTRACT_CHOSEN = "salesContractChosen";
	@NonNull
	private static final String SALES_CONTRACT_A = "salesContractA";
	@NonNull
	private static final String SALES_CONTRACT_B = "salesContractB";
	@NonNull
	private static final String SALES_CONTRACT_C = "salesContractC";

	@NonNull
	private static final String LOAD_PORT = InternalDataConstants.PORT_CAMERON;
	@NonNull
	private static final String DISCHARGE_PORT = InternalDataConstants.PORT_HIMEJI;

	private <T extends Contract> @NonNull Set<@NonNull T> buildContractsSetFromNames(final Function<String, T> contractFinder, final String... contractNames) {
		return Arrays.stream(contractNames).map(contractFinder).collect(Collectors.toSet());
	}

	private static <T extends Contract> void noRestrictionsAnywhereTest(@NonNull final T contract, final Function<@NonNull T, @NonNull Slot<T>> slotMaker) {
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		final List<Contract> slotOrDelegateContractRestrictions = slot.getSlotOrDelegateContractRestrictions();
		Assertions.assertFalse(slot.getSlotOrDelegateContractRestrictionsArePermissive());
		Assertions.assertTrue(slotOrDelegateContractRestrictions.isEmpty());
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotNoRestrictionsAnywhereTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		final List<Contract> slotOrDelegateContractRestrictions = spotSlot.getSlotOrDelegateContractRestrictions();
		Assertions.assertFalse(spotSlot.getSlotOrDelegateContractRestrictionsArePermissive());
		Assertions.assertTrue(slotOrDelegateContractRestrictions.isEmpty());
	}

	private static <T extends Contract> void noRestrictionsOverrideWithDelegateRestrictionsTest(@NonNull final T contract, final Function<@NonNull T, @NonNull Slot<T>> slotMaker,
			final boolean delegateRestrictionsArePermissive, @NonNull final Set<@NonNull Contract> delegateContractRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		contract.setRestrictedContractsArePermissive(delegateRestrictionsArePermissive);
		contract.getRestrictedContracts().addAll(delegateContractRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegateContractRestrictionsArePermissive();
		if (delegateRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final List<Contract> slotOrDelegateContractRestrictions = slot.getSlotOrDelegateContractRestrictions();
		Assertions.assertEquals(delegateContractRestrictions.size(), slotOrDelegateContractRestrictions.size());
		Assertions.assertTrue(delegateContractRestrictions.containsAll(slotOrDelegateContractRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotNoRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean delegateRestrictionsArePermissive, @NonNull final Set<@NonNull Contract> delegateContractRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		spotMarket.setRestrictedContractsArePermissive(delegateRestrictionsArePermissive);
		spotMarket.getRestrictedContracts().addAll(delegateContractRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegateContractRestrictionsArePermissive();
		if (delegateRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final List<Contract> slotOrDelegateContractRestrictions = spotSlot.getSlotOrDelegateContractRestrictions();
		Assertions.assertEquals(delegateContractRestrictions.size(), slotOrDelegateContractRestrictions.size());
		Assertions.assertTrue(delegateContractRestrictions.containsAll(slotOrDelegateContractRestrictions));
	}

	private static <T extends Contract> void restrictionsOverrideWithNoDelegateRestrictionsTest(@NonNull final T contract, final Function<@NonNull T, @NonNull Slot<T>> slotMaker,
			final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Contract> slotContractRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final Slot<T> slot = slotMaker.apply(contract);

		slot.setRestrictedContractsOverride(true);
		slot.setRestrictedContractsArePermissive(slotRestrictionsArePermissive);
		slot.getRestrictedContracts().addAll(slotContractRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegateContractRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final List<Contract> slotOrDelegateContractRestrictions = slot.getSlotOrDelegateContractRestrictions();
		Assertions.assertEquals(slotContractRestrictions.size(), slotOrDelegateContractRestrictions.size());
		Assertions.assertTrue(slotContractRestrictions.containsAll(slotOrDelegateContractRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotRestrictionsOverrideWithNoDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Contract> slotContractRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		spotSlot.setRestrictedContractsOverride(true);
		spotSlot.setRestrictedContractsArePermissive(slotRestrictionsArePermissive);
		spotSlot.getRestrictedContracts().addAll(slotContractRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegateContractRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final List<Contract> slotOrDelegateContractRestrictions = spotSlot.getSlotOrDelegateContractRestrictions();
		Assertions.assertEquals(slotContractRestrictions.size(), slotOrDelegateContractRestrictions.size());
		Assertions.assertTrue(slotContractRestrictions.containsAll(slotOrDelegateContractRestrictions));
	}

	private static <T extends Contract> void restrictionsOverrideWithDelegateRestrictionsTest(@NonNull final T contract, @NonNull final Function<@NonNull T, @NonNull Slot<T>> slotMaker,
			final boolean delegateRestrictionsArePermissive, final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Contract> delegateContractRestrictions,
			@NonNull final Set<@NonNull Contract> slotContractRestrictions) {
		@SuppressWarnings("null")
		@NonNull
		final Slot<@NonNull T> slot = slotMaker.apply(contract);

		Assertions.assertFalse(slotContractRestrictions.equals(delegateContractRestrictions));

		contract.setRestrictedContractsArePermissive(delegateRestrictionsArePermissive);
		contract.getRestrictedContracts().addAll(delegateContractRestrictions);
		slot.setRestrictedContractsOverride(true);
		slot.setRestrictedContractsArePermissive(slotRestrictionsArePermissive);
		slot.getRestrictedContracts().addAll(slotContractRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = slot.getSlotOrDelegateContractRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final List<Contract> slotOrDelegateContractRestrictions = slot.getSlotOrDelegateContractRestrictions();
		Assertions.assertEquals(slotContractRestrictions.size(), slotOrDelegateContractRestrictions.size());
		Assertions.assertTrue(slotContractRestrictions.containsAll(slotOrDelegateContractRestrictions));
	}

	private static <T extends SpotMarket, U extends Slot & SpotSlot> void spotRestrictionsOverrideWithDelegateRestrictionsTest(final Supplier<T> spotMarketMaker, final Function<T, U> spotSlotMaker,
			final boolean delegateRestrictionsArePermissive, final boolean slotRestrictionsArePermissive, @NonNull final Set<@NonNull Contract> delegateContractRestrictions,
			@NonNull final Set<@NonNull Contract> slotContractRestrictions) {
		final T spotMarket = spotMarketMaker.get();
		final U spotSlot = spotSlotMaker.apply(spotMarket);

		Assertions.assertFalse(slotContractRestrictions.equals(delegateContractRestrictions));

		spotMarket.setRestrictedContractsArePermissive(delegateRestrictionsArePermissive);
		spotMarket.getRestrictedContracts().addAll(delegateContractRestrictions);
		spotSlot.setRestrictedContractsOverride(true);
		spotSlot.setRestrictedContractsArePermissive(slotRestrictionsArePermissive);
		spotSlot.getRestrictedContracts().addAll(slotContractRestrictions);

		final boolean slotOrDelegateRestrictionsArePermissive = spotSlot.getSlotOrDelegateContractRestrictionsArePermissive();
		if (slotRestrictionsArePermissive) {
			Assertions.assertTrue(slotOrDelegateRestrictionsArePermissive);
		} else {
			Assertions.assertFalse(slotOrDelegateRestrictionsArePermissive);
		}
		final List<Contract> slotOrDelegateContractRestrictions = spotSlot.getSlotOrDelegateContractRestrictions();
		Assertions.assertEquals(slotContractRestrictions.size(), slotOrDelegateContractRestrictions.size());
		Assertions.assertTrue(slotContractRestrictions.containsAll(slotOrDelegateContractRestrictions));
	}

	private @NonNull PurchaseContract buildBasicPurchaseContract(@NonNull final String name) {
		final PurchaseContract pc = commercialModelBuilder.makeExpressionPurchaseContract(name, entity, "1");
		// Sanity checks
		Assertions.assertFalse(pc.isRestrictedContractsArePermissive());
		Assertions.assertTrue(pc.getRestrictedContracts().isEmpty());
		return pc;
	}

	private @NonNull LoadSlot buildBasicLoadSlot(@NonNull final PurchaseContract pc) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final LocalDate loadStart = LocalDate.of(2021, 1, 20);
		final LoadSlot loadSlot = cargoModelBuilder.createFOBPurchase("loadSlot", loadStart, loadPort, pc, entity, null, 23.0);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedContractsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegateContractRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull SalesContract buildBasicSalesContract(@NonNull final String name) {
		final SalesContract sc = commercialModelBuilder.makeExpressionSalesContract(name, entity, "1");
		// Sanity checks
		Assertions.assertFalse(sc.isRestrictedContractsArePermissive());
		Assertions.assertTrue(sc.getRestrictedContracts().isEmpty());
		return sc;
	}

	private @NonNull DischargeSlot buildBasicDischargeSlot(@NonNull final SalesContract sc) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final LocalDate dischargeStart = LocalDate.of(2021, 2, 1);
		final DischargeSlot dischargeSlot = cargoModelBuilder.createDESSale("dischargeSlot", dischargeStart, dischargePort, sc, entity, null);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedContractsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegateContractRestrictionsArePermissive());
		return dischargeSlot;
	}

	private @NonNull FOBPurchasesMarket buildBasicFobPurchaseMarket() {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final FOBPurchasesMarket pm = spotMarketsModelBuilder.makeFOBPurchaseMarket("fobPurchaseMarket", loadPort, entity, "1", 23.0).withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedContractsArePermissive());
		Assertions.assertTrue(pm.getRestrictedContracts().isEmpty());
		return pm;
	}

	private @NonNull DESPurchaseMarket buildBasicDesPurchaseMarket() {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final DESPurchaseMarket pm = spotMarketsModelBuilder.makeDESPurchaseMarket("desPurchaseMarket", Collections.singletonList(dischargePort), entity, "1", 23.0).withEnabled(true)
				.withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedContractsArePermissive());
		Assertions.assertTrue(pm.getRestrictedContracts().isEmpty());
		return pm;
	}

	private @NonNull SpotLoadSlot buildBasicSpotLoadSlot(@NonNull final FOBPurchasesMarket pm) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final SpotLoadSlot loadSlot = cargoModelBuilder.createSpotFOBPurchase("spotLoadSlot", pm, YearMonth.of(2021, 1), loadPort);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedContractsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegateContractRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull SpotLoadSlot buildBasicSpotLoadSlot(@NonNull final DESPurchaseMarket pm) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final SpotLoadSlot loadSlot = cargoModelBuilder.createSpotDESPurchase("spotLoadSlot", pm, YearMonth.of(2021, 1), dischargePort);
		// Sanity checks
		Assertions.assertFalse(loadSlot.isRestrictedContractsOverride());
		Assertions.assertFalse(loadSlot.getSlotOrDelegateContractRestrictionsArePermissive());
		return loadSlot;
	}

	private @NonNull DESSalesMarket buildBasicDesSalesMarket() {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final DESSalesMarket pm = spotMarketsModelBuilder.makeDESSaleMarket("desSalesMarket", dischargePort, entity, "1").withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedContractsArePermissive());
		Assertions.assertTrue(pm.getRestrictedContracts().isEmpty());
		return pm;
	}

	private @NonNull FOBSalesMarket buildBasicFobSalesMarket() {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final FOBSalesMarket pm = spotMarketsModelBuilder.makeFOBSaleMarket("fobSalesMarket", Collections.singletonList(loadPort), entity, "1").withEnabled(true).withAvailabilityConstant(1).build();
		// Sanity checks
		Assertions.assertFalse(pm.isRestrictedContractsArePermissive());
		Assertions.assertTrue(pm.getRestrictedContracts().isEmpty());
		return pm;
	}

	private @NonNull SpotDischargeSlot buildBasicSpotDischargeSlot(@NonNull final DESSalesMarket sm) {
		final Port dischargePort = portFinder.findPortById(DISCHARGE_PORT);
		final SpotDischargeSlot dischargeSlot = cargoModelBuilder.createSpotDESSale("spotDischargeSlot", sm, YearMonth.of(2021, 2), dischargePort);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedContractsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegateContractRestrictionsArePermissive());
		return dischargeSlot;
	}

	private @NonNull SpotDischargeSlot buildBasicSpotDischargeSlot(@NonNull final FOBSalesMarket sm) {
		final Port loadPort = portFinder.findPortById(LOAD_PORT);
		final SpotDischargeSlot dischargeSlot = cargoModelBuilder.createSpotFOBSale("spotDischargeSlot", sm, YearMonth.of(2021, 2), loadPort);
		// Sanity checks
		Assertions.assertFalse(dischargeSlot.isRestrictedContractsOverride());
		Assertions.assertFalse(dischargeSlot.getSlotOrDelegateContractRestrictionsArePermissive());
		return dischargeSlot;
	}

	@BeforeEach
	public void init() {
		buildBasicPurchaseContract(PURCHASE_CONTRACT_CHOSEN);
		buildBasicPurchaseContract(PURCHASE_CONTRACT_A);
		buildBasicPurchaseContract(PURCHASE_CONTRACT_B);
		buildBasicPurchaseContract(PURCHASE_CONTRACT_C);

		buildBasicSalesContract(SALES_CONTRACT_CHOSEN);
		buildBasicSalesContract(SALES_CONTRACT_A);
		buildBasicSalesContract(SALES_CONTRACT_B);
		buildBasicSalesContract(SALES_CONTRACT_C);
	}

	/*
	 * [Override: None], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testNoRestrictionsAnywhere() {
		noRestrictionsAnywhereTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot);
		noRestrictionsAnywhereTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot);
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
		final Set<@NonNull Contract> delegatePurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_CHOSEN, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> delegateSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_CHOSEN, SALES_CONTRACT_A);
		noRestrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, delegateRestrictionsArePermissive,
				delegateSalesContractRestrictions);
		noRestrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, delegateRestrictionsArePermissive,
				delegatePurchaseContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateSalesContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateSalesContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive,
				delegatePurchaseContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive,
				delegatePurchaseContractRestrictions);
	}

	/*
	 * [Override: None], [Delegate: Disallow]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testNoRestrictionsOverrideWithDisallowRestrictionsOnDelegate() {
		final boolean delegateRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Contract> delegatePurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> delegateSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_A);
		noRestrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, delegateRestrictionsArePermissive,
				delegateSalesContractRestrictions);
		noRestrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, delegateRestrictionsArePermissive,
				delegatePurchaseContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateSalesContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, delegateSalesContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive,
				delegatePurchaseContractRestrictions);
		spotNoRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive,
				delegatePurchaseContractRestrictions);
	}

	/*
	 * [Override: Allow], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testAllowRestrictionsOverrideWithNoRestrictionsOnDelegate() {
		final boolean slotRestrictionsArePermissive = true;
		@NonNull
		final Set<@NonNull Contract> slotPurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_CHOSEN, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> slotSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_CHOSEN, SALES_CONTRACT_A);
		restrictionsOverrideWithNoDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, slotRestrictionsArePermissive,
				slotSalesContractRestrictions);
		restrictionsOverrideWithNoDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, slotRestrictionsArePermissive,
				slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotPurchaseContractRestrictions);
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
		final Set<@NonNull Contract> delegatePurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_CHOSEN, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> delegateSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_CHOSEN, SALES_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> slotPurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_CHOSEN, PURCHASE_CONTRACT_B,
				PURCHASE_CONTRACT_C);
		@NonNull
		final Set<@NonNull Contract> slotSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_CHOSEN, SALES_CONTRACT_B, SALES_CONTRACT_C);
		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegateSalesContractRestrictions, slotSalesContractRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
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
		final Set<@NonNull Contract> delegatePurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> delegateSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> slotPurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_CHOSEN, PURCHASE_CONTRACT_B,
				PURCHASE_CONTRACT_C);
		@NonNull
		final Set<@NonNull Contract> slotSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_CHOSEN, SALES_CONTRACT_B, SALES_CONTRACT_C);
		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegateSalesContractRestrictions, slotSalesContractRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
	}

	/*
	 * [Override: Disallow], [Delegate: None]
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	void testDisallowRestrictionsOverrideWithNoRestrictionsOnDelegate() {
		final boolean slotRestrictionsArePermissive = false;
		@NonNull
		final Set<@NonNull Contract> slotPurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> slotSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_A);
		restrictionsOverrideWithNoDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, slotRestrictionsArePermissive,
				slotSalesContractRestrictions);
		restrictionsOverrideWithNoDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, slotRestrictionsArePermissive,
				slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, slotRestrictionsArePermissive, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithNoDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, slotRestrictionsArePermissive, slotPurchaseContractRestrictions);
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
		final Set<@NonNull Contract> delegatePurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_CHOSEN, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> delegateSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_CHOSEN, SALES_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> slotPurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_B, PURCHASE_CONTRACT_C);
		@NonNull
		final Set<@NonNull Contract> slotSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_B, SALES_CONTRACT_C);

		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegateSalesContractRestrictions, slotSalesContractRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
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
		final Set<@NonNull Contract> delegatePurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> delegateSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_A);
		@NonNull
		final Set<@NonNull Contract> slotPurchaseContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findPurchaseContract, PURCHASE_CONTRACT_B, PURCHASE_CONTRACT_C);
		@NonNull
		final Set<@NonNull Contract> slotSalesContractRestrictions = buildContractsSetFromNames(commercialModelFinder::findSalesContract, SALES_CONTRACT_B, SALES_CONTRACT_C);
		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findPurchaseContract(PURCHASE_CONTRACT_CHOSEN), this::buildBasicLoadSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegateSalesContractRestrictions, slotSalesContractRestrictions);
		restrictionsOverrideWithDelegateRestrictionsTest(commercialModelFinder.findSalesContract(SALES_CONTRACT_CHOSEN), this::buildBasicDischargeSlot, delegateRestrictionsArePermissive,
				slotRestrictionsArePermissive, delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesPurchaseMarket, this::buildBasicSpotLoadSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegateSalesContractRestrictions, slotSalesContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicDesSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
		spotRestrictionsOverrideWithDelegateRestrictionsTest(this::buildBasicFobSalesMarket, this::buildBasicSpotDischargeSlot, delegateRestrictionsArePermissive, slotRestrictionsArePermissive,
				delegatePurchaseContractRestrictions, slotPurchaseContractRestrictions);
	}
}
