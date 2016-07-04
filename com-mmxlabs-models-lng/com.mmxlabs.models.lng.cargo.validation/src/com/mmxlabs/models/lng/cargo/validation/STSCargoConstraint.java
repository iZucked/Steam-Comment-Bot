/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class STSCargoConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		final int severity = extraContext.isValidatingClone() ? IStatus.WARNING : IStatus.ERROR;

		if (target instanceof Slot) {
			// final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;
			DischargeSlot transferFrom = null;
			LoadSlot transferTo = null;
			Cargo cargo = null;

			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.getTransferFrom() != null) {
					transferFrom = loadSlot.getTransferFrom();
					transferTo = loadSlot;
					cargo = ((DischargeSlot) extraContext.getOriginal(transferFrom)).getCargo();
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (dischargeSlot.getTransferTo() != null) {
					transferTo = dischargeSlot.getTransferTo();
					transferFrom = dischargeSlot;
					cargo = ((LoadSlot) extraContext.getOriginal(transferTo)).getCargo();
				}
			}

			if (transferFrom != null && transferTo != null) {
				validateAttributes(ctx, transferTo, transferFrom, failures, severity);
				validateSlotPlacements(ctx, extraContext, transferTo, transferFrom, failures, severity);

				if (cargo != null) {
					final Slot s = cargo.getSortedSlots().get(0);
					if (s instanceof LoadSlot) {
						final double cv = ((LoadSlot) s).getSlotOrDelegatedCV();
						if (cv != transferTo.getSlotOrDelegatedCV()) {
							final String failureMessage = String.format("Ship to Ship %s must be in sync", "CV");
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
							dsd.addEObjectAndFeature(transferTo, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
							dsd.addEObjectAndFeature(s, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
							failures.add(dsd);
						}
					}

					// make sure cargo is assigned to a vessel
					boolean isAssigned = cargo.getVesselAssignmentType() != null;

					if (!isAssigned) {
						final String failureMessage = String.format("Cargo '%s' must be assigned a vessel", cargo.getLoadName());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
						dsd.addEObjectAndFeature(transferTo.getCargo(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
						dsd.addEObjectAndFeature(transferFrom.getCargo(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
						failures.add(dsd);
					}
				}

				else {
					final String failureMessage = String.format("Ship to Ship '%s' must be part of a cargo", slot.getName());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
					dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CARGO);
					failures.add(dsd);
				}

			}

		}
		return Activator.PLUGIN_ID;
	}

	private void validateAttributes(final IValidationContext ctx, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final List<IStatus> failures, final int severity) {

		if (loadSlot.isOptional()) {
			final String failureMessage = String.format("Ship to Ship slot cannot be optional");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Optional());
			failures.add(dsd);
		}

		if (dischargeSlot.isOptional()) {
			final String failureMessage = String.format("Ship to Ship slot cannot be optional");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Optional());
			failures.add(dsd);
		}

		final EStructuralFeature[] features = { CargoPackage.Literals.SLOT__WINDOW_START, CargoPackage.Literals.SLOT__WINDOW_START_TIME, CargoPackage.Literals.SLOT__DURATION,
				CargoPackage.Literals.SLOT__PORT };

		for (final EStructuralFeature feature : features) {
			if (!Equality.isEqual(loadSlot.eGet(feature), dischargeSlot.eGet(feature))) {
				final String failureMessage = String.format("Ship to Ship %s must be in sync", feature.getName());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
				dsd.addEObjectAndFeature(loadSlot, feature);
				dsd.addEObjectAndFeature(dischargeSlot, feature);
				failures.add(dsd);
			}
		}

		if (!Equality.isEqual(loadSlot.getSlotOrContractMaxQuantity(), dischargeSlot.getSlotOrContractMaxQuantity())) {
			final String failureMessage = String.format("Ship to Ship %s must be in sync", "Max Quantity");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
			dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
			failures.add(dsd);
		}

		if (!Equality.isEqual(loadSlot.getSlotOrContractMinQuantity(), dischargeSlot.getSlotOrContractMinQuantity())) {
			final String failureMessage = String.format("Ship to Ship %s must be in sync", "Min Quantity");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
			dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
			failures.add(dsd);
		}

	}

	private void validateSlotPlacements(final IValidationContext ctx, final IExtraValidationContext extraContext, final LoadSlot loadSlot, final DischargeSlot dischargeSlot,
			final List<IStatus> failures, final int severity) {

		final Cargo loadCargo = loadSlot.getCargo();
		final Cargo dischargeCargo = dischargeSlot.getCargo();

		if (loadCargo != null && dischargeCargo != null) {

			// Check different cargoes
			if (Equality.isEqual(loadCargo, dischargeCargo)) {
				final String failureMessage = String.format("Ship to Ship cargoes must be different");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
				dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo());
				dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo());
				failures.add(dsd);
			}

			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {

				final VesselAssignmentType loadVesselSet = loadCargo.getVesselAssignmentType();
				final VesselAssignmentType dischargeVesselSet = dischargeCargo.getVesselAssignmentType();

				boolean problem = false;
				if (loadVesselSet instanceof VesselAvailability && dischargeVesselSet instanceof VesselAvailability) {
					if (loadVesselSet.equals(dischargeVesselSet)) {
						problem = true;
					}
				}

				else if (loadVesselSet instanceof CharterInMarket && dischargeVesselSet instanceof CharterInMarket) {
					if (loadVesselSet.equals(dischargeVesselSet)) {
						if (loadCargo.getSpotIndex() == dischargeCargo.getSpotIndex()) {
							problem = true;
						}
					}
				}
				if (problem) {

					final String failureMessage = String.format("Ship to Ship slots must be on different vessels");
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
					dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo());
					dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo());
					dsd.addEObjectAndFeature(loadCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
					dsd.addEObjectAndFeature(dischargeCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);

					failures.add(dsd);

				}
			}

		}

	}
}
