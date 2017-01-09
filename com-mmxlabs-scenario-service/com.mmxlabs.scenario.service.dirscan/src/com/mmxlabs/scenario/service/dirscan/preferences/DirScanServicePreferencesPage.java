/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.scenario.service.dirscan.internal.Activator;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that
 * allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main plug-in class. That way, preferences can be accessed directly via the preference
 * store.
 */

public class DirScanServicePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public DirScanServicePreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Folder Sync");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of preferences. Each field editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(new BooleanFieldEditor(PreferenceConstants.P_ENABLED_KEY, "&Enabled:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_NAME_KEY, "&Name:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH_KEY, "&Path:", getFieldEditorParent()));
		
		
		addField(new BooleanFieldEditor("option2." + PreferenceConstants.P_ENABLED_KEY, "&Enabled:", getFieldEditorParent()));
		addField(new StringFieldEditor("option2." + PreferenceConstants.P_NAME_KEY, "&Name:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("option2." + PreferenceConstants.P_PATH_KEY, "&Path:", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor("option3." + PreferenceConstants.P_ENABLED_KEY, "&Enabled:", getFieldEditorParent()));
		addField(new StringFieldEditor("option3." + PreferenceConstants.P_NAME_KEY, "&Name:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("option3." + PreferenceConstants.P_PATH_KEY, "&Path:", getFieldEditorParent()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

}