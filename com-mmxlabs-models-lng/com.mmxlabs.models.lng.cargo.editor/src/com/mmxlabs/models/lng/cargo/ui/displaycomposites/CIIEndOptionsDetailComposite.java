package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.List;

import org.eclipse.emf.common.notify.impl.BasicNotifierImpl.EScannableAdapterList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.AbstractComboInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class CIIEndOptionsDetailComposite extends DefaultDetailComposite {

	public CIIEndOptionsDetailComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {
		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withFeature(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE, "Rating", 75) //
				.makeRow() //
				.make();
	}
	
	@Override
	public @Nullable IInlineEditor addInlineEditor(IInlineEditor editor) {
		if (editor.getFeature().equals(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE)) {
			return super.addInlineEditor(new CIIEndOptionsInlineEditor(editor.getEditorTarget()));
		}
		return super.addInlineEditor(editor);
	}
	
	static class CIIEndOptionsInlineEditor extends AbstractComboInlineEditor {
		
		public CIIEndOptionsInlineEditor(EObject editorTarget) {
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
}
