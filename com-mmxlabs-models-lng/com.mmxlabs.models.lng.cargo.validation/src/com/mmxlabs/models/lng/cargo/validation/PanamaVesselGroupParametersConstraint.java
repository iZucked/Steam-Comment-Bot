package com.mmxlabs.models.lng.cargo.validation;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Validation constraint to check Panama vessel group waiting time parameters and Panama booking codes on bookings set correctly. Checks:
 * 1. default group is present (one with no vessels specified).
 * 2. No group has a vessel group parameters specified on the same vessel twice.
 * 3. Checks Panama bookings vessel + vessel groups are compatible.
 * @author Patrick
 */
public class PanamaVesselGroupParametersConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof CanalBookings) {
			CanalBookings canalBookings = (CanalBookings) target;
			EList<VesselGroupCanalParameters> vesselGroupParameters = canalBookings.getVesselGroupCanalParameters();
			checkDefaultVesselGroupPresent(ctx, statuses, canalBookings, vesselGroupParameters);
			checkOnlyOneDefaultVesselGroupPresent(ctx, statuses, canalBookings, vesselGroupParameters);
			checkNoOverlappingVesselGroups(ctx, statuses, canalBookings, vesselGroupParameters);
			checkNoIncompatibleVesselBookingCodeBookings(ctx, statuses, canalBookings, vesselGroupParameters);
		}	
		return Activator.PLUGIN_ID;
	}
	
	private void checkNoIncompatibleVesselBookingCodeBookings(@NonNull IValidationContext ctx, @NonNull List<IStatus> statuses, CanalBookings canalBookings,
			EList<VesselGroupCanalParameters> vesselGroupParameters) {
		EList<CanalBookingSlot> bookings = canalBookings.getCanalBookingSlots();
		
		for (CanalBookingSlot booking : bookings) {
			if (!isCompatibleVesselBookingCode(booking)) {
				addValidationError(ctx, statuses, booking, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, "Vessel must be a member of vessel group parameters named as booking code.");
			}
			else if (booking.getBookingCode() == null) {
				addValidationError(ctx, statuses, booking, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, "Booking code on Panama booking must be set.");
			}
		}
		
	}

	private boolean isCompatibleVesselBookingCode(CanalBookingSlot booking) {
		if (booking.getVessel() == null) {
			//Unallocated so we don't care.
			return true;
		}
		AVesselSet<Vessel> assignedVessels = booking.getVessel();
		final Set<Vessel> vessels = SetUtils.getObjects(assignedVessels);
		if (booking.getBookingCode() == null || (booking.getBookingCode().getVesselGroup() == null || booking.getBookingCode().getVesselGroup().isEmpty())) {
			//If no booking code or vessel group is default, then no problem.
			return true;
		}
		else {
			//Otherwise we need to check the vessel(s) assigned are within the vessel group on the booking code.
			final Set<Vessel> compatibleVessels = SetUtils.getObjects(booking.getBookingCode().getVesselGroup());
			for (Vessel vessel : vessels) { 
				if (!compatibleVessels.contains(vessel)) {
					return false;
				}
			}
		}
		return true;
	}

	private void checkNoOverlappingVesselGroups(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings, final EList<VesselGroupCanalParameters> vesselGroupParameters ) {
		VesselGroupCanalParameters vgParams = getOverlappingVesselGroups(vesselGroupParameters);
		if (vgParams != null) {
			addValidationError(ctx, statuses, vgParams, CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP, "Waiting time vessel groups must not contain common vessels for 2 different waiting times.");
		}
	}

	private void addValidationError(final IValidationContext ctx, final List<IStatus> statuses, final EObject eObject, 
			final EStructuralFeature feature,
			final String msg) {
		final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus().withName("Panama canal parameters");
		statuses.add(baseFactory.copyName() //
				.withObjectAndFeature(eObject, feature) //
				.withMessage(msg) //
				.make(ctx));
	}
	
	private VesselGroupCanalParameters getOverlappingVesselGroups(EList<VesselGroupCanalParameters> vesselGroupParameters) {
		final Set<Vessel> vessels = new HashSet<>();	
		for (var vgParams : vesselGroupParameters) {
			final Set<Vessel> vgVessels = SetUtils.getObjects(vgParams.getVesselGroup());
			if (!Collections.disjoint(vessels, vgVessels)) {
				return vgParams;
			}
			vessels.addAll(vgVessels);
		}		
		return null;
	}
	
	private void checkDefaultVesselGroupPresent(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings, final EList<VesselGroupCanalParameters> vesselGroupParameters ) {
		if (!isDefaultVesselGroupPresent(vesselGroupParameters)) {
			addValidationError(ctx, statuses, canalBookings, CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP, "A default vessel group with no vessels and default number of waiting days for Southbound and Northbound unbooked panama crossings must be defined.");
		}
	}

	private void checkOnlyOneDefaultVesselGroupPresent(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings, final EList<VesselGroupCanalParameters> vesselGroupParameters ) {
		if (!isOnlyOneDefaultVesselGroupPresent(vesselGroupParameters)) {
			addValidationError(ctx, statuses, canalBookings, CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP, "Only one default vessel group with no vessels and default waiting days can be defined.");
		}
	}
	
	private boolean isDefaultVesselGroupPresent(EList<VesselGroupCanalParameters> vesselGroupParameters) {
		for (var vgParams : vesselGroupParameters) {
			final Set<Vessel> vessels = SetUtils.getObjects(vgParams.getVesselGroup());
			if (vessels.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isOnlyOneDefaultVesselGroupPresent(EList<VesselGroupCanalParameters> vesselGroupParameters) {
		int cnt = 0;
		for (var vgParams : vesselGroupParameters) {
			if (vgParams.getVesselGroup() == null) {
				cnt++;
			}
			else {
				final Set<Vessel> vessels = SetUtils.getObjects(vgParams.getVesselGroup());
				if (vessels.isEmpty()) {
					cnt++;
				}
			}
		}
		return (cnt <= 1);
	}
}
