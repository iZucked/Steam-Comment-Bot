/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class MarketSlotConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {

		final List<IStatus> failures = new LinkedList<IStatus>();

		final EObject object = ctx.getTarget();
		if (object instanceof SpotSlot) {

			final SpotSlot spotSlot = (SpotSlot) object;
			final SpotMarket market = spotSlot.getMarket();

			if (spotSlot instanceof SpotLoadSlot) {
				final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) spotSlot;

				if (market instanceof DESPurchaseMarket) {
					final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;

					if (spotLoadSlot.getContract() != null) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotLoadSlot.getName()+"] DES purchase should not have a contract set."), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}
					if (!desPurchaseMarket.getDestinationPorts().contains(spotLoadSlot.getPort())) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotLoadSlot.getName()+"] DES purchase port is not a market port."),
								IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBPurchasesMarket) {
					final FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) market;
					if (spotLoadSlot.getPort() != fobPurchasesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotLoadSlot.getName()+"] FOB Purchase Port does not match market port of "+fobPurchasesMarket.getNotionalPort()+"."
								+ fobPurchasesMarket.getNotionalPort().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

					if (spotLoadSlot.getContract() != null) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotLoadSlot.getName()+"] FOB purchase should not have a contract set."), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}

				}

			} else if (spotSlot instanceof SpotDischargeSlot) {
				final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) spotSlot;

				if (market instanceof DESSalesMarket) {
					final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
					if (spotDischargeSlot.getContract() != null) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotDischargeSlot.getName()+"] DES sales should not have a contract set."), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}

					if (spotDischargeSlot.getPort() != desSalesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotDischargeSlot.getName()+"] DES sales Port does not match market port of '"+desSalesMarket.getNotionalPort()+"'."
								+ desSalesMarket.getNotionalPort().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBSalesMarket) {
					final FOBSalesMarket fobSalesMarket = (FOBSalesMarket) market;

					if (spotDischargeSlot.getContract() != null) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotDischargeSlot.getName()+"] FOB sale should not have a contract set."), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}
					if (spotDischargeSlot.getPort() != fobSalesMarket.getLoadPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Market model|"+spotDischargeSlot.getName()+"] FOB sale port does not match market port of '" + fobSalesMarket.getLoadPort()+"'."
								+ fobSalesMarket.getLoadPort().getName()), IStatus.WARNING);
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
