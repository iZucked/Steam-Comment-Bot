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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Check that the vessel / port combination on the slots assigned to a cargo wrt the [minVesselCapacity, maxVesselCapacity] bounds for a Port.
 */
public class VesselCapacityPortMinMaxVesselSizeValidator extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;

			final Vessel vessel = getVessel(cargo);
			if (vessel != null) {
				for (Slot<?> slot : cargo.getSlots()) {					
					Port port = slot.getPort();
					if (port != null && port.isSetMinVesselSize() && vessel.getVesselOrDelegateCapacity() < port.getMinVesselSize()) {
						statuses.add(createInvalidVesselPortAssignmentStatus("is less than", "min", ctx, vessel, slot, port, port.getMinVesselSize()));	
					}
					if (port != null && port.isSetMaxVesselSize() && vessel.getVesselOrDelegateCapacity() > port.getMaxVesselSize()) {
						statuses.add(createInvalidVesselPortAssignmentStatus("exceeds", "max", ctx, vessel, slot, port, port.getMaxVesselSize()));							
					}						
				}
			} else {
				return Activator.PLUGIN_ID;
			}
		}
		return Activator.PLUGIN_ID;
	}

	@NonNull
	private DetailConstraintStatusDecorator createInvalidVesselPortAssignmentStatus(final String violationMsg, final String violationType, 
			final IValidationContext ctx, final Vessel vessel, final Slot slot, final Port port, int vesselSizeBound) {
		final String message = String.format("Vessel %s cannot dock at port %s as vessel capacity (%d) %s port %s vessel size (%d).", 
				vessel.getName(), port.getName(), vessel.getVesselOrDelegateCapacity(), violationMsg, violationType, vesselSizeBound);
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		dcsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
		return dcsd;
	}
	
	/**
	 * Get the vessel from the vessel assignment type object.
	 * @param vat
	 * @return the vessel, or null if none assigned.
	 */
	@Nullable
	private Vessel getVessel(VesselAssignmentType vat) {
		if (vat instanceof VesselAvailability) {
			return ((VesselAvailability)vat).getVessel();
		}
		else if (vat instanceof CharterInMarketOverride) {
			return getVessel(((CharterInMarketOverride)vat).getCharterInMarket());
		}
		else if (vat instanceof CharterInMarket) {
			return ((CharterInMarket)vat).getVessel();
		}
		return null;
	}
	
	@Nullable
	private Vessel getVessel(final Cargo cargo) {
		if (cargo.getVesselAssignmentType() != null) {
			return getVessel(cargo.getVesselAssignmentType());
		}
		Vessel vessel = getLoadSlot(cargo).getNominatedVessel();
		if (vessel != null) {
			return vessel;
		}
		vessel = getDischargeSlot(cargo).getNominatedVessel();
		return vessel;
	}
	
	@NonNull
	private LoadSlot getLoadSlot(final @NonNull Cargo cargo) {
		for (final Slot<?> s : cargo.getSlots()) {
			if (s instanceof LoadSlot) {
				return (LoadSlot) s;
			}
		}
		throw new IllegalArgumentException(String.format("Cargo %s does not have a load slot.", cargo.getLoadName()));
	}
	
	@NonNull
	private DischargeSlot getDischargeSlot(final @NonNull Cargo cargo) {
		for (final Slot<?> s : cargo.getSlots()) {
			if (s instanceof DischargeSlot) {
				return (DischargeSlot) s;
			}
		}
		throw new IllegalArgumentException(String.format("Cargo %s does not have a discharge slot.", cargo.getLoadName()));
	}
}
