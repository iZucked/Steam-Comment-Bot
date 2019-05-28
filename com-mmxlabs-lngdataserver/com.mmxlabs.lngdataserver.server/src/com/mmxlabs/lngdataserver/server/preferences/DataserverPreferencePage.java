/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.preferences;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.server.DataServerActivator;
import com.mmxlabs.lngdataserver.server.HttpClientUtil;
import com.mmxlabs.lngdataserver.server.HttpClientUtil.CertInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataserverPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public DataserverPreferencePage() {
		super(GRID);
		setDescription("Data Hub - Upstream");
		setPreferenceStore(DataServerActivator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {

		StringFieldEditor editor = new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_URL_KEY, "&URL", getFieldEditorParent());
		addField(editor);
		addField(new BooleanFieldEditor(StandardDateRepositoryPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY, "&Base case sharing", getFieldEditorParent()));
		if (LicenseFeatures.isPermitted("features:hub-team-folder")) {
			addField(new BooleanFieldEditor(StandardDateRepositoryPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY, "&Team folder", getFieldEditorParent()));
		}

		ExpandableComposite debugCompositeParent = new ExpandableComposite(getFieldEditorParent(), ExpandableComposite.TWISTIE);
		debugCompositeParent.setExpanded(false);
		debugCompositeParent.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, true).create());
		debugCompositeParent.setLayout(new FillLayout());
		debugCompositeParent.setText("SSL Info");
		Composite debugComposite = new Composite(debugCompositeParent, SWT.BORDER);

		debugCompositeParent.setClient(debugComposite);
		debugComposite.setLayout(new GridLayout(2, false));

		debugCompositeParent.addExpansionListener(new IExpansionListener() {

			@Override
			public void expansionStateChanging(ExpansionEvent e) {
				// Nothing to do
			}

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				debugComposite.layout();
			}
		});

		addField(new BooleanFieldEditor(StandardDateRepositoryPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK, "Disable hostname checks (Apply changes to test)", debugComposite));

		Button btn3 = new Button(debugComposite, SWT.PUSH);
		btn3.setText("Check connection");
		btn3.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
		btn3.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent se) {

				String url = editor.getStringValue();
				if (url == null || url.isEmpty()) {
					return;
				}

				if (!url.startsWith("https://")) {
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

				OkHttpClient client = HttpClientUtil.basicBuilder().build();

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
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Button btn = new Button(debugComposite, SWT.PUSH);
		btn.setText("Check Remote SSL");
		btn.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

		Button btn2 = new Button(debugComposite, SWT.PUSH);
		btn2.setText("Check Local certificates");
		btn2.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

		Text lbl = new Text(debugComposite, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		lbl.setText("");
		lbl.setLayoutData(GridDataFactory.fillDefaults().span(2, 5).minSize(250, 300).hint(250, 300).create());

		btn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromHost(editor.getStringValue());

					StringBuilder sb = new StringBuilder();
					int counter = 1;
					for (CertInfo info : infos) {
						sb.append("Certificate " + counter++ + "\n");
						sb.append(info);
						sb.append("\n");
					}
					lbl.setText(sb.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		btn2.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					List<CertInfo> infos = HttpClientUtil.extractSSLInfoFromLocalStore();

					StringBuilder sb = new StringBuilder();
					int counter = 1;
					for (CertInfo info : infos) {
						sb.append("Certificate " + counter++ + "\n");
						sb.append(info);
						sb.append("\n");
					}
					lbl.setText(sb.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

	}

	@Override
	public void init(IWorkbench workbench) {

	}

}
