/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class SubContractProfileConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof SubContractProfile<?, ?>) {
			final SubContractProfile<?, ?> subContractProfile = (SubContractProfile<?, ?>) target;
			String name = "<unknown>";
			if (subContractProfile.eContainer() instanceof ContractProfile<?, ?>) {
				final ContractProfile<?, ?> contractProfile = (ContractProfile<?, ?>) subContractProfile.eContainer();
				final Contract contract = contractProfile.getContract();
				if (contract != null) {
					final String n = contract.getName();
					if (n != null && !n.isEmpty()) {
						name = n;
					}
				}

			}
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", name) //
					.withTag(ValidationConstants.TAG_ADP);

			if (subContractProfile.getDistributionModel() == null) {
				factory.copyName() //
						.withObjectAndFeature(subContractProfile, ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL) //
						.withMessage("No distribution model selected") //
						.make(ctx, statuses);
			}
			if (subContractProfile.getContractType() == null || subContractProfile.getContractType() == ContractType.BOTH) {
				factory.copyName() //
						.withObjectAndFeature(subContractProfile, ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONTRACT_TYPE) //
						.withMessage("Contract type should be FOB or DES") //
						.make(ctx, statuses);
			}
		}

	}
}
