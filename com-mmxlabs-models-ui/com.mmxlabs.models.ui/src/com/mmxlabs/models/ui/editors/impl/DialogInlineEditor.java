/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public abstract class DialogInlineEditor extends UnsettableInlineEditor {
	public DialogInlineEditor(final ETypedElement feature) {
		super(feature);
	}

	private Button button;
	private Label description;

	protected Shell getShell() {
		return button.getShell();
	}

	/**
	 */
	@Override
	public Control createValueControl(final Composite parent) {
		final Composite contents = toolkit.createComposite(parent);
		contents.setLayout(new GridLayout(2, false));

		this.description = toolkit.createLabel(contents, "", SWT.WRAP);
		description.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		this.button = toolkit.createButton(contents, "", SWT.NONE);
		button.setImage(CommonImages.getImage(IconPaths.Edit, IconMode.Enabled));

		button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		final SelectionAdapter selectionListener = new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final Object o = displayDialog(getValue());
				if (o != null) {
					doSetValue(o, false);
					updateDisplay(getValue());
				}
			}
		};
		button.addSelectionListener(selectionListener);
		button.addDisposeListener(e -> button.removeSelectionListener(selectionListener));

		return super.wrapControl(contents);
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (!description.isDisposed())
			description.setText(render(value));
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;
		super.setControlsEnabled(controlsEnabled);
		button.setEnabled(controlsEnabled);
		description.setEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		super.setControlsVisible(visible);
		button.setVisible(visible);
		description.setVisible(visible);
	}

	protected abstract Object displayDialog(final Object currentValue);

	protected abstract String render(final Object value);
}
