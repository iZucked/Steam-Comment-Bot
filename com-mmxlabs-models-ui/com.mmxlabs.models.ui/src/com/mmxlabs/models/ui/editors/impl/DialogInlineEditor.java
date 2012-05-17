/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public abstract class DialogInlineEditor extends BasicAttributeInlineEditor {
	public DialogInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}
	private Button button;
	private Label description;
	
	protected Shell getShell() {
		return button.getShell();
	}
	
	@Override
	public Control createControl(final Composite parent) {
		final Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayout(new GridLayout(2, false));
		
		final Label description = new Label(contents, SWT.WRAP);
		description.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
		
		final Button button = new Button(contents, SWT.NONE);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		button.setText("Edit");
		this.description = description;
		this.button = button;
		button.addSelectionListener(
				new SelectionListener() {
					{
						final SelectionListener sl = this;
						button.addDisposeListener(
								new DisposeListener() {
									@Override
									public void widgetDisposed(final DisposeEvent e) {
										button.removeSelectionListener(sl);
									}
								});
					}
					
					@Override
					public void widgetSelected(final SelectionEvent e) {
						final Object o = displayDialog(getValue());
						if (o != null) {
							doSetValue(o);
							updateDisplay(getValue());
						}
					}
					
					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {}
				}
				);
		
		return super.wrapControl(contents);
	}

	@Override
	protected void updateDisplay(final Object value) {
		if (!description.isDisposed())
			description.setText(render(value));
	}

	protected abstract Object displayDialog(final Object currentValue);
	protected abstract String render(final Object value);
}
