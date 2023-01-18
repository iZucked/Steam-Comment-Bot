/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.preferences;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.client.methods.HttpGet;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.HttpClientUtil.CertInfo;

public class TLSPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final Logger LOG = LoggerFactory.getLogger(TLSPreferencePage.class);
	private static final String CONNECTION_CHECKER = "Connection checker";

	public TLSPreferencePage() {
		super(GRID);
		setPreferenceStore(DataHubActivator.getDefault().getPreferenceStore());
	}

	protected BooleanFieldEditor useWindowsTrustStore;
	protected BooleanFieldEditor useJavaTrustStore;

	@Override
	protected void createFieldEditors() {
		{
			final Label label = new Label(getFieldEditorParent(), SWT.FILL);
			label.setText("Enabled certificate truststores. (Note a restart may be required to take effect)");
			label.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		}

		useWindowsTrustStore = new BooleanFieldEditor(TLSPreferenceConstants.P_TLS_USE_WINDOWS_TRUSTSTORE, "&Use Windows Truststore", getFieldEditorParent());
		addField(useWindowsTrustStore);

		useJavaTrustStore = new BooleanFieldEditor(TLSPreferenceConstants.P_TLS_USE_JAVA_TRUSTSTORE, "&Use bundled Java Truststore", getFieldEditorParent());
		addField(useJavaTrustStore);

		{
			final Label spacer = new Label(getFieldEditorParent(), SWT.FILL);
			spacer.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		}
		{
			final Label label = new Label(getFieldEditorParent(), SWT.FILL);
			label.setText("Test SSL certificates may not report the correct hostname. The following option can disable the hostname check, but should not be used in production.");
			label.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		}
		BooleanFieldEditor hostnameCheckEditor = new BooleanFieldEditor(DataHubPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK, "Disable SSL hostname checks", getFieldEditorParent());
		addField(hostnameCheckEditor);
		{
			final Label spacer = new Label(getFieldEditorParent(), SWT.FILL);
			spacer.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		}
		{
			final Label label = new Label(getFieldEditorParent(), SWT.FILL);
			label.setText("TLS/SSL Debugging tools");
			label.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		}
		{
			final Label label = new Label(getFieldEditorParent(), SWT.FILL);
			label.setText("(Note: changes to above settings need to be \"Applied\" before they will take effect here.)");
			label.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		}
		// Override to create a space for the test connection button in the layout
		StringFieldEditor editor = new StringFieldEditor("DebugURL", "Target URL", getFieldEditorParent()) {
			@Override
			public int getNumberOfControls() {
				return 3;
			}
			@Override
			protected void doFillIntoGrid(Composite parent, int numColumns) {
				super.doFillIntoGrid(parent, 2);
				GridData gridData = (GridData)getTextControl(parent).getLayoutData();
				gridData.horizontalSpan = 1;

			}
			
			@Override
			protected void adjustForNumColumns(int numColumns) {
				// Ignore the default implementation as it resets the span
			}
			
		};
		addField(editor);
		final Button testBtn = new Button(getFieldEditorParent(), SWT.PUSH);
		testBtn.setText("Test connection");
		testBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent se) {

				String url = editor.getStringValue();
				if (url == null || url.isEmpty()) {
					MessageDialog.openConfirm(getShell(), CONNECTION_CHECKER, "The URL is empty");
					return;
				}

				if (!url.startsWith("https://")) {
					MessageDialog.openConfirm(getShell(), CONNECTION_CHECKER, "The URL must begin with https");
					return;
				}

				if (url.charAt(url.length() - 1) == '/') {
					url = url.substring(0, url.length() - 1);
				}

				HttpGet pingRequest = null;
				try {
					pingRequest = new HttpGet(url);
				} catch (final IllegalArgumentException e) {
					MessageDialog.openError(getShell(), CONNECTION_CHECKER, "Invalid URL");
					return;
				}

				try (final var client = HttpClientUtil.createBasicHttpClient(pingRequest, true).build()) {

					try (final var pingResponse = client.execute(pingRequest)) {
						final int responseCode = pingResponse.getStatusLine().getStatusCode();
						if (HttpClientUtil.isSuccessful(responseCode)) {
							MessageDialog.openConfirm(getShell(), CONNECTION_CHECKER, "Connected successfully " + responseCode);
						} else {
							MessageDialog.openError(getShell(), CONNECTION_CHECKER, "Connected with an error repsonse " + responseCode + " - " + pingResponse.getStatusLine().getReasonPhrase());
						}
					}
				} catch (final UnknownHostException e) {
					MessageDialog.openError(getShell(), CONNECTION_CHECKER, "Connection failed - Unknown host");
				} catch (final SSLPeerUnverifiedException e) {
					e.printStackTrace();
					MessageDialog.openError(getShell(), CONNECTION_CHECKER, "Connection failed - hostname mismatch between SSL certificate and URL");
				} catch (final SSLException e) {
					MessageDialog.openError(getShell(), CONNECTION_CHECKER, "Connection failed - SSL Error " + e.getMessage());
				} catch (final IOException e) {
					MessageDialog.openError(getShell(), CONNECTION_CHECKER, "Connection failed - Unknown Error " + e.getMessage());
				}

			}
		});

		
		final Label separator = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		
		Composite buttonsComposite = new Composite(getFieldEditorParent(), SWT.NONE);
		buttonsComposite.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).grab(true, false).create());
		buttonsComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());

		final Button btnCheckRemoteSSL = new Button(buttonsComposite, SWT.PUSH);
		btnCheckRemoteSSL.setText("Query remote SSL certificates");
		btnCheckRemoteSSL.setToolTipText("Queries the remote server's TLS/SSL certificates");

		final Button btnCheckLocalSSL = new Button(buttonsComposite, SWT.PUSH);
		btnCheckLocalSSL.setText("List LiNGO certificates");
		btnCheckLocalSSL.setToolTipText("Lists additional certificates stored in LiNGO's cacerts folders");

		final Button btnCheckHubSSLCompatibility = new Button(buttonsComposite, SWT.PUSH);
		btnCheckHubSSLCompatibility.setText("Check remote SSL compatibility");
		btnCheckHubSSLCompatibility.setToolTipText("Query the remote server's supported TLS protocols and ciphers and those supported by LiNGO");

		final Button btnCheckLocalTrustedSSL = new Button(buttonsComposite, SWT.PUSH);
		btnCheckLocalTrustedSSL.setText("List trusted certificates");
		btnCheckLocalTrustedSSL.setToolTipText("Lists the combined set of trusted certificates from enabled sources");

		final Button btnCheckWindowsTrustedSSL = new Button(buttonsComposite, SWT.PUSH);
		btnCheckWindowsTrustedSSL.setText("List Windows trusted certificates");
		btnCheckWindowsTrustedSSL.setToolTipText("Queries the Windows truststore for trust roots");

		final Text lbl = new Text(getFieldEditorParent(), SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		lbl.setText("");
		lbl.setLayoutData(GridDataFactory.fillDefaults().span(2, 5).minSize(250, 300).hint(250, 300).create());

		btnCheckRemoteSSL.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					final Bundle bundle = FrameworkUtil.getBundle(this.getClass());
					final ServiceTracker<IProxyService, IProxyService> proxyTracker = new ServiceTracker<>(bundle.getBundleContext(), IProxyService.class.getName(), null);
					proxyTracker.open();
					try {
						final StringBuilder sb = new StringBuilder();

						final IProxyService service = proxyTracker.getService();
						if (service != null) {
							if (service.isProxiesEnabled()) {
								sb.append("Direct connection info\n\n");
							}
						}
						try {
							final List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromHost(editor.getStringValue(), null);
							int counter = 1;
							for (final CertInfo info : infos) {
								sb.append("Certificate " + counter++ + "\n");
								sb.append(info);
								sb.append("\n");
							}
						} catch (final Exception ex) {
							sb.append("Error getting direct connection certificates\n");
							LOG.error("Error getting direct connection certificates", ex);
						}
						if (service != null && service.isProxiesEnabled()) {
							try {
								final IProxyData[] data = service.select(new URI(editor.getStringValue()));
								if (data != null && data.length > 0) {
									sb.append("Proxy server configuration detected\n\n");
									for (final var pd : data) {
										if (Objects.equals(pd.getType(), IProxyData.HTTPS_PROXY_TYPE)) {
											sb.append("Proxy connection via " + pd.getHost() + "\n\n");

											final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(pd.getHost(), pd.getPort()));
											final List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromHost(editor.getStringValue(), proxy);
											int counter = 1;
											for (final CertInfo info : infos) {
												sb.append("Certificate " + counter++ + "\n");
												sb.append(info);
												sb.append("\n");
											}
										}
									}
								}
							} catch (final URISyntaxException uriex) {
								// Ignore URI issues
							} catch (final Exception ex) {
								sb.append("Error getting proxy connection certificates\n");
								LOG.error("Error getting proxy connection certificates", ex);
							}
						}

						lbl.setText(sb.toString());
					} finally {
						proxyTracker.close();
					}
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
		btnCheckLocalTrustedSSL.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					final List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromLocalTrustStore();

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
		btnCheckWindowsTrustedSSL.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					final List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromWindowsTrustStore();

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

					final String[] selectedCipher = new String[1];
					final String[] selectedTlsVersion = new String[1];
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

						final Set<String> supportedVersion = new HashSet<>();
						final Set<String> supportedCiphers = new HashSet<>();
						HttpClientUtil.extractSSLCompatibilityFromHost(url, (tlsVersion, cipher) -> {
							supportedVersion.add(tlsVersion);
							supportedCiphers.add(cipher);
						});
						sb.append("Supported TLS Versions\n");
						for (final String info : supportedVersion) {
							sb.append(info);
							sb.append("\n");
						}
						sb.append("\n");
						sb.append("Supported Ciphers\n");
						for (final String info : supportedCiphers) {
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
	}

	@Override
	public void init(final IWorkbench workbench) {
		// Nothing needed here
	}

}
