/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ThirdPartyContractEntityConstraint extends AbstractModelMultiConstraint {

	public static final String KEY_THIRD_PARTY_ENTITY = "ThirdPartyContractEntityConstraint.third-party-entity";

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof SalesContract contract) {
			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(contract), ScenarioElementNameHelper.getNonNullString(contract.getName()));

			if (contract.getEntity() != null && contract.getEntity().isThirdParty()) {
				if (contract.getContractType() != ContractType.FOB) {
					baseFactory.copyName() //
							.withFormattedMessage("Contract with a third-party entity should be a FOB sale contract") //
							.withObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_ContractType()) //
							.withConstraintKey(KEY_THIRD_PARTY_ENTITY) //
							.make(ctx, statuses);
				}

			}
		}

		if (target instanceof PurchaseContract contract) {
			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(contract), ScenarioElementNameHelper.getNonNullString(contract.getName()));

			if (contract.getEntity() != null && contract.getEntity().isThirdParty()) {
				if (contract.getContractType() != ContractType.DES) {
					baseFactory.copyName() //
							.withFormattedMessage("Contract with a third-party entity should be a DES purchase contract") //
							.withObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_ContractType()) //
							.withConstraintKey(KEY_THIRD_PARTY_ENTITY) //
							.make(ctx, statuses);
				}

			}
		}
	}

}
