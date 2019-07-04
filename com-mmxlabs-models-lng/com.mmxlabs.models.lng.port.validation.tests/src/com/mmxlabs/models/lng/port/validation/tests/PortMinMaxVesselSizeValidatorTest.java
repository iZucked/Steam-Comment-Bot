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
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.validation.PortMinMaxVesselSizeValidator;

public class PortMinMaxVesselSizeValidatorTest {

	@Test
	public void testLoadPort_OK_UnsetMinMaxVesselSizes() {
		final Port port = PortFactory.eINSTANCE.createPort();
		checkValidator(port, true);
	}

	@Test
	public void testLoadPort_OK_MinVesselSizeSet() {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setMinVesselSize(10000);
		checkValidator(port, true);
	}

	@Test
	public void testLoadPort_OK_MaxVesselSizeSet() {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setMaxVesselSize(20000);
		checkValidator(port, true);
	}
	
	@Test
	public void testLoadPort_OK_MinMaxVesselSizeSet() {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setMinVesselSize(10000);
		port.setMaxVesselSize(20000);
		checkValidator(port, true);
	}	
	
	@Test
	public void testLoadPort_CV_NegativeMinVesselSize() {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setMinVesselSize(-10000);
		checkValidator(port, false);
	}

	@Test
	public void testLoadPort_CV_NegativeMaxVesselSize() {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setMaxVesselSize(-20000);
		checkValidator(port, false);
	}

	@Test
	public void testNonLoadPort_MaxLessThanMinVesselSize() {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setMinVesselSize(20000);
		port.setMaxVesselSize(10000);
		checkValidator(port, false);
	}

	private void checkValidator(final EObject target, boolean expectSuccess) {
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(ArgumentMatchers.anyString())).thenReturn(failureStatus);

		final PortMinMaxVesselSizeValidator constraint = new PortMinMaxVesselSizeValidator();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assertions.assertTrue(status.isOK(), "Success expected");
		} else {
			Assertions.assertFalse(status.isOK(), "Failure expected");
		}
	}
}
