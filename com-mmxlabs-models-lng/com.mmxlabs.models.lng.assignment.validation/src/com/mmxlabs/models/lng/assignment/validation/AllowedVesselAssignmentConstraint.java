/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 # * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Constraint to check the Assigned Vessel is in the allowed vessel list, if specified.
 * 
 */
public class AllowedVesselAssignmentConstraint extends AbstractModelMultiConstraint {

	private static final Logger log = LoggerFactory.getLogger(AllowedVesselAssignmentConstraint.class);

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;

			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
			if (vesselAssignmentType == null) {
				if (assignableElement instanceof CharterOutEvent && ((CharterOutEvent) assignableElement).isOptional()) {
					return Activator.PLUGIN_ID;
				}
				if (assignableElement instanceof VesselEvent) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Vessel events must have a vessel assigned to them."));
					status.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
					failures.add(status);
					return Activator.PLUGIN_ID;
				} else {
					return Activator.PLUGIN_ID;
				}
			}

			final Set<EObject> targets = new HashSet<>();
			if (assignableElement instanceof Cargo) {
				final Cargo cargo = (Cargo) assignableElement;
				targets.addAll(cargo.getSlots());

			} else {
				targets.add(assignableElement);
			}

			for (final EObject target : targets) {
				List<AVesselSet<Vessel>> allowedVessels = null;
				boolean isVesselRestrictionsPermissive = false;
				
				if (target instanceof Slot) {
					final Slot slot = (Slot) target;
					allowedVessels = slot.getSlotOrDelegateVesselRestrictions();
					isVesselRestrictionsPermissive = slot.getSlotOrDelegateVesselRestrictionsArePermissive();
					
				} else if (target instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) target;
					allowedVessels = vesselEvent.getAllowedVessels();
					isVesselRestrictionsPermissive = !allowedVessels .isEmpty();
					
				}

				if (allowedVessels == null || allowedVessels.isEmpty()) {
					continue;
				}

				// Expand out VesselGroups
				final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<>();
				for (final AVesselSet<Vessel> s : allowedVessels) {
					if (s instanceof Vessel) {
						expandedVessels.add(s);
					} else {
						// This is ok as other impl (VesselGroup and VesselTypeGroup) only permit contained Vessels
						expandedVessels.addAll(SetUtils.getObjects(s));
					}
				}

				Vessel vesselAssignment = null;
				if (vesselAssignmentType instanceof VesselAvailability) {
					final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
					vesselAssignment = vesselAvailability.getVessel();
				} else if (vesselAssignmentType instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
					vesselAssignment = charterInMarket.getVessel();
				} else if (vesselAssignmentType instanceof CharterInMarketOverride) {
					final CharterInMarketOverride charterInMarketOverride = (CharterInMarketOverride) vesselAssignmentType;
					final CharterInMarket charterInMarket = charterInMarketOverride.getCharterInMarket();
					vesselAssignment = charterInMarket.getVessel();
				} else {
					log.error("Assignment is not a VesselAvailability or CharterInMarket - unable to validate");
					return Activator.PLUGIN_ID;
				}

				boolean permitted = false;
				if (expandedVessels.contains(vesselAssignment) == isVesselRestrictionsPermissive) {
					permitted = true;
				}

				if (!permitted) {

					final String message;

					if (target instanceof Slot) {
						message = String.format("Slot '%s': Assignment '%s' is not in the allowed vessels list.", ((Slot) target).getName(), vesselAssignment.getName());
					} else if (target instanceof VesselEvent) {
						message = String.format("Vessel Event '%s': Assignment requires vessel(s) not in the allowed vessels list.", ((VesselEvent) target).getName());
					} else {
						throw new IllegalStateException("Unexpected code branch.");
					}
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					failure.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
					if (target instanceof Cargo) {
						failure.addEObjectAndFeature(target, CargoPackage.eINSTANCE.getSlot_RestrictedVessels());
					} else if (target instanceof VesselEvent) {
						failure.addEObjectAndFeature(target, CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels());
					}

					failures.add(failure);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
