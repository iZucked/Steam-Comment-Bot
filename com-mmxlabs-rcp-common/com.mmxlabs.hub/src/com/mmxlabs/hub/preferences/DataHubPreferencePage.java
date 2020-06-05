/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.auth.AuthenticationManager;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.hub.auth.OAuthAuthenticationManager;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.HttpClientUtil.CertInfo;
import com.mmxlabs.license.features.LicenseFeatures;

import okhttp3.CipherSuite;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class DataHubPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	static AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
	BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.getInstance();
	OAuthAuthenticationManager oauthAuthenticationManager = OAuthAuthenticationManager.getInstance();

	private static final Logger LOGGER = LoggerFactory.getLogger(DataHubPreferencePage.class);

	final OkHttpClient client = HttpClientUtil.basicBuilder().build();

	public DataHubPreferencePage() {
		super(GRID);
		setPreferenceStore(DataHubActivator.getDefault().getPreferenceStore());
	}

	protected static String loginButtonText = "Login";
	protected static String logoutButtonText = "Logout";
	protected static Button button;

	public static void setButtonText() {
		if (authenticationManager.isAuthenticated()) {
			button.setText(logoutButtonText);
		} else {
			button.setText(loginButtonText);
		}
	}

	@Override
	protected void createFieldEditors() {

		final StringFieldEditor editor = new StringFieldEditor(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, "&URL", getFieldEditorParent());
		addField(editor);

		button = new Button(getFieldEditorParent(), SWT.PUSH);
		button.setText("Login");
		button.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				// trigger authentication shell
				if (button.getText().equals(loginButtonText)) {
					authenticationManager.run(getShell());
					if (authenticationManager.isAuthenticated()) {
						button.setText("Logout");
					}
				} else if (button.getText().equals(logoutButtonText)) {
					authenticationManager.logout(getShell());
					if (!authenticationManager.isAuthenticated()) {
						button.setText("Login");
					}
				}
			}
		});

		setButtonText();

		BooleanFieldEditor forceBasicAuth = new BooleanFieldEditor(DataHubPreferenceConstants.P_FORCE_BASIC_AUTH, "&Force local authentication", getFieldEditorParent());
		addField(forceBasicAuth);

		addField(new BooleanFieldEditor(DataHubPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY, "&Base case sharing", getFieldEditorParent()));
		if (LicenseFeatures.isPermitted("features:hub-team-folder")) {
			addField(new BooleanFieldEditor(DataHubPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY, "&Team folder", getFieldEditorParent()));
		}

		final ExpandableComposite debugCompositeParent = new ExpandableComposite(getFieldEditorParent(), ExpandableComposite.TWISTIE);
		debugCompositeParent.setExpanded(false);
		debugCompositeParent.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, true).create());
		debugCompositeParent.setLayout(new FillLayout());
		debugCompositeParent.setText("SSL Info");
		final Composite debugComposite = new Composite(debugCompositeParent, SWT.BORDER);

		debugCompositeParent.setClient(debugComposite);
		debugComposite.setLayout(new GridLayout(2, false));

		debugCompositeParent.addExpansionListener(new IExpansionListener() {

			@Override
			public void expansionStateChanging(final ExpansionEvent e) {
				// Nothing to do
			}

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
		btn3.addSelectionListener(new SelectionListener() {

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
						return;
					} else {
						MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - error code is " + pingResponse.message());
						return;
					}
				} catch (final UnknownHostException e) {
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - Unknown host");
					return;
				} catch (final SSLPeerUnverifiedException e) {
					e.printStackTrace();
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - hostname mismatch between SSL certificate and URL");
					return;
				} catch (final SSLException e) {
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - SSL Error " + e.getMessage());
					return;
				} catch (final IOException e) {
					MessageDialog.openError(getShell(), "Data Hub connection checker", "Connection failed - Unknown Error " + e.getMessage());
					return;
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

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

		btnCheckRemoteSSL.addSelectionListener(new SelectionListener() {

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

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});
		btnCheckLocalSSL.addSelectionListener(new SelectionListener() {

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

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		btnCheckHubSSLCompatibility.addSelectionListener(new SelectionListener() {

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

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		final Label noteLabel = new Label(getFieldEditorParent(), SWT.FILL);
		noteLabel.setText("Note: changes must be applied to take effect");
		noteLabel.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
	}

	@Override
	public void init(final IWorkbench workbench) {

	}

}
