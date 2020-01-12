/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.dialogs;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

public class AuthDetailsPromptDialog extends Dialog {

	private Text passwordField;

	private char[] password;

	private Text nameField;

	private String username;
	
	private String url;
	
	private boolean errorLabelPresent = false;
	
	public AuthDetailsPromptDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Authentication for LiNGO Hub");
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
	
	private void saveToSecureStorage(String username, String password) {
		 ISecurePreferences preferences = SecurePreferencesFactory
                 .getDefault();
         ISecurePreferences node = preferences.node("upstream");
         try {
             node.put("username", username, true);
             node.put("password", password, true);
         } catch (StorageException e1) {
             e1.printStackTrace();
         }		
	}
	
	@Override
	protected void okPressed() {
		if (!UpstreamUrlProvider.INSTANCE.testUpstreamAvailability(url)) {
			super.okPressed();
			return;
		}

		if (!UpstreamUrlProvider.INSTANCE.checkCredentials(url, nameField.getText(), passwordField.getText())) {
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
		
		saveToSecureStorage(username, passwordField.getText());
		
		super.okPressed();
	}

	public char[] getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}
}
