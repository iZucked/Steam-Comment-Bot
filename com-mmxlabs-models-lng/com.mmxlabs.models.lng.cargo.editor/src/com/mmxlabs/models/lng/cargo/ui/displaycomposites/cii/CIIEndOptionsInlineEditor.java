package com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.impl.AbstractComboInlineEditor;

class CIIEndOptionsInlineEditor extends AbstractComboInlineEditor {
	
	public CIIEndOptionsInlineEditor() {
		super(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE);
	}

	@Override
	protected void doSelectionChange(IStructuredSelection sel) {
		if (sel.getFirstElement() instanceof String s) {
			doSetValue(s, true);
		}
	}

	@Override
	protected void setProposalHelper() {
		// //
	}

	@Override
	protected void setInitialValues() {
		addValues(List.of("A", "B", "C", "D", "E"), false);
	}

	@Override
	protected void updateValueDisplay(Object value) {
		if (combo == null || ccombo.isDisposed()) {
			return;
		}
		combo.getCombo().setText(String.valueOf(value));
	}
}