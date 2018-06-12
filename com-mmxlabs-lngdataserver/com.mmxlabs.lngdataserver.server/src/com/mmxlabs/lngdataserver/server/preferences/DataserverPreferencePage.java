/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.lngdataserver.server.internal.Activator;

public class DataserverPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public DataserverPreferencePage() {
		super(GRID);
		setDescription("Data Hub - Upstream");
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {

		addField(new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_URL_KEY, "&URL", getFieldEditorParent()));
		// addField(new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY, "&Username", getFieldEditorParent()));
		// addField(new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY, "&Password", getFieldEditorParent()));
		addField(new BooleanFieldEditor(StandardDateRepositoryPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY, "Enable &Base case sharing", getFieldEditorParent()));
//		addField(new BooleanFieldEditor(StandardDateRepositoryPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY, "Enable &Team workspace", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}

}
