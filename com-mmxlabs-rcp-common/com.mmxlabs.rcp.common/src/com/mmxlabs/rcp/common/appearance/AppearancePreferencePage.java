/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.appearance;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class AppearancePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private final @NonNull ScopedPreferenceStore scopedPreferenceStore;

	public static final @NonNull String PROPERTY_NAME = "DebugColouringEnabled";

	public AppearancePreferencePage() {
		scopedPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.ui.workbench");
		setPreferenceStore(scopedPreferenceStore);
	}

	@Override
	public void init(final IWorkbench workbench) {
		// Nothing required here ...
	}

	@Override
	protected void createFieldEditors() {

		final BooleanFieldEditor debugColour = new BooleanFieldEditor(PROPERTY_NAME, "&Testing mode (restart required)", getFieldEditorParent());
		addField(debugColour);
	}
}
