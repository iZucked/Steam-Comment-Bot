/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author SG
 * 
 *         NOTE! Restricted elements constraint is in EmptyRestrictionsConstraint
 *
 */
public class MarketSlotConstraint extends AbstractModelMultiConstraint {

	private final ComposedAdapterFactory adapterFactory = createAdapterFactory();

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> failures) {

		final EObject object = ctx.getTarget();
		if (object instanceof SpotSlot spotSlot) {

			// Ignore extra spot slots.
			if (!(extraContext.getContainer(spotSlot) instanceof CargoModel)) {
				return;
			}

			final SpotMarket market = spotSlot.getMarket();
			if (market == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("[Market model|" + ((Slot) spotSlot).getName() + "] needs a market set."), IStatus.WARNING);
				dsd.addEObjectAndFeature(spotSlot, CargoPackage.eINSTANCE.getSpotSlot_Market());
				failures.add(dsd);
			}

			String type = "<Unknown type>";
			Slot<?> slot = null;
			if (spotSlot instanceof SpotLoadSlot spotLoadSlot) {
				type = spotLoadSlot.isDESPurchase() ? "DES Purchase" : "FOB Purchase";
				slot = spotLoadSlot;
			} else if (spotSlot instanceof SpotDischargeSlot spotDischargeSlot) {
				type = spotDischargeSlot.isFOBSale() ? "FOB Sale" : "DES Sale";
				slot = spotDischargeSlot;
			}

			// Generic constraints
			if (slot != null) {
				for (var feature : slot.eClass().getEAllStructuralFeatures()) {
					// feature == Name
					if (feature == CargoPackage.Literals.SLOT__CARGO //
							|| feature == CargoPackage.Literals.SLOT__NOTES || feature == MMXCorePackage.Literals.UUID_OBJECT__UUID) {
						// Ignore completely
					} else if (feature == MMXCorePackage.Literals.NAMED_OBJECT__NAME || feature == CargoPackage.Literals.SLOT__WINDOW_START || feature == CargoPackage.Literals.SPOT_SLOT__MARKET) {

					} else if (feature == CargoPackage.Literals.SLOT__OPTIONAL //
							|| feature == CargoPackage.Literals.SLOT__WINDOW_START_TIME //
							|| feature == CargoPackage.Literals.SLOT__WINDOW_SIZE || feature == CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS //
							|| feature == CargoPackage.Literals.SLOT__PORT //
							|| feature == CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE || feature == CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE //
							|| (feature == CargoPackage.Literals.SLOT__WINDOW_FLEX_UNITS && slot.getWindowFlex() == 0) // ignore units change if flex is zero
							|| (feature == CargoPackage.Literals.LOAD_SLOT__ARRIVE_COLD && ((LoadSlot) slot).isDESPurchase()) //
							) {
						// Values need to have a specific value
					} else {
						{
							EAttribute overrideToggleFeature = null;
							EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
							if (eAnnotation == null) {
								eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
							}
							if (eAnnotation != null) {
								for (EAttribute f : feature.getEContainingClass().getEAllAttributes()) {
									if (f.getName().equals(feature.getName() + "Override")) {
										overrideToggleFeature = f;
									}
								}
							}

							if ((overrideToggleFeature == null && slot.eIsSet(feature)) //
									|| (overrideToggleFeature != null && (Boolean) slot.eGet(overrideToggleFeature))) {

								// This will fetch the property source of the input object
								final IItemPropertySource inputPropertySource = (IItemPropertySource) adapterFactory.adapt(slot, IItemPropertySource.class);

								String featureName = feature.getName();
								// Iterate through the property descriptors to find a matching
								// descriptor for the feature
								for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(slot)) {

									if (feature.equals(descriptor.getFeature(slot))) {
										// Found match
										featureName = descriptor.getDisplayName(slot);
										break;
									}
								}

								if (feature == CargoPackage.Literals.SLOT__DURATION || feature == CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT
								// Mark as warnings for now (2023/07/10) pending clients making changes to scenarios
								) {
									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
											(IConstraintStatus) ctx.createFailureStatus(
													"[Market model|" + slot.getName() + "] " + type + " should not have " + featureName + " set. NOTE: This warning will soon become an ERROR."),
											IStatus.WARNING);
									dsd.addEObjectAndFeature(slot, feature);

									failures.add(dsd);
								} else {

									final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
											(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have " + featureName + " set."));
									dsd.addEObjectAndFeature(slot, feature);
									failures.add(dsd);
								}
							}
						}
					}
				}
				 
				if (!extraContext.isRelaxedChecking()) {
					// Period scenarios can alter these values
					if (slot.getWindowStart() != null && slot.getWindowStart().getDayOfMonth() != 1) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should start on the 1st of the month."));
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
						failures.add(dsd);
					}
					if (!slot.isSetWindowStartTime() || slot.getWindowStartTime() != 0) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should have start time set to zero"));
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime());
						failures.add(dsd);
					}

					boolean foundAltWindowSize = false;
					final int actual = slot.getWindowSize();
					final Port port = slot.getPort();
					if (slot.isSetWindowSizeUnits() && slot.getWindowSizeUnits() == TimePeriod.HOURS || (port != null && port.getDefaultWindowSizeUnits() == TimePeriod.HOURS)) {
						if (slot.isSetWindowSize()) {
							final LocalDate windowStart = slot.getWindowStart();
							final int expected = Hours.between(windowStart, windowStart.plusMonths(1)) - 1;
							if (expected == actual) {
								foundAltWindowSize = true;
							}
						}
					}

					if (!slot.isSetWindowSizeUnits() || slot.getWindowSizeUnits() != TimePeriod.MONTHS) {
						final int eType = foundAltWindowSize ? IStatus.WARNING : IStatus.ERROR;
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should have a one month window"), eType);
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSizeUnits());
						failures.add(dsd);
					} else if (!slot.isSetWindowSize() || actual != 1) {
						final int eType = foundAltWindowSize ? IStatus.WARNING : IStatus.ERROR;
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should have a one month window"), eType);
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
						failures.add(dsd);
					}
				}
			}

			if (spotSlot instanceof SpotLoadSlot spotLoadSlot) {
				if (market instanceof DESPurchaseMarket desPurchaseMarket) {

					final EList<APortSet<Port>> destinationPortSets = desPurchaseMarket.getDestinationPorts();
					final Set<Port> destinationPorts = SetUtils.getObjects(destinationPortSets);
					if (!destinationPorts.contains(spotLoadSlot.getPort())) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + spotLoadSlot.getName() + "] DES purchase port is not a market port."), IStatus.ERROR);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBPurchasesMarket fobPurchasesMarket) {
					if (spotLoadSlot.getPort() != fobPurchasesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(
										"[Market model|" + spotLoadSlot.getName() + "] FOB Purchase port does not match market port of " + fobPurchasesMarket.getNotionalPort().getName() + "."),
								IStatus.ERROR);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}
				}

			} else if (spotSlot instanceof SpotDischargeSlot spotDischargeSlot) {

				if (market instanceof DESSalesMarket desSalesMarket) {

					if (spotDischargeSlot.getPort() != desSalesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(
										"[Market model|" + spotDischargeSlot.getName() + "] DES sales port does not match market port of '" + desSalesMarket.getNotionalPort().getName() + "'."),
								IStatus.ERROR);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBSalesMarket fobSalesMarket) {

					final Set<Port> originPorts = SetUtils.getObjects(fobSalesMarket.getOriginPorts());
					if (!originPorts.contains(spotDischargeSlot.getPort())) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + spotDischargeSlot.getName() + "] FOB sale port does not match market ports"), IStatus.ERROR);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}
				}

			}
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
				if (lngScenarioModel.getPromptPeriodStart() != null) {
					final ZonedDateTime windowEndWithSlotOrPortTime = slot.getSchedulingTimeWindow().getEnd();
					if (windowEndWithSlotOrPortTime.isBefore(lngScenarioModel.getPromptPeriodStart().atStartOfDay(ZoneId.from(windowEndWithSlotOrPortTime)))) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] Spot slot is in the past"), IStatus.WARNING);
						dsd.addEObjectAndFeature(slot, MMXCorePackage.eINSTANCE.getNamedObject_Name());
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
						failures.add(dsd);

					}
				}

			}
		}
	}

	/**
	 * Utility method to create a {@link ComposedAdapterFactory}. Taken from org.eclipse.emf.compare.util.AdapterUtils.
	 * 
	 * @return
	 */
	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}
}
