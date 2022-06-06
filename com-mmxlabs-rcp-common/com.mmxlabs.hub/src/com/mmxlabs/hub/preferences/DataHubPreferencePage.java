/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.hub.preferences;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
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
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.HttpClientUtil.CertInfo;
import com.mmxlabs.license.features.LicenseFeatures;

import okhttp3.CipherSuite;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class DataHubPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static Logger LOGGER = LoggerFactory.getLogger(DataHubPreferencePage.class);

	private static AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	/*
	 * This listener fires whenever the Data Hub URL is modified but the changes
	 * have not yet been applied. Login will use the URL from the preferences which
	 * is saved only when the Apply button is pressed
	 */
	// display note and disable login button
	private IPropertyChangeListener disableLogin = event -> disableLogin();
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
	protected void initialize() {
		super.initialize();
		editor.setPropertyChangeListener(disableLogin);
		forceBasicAuth.setPropertyChangeListener(disableLogin);
	}

	public void disableLogin() {
		System.out.println("Disable login fired");
		var store = DataHubActivator.getDefault().getPreferenceStore();

		System.out.println("After (pref) " + store.getString(DataHubPreferenceConstants.P_DATAHUB_URL_KEY));

		
		LOGGER.info("disableLogin event fired");

		detailsValid = false;

		loginButton.setEnabled(false);
		refreshButton.setEnabled(false);
		noteLabel.setVisible(true);
	}

	public void enableLogin() {
		LOGGER.info("enableLogin event fired");

		detailsValid = true;

		loginButton.setEnabled(true);
		refreshButton.setEnabled(true);
		noteLabel.setVisible(false);
	}

	public void setForceBasicAuthEnabled() {
		forceBasicAuth.setEnabled(authenticationManager.isOAuthEnabled(), getFieldEditorParent());
	}

	/*
	 * This listener fires when there are changes to the details in the
	 * UpstreamURLProvider: when the Datahub URL, the hostname check or the force
	 * local authentication checkbox have been changed and the changes have been
	 * applied
	 */
	private final IUpstreamDetailChangedListener enableLoginListener = () -> {

		detailsValid = true;

		UpstreamUrlProvider.INSTANCE.updateOnlineStatus();
		setLoginButtonText();
		enableLogin();
		setLoginButtonEnabled();
		setForceBasicAuthEnabled();
	};

	private @NonNull IDataHubStateChangeListener stateChangeListener = new IDataHubStateChangeListener() {

		@Override
		public void hubStateChanged(boolean online, boolean loggedin, boolean changedToOnlineAndLoggedIn) {

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

		editor = new StringFieldEditor(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, "&URL", getFieldEditorParent()) {
			
			@Override
			protected void valueChanged() {
			System.out.println("URL value changed");
				super.valueChanged();
			}
			
		};
		addField(editor);

		Composite c = new Composite(getFieldEditorParent(), SWT.NONE);
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
				UpstreamUrlProvider.OnlineState state = UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
				UpstreamUrlProvider.StateReason reason = state.getReason();
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
						LOGGER.error(state.getException().getMessage(), state.getException());
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

						boolean oAuthEnabled = authenticationManager.isOAuthEnabled() && !authenticationManager.forceBasicAuthentication.get();
						if (oAuthEnabled) {
							if (OAuthManager.getInstance().hasToken()) {
								boolean tokenValid = OAuthManager.getInstance().isTokenValid(UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable());
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

		Label lbl2 = new Label(getFieldEditorParent(), SWT.NONE);
		lbl2.setText("Check to prefer the Edge browser for web based login pages. Note: This requires the Edge WebView2 runtime to be installed.");
		lbl2.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
		preferEdgeBrowser = new BooleanFieldEditor(DataHubPreferenceConstants.P_PREFER_EDGE_BROWSER, "&Prefer Edge Browser", getFieldEditorParent());
		addField(preferEdgeBrowser);

		final ExpandableComposite debugCompositeParent = new ExpandableComposite(getFieldEditorParent(), ExpandableComposite.TWISTIE);
		debugCompositeParent.setExpanded(false);
		debugCompositeParent.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, true).create());
		debugCompositeParent.setLayout(new FillLayout());
		debugCompositeParent.setText("SSL Info");
		final Composite debugComposite = new Composite(debugCompositeParent, SWT.BORDER);

		debugCompositeParent.setClient(debugComposite);
		debugComposite.setLayout(new GridLayout(2, false));

		debugCompositeParent.addExpansionListener(new ExpansionAdapter() {

			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				debugComposite.layout();
			}
		});

		addField(new BooleanFieldEditor(DataHubPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK, "Disable hostname checks", debugComposite));

		final Label separator = new Label(debugComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		final Label label = new Label(debugComposite, SWT.FILL);
		label.setText("SSL checks use the current URL");
		label.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		final Button btn3 = new Button(debugComposite, SWT.PUSH);
		btn3.setText("Check connection");
		btn3.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
		btn3.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent se) {

				String url = editor.getStringValue();
				if (url == null || url.isEmpty()) {
					MessageDialog.openConfirm(getShell(), "Data Hub connection checker", "The URL is empty");
					return;
				}

				if (!url.startsWith("https://")) {
					MessageDialog.openConfirm(getShell(), "Data Hub connection checker", "The URL must begin with https");
					return;
				}

				if (url.charAt(url.length() - 1) == '/') {
					url = url.substring(0, url.length() - 1);
				}

				Request pingRequest = null;
				try {
					pingRequest = new Request.Builder().url(url + "/ping").get().build();
				} catch (final IllegalArgumentException e) {
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Invalid URL");
					return;
				}

				final OkHttpClient client = HttpClientUtil.basicBuilder().build();

				try (final Response pingResponse = client.newCall(pingRequest).execute()) {
					if (pingResponse.isSuccessful()) {
						MessageDialog.openConfirm(getShell(), "Data Hub connection checker", "Connected successfully.");
						DataHubServiceProvider.getInstance().setOnlineState(true);
					} else {
						MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - error code is " + pingResponse.message());
						DataHubServiceProvider.getInstance().setOnlineState(false);
					}

				} catch (final UnknownHostException e) {
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - Unknown host");
				} catch (final SSLPeerUnverifiedException e) {
					e.printStackTrace();
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - hostname mismatch between SSL certificate and URL");
				} catch (final SSLException e) {
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - SSL Error " + e.getMessage());
				} catch (final IOException e) {
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - Unknown Error " + e.getMessage());
				}

				UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
				setLoginButtonEnabled();
			}
		});

		final Button btnCheckRemoteSSL = new Button(debugComposite, SWT.PUSH);
		btnCheckRemoteSSL.setText("Check Remote SSL");
		btnCheckRemoteSSL.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

		final Button btnCheckLocalSSL = new Button(debugComposite, SWT.PUSH);
		btnCheckLocalSSL.setText("Check Local certificates");
		btnCheckLocalSSL.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

		final Button btnCheckHubSSLCompatibility = new Button(debugComposite, SWT.PUSH);
		btnCheckHubSSLCompatibility.setText("Check remote SSL compatibility");
		btnCheckHubSSLCompatibility.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

		final Text lbl = new Text(debugComposite, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		lbl.setText("");
		lbl.setLayoutData(GridDataFactory.fillDefaults().span(2, 5).minSize(250, 300).hint(250, 300).create());

		btnCheckRemoteSSL.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					final List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromHost(editor.getStringValue());

					final StringBuilder sb = new StringBuilder();
					int counter = 1;
					for (final CertInfo info : infos) {
						sb.append("Certificate " + counter++ + "\n");
						sb.append(info);
						sb.append("\n");
					}
					lbl.setText(sb.toString());
				} catch (final Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		btnCheckLocalSSL.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					final List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromLocalStore();

					final StringBuilder sb = new StringBuilder();
					int counter = 1;
					for (final CertInfo info : infos) {
						sb.append("Certificate " + counter++ + "\n");
						sb.append(info);
						sb.append("\n");
					}
					lbl.setText(sb.toString());
				} catch (final Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		btnCheckHubSSLCompatibility.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final StringBuilder sb = new StringBuilder();

				BusyIndicator.showWhile(Display.getCurrent(), () -> {
					final String url = editor.getStringValue();

					if (!url.startsWith("https://")) {
						sb.append("Invalid https URL");
						return;
					}

					final CipherSuite[] selectedCipher = new CipherSuite[1];
					final TlsVersion[] selectedTlsVersion = new TlsVersion[1];
					HttpClientUtil.getSelectedProtocolAndVersion(url, selectedTlsVersion, selectedCipher);

					if (selectedCipher[0] != null && selectedTlsVersion[0] != null) {
						sb.append("Selected TLS Version: ");
						sb.append(selectedTlsVersion[0]);
						sb.append("\n\n");
						sb.append("Selected Cipher Version: ");
						sb.append(selectedCipher[0]);
						sb.append("\n\n");
					}

					try {

						final Set<TlsVersion> supportedVersion = new HashSet<>();
						final Set<CipherSuite> supportedCiphers = new HashSet<>();
						HttpClientUtil.extractSSLCompatibilityFromHost(url, (tlsVersion, cipher) -> {
							supportedVersion.add(tlsVersion);
							supportedCiphers.add(cipher);
						});
						sb.append("Supported TLS Versions\n");
						for (final TlsVersion info : supportedVersion) {
							sb.append(info);
							sb.append("\n");
						}
						sb.append("\n");
						sb.append("Supported Ciphers\n");
						for (final CipherSuite info : supportedCiphers) {
							sb.append(info);
							sb.append("\n");
						}
					} catch (final Exception e1) {
						e1.printStackTrace();
					}
				});

				lbl.setText(sb.toString());

			}
		});

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
