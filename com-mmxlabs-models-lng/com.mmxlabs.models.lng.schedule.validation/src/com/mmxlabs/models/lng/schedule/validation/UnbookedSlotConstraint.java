/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class UnbookedSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (!ScheduleModelValidationHelper.isMainScheduleModel(target)) {
			return Activator.PLUGIN_ID;
		}
		if (!LicenseFeatures.isPermitted("features:panama-canal-bookings")) {
			return Activator.PLUGIN_ID;
		}

		if (target instanceof Schedule) {
			Schedule schedule = (Schedule) target;

			final LNGScenarioModel scenarioModel = (LNGScenarioModel) extraContext.getRootObject();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			final CanalBookings canalBookings = cargoModel.getCanalBookings();
			if (canalBookings == null) {
				return Activator.PLUGIN_ID;
			}

			// if (scenarioModel.getPromptPeriodStart() != null && canalBookings != null) {
			// final LocalDate date = scenarioModel.getPromptPeriodStart();
			// final LocalDate strictBound = date.plusDays(canalBookings.getStrictBoundaryOffsetDays());
			// final LocalDate relaxedBound = date.plusDays(canalBookings.getRelaxedBoundaryOffsetDays());

			// int northboundExcessRelaxedBookings = 0;
			int southboundExcessRelaxedBookings = 0;
			for (Sequence sequence : schedule.getSequences()) {
				if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
					continue;
				}
				for (Event event : sequence.getEvents()) {
					if (event instanceof Journey) {
						Journey journey = (Journey) event;
						final RouteOption routeOption = journey.getRouteOption();
						if (routeOption != null) {
							if (routeOption == RouteOption.PANAMA) {
								if (journey.getCanalBooking() == null) {
									// No Booking!
									if (journey.getCanalBookingPeriod() == PanamaBookingPeriod.RELAXED) {
										if (journey.getCanalEntrance() == CanalEntry.SOUTHSIDE) {
											// ++northboundExcessRelaxedBookings;
										} else {
											++southboundExcessRelaxedBookings;
										}
									}
								}
							}
						}
					}
				}
			}

			// if (northboundExcessRelaxedBookings > canalBookings.getFlexibleBookingAmountNorthbound()) {
			// DetailConstraintStatusFactory f = DetailConstraintStatusFactory.makeStatus() //
			// .withPrefix("[Evaluated State]") //
			// .withMessage(String.format("Panama canal: There are %d flexible northbound voyages but only %d permitted.", northboundExcessRelaxedBookings,
			// canalBookings.getFlexibleBookingAmountNorthbound())) //
			// .withObjectAndFeature(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND) //
			// ;
			// statuses.add(f.withSeverity(IStatus.ERROR).make(ctx));
			// }
			if (southboundExcessRelaxedBookings > canalBookings.getFlexibleBookingAmountSouthbound()) {
				DetailConstraintStatusFactory f = DetailConstraintStatusFactory.makeStatus() //
						.withTag(ValidationConstants.TAG_EVALUATED_SCHEDULE) //
						.withMessage(String.format("Panama canal: There are %d unbooked southbound voyages but only %d are permitted (%s over).", southboundExcessRelaxedBookings,
								canalBookings.getFlexibleBookingAmountSouthbound(), southboundExcessRelaxedBookings - canalBookings.getFlexibleBookingAmountSouthbound())) //
						.withObjectAndFeature(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND) //
				;
				statuses.add(f.withSeverity(IStatus.ERROR).make(ctx));
			}

		}
		if (target instanceof Journey && target.eContainer() instanceof Sequence) {
			final Journey journey = (Journey) target;

			Sequence sequence = (Sequence) target.eContainer();
			if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
				return Activator.PLUGIN_ID;
			}

			final RouteOption routeOption = journey.getRouteOption();
			if (routeOption != null) {
				if (routeOption == RouteOption.PANAMA) {
					if (journey.getCanalBooking() == null) {
						// No Booking!
						if (journey.getCanalBookingPeriod() == PanamaBookingPeriod.STRICT || journey.getCanalBookingPeriod() == PanamaBookingPeriod.RELAXED) {

							DetailConstraintStatusFactory f = DetailConstraintStatusFactory.makeStatus() //
									.withTag(ValidationConstants.TAG_EVALUATED_SCHEDULE) //
									.withMessage("Panama canal voyage without a booking") //
									.withObjectAndFeature(journey, SchedulePackage.Literals.JOURNEY__CANAL_BOOKING) //
							;
							String fromId = "<unknown";
							String toId = "<unknown";
							final Event prevEvent = journey.getPreviousEvent();
							if (prevEvent instanceof SlotVisit) {
								final SlotVisit slotVisit = (SlotVisit) prevEvent;
								f = f.withObjectAndFeature(slotVisit.getSlotAllocation().getSlot(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
							} else if (prevEvent instanceof VesselEventVisit) {
								final VesselEventVisit slotVisit = (VesselEventVisit) prevEvent;
								f = f.withObjectAndFeature(slotVisit.getVesselEvent(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
							}
							if (prevEvent != null) {
								fromId = prevEvent.name();
							}
							Event nextEvent = journey.getNextEvent();
							while (nextEvent != null && !(nextEvent instanceof PortVisit)) {
								nextEvent = nextEvent.getNextEvent();
							}
							if (nextEvent != null) {
								toId = nextEvent.name();
							}

							f = f.withName(String.format("Journey between %s and %s", fromId, toId));

							if (journey.getCanalBookingPeriod() == PanamaBookingPeriod.STRICT) {
								// Northbound is only a warning
								int severity = journey.getCanalEntrance() == CanalEntry.SOUTHSIDE ? IStatus.WARNING : IStatus.ERROR;
								statuses.add(f.withSeverity(severity).make(ctx));
							} else if (journey.getCanalBookingPeriod() == PanamaBookingPeriod.RELAXED) {
								statuses.add(f.withSeverity(IStatus.WARNING).make(ctx));
							}
						}

					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
