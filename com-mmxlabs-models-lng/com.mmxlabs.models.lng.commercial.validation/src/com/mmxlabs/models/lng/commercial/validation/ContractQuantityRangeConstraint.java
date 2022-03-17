/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractQuantityRangeConstraint extends AbstractModelMultiConstraint {

	private static final int SENSIBLE_M3 = 300_000;

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Contract contract) {
			checkRange(contract, ctx, failures);
			if (contract.getOperationalTolerance() < 0.0) {
				final String failureMessage = String.format("Contract '%s' operational tolerance is less than zero", contract.getName());

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__OPERATIONAL_TOLERANCE);
				failures.add(dsd);
			}
			if (contract.getOperationalTolerance() > 1.0) {
				final String failureMessage = String.format("Contract '%s' operational tolerance is greater than 100%%", contract.getName());

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__OPERATIONAL_TOLERANCE);
				failures.add(dsd);
			}
		}
	}

	private void checkRange(final Contract contract, final IValidationContext ctx, final List<IStatus> failures) {
		if (contract.getMinQuantity() >= 0 && contract.getMaxQuantity() > 0) {
			final double minQuantity = contract.getMinQuantity();
			final double maxQuantity = contract.getMaxQuantity();

			if (minQuantity > maxQuantity) {
				final String failureMessage = String.format("Contract '%s' has minimum quantity (%.2f) greater than maximum quantity (%.2f)", contract.getName(), minQuantity, maxQuantity);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__MIN_QUANTITY);
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__MAX_QUANTITY);
				failures.add(dsd);
			}
			if (minQuantity > SENSIBLE_M3 && contract.getVolumeLimitsUnit() == VolumeUnits.M3) {
				final String failureMessage = String.format("Contract '%s' min volume limit (%s) is not sensible, note units are in M3", contract.getName(), minQuantity);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__MIN_QUANTITY);
				failures.add(dsd);
			}
			if (maxQuantity > SENSIBLE_M3 && contract.getVolumeLimitsUnit() == VolumeUnits.M3) {
				final String failureMessage = String.format("Contract '%s' max volume limit (%s) is not sensible, note units are in M3", contract.getName(), maxQuantity);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__MAX_QUANTITY);
				failures.add(dsd);
			}
		}
	}

}
