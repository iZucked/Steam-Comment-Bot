/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.Instant;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.threeten.extra.Interval;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NonShippedVesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		Vessel nominatedVessel = null;

		Interval interval = null;
		String type = "";
		String name = "";
		final EObject target = ctx.getTarget();
		if (target instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) target;
			if (loadSlot.isDESPurchase()) {
				nominatedVessel = loadSlot.getNominatedVessel();

				if (loadSlot.getWindowStart() == null) {
					return Activator.PLUGIN_ID;
				}
				final ZonedDateTime start = loadSlot.getWindowStartWithSlotOrPortTime();
				ZonedDateTime end = loadSlot.getWindowEndWithSlotOrPortTime();
				// For divertible cargoes, we should find the round trip time
				if (loadSlot.isDivertible()) {
					end = end.plusDays(loadSlot.getShippingDaysRestriction());
				}
				interval = Interval.of(Instant.from(start), Instant.from(end));
				type = "DES Purchase";
				name = loadSlot.getName();
			}
		} else if (target instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) target;
			if (dischargeSlot.isFOBSale()) {
				if (dischargeSlot.getWindowStart() == null) {
					return Activator.PLUGIN_ID;
				}

				nominatedVessel = dischargeSlot.getNominatedVessel();
				final ZonedDateTime start = dischargeSlot.getWindowStartWithSlotOrPortTime();
				final ZonedDateTime end = dischargeSlot.getWindowEndWithSlotOrPortTime();
				interval = Interval.of(Instant.from(start), Instant.from(end));
				type = "FOB Sale";
				name = dischargeSlot.getName();
			}
		}

		if (nominatedVessel == null) {
			return Activator.PLUGIN_ID;
		}

		final MMXRootObject rootObject = extraContext.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
			final CargoModel cargoModel = portfolioModel.getCargoModel();
			for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
				Interval availabilityInterval = null;

				{
					ZonedDateTime start;
					if (va.isSetStartAfter()) {
						start = va.getStartAfterAsDateTime();
					} else {
						start = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
					}
					ZonedDateTime end;
					if (va.isSetEndBy()) {
						end = va.getEndByAsDateTime();
					} else {
						end = ZonedDateTime.now().withYear(Year.MAX_VALUE);
					}
					availabilityInterval = Interval.of(Instant.from(start), Instant.from(end));
				}

				if (nominatedVessel == va.getVessel() || extraContext.getOriginal(nominatedVessel) == va.getVessel()) {
					// Match dates to availability.
					if (availabilityInterval.overlaps(interval)) {
						// Error
						final String message = String.format("%s %s| Nominated vessel is also used for shipped cargoes during the assigned period.", type, name);
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);

						statuses.add(failure);
					}

				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
