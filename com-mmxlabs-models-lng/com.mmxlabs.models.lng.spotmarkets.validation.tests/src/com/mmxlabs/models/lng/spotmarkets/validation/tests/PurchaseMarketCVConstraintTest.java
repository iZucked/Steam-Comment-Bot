/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation.tests;

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

import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.validation.PurchaseMarketCVConstraint;

public class PurchaseMarketCVConstraintTest {

	@Test
	public void testDESPurchaseMarket_OKCV() {

		final DESPurchaseMarket target = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
		target.setCv(20.0);

		checkConstraint(target, true);
	}

	@Test
	public void testDESPurchaseMarket_CV_TooLow() {

		final DESPurchaseMarket target = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
		target.setCv(0.01);

		checkConstraint(target, false);
	}

	@Test
	public void testDESPurchaseMarket_CV_TooHigh() {

		final DESPurchaseMarket target = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
		target.setCv(41);

		checkConstraint(target, false);
	}

	@Test
	public void testFOBPurchaseMarket_OKCV() {

		final FOBPurchasesMarket target = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
		target.setCv(20.0);

		checkConstraint(target, true);
	}

	@Test
	public void testFOBPurchaseMarket_CV_TooLow() {

		final FOBPurchasesMarket target = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
		target.setCv(0.01);

		checkConstraint(target, false);
	}

	@Test
	public void testFOBPurchaseMarket_CV_TooHigh() {

		final FOBPurchasesMarket target = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
		target.setCv(41);

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

		final PurchaseMarketCVConstraint constraint = new PurchaseMarketCVConstraint();
		final IStatus status = constraint.validate(ctx);

		if (expectSuccess) {
			Assert.assertTrue("Sucess expected", status.isOK());
		} else {
			Assert.assertFalse("Failure expected", status.isOK());

		}
	}
}
