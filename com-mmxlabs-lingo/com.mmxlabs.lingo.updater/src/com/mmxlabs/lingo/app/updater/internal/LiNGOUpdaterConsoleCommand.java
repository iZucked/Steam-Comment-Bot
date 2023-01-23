/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater.internal;

import java.util.Objects;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.app.updater.LiNGOUpdater;

public class LiNGOUpdaterConsoleCommand implements CommandProvider {

	public static final String COMMAND = "command";

	public void _updateapp(CommandInterpreter ci) throws Exception {

		String source = null;
		String user = null;
		String pw = null;

		String arg = null;

		boolean promptPassword = false;
		boolean disableSigCheck = false;

		while ((arg = ci.nextArgument()) != null) {

			if (Objects.equals("nosig", arg)) {
				disableSigCheck = true;
				continue;
			}
			if (Objects.equals("prompt", arg)) {
				promptPassword = true;
				continue;
			}

			if (source == null) {
				source = arg;
			} else if (user == null) {
				user = arg;
			} else if (pw == null) {
				pw = arg;
			}
		}

		LiNGOUpdater u = new LiNGOUpdater();

		if (promptPassword) {
			final String[] password = new String[1];
			final Display display = PlatformUI.getWorkbench().getDisplay();
			display.syncExec(() -> {
				final PasswordPromptDialog dialog = new PasswordPromptDialog(display.getActiveShell());
				dialog.setBlockOnOpen(true);
				if (dialog.open() == Window.OK) {
					password[0] = dialog.getPassword();
				}
			});

			if (password[0] == null) {
				return;
			}
			pw = password[0];
		}

		u.setAuth(user, pw);
		if (disableSigCheck) {
			u.disableSigCheck(true);
		}

		final String s = source;
		Display.getDefault().asyncExec(() -> {
			try {
				u.updateWithDialog(s);
			} catch (Exception e) {
				e.printStackTrace();

				Display.getDefault().asyncExec(() -> {
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error updating", e.getMessage());

				});
			}
		});

	}

	@Override
	public String getHelp() {
//		StringBuilder buffer = new StringBuilder();
//		buffer.append("updateapp <url> <user> <pass> - Update with optional URL and basic auth credentials\n");
		return null;// buffer.toString();
	}

	private class PasswordPromptDialog extends Dialog {

		private Text passwordField;

		private String password;

		public PasswordPromptDialog(final Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected void configureShell(final Shell newShell) {
			super.configureShell(newShell);
			newShell.setText("Enter password to access update");
		}

		@Override
		protected Control createDialogArea(final Composite parent) {
			final Composite comp = (Composite) super.createDialogArea(parent);

			final GridLayout layout = (GridLayout) comp.getLayout();
			layout.numColumns = 2;
//			{
//				final Label nameLabel = new Label(comp, SWT.RIGHT);
//				nameLabel.setText("Name: ");
			//
//				Text nameField = new Text(comp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
//				final GridData data = new GridData(GridData.FILL_HORIZONTAL);
//				nameField.setLayoutData(data);
//				nameField.setText(name);
//			}
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
			password = passwordField.getText();
			super.okPressed();
		}

		public String getPassword() {
			return password;
		}
	}
}
