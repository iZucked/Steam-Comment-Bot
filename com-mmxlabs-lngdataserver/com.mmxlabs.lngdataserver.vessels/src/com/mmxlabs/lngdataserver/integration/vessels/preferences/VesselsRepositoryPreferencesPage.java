package com.mmxlabs.lngdataserver.integration.vessels.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.lngdataserver.commons.impl.StandardDateRepositoryPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.vessels.internal.Activator;

public class VesselsRepositoryPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public VesselsRepositoryPreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Vessels");
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_URL_KEY, "&URL", getFieldEditorParent()));
		addField(new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY, "&Username", getFieldEditorParent()));
		addField(new StringFieldEditor(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY, "&Password", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}

}
