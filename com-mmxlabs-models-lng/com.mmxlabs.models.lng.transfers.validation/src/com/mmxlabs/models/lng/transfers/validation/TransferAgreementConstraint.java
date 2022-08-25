package com.mmxlabs.models.lng.transfers.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
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
			if (object instanceof TransferAgreement transferAgreement) {
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
				if (transferAgreement.getFromEntity() != null && transferAgreement.getToEntity() != null //
						&& transferAgreement.getFromEntity().equals(transferAgreement.getToEntity())) {
					final String failureMessage = String.format("[%s]: source and destination  entities must differ.", name);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
					dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__FROM_ENTITY);
					dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__TO_ENTITY);
					statuses.add(dsd);
				}
				if (transferAgreement.getPricingBasis() == null || transferAgreement.getPricingBasis().isBlank()) {
					if (transferAgreement.getPriceExpression() == null || transferAgreement.getPriceExpression().isBlank()) {
						final String failureMessage = String.format("[%s]: price expression or pricing basis must be set.", name);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
						dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION);
						dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICING_BASIS);
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
				} else {
					if (transferAgreement.getPriceExpression() == null || transferAgreement.getPriceExpression().isBlank()) {
						final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICING_BASIS,
								transferAgreement.getPricingBasis(), PriceIndexType.PRICING_BASIS);
						if (!result.isOk()) {
							final String message = String.format("[%s]: %s", name, result.getErrorDetails());
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICING_BASIS);
							statuses.add(dsd);
						}
					} else {
						final String failureMessage = String.format("[%s]: only one of the two, price expression or pricing basis, must be set.", name);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
						dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICE_EXPRESSION);
						dsd.addEObjectAndFeature(transferAgreement, TransfersPackage.Literals.TRANSFER_AGREEMENT__PRICING_BASIS);
						statuses.add(dsd);
					}
				}
			}
		}
	}
}
