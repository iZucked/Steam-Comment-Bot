/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.DESPurchaseMarket;
import com.mmxlabs.models.lng.pricing.DESSalesMarket;
import com.mmxlabs.models.lng.pricing.FOBPurchasesMarket;
import com.mmxlabs.models.lng.pricing.FOBSalesMarket;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SpotMarketConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof SpotMarket) {
			final SpotMarket spotMarket = (SpotMarket) target;

			if (spotMarket.getMaxQuantity() != 0 && spotMarket.getMinQuantity() != 0) {
				if (spotMarket.getMaxQuantity() < spotMarket.getMinQuantity()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Min quantity should be less than or equal to max quantity."));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getSpotMarket_MinQuantity());
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getSpotMarket_MaxQuantity());
					failures.add(dsd);
				}
			}

			if (spotMarket instanceof DESPurchaseMarket) {
				final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) spotMarket;

				if (desPurchaseMarket.getCv() < 0.001) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A positive CV value should be set"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getDESPurchaseMarket_Cv());
					failures.add(dsd);
				}

				final Set<APort> ports = SetUtils.getPorts(desPurchaseMarket.getDestinationPorts());
				if (ports.isEmpty()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("One or more discharge ports must be set"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getDESPurchaseMarket_DestinationPorts());
					failures.add(dsd);
				}
				for (final APort p : ports) {
					if (p instanceof Port) {
						final Port port = (Port) p;
						if (!port.getCapabilities().contains(PortCapability.DISCHARGE)) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Port " + port.getName()
									+ " is not a discharge port"), IStatus.WARNING);
							dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getDESPurchaseMarket_DestinationPorts());
							failures.add(dsd);
						}
					}
				}

				if (desPurchaseMarket.getContract() == null || !(desPurchaseMarket.getContract() instanceof PurchaseContract)) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A purchase contract must be specified"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getDESPurchaseMarket_Contract());
					failures.add(dsd);
				}
			}

			if (spotMarket instanceof DESSalesMarket) {
				final DESSalesMarket desSalesMarket = (DESSalesMarket) spotMarket;

				final Port notionalPort = desSalesMarket.getNotionalPort();
				if (notionalPort == null || notionalPort.getCapabilities().contains(PortCapability.DISCHARGE) == false) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A notional discharge port must be set"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getDESSalesMarket_NotionalPort());
					failures.add(dsd);
				}

				if (desSalesMarket.getContract() == null || !(desSalesMarket.getContract() instanceof SalesContract)) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A sales contract must be specified"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getDESSalesMarket_Contract());
					failures.add(dsd);
				}
			}

			if (spotMarket instanceof FOBPurchasesMarket) {
				final FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) spotMarket;

				final Port notionalPort = fobPurchasesMarket.getNotionalPort();
				if (notionalPort == null || notionalPort.getCapabilities().contains(PortCapability.LOAD) == false) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A notional load port must be set"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getFOBPurchasesMarket_NotionalPort());
					failures.add(dsd);
				}

				if (fobPurchasesMarket.getCv() < 0.001) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A positive CV value should be set"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getFOBPurchasesMarket_Cv());
					failures.add(dsd);
				}

				if (fobPurchasesMarket.getContract() == null || !(fobPurchasesMarket.getContract() instanceof PurchaseContract)) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A purchase contract must be specified"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getFOBPurchasesMarket_Contract());
					failures.add(dsd);
				}
			}

			if (spotMarket instanceof FOBSalesMarket) {
				final FOBSalesMarket fobSalesMarket = (FOBSalesMarket) spotMarket;

				final Port loadPort = fobSalesMarket.getLoadPort();
				if (loadPort == null || loadPort.getCapabilities().contains(PortCapability.LOAD) == false) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A load port must be set"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getFOBSalesMarket_LoadPort());
					failures.add(dsd);
				}

				if (fobSalesMarket.getContract() == null || !(fobSalesMarket.getContract() instanceof SalesContract)) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A sales contract must be specified"));
					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getFOBSalesMarket_Contract());
					failures.add(dsd);
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
