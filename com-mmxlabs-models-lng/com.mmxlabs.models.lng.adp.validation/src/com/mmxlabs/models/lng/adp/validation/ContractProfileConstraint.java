/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.time.YearMonth;
import java.util.List;
import java.util.OptionalInt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.VesselUsageDistribution;
import com.mmxlabs.models.lng.adp.VesselUsageDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.adp.utils.AdpPeriod;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractProfileConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final ContractProfile<?, ?> profile) {

			DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName("ADP Profile") //
					.withTag(ValidationConstants.TAG_ADP);

			if (profile.getContract() == null) {
				factory.copyName() //
						.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__CONTRACT) //
						.withMessage("No contract specified") //
						.make(ctx, statuses);
			}
			// Update name
			factory = factory.withTypedName("ADP Profile", DetailConstraintStatusFactory.getName(profile.getContract(), "<Unknown contract>"));

			if (profile.isEnabled()) {

				if (profile.getSubProfiles().isEmpty()) {
					factory.copyName() //
							.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES) //
							.withMessage("No volume profiles defined") //
							.make(ctx, statuses);
				}

				final MMXRootObject rootObject = extraContext.getRootObject();

				if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
					final ADPModel adpModel = ScenarioModelUtil.getADPModel(lngScenarioModel);
					if (adpModel != null) {
						final YearMonth start = adpModel.getYearStart();
						final YearMonth end = adpModel.getYearEnd();

						final Contract contract = profile.getContract();
						if (contract.isSetStartDate()) {
							if (contract.getStartDate().isAfter(end)) {
								factory.copyName() //
										.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__ENABLED) //
										.withMessage("Contract start date is outside of the ADP period") //
										.make(ctx, statuses);
							}
						}
						if (contract.isSetEndDate()) {
							if (contract.getEndDate().isBefore(start)) {
								factory.copyName() //
										.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__ENABLED) //
										.withMessage("Contract end date is outside of the ADP period") //
										.make(ctx, statuses);
							}
						}

						final Pair<YearMonth, YearMonth> adpPeriod = ADPModelUtil.getContractProfilePeriod(adpModel, contract);

						if (adpPeriod == null) {
							factory.copyName() //
									.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__ENABLED) //
									.withMessage("Contract period is outside of the ADP period") //
									.make(ctx, statuses);
						}

						final LNGVolumeUnit unit = profile.getVolumeUnit();
						if (!profile.getSubProfiles().isEmpty()) {
							for (var subProfile : profile.getSubProfiles()) {

								final DistributionModel model = subProfile.getDistributionModel();

								if (model != null && model.isSetVolumeUnit()) {
									if (unit != model.getVolumeUnit()) {
										factory.copyName() //
												.withObjectAndFeature(adpModel, ADPPackage.Literals.CONTRACT_PROFILE__VOLUME_UNIT) //
												.withMessage("Not compatible volume units") //
												.make(ctx, statuses);
									}
								}
							}
						}
						if (start != null && end != null) {
							final AdpPeriod adpYearPeriod = new AdpPeriod(start, end);
							final List<VesselUsageDistribution> vesselUsageDistributions = profile.getConstraints().stream() //
									.filter(VesselUsageDistributionProfileConstraint.class::isInstance) //
									.map(VesselUsageDistributionProfileConstraint.class::cast) //
									.map(VesselUsageDistributionProfileConstraint::getDistributions) //
									.flatMap(List::stream) //
									.toList();
							if (!vesselUsageDistributions.isEmpty()) {
								final List<PeriodDistribution> coverageConstraints = profile.getConstraints().stream() //
										.filter(PeriodDistributionProfileConstraint.class::isInstance) //
										.map(PeriodDistributionProfileConstraint.class::cast) //
										.map(PeriodDistributionProfileConstraint::getDistributions) //
										.flatMap(List::stream) //
										.filter(dist -> adpYearPeriod.equalsRange(dist.getRange())) //
										.toList();
								final OptionalInt maxOfMinimumsCoverage = coverageConstraints.stream() //
										.filter(dist -> dist.isSetMinCargoes()) //
										.mapToInt(dist -> dist.getMinCargoes()) //
										.max();
								if (maxOfMinimumsCoverage.isEmpty()) {
									factory.copyName() //
											.withObjectAndFeature(profile, ADPPackage.eINSTANCE.getContractProfile_Constraints()) //
											.withMessage("Period constraints must have minimum bound for ADP period") //
											.make(ctx, statuses);
								}
								final OptionalInt minOfMaximumsCoverage = coverageConstraints.stream() //
										.filter(dist -> dist.isSetMaxCargoes()) //
										.mapToInt(dist -> dist.getMaxCargoes()) //
										.min();
								if (minOfMaximumsCoverage.isEmpty()) {
									factory.copyName() //
											.withObjectAndFeature(profile, ADPPackage.eINSTANCE.getContractProfile_Constraints()) //
											.withMessage("Period constraints must have minimum bound over ADP period") //
											.make(ctx, statuses);
								}
								if (maxOfMinimumsCoverage.isPresent() && minOfMaximumsCoverage.isPresent()) {
									int minBound = maxOfMinimumsCoverage.getAsInt();
									int maxBound = minOfMaximumsCoverage.getAsInt();
									if (minBound != maxBound) {
										factory.copyName() //
											.withObjectAndFeature(profile, ADPPackage.eINSTANCE.getContractProfile_Constraints()) //
											.withMessage("Period constraints must have equal min and max bound over ADP period") //
											.make(ctx, statuses);
									} else {
										final int impliedSlotCoverage = vesselUsageDistributions.stream() //
												.mapToInt(dist -> dist.getCargoes()) //
												.sum();
										if (impliedSlotCoverage != minBound) {
											factory.copyName() //
											.withObjectAndFeature(profile, ADPPackage.eINSTANCE.getContractProfile_Constraints()) //
											.withMessage("Number of slots required by vessel usage must equal number required by profile constraints") //
											.make(ctx, statuses);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
