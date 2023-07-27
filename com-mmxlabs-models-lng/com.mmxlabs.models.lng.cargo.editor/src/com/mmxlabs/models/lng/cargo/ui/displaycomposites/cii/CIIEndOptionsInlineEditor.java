package com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.impl.AbstractComboInlineEditor;
import com.mmxlabs.models.ui.editors.impl.ValueListInlineEditor;

class CIIEndOptionsInlineEditor extends ValueListInlineEditor<String> {

	public CIIEndOptionsInlineEditor() {
		super(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE, List.of(
				Pair.of("Not specified", null),
				Pair.of("A", "A"),
				Pair.of("B", "B"),
				Pair.of("C", "C"),
				Pair.of("D", "D"),
				Pair.of("E", "E")
		));
	}
}

//class CIIEndOptionsInlineEditor extends AbstractComboInlineEditor {
//	
//	public CIIEndOptionsInlineEditor() {
//		super(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE, SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
//	}
//
//	@Override
//	protected void doSelectionChange(IStructuredSelection sel) {
//		if (sel.getFirstElement() instanceof String s) {
//			doSetValue(s, true);
//		}
//	}
//
//	@Override
//	protected void setProposalHelper() {
//		// //
//	}
//
//	@Override
//	protected void setInitialValues() {
//		addValues(Lists.newArrayList("A", "B", "C", "D", "E"), false);
//	}
//
//	@Override
//	protected void updateValueDisplay(Object value) {
//		if (combo == null || ccombo.isDisposed()) {
//			return;
//		}
//		combo.getCombo().setText(String.valueOf(value));
//	}
//}