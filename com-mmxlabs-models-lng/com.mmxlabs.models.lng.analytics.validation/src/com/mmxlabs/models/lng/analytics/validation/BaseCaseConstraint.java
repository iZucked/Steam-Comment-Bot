package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseCaseConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof BaseCase) {
			final BaseCase baseCase = (BaseCase) target;

//			ShippingType nonShipped = AnalyticsBuilder.isNonShipped(baseCaseRow);
//			if (nonShipped == ShippingType.Shipped) {
//				if (baseCaseRow.getShipping() == null) {
//					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case - no shipping option defined."));
//					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
//					statuses.add(deco);
//				}
//				if (!(baseCaseRow.getShipping() instanceof FleetShippingOption || baseCaseRow.getShipping() instanceof RoundTripShippingOption)) {
//					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case - incompatible shipping option defined."));
//					deco.addEObjectAndFeature(baseCaseRow, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);
//					statuses.add(deco);
//				}
//			}
		}

		return Activator.PLUGIN_ID;
	}

}
