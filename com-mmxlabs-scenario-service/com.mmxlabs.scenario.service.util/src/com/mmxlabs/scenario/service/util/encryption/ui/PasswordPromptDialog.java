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

public class PasswordPromptDialog extends Dialog {

	private Text nameField;
	private Text passwordField;

	private String name;

	private char[] password;

	public PasswordPromptDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Enter password to access scenario data");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);

		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;
		{
			Label nameLabel = new Label(comp, SWT.RIGHT);
			nameLabel.setText("Name: ");

			nameField = new Text(comp, SWT.SINGLE | SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			nameField.setLayoutData(data);
		}
		{
			Label passwordLabel = new Label(comp, SWT.RIGHT);
			passwordLabel.setText("Password: ");

			passwordField = new Text(comp, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			passwordField.setLayoutData(data);
		}
		return comp;
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
