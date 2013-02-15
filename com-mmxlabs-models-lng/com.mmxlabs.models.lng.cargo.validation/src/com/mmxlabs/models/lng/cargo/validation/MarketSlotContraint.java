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
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class MarketSlotContraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {

		final List<IStatus> failures = new LinkedList<IStatus>();

		final EObject object = ctx.getTarget();
		if (object instanceof SpotSlot) {

			final SpotSlot spotSlot = (SpotSlot) object;
			final ASpotMarket market = spotSlot.getMarket();

			if (spotSlot instanceof SpotLoadSlot) {
				final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) spotSlot;

				if (market instanceof DESPurchaseMarket) {
					final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;

					if (spotLoadSlot.getContract() != desPurchaseMarket.getContract()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("DES Purchase Contract does not match market contract of ." + desPurchaseMarket.getContract().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}
					if (!desPurchaseMarket.getDestinationPorts().contains(spotLoadSlot.getPort())) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("DES Purchase Port does is not a market port."),
								IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBPurchasesMarket) {
					final FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) market;
					if (spotLoadSlot.getPort() != fobPurchasesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("FOB Purchase Port does not match market port of ."
								+ fobPurchasesMarket.getNotionalPort().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

					if (spotLoadSlot.getContract() != fobPurchasesMarket.getContract()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("FOB Purchase Contract does not match market contract of ." + fobPurchasesMarket.getContract().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotLoadSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}

				}

			} else if (spotSlot instanceof SpotDischargeSlot) {
				final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) spotSlot;

				if (market instanceof DESSalesMarket) {
					final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
					if (spotDischargeSlot.getContract() != desSalesMarket.getContract()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("DES Sales Contract does not match market contract of ." + desSalesMarket.getContract().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}

					if (spotDischargeSlot.getPort() != desSalesMarket.getNotionalPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("DES Sales Port does not match market port of ."
								+ desSalesMarket.getNotionalPort().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);

					}

				} else if (market instanceof FOBSalesMarket) {
					final FOBSalesMarket fobSalesMarket = (FOBSalesMarket) market;

					if (spotDischargeSlot.getContract() != fobSalesMarket.getContract()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("FOB Sales Contract does not match market contract of ." + fobSalesMarket.getContract().getName()), IStatus.WARNING);
						dsd.addEObjectAndFeature(spotDischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
						failures.add(dsd);

					}
					if (spotDischargeSlot.getPort() != fobSalesMarket.getLoadPort()) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("FOB Sales Port does not match market port of ."
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
