/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ADPModelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {
		EObject target = ctx.getTarget();

		if (target instanceof ADPModel) {
			ADPModel adpModel = (ADPModel) target;

			DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName("ADP Model") //
					.withTag(ValidationConstants.TAG_ADP);

			if (adpModel.getYearStart() == null) {
				factory.copyName() //
						.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_START) //
						.withMessage("No starting month") //
						.make(ctx, statuses);
			} else {
				if (adpModel.getYearStart().getYear() < 2010 || adpModel.getYearStart().getYear() > 2100) {
					factory.copyName() //
							.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_START) //
							.withMessage("Starting month is invalid") //
							.make(ctx, statuses);
				}
			}
			if (adpModel.getYearEnd() == null) {
				factory.copyName() //
						.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_END) //
						.withMessage("No ending month") //
						.make(ctx, statuses);
			} else {
				if (adpModel.getYearEnd().getYear() < 2010 || adpModel.getYearEnd().getYear() > 2100) {
					factory.copyName() //
							.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_END) //
							.withMessage("Ending month is invalid") //
							.make(ctx, statuses);
				}
			}
			if (adpModel.getYearStart() != null && adpModel.getYearEnd() != null) {
				if (adpModel.getYearStart().isAfter(adpModel.getYearEnd())) {
					factory.copyName() //
							.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_START) //
							.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_END) //
							.withMessage("Start month is after end month") //
							.make(ctx, statuses);
				} else {
					if (Months.between(adpModel.getYearStart(), adpModel.getYearEnd()) > 18) {
						factory.copyName() //
								.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_START) //
								.withObjectAndFeature(adpModel, ADPPackage.Literals.ADP_MODEL__YEAR_END) //
								.withMessage("ADP date range is too large: truncate to 18 month") //
								.make(ctx, statuses);
					}
				}
			}
			CargoModel cargoModel = ScenarioModelUtil.getCargoModel(extraContext.getScenarioDataProvider());
			int slotCount = cargoModel.getLoadSlots().size() + cargoModel.getDischargeSlots().size();
			if (slotCount == 0) {
				factory.copyName() //
						.withObjectAndFeature(adpModel, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS) //
						.withObjectAndFeature(adpModel, CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS) //
						.withMessage("No existing slots for ADP: re-generate slots") //
						.make(ctx, statuses);
			}
			for (final PurchaseContractProfile purchaseContractProfile : adpModel.getPurchaseContractProfiles()) {
				for (final SubContractProfile subContractProfile : purchaseContractProfile.getSubProfiles()) {
					if (subContractProfile.getPort() == null) {
						factory.copyName() //
								.withObjectAndFeature(adpModel, ADPPackage.Literals.SUB_CONTRACT_PROFILE__PORT) //
								.withMessage(String.format("Purchase contract %s profile will generate slots without ports", purchaseContractProfile.getContract().getName())) //
								.withSeverity(IStatus.WARNING) //
								.make(ctx, statuses);
					}
				}
			}
			for (final SalesContractProfile salesContractProfile : adpModel.getSalesContractProfiles()) {
				for (final SubContractProfile subContractProfile : salesContractProfile.getSubProfiles()) {
					if (subContractProfile.getPort() == null) {
						factory.copyName() //
								.withObjectAndFeature(adpModel, ADPPackage.Literals.SUB_CONTRACT_PROFILE__PORT) //
								.withMessage(String.format("Sales contract %s profile will generate slots without ports", salesContractProfile.getContract().getName())) //
								.withSeverity(IStatus.WARNING) //
								.make(ctx, statuses);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

}
