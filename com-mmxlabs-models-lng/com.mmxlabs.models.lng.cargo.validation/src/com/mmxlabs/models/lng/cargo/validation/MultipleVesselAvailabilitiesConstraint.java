/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
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
public class MultipleVesselAvailabilitiesConstraint extends AbstractModelMultiConstraint {

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
			CargoModel cargoModel;
			if (extraContext.isValidatingClone()) {
				cargoModel = (CargoModel) target.eContainer();
			} else {
				cargoModel = (CargoModel) extraContext.getContainer(target);
			}

			HashMap<Vessel, List<VesselAvailability>> currentConstraintData = getCurrentConstraintData(ctx, extraContext);
			validateAvailability(ctx, statuses, currentConstraintData, (VesselAvailability) target);
		}

		return Activator.PLUGIN_ID;
	}

	private void validateAvailability(final IValidationContext ctx, final List<IStatus> statuses, HashMap<Vessel, List<VesselAvailability>> currentConstraintData, VesselAvailability availability) {
		final List<VesselAvailability> conflicts = currentConstraintData.get(availability.getVessel());
		// conflicts.remove(availability);
		Collections.sort(conflicts, (a, b) -> a.getStartBy() == null ? - 1 : b.getStartBy() == null ? 1 : a.getStartBy().compareTo(b.getStartBy()));
		// sort out nulls
		boolean nulls = false;
		if (conflicts.stream().filter(c -> c.getStartBy() == null).count() > 1) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only one availability with the same vessel can have an empty Start By date", availability.getVessel().getName())));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.indexOf(availability) != 0 && availability.getStartBy() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only the earliest multiple availability can have an empty Start By date.", availability.getVessel().getName())));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.stream().filter(c -> c.getEndBy() == null).count() > 1) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only one availability with the same vessel can have an empty End By date", availability.getVessel().getName())));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.indexOf(availability) != conflicts.size() - 1 && availability.getEndBy() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only the earliest availability with the same vessel can have an empty End By date.", availability.getVessel().getName())));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY);
			statuses.add(dcsd);
			return;
		}
		if (nulls == false && conflicts != null && (conflicts.size() > 1)) {
			for (VesselAvailability sibling : conflicts) {
				if (sibling == availability) {
					continue;
				}
				if (!(isBefore(sibling, availability) || isAfter(sibling, availability))) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
							.createFailureStatus(String.format("%s - Multiple availabilities assigned to the same vessel must not overlap.", availability.getVessel().getName())));
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY);
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY);
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES);
					statuses.add(dcsd);
					break;
				}
			}
		}
	}

	private boolean isAfter(VesselAvailability sibling, VesselAvailability availability) {
		if (sibling.getStartBy() == null) {
			return false;
		}
		if (availability.getEndBy() == null) {
			return false;
		}
		if ((availability.getStartBy() == null || sibling.getStartBy().isAfter(availability.getStartBy())) && (sibling.getEndBy() == null || sibling.getEndBy().isAfter(availability.getEndBy())) && !availability.getEndBy().isBefore(sibling.getStartBy())) {
			return true;
		}
		return false;
	}
	
	private boolean isBefore(VesselAvailability sibling, VesselAvailability availability) {
		if (availability.getStartBy() == null) {
			return false;
		}
		if (sibling.getEndBy() == null) {
			return false;
		}
		if (((sibling.getStartBy() == null || sibling.getStartBy().isBefore(availability.getStartBy())) && (availability.getEndBy() == null || sibling.getEndBy().isBefore(availability.getEndBy()))
				&& !sibling.getEndBy().isAfter(availability.getStartBy()))) {
			return true;
		}
		return false;
	}

}
