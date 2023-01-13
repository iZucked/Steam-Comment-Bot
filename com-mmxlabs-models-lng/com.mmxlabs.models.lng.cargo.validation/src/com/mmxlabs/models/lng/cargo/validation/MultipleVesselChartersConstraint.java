/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MultipleVesselChartersConstraint extends AbstractModelMultiConstraint {

	protected HashMap<Vessel, List<VesselCharter>> createCurrentConstraintData(final IValidationContext ctx, final IExtraValidationContext extraContext) {
		final HashMap<Vessel, List<VesselCharter>> result = new HashMap<Vessel, List<VesselCharter>>();

		final VesselCharter target = (VesselCharter) ctx.getTarget();
		final Pair<EObject, EReference> containerAndFeature = new Pair<EObject, EReference>(extraContext.getContainer(target), extraContext.getContainment(target));

		final EObject container = containerAndFeature.getFirst(); // target.eContainer();
		if (container == null) {
			return result;
		}

		final List<EObject> siblings = extraContext.getSiblings(target);

		for (final EObject sibling : siblings) {
			final Vessel vessel = ((VesselCharter) sibling).getVessel();
			if (!result.containsKey(vessel)) {
				result.put(vessel, new LinkedList<VesselCharter>());
			}
			result.get(vessel).add((VesselCharter) sibling);
		}

		return result;
	}

	protected HashMap<Vessel, List<VesselCharter>> getCurrentConstraintData(final IValidationContext ctx, final IExtraValidationContext extraContext) {
		HashMap<Vessel, List<VesselCharter>> result = (HashMap<Vessel, List<VesselCharter>>) ctx.getCurrentConstraintData();
		if (result == null) {
			result = createCurrentConstraintData(ctx, extraContext);
			ctx.putCurrentConstraintData(result);
		}
		return result;
	}

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof VesselCharter vesselCharter) {
			EObject container;
			if (extraContext.isValidatingClone()) {
				container = vesselCharter.eContainer();
			} else {
				container = extraContext.getContainer(vesselCharter);
			}

			if (container instanceof CargoModel) {

				final HashMap<Vessel, List<VesselCharter>> currentConstraintData = getCurrentConstraintData(ctx, extraContext);
				validateVesselCharter(ctx, statuses, currentConstraintData, vesselCharter);
			}
		}
	}

	private void validateVesselCharter(final IValidationContext ctx, final List<IStatus> statuses, final HashMap<Vessel, List<VesselCharter>> currentConstraintData,
			final VesselCharter vesselCharter) {
		final Vessel vessel = vesselCharter.getVessel();
		final List<VesselCharter> conflicts = currentConstraintData.get(vessel);
		// conflicts.remove(vesselCharter);
		if (conflicts == null) {
			return;
		}
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
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only one charter with the same vessel can have an empty Start After date", vesselName)));
			dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.indexOf(vesselCharter) != 0 && vesselCharter.getStartAfter() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only the earliest charter can have an empty Start After date.", vesselName)));
			dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.stream().filter(c -> c.getEndBy() == null).count() > 1) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only one charter with the same vessel can have an empty End By date", vesselName)));
			dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__END_BY);
			statuses.add(dcsd);
			return;
		}
		if (conflicts.indexOf(vesselCharter) != conflicts.size() - 1 && vesselCharter.getEndBy() == null) {
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Only the earliest charter with the same vessel can have an empty End By date.", vesselName)));
			dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__END_BY);
			statuses.add(dcsd);
			return;
		}
		if (nulls == false && conflicts != null && (conflicts.size() > 1)) {
			for (final VesselCharter sibling : conflicts) {
				if (sibling == vesselCharter) {
					continue;
				}
				if (overlaps(sibling, vesselCharter)) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("%s - Multiple charters assigned to the same vessel must not overlap.", vesselName)));
					dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__VESSEL);
					dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER);
					dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_CHARTER__END_BY);
					dcsd.addEObjectAndFeature(vesselCharter, CargoPackage.Literals.CARGO_MODEL__VESSEL_CHARTERS);
					statuses.add(dcsd);
					break;
				}
			}
		}
	}

	private boolean overlaps(final VesselCharter a, final VesselCharter b) {

		final NonNullPair<ZonedDateTime, ZonedDateTime> aRange = getRange(a);
		final NonNullPair<ZonedDateTime, ZonedDateTime> bRange = getRange(b);

		return TimeUtils.overlaps(aRange, bRange);
	}

	private NonNullPair<ZonedDateTime, ZonedDateTime> getRange(final VesselCharter vesselCharter) {
		ZonedDateTime start = vesselCharter.getStartAfterAsDateTime();
		if (start == null) {
			start = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		}
		ZonedDateTime end = vesselCharter.getEndByAsDateTime();
		if (end == null) {
			end = ZonedDateTime.of(2050, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		}
		return new NonNullPair<>(start, end);
	}

}
