/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractParamsConstraintHelper {

	public static void pricingEventConstraint(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, Contract contract, PricingEvent restrictedEvent,
			final String contractName) {
		if (contract.getPricingEvent() != restrictedEvent) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Contract|%s Pricing Event must be set to %s with %s contracts.", contract.getName(), restrictedEvent, contractName)));
			dsd.addEObjectAndFeature(contract, CargoPackage.Literals.SLOT__PRICING_EVENT);
			failures.add(dsd);
		}
	}

	public static void purchaseContractTypeConstraint(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, Contract contract,
			ContractType expectedContractType) {

		if (contract.getContractType() != expectedContractType) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Contract|%s should be a %s purchase contract.", contract.getName(), getContractTypeString(expectedContractType))));
			dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__CONTRACT_TYPE);
			failures.add(dsd);
		}
	}

	public static void salesContractTypeConstraint(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, Contract contract,
			ContractType expectedContractType) {

		if (contract.getContractType() != expectedContractType) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Contract|%s should be a %s sales contract.", contract.getName(), getContractTypeString(expectedContractType))));
			dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__CONTRACT_TYPE);
			failures.add(dsd);
		}
	}

	@NonNull
	public static String getContractTypeString(@NonNull ContractType type) {
		switch (type) {
		case BOTH:
			return "both FOB and DES";
		case DES:
			return "DES";
		case FOB:
			return "FOB";
		default:
			throw new IllegalStateException("Unknown Contract type");
		}

	}
}
