package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ShippingCostRowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(IValidationContext ctx, List<IStatus> statuses) {
		EObject target = ctx.getTarget();
		if (target instanceof ShippingCostRow) {
			ShippingCostRow shippingCostRow = (ShippingCostRow) target;
			if (shippingCostRow.getDate() == null) {
				DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No date specified"));
				deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_Date());
				statuses.add(deco);
			}

			DestinationType destinationType = shippingCostRow.getDestinationType();
			if (destinationType == null) {
				DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No type specified"));
				deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
				statuses.add(deco);
			} else {
				if (destinationType == DestinationType.START || destinationType == DestinationType.DISCHARGE || destinationType == DestinationType.OTHER) {
					if (shippingCostRow.getCargoPrice() < 0.0001) {
						DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No cargo must be non-zero"));
						deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice());
						statuses.add(deco);
					}
				}
				if (destinationType == DestinationType.START || destinationType == DestinationType.LOAD || destinationType == DestinationType.OTHER) {
					if (shippingCostRow.getCvValue() < 0.0001) {
						DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No cargo CV be non-zero"));
						deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice());
						statuses.add(deco);
					}
				}
			}

		}
		return "com.mmxlabs.models.lng.analytics.validation";
	}

}
