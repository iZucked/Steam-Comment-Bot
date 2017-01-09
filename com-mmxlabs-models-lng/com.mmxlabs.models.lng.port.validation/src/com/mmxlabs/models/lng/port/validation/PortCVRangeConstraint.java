/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PortCVRangeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Port) {
			final Port port = (Port) target;
			DetailConstraintStatusDecorator rangeCheckDSD = checkRange(port, ctx);
			if (rangeCheckDSD != null) {
				failures.add(rangeCheckDSD);
			}
			DetailConstraintStatusDecorator minCheck = checkNonNegative(port, PortPackage.Literals.PORT__MIN_CV_VALUE, ctx);
			if (minCheck != null) {
				failures.add(minCheck);
			}
			DetailConstraintStatusDecorator maxCheck = checkNonNegative(port, PortPackage.Literals.PORT__MAX_CV_VALUE, ctx);
			if (maxCheck != null) {
				failures.add(maxCheck);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private DetailConstraintStatusDecorator checkRange(Port port, final IValidationContext ctx) {
		if (port.isSetMinCvValue() && port.isSetMaxCvValue()) {
			final double minCvValue = port.getMinCvValue();
			final double maxCvValue = port.getMaxCvValue();

			if (minCvValue > maxCvValue) {
				final String failureMessage = String.format("Port '%s' has minimum CV %.2f greater than maximum CV %.2f", port.getName(), minCvValue, maxCvValue);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_MinCvValue());
				dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_MaxCvValue());
				return dsd;
			}

		}
		return null;
	}

	/*
	 * Check non zero field values but handled in the emf attribute bounds
	 */
	private DetailConstraintStatusDecorator checkNonNegative(Port port, EStructuralFeature feature, final IValidationContext ctx) {
		if (port.eIsSet(feature)) {
			final double value = (Double) port.eGet(feature);
			if (value < 0) {
				final String failureMessage = String.format("Port '%s' has %s less than 0", port.getName(), feature.getName());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(port, feature);
				return dsd;
			}
		}
		return null;
	}

}
