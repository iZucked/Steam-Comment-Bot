/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.joda.time.DateTime;
import org.joda.time.Interval;

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

				final DateTime start = new DateTime(loadSlot.getWindowStartWithSlotOrPortTime());
				DateTime end = new DateTime(loadSlot.getWindowEndWithSlotOrPortTime());
				// For divertible cargoes, we should find the round trip time
				if (loadSlot.isDivertible()) {
					end = end.plusDays(loadSlot.getShippingDaysRestriction());
				}
				interval = new Interval(start, end);
				type = "DES Purchase";
				name = loadSlot.getName();
			}
		} else if (target instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) target;
			if (dischargeSlot.isFOBSale()) {
				nominatedVessel = dischargeSlot.getNominatedVessel();
				final DateTime start = new DateTime(dischargeSlot.getWindowStartWithSlotOrPortTime());
				final DateTime end = new DateTime(dischargeSlot.getWindowEndWithSlotOrPortTime());
				interval = new Interval(start, end);
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
					DateTime start;
					if (va.isSetStartAfter()) {
						start = new DateTime(va.getStartAfter());
					} else {
						start = new DateTime(0);
					}
					DateTime end;
					if (va.isSetEndBy()) {
						end = new DateTime(va.getEndBy());
					} else {
						end = new DateTime(Long.MAX_VALUE);
					}
					availabilityInterval = new Interval(start, end);
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
