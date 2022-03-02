/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.CargoRestrictedSlotConstraint;

public class RestrictedSlotsConstraintTest {

	@Test
	public void testRestrictedSlotsContraint_Pass() {

		// This is the constraint we will be testing
		final CargoRestrictedSlotConstraint constraint = new CargoRestrictedSlotConstraint();

		final LoadSlot loadSlot = mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = mock(DischargeSlot.class);
		final EList<Slot<?>> restrictedSlots = new BasicEList<>();
		restrictedSlots.add(dischargeSlot);
		when(loadSlot.getRestrictedSlots()).thenReturn(restrictedSlots);
		
		final Cargo cargo = mock(Cargo.class);
		when(loadSlot.getCargo()).thenReturn(cargo);
		when(dischargeSlot.getCargo()).thenReturn(cargo);

		final EList<Slot<?>> slots = new BasicEList<>();
		slots.add(loadSlot);
		slots.add(dischargeSlot);
		when(cargo.getSlots()).thenReturn(slots);

		final IValidationContext validationContext = mock(IValidationContext.class);
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(validationContext.createSuccessStatus()).thenReturn(successStatus);
		when(validationContext.createFailureStatus()).thenReturn(failureStatus);
		when(validationContext.createFailureStatus(ArgumentMatchers.any())).thenReturn(failureStatus);
		when(successStatus.isOK()).thenReturn(true);
		when(failureStatus.isOK()).thenReturn(false);
		when(validationContext.getTarget()).thenReturn((Slot)loadSlot);
		when(successStatus.getSeverity()).thenReturn(IStatus.OK);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		// Set up the expected return values of methods.
		when(loadSlot.isRestrictedSlotsArePermissive()).thenReturn(true);

		// validate the constraint using the mocked expected values set above
		final IStatus status1 = constraint.validate(validationContext);
		Assertions.assertTrue(status1.isOK());
		
		// Set up the expected return values of methods.
		when(loadSlot.isRestrictedSlotsArePermissive()).thenReturn(false);

		// validate the constraint using the mocked expected values set above
		final IStatus status2 = constraint.validate(validationContext);
		Assertions.assertFalse(status2.isOK());
	}

}
