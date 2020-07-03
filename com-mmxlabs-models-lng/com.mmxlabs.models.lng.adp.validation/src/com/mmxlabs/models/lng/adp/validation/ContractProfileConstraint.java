/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.time.YearMonth;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CargoSizeDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
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
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof ContractProfile<?, ?>) {
			final ContractProfile profile = (ContractProfile) target;

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
			factory = factory.withTypedName("ADP Profile", factory.getName(profile.getContract(), "<Unknown contract>"));

			if (profile.isEnabled()) {
				
				if (profile.getSubProfiles().isEmpty()) {
					factory.copyName() //
							.withObjectAndFeature(profile, ADPPackage.Literals.CONTRACT_PROFILE__SUB_PROFILES) //
							.withMessage("No volume profiles defined") //
							.make(ctx, statuses);
				}
				
				final MMXRootObject rootObject = extraContext.getRootObject();
				
				if (rootObject instanceof LNGScenarioModel) {
					final ADPModel adpModel = ScenarioModelUtil.getADPModel((LNGScenarioModel)rootObject);
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
						for (final Object subProfile : profile.getSubProfiles()) {
							if (subProfile instanceof SubContractProfile<?, ?>) {
								
								final DistributionModel model = ((SubContractProfile<?, ?>)subProfile).getDistributionModel();

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
					}
					
				}
				

				
				
			}
		}

		return Activator.PLUGIN_ID;
	}

}
