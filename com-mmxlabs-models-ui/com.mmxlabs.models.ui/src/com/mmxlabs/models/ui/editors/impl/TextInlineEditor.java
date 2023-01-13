/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;

public class TextInlineEditor extends UnsettableInlineEditor {
	private Text text;
	protected final int style;

	public TextInlineEditor(final ETypedElement typedElement) {
		this(typedElement, SWT.BORDER);
	}

	/**
	 * @param inputPath
	 * @param annotatedObject_Notes
	 * @param editingDomain
	 * @param commandProcessor
	 * @param i
	 */
	public TextInlineEditor(final ETypedElement typedElement, final int style) {
		super(typedElement);
		this.style = style;
	}

	@Override
	public Control createValueControl(final Composite parent) {

		text = createText(parent);

		text.addModifyListener(new ModifyListener() {
			{
				final ModifyListener ml = this;
				text.addDisposeListener(e -> text.removeModifyListener(ml));
			}

			@Override
			public void modifyText(final ModifyEvent e) {
				TextInlineEditor.this.doSetValue(text.getText(), false);
			}
		});

		return text;
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (text.isDisposed())
			return;
		text.setText(value == null ? "" : value.toString());
	}

	@Override
	protected Object getInitialUnsetValue() {
		return "";
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		if (text != null && !text.isDisposed()) {
			text.setEnabled(controlsEnabled);
		}

		super.setControlsEnabled(controlsEnabled);
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
		this.proposalHelper = createProposalHelper(text);
		return text;
	}

	protected IMMXContentProposalProvider createProposalHelper(final Text text) {
		return AutoCompleteHelper.createControlProposalAdapter(text, typedElement);
	}
}
