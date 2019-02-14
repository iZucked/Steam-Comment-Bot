/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.internal.localstore.IsSynchronizedVisitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
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
					.withName(name) //
					.withTag(ValidationConstants.TAG_ADP);

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
			if (periodDistribution.isSetMinCargoes()) {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(extraContext.getScenarioDataProvider());
				Map<YearMonth, Long> counts;
				if (profile.getContract() instanceof PurchaseContract) {
					counts = cargoModel.getLoadSlots().stream() //
							.filter(s -> s.getContract() == profile.getContract())//
							.map(s -> YearMonth.from(s.getWindowStart())) //
							.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
				} else {
					counts = cargoModel.getDischargeSlots().stream() //
							.filter(s -> s.getContract() == profile.getContract())//
							.map(s -> YearMonth.from(s.getWindowStart())) //
							.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
				}

				long sum = 0;
				for (final YearMonth ym : periodDistribution.getRange()) {
					sum += counts.getOrDefault(ym, 0L);
				}
				if (periodDistribution.getMinCargoes() > sum) {
					factory.copyName() //
							.withObjectAndFeature(periodDistribution, ADPPackage.Literals.PERIOD_DISTRIBUTION__MIN_CARGOES) //
							.withFormattedMessage("Min cargoes is %d but only %d cargoes in the period", periodDistribution.getMinCargoes(), sum) //
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
