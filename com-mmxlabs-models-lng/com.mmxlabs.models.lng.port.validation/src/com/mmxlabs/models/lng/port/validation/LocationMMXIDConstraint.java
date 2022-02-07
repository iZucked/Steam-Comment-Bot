/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

@NonNullByDefault
public class LocationMMXIDConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Location location) {
			final Port port = (Port) location.eContainer();
			if (port != null && location.getMmxId() == null || location.getMmxId().isBlank()) {
				final String message = String.format("Port %s is missing internal ID. Please run the distances update from the LiNGO DB menu", ScenarioElementNameHelper.getName(port));

				DetailConstraintStatusFactory.makeStatus() //
						.withMessage(message) //
						.withObjectAndFeature(location, PortPackage.Literals.LOCATION__MMX_ID) //
						.make(ctx, statuses);
			}

		}
	}
}
