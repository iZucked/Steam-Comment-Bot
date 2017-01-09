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

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.LoadSlotCVConstraint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class LoadSlotCVConstraintTest {

	@Test
	public void testCV_OK() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setCargoCV(20.0);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final LoadSlotCVConstraint constraint = new LoadSlotCVConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertTrue(status.isOK());
	}

	@Test
	public void testCV_TooLow() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setCargoCV(0.01);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final LoadSlotCVConstraint constraint = new LoadSlotCVConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertFalse(status.isOK());
	}

	@Test
	public void testCV_TooHigh() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setCargoCV(41.0);

		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final LoadSlotCVConstraint constraint = new LoadSlotCVConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertFalse(status.isOK());
	}

	@Test
	public void testUnsetCV_OK() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.unsetCargoCV();

		// Slot falls back to port
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setCvValue(20.0);
		slot.setPort(port);
		
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(slot);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final LoadSlotCVConstraint constraint = new LoadSlotCVConstraint();
		final IStatus status = constraint.validate(ctx);

		Assert.assertTrue(status.isOK());
	}

}
