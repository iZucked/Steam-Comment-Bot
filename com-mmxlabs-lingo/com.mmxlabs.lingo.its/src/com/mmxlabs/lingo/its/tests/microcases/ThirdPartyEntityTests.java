/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.validation.ValidationTestUtil;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.validation.ThirdPartySlotEntityConstraint;
import com.mmxlabs.models.lng.cargo.validation.VesselCharterConstraint;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.validation.ThirdPartyContractEntityConstraint;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.validation.CharterInMarketConstraint;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ThirdPartyEntityTests extends AbstractMicroTestCase {

	@Override
	public void setPromptDates() {
		scenarioModelBuilder.setPromptPeriod(LocalDate.now(), LocalDate.now().plusMonths(3));
	}

	/**
	 * A third-party cargo can only be non-shipped and should have no P&L. It can
	 * however have a purchase cost and sales revenue
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNonShippedCargo() throws Exception {

		final LegalEntity entity = commercialModelBuilder.createEntity("Third", LocalDate.of(2000, Month.JANUARY, 1), 0);
		entity.setThirdParty(true);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "5", 23.5, null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.build() //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assertions.assertNotNull(cargoAllocation);
			Assertions.assertEquals(0L, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertNotEquals(0L, cargoAllocation.getSlotAllocations().get(0).getVolumeTransferred());
			Assertions.assertNotEquals(0L, cargoAllocation.getSlotAllocations().get(0).getVolumeValue());

			Assertions.assertNotEquals(0L, cargoAllocation.getSlotAllocations().get(1).getVolumeTransferred());
			Assertions.assertNotEquals(0L, cargoAllocation.getSlotAllocations().get(1).getVolumeValue());

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSlotKeepOpen() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		final LoadSlot slot = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2021, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build();

		slot.setEntity(thirdPartyEntity);

		// No error expected when slot is locked (i.e. keep open)
		slot.setLocked(true);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_SLOT_KEEP_OPEN);
			Assertions.assertTrue(children.isEmpty());

		}

		// Expect the keep open validation branch to be triggered when locked is false
		// (i.e. not keep open)
		slot.setLocked(false);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_SLOT_KEEP_OPEN);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(slot);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoNonShippedConstraint() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, thirdPartyEntity, "5", 23.5) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, thirdPartyEntity, "7") //
				.build() //
				.build();

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_CARGO_NON_SHIPPED);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(cargo1);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoNonShippedConstraint2() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, thirdPartyEntity, "5", 23.5, null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, thirdPartyEntity, "7") //
				.build() //
				.build();

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_CARGO_NON_SHIPPED);
			Assertions.assertTrue(children.isEmpty());
		}
	}

	/**
	 * Testing real and third-party entity mix triggers error
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoSameEntity1() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		final LegalEntity realEntity = commercialModelBuilder.createEntity("real-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		realEntity.setThirdParty(false);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, thirdPartyEntity, "5", 23.5, null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, realEntity, "7") //
				.build() //
				.build();

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_CARGO_SAME_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(cargo1);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	/**
	 * Similar to testCargoSameEntity1 but with two different third party entities
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoSameEntity2() {

		final LegalEntity thirdPartyEntity1 = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity1.setThirdParty(true);

		final LegalEntity thirdPartyEntity2 = commercialModelBuilder.createEntity("third-party-2", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity2.setThirdParty(true);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, thirdPartyEntity1, "5", 23.5, null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, thirdPartyEntity2, "7") //
				.build() //
				.build();

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_CARGO_SAME_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(cargo1);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	/**
	 * Test cargo need wiring locked
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoLockedWiring() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		final LegalEntity realEntity = commercialModelBuilder.createEntity("real-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		realEntity.setThirdParty(false);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, thirdPartyEntity, "5", 23.5, null) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, realEntity, "7") //
				.build() //
				.build();

		// No error expected when allow re-wiring is false
		cargo1.setAllowRewiring(false);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_CARGO_WIRING);
			Assertions.assertTrue(children.isEmpty());
		}

		// Error expected when allow re-wiring is true
		cargo1.setAllowRewiring(true);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartySlotEntityConstraint.KEY_CARGO_WIRING);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(cargo1);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	/**
	 * Test charter needs a real entity
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselCharterNeedRealEntity() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		final LegalEntity realEntity = commercialModelBuilder.createEntity("real-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		realEntity.setThirdParty(false);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter charter = cargoModelBuilder.makeVesselCharter(vessel, realEntity).build();

		// No error expected when using a real entity
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VesselCharterConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertTrue(children.isEmpty());
		}

		// Error expected with the third party entity;
		charter.setEntity(thirdPartyEntity);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VesselCharterConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(charter);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	/**
	 * Test charter in market needs a real entity
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselCharterInMarketNeedRealEntity() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		final LegalEntity realEntity = commercialModelBuilder.createEntity("real-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		realEntity.setThirdParty(false);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("Market1", vessel, realEntity, "80000", 1);

		// No error expected when using a real entity
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, CharterInMarketConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertTrue(children.isEmpty());

		}

		// Error expected with the third party entity;
		market.setEntity(thirdPartyEntity);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, CharterInMarketConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(market);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	/**
	 * Test purchase contract with 3rd-party entity is a DES purchase contract
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractIsDES() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("contract", thirdPartyEntity, "5");

		// No error expected when with DES Purchase
		contract.setContractType(ContractType.DES);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartyContractEntityConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertTrue(children.isEmpty());

		}

		// Error expected with FOB Purchase
		contract.setContractType(ContractType.FOB);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartyContractEntityConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(contract);
			}

			Assertions.assertTrue(foundL1);
		}

		// Error expected with either
		contract.setContractType(ContractType.BOTH);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartyContractEntityConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(contract);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	/**
	 * Test sales contract with 3rd-party entity is a FOB Sale contract
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSalesContractIsFOB() {

		final LegalEntity thirdPartyEntity = commercialModelBuilder.createEntity("third-party-1", LocalDate.of(2000, Month.JANUARY, 1), 0);
		thirdPartyEntity.setThirdParty(true);

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("contract", thirdPartyEntity, "5");

		// No error expected when with FOB Sale
		contract.setContractType(ContractType.FOB);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartyContractEntityConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertTrue(children.isEmpty());

		}

		// Error expected with DES Sale
		contract.setContractType(ContractType.DES);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartyContractEntityConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(contract);
			}

			Assertions.assertTrue(foundL1);
		}

		// Error expected with either
		contract.setContractType(ContractType.BOTH);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, ThirdPartyContractEntityConstraint.KEY_THIRD_PARTY_ENTITY);
			Assertions.assertFalse(children.isEmpty());

			// make sure both slots are detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(contract);
			}

			Assertions.assertTrue(foundL1);
		}
	}
}