/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VesselAvailabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignment = (AssignableElement) object;

			if (assignment.getAssignment() == null) {
				return Activator.PLUGIN_ID;
			}
			CargoModel cargoModel = null;
			final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
			final MMXRootObject rootObject = extraValidationContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
				if (portfolioModel != null) {
					cargoModel = portfolioModel.getCargoModel();
				}

			}

			if (cargoModel == null) {
				return Activator.PLUGIN_ID;
			}

			if (assignment.getAssignment() instanceof Vessel) {
				final Vessel vessel = (Vessel) assignment.getAssignment();

				VesselAvailability vesselAvailability = null;
				for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
					if (va.getVessel() == extraValidationContext.getOriginal(vessel) || va.getVessel() == extraValidationContext.getReplacement(vessel)) {
						vesselAvailability = va;
						break;

					}
				}

				if (vesselAvailability != null) {

					if (assignment instanceof Cargo) {
						final Cargo cargo = (Cargo) assignment;
						for (final Slot slot : cargo.getSlots()) {
							if (vesselAvailability.isSetStartAfter()) {
								if (slot.getWindowEndWithSlotOrPortTime().before(vesselAvailability.getStartAfter())) {
									final String message = String.format("Slot|%s is assigned to vessel %s but window date is before the vessel start date.", slot.getName(), vessel.getName());
									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
									failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
									failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
									failures.add(failure);
								}
							} else if (vesselAvailability.isSetStartBy()) {
								// N/A
							} else if (vesselAvailability.isSetEndAfter()) {
								// N/A
							} else if (vesselAvailability.isSetEndBy()) {
								if (slot.getWindowStartWithSlotOrPortTime().after(vesselAvailability.getEndBy())) {
									final String message = String.format("Slot|%s is assigned to vessel %s but window date is after the vessel end date.", slot.getName(), vessel.getName());
									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
									failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
									failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
									failures.add(failure);
								}
							}
						}

					} else if (assignment instanceof VesselEvent) {
						final VesselEvent vesselEvent = (VesselEvent) assignment;
						if (vesselAvailability.isSetStartAfter()) {
							if (vesselEvent.getStartBy().before(vesselAvailability.getStartAfter())) {
								final String message = String.format("Vessel Event|%s is assigned to vessel %s but window date is before the vessel start date.", vesselEvent.getName(),
										vessel.getName());
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
								failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
								failures.add(failure);
							}
						} else if (vesselAvailability.isSetStartBy()) {
						} else if (vesselAvailability.isSetEndAfter()) {
						} else if (vesselAvailability.isSetEndBy()) {
							if (vesselEvent.getStartAfter().after(vesselAvailability.getEndBy())) {
								final String message = String.format("Vessel Event|%s is assigned to vessel %s but window date is after the vessel end date.", vesselEvent.getName(), vessel.getName());
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
								failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
								failures.add(failure);
							}
						}
					}

				}

			}
		}
		return Activator.PLUGIN_ID;
	}
}
