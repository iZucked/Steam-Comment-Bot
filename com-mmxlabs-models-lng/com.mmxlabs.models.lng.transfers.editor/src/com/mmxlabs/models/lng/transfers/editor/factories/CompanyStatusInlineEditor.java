package com.mmxlabs.models.lng.transfers.editor.factories;

import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.lng.transfers.CompanyStatus;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.ui.editors.impl.SimpleOperationInlineEditor;

public class CompanyStatusInlineEditor extends SimpleOperationInlineEditor {

	public CompanyStatusInlineEditor(String name, ETypedElement typedElement) {
		super(name, typedElement);
	}

	@Override
	protected void updateDisplay(Object value) {
		if (value instanceof CompanyStatus status) {
			switch (status) {
			case INTRA: {
				text.setText("Intra-company");
				break;
			}
			case INTER: {
				text.setText("Inter-company");
				break;
			}
			default:
				text.setText(status.getLiteral());
			}
		} else {
			super.updateDisplay(value);
		}
	}
	
	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT.equals(changedFeature);
	}
	
}
