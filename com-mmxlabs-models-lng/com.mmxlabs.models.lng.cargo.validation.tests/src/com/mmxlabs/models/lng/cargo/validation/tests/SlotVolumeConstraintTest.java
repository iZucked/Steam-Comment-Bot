/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Test;
import org.mockito.Matchers;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.SlotVolumeConstraint;

/**
 * Details from case 171:
 * 
 * SlotVolumeConstraint is supposed to check that a slot's min and max volume limits are set sensibly, i.e. that the minimum volume is not greater than the maximum volume.
 * 
 * Although slot volumes limits can be queried by the Slot.getMinQuantity() and Slot.getMaxQuantity() methods, these values are "unsettable", and have corresponding Slot.isSetMinQuantity() and
 * isSetMaxQuantity() methods. If the values aren't set, then the min and max values are taken from the slot's contract instead, and so on
 * 
 * However, all the code uses the convenience method Slot.getOrContractMinQuantity(Scenario) and its Max counterpart, so you can Mock a slot which has these.
 * 
 * A little fiddle here is that the constraint needs to figure out the Scenario which contains a slot, your mock slot will need to have Slot.eContainer() and Slot.eContainingFeature() methods.
 * eContainer() should return a mocked Scenario and eContainingFeature() can probably return null. Something similar will be required in any other constraints that need to find out which Scenario the
 * object they are validating belongs to, as the IValidationContext only gives the Slot or whatever.
 * 
 */
public class SlotVolumeConstraintTest {
	/**
	 * Check that the constraint succeeds if everything is correct.
	 */
	@Test
	public void testSlotVolumeConstraintCorrectMinMax() {

		// The max and min to test. They are correct, i.e. non-negative and the
		// max is greater than the min.
		final int max = 2;
		final int min = 1;

		testSlotVolumeConstraint(true, min, max);
	}

	/**
	 * Test that an equal min and max still work.
	 */
	@Test
	public void testSlotVolumeConstraintEqualMinMax() {
		final int max = 2;
		final int min = 2;

		testSlotVolumeConstraint(true, max, min);
	}

	/**
	 * Test a max smaller than a min produces a failure.
	 */
	@Test
	public void testSlotVolumeConstraintMaxLessThanMin() {

		final int min = 2;
		final int max = 1;

		testSlotVolumeConstraint(false, min, max);
	}

	/**
	 * Test a negative minimum fails
	 */
	@Test
	public void testSlotVolumeConstraintNegativeMinQuantity() {

		final int min = -1;
		// The max doesn't matter and won't get checked, but need a value
		// anyway.
		final int max = 2;

		testSlotVolumeConstraint(false, min, max);
	}

	/**
	 * Test a negative maximum fails.
	 */
	@Test
	public void testSlotVolumeConstraintNegativeMaxQuantity() {

		final int min = 1;
		final int max = -1;

		testSlotVolumeConstraint(false, min, max);
	}

	private void testSlotVolumeConstraint(final boolean expectSuccess, final int min, final int max) {

		// This is the constraint we will be testing
		final SlotVolumeConstraint constraint = new SlotVolumeConstraint();

		final Slot slot = mock(Slot.class);
		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);

		// Set up the expected return values of methods.
		when(slot.getName()).thenReturn("Slot");
		when(slot.eContainingFeature()).thenReturn(null);
		when(slot.getSlotOrContractMinQuantity()).thenReturn(min);
		when(slot.getSlotOrContractMaxQuantity()).thenReturn(max);
		when(slot.isSetVolumeLimitsUnit()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn(slot);
		when(validationContext.getEventType()).thenReturn(EMFEventType.NULL);

		if (expectSuccess) {
			when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		} else {
			when(validationContext.createFailureStatus(Matchers.anyVararg())).thenReturn(successStatus);
		}

		// validate the constraint using the mocked expected values set above
		constraint.validate(validationContext);

		// verify that the mocked methods are called.
		verify(slot, atLeast(0)).getName();
		verify(slot, atLeast(0)).getSlotOrContractMinQuantity();
		verify(slot, atLeast(0)).getSlotOrContractMaxQuantity();
		verify(slot, atLeast(0)).isSetVolumeLimitsUnit();
		verify(validationContext).getTarget();
		verify(validationContext).getEventType();
		verify(validationContext, atLeast(0)).createSuccessStatus();
		verify(validationContext, atLeast(0)).createFailureStatus(Matchers.anyVararg());
		// verify that only the methods above are called.
		verifyNoMoreInteractions(slot);
		verifyNoMoreInteractions(validationContext);
	}
}
