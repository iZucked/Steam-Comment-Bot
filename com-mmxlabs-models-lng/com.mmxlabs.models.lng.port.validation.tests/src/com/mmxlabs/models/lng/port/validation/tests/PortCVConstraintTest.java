/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.validation.PortCVConstraint;
import com.mmxlabs.models.lng.types.PortCapability;

public class PortCVConstraintTest {

	@Test
	public void testLoadPort_OKCV() {

		final Port port = PortFactory.eINSTANCE.createPort();
		port.getCapabilities().add(PortCapability.LOAD);
		port.setCvValue(20.0);

		checkConstraint(port, true);
	}

	@Test
	@Disabled("Zero CV is now permitted")
	public void testLoadPort_CV_TooLow() {

		final Port port = PortFactory.eINSTANCE.createPort();
		port.getCapabilities().add(PortCapability.LOAD);
		port.setCvValue(0.1);

		checkConstraint(port, false);
	}

	@Test
	public void testLoadPort_CV_TooHigh() {

		final Port port = PortFactory.eINSTANCE.createPort();
		port.getCapabilities().add(PortCapability.LOAD);
		port.setCvValue(41.0);

		checkConstraint(port, false);
	}

	@Test
	public void testNonLoadPort_OKCV() {

		final Port port = PortFactory.eINSTANCE.createPort();
		port.setCvValue(50.0);

		checkConstraint(port, true);
	}

	private void checkConstraint(final EObject target, boolean expectSuccess) {
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final PortCVConstraint constraint = new PortCVConstraint();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assertions.assertTrue(status.isOK(), "Success expected");
		} else {
			Assertions.assertFalse(status.isOK(), "Failure expected");

		}
	}
}
