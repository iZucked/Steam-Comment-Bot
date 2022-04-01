/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.time.TimeUtils;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NonShippedVesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		Vessel nominatedVessel = null;

		NonNullPair<ZonedDateTime, ZonedDateTime> interval = null;
		String type = "";
		String name = "";
		final EObject target = ctx.getTarget();
		if (target instanceof LoadSlot loadSlot) {
			if (loadSlot.isDESPurchase()) {
				nominatedVessel = loadSlot.getNominatedVessel();

				if (loadSlot.getWindowStart() == null) {
					return;
				}
				final ZonedDateTime start = loadSlot.getSchedulingTimeWindow().getStart();
				ZonedDateTime end = loadSlot.getSchedulingTimeWindow().getEnd();
				// For divertible cargoes, we should find the round trip time
				if (loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
					end = end.plusDays(loadSlot.getSlotOrDelegateShippingDaysRestriction());
				}
				interval = new NonNullPair<>(start, end);
				type = "DES Purchase";
				name = loadSlot.getName();
			}
		} else if (target instanceof DischargeSlot dischargeSlot) {
			if (dischargeSlot.isFOBSale()) {
				if (dischargeSlot.getWindowStart() == null) {
					return;
				}

				nominatedVessel = dischargeSlot.getNominatedVessel();
				final ZonedDateTime start = dischargeSlot.getSchedulingTimeWindow().getStart();
				final ZonedDateTime end = dischargeSlot.getSchedulingTimeWindow().getEnd();
				interval = new NonNullPair<>(start, end);
				type = "FOB Sale";
				name = dischargeSlot.getName();
			}
		}

		if (nominatedVessel == null) {
			return;
		}

		final MMXRootObject rootObject = extraContext.getRootObject();
		if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
				NonNullPair<ZonedDateTime, ZonedDateTime> availabilityInterval = null;

				if (va.isSetMaxDuration()) {
					// We can only usefully use max duration when the start or end is properly
					// bounded.
					if (va.isSetStartAfter() && va.isSetStartBy()) {
						ZonedDateTime start = va.getStartAfterAsDateTime();
						ZonedDateTime end = va.getStartByAsDateTime().plusDays(va.getMaxDuration());

						if (va.isSetEndAfter()) {
							ZonedDateTime end2 = va.getEndAfterAsDateTime();
							if (end2.isAfter(end)) {
								end = end2;
							}
						}
						availabilityInterval = new NonNullPair<>(start, end);

					} else if (va.isSetEndAfter() && va.isSetEndBy()) {
						ZonedDateTime end = va.getEndByAsDateTime();
						ZonedDateTime start = va.getEndAfterAsDateTime().minusDays(va.getMaxDuration());

						if (va.isSetStartAfter()) {
							ZonedDateTime start2 = va.getStartAfterAsDateTime();
							if (start2.isBefore(start)) {
								start = start2;
							}
						}
						availabilityInterval = new NonNullPair<>(start, end);
					}
				}

				if (availabilityInterval == null) {
					// No max, duration, use the defined dates

					ZonedDateTime start;
					if (va.isSetStartAfter()) {
						start = va.getStartAfterAsDateTime();
					} else {
						// Start is unbounded.
						start = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
					}
					ZonedDateTime end;
					if (va.isSetEndBy()) {
						end = va.getEndByAsDateTime();
					} else {
						// End date is unbounded
						end = ZonedDateTime.now().withYear(Year.MAX_VALUE);
					}
					availabilityInterval = new NonNullPair<>(start, end);
				}

				if (nominatedVessel == va.getVessel() || extraContext.getOriginal(nominatedVessel) == va.getVessel()) {
					// Match dates to availability.
					if (TimeUtils.overlaps(availabilityInterval, interval)) {
						// Error
						final String message = String.format("%s %s| Nominated vessel is also used for shipped cargoes during the assigned period.", type, name);
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);

						statuses.add(failure);
					}

				}
			}
		}
	}
}
