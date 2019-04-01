/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DeliverToProfileFlow;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class DeliverToProfileFlowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof DeliverToProfileFlow) {
			final DeliverToProfileFlow flow = (DeliverToProfileFlow) target;

			if (flow.getProfile() == null) {
				createSimpleStatus(ctx, statuses, "Missing profile", flow, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__PROFILE);
			}
			if (flow.getSubProfile() == null) {
				createSimpleStatus(ctx, statuses, "Missing sub-profile", flow, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE);
			}

			if (flow.getProfile() != null) {
				SalesContractProfile toProfile = (SalesContractProfile) flow.getProfile();
				SubContractProfile<?, ?> rule = (SubContractProfile<?, ?>) flow.eContainer();
				if (rule != null) {
					PurchaseContractProfile fromProfile = (PurchaseContractProfile) rule.eContainer();

					Contract purchaseContract = fromProfile.getContract();
					Contract salesContract = toProfile.getContract();
					// Check purchase contract permissions
					{
						if (purchaseContract.isRestrictedContractsArePermissive()) {
							if (!purchaseContract.getRestrictedContracts().contains(salesContract)) {
								createSimpleStatus(ctx, statuses, "Purchase contract cannot supply this sales contract", flow, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__PROFILE);
							}
						} else {
							if (purchaseContract.getRestrictedContracts().contains(salesContract)) {
								createSimpleStatus(ctx, statuses, "Purchase contract cannot supply this sales contract", flow, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__PROFILE);
							}
						}
					}
					// Check sales contract permissions
					{
						if (salesContract.isRestrictedContractsArePermissive()) {
							if (!salesContract.getRestrictedContracts().contains(purchaseContract)) {
								createSimpleStatus(ctx, statuses, "Sales contract cannot be supplied from this purchase contract", flow, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__PROFILE);
							}
						} else {
							if (salesContract.getRestrictedContracts().contains(purchaseContract)) {
								createSimpleStatus(ctx, statuses, "Sales contract cannot be supplied from this purchase contract", flow, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__PROFILE);
							}
						}
					}
				}
			}

			if (flow.getProfile() != null && flow.getSubProfile() != null) {
				if (!flow.getProfile().getSubProfiles().contains(flow.getSubProfile())) {
					createSimpleStatus(ctx, statuses, "Invalid sub-profile", flow, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__PROFILE, ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
