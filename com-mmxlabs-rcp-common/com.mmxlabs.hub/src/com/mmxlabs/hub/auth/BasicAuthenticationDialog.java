/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BasicAuthenticationDialog extends Dialog {

	private Text passwordField;

	private char[] password;

	private Text nameField;

	private String username;

	private String url;

	private boolean errorLabelPresent = false;
	private List<Runnable> disposeHandlers = new LinkedList<>();

	BasicAuthenticationManager authenticationManager = BasicAuthenticationManager.getInstance();

	public BasicAuthenticationDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		newShell.setLayout(super.getLayout());
		newShell.setText("Data Hub Basic Login");
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite comp = (Composite) super.createDialogArea(parent);

		final GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;
		{
			final Label nameLabel = new Label(comp, SWT.RIGHT);
			nameLabel.setText("Username: ");

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
		}
		return comp;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	protected void okPressed() {
		if (authenticationManager.checkCredentials(url, nameField.getText(), passwordField.getText())) {
			authenticationManager.storeInSecurePreferences("username", nameField.getText());
			authenticationManager.storeInSecurePreferences("password", passwordField.getText());
		} else {
			if (!errorLabelPresent) {

				final Composite comp = this.getContents().getParent();
				final Label errorLabel = new Label(comp, SWT.TOP | SWT.CENTER);
				errorLabel.setText("Error: invalid credentials");
				errorLabelPresent = true;
				comp.layout();
				comp.pack();
			}

			return;
		}

		username = nameField.getText();
		password = passwordField.getTextChars();

		super.okPressed();
	}

	public char[] getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void addDisposeListener(Runnable hook) {
		disposeHandlers.add(hook);
	}

	@Override
	public boolean close() {
		try {
			disposeHandlers.forEach(Runnable::run);
		} catch (Exception e) {
			e.printStackTrace();
		}
		disposeHandlers.clear();
		return super.close();
	}
}
