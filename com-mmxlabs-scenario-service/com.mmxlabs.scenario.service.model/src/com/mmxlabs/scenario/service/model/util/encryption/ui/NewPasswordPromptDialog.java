/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewPasswordPromptDialog extends Dialog {

	private Text nameField;
	private String name;

	public NewPasswordPromptDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Enter name for new key");
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite comp = (Composite) super.createDialogArea(parent);

		final GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;
		{
			final Label nameLabel = new Label(comp, SWT.RIGHT);
			nameLabel.setText("Name: ");

			nameField = new Text(comp, SWT.SINGLE | SWT.BORDER);
			final GridData data = new GridData(GridData.FILL_HORIZONTAL);
			nameField.setLayoutData(data);
		}

		return comp;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control c = super.createButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		return c;

	}

	@Override
	protected void okPressed() {
		name = nameField.getText();
		super.okPressed();
	}

	public String getName() {
		return name;
	}
}
