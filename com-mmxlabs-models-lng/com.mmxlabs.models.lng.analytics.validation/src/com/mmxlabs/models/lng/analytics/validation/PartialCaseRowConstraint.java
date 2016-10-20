package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder.ShippingType;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PartialCaseRowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PartialCaseRow) {
			final PartialCaseRow partialCaseRow = (PartialCaseRow) target;

			ShippingType nonShipped = AnalyticsBuilder.isNonShipped(partialCaseRow);
			if (nonShipped == ShippingType.Shipped) {
				if (partialCaseRow.getShipping() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Partial case - no shipping option defined."));
					deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
				for (ShippingOption opt : partialCaseRow.getShipping()) {
					if (!(opt instanceof FleetShippingOption || opt instanceof RoundTripShippingOption)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Partial case - incompatible shipping option defined."));
						deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
						statuses.add(deco);
					}
				}
			}

			for (ShippingOption opt : partialCaseRow.getShipping()) {
				if (opt.eContainer() == null) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Partial case - uncontained shipping"));
					deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING);
					statuses.add(deco);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

}
