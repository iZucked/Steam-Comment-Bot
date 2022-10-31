/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;

public class SimpleOperationInlineEditor extends BasicOperationInlineEditor {
	protected Text text;
	protected final int style;
	private FormToolkit toolkit;
	private String name;

	public SimpleOperationInlineEditor(final String name, final ETypedElement typedElement) {
		this(typedElement, SWT.BORDER);
		this.name = name;
	}

	/**
	 * @param inputPath
	 * @param annotatedObject_Notes
	 * @param editingDomain
	 * @param commandProcessor
	 * @param i
	 */
	public SimpleOperationInlineEditor(final ETypedElement typedElement, final int style) {
		super(typedElement);
		this.style = style;
	}
	
	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		this.toolkit = toolkit;
		final Composite composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(1, false));

		this.text = createText(composite);
		text.setEnabled(false);
		
		return composite;
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		// Do nothing
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
		if (!text.isDisposed()) {
			text.setVisible(visible);
		}

		super.setControlsVisible(visible);
	}

	protected Text createText(final Composite parent) {
		final Text text = toolkit.createText(parent, "", style);
		text.setEditable(false);
		this.proposalHelper = AutoCompleteHelper.createControlProposalAdapter(text, typedElement);
		return text;
	}

	@Override
	protected void updateDisplay(Object value) {
		if (value instanceof String string) {
			text.setText(string);
		} else if (value instanceof NamedObject object) {
			text.setText(object.getName());
		}
	}
	
	@Override
	public void setLabel(final Label label) {
		this.label = label;
		this.label.setText(name);
	}

	/**
	 */
	@Override
	public Label getLabel() {
		return label;
	}
}
