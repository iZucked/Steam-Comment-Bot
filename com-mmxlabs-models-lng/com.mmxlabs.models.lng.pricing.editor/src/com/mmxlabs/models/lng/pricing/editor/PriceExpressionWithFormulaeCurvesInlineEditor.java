/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.ExpressionAnnotationConstants;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

public class PriceExpressionWithFormulaeCurvesInlineEditor extends UnsettableInlineEditor {

	protected ComboViewer combo;
	private Control ccombo;
	private boolean modifyListenerEnabled = true;
	
	protected final List<EObject> valueList = new ArrayList<>();
	
	public PriceExpressionWithFormulaeCurvesInlineEditor(final ETypedElement typedElement) {
		super(typedElement);
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
				if (sel.getFirstElement() instanceof NamedObject pfw) {
					doSetValue(pfw.getName(), true);
				} else {
					doSetValue(sel.getFirstElement(), true);
				}
			}
		});
		
		combo.getCombo().addModifyListener(e -> {
			
			if (modifyListenerEnabled && combo.getCombo().getSelection() != null) {
				doSetValue(combo.getCombo().getText(), true);
			}
			
		});

		this.proposalHelper = AutoCompleteHelper.createControlProposalAdapter(ccombo, ExpressionAnnotationConstants.TYPE_COMMODITY);
		EditingDomain editingDomain = commandHandler.getEditingDomain();
		for (Resource r : editingDomain.getResourceSet().getResources()) {
			for (EObject o : r.getContents()) {
				if (o instanceof MMXRootObject mmxo) {
					this.proposalHelper.setRootObject(mmxo);
				}
			}
		}
		
		return super.wrapControl(ccombo);
	}
	
	public void addValues(final List<? extends EObject> values, final boolean clear) {
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
	protected void updateDisplay(final Object value) {
		if (value == null) {
			if (combo == null || ccombo.isDisposed()) {
				return;
			}
			combo.getCombo().setText("");
		} else {
			super.updateDisplay(value);
		}
	}
	
	@Override
	protected void updateValueDisplay(Object value) {
		if (combo == null || ccombo.isDisposed()) {
			return;
		}
		if (value instanceof NamedObject no) {
			combo.getCombo().setText(no.getName());
		} else if (value instanceof String s) {
			combo.getCombo().setText(s);
		} else {
			combo.getCombo().setText("");
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}
	
	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT.equals(changedFeature) ||
				CargoPackage.Literals.SLOT__CONTRACT.equals(changedFeature) ||
				super.updateOnChangeToFeature(changedFeature);
	}

}
