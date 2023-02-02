/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.editor.PricingBasisInlineEditor;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.editor.PricingBasisWrapper;
import com.mmxlabs.models.lng.transfers.editor.factories.CompanyStatusInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.SimpleOperationInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class TransferRecordComponentHelper extends DefaultComponentHelper {

	public TransferRecordComponentHelper() {
		super(TransfersPackage.Literals.TRANSFER_RECORD);
		this.includeEOperations = true;
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY, topClass -> {
			return new SimpleOperationInlineEditor("From", TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY) {
				@Override
				protected boolean updateOnChangeToFeature(final Object changedFeature) {
					return TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT.equals(changedFeature);
				}
			};
		});
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD___GET_TO_ENTITY, topClass -> {
			return new SimpleOperationInlineEditor("To", TransfersPackage.Literals.TRANSFER_RECORD___GET_TO_ENTITY) {
				@Override
				protected boolean updateOnChangeToFeature(final Object changedFeature) {
					return TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT.equals(changedFeature);
				}
			};
		});
		if(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PRICING_BASES)) {
			addEditor(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS, topClass -> {
				return new PricingBasisWrapper(new PricingBasisInlineEditor(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS));
			});
		} else {
			ignoreFeatures.add(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS);
		}
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD___GET_COMPANY_STATUS, topClass -> {
			return new CompanyStatusInlineEditor("Type", TransfersPackage.Literals.TRANSFER_RECORD___GET_COMPANY_STATUS);
		});
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD__NOTES, topClass -> new MultiTextInlineEditor(TransfersPackage.Literals.TRANSFER_RECORD__NOTES));
	}

}
