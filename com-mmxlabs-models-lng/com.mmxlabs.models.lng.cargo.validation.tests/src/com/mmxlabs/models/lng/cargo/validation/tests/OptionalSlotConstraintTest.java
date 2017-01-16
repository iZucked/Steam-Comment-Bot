/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.OptionalSlotConstraint;

public class OptionalSlotConstraintTest {

	@Test
	public void testOptionalLoadSlot_NoCargo() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();

		slot.setOptional(true);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final OptionalSlotConstraint constraint = new OptionalSlotConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertTrue(status.isOK());
	}

	@Test
	public void testNonOptionalLoadSlot_NoCargo() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();

		slot.setOptional(false);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final OptionalSlotConstraint constraint = new OptionalSlotConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertFalse(status.isOK());
	}

	@Test
	public void testNonOptionalLoadSlot_InCargo() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();

		slot.setOptional(false);

		cargo.getSlots().add(slot);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final OptionalSlotConstraint constraint = new OptionalSlotConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertTrue(status.isOK());
	}

	@Test
	public void testOptionalDischargeSlot_NoCargo() {

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();

		slot.setOptional(true);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final OptionalSlotConstraint constraint = new OptionalSlotConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertTrue(status.isOK());
	}

	@Test
	public void testNonOptionalDischargeSlot_NoCargo() {

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();

		slot.setOptional(false);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final OptionalSlotConstraint constraint = new OptionalSlotConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertFalse(status.isOK());
	}

	@Test
	public void testNonOptionalDischargeSlot_InCargo() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();

		slot.setOptional(false);

		cargo.getSlots().add(slot);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final OptionalSlotConstraint constraint = new OptionalSlotConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertTrue(status.isOK());
	}

}
