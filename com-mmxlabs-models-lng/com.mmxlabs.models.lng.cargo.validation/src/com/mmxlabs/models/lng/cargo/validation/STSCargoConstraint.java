package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class STSCargoConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();
		final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();

		final int severity = extraValidationContext.isValidatingClone() ? IStatus.WARNING : IStatus.ERROR;

		if (target instanceof Slot) {
			// final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;

			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.getTransferFrom() != null) {
					final DischargeSlot transferFrom = loadSlot.getTransferFrom();
					validateAttributes(ctx, loadSlot, transferFrom, failures, severity);
					validateSlotPlacements(ctx, loadSlot, transferFrom, failures, severity);

					final Cargo cargo = ((DischargeSlot) extraValidationContext.getOriginal(transferFrom)).getCargo();
					if (cargo != null) {
						final Slot s = cargo.getSortedSlots().get(0);
						if (s instanceof LoadSlot) {
							final double cv = ((LoadSlot) s).getSlotOrPortCV();
							if (cv != loadSlot.getSlotOrPortCV()) {
								final String failureMessage = String.format("Ship to Ship %s must be in sync", "CV");
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
								dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
								dsd.addEObjectAndFeature(s, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
								failures.add(dsd);
							}
						}
					}
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (dischargeSlot.getTransferTo() != null) {
					final LoadSlot transferTo = dischargeSlot.getTransferTo();
					validateAttributes(ctx, transferTo, dischargeSlot, failures, severity);
					validateSlotPlacements(ctx, transferTo, dischargeSlot, failures, severity);

					final Cargo cargo = dischargeSlot.getCargo();
					if (cargo != null) {
						final Slot s = cargo.getSortedSlots().get(0);
						if (s instanceof LoadSlot) {
							final double cv = ((LoadSlot) s).getSlotOrPortCV();
							if (cv != transferTo.getSlotOrPortCV()) {
								final String failureMessage = String.format("Ship to Ship %s must be in sync", "CV");
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
								dsd.addEObjectAndFeature(transferTo, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
								dsd.addEObjectAndFeature(s, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
								failures.add(dsd);
							}
						}
					}
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

		if (!Equality.isEqual(loadSlot.getWindowStart(), dischargeSlot.getWindowStart())) {
			final String failureMessage = String.format("Ship to Ship %s must be in sync", "Window Start");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
			dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
			failures.add(dsd);
		}

		if (!Equality.isEqual(loadSlot.getWindowStartTime(), dischargeSlot.getWindowStartTime())) {
			final String failureMessage = String.format("Ship to Ship %s must be in sync", "Window Start Time");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime());
			dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime());
			failures.add(dsd);
		}

		if (!Equality.isEqual(loadSlot.getDuration(), dischargeSlot.getDuration())) {
			final String failureMessage = String.format("Ship to Ship %s must be in sync", "duration");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Duration());
			dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Duration());
			failures.add(dsd);
		}

		if (!Equality.isEqual(loadSlot.getPort(), dischargeSlot.getPort())) {
			final String failureMessage = String.format("Ship to Ship %s must be in sync", "Port");
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
			dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Port());
			dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
			failures.add(dsd);
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

	private void validateSlotPlacements(final IValidationContext ctx, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final List<IStatus> failures, final int severity) {

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

			final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
			final MMXRootObject rootObject = extraValidationContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {

				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final AssignmentModel assignmentModel = lngScenarioModel.getPortfolioModel().getAssignmentModel();

				final ElementAssignment loadAssignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, loadCargo);
				final ElementAssignment dischargeAssignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, dischargeCargo);

				if (loadAssignment != null && dischargeAssignment != null) {

					final AVesselSet<Vessel> loadVesselSet = loadAssignment.getAssignment();
					final AVesselSet<Vessel> dischargeVesselSet = dischargeAssignment.getAssignment();

					boolean problem = false;
					if (loadVesselSet instanceof Vessel && dischargeVesselSet instanceof Vessel) {
						if (loadVesselSet.equals(dischargeVesselSet)) {
							problem = true;
						}
					}

					else if (loadVesselSet instanceof VesselClass && dischargeVesselSet instanceof VesselClass) {
						if (loadAssignment.getSpotIndex() == dischargeAssignment.getSpotIndex()) {
							problem = true;
						}
					}
					if (problem) {

						final String failureMessage = String.format("Ship to Ship slots must be on different vessels");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), severity);
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo());
						dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo());
						dsd.addEObjectAndFeature(loadAssignment, AssignmentPackage.Literals.ELEMENT_ASSIGNMENT__ASSIGNMENT);
						dsd.addEObjectAndFeature(dischargeAssignment, AssignmentPackage.Literals.ELEMENT_ASSIGNMENT__ASSIGNMENT);

						failures.add(dsd);

					}
				}

			}

		}
	}
}
