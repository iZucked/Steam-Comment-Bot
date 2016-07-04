/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation.tests;

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

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.SlotPricingConstraint;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.PurchaseContract;

public class SlotPricingConstraintTest {

	@Test
	public void testSlot_NoContract_NoPriceExpression() {

		LoadSlot target = CargoFactory.eINSTANCE.createLoadSlot();

		checkConstraint(target, false);
	}

	@Test
	public void testSlot_NoContract_PriceExpression() {

		LoadSlot target = CargoFactory.eINSTANCE.createLoadSlot();
		// Doesn't need to be valid
		target.setPriceExpression("");

		checkConstraint(target, true);
	}

	@Test
	public void testSlot_Contract_PriceExpression() {

		LoadSlot target = CargoFactory.eINSTANCE.createLoadSlot();
		// Doesn't need to be valid
		target.setPriceExpression("");

		PurchaseContract purchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		target.setContract(purchaseContract);

		checkConstraint(target, true);
	}

	@Test
	public void testSlot_Contract_NoPriceExpression() {

		LoadSlot target = CargoFactory.eINSTANCE.createLoadSlot();

		PurchaseContract purchaseContract = CommercialFactory.eINSTANCE.createPurchaseContract();
		target.setContract(purchaseContract);

		checkConstraint(target, true);
	}

	private void checkConstraint(final EObject target, final boolean expectSuccess) {
		final IConstraintStatus successStatus = mock(IConstraintStatus.class);
		when(successStatus.isOK()).thenReturn(Boolean.TRUE);

		final IConstraintStatus failureStatus = mock(IConstraintStatus.class);
		when(failureStatus.getSeverity()).thenReturn(IStatus.ERROR);

		final IValidationContext ctx = Mockito.mock(IValidationContext.class);
		Mockito.when(ctx.getTarget()).thenReturn(target);
		Mockito.when(ctx.createSuccessStatus()).thenReturn(successStatus);
		Mockito.when(ctx.createFailureStatus(Matchers.anyString())).thenReturn(failureStatus);

		final SlotPricingConstraint constraint = new SlotPricingConstraint();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assert.assertTrue("Sucess expected", status.isOK());
		} else {
			Assert.assertFalse("Failure expected", status.isOK());

		}
	}

}
