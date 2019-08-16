/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.cargo.util.IShippingDaysRestrictionSpeedProvider;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.rcp.common.ServiceHelper;

/**
 * Check that the end of any cargo's discharge window is not before the start of its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoDateConstraint extends AbstractModelMultiConstraint {

	private static final String DATE_ORDER_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_order";
	private static final String TRAVEL_TIME_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_travel_time";
	private static final String AVAILABLE_TIME_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_available_time";
	private static final String NON_SHIPPED_TRAVEL_TIME_ID = "com.mmxlabs.models.lng.cargo.validation.CargoDateConstraint.cargo_non_shipped_travel_time";

	/**
	 * This is the maximum sensible amount of travel time in a cargo, in days
	 */
	private static final int SENSIBLE_TRAVEL_TIME = 160;

	/**
	 * Validate that the available time is not negative.
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private void validateSlotOrder(final IValidationContext ctx, final Cargo cargo, final Slot<?> slot, final long availableTime, final List<IStatus> failures) {
		if (availableTime < 0) {
			final int severity = IStatus.ERROR;
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(" [Cargo|" + cargo.getLoadName() + "] Load is after discharge (note timezone)."), severity);
			status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
			status.setTag(ValidationConstants.TAG_TRAVEL_TIME);

			failures.add(status);
		}
	}

	private String formatHours(final long hours) {
		if (hours < 24L) {
			if (hours == 1) {
				return hours + " hour";
			} else {
				return hours + " hours";
			}
		} else {
			final long remainderHours = hours % 24L;
			final long days = hours / 24L;
			return days + " day" + (days > 1 ? "s" : "") + (remainderHours > 0 ? (", " + remainderHours + " hour" + (remainderHours > 1 ? "s" : "")) : "");
		}
	}

	/**
	 * Validate that the available time is enough to get from A to B, if it's not negative
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private void validateSlotTravelTime(final IValidationContext ctx, final IExtraValidationContext extraContext, final Cargo cargo, final Slot<?> from, final Slot<?> to, final long availableTime,
			final List<IStatus> failures) {
		// Skip for FOB/DES cargoes.
		if (cargo.getCargoType() != CargoType.FLEET) {
			return;
		}

		if (availableTime >= 0) {
			final MMXRootObject scenario = extraContext.getRootObject();
			if (scenario instanceof LNGScenarioModel) {

				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;
				double maxSpeedKnots = 0.0;
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
				final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);

				final Set<Vessel> usedVessels = new HashSet<>();

				for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
					final Vessel vessel = va.getVessel();
					if (vessel != null) {
						usedVessels.add(vessel);
					}
				}
				for (final CharterInMarket charterCostModel : spotMarketsModel.getCharterInMarkets()) {
					if (charterCostModel.getCharterInRate() != null && charterCostModel.getSpotCharterCount() > 0) {
						usedVessels.add(charterCostModel.getVessel());
					}
				}
				usedVessels.remove(null);

				if (usedVessels.isEmpty()) {
					// Cannot perform our validation, so return
					return;
				}

				for (final Vessel vessel : usedVessels) {
					maxSpeedKnots = Math.max(vessel.getVesselOrDelegateMaxSpeed(), maxSpeedKnots);
				}

				@NonNull
				final ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
				final Integer minTime = CargoTravelTimeUtils.getFobMinTimeInHours(from, to, cargo.getSortedSlots().get(0).getWindowStart(), cargo.getVesselAssignmentType(),
						ScenarioModelUtil.getPortModel(lngScenarioModel), maxSpeedKnots, modelDistanceProvider);
				int severity = IStatus.ERROR;
				if (minTime == null) {
					// distance line is missing
					// TODO customise message for this case.
					// seems like a waste to run the same code twice
					final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName() + "'", "infinity", formatHours(availableTime));
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, severity);
					dsd.addEObjectAndFeature(from, CargoPackage.eINSTANCE.getSlot_Port());

					failures.add(dsd);
				} else {
					if (minTime > availableTime) {
						// If difference is within the slot flex, then downgrade to warning.
						final long diff = minTime - availableTime;
						// We want to sum the negative flex on the from to the positive flex on the to - this gives the sum additional flex on top of the windows.
						final int totalFlex = -from.getWindowFlex() + to.getWindowFlex();
						if (diff < totalFlex) {
							severity = IStatus.WARNING;
						}
						final int finalSeverity = (cargo.isAllowRewiring()) ? IStatus.WARNING : severity;
						final String extraInfo = cargo.isAllowRewiring() ? "." : " - and cargo is locked.";
						final String message = String.format("'%s': Laden leg to %s is too long by %s (%s vs. %s available)" + extraInfo, from.getName(), to.getPort().getName(),
								TravelTimeUtils.formatShortHours(minTime - availableTime), TravelTimeUtils.formatShortHours(minTime), TravelTimeUtils.formatShortHours(availableTime));

						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, finalSeverity);
						dsd.addEObjectAndFeature(from, CargoPackage.eINSTANCE.getSlot_WindowStart());
						dsd.addEObjectAndFeature(to, CargoPackage.eINSTANCE.getSlot_WindowStart());
						dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);

						failures.add(dsd);
					}
				}
			}
		}
	}

	/**
	 * Validate that a ridiculous amount of time has not been allocated
	 * 
	 * @param ctx
	 * @param cargo
	 * @param availableTime
	 * @return
	 */
	private void validateSlotAvailableTime(final IValidationContext ctx, final Cargo cargo, final Slot<?> slot, final long availableTime, final List<IStatus> failures) {
		if ((availableTime / 24) > SENSIBLE_TRAVEL_TIME) {
			final int severity = IStatus.WARNING;
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(
							String.format("[Cargo|%s] Travel time is excessive (%d days); %d is a sensible maximum.", "'" + cargo.getLoadName() + "'", availableTime / 24L, SENSIBLE_TRAVEL_TIME)),
					severity);
			status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
			status.setTag(ValidationConstants.TAG_TRAVEL_TIME);

			failures.add(status);
		}
	}

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof Cargo) {
			final String constraintID = ctx.getCurrentConstraintId();
			final Cargo cargo = (Cargo) object;
			if (cargo.getCargoType().equals(CargoType.FLEET)) {

				Slot<?> prevSlot = null;
				for (final Slot<?> slot : cargo.getSortedSlots()) {
					if (prevSlot != null) {
						final Port loadPort = prevSlot.getPort();
						final Port dischargePort = slot.getPort();
						if ((loadPort != null) && (dischargePort != null)) {

							final ZonedDateTime windowEndWithSlotOrPortTime = slot.getSchedulingTimeWindow().getEndWithFlex();
							final ZonedDateTime windowStartWithSlotOrPortTime = prevSlot.getSchedulingTimeWindow().getStart();

							if (windowEndWithSlotOrPortTime != null && windowStartWithSlotOrPortTime != null) {

								final int availableTime = Hours.between(windowStartWithSlotOrPortTime, windowEndWithSlotOrPortTime) - prevSlot.getSchedulingTimeWindow().getDuration();

								if (constraintID.equals(DATE_ORDER_ID)) {
									validateSlotOrder(ctx, cargo, prevSlot, availableTime, failures);
								} else if (constraintID.equals(TRAVEL_TIME_ID)) {
									validateSlotTravelTime(ctx, extraContext, cargo, prevSlot, slot, availableTime, failures);
								} else if (constraintID.equals(AVAILABLE_TIME_ID)) {
									validateSlotAvailableTime(ctx, cargo, prevSlot, availableTime, failures);
								}
							}
						}
					}
					prevSlot = slot;
				}
			} else if (constraintID.equals(NON_SHIPPED_TRAVEL_TIME_ID)) {
				// Divertible DES and FOB Sale
				if (cargo.getSortedSlots().size() == 2) {
					final LoadSlot loadSlot = (LoadSlot) cargo.getSortedSlots().get(0);
					final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSortedSlots().get(1);
					if (loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE //
							|| dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
						validateNonShippedSlotTravelTime(ctx, extraContext, cargo, loadSlot, dischargeSlot, failures);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void validateNonShippedSlotTravelTime(final IValidationContext ctx, final IExtraValidationContext extraContext, final Cargo cargo, final LoadSlot from, final DischargeSlot to,
			final List<IStatus> failures) {

		Vessel lVessel = null;
		if (from.isDESPurchase()) {
			lVessel = from.getNominatedVessel();
		} else if (to.isFOBSale()) {
			lVessel = to.getNominatedVessel();
		}
		if (lVessel == null) {
			return;
		}

		final Vessel vessel = lVessel;
		final Integer windowLength = getLadenMaxWindow(from, to);
		if (windowLength == null) {
			return;
		}
		if (windowLength < 0) {
			final int severity = IStatus.ERROR;
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus("[Cargo|" + cargo.getLoadName() + "] purchase is after sale"), severity);
			status.addEObjectAndFeature(from, CargoPackage.eINSTANCE.getSlot_WindowStart());
			status.addEObjectAndFeature(to, CargoPackage.eINSTANCE.getSlot_WindowStart());
			status.setTag(ValidationConstants.TAG_TRAVEL_TIME);

			failures.add(status);
		} else {
			ServiceHelper.withCheckedServiceConsumer(IShippingDaysRestrictionSpeedProvider.class, shippingDaysSpeedProvider -> {

				final LNGScenarioModel scenarioModel = getScenarioModel(extraContext);
				if (scenarioModel != null) {
					final int travelTime;
					if (from.isDESPurchase()) {
						travelTime = CargoTravelTimeUtils.getDivertibleDESMinRouteTimeInHours(from, from, to, shippingDaysSpeedProvider, ScenarioModelUtil.getPortModel(scenarioModel), vessel,
								CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, from, vessel, true),
								extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class));
					} else {
						travelTime = CargoTravelTimeUtils.getDivertibleFOBMinRouteTimeInHours(to, from, to, shippingDaysSpeedProvider, ScenarioModelUtil.getPortModel(scenarioModel), vessel,
								CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, from, vessel, true),
								extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class));
					}
					int slotDur = from.getSchedulingTimeWindow().getDuration();
					final boolean isCP = from.isWindowCounterParty();
					if (travelTime + from.getSchedulingTimeWindow().getDuration() > windowLength) {
						// final String message = String.format("Purchase %s is being shipped to %s but the laden leg (%s travel, %s loading) is greater than the shortest journey by %s.",
						// from.getName(), to.getPort().getName(), TravelTimeUtils.formatShortHours(travelTime),TravelTimeUtils.formatShortHours(slotDur),
						// TravelTimeUtils.formatHours((travelTime + slotDur) - windowLength));
						final String message = String.format("Purchase '%s': Laden leg to %s is too long: %s " + (isCP ? "C/P window and " : "")  + "loading, %s travel is %s more than the %s available.", from.getName(),
								to.getPort().getName(), TravelTimeUtils.formatShortHours(slotDur), TravelTimeUtils.formatShortHours(travelTime),
								TravelTimeUtils.formatShortHours((travelTime + slotDur) - windowLength), TravelTimeUtils.formatShortHours(windowLength));
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.WARNING);
						dsd.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargoModel_Cargoes());
						dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);

						failures.add(dsd);
					}
				}
			});
		}

	}

	private Integer getLadenMaxWindow(final Slot<?> startSlot, final Slot<?> endSlot) {
		final ZonedDateTime dateStart = startSlot.getSchedulingTimeWindow().getStart();
		final ZonedDateTime dateEnd = endSlot.getSchedulingTimeWindow().getEndWithFlex();

		if (dateStart != null && dateEnd != null) {
			return Hours.between(dateStart, dateEnd);
		} else {
			return null;
		}
	}

	public static @Nullable LNGScenarioModel getScenarioModel(final @NonNull IExtraValidationContext extraContext) {
		final MMXRootObject scenario = extraContext.getRootObject();
		if (scenario instanceof LNGScenarioModel) {
			return (LNGScenarioModel) scenario;
		}
		return null;
	}
}
