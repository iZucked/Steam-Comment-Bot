package com.mmxlabs.lngdataserver.integration.pricing.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.lngdataserver.integration.pricing.internal.Activator;

public class PricingRepositoryPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public PricingRepositoryPreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Pricing");
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
