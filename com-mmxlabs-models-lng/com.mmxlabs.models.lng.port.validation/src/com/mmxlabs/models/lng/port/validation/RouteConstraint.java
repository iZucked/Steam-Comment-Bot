/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class RouteConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Route) {
			Route route = (Route) target;

			if (route.getRouteOption() != null && route.getRouteOption() != RouteOption.DIRECT) {
				DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus().withTypedName("Canal", route.getName());

				if (route.getNorthEntrance() == null || route.getNorthEntrance().getPort() == null) {
					DetailConstraintStatusFactory f2 = factory.copyName();
					f2.withMessage("Missing north entrance");
					f2.withObjectAndFeature(target, PortPackage.eINSTANCE.getRoute_NorthEntrance());
					if (route.getNorthEntrance() != null) {
						f2.withObjectAndFeature(route.getNorthEntrance(), PortPackage.eINSTANCE.getEntryPoint_Port());
					}
					f2.make(ctx, statuses);
				}
				if (route.getSouthEntrance() == null || route.getSouthEntrance().getPort() == null) {
					DetailConstraintStatusFactory f2 = factory.copyName();
					f2.withMessage("Missing south entrance");
					f2.withObjectAndFeature(target, PortPackage.eINSTANCE.getRoute_SouthEntrance());
					if (route.getSouthEntrance() != null) {
						f2.withObjectAndFeature(route.getSouthEntrance(), PortPackage.eINSTANCE.getEntryPoint_Port());
					}
					f2.make(ctx, statuses);
				}
				if (route.getDistance() < 1.0) {
					DetailConstraintStatusFactory f2 = factory.copyName();
					f2.withMessage("Missing canal distance");
					f2.withObjectAndFeature(target, PortPackage.eINSTANCE.getRoute_Distance());
					f2.make(ctx, statuses);
				}
			}
		}
	}
}
