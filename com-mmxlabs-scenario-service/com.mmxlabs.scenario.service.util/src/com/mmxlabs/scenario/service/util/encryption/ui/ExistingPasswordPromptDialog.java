/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExistingPasswordPromptDialog extends Dialog {

	private Text passwordField;

	private char[] password;

	public ExistingPasswordPromptDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Enter password to access scenario data");
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite comp = (Composite) super.createDialogArea(parent);

		final GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;
//		{
//			final Label nameLabel = new Label(comp, SWT.RIGHT);
//			nameLabel.setText("Name: ");
//
//			Text nameField = new Text(comp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
//			final GridData data = new GridData(GridData.FILL_HORIZONTAL);
//			nameField.setLayoutData(data);
//			nameField.setText(name);
//		}
		{
			final Label passwordLabel = new Label(comp, SWT.RIGHT);
			passwordLabel.setText("Password: ");

			passwordField = new Text(comp, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
			final GridData data = new GridData(GridData.FILL_HORIZONTAL);
			passwordField.setLayoutData(data);
		}
		return comp;
	}

	@Override
	protected void okPressed() {
		password = passwordField.getTextChars();
		super.okPressed();
	}

	public char[] getPassword() {
		return password;
	}
}
