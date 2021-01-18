/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.cargo.util.IShippingDaysRestrictionSpeedProvider;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.rcp.common.ServiceHelper;

/**
 * 
 */
public class ShippingDaysRestrictionConstraint extends AbstractModelMultiConstraint {

	private static final int MAX_SHIPPING_DAYS = 90;

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (!(extraContext.getContainer(object) instanceof CargoModel)) {
			return Activator.PLUGIN_ID;
		}

		@NonNull
		ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
		// Valid slot data checks
		if (object instanceof Slot) {
			if (object instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) object;
				if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {
					if (loadSlot.getSlotOrDelegateShippingDaysRestriction() > MAX_SHIPPING_DAYS) {
						final String message = String.format("DES Purchase|%s shipping days restriction is too big.", loadSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.WARNING);
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
						dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);

						failures.add(dsd);
					} else if (loadSlot.getSlotOrDelegateShippingDaysRestriction() == 0) {
						final String message = String.format("DES Purchase|%s shipping days restriction is set to zero - unable to ship anywhere!", loadSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.WARNING);
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
						dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);

						failures.add(dsd);
					}
				} else if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DEST_ONLY) {

					final Cargo cargo = loadSlot.getCargo();
					if (cargo != null) {
						for (final Slot slot : cargo.getSlots()) {
							if (slot instanceof DischargeSlot) {
								final DischargeSlot dischargeSlot = (DischargeSlot) slot;
								if (loadSlot.getPort() != dischargeSlot.getPort()) {
									final String message = String.format("DES Purchase|%s is not divertible, but is linked to a DES Sale at a different discharge port", loadSlot.getName());
									final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.ERROR);
									dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Port());
									failures.add(dsd);
								}
							}
						}
					}
				}
			} else if (object instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) object;
				// if (SlotClassifier.classify(dischargeSlot) == SlotType.FOB_Sale_AnyLoadPort) {
				// if (dischargeSlot.getShippingDaysRestriction() > MAX_SHIPPING_DAYS) {
				// final String message = String.format("FOB Sale|%s shipping days restriction is too big.", dischargeSlot.getName());
				// final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
				// final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.WARNING);
				// dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
				// failures.add(dsd);
				// } else if (dischargeSlot.getShippingDaysRestriction() == 0) {
				// final String message = String.format("FOB Sale|%s shipping days restriction is set to zero - unable to ship anywhere!", dischargeSlot.getName());
				// final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
				// final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.WARNING);
				// dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
				// failures.add(dsd);
				// }
				// } else
				if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.SOURCE_ONLY) {
					final Cargo cargo = dischargeSlot.getCargo();
					if (cargo != null) {
						for (final Slot<?> slot : cargo.getSlots()) {
							if (slot instanceof LoadSlot) {
								final LoadSlot loadSlot = (LoadSlot) slot;
								if (loadSlot.getPort() != dischargeSlot.getPort()) {
									final String message = String.format("FOB Sale|%s is not divertible, but is linked to a FOB Purchase at a different load port", dischargeSlot.getName());
									final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.ERROR);
									dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
									failures.add(dsd);
								}
							}
						}
					}
				}
			}
		}

		// Shipping checks
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			if (cargo.getCargoType() != CargoType.FLEET) {
				final MMXRootObject scenario = extraContext.getRootObject();
				if (scenario instanceof LNGScenarioModel) {

					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;

					if (cargo.getCargoType() == CargoType.DES) {

						return ServiceHelper.withOptionalService(IShippingDaysRestrictionSpeedProvider.class, shippingDaysSpeedProvider -> {

							LoadSlot desPurchase = null;
							DischargeSlot dischargeSlot = null;
							for (final Slot s : cargo.getSlots()) {
								if (s instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) s;
									if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {
										desPurchase = loadSlot;
									}
								} else if (s instanceof DischargeSlot) {
									dischargeSlot = (DischargeSlot) s;
								}
							}
							// Found a slot to validate
							if (desPurchase != null && dischargeSlot != null) {

								if (desPurchase.getNominatedVessel() != null) {
									final Vessel vessel = desPurchase.getNominatedVessel();

									if (vessel == null) {
										return Activator.PLUGIN_ID;
									}

									final int loadDurationInHours = desPurchase.getSchedulingTimeWindow().getDuration();
									final int dischargeDurationInHours = dischargeSlot.getSchedulingTimeWindow().getDuration();

									@NonNull
									PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
									final int ladenTravelTimeInHours = CargoTravelTimeUtils.getDivertibleDESMinRouteTimeInHours(desPurchase, desPurchase, dischargeSlot, shippingDaysSpeedProvider,
											portModel, vessel, CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, desPurchase, vessel, true), modelDistanceProvider);
									final int ballastTravelTimeInHours = CargoTravelTimeUtils.getDivertibleDESMinRouteTimeInHours(desPurchase, dischargeSlot, desPurchase, shippingDaysSpeedProvider,
											portModel, vessel, CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, desPurchase, vessel, false), modelDistanceProvider);

									// Calculate minimum time due to slot windows
									final int ladenMaxWindowInHours;
									final int ladenMinWindowInHours;
									{
										// TODO: check overlaps
										final ZonedDateTime loadDateStart = desPurchase.getSchedulingTimeWindow().getStart();
										final ZonedDateTime loadDateEnd = desPurchase.getSchedulingTimeWindow().getEnd();
										final ZonedDateTime dischargeDateStart = dischargeSlot.getSchedulingTimeWindow().getStart();
										final ZonedDateTime dischargeDateEnd = dischargeSlot.getSchedulingTimeWindow().getEnd();

										if (loadDateStart != null && dischargeDateEnd != null) {
											ladenMaxWindowInHours = Math.max(0, Hours.between(loadDateStart, dischargeDateEnd) - (loadDurationInHours));
										} else {
											return Activator.PLUGIN_ID;
										}

										if (loadDateEnd != null && dischargeDateStart != null) {
											// There could be an overlap
											// Note: loadDateStart is the value used in the ShippingHoursRestrictionChecker
											ladenMinWindowInHours = Math.max(0, Hours.between(loadDateStart, dischargeDateStart) - (loadDurationInHours));
										} else {
											return Activator.PLUGIN_ID;
										}
									}

									if (ladenMaxWindowInHours < 0) {
										final String message = String.format("DES Purchase|%s available laden travel time is negative!", desPurchase.getName());
										final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
										final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
										dsd.addEObjectAndFeature(desPurchase, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
										failures.add(dsd);
										dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);
										// No point going further!
										return Activator.PLUGIN_ID;
									}

									// Smallest amount of time permitted between slots
									final int ladenTimeInHours = Math.max(ladenTravelTimeInHours, ladenMinWindowInHours);

									// Total min travel time.
									final int totalRoundTripTimeInHours = loadDurationInHours + ladenTimeInHours + dischargeDurationInHours + ballastTravelTimeInHours;

									if (totalRoundTripTimeInHours > desPurchase.getSlotOrDelegateShippingDaysRestriction() * 24) {
										final String message = String.format(
												"DES Purchase|%s is paired with a sale at %s. However the round trip time (%s) is greater than the permitted restriction (%s) by (%s).",
												desPurchase.getName(), dischargeSlot.getPort().getName(), TravelTimeUtils.formatHours(totalRoundTripTimeInHours),
												TravelTimeUtils.formatHours(desPurchase.getSlotOrDelegateShippingDaysRestriction() * 24),
												TravelTimeUtils.formatHours(totalRoundTripTimeInHours - desPurchase.getSlotOrDelegateShippingDaysRestriction() * 24));
										final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
										final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
										dsd.addEObjectAndFeature(desPurchase, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
										dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);

										failures.add(dsd);
									}
								}
							}
							return Activator.PLUGIN_ID;
						});
					} else if (cargo.getCargoType() == CargoType.FOB) {

						// FOB?
						return ServiceHelper.withOptionalService(IShippingDaysRestrictionSpeedProvider.class, shippingDaysSpeedProvider -> {

							LoadSlot fobPurchase = null;
							DischargeSlot fobSale = null;
							for (final Slot s : cargo.getSlots()) {
								if (s instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) s;
									if (SlotClassifier.classify(loadSlot) == SlotType.FOB_Buy) {
										fobPurchase = loadSlot;
									}
								} else if (s instanceof DischargeSlot) {
									if (SlotClassifier.classify(s) == SlotType.FOB_Sale_AnyLoadPort) {
										fobSale = (DischargeSlot) s;
									}
								}
							}
							// Found a slot to validate
							if (fobPurchase != null && fobSale != null) {

								{
									final Vessel vessel = fobSale.getNominatedVessel();

									if (vessel == null) {
										return Activator.PLUGIN_ID;
									}

									final int loadDurationInHours = fobPurchase.getSchedulingTimeWindow().getDuration();
									final int dischargeDurationInHours = fobSale.getSchedulingTimeWindow().getDuration();

									@NonNull
									PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
									final int ladenTravelTimeInHours = CargoTravelTimeUtils.getDivertibleFOBMinRouteTimeInHours(fobSale, fobPurchase, fobSale, shippingDaysSpeedProvider, portModel,
											vessel, CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, fobSale, vessel, true), modelDistanceProvider);

									final int ballastTravelTimeInHours = CargoTravelTimeUtils.getDivertibleFOBMinRouteTimeInHours(fobSale, fobSale, fobPurchase, shippingDaysSpeedProvider, portModel,
											vessel, CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, fobSale, vessel, false), modelDistanceProvider);

									// Calculate minimum time due to slot windows
									final int ladenMaxWindowInHours;
									final int ladenMinWindowInHours;
									{
										// TODO: check overlaps
										final ZonedDateTime loadDateStart = fobPurchase.getSchedulingTimeWindow().getStart();
										final ZonedDateTime loadDateEnd = fobPurchase.getSchedulingTimeWindow().getEnd();
										final ZonedDateTime dischargeDateStart = fobSale.getSchedulingTimeWindow().getStart();
										final ZonedDateTime dischargeDateEnd = fobSale.getSchedulingTimeWindow().getEnd();

										if (loadDateStart != null && dischargeDateEnd != null) {
											ladenMaxWindowInHours = Math.max(0, Hours.between(loadDateStart, dischargeDateEnd) - (loadDurationInHours));
										} else {
											return Activator.PLUGIN_ID;
										}

										if (loadDateEnd != null && dischargeDateStart != null) {
											// There could be an overlap
											// Note: loadDateStart is the value used in the ShippingHoursRestrictionChecker
											ladenMinWindowInHours = Math.max(0, Hours.between(loadDateStart, dischargeDateStart) - (loadDurationInHours));
										} else {
											return Activator.PLUGIN_ID;
										}
									}

									if (ladenMaxWindowInHours < 0) {
										final String message = String.format("FOB Sale|%s available laden travel time is negative!", fobSale.getName());
										final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
										final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
										dsd.addEObjectAndFeature(fobSale, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
										failures.add(dsd);
										dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);

										// No point going further!
										return Activator.PLUGIN_ID;
									}

									// Smallest amount of time permitted between slots
									final int ladenTimeInHours = Math.max(ladenTravelTimeInHours, ladenMinWindowInHours);

									// Total min travel time.
									final int totalRoundTripTimeInHours = loadDurationInHours + ladenTimeInHours + dischargeDurationInHours + ballastTravelTimeInHours;

									if (totalRoundTripTimeInHours > fobSale.getSlotOrDelegateShippingDaysRestriction() * 24) {
										final String message = String.format(
												"DES Purchase|%s is paired with a sale at %s. However the round trip time (%s) is greater than the permitted restriction (%s) by (%s).",
												fobPurchase.getName(), fobSale.getPort().getName(), TravelTimeUtils.formatHours(totalRoundTripTimeInHours),
												TravelTimeUtils.formatHours(fobSale.getSlotOrDelegateShippingDaysRestriction() * 24),
												TravelTimeUtils.formatHours(totalRoundTripTimeInHours - fobSale.getSlotOrDelegateShippingDaysRestriction() * 24));
										final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
										final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
										dsd.addEObjectAndFeature(fobSale, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
										dsd.setTag(ValidationConstants.TAG_TRAVEL_TIME);

										failures.add(dsd);
									}

									cargoDateConstraint(fobPurchase, fobSale, loadDurationInHours + ladenTimeInHours, ctx, failures);
								}
							}
							return Activator.PLUGIN_ID;
						});
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void cargoDateConstraint(LoadSlot load, DischargeSlot discharge, int ladenTravelTimeInHours, final IValidationContext ctx, final List<IStatus> failures) {
		ZonedDateTime windowStartWithSlotOrPortTime = load.getSchedulingTimeWindow().getStart();
		ZonedDateTime windowEndWithSlotOrPortTime = discharge.getSchedulingTimeWindow().getEnd();
		long maxTime = ChronoUnit.HOURS.between(windowStartWithSlotOrPortTime, windowEndWithSlotOrPortTime);
		if (maxTime < ladenTravelTimeInHours) {
			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus().withTypedName("Purchase", load.getName());
			failures.add(baseFactory.withObjectAndFeature(load.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes()) //
					.withMessage(
							(ladenTravelTimeInHours - maxTime) > ladenTravelTimeInHours ? (String.format("is paired with a sale at %s. However the slot windows are incompatible", discharge.getName()))
									: (String.format("is paired with a sale at %s. However the laden travel time (%s) is greater than the shortest possible journey by %s", discharge.getName(),
											TravelTimeUtils.formatHours(ladenTravelTimeInHours), TravelTimeUtils.formatHours(ladenTravelTimeInHours - maxTime)))) //
					.withSeverity(IStatus.WARNING) //
					.make(ctx));
		}
	}
}
