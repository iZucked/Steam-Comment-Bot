/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Checks that {@link MaintenanceEvent} and {@link DryDockEvent} have a {@link Vessel} specified.
 * 
 * @author Simon Goodall
 * 
 */
public class VesselEventVesselSetConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof MaintenanceEvent) {
			final MaintenanceEvent maintenanceEvent = (MaintenanceEvent) target;

			final EList<AVesselSet> allowedVessels = maintenanceEvent.getAllowedVessels();
			final Set<AVessel> vessels = SetUtils.getVessels(allowedVessels);
			if (vessels.size() != 1) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Maintenance events need a single vessel assignemnt"));
				status.addEObjectAndFeature(maintenanceEvent, FleetPackage.eINSTANCE.getVesselEvent_AllowedVessels());
				return status;
			}

		}

		else if (target instanceof DryDockEvent) {
			final DryDockEvent dryDockEvent = (DryDockEvent) target;

			final EList<AVesselSet> allowedVessels = dryDockEvent.getAllowedVessels();
			final Set<AVessel> vessels = SetUtils.getVessels(allowedVessels);
			if (vessels.isEmpty()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Drydock events need a vessel specified"));
				status.addEObjectAndFeature(dryDockEvent, FleetPackage.eINSTANCE.getVesselEvent_AllowedVessels());
				return status;
			}
		}
		return ctx.createSuccessStatus();
	}
}
