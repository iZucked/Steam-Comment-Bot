/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
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
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ShippingCostRowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();


		if (target instanceof ShippingCostRow) {
			final ShippingCostRow shippingCostRow = (ShippingCostRow) target;
			{
				final EObject plan = shippingCostRow.eContainer();
				final EObject original = extraContext.getOriginal(plan);
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
					if (shippingCostRow.getHeelVolume() > 0) {
						if (shippingCostRow.getCargoPrice() < 0.0001) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No gas price specified -  must be non-zero"));
							deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice());
							statuses.add(deco);
						}
						if (shippingCostRow.getCargoPrice() > 70.0) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Gas price is too high."));
							deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice());
							statuses.add(deco);
						}
						EObject container = shippingCostRow.eContainer();
						if (container == null) {

							final EObject original = extraContext.getOriginal(shippingCostRow);
							if (original != null) {
								container = original.eContainer();
							}
						}

						if (container instanceof ShippingCostPlan) {

							final ShippingCostPlan shippingCostPlan = (ShippingCostPlan) container;
							if (shippingCostPlan.getVessel() != null) {
								final Vessel vessel = shippingCostPlan.getVessel();
								final VesselClass vesselClass = shippingCostPlan.getVessel().getVesselClass();
								final double capacity = (double) vessel.getVesselOrVesselClassCapacity() * vessel.getVesselOrVesselClassFillCapacity();
								if (shippingCostRow.getHeelVolume() > capacity) {
									final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
											"Heel volume is greater than vessel capacity of %.0f (%d) ", capacity, vesselClass.getCapacity())));
									deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_HeelVolume());
									statuses.add(deco);
								}
							}
						}
					}
				}
				if (destinationType == DestinationType.START || destinationType == DestinationType.LOAD || destinationType == DestinationType.OTHER) {
					if (shippingCostRow.getHeelVolume() > 0) {
						if (shippingCostRow.getCvValue() < 0.0001) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No gas CV specified - must be non-zero"));
							deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CvValue());
							statuses.add(deco);
						}
						if (shippingCostRow.getCvValue() > 50.0) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Gas CV is too high"));
							deco.addEObjectAndFeature(shippingCostRow, AnalyticsPackage.eINSTANCE.getShippingCostRow_CvValue());
							statuses.add(deco);
						}
					}
				}
			}

		}
		return Activator.PLUGIN_ID;
	}
}
