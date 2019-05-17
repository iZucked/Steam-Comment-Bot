/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.preferences;

import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.server.DataServerActivator;
import com.mmxlabs.lngdataserver.server.HttpClientUtil;
import com.mmxlabs.lngdataserver.server.HttpClientUtil.CertInfo;

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
		Button btn = new Button(getFieldEditorParent(), SWT.PUSH);
		btn.setText("Check Remote SSL");
		btn.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		Text lbl = new Text(getFieldEditorParent(), SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		lbl.setText("");
		lbl.setLayoutData(GridDataFactory.fillDefaults().span(2, 5).minSize(100, 300).hint(100, 300).create());

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
	}

	@Override
	public void init(IWorkbench workbench) {

	}

}
