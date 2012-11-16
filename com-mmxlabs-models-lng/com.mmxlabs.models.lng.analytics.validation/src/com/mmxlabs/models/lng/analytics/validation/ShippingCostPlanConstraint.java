package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ShippingCostPlanConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(IValidationContext ctx, List<IStatus> statuses) {
		EObject target = ctx.getTarget();
		if (target instanceof ShippingCostPlan) {
			ShippingCostPlan shippingCostPlan = (ShippingCostPlan) target;

			if (shippingCostPlan.getBaseFuelPrice() < 0.0001) {
				DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base fuel price must be non-zero"));
				deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_BaseFuelPrice());
				statuses.add(deco);
			}

			DestinationType lastType = null;
			int idx = 0;
			for (ShippingCostRow row : shippingCostPlan.getRows()) {
				DestinationType type = row.getDestinationType();
				if (idx == 0) {
					if (type != DestinationType.START) {
						DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("First row must be a START row"));
						deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
						deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
						statuses.add(deco);
					}
				} else if (idx == shippingCostPlan.getRows().size() - 1) {
					if (type != DestinationType.END) {
						DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Last row must be a END row"));
						deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
						deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
						statuses.add(deco);
					}
				} else {
					if (type == DestinationType.START || type == DestinationType.END) {
						DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Waypoint rows cannot be START or END types"));
						deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
						deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
						statuses.add(deco);
					}
				}
				if (lastType == DestinationType.LOAD && type != DestinationType.DISCHARGE) {
					DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("LOAD rows must be followed by DISCHARGE rows"));
					deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
					deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
					statuses.add(deco);
				}
				if (lastType != DestinationType.LOAD && type == DestinationType.DISCHARGE) {
					DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("LOAD rows must be followed by DISCHARGE rows"));
					deco.addEObjectAndFeature(shippingCostPlan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows());
					deco.addEObjectAndFeature(row, AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType());
					statuses.add(deco);
				}

				lastType = type;
				idx++;

			}
		}
		return "com.mmxlabs.models.lng.analytics.validation";
	}

}
