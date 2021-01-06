/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Validates the "allowed vessels" setting on {@link VesselEvent} objects. There must be at least one matching {@link Vessel} in the fleet, and exactly one {@link Vessel} for {@link MaintenanceEvent}
 * and {@link DryDockEvent} objects.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class VesselEventVesselSetConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (!(extraContext.getContainer(target) instanceof CargoModel)) {
			return;
		}

		if (target instanceof VesselEvent) {
			final VesselEvent ve = (VesselEvent) target;

			final Set<Vessel> vessels = SetUtils.getObjects(ve.getAllowedVessels());
			int possibleVesselCount = vessels.size();

			if (target instanceof MaintenanceEvent || target instanceof DryDockEvent) {
				if (possibleVesselCount != 1) {
					final String eventTypeString = (target instanceof MaintenanceEvent) ? "Maintenance" : "Drydock";
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
							eventTypeString + " events must have exactly one allowed vessel. The current allowed vessel settings allow for " + possibleVesselCount + " fleet vessels."));
					status.addEObjectAndFeature(ve, CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels());
					statuses.add(status);
				}
			} else {
				if (possibleVesselCount == 0) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Vessel events must have at least one allowed vessel. The current allowed vessel settings exclude all fleet vessels."));
					status.addEObjectAndFeature(ve, CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels());
					statuses.add(status);
				}
			}

		}
	}
}
