/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ContractCvValueConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;

			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;

					final Contract contract = dischargeSlot.getContract();
					if (contract instanceof SalesContract) {

						for (final Slot slot2 : cargo.getSlots()) {
							if (slot2 instanceof LoadSlot) {

								final LoadSlot loadSlot = (LoadSlot) slot2;
								final SalesContract salesContract = (SalesContract) contract;
								final double cv = loadSlot.getSlotOrPortCV();
								final String format = "Cargo '%s' has CV %.2f which is %s than the %s CV (%.2f) for the sales contract '%s'.";
								if (salesContract.isSetMinCvValue()) {
									final double minCvValue = salesContract.getMinCvValue();
									if (cv < minCvValue) {
										final String failureMessage = String.format(format, cargo.getName(), cv, "less", "minimum", minCvValue, contract.getName());
										final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
										dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
										dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
										failures.add(dsd);
									}
								}
								if (salesContract.isSetMaxCvValue()) {
									final double maxCvValue = salesContract.getMaxCvValue();
									if (cv > maxCvValue) {
										final String failureMessage = String.format(format, cargo.getName(), cv, "more", "maximum", maxCvValue, contract.getName());
										final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
										dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract());
										dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
										failures.add(dsd);
									}
								}
							}
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

}
