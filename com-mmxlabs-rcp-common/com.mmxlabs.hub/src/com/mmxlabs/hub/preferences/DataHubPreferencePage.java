/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.preferences;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IDataHubStateChangeListener;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.UpstreamUrlProvider.StateReason;
import com.mmxlabs.hub.auth.AuthenticationManager;
import com.mmxlabs.hub.auth.OAuthManager;
import com.mmxlabs.license.features.LicenseFeatures;

public class DataHubPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final Logger LOG = LoggerFactory.getLogger(DataHubPreferencePage.class);

	private static AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	/*
	 * This listener fires whenever the Data Hub URL is modified but the changes have not yet been applied. Login will use the URL from the preferences which is saved only when the Apply button is
	 * pressed
	 */
	// display note and disable login button
	private final IPropertyChangeListener disableLogin = event -> disableLogin();
	private boolean detailsValid = true;

	public DataHubPreferencePage() {
		super(GRID);
		setPreferenceStore(DataHubActivator.getDefault().getPreferenceStore());
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(enableLoginListener);
	}

	@Override
	public void dispose() {

		DataHubServiceProvider.getInstance().removeDataHubStateListener(stateChangeListener);

		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(enableLoginListener);
		if (forceBasicAuth != null) {
			forceBasicAuth.dispose();
		}
		if (editor != null) {
			editor.dispose();
		}

		super.dispose();
	}

	protected String loginButtonText = "Login";
	protected String logoutButtonText = "Logout";
	protected Button loginButton;
	protected Button refreshButton;

	public void setLoginButtonText() {
		if (!loginButton.isDisposed()) {
			if (authenticationManager.isAuthenticated()) {
				loginButton.setText(logoutButtonText);
			} else {
				loginButton.setText(loginButtonText);
			}
		}
	}

	public void setLoginButtonEnabled() {
		loginButton.setEnabled(DataHubServiceProvider.getInstance().isHubOnline());
	}

	protected Label noteLabel;

	protected BooleanFieldEditor forceBasicAuth;
	protected BooleanFieldEditor preferEdgeBrowser;

	protected StringFieldEditor editor;

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getSource() == editor || event.getSource() == forceBasicAuth) {
			disableLogin();
		}
	}

	public void disableLogin() {
		// LOG.info("disableLogin event fired");

		detailsValid = false;

		loginButton.setEnabled(false);
		refreshButton.setEnabled(false);
		noteLabel.setVisible(true);
	}

	public void enableLogin() {
		// LOG.info("enableLogin event fired");

		detailsValid = true;

		loginButton.setEnabled(true);
		refreshButton.setEnabled(true);
		noteLabel.setVisible(false);
	}

	public void setForceBasicAuthEnabled() {
		forceBasicAuth.setEnabled(authenticationManager.isOAuthEnabled(), getFieldEditorParent());
	}

	/*
	 * This listener fires when there are changes to the details in the UpstreamURLProvider: when the Datahub URL, the hostname check or the force local authentication checkbox have been changed and
	 * the changes have been applied
	 */
	private final IUpstreamDetailChangedListener enableLoginListener = () -> {

		detailsValid = true;

		UpstreamUrlProvider.INSTANCE.updateOnlineStatus();
		setLoginButtonText();
		enableLogin();
		setLoginButtonEnabled();
		setForceBasicAuthEnabled();
	};

	private @NonNull final IDataHubStateChangeListener stateChangeListener = new IDataHubStateChangeListener() {

		@Override
		public void hubStateChanged(final boolean online, final boolean loggedin, final boolean changedToOnlineAndLoggedIn) {

			// Do not update state if we are waiting for the user to press "apply"
			if (!detailsValid) {
				return;
			}

			if (!getControl().isDisposed()) {
				Display.getDefault().asyncExec(() -> {
					if (loginButton != null && !loginButton.isDisposed()) {
						loginButton.setEnabled(online);

						if (loggedin) {
							loginButton.setText("Logout");
						} else {
							loginButton.setText("Login");
						}
						// forceBasicAuth has no dispose check, so assume it will be valid if the login
						// button is valid.
						if (forceBasicAuth != null) {
							forceBasicAuth.setEnabled(authenticationManager.isOAuthEnabled(), getFieldEditorParent());
						}
					}
				});
			}
		}

		@Override
		public void hubPermissionsChanged() {
			// Nothing needed here
		}

	};

	@Override
	protected void createFieldEditors() {

		editor = new StringFieldEditor(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, "&URL", getFieldEditorParent());
		addField(editor);

		final Composite c = new Composite(getFieldEditorParent(), SWT.NONE);
		c.setLayout(new GridLayout(2, true));
		c.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		loginButton = new Button(c, SWT.PUSH);
		loginButton.setText("Login");
		loginButton.setData("org.eclipse.swtbot.widget.key", "login"); // this id is used in swtbot tests
		loginButton.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

		loginButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {

				if (loginButton.getText().equals(loginButtonText)) {
					// why create this shell here? The Window#create() will create a shell
					// final Shell shell = getShell();
					if (!authenticationManager.isAuthenticated()) {
						authenticationManager.run((Shell) null);
					}
				} else if (loginButton.getText().equals(logoutButtonText)) {
					authenticationManager.logout(getShell());
				}

				if (authenticationManager.isAuthenticated() && !loginButton.isDisposed()) {
					loginButton.setText("Logout");
				} else if (!loginButton.isDisposed()) {
					loginButton.setText("Login");
				}

				// refresh datahub service logged in state
				UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
			}
		});

		setLoginButtonText();
		setLoginButtonEnabled();

		refreshButton = new Button(c, SWT.PUSH);
		refreshButton.setText("Check connection");
		refreshButton.setData("refreshButtonId"); // this id is used in swtbot tests
		refreshButton.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				// trigger refresh datahub service logged in state
				final UpstreamUrlProvider.OnlineState state = UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
				final UpstreamUrlProvider.StateReason reason = state.getReason();
				String msg = null;
				switch (reason) {
				case EMPTY_URL:
					msg = "Data Hub URL is empty";
					break;
				case INVALID_URL:
					msg = "Data Hub URL is not valid or unreachable";
					break;
				case UNKNOWN_ERROR:
					if (state.getException() != null) {
						msg = state.getMessage() + " (see error log for details)";
						LOG.error(state.getException().getMessage(), state.getException());
					} else {
						msg = state.getMessage();
					}
					break;
				case HUB_ONLINE:
					msg = "Data Hub is online";
					break;
				default:
					break;
				}
				if (msg != null) {
					if (reason == StateReason.HUB_ONLINE) {

						final boolean oAuthEnabled = authenticationManager.isOAuthEnabled() && !authenticationManager.forceBasicAuthentication.get();
						if (oAuthEnabled) {
							if (OAuthManager.getInstance().hasToken()) {
								final boolean tokenValid = OAuthManager.getInstance().isTokenValid(UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable());
								if (!tokenValid) {
									MessageDialog.openError(getShell(), "Connection check", msg + "\n\nInvalid/Expired SSO token found.");
								} else {
									MessageDialog.openInformation(getShell(), "Connection check", msg + "\n\nValid SSO token found.");
								}
							} else {
								MessageDialog.openError(getShell(), "Connection check", msg + "\n\nNo SSO token found.");
							}
						} else {
							MessageDialog.openInformation(getShell(), "Connection check", msg);
						}
					} else {
						MessageDialog.openError(getShell(), "Connection check", msg);
					}
				}
			}
		});

		forceBasicAuth = new BooleanFieldEditor(DataHubPreferenceConstants.P_FORCE_BASIC_AUTH, "&Force local authentication", getFieldEditorParent());
		addField(forceBasicAuth);
		setForceBasicAuthEnabled();

		addField(new BooleanFieldEditor(DataHubPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY, "&Base case sharing", getFieldEditorParent()));
		if (LicenseFeatures.isPermitted("features:hub-team-folder")) {
			addField(new BooleanFieldEditor(DataHubPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY, "&Team folder", getFieldEditorParent()));
		}

		final Label lbl2 = new Label(getFieldEditorParent(), SWT.NONE);
		lbl2.setText("Check to prefer the Edge browser for web based login pages. Note: This requires the Edge WebView2 runtime to be installed.");
		lbl2.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
		preferEdgeBrowser = new BooleanFieldEditor(DataHubPreferenceConstants.P_PREFER_EDGE_BROWSER, "&Prefer Edge Browser", getFieldEditorParent());
		addField(preferEdgeBrowser);

		noteLabel = new Label(getFieldEditorParent(), SWT.FILL);
		noteLabel.setVisible(false);
		noteLabel.setText("Note: changes must be applied to take effect");
		noteLabel.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		DataHubServiceProvider.getInstance().addDataHubStateListener(stateChangeListener);
	}

	@Override
	public void init(final IWorkbench workbench) {
		// Nothing needed here
	}

}
