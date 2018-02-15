package com.mmxlabs.lngdataserver.integration.distances.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.lngdataserver.commons.impl.StandardDateRepositoryPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.distances.internal.Activator;

public class DistancesRepositoryPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public DistancesRepositoryPreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Distances");
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
