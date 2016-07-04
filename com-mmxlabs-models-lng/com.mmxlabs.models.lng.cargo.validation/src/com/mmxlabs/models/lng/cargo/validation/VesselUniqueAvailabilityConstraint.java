/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
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

	protected HashMap<Vessel, List<VesselAvailability>> createCurrentConstraintData(final IValidationContext ctx, final IExtraValidationContext extraContext) {
		final HashMap<Vessel, List<VesselAvailability>> result = new HashMap<Vessel, List<VesselAvailability>>();

		final VesselAvailability target = (VesselAvailability) ctx.getTarget();
		final Pair<EObject, EReference> containerAndFeature = new Pair<EObject, EReference>(extraContext.getContainer(target), extraContext.getContainment(target));

		final EObject container = containerAndFeature.getFirst(); // target.eContainer();
		if (container == null) {
			return result;
		}

		final List<EObject> siblings = extraContext.getSiblings(target);

		for (final EObject sibling : siblings) {
			final Vessel vessel = ((VesselAvailability) sibling).getVessel();
			if (!result.containsKey(vessel)) {
				result.put(vessel, new LinkedList<VesselAvailability>());
			}
			result.get(vessel).add((VesselAvailability) sibling);
		}

		return result;
	}

	protected HashMap<Vessel, List<VesselAvailability>> getCurrentConstraintData(final IValidationContext ctx, final IExtraValidationContext extraContext) {
		HashMap<Vessel, List<VesselAvailability>> result = (HashMap<Vessel, List<VesselAvailability>>) ctx.getCurrentConstraintData();
		if (result == null) {
			result = createCurrentConstraintData(ctx, extraContext);
			ctx.putCurrentConstraintData(result);
		}
		return result;
	}

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof VesselAvailability) {
			final VesselAvailability availability = (VesselAvailability) target;
			final List<VesselAvailability> conflicts = getCurrentConstraintData(ctx, extraContext).get(availability.getVessel());

			if (conflicts != null && (conflicts.size() > 1 || conflicts.get(0) != availability)) {
				// add a validation failure to the current availability
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("More than one availability assigned to same vessel."));
				dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
				statuses.add(dcsd);
			}

		}

		return Activator.PLUGIN_ID;
	}
}
