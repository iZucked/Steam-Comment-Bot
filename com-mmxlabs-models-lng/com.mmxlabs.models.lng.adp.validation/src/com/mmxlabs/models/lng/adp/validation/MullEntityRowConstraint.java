package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MullEntityRowConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof MullEntityRow) {
			final MullEntityRow mullEntityRow = (MullEntityRow) target;
			
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "MULL Generation") //
					.withTag(ValidationConstants.TAG_ADP);
			
			if (mullEntityRow.getEntity() == null) {
				factory.copyName() //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__ENTITY) //
				.withMessage("No entity selected") //
				.make(ctx, statuses);
			}
			
			if (mullEntityRow.getInitialAllocation() == null) {
				factory.copyName() //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__INITIAL_ALLOCATION) //
				.withMessage("A value must be provided for the initial allocation") //
				.make(ctx, statuses);
			} else {
				if (!mullEntityRow.getInitialAllocation().matches("-?\\d+")) {
					factory.copyName() //
					.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__INITIAL_ALLOCATION) //
					.withMessage("The initial allocation must be a whole number") //
					.make(ctx, statuses);
				}
			}
			
			if (mullEntityRow.getRelativeEntitlement() <= 0) {
				factory.copyName() //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT) //
				.withMessage("Reference entitlement must be greater than zero") //
				.make(ctx, statuses);
			}
		}
	}
}