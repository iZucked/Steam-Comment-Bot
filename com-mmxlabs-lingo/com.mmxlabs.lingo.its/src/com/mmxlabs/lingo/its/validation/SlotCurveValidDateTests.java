/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.validation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.SlotCurveDataExistsConstraint;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Test cases to check that curve data exists for a given slot. This can be the slot expression, expressions in the contract or spot market pricing and cooldown curves, tax rates.
 * 
 * @author Simon Goodall
 *
 */
@ExtendWith(ShiroRunner.class)
public class SlotCurveValidDateTests extends AbstractMicroTestCase {

	enum Mode {
		Expression, Contract, Market, CooldownVol, CooldownLump, Tax
	}

	record TestData(Mode mode, YearMonth indexDate, LocalDate windowStart, @Nullable LocalDate pricingEvent, boolean valid) {
		// Used for the test label
		public String toString() {
			if (pricingEvent != null) {
				return String.format("%s: Idx %s, Window: %s PriceDate: %s valid = %s", mode, indexDate, windowStart, pricingEvent, valid);
			} else {
				return String.format("%s: Idx %s, Window: %s valid = %s", mode, indexDate, windowStart,  valid);
			}
		}
	};

	// CurveDataExistsConstraint
	// MissingCurveValueConstraint -- for schedule charters (check for no start date, check null date in index
	// See class for VesselCharterConstraint

	/**
	 * Generate testing data. Each test will create a load slot with the given window start and optional pricing event date. A single pruce cruve will be created with the given date and it will be
	 * used for the expression depending on the mode. For Tax mode, the curve is ignored and the index date is used as the tax date.
	 * 
	 * @return
	 */
	@SuppressWarnings("null")
	public static Iterable<TestData> generateTests() {
		return Lists.newArrayList( //
				new TestData(Mode.Expression, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), null, true), // Valid - window start >= index start
				new TestData(Mode.Expression, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), null, false), // Invalid - window start < index start
				new TestData(Mode.Expression, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), true), // Valid - pricing event >= index start
				new TestData(Mode.Expression, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), LocalDate.of(2023, 1, 1), false), // Invalid - pricing event < index start
				//
				new TestData(Mode.Contract, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), null, true), // Valid - window start >= index start
				new TestData(Mode.Contract, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), null, false), // Invalid - window start < index start
				new TestData(Mode.Contract, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), true), // Valid - pricing event >= index start
				new TestData(Mode.Contract, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), LocalDate.of(2023, 1, 1), false), // Invalid - pricing event < index start
				//
				new TestData(Mode.Market, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), null, true), // Valid - window start >= index start
				new TestData(Mode.Market, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), null, false), // Invalid - window start < index start
				// Cooldown - ignore pricing event
				new TestData(Mode.CooldownLump, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), null, true), // Valid - window start >= index start
				new TestData(Mode.CooldownLump, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), null, false), // Invalid - window start < index start
				new TestData(Mode.CooldownLump, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), LocalDate.of(2023, 1, 1), true), // Valid - window event >= index start
				new TestData(Mode.CooldownLump, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), false), // Invalid - window event < index start
				// Cooldown - ignore pricing event
				new TestData(Mode.CooldownVol, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), null, true), // Valid - window start >= index start
				new TestData(Mode.CooldownVol, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), null, false), // Invalid - window start < index start
				new TestData(Mode.CooldownVol, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), LocalDate.of(2023, 1, 1), true), // Valid - window event >= index start
				new TestData(Mode.CooldownVol, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), false), // Invalid - window event < index start
				// Tax - ignore pricing event
				new TestData(Mode.Tax, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), null, true), // Valid - window start >= index start
				new TestData(Mode.Tax, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), null, false), // Invalid - window start < index start
				new TestData(Mode.Tax, YearMonth.of(2023, 2), LocalDate.of(2023, 2, 1), LocalDate.of(2023, 1, 1), true), // Valid - window event >= index start
				new TestData(Mode.Tax, YearMonth.of(2023, 2), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), false) // Invalid - window event < index start
		//
		);

	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	@Tag(TestCategories.MICRO_TEST)
	public void run(final TestData data) {

		pricingModelBuilder.makeCommodityDataCurve("testcurve", "$", "mmBtu") //
				.addIndexPoint(data.indexDate, 1) //
				.build();

		final String slotExpresion = data.mode == Mode.Expression ? "testcurve" : "1";
		LoadSlot slot = null;

		if (data.mode == Mode.Expression) {
			slot = cargoModelBuilder.makeFOBPurchase("L1", data.windowStart, portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, slotExpresion, null) //
					.build();
		} else if (data.mode == Mode.Contract) {
			final var contract = commercialModelBuilder.makeExpressionPurchaseContract("contract", entity, "testcurve");
			slot = cargoModelBuilder.makeFOBPurchase("L1", data.windowStart, portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, null, null, null) //
					.build();

		} else if (data.mode == Mode.Market) {
			FOBPurchasesMarket mkt = spotMarketsModelBuilder.makeFOBPurchaseMarket("market", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "testcurve", null).build();
			slot = cargoModelBuilder.createSpotFOBPurchase("L1", mkt, YearMonth.from(data.windowStart), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN))//
			;
		} else if (data.mode == Mode.CooldownVol) {
			slot = cargoModelBuilder.makeFOBPurchase("L1", data.windowStart, portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "1", null) //
					.build();

			costModelBuilder.createCooldownPrice("testcurve", null, Collections.singleton(slot.getPort()));
		} else if (data.mode == Mode.CooldownLump) {
			slot = cargoModelBuilder.makeFOBPurchase("L1", data.windowStart, portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "1", null) //
					.build();

			costModelBuilder.createCooldownPrice("testcurve", null, Collections.singleton(slot.getPort()));
		} else if (data.mode == Mode.Tax) {
			slot = cargoModelBuilder.makeFOBPurchase("L1", data.windowStart, portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "1", null) //
					.build();
			entity.getShippingBook().getTaxRates().clear();
			entity.getTradingBook().getTaxRates().clear();
			entity.getUpstreamBook().getTaxRates().clear();

			commercialModelBuilder.setTaxRates(entity, data.indexDate.atDay(1), 0.0f);
		}
		assert slot != null;
		if (data.pricingEvent != null) {
			assert data.mode != Mode.Market;
			slot.setPricingDate(data.pricingEvent);
		}

		final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(statusOrig);

		// Clear out other validation errors, e.g. validation constraints may fail and
		// get disabled, so do not abort this test.
		final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

		// Depending on the mode we will expect a different part of the constraint to fail.
		final Object key = switch (data.mode) {
		case Expression -> SlotCurveDataExistsConstraint.KEY_EXPRESSION_FAILURE;
		case Contract -> SlotCurveDataExistsConstraint.KEY_CONTRACT_FAILURE;
		case Market -> SlotCurveDataExistsConstraint.KEY_MARKET_FAILURE;
		case CooldownVol, CooldownLump -> SlotCurveDataExistsConstraint.KEY_COOLDOWN_FAILURE;
		case Tax -> SlotCurveDataExistsConstraint.KEY_TAX_FAILURE;
		};

		final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, key);
		if (data.valid) {
			Assertions.assertTrue(children.isEmpty());
		} else {
			Assertions.assertFalse(children.isEmpty());
			// Should we assert further on returned state?
		}
	}
}
