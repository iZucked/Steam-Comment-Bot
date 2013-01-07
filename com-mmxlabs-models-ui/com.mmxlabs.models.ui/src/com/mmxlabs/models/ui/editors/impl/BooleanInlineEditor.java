/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BooleanInlineEditor extends BasicAttributeInlineEditor {
	private Button button;

	public BooleanInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public Control createControl(final Composite parent) {
		final Button button = new Button(parent, SWT.CHECK);

		button.addSelectionListener(new SelectionListener() {
			{
				final SelectionListener sl = this;
				button.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						button.removeSelectionListener(sl);
					}
				});
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				doSetValue((Boolean) button.getSelection(), false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.button = button;

		return super.wrapControl(button);
	}

	@Override
	protected void updateDisplay(final Object value) {
		if (button.isDisposed())
			return;
		if (Boolean.TRUE.equals(value)) {
			this.button.setSelection(true);
		} else {
			this.button.setSelection(false);
		}
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {

		super.setControlsEnabled(enabled);
		button.setEnabled(enabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		super.setControlsVisible(visible);
		button.setVisible(visible);
	}
}
