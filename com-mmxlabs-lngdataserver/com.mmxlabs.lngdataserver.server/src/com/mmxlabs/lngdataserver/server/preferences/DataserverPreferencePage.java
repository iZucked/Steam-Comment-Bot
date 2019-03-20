/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.server.DataServerActivator;

public class DataserverPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public DataserverPreferencePage() {
		super(GRID);
		setDescription("Data Hub - Upstream");
		setPreferenceStore(DataServerActivator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {

		addField(new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_URL_KEY, "&URL", getFieldEditorParent()));
		addField(new BooleanFieldEditor(StandardDateRepositoryPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY, "&Base case sharing", getFieldEditorParent()));
		if (LicenseFeatures.isPermitted("features:hub-team-folder")) {
			addField(new BooleanFieldEditor(StandardDateRepositoryPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY, "&Team folder", getFieldEditorParent()));
		}
	}

	@Override
	public void init(IWorkbench workbench) {

	}

}
