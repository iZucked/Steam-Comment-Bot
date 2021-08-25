/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class IntervalDistributionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof CargoIntervalDistributionModel) {
			final CargoIntervalDistributionModel distributionModel = (CargoIntervalDistributionModel) target;

			String name = "<unknown>";
			final ContractProfile<?, ?> profile = getProfile(distributionModel, extraContext);
			if (profile != null && profile.getContract() != null) {
				final String n = profile.getContract().getName();
				if (n != null && !n.isEmpty()) {
					name = n;
				}
			}

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", name) //
					.withTag(ValidationConstants.TAG_ADP);

			if (distributionModel.getIntervalType() == null) {
				factory.copyName() //
						.withObjectAndFeature(distributionModel, ADPPackage.Literals.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE) //
						.withMessage("No interval specified") //
						.make(ctx, statuses);
			}
			// Values is no longer displayed so pretend it is not set
			if (distributionModel.getIntervalType() == IntervalType.WEEKLY) {
				factory.copyName() //
				.withObjectAndFeature(distributionModel, ADPPackage.Literals.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE) //
				.withMessage("No interval specified") //
				.make(ctx, statuses);
			}
		}
	}

	private @Nullable ContractProfile<?, ?> getProfile(final DistributionModel periodDistribution, @NonNull final IExtraValidationContext extraContext) {

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
