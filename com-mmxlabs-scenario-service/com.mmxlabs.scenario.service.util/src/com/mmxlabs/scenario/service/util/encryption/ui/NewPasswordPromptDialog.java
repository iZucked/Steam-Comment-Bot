/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.ui;

import java.util.Arrays;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewPasswordPromptDialog extends Dialog {

	private Text nameField;
	private Text passwordField;
	private Text confirmField;

	private String name;

	private char[] password;

	public NewPasswordPromptDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Enter new password for new key");
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
		{
			final Label passwordLabel = new Label(comp, SWT.RIGHT);
			passwordLabel.setText("Password: ");

			passwordField = new Text(comp, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
			final GridData data = new GridData(GridData.FILL_HORIZONTAL);
			passwordField.setLayoutData(data);
			passwordField.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {
					final boolean enabled = passwordField.getTextChars().length > 0 && Arrays.equals(passwordField.getTextChars(), confirmField.getTextChars());
					getButton(IDialogConstants.OK_ID).setEnabled(enabled);
				}
			});
		}
		{
			final Label passwordLabel = new Label(comp, SWT.RIGHT);
			passwordLabel.setText("Confirm: ");

			confirmField = new Text(comp, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
			final GridData data = new GridData(GridData.FILL_HORIZONTAL);
			confirmField.setLayoutData(data);
			confirmField.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {
					final boolean enabled = passwordField.getTextChars().length > 0 && Arrays.equals(passwordField.getTextChars(), confirmField.getTextChars());
					getButton(IDialogConstants.OK_ID).setEnabled(enabled);
				}
			});
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
		password = passwordField.getTextChars();
		name = nameField.getText();
		super.okPressed();
	}

	public char[] getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}
}
