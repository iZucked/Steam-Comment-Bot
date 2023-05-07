/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.pricing.editor.PricingBasisInlineEditor;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.editor.PricingBasisWrapper;
import com.mmxlabs.models.lng.transfers.editor.factories.CompanyStatusInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;
import com.mmxlabs.models.ui.editors.impl.SimpleOperationInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class TransferRecordComponentHelper extends DefaultComponentHelper {

	public TransferRecordComponentHelper() {
		super(TransfersPackage.Literals.TRANSFER_RECORD);
		this.includeEOperations = true;
		//TODO: check layout for the feature:
		// ignoreFeatures.add(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS);
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY, topClass -> {
			return new SimpleOperationInlineEditor("From", TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY) {
				@Override
				protected boolean updateOnChangeToFeature(final Object changedFeature) {
					return TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT.equals(changedFeature);
				}
			};
		});
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD__FROM_BU, topClass -> {
			return new ReferenceInlineEditor(TransfersPackage.Literals.TRANSFER_RECORD__FROM_BU) {
				@Override
				protected void doSetOverride(final Object value, final boolean forceCommandExecution) {
					if (currentlySettingValue) {
						return;
					}
					if (value == null && !valueList.isEmpty()) {
						doSetValue(valueList.get(0), forceCommandExecution);
					} else {
						doSetValue(value, forceCommandExecution);
					}
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
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD__TO_BU, topClass -> {
			return new ReferenceInlineEditor(TransfersPackage.Literals.TRANSFER_RECORD__TO_BU) {
				@Override
				protected void doSetOverride(final Object value, final boolean forceCommandExecution) {
					if (currentlySettingValue) {
						return;
					}
					if (value == null && !valueList.isEmpty()) {
						doSetValue(valueList.get(0), forceCommandExecution);
					} else {
						doSetValue(value, forceCommandExecution);
					}
				}
			};
		});
		addEditorWithWrapperForLicenseFeature(KnownFeatures.FEATURE_PRICING_BASES, TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS, //
				PricingBasisInlineEditor::new, PricingBasisWrapper::new);
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION, topClass -> {
			return new TextInlineEditor(TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION) {
				@Override
				protected boolean updateOnChangeToFeature(final Object changedFeature) {
					return TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT.equals(changedFeature);
				}
				
				@Override
				protected void updateDisplay(final Object value) {
					if (value == null) {
						updateValueDisplay(value);
					} else {
						super.updateDisplay(value);
					}
				}
			};
		});
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD___GET_COMPANY_STATUS, topClass -> {
			return new CompanyStatusInlineEditor("Type", TransfersPackage.Literals.TRANSFER_RECORD___GET_COMPANY_STATUS);
		});
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD__NOTES, topClass -> new MultiTextInlineEditor(TransfersPackage.Literals.TRANSFER_RECORD__NOTES));
	}

}
