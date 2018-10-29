/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PeriodDistributionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PeriodDistribution) {
			final PeriodDistribution periodDistribution = (PeriodDistribution) target;

			String name = "Unknown adp profile";
			final ContractProfile<?, ?> profile = getProfile(periodDistribution, extraContext);
			if (profile != null && profile.getContract() != null) {
				name = "ADP profile for " + profile.getContract().getName();
			}

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName(name);

			if (periodDistribution.getRange().isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__RANGE) //
						.withMessage("No months specified") //
						.make(ctx, statuses);
			}
			if (periodDistribution.isSetMinCargoes() && periodDistribution.isSetMaxCargoes()) {
				if (periodDistribution.getMinCargoes() > periodDistribution.getMaxCargoes()) {
					factory.copyName() //
							.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__MIN_CARGOES) //
							.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__MAX_CARGOES) //
							.withMessage("Min cargoes is greater than max cargoes") //
							.make(ctx, statuses);
				}
			}
		}
	}

	private @Nullable ContractProfile<?, ?> getProfile(final PeriodDistribution periodDistribution, @NonNull final IExtraValidationContext extraContext) {

		EObject container = extraContext.getContainer(periodDistribution);
		while (container != null) {
			if (container instanceof ContractProfile<?, ?>) {
				return (ContractProfile<?, ?>) container;
			}
			container = extraContext.getContainer(container);

		}
		return null;
	}
}
