/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

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

import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
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

				checkSpotMarketGroup(ctx, failures, spotMarket, SpotType.DES_PURCHASE);

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

				checkSpotMarketGroup(ctx, failures, spotMarket, SpotType.DES_SALE);

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
				} else {
					final SalesContract salesContract = (SalesContract) desSalesMarket.getContract();
					final EList<APortSet> allowedPorts = salesContract.getAllowedPorts();
					if (allowedPorts != null && !allowedPorts.isEmpty()) {
						if (!allowedPorts.contains(notionalPort)) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Notional port is not a valid contract port."));
							dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getDESSalesMarket_NotionalPort());
							failures.add(dsd);
						}
					}
				}
			}

			if (spotMarket instanceof FOBPurchasesMarket) {
				final FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) spotMarket;

				checkSpotMarketGroup(ctx, failures, spotMarket, SpotType.FOB_PURCHASE);

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
				} else {
					final PurchaseContract salesContract = (PurchaseContract) fobPurchasesMarket.getContract();
					final EList<APortSet> allowedPorts = salesContract.getAllowedPorts();
					if (allowedPorts != null && !allowedPorts.isEmpty()) {
						if (!allowedPorts.contains(notionalPort)) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Notional port is not a valid contract port."));
							dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getFOBPurchasesMarket_NotionalPort());
							failures.add(dsd);
						}
					}
				}
			}

			if (spotMarket instanceof FOBSalesMarket) {
				final FOBSalesMarket fobSalesMarket = (FOBSalesMarket) spotMarket;

				checkSpotMarketGroup(ctx, failures, spotMarket, SpotType.FOB_SALE);

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
				} else {
					final SalesContract salesContract = (SalesContract) fobSalesMarket.getContract();
					final EList<APortSet> allowedPorts = salesContract.getAllowedPorts();
					if (allowedPorts != null && !allowedPorts.isEmpty()) {
						if (!allowedPorts.contains(loadPort)) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Notional port is not a valid contract port."));
							dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getFOBSalesMarket_LoadPort());
							failures.add(dsd);
						}
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

	private void checkSpotMarketGroup(final IValidationContext ctx, final List<IStatus> failures, final SpotMarket spotMarket, final SpotType spotType) {
		if (spotMarket.eContainer() instanceof SpotMarketGroup) {
			final SpotMarketGroup spotMarketGroup = (SpotMarketGroup) spotMarket.eContainer();
			if (spotMarketGroup.getType() != spotType) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(spotMarket.eClass().getName()
						+ " is in the SpotMarketGroup for " + spotType));
				dsd.addEObjectAndFeature(spotMarketGroup, PricingPackage.eINSTANCE.getSpotMarketGroup_Markets());
				failures.add(dsd);
			}
		}
	}
}
