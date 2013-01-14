/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NetbackPurchaseContract;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class NetbackPurchaseContractConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof NetbackPurchaseContract) {
			final NetbackPurchaseContract contract = (NetbackPurchaseContract) target;
			if (contract.getNotionalBallastParameters().isEmpty()) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("Netback contract needs at least one notional ballast parameters"));
				dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getNetbackPurchaseContract_NotionalBallastParameters());
				failures.add(dsd);
			} else {
				final MMXRootObject scenario = Activator.getDefault().getExtraValidationContext().getRootObject();
				if (scenario != null) {
					final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
					final Set<VesselClass> vesselClasses = new HashSet<VesselClass>(fleetModel.getVesselClasses());
					for (final NotionalBallastParameters p : contract.getNotionalBallastParameters()) {
						vesselClasses.removeAll(p.getVesselClasses());
					}
					if (!vesselClasses.isEmpty()) {
						for (final VesselClass vc : vesselClasses) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Netback contract need notional ballast parameter for vessel class " + vc.getName()));
							dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getNetbackPurchaseContract_NotionalBallastParameters());
							failures.add(dsd);
						}
					}
				}
			}
			
			if (contract.getFloorPrice() < 0.0) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus("Floor price should be positive"));
				dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getNetbackPurchaseContract_FloorPrice());
				failures.add(dsd);
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
