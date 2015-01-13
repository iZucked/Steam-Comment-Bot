/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import static org.ops4j.peaberry.Peaberry.osgiModule;

import java.util.Date;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.IShippingDaysRestrictionSpeedProvider;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 */
public class ShippingDaysRestrictionConstraint extends AbstractModelMultiConstraint {

	public ShippingDaysRestrictionConstraint() {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				// TODO Auto-generated method stub
				install(osgiModule(FrameworkUtil.getBundle(ShippingDaysRestrictionConstraint.class).getBundleContext()));
				bind(IShippingDaysRestrictionSpeedProvider.class).toProvider(Peaberry.service(IShippingDaysRestrictionSpeedProvider.class).single());
			}
		});
		injector.injectMembers(this);
	}

	private static final int MAX_SHIPPING_DAYS = 90;

	@Inject(optional = true)
	private IShippingDaysRestrictionSpeedProvider shippingDaysSpeedProvider;

	private int getMinRouteTimeInHours(final Slot from, final Slot to, final LNGScenarioModel lngScenarioModel, final Vessel vessel, final double referenceSpeed) {

		int minDuration = Integer.MAX_VALUE;
		for (final Route route : lngScenarioModel.getPortModel().getRoutes()) {
			assert route != null;
			final Port fromPort = from.getPort();
			final Port toPort = to.getPort();
			if (fromPort != null && toPort != null) {

				final int distance = getDistance(route, fromPort, toPort);

				int extraTime = 0;
				for (final VesselClassRouteParameters vcrp : vessel.getVesselClass().getRouteParameters()) {
					if (vcrp.getRoute().equals(route)) {
						extraTime = vcrp.getExtraTransitTime();
					}
				}
				final double travelTime = distance / referenceSpeed;
				final int totalTime = (int) (Math.floor(travelTime) + extraTime);
				if (totalTime < minDuration) {
					minDuration = totalTime;
				}
			}
		}
		return minDuration;
	}

	public int getDistance(@NonNull final Route route, @NonNull final Port from, @NonNull final Port to) {
		for (final RouteLine dl : route.getLines()) {
			if (dl.getFrom().equals(from) && dl.getTo().equals(to)) {
				return dl.getDistance();
			}
		}
		return Integer.MAX_VALUE;
	}

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

							if (desPurchase.getAssignment() instanceof Vessel) {
								final Vessel vessel = (Vessel) desPurchase.getAssignment();

								final VesselClass vesselClass = vessel.getVesselClass();
								if (vesselClass == null) {
									return Activator.PLUGIN_ID;
								}
								final double referenceSpeed = shippingDaysSpeedProvider == null ? vesselClass.getMaxSpeed() : shippingDaysSpeedProvider.getSpeed(vesselClass);

								final int loadDurationInHours = desPurchase.getSlotOrPortDuration();
								final int dischargeDurationInHours = dischargeSlot.getSlotOrPortDuration();

								final int ladenTravelTimeInHours = getMinRouteTimeInHours(desPurchase, dischargeSlot, lngScenarioModel, vessel, referenceSpeed);
								final int ballastTravelTimeInHours = getMinRouteTimeInHours(dischargeSlot, desPurchase, lngScenarioModel, vessel, referenceSpeed);

								// Calculate minimum time due to slot windows
								final int ladenMaxWindowInHours;
								final int ladenMinWindowInHours;
								{
									// TODO: check overlaps
									final Date loadDateStart = desPurchase.getWindowStartWithSlotOrPortTime();
									final Date loadDateEnd = desPurchase.getWindowEndWithSlotOrPortTime();
									final Date dischargeDateStart = dischargeSlot.getWindowStartWithSlotOrPortTime();
									final Date dischargeDateEnd = dischargeSlot.getWindowEndWithSlotOrPortTime();

									if (loadDateStart != null && dischargeDateEnd != null) {
										ladenMaxWindowInHours = (int) ((dischargeDateEnd.getTime() - loadDateStart.getTime()) / Timer.ONE_HOUR) - (loadDurationInHours);
									} else {
										return Activator.PLUGIN_ID;
									}

									if (loadDateEnd != null && dischargeDateStart != null) {
										// There could be an overlap
										ladenMinWindowInHours = Math.max(0, (int) ((dischargeDateStart.getTime() - loadDateEnd.getTime()) / Timer.ONE_HOUR) - (loadDurationInHours));
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

								if (ladenTravelTimeInHours > ladenMaxWindowInHours) {
									final String message = String.format("DES Purchase|%s available laden travel time (%s) is greater than available time from windows (%s)!", desPurchase.getName(),
											formatHours(ladenTravelTimeInHours), formatHours(ladenMaxWindowInHours));
									final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
									dsd.addEObjectAndFeature(desPurchase, CargoPackage.eINSTANCE.getSlot_WindowStart());
									dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
									failures.add(dsd);
								}

								// Smallest amount of time permitted between slots
								final int ladenTimeInHours = Math.max(ladenTravelTimeInHours, ladenMinWindowInHours);

								// Total min travel time.
								final int totalRoundTripTimeInHours = loadDurationInHours + ladenTimeInHours + dischargeDurationInHours + ballastTravelTimeInHours;

								if (totalRoundTripTimeInHours > desPurchase.getShippingDaysRestriction() * 24) {
									final String message = String.format(
											"DES Purchase|%s is paired with a sale at %s. However the round trip time (%s) is greater than the permitted restriction (%s).", desPurchase.getName(),
											dischargeSlot.getPort().getName(), formatHours(totalRoundTripTimeInHours), formatHours(desPurchase.getShippingDaysRestriction() * 24));
									final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
									dsd.addEObjectAndFeature(desPurchase, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
									failures.add(dsd);
								}

							}
						}

					} else {

						// FOB?

					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private String formatHours(final int hours) {
		if (hours < 24) {
			if (hours == 1) {
				return hours + " hour";
			} else {
				return hours + " hours";
			}
		} else {
			final int remainderHours = hours % 24;
			final int days = hours / 24;
			return days + " day" + (days > 1 ? "s" : "") + (remainderHours > 0 ? (", " + remainderHours + " hour" + (remainderHours > 1 ? "s" : "")) : "");
		}
	}
}
