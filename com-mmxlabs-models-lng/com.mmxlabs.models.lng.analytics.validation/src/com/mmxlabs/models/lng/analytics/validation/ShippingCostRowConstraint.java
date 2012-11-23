package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ShippingCostRowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();

		if (target instanceof ShippingCostRow) {
			final ShippingCostRow shippingCostRow = (ShippingCostRow) target;
			{
				final EObject plan = shippingCostRow.eContainer();
				final EObject original = extraValidationContext.getOriginal(plan);
				if (plan != null && original != plan) {
					return Activator.PLUGIN_ID;
				}
			}
			if (shippingCostRow.getDate() == null) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No date specified"));
				deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_Date());
				statuses.add(deco);
			}
			if (shippingCostRow.getPort() == null) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No port specified"));
				deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_Port());
				statuses.add(deco);
			}

			final DestinationType destinationType = shippingCostRow.getDestinationType();
			if (destinationType == null) {
				final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No type specified"));
				deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
				statuses.add(deco);
			} else {
				if (destinationType == DestinationType.START || destinationType == DestinationType.DISCHARGE || destinationType == DestinationType.OTHER) {
					if (shippingCostRow.getCargoPrice() < 0.0001) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No cargo price specified -  must be non-zero"));
						deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice());
						statuses.add(deco);
					}
				}
				if (destinationType == DestinationType.START || destinationType == DestinationType.LOAD || destinationType == DestinationType.OTHER) {
					if (shippingCostRow.getCvValue() < 0.0001) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No cargo CV specified - be non-zero"));
						deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice());
						statuses.add(deco);
					}
				}
			}

		}
		return Activator.PLUGIN_ID;
	}

}
