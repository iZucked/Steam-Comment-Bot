package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * Combo box editor with support for proposal helper to autocomplete price expression
 * and a functionality to select
 * pre-set values from the drop-down list
 * @author FM
 *
 */
public abstract class AbstractComboInlineEditor extends UnsettableInlineEditor {
	
	protected ComboViewer combo;
	protected Control ccombo;
	protected boolean modifyListenerEnabled = true;
	
	protected final List<Object> valueList = new ArrayList<>();

	protected AbstractComboInlineEditor(final ETypedElement feature) {
		super(feature);
	}
	
	@Override
	protected Control createValueControl(Composite parent) {
		this.combo = new ComboViewer(parent, SWT.FLAT | SWT.BORDER);
		combo.setContentProvider(new ArrayContentProvider());
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(final Object element) {

				// Is the element missing?
				if (element == null || "".equals(element)) {
					return "";
				}

				if (element instanceof NamedObject namedObject) {
					return namedObject.getName();
				}
				return super.getText(element);
			}
		});
		this.ccombo = combo.getControl();
		toolkit.adapt(ccombo, true, true);

		if (typedElement instanceof EOperation) {
			ccombo.setEnabled(false);
		} else if (typedElement instanceof EStructuralFeature feature) {
			ccombo.setEnabled(feature.isChangeable() && isEditorEnabled());
		}
		
		combo.addSelectionChangedListener(event -> {
			final IStructuredSelection sel = combo.getStructuredSelection();
			if (sel != null) {
				doSelectionChange(sel);
			}
		});
		
		combo.getCombo().addModifyListener(e -> {
			
			if (modifyListenerEnabled && combo.getCombo().getSelection() != null) {
				doSetValue(combo.getCombo().getText(), true);
			}
			
		});

		setInitialValues();
		setProposalHelper();
		
		return super.wrapControl(ccombo);
	}
	
	public void addValues(final List<Object> values, final boolean clear) {
		try {
			modifyListenerEnabled = false;
			if (combo != null && !ccombo.isDisposed()) {
				if (clear) {
					valueList.clear();
				}
				if (!values.isEmpty()) {
					valueList.addAll(0, values);
				}
				combo.setInput(valueList);
			}
		} finally {
			modifyListenerEnabled = true;
		}
	}
	
	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}
	
	protected abstract void doSelectionChange(final IStructuredSelection sel);
	
	protected abstract void setProposalHelper();
	
	protected abstract void setInitialValues();
}
