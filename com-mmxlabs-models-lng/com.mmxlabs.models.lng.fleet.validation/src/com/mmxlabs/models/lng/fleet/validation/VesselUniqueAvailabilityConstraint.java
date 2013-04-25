/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks that {@link BaseFuel} values are none-zero
 * 
 * @author Simon Goodall
 * 
 */
public class VesselUniqueAvailabilityConstraint extends AbstractModelMultiConstraint {

	protected HashMap<Vessel, List<VesselAvailability>> createCurrentConstraintData(IValidationContext ctx) {
		final HashMap<Vessel, List<VesselAvailability>> result = new HashMap<Vessel, List<VesselAvailability>>();

		final VesselAvailability target = (VesselAvailability) ctx.getTarget();
		final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
		final Pair<EObject, EReference> containerAndFeature = new Pair<EObject, EReference>(extraValidationContext.getContainer(target), extraValidationContext.getContainment(target));

		final EObject container = containerAndFeature.getFirst(); // target.eContainer();
		if (container == null) {
			return result;
		}
		
		List<EObject> siblings = extraValidationContext.getSiblings(target);
		
		for (EObject sibling: siblings) {
			final Vessel vessel = ((VesselAvailability) sibling).getVessel();
			if (!result.containsKey(vessel)) {
				result.put(vessel, new LinkedList<VesselAvailability>());
			}
			result.get(vessel).add((VesselAvailability) sibling);
		}			
		
		return result;
	}
	
	protected HashMap<Vessel, List<VesselAvailability>> getCurrentConstraintData(IValidationContext ctx) {
		HashMap<Vessel, List<VesselAvailability>> result = (HashMap<Vessel, List<VesselAvailability>>) ctx.getCurrentConstraintData();
		if (result == null) {
			result = createCurrentConstraintData(ctx);
			ctx.putCurrentConstraintData(result);
		}
		return result;
	}
	
	@Override
	protected String validate(IValidationContext ctx, List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		
		if (target instanceof VesselAvailability) {
			final VesselAvailability availability = (VesselAvailability) target;
			List<VesselAvailability> conflicts = getCurrentConstraintData(ctx).get(availability.getVessel());
			
			if (conflicts != null && (conflicts.size() > 1 || conflicts.get(0) != availability)) {
				// add a validation failure to the current availability
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("More than one availability assigned to same vessel."));
				dcsd.addEObjectAndFeature(availability, FleetPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
				statuses.add(dcsd);				
			}			
			
		}

		return Activator.PLUGIN_ID;
	}
}
