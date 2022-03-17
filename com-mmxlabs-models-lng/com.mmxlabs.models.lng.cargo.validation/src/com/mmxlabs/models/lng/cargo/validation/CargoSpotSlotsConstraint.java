/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Check that there are not more than one spot slot and not less than one real
 * slot
 * 
 * @author FM
 * 
 */
public class CargoSpotSlotsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof Cargo cargo) {

			List<Slot<?>> sortedSlots = cargo.getSortedSlots();
			// Count how many spot and real slots we have
			int spotSlotsCount = 0;
			int realSlotsCount = 0;
			for (final Slot<?> slot : sortedSlots) {
				if (slot instanceof SpotSlot) {
					spotSlotsCount++;
				} else if (slot != null) {
					realSlotsCount++;
				}
			}

			// Usual situations are realSlotsCount >= 1 and spotSlotsCount <= 1
			if (spotSlotsCount > 1) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName() + "' - Cargo has too many spot slots. Maximum allowed is one!"));
				dsd.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_Slots());
				failures.add(dsd);
			}
			if (realSlotsCount < 1) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName() + "' - Cargo should have at least one real slot!"));
				dsd.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_Slots());
				failures.add(dsd);
			}
		}
	}
}
