package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class STSCargoConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();
		IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();

		int severity = extraValidationContext.isValidatingClone() ? IStatus.WARNING : IStatus.ERROR;

		if (target instanceof Slot) {
			// final SeriesParser parser = getParser();
			final Slot slot = (Slot) target;

			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.getTransferFrom() != null) {
					final DischargeSlot transferFrom = loadSlot.getTransferFrom();
					validateAttriutes(ctx, loadSlot, transferFrom, failures, severity);

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
					validateAttriutes(ctx, transferTo, dischargeSlot, failures, severity);

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

	private void validateAttriutes(final IValidationContext ctx, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final List<IStatus> failures, int severity) {

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

}
