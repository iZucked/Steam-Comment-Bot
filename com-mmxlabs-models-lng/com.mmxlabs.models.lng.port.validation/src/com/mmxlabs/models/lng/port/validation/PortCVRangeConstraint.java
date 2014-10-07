package com.mmxlabs.models.lng.port.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PortCVRangeConstraint extends AbstractModelConstraint{

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof Port) {
			final Port port = (Port) target;
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
		}
		
		return ctx.createSuccessStatus();
	}

}
