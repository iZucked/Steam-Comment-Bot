/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.SlotNameUniquenessConstraint;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

@ExtendWith(ShiroRunner.class)
public class SlotNameUniquenessTests extends AbstractMicroTestCase {

	@BeforeEach
	public void setPromptDate() {
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 1).plusMonths(3));
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testValid() {

		cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2021, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build();
		cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2021, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build();

		final IStatus status = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(status);

		Assertions.assertTrue(status.isOK());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDuplicateFOBPurchase() {

		final LoadSlot l1 = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2021, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build();
		final LoadSlot l2 = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2021, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build();

		final IStatus status = ValidationTestUtil.validate(scenarioDataProvider, false, false);
		Assertions.assertNotNull(status);

		Assertions.assertFalse(status.isOK());

		final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, SlotNameUniquenessConstraint.KEY_SLOT_NAME_UNIQUENESS);
		Assertions.assertFalse(children.isEmpty());

		// make sure both slots are detected.
		boolean foundL1 = false;
		boolean foundL2 = false;
		for (final var dscd : children) {
			foundL1 |= dscd.getObjects().contains(l1);
			foundL2 |= dscd.getObjects().contains(l2);
		}

		Assertions.assertTrue(foundL1);
		Assertions.assertTrue(foundL2);
	}
}
