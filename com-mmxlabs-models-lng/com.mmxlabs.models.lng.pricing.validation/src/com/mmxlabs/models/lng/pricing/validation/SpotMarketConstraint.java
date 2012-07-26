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

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.lng.pricing.SpotType;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SpotMarketConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof SpotMarket) {
			final SpotMarket spotMarket = (SpotMarket) target;
//
//			if (spotMarket.getContract() == null) {
//				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A contract must be specified"));
//				dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getSpotMarket_Contract());
//				failures.add(dsd);
//			}

//			if (spotMarket.eContainer() instanceof SpotMarketGroup) {
//				final SpotMarketGroup spotMarketGroup = (SpotMarketGroup) spotMarket.eContainer();
//				if (spotMarketGroup.getType() != spotMarket.getType()) {
//					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
//							(IConstraintStatus) ctx.createFailureStatus("Spot Market Type should be the same as it's container."));
//					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getSpotMarket_Type());
//					failures.add(dsd);
//				}
//			}
//			
//			if (spotMarket.getType() == SpotType.DES_PURCHASE) {
//				Set<APort> ports = SetUtils.getPorts(spotMarket.getPorts());
//				if (ports.isEmpty()) {
//					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
//							(IConstraintStatus) ctx.createFailureStatus("DES Purchase Markets need to be linked to discharge ports."));
//					dsd.addEObjectAndFeature(spotMarket, PricingPackage.eINSTANCE.getSpotMarket_Ports());
//					failures.add(dsd);
//				}
//			}
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
