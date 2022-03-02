/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.CargoSlotOrderConstraint;

public class CargoSlotOrderConstraintTest {

	@Test
	public void testCorrectSlotOrder_LD() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final LoadSlot loadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();

		final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();

		setDate(loadSlot1, 2013, 01, 01);
		setDate(dischargeSlot1, 2013, 03, 01);

		cargo.getSlots().add(loadSlot1);
		cargo.getSlots().add(dischargeSlot1);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(cargo);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final CargoSlotOrderConstraint constraint = new CargoSlotOrderConstraint();
		final IStatus status = constraint.validate(ctx);

		Assertions.assertSame(successStatus, status);
	}

	@Test
	public void testWrongSlotOrder_LD() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final LoadSlot loadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();

		final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();

		setDate(loadSlot1, 2013, 03, 01);
		setDate(dischargeSlot1, 2013, 01, 01);

		cargo.getSlots().add(loadSlot1);
		cargo.getSlots().add(dischargeSlot1);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(cargo);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final CargoSlotOrderConstraint constraint = new CargoSlotOrderConstraint();
		final IStatus status = constraint.validate(ctx);

		Assertions.assertSame(successStatus, status);
		Assertions.assertTrue(status.isOK(), "Expect success status");
	}

	@Test
	public void testCorrectSlotOrder_LDD() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final LoadSlot loadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();
		final LoadSlot loadSlot2 = CargoFactory.eINSTANCE.createLoadSlot();

		final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
		final DischargeSlot dischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();

		setDate(loadSlot1, 2013, 01, 01);
		setDate(loadSlot2, 2013, 02, 01);
		setDate(dischargeSlot1, 2013, 03, 01);
		setDate(dischargeSlot2, 2013, 04, 01);

		cargo.getSlots().add(loadSlot1);
		// cargo.getSlots().add(loadSlot2);
		cargo.getSlots().add(dischargeSlot1);
		cargo.getSlots().add(dischargeSlot2);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(cargo);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final CargoSlotOrderConstraint constraint = new CargoSlotOrderConstraint();
		final IStatus status = constraint.validate(ctx);

		Assertions.assertSame(successStatus, status);
	}

	@Test
	public void testWrongSlotOrder_LDD() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final LoadSlot loadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();

		final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
		final DischargeSlot dischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();

		setDate(loadSlot1, 2013, 03, 01);
		setDate(dischargeSlot1, 2013, 02, 01);
		setDate(dischargeSlot2, 2013, 04, 01);

		cargo.getSlots().add(loadSlot1);
		cargo.getSlots().add(dischargeSlot1);
		cargo.getSlots().add(dischargeSlot2);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(cargo);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final CargoSlotOrderConstraint constraint = new CargoSlotOrderConstraint();
		final IStatus status = constraint.validate(ctx);

		Assertions.assertSame(successStatus, status);
		Assertions.assertTrue(status.isOK(), "Expect success status");
	}

	@Test
	public void testBadSlotCombination_LLD() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final LoadSlot loadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();
		final LoadSlot loadSlot2 = CargoFactory.eINSTANCE.createLoadSlot();

		final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();

		setDate(loadSlot1, 2013, 01, 01);
		setDate(loadSlot2, 2013, 02, 01);
		setDate(dischargeSlot1, 2013, 03, 01);

		cargo.getSlots().add(loadSlot1);
		cargo.getSlots().add(loadSlot2);
		cargo.getSlots().add(dischargeSlot1);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(cargo);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final CargoSlotOrderConstraint constraint = new CargoSlotOrderConstraint();
		final IStatus status = constraint.validate(ctx);

		Assertions.assertNotSame(successStatus, status);
		Assertions.assertFalse(status.isOK(), "Expect failure status");
	}

	@Test
	public void testBadSlotCombination_LLDD() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

		final LoadSlot loadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();
		final LoadSlot loadSlot2 = CargoFactory.eINSTANCE.createLoadSlot();

		final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
		final DischargeSlot dischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();

		setDate(loadSlot1, 2013, 01, 01);
		setDate(loadSlot2, 2013, 02, 01);
		setDate(dischargeSlot1, 2013, 03, 01);
		setDate(dischargeSlot2, 2013, 04, 01);

		cargo.getSlots().add(loadSlot1);
		cargo.getSlots().add(loadSlot2);
		cargo.getSlots().add(dischargeSlot1);
		cargo.getSlots().add(dischargeSlot2);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(cargo);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final CargoSlotOrderConstraint constraint = new CargoSlotOrderConstraint();
		final IStatus status = constraint.validate(ctx);

		Assertions.assertNotSame(successStatus, status);
		Assertions.assertFalse(status.isOK(), "Expect failure status");
	}

	private void setDate(final Slot slot, final int year, final int month, final int day) {
		slot.setWindowStart(LocalDate.of(year, month, day));
	}
}
