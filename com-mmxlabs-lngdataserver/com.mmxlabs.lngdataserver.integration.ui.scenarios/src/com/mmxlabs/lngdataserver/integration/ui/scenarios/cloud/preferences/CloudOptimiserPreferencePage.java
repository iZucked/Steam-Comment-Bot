/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.Activator;

public class CloudOptimiserPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudOptimiserPreferencePage.class);

	public CloudOptimiserPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {

		StringFieldEditor gateway = new StringFieldEditor(CloudOptimiserPreferenceConstants.P_GATEWAY_URL_KEY, "&URL", getFieldEditorParent());
		addField(gateway);

		StringFieldEditor version = new StringFieldEditor(CloudOptimiserPreferenceConstants.P_DEV_VERSION, "Version", getFieldEditorParent());
		addField(version);

		StringFieldEditor username = new StringFieldEditor(CloudOptimiserPreferenceConstants.P_USERNAME, "Username", getFieldEditorParent());
		addField(username);

		StringFieldEditor password = new StringFieldEditor(CloudOptimiserPreferenceConstants.P_PASSWORD, "Password", getFieldEditorParent());
		password.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(password);

	}

	@Override
	public void init(final IWorkbench workbench) {
		// Nothing needed here
	}

}
