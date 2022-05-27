/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SensibleEventDateConstraint extends AbstractModelMultiConstraint {

	private final LocalDateTime earliestDate = LocalDateTime.of(2000, 1, 1, 0, 0);
	private final EStructuralFeature[] eventDateFields = { CargoPackage.Literals.VESSEL_EVENT__START_AFTER, CargoPackage.Literals.VESSEL_EVENT__START_BY, };

	/**
	 * Impose sensible date cutoffs for vessel charters and events
	 */
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselEvent event) {
			for (final EStructuralFeature feature : eventDateFields) {
				final LocalDateTime date = (LocalDateTime) object.eGet(feature);
				if (date != null && date.isBefore(earliestDate)) {
					String msg = String.format("'%s': '%s' date is before %s.", event.getName(), feature.getName(), earliestDate.toString());
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
					status.addEObjectAndFeature(object, feature);
					failures.add(status);
				}
			}
		}
	}
}
