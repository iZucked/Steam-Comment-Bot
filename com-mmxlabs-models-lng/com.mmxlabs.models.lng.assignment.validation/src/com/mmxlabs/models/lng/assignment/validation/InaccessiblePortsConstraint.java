/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class InaccessiblePortsConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statues) {
		final EObject target = ctx.getTarget();
		if (target instanceof AssignableElement assignableElement) {
			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();

			List<APortSet<Port>> inaccessiblePorts = null;

			if (vesselAssignmentType instanceof VesselAvailability vesselAvailability) {
				final Vessel vessel = vesselAvailability.getVessel();
				if (vessel != null) {
					inaccessiblePorts = vessel.getVesselOrDelegateInaccessiblePorts();
				}
			} else if (vesselAssignmentType instanceof CharterInMarket charterInMarket) {
				final Vessel vessel = charterInMarket.getVessel();
				if (vessel != null) {
					inaccessiblePorts = vessel.getVesselOrDelegateInaccessiblePorts();
				}
			} else if (vesselAssignmentType instanceof CharterInMarketOverride charterInMarketOverride) {
				final CharterInMarket charterInMarket = charterInMarketOverride.getCharterInMarket();
				final Vessel vessel = charterInMarket.getVessel();
				if (vessel != null) {
					inaccessiblePorts = vessel.getInaccessiblePorts();
				}
			}

			if (inaccessiblePorts != null) {
				final Set<Port> inaccessiblePortSet = SetUtils.getObjects(inaccessiblePorts);
				if (!inaccessiblePortSet.isEmpty()) {
					EObject currentTarget = assignableElement;
					if (currentTarget instanceof Slot<?> slot) {
						if (slot.getCargo() != null) {
							currentTarget = slot.getCargo();
						} else {
							if (inaccessiblePortSet.contains(slot.getPort())) {
								final String msg = String.format("The port %s is not an accessible port for the assigned vessel", slot.getPort().getName());
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
								dsd.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
								dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
								statues.add(dsd);
							}
						}
					}

					if (currentTarget instanceof Cargo cargo) {
						for (final Slot<?> slot : cargo.getSlots()) {
							if (inaccessiblePortSet.contains(slot.getPort())) {
								final String msg = String.format("The port %s is not an accessible port for the assigned vessel", slot.getPort().getName());
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
								dsd.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
								dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
								statues.add(dsd);
							}
						}
					} else if (currentTarget instanceof VesselEvent vesselEvent) {
						if (inaccessiblePortSet.contains(vesselEvent.getPort())) {
							final String msg = String.format("The port %s is not an accessible port for the assigned vessel", vesselEvent.getPort().getName());
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
							dsd.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
							dsd.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_Port());
							statues.add(dsd);
						}
						if (vesselEvent instanceof CharterOutEvent) {
							final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
							if (inaccessiblePortSet.contains(charterOutEvent.getRelocateTo())) {
								final String msg = String.format("The port %s is not an accessible port for the assigned vessel", charterOutEvent.getRelocateTo().getName());
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
								dsd.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
								dsd.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getCharterOutEvent_RelocateTo());
								statues.add(dsd);
							}
						}
					}
				}
			}
		} else if (target instanceof Slot<?> targetSlot) {

			List<APortSet<Port>> inaccessiblePorts = null;
			final List<Slot<?>> slotsToValidate = new LinkedList<>();
			if (targetSlot instanceof LoadSlot loadSlot) {
				if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
					final Vessel nominatedVessel = loadSlot.getNominatedVessel();
					if (nominatedVessel != null) {
						inaccessiblePorts = nominatedVessel.getVesselOrDelegateInaccessiblePorts();
					}
				}
			}

			else if (targetSlot instanceof DischargeSlot dischargeSlot) {
				if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
					final Vessel nominatedVessel = dischargeSlot.getNominatedVessel();
					if (nominatedVessel != null) {
						inaccessiblePorts = nominatedVessel.getVesselOrDelegateInaccessiblePorts();
					}
				}
			}

			if (inaccessiblePorts != null) {
				final Set<Port> inaccessiblePortSet = SetUtils.getObjects(inaccessiblePorts);

				final Cargo cargo = targetSlot.getCargo();
				if (cargo != null) {
					slotsToValidate.addAll(cargo.getSlots());
				} else {
					slotsToValidate.add(targetSlot);
				}

				for (final Slot<?> slot : slotsToValidate) {
					if (inaccessiblePortSet.contains(slot.getPort())) {
						final String msg = String.format("The port %s is not an accessible port for the nominated vessel", slot.getPort().getName());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						if (slot == targetSlot) {
							dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
						}
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
						statues.add(dsd);
					}
				}

			}
		}
	}
}
