/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class EmptyVesselRestrictionsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Slot) {
			final Slot slot = (Slot) target;
			checkSlot(ctx, slot, statuses);
		}

		return Activator.PLUGIN_ID;
	}
	
	/**
	 * Check slot2 can be paired to slot1 based on slot1's restrictions
	 * 
	 * @param ctx
	 * @param slot1
	 * @param slot2
	 * @param cargoName
	 * @param statuses
	 */
	private void checkSlot(final IValidationContext ctx, final Slot slot, final List<IStatus> statuses) {

		final List<AVesselSet<Vessel>> restrictedVesselsSets;

		boolean restrictedVesselsArePermissive = slot.getSlotOrDelegateVesselRestrictionsArePermissive();

		if (slot instanceof SpotSlot) {
			SpotSlot spotSlot = (SpotSlot) slot;
			SpotMarket market = spotSlot.getMarket();
			if (market != null) {
				restrictedVesselsArePermissive = market.isRestrictedVesselsArePermissive();
				restrictedVesselsSets = market.getRestrictedVessels();
			} else {
				restrictedVesselsArePermissive = false;
				restrictedVesselsSets = Collections.emptyList();
			}
		} else {
			restrictedVesselsSets = slot.getSlotOrDelegateVesselRestrictions();
		}
		
		if (restrictedVesselsSets.isEmpty() && restrictedVesselsArePermissive) {
			final String msg = "Empty allowed vessels list.";
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive());
			statuses.add(d);
		}
	}
}
