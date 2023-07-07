/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class TransferAgreementConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL)) {
			final EObject object = ctx.getTarget();
			if (object instanceof final TransferAgreement transferAgreement) {
				final String name = transferAgreement.getName();
				if (transferAgreement.getFromEntity() == null) {
					final String failureMessage = String.format("[%s]: source entity must be selected.", name);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
					dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_ENTITY);
					statuses.add(dsd);
				}
				if (transferAgreement.getToEntity() == null) {
					final String failureMessage = String.format("[%s]: destination entities must be selected.", name);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
					dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_ENTITY);
					statuses.add(dsd);
				}
				if (transferAgreement.getFromEntity() != null && transferAgreement.getToEntity() != null) {
					if (transferAgreement.getFromEntity().equals(transferAgreement.getToEntity())) {
						if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL_ALLOW_TRANSFER_WITING_ENTITY)) {
							final BusinessUnit fromBU = getFromBU(transferAgreement);
							final BusinessUnit toBU = getToBU(transferAgreement);
							if (fromBU != null && toBU != null && fromBU.equals(toBU)) {
								final String failureMessage = String.format("[%s]: source and destination business units must differ.", name);
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
								dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_BU);
								dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_BU);
								statuses.add(dsd);
							}
						} else {
							final String failureMessage = String.format("[%s]: source and destination entities must differ.", name);
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
							dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_ENTITY);
							dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_ENTITY);
							statuses.add(dsd);
						}
					}
				}
				
					if (transferAgreement.getPriceExpression() == null || transferAgreement.getPriceExpression().isBlank()) {
						final String failureMessage = String.format("[%s]: price expression must be set.", name);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
						dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION);
						statuses.add(dsd);
					} else {
						final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION,
								transferAgreement.getPriceExpression(), PriceIndexType.COMMODITY);
						if (!result.isOk()) {
							final String message = String.format("[%s]: %s", name, result.getErrorDetails());
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION);
							statuses.add(dsd);
						}
					}
				
				if (transferAgreement.getPreferredFormulae() != null && !transferAgreement.getPreferredFormulae().isEmpty()) {
					transferAgreement.getPreferredFormulae().forEach( w -> {
						final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, transferAgreement, //
								TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_FORMULAE, //
								w.getName(), PriceIndexType.COMMODITY);
						if (!result.isOk()) {
							final String message = String.format("[%s]: %s", name, result.getErrorDetails());
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dsd.addEObjectAndFeature(w, TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_FORMULAE);
							statuses.add(dsd);
						}
					});
				}
			}
		}
	}
	
	private BusinessUnit getToBU(final TransferAgreement transferAgreement) {
		if (transferAgreement.eIsSet(TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_BU)) {
			return transferAgreement.getToBU();
		}
		return transferAgreement.getAgreementOrDelegateToBU();
	}
	
	private BusinessUnit getFromBU(final TransferAgreement transferAgreement) {
		if (transferAgreement.eIsSet(TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_BU)) {
			return transferAgreement.getFromBU();
		}
		return transferAgreement.getAgreementOrDelegateFromBU();
	}
}
