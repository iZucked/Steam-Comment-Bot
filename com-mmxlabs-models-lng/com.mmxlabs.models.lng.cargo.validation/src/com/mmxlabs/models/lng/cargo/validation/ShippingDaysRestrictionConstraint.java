/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.Cargo;
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
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
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

		// Valid slot data checks
		if (object instanceof Slot) {
			if (object instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) object;
				if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {
					if (loadSlot.getShippingDaysRestriction() > MAX_SHIPPING_DAYS) {
						final String message = String.format("DES Purchase|%s shipping days restriction is too big.", loadSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.WARNING);
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
						failures.add(dsd);
					} else if (loadSlot.getShippingDaysRestriction() == 0) {
						final String message = String.format("DES Purchase|%s shipping days restriction is set to zero - unable to ship anywhere!", loadSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status, IStatus.WARNING);
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
						failures.add(dsd);
					}
				} else if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy) {
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
				if (SlotClassifier.classify(dischargeSlot) == SlotType.FOB_Sale) {
					final Cargo cargo = dischargeSlot.getCargo();
					if (cargo != null) {
						for (final Slot slot : cargo.getSlots()) {
							if (slot instanceof LoadSlot) {
								final LoadSlot loadSlot = (LoadSlot) slot;
								if (loadSlot.getPort() != dischargeSlot.getPort()) {
									final String message = String.format("FOB Sale|%s is not divertable, but is linked to a FOB Purchase at a different load port", dischargeSlot.getName());
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

									final VesselClass vesselClass = vessel.getVesselClass();
									if (vesselClass == null) {
										return Activator.PLUGIN_ID;
									}

									final int loadDurationInHours = desPurchase.getSlotOrPortDuration();
									final int dischargeDurationInHours = dischargeSlot.getSlotOrPortDuration();

									@NonNull
									PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
									final int ladenTravelTimeInHours = CargoTravelTimeUtils.getDivertableDESMinRouteTimeInHours(desPurchase, desPurchase, dischargeSlot, shippingDaysSpeedProvider,
											portModel, vessel, CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, desPurchase, vesselClass, true));
									final int ballastTravelTimeInHours = CargoTravelTimeUtils.getDivertableDESMinRouteTimeInHours(desPurchase, dischargeSlot, desPurchase, shippingDaysSpeedProvider,
											portModel, vessel, CargoTravelTimeUtils.getReferenceSpeed(shippingDaysSpeedProvider, desPurchase, vesselClass, false));

									// Calculate minimum time due to slot windows
									final int ladenMaxWindowInHours;
									final int ladenMinWindowInHours;
									{
										// TODO: check overlaps
										final ZonedDateTime loadDateStart = desPurchase.getWindowStartWithSlotOrPortTime();
										final ZonedDateTime loadDateEnd = desPurchase.getWindowEndWithSlotOrPortTime();
										final ZonedDateTime dischargeDateStart = dischargeSlot.getWindowStartWithSlotOrPortTime();
										final ZonedDateTime dischargeDateEnd = dischargeSlot.getWindowEndWithSlotOrPortTime();

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
										// No point going further!
										return Activator.PLUGIN_ID;
									}

									// Smallest amount of time permitted between slots
									final int ladenTimeInHours = Math.max(ladenTravelTimeInHours, ladenMinWindowInHours);

									// Total min travel time.
									final int totalRoundTripTimeInHours = loadDurationInHours + ladenTimeInHours + dischargeDurationInHours + ballastTravelTimeInHours;

									if (totalRoundTripTimeInHours > desPurchase.getShippingDaysRestriction() * 24) {
										final String message = String.format(
												"DES Purchase|%s is paired with a sale at %s. However the round trip time (%s) is greater than the permitted restriction (%s) by (%s).",
												desPurchase.getName(), dischargeSlot.getPort().getName(), TravelTimeUtils.formatHours(totalRoundTripTimeInHours),
												TravelTimeUtils.formatHours(desPurchase.getShippingDaysRestriction() * 24),
												TravelTimeUtils.formatHours(totalRoundTripTimeInHours - desPurchase.getShippingDaysRestriction() * 24));
										final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
										final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
										dsd.addEObjectAndFeature(desPurchase, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
										failures.add(dsd);
									}
								}
							}
							return Activator.PLUGIN_ID;
						});
					} else {

						// FOB?

					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
