/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DESSalesMarketAllocationRowConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof DESSalesMarketAllocationRow) {
			final DESSalesMarketAllocationRow desSalesMarketAllocationRow = (DESSalesMarketAllocationRow) target;
			
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "MULL Generation") //
					.withTag(ValidationConstants.TAG_ADP);
			
			if (desSalesMarketAllocationRow.getDesSalesMarket() == null) {
				factory.copyName() //
				.withObjectAndFeature(desSalesMarketAllocationRow, ADPPackage.Literals.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET) //
				.withMessage("No DES sales Market selected") //
				.make(ctx, statuses);
			}
		}
	}
}