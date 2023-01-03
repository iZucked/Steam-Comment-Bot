/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.Month;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Validation constraint to check Panama vessel group waiting time parameters
 * and Panama booking codes on bookings set correctly. Checks: 1. default group
 * is present (one where vessel groups list is empty). 2. No group has a vessel
 * group parameters specified on the same vessel twice. 3. Checks Panama
 * bookings vessel + vessel groups are compatible. 4. Checks that no parameters
 * consist of all empty vessel groups
 * 
 * @author Patrick, FM
 * @version 2
 * @since 14/09/2021
 */
public class PanamaVesselGroupParametersConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof CanalBookings canalBookings) {
			List<VesselGroupCanalParameters> vesselGroupParameters = canalBookings.getVesselGroupCanalParameters();
			checkDefaultVesselGroupPresent(ctx, statuses, canalBookings, vesselGroupParameters);
			checkOnlyOneDefaultVesselGroupPresent(ctx, statuses, canalBookings, vesselGroupParameters);
			checkNoOverlappingVesselGroups(ctx, statuses, canalBookings, vesselGroupParameters);
			checkNoIncompatibleVesselBookingCodeBookings(ctx, statuses, canalBookings, vesselGroupParameters);
			checkNoEmptyVesselGroups(ctx, statuses, canalBookings, vesselGroupParameters);
			checkSeasonality(ctx, statuses, canalBookings, vesselGroupParameters);
		}
	}

	private void checkNoIncompatibleVesselBookingCodeBookings(@NonNull IValidationContext ctx, @NonNull List<IStatus> statuses, CanalBookings canalBookings,
			List<VesselGroupCanalParameters> vesselGroupParameters) {
		List<CanalBookingSlot> bookings = canalBookings.getCanalBookingSlots();

		for (CanalBookingSlot booking : bookings) {
			if (!isCompatibleVesselBookingCode(booking)) {
				addValidationError(ctx, statuses, booking, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS,
						"Vessel must be a member of vessel group parameters named as booking code.");
			} else if (booking.getBookingCode() == null) {
				addValidationError(ctx, statuses, booking, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, "Booking code on Panama booking must be set.");
			}
		}

	}

	private boolean isCompatibleVesselBookingCode(CanalBookingSlot booking) {
		if (booking.getVessel() == null) {
			// Unallocated so we don't care.
			return true;
		}
		AVesselSet<Vessel> assignedVessels = booking.getVessel();
		final Set<Vessel> vessels = SetUtils.getObjects(assignedVessels);
		if (booking.getBookingCode() == null || (booking.getBookingCode().getVesselGroup() == null || booking.getBookingCode().getVesselGroup().isEmpty())) {
			// If no booking code or vessel group is default, then no problem.
			return true;
		} else {
			// Otherwise we need to check the vessel(s) assigned are within the vessel group
			// on the booking code.
			final Set<Vessel> compatibleVessels = SetUtils.getObjects(booking.getBookingCode().getVesselGroup());
			for (Vessel vessel : vessels) {
				if (!compatibleVessels.contains(vessel)) {
					return false;
				}
			}
		}
		return true;
	}

	private void checkNoEmptyVesselGroups(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings,
			final List<VesselGroupCanalParameters> vesselGroupParameters) {
		final List<VesselGroupCanalParameters> parametersWithOnlyEmptyVesselGroups = getParametersWithOnlyEmptyVesselGroups(vesselGroupParameters);
		for (final VesselGroupCanalParameters vgParams : parametersWithOnlyEmptyVesselGroups) {
			addValidationError(ctx, statuses, vgParams, CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP, "Vessel groups must be non-empty.");
		}
	}

	private void checkNoOverlappingVesselGroups(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings,
			final List<VesselGroupCanalParameters> vesselGroupParameters) {
		VesselGroupCanalParameters vgParams = getOverlappingVesselGroups(vesselGroupParameters);
		if (vgParams != null) {
			addValidationError(ctx, statuses, vgParams, CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP,
					"Waiting time vessel groups must not contain common vessels for 2 different waiting times.");
		}
	}

	private void addValidationError(final IValidationContext ctx, final List<IStatus> statuses, final EObject eObject, final EStructuralFeature feature, final String msg) {
		final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus().withName("Panama canal parameters");
		statuses.add(baseFactory.copyName() //
				.withObjectAndFeature(eObject, feature) //
				.withMessage(msg) //
				.make(ctx));
	}

	private List<VesselGroupCanalParameters> getParametersWithOnlyEmptyVesselGroups(List<VesselGroupCanalParameters> vesselGroupParameters) {
		return vesselGroupParameters.stream() //
				.filter(vgParams -> !vgParams.getVesselGroup().isEmpty() && SetUtils.getObjects(vgParams.getVesselGroup()).isEmpty()) //
				.collect(Collectors.toList());
	}

	private VesselGroupCanalParameters getOverlappingVesselGroups(final List<VesselGroupCanalParameters> vesselGroupParameters) {
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

	private void checkDefaultVesselGroupPresent(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings,
			final List<VesselGroupCanalParameters> vesselGroupParameters) {
		if (!isDefaultVesselGroupPresent(vesselGroupParameters)) {
			addValidationError(ctx, statuses, canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, "A default booking code with no vessels must be defined.");
		}
	}

	private void checkOnlyOneDefaultVesselGroupPresent(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings,
			final List<VesselGroupCanalParameters> vesselGroupParameters) {
		if (!isOnlyOneDefaultVesselGroupPresent(vesselGroupParameters)) {
			addValidationError(ctx, statuses, canalBookings, CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP, "Only one default vessel group with no vessels can be defined.");
		}
	}

	private void checkSeasonality(final IValidationContext ctx, final List<IStatus> statuses, @NonNull final CanalBookings canalBookings,
			final List<VesselGroupCanalParameters> vesselGroupParameters) {
		final List<PanamaSeasonalityRecord> panamaSeasonalityRecords = canalBookings.getPanamaSeasonalityRecords();
		for (final VesselGroupCanalParameters vgcp : vesselGroupParameters) {
			final String name = vgcp.getName();
			final List<PanamaSeasonalityRecord> matchingRecords = panamaSeasonalityRecords.stream()//
					.filter(psr -> vgcp.equals(psr.getVesselGroupCanalParameter())).collect(Collectors.toList());
			if (matchingRecords.isEmpty()) {
				addValidationError(ctx, statuses, vgcp, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, //
						String.format("Vessel group %s has no seasonality record.", vgcp.getName()));
			}
			final List<PanamaSeasonalityRecord> matchingAnyRecords = matchingRecords.stream()//
					.filter(foo -> foo.getStartMonth() == 0).collect(Collectors.toList());
			final List<PanamaSeasonalityRecord> matchingMonthRecords = matchingRecords.stream()//
					.filter(foo -> foo.getStartMonth() > 0).collect(Collectors.toList());

			if (matchingAnyRecords.size() > 1) {
				addValidationError(ctx, statuses, matchingAnyRecords.get(0), CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER, //
						String.format("Only one \'ANY\' month can be set for each vessel group. Booking code: %s", name));
			}
			if (!matchingAnyRecords.isEmpty() && !matchingMonthRecords.isEmpty()) {
				addValidationError(ctx, statuses, matchingAnyRecords.get(0), CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER, //
						String.format("\'ANY\' month setting can not be used with a specific month boundary setting. Booking code: %s", name));
			}
			if (matchingMonthRecords.size() == 1) {
				addValidationError(ctx, statuses, matchingMonthRecords.get(0), CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER, //
						String.format("At least two specified boundary settings must be provided. Booking code: %s", name));
			}
			for (final PanamaSeasonalityRecord psr : matchingRecords) {
				final List<PanamaSeasonalityRecord> matchingDuplicates = matchingRecords.stream()//
						.filter(psr2 -> (psr != psr2 && psr.getStartDay() == psr2.getStartDay() //
								&& psr.getStartMonth() == psr2.getStartMonth() //
								&& psr.getStartMonth() != 0 //
								&& psr2.getStartMonth() != 0 //
								&& psr.getStartYear() == psr2.getStartYear()))//
						.collect(Collectors.toList());
				if (!matchingDuplicates.isEmpty()) {
					addValidationError(ctx, statuses, psr, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER, //
							String.format("Duplicate seasonality record. Booking code: %s", name));
				}
			}
		}

		for (final PanamaSeasonalityRecord psr : panamaSeasonalityRecords) {
			final String name;
			if (psr.getVesselGroupCanalParameter() == null) {
				addValidationError(ctx, statuses, psr, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER, //
						String.format("Seasonality record must have an assigned vessel group."));
				name = "<Unknown>";
			} else {
				name = psr.getVesselGroupCanalParameter().getName();
			}

			if (psr.getStartMonth() > 0) {
				final Month m = Month.of(psr.getStartMonth());
				if (psr.getStartDay() > m.maxLength()) {
					addValidationError(ctx, statuses, psr, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_DAY, //
							String.format("Seasonality record must have a feasible start day. Booking code: %s", name));
				} else if (psr.getStartDay() < 1) {
					addValidationError(ctx, statuses, psr, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_MONTH, //
							String.format("Seasonality record must have a positive value for the start day. Booking code: %s", name));
				}
			}
		}
	}

	private boolean isDefaultVesselGroupPresent(final List<VesselGroupCanalParameters> vesselGroupParameters) {
		return vesselGroupParameters.stream().map(VesselGroupCanalParameters::getVesselGroup).anyMatch(List::isEmpty);
	}

	private boolean isOnlyOneDefaultVesselGroupPresent(final List<VesselGroupCanalParameters> vesselGroupParameters) {
		int cnt = 0;
		for (var vgParams : vesselGroupParameters) {
			if (vgParams.getVesselGroup() == null) {
				cnt++;
			} else {
				if (vgParams.getVesselGroup().isEmpty()) {
					cnt++;
				}
			}
		}
		return (cnt <= 1);
	}
}
