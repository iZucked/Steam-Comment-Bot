/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class UnbookedSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (!ScheduleModelValidationHelper.isMainScheduleModel(target)) {
			return;
		}
		if (!LicenseFeatures.isPermitted("features:panama-canal-bookings")) {
			return;
		}

//		if (target instanceof Journey && target.eContainer() instanceof Sequence) {
//			final Journey journey = (Journey) target;
//
//			Sequence sequence = (Sequence) target.eContainer();
//			if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
//				return Activator.PLUGIN_ID;
//			}
//
//			final RouteOption routeOption = journey.getRouteOption();
//			if (routeOption != null) {
//				if (routeOption == RouteOption.PANAMA) {
//					if (journey.getCanalBooking() == null) {
//						//Comment out this for now, as have a feeling it may make a return in the future...
//						// No Booking!
////						if (journey.getCanalBookingPeriod() == PanamaBookingPeriod.STRICT) {
////
////							DetailConstraintStatusFactory f = DetailConstraintStatusFactory.makeStatus() //
////									.withTag(ValidationConstants.TAG_EVALUATED_SCHEDULE) //
////									.withMessage("Panama canal voyage without a booking") //
////									.withObjectAndFeature(journey, SchedulePackage.Literals.JOURNEY__CANAL_BOOKING) //
////							;
////							String fromId = "<unknown";
////							String toId = "<unknown";
////							final Event prevEvent = journey.getPreviousEvent();
////							if (prevEvent instanceof SlotVisit) {
////								final SlotVisit slotVisit = (SlotVisit) prevEvent;
////								f = f.withObjectAndFeature(slotVisit.getSlotAllocation().getSlot(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
////							} else if (prevEvent instanceof VesselEventVisit) {
////								final VesselEventVisit slotVisit = (VesselEventVisit) prevEvent;
////								f = f.withObjectAndFeature(slotVisit.getVesselEvent(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
////							}
////							if (prevEvent != null) {
////								fromId = prevEvent.name();
////							}
////							Event nextEvent = journey.getNextEvent();
////							while (nextEvent != null && !(nextEvent instanceof PortVisit)) {
////								nextEvent = nextEvent.getNextEvent();
////							}
////							if (nextEvent != null) {
////								toId = nextEvent.name();
////							}
////
////							f = f.withName(String.format("Journey between %s and %s", fromId, toId));
////
////							statuses.add(f.withSeverity(IStatus.WARNING).make(ctx));
////						}
////
////					}
//				}
//			}
//		}
	}
}
