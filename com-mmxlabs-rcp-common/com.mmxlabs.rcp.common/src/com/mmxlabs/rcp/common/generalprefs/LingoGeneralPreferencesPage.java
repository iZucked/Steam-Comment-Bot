package com.mmxlabs.rcp.common.generalprefs;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class LingoGeneralPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private @NonNull ScopedPreferenceStore scopedPreferenceStore;

	public LingoGeneralPreferencesPage() {
		scopedPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.ui.workbench");
		setPreferenceStore(scopedPreferenceStore);
	}

	@Override
	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	@Override
	protected void createFieldEditors() {
		final IntegerFieldEditor maxOpenedEditors = new IntegerFieldEditor("REUSE_OPEN_EDITORS", "&Max open scenarios:", getFieldEditorParent());
		addField(maxOpenedEditors);
	}

	@Override
	protected Control createContents(Composite parent) {
		final Composite c = (Composite) super.createContents(parent);
		final Composite sub = new Composite(c, SWT.NONE);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, ((GridLayout) c.getLayout()).numColumns, 1));
		sub.setLayout(new GridLayout(2, false));
		return c;
	}
}
