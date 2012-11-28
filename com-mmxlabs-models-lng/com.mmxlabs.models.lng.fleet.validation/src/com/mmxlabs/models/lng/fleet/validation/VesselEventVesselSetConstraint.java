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
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Validates the "allowed vessels" setting on {@link VesselEvent} objects. There must be at least one matching {@link Vessel}
 * in the fleet, and exactly one {@link Vessel} for {@link MaintenanceEvent} and {@link DryDockEvent} objects. 
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class VesselEventVesselSetConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof VesselEvent) {
			final VesselEvent ve = (VesselEvent) target;
			
			final Set<AVessel> vessels = SetUtils.getVessels(ve.getAllowedVessels());			
			int possibleVesselCount = vessels.size();
			
			if (target instanceof MaintenanceEvent || target instanceof DryDockEvent) {
				if (possibleVesselCount != 1) {
					final String eventTypeString = (target instanceof MaintenanceEvent) ? "Maintenance" : "Drydock";
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(eventTypeString + " events must have exactly one allowed vessel. The current allowed vessel settings allow for " + possibleVesselCount + " fleet vessels."));
					status.addEObjectAndFeature(ve, FleetPackage.eINSTANCE.getVesselEvent_AllowedVessels());
					return status;
				}
			}
			else {
				if (possibleVesselCount == 0) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Vessel events must have at least one allowed vessel. The current allowed vessel settings exclude all fleet vessels."));
					status.addEObjectAndFeature(ve, FleetPackage.eINSTANCE.getVesselEvent_AllowedVessels());
					return status;
				}				
			}
			
		}

		return ctx.createSuccessStatus();
	}
}
