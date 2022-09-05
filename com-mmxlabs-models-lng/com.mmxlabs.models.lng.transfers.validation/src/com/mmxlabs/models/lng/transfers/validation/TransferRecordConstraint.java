package com.mmxlabs.models.lng.transfers.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.transfers.TransferIncoterm;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class TransferRecordConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL)) {
			final EObject object = ctx.getTarget();
			if (object instanceof TransferRecord transferRecord) {
				final String name = transferRecord.getName();
				if (transferRecord.getTransferAgreement() == null) {
					final String failureMessage = String.format("[%s]: transfer agreement must be set.", name);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
					dsd.addEObjectAndFeature(transferRecord, TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT);
					statuses.add(dsd);
				}
				if (transferRecord.eIsSet(TransfersPackage.Literals.TRANSFER_RECORD__INCOTERM) && transferRecord.getIncoterm() == TransferIncoterm.BOTH) {
					final String failureMessage = String.format("[%s]: incoterm for the record must not be \"Both\".", name);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
					dsd.addEObjectAndFeature(transferRecord, TransfersPackage.Literals.TRANSFER_RECORD__INCOTERM);
					statuses.add(dsd);
				}
				
				if (transferRecord.eIsSet(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS) &&
						transferRecord.eIsSet(TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION)) {
					final String failureMessage = String.format("[%s]: only one of the two, price expression or pricing basis, should be set", name);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
					dsd.addEObjectAndFeature(transferRecord, TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION);
					dsd.addEObjectAndFeature(transferRecord, TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS);
					statuses.add(dsd);
				}
				if (transferRecord.eIsSet(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS)) {
					validatePrice(ctx, statuses, transferRecord.getPricingBasis(), PriceIndexType.PRICING_BASIS, name, transferRecord, TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS);
				}
				if (transferRecord.eIsSet(TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION)) {
					validatePrice(ctx, statuses, transferRecord.getPriceExpression(), PriceIndexType.COMMODITY, name, transferRecord, TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION);
				}
			}
		}
	}
	
	private void validatePrice(final IValidationContext ctx, final List<IStatus> statuses, final String price, final PriceIndexType type, final String targetName, //
			final EObject target, final EStructuralFeature feature) {
		if (price == null || price.isBlank()) {
			final String failureMessage = String.format("[%s]: price must be set.", targetName);
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
			dsd.addEObjectAndFeature(target, feature);
			statuses.add(dsd);
		} else {
			final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, target, feature, price, type);
			if (!result.isOk()) {
				final String message = String.format("[%s]: %s", targetName, result.getErrorDetails());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(target, feature);
				statuses.add(dsd);
			}
		}
	}
}
