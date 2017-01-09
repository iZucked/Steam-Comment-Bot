/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.validation.VesselMinHeelConstraint;

public class VesselMinHeelConstraintTest {

	@Test
	public void testNoOverride_OK() {

		final Vessel target = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		target.setVesselClass(vesselClass);

		vesselClass.setMinHeel(500);
		vesselClass.setCapacity(1000);
		vesselClass.setFillCapacity(1.0);

		checkConstraint(target, true);
	}

	@Test
	public void testNOOverride_VC_BAD_() {

		final Vessel target = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		target.setVesselClass(vesselClass);

		vesselClass.setMinHeel(500);
		vesselClass.setCapacity(100);
		vesselClass.setFillCapacity(1.0);

		checkConstraint(target, true);
	}

	@Test
	public void testMinHeel_CapacityTooLow() {

		final Vessel target = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		target.setVesselClass(vesselClass);

		vesselClass.setMinHeel(500);
		vesselClass.setCapacity(1000);
		vesselClass.setFillCapacity(1.0);

		target.setCapacity(100);

		checkConstraint(target, false);
	}

	@Test
	public void testMinHeel_FillPercentTooLow() {

		final Vessel target = FleetFactory.eINSTANCE.createVessel();
		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();
		target.setVesselClass(vesselClass);

		vesselClass.setMinHeel(500);
		vesselClass.setCapacity(1000);
		vesselClass.setFillCapacity(1.0);

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

		final VesselMinHeelConstraint constraint = new VesselMinHeelConstraint();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assert.assertTrue("Sucess expected", status.isOK());
		} else {
			Assert.assertFalse("Failure expected", status.isOK());

		}
	}

}
