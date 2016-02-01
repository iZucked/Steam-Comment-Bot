/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.validation.VesselClassMinHeelConstraint;

public class VesselClassMinHeelConstraintTest {

	@Test
	public void testMinHeel_Ok() {

		final VesselClass target = FleetFactory.eINSTANCE.createVesselClass();

		target.setMinHeel(500);
		target.setCapacity(1000);
		target.setFillCapacity(1.0);

		checkConstraint(target, true);
	}

	@Test
	public void testMinHeel_CapacityTooLow() {

		final VesselClass target = FleetFactory.eINSTANCE.createVesselClass();

		target.setMinHeel(500);
		target.setCapacity(100);
		target.setFillCapacity(1.0);

		checkConstraint(target, false);
	}

	@Test
	public void testMinHeel_FillPercentTooLow() {

		final VesselClass target = FleetFactory.eINSTANCE.createVesselClass();

		target.setMinHeel(500);
		target.setCapacity(1000);
		target.setFillCapacity(0.1);

		checkConstraint(target, false);
	}

	private void checkConstraint(final EObject target, boolean expectSuccess) {
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final VesselClassMinHeelConstraint constraint = new VesselClassMinHeelConstraint();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assert.assertTrue("Sucess expected", status.isOK());
		} else {
			Assert.assertFalse("Failure expected", status.isOK());

		}
	}

}
