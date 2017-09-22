package com.mmxlabs.lngdataserver.distances.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.lngdataserver.distances.internal.Activator;

public class DistancesRepositoryPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public DistancesRepositoryPreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Distances");
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_URL_KEY, "&URL", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_USERNAME_KEY, "&Username", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PASSWORD_KEY, "&Password", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		
	}
	
}
