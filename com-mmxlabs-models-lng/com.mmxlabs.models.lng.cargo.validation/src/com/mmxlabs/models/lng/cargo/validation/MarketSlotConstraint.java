/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class MarketSlotConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {

		final List<IStatus> failures = new LinkedList<IStatus>();

		final EObject object = ctx.getTarget();
		if (object instanceof SpotSlot) {

			final SpotSlot spotSlot = (SpotSlot) object;
			final SpotMarket market = spotSlot.getMarket();
			if (market == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("[Market model|" + ((Slot) spotSlot).getName() + "] needs a market set."), IStatus.WARNING);
				dsd.addEObjectAndFeature(spotSlot, CargoPackage.eINSTANCE.getSpotSlot_Market());
				failures.add(dsd);
			}

			String type = "<Unknown type>";
			Slot slot = null;
			if (spotSlot instanceof SpotLoadSlot) {
				final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) spotSlot;
				type = spotLoadSlot.isDESPurchase() ? "DES Purchase" : "FOB Purchase";
				slot = spotLoadSlot;
			} else if (spotSlot instanceof SpotDischargeSlot) {
				final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) spotSlot;
				type = spotDischargeSlot.isFOBSale() ? "FOB Sale" : "DES Sale";
				slot = spotDischargeSlot;
			}

			// Generic constraints
			if (slot != null) {
				if (slot.getContract() != null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have a contract set."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
					failures.add(dsd);
				}

				if (slot.isSetCancellationExpression()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have a cancellation fee set."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_CancellationExpression());
					failures.add(dsd);
				}

				if (slot.getEntity() != null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have an entity set."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Entity());
					failures.add(dsd);
				}
				if (slot.getPriceExpression() != null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have a price expression set."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PriceExpression());
					failures.add(dsd);
				}
				if (slot.getPricingDate() != null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have a pricing date set."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PricingDate());
					failures.add(dsd);
				}
				if (slot.isSetMinQuantity()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have a min quantity."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					failures.add(dsd);
				}
				if (slot.isSetMaxQuantity()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have a max quantity."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					failures.add(dsd);
				}
				if (slot.isSetPricingEvent()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have a pricing event."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PricingEvent());
					failures.add(dsd);
				}
				if (slot.getWindowFlex() != 0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should not have window flex."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowFlex());
					failures.add(dsd);
				}
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
				int actual = slot.getWindowSize();
				if (slot.isSetWindowSizeUnits() && slot.getWindowSizeUnits() == TimePeriod.HOURS || slot.getPort().getDefaultWindowSizeUnits() == TimePeriod.HOURS) {
					if (slot.isSetWindowSize()) {
						LocalDate windowStart = slot.getWindowStart();
						int expected = Hours.between(windowStart, windowStart.plusMonths(1)) - 1;
						if (expected == actual) {
							foundAltWindowSize = true;
						}
					}
				}

				if (!slot.isSetWindowSizeUnits() || slot.getWindowSizeUnits() != TimePeriod.MONTHS) {
					int eType = foundAltWindowSize ? IStatus.WARNING : IStatus.ERROR;
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should have a one month window"), eType);
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSizeUnits());
					failures.add(dsd);
				}
				if (!slot.isSetWindowSize() || actual != 1) {
					int eType = foundAltWindowSize ? IStatus.WARNING : IStatus.ERROR;
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + slot.getName() + "] " + type + " should have a one month window"), eType);
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowSize());
					failures.add(dsd);
				}
			}

			if (spotSlot instanceof SpotLoadSlot) {
				final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) spotSlot;

				if (spotLoadSlot.isSetCargoCV()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("[Market model|" + spotLoadSlot.getName() + "] " + type + " should not have CV set."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
					failures.add(dsd);
				}

				if (market instanceof DESPurchaseMarket) {
					final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;

					final EList<APortSet<Port>> destinationPortSets = desPurchaseMarket.getDestinationPorts();
					final Set<Port> destinationPorts = SetUtils.getObjects(destinationPortSets);
					if (!destinationPorts.contains(spotLoadSlot.getPort())) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + spotLoadSlot.getName() + "] DES purchase port is not a market port."), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBPurchasesMarket) {
					final FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) market;
					if (spotLoadSlot.getPort() != fobPurchasesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Market model|" + spotLoadSlot.getName()
								+ "] FOB Purchase Port does not match market port of " + fobPurchasesMarket.getNotionalPort() + "." + fobPurchasesMarket.getNotionalPort().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}
				}

			} else if (spotSlot instanceof SpotDischargeSlot) {
				final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) spotSlot;

				if (market instanceof DESSalesMarket) {
					final DESSalesMarket desSalesMarket = (DESSalesMarket) market;

					if (spotDischargeSlot.getPort() != desSalesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Market model|" + spotDischargeSlot.getName()
								+ "] DES sales Port does not match market port of '" + desSalesMarket.getNotionalPort() + "'." + desSalesMarket.getNotionalPort().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBSalesMarket) {
					final FOBSalesMarket fobSalesMarket = (FOBSalesMarket) market;

					final Set<Port> originPorts = SetUtils.getObjects(fobSalesMarket.getOriginPorts());
					if (!originPorts.contains(spotDischargeSlot.getPort())) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|" + spotDischargeSlot.getName() + "] FOB sale port does not match market ports"), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}
				}

			}

		}

		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}
}
