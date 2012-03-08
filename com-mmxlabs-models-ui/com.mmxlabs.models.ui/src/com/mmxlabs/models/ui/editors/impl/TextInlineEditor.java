/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class TextInlineEditor extends UnsettableInlineEditor {
	private Text text;
	private final int style;

	public TextInlineEditor(
			final EStructuralFeature feature) {
		this(feature, SWT.BORDER);
	}

	/**
	 * @param inputPath
	 * @param annotatedObject_Notes
	 * @param editingDomain
	 * @param commandProcessor
	 * @param i
	 */
	public TextInlineEditor(final EStructuralFeature feature, final int style) {
		super(feature);
		this.style = style;
	}

	@Override
	public Control createValueControl(Composite parent) {
		final Text text = new Text(parent, style);

		text.addModifyListener(new ModifyListener() {
			{
				final ModifyListener ml = this;
				text.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						text.removeModifyListener(ml);
					}
				});
			}

			@Override
			public void modifyText(ModifyEvent e) {
				TextInlineEditor.this.doSetValue(text.getText());
			}
		});

		this.text = text;

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
}
