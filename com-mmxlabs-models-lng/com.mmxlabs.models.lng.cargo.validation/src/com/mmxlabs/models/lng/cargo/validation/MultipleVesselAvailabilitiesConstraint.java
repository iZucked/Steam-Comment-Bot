/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.TimeUtils;
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

			final HashMap<Vessel, List<VesselAvailability>> currentConstraintData = getCurrentConstraintData(ctx, extraContext);
			validateAvailability(ctx, statuses, currentConstraintData, (VesselAvailability) target);
		}

		return Activator.PLUGIN_ID;
	}

	private void validateAvailability(final IValidationContext ctx, final List<IStatus> statuses, final HashMap<Vessel, List<VesselAvailability>> currentConstraintData,
			final VesselAvailability availability) {
		final Vessel vessel = availability.getVessel();
		final List<VesselAvailability> conflicts = currentConstraintData.get(vessel);
		// conflicts.remove(availability);
		Collections.sort(conflicts, (a, b) -> {
			if (a.getStartAfter() == null) {
				return -1;
			}
			if (a.getEndBy() == null) {
				return 1;
			}
			if (b.getStartAfter() == null) {
				return 1;
			}
			if (b.getEndBy() == null) {
				return -1;
			}
			return a.getStartAfter().compareTo(b.getStartAfter());
		});

		// sort out nulls
		final boolean nulls = false;
		final String vesselName = vessel == null ? "<Unspecified vessel>" : vessel.getName();
		if (conflicts.stream().filter(c -> c.getStartAfter() == null).count() > 1) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only one availability with the same vessel can have an empty Start After date", vesselName)));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.indexOf(availability) != 0 && availability.getStartAfter() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only the earliest multiple availability can have an empty Start After date.", vesselName)));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.stream().filter(c -> c.getEndBy() == null).count() > 1) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only one availability with the same vessel can have an empty End By date", vesselName)));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.indexOf(availability) != conflicts.size() - 1 && availability.getEndBy() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only the earliest availability with the same vessel can have an empty End By date.", vesselName)));
			dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY);
			statuses.add(dcsd);
			return;
		}
		if (nulls == false && conflicts != null && (conflicts.size() > 1)) {
			for (final VesselAvailability sibling : conflicts) {
				if (sibling == availability) {
					continue;
				}
				if (overlaps(sibling, availability)) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Multiple availabilities assigned to the same vessel must not overlap.", vesselName)));
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER);
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY);
					dcsd.addEObjectAndFeature(availability, CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES);
					statuses.add(dcsd);
					break;
				}
			}
		}
	}

	private boolean overlaps(final VesselAvailability a, final VesselAvailability b) {

		final NonNullPair<ZonedDateTime, ZonedDateTime> aRange = getRange(a);
		final NonNullPair<ZonedDateTime, ZonedDateTime> bRange = getRange(b);

		return TimeUtils.overlaps(aRange, bRange);
	}

	private NonNullPair<ZonedDateTime, ZonedDateTime> getRange(final VesselAvailability availabilty) {
		ZonedDateTime start = availabilty.getStartAfterAsDateTime();
		if (start == null) {
			start = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		}
		ZonedDateTime end = availabilty.getEndByAsDateTime();
		if (end == null) {
			end = ZonedDateTime.of(2050, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		}
		return new NonNullPair<ZonedDateTime, ZonedDateTime>(start, end);
	}

}
