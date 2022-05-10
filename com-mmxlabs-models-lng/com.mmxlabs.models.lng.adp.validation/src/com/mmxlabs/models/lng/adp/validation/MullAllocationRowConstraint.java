/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.MullAllocationRow;
import com.mmxlabs.models.lng.adp.presentation.customisation.IInventoryBasedGenerationPresentationCustomiser;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.rcp.common.ServiceHelper;

public class MullAllocationRowConstraint extends AbstractModelMultiConstraint {
	private final String typedName;
	
	public MullAllocationRowConstraint() {
		final IInventoryBasedGenerationPresentationCustomiser[] customiserArr = new IInventoryBasedGenerationPresentationCustomiser[1];
		ServiceHelper.withOptionalServiceConsumer(IInventoryBasedGenerationPresentationCustomiser.class, v -> customiserArr[0] = v);
		typedName = customiserArr[0] != null ? customiserArr[0].getDropDownMenuLabel() : "Inventory-based generation";
	}
	
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof MullAllocationRow) {
			final MullAllocationRow mullAllocationRow = (MullAllocationRow) target;
			
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", typedName) //
					.withTag(ValidationConstants.TAG_ADP);
			
			if (mullAllocationRow.getVessels().isEmpty()) {
				factory.copyName() //
				.withObjectAndFeature(mullAllocationRow, ADPPackage.Literals.MULL_ALLOCATION_ROW__VESSELS) //
				.withMessage("No vessels selected") //
				.make(ctx, statuses);
			}
		}
	}
}
