/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;

public class TradesTablePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public TradesTablePreferencesPage() {
		super(GRID);
		setPreferenceStore(CargoEditorPlugin.getPlugin().getPreferenceStore());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of preferences. Each field editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		final GridDataFactory labelLayoutData = GridDataFactory.fillDefaults().span(2, 1).hint(200, SWT.DEFAULT);
		{
			final Label label = new Label(getFieldEditorParent(), SWT.WRAP);
			label.setText("Contract to consider as open when computing Long/Shorts.For multiple names, separate with a comma");
			label.setLayoutData(labelLayoutData.create());
			final StringFieldEditor leeway = new StringFieldEditor(PreferenceConstants.P_CONTRACTS_TO_CONSIDER_OPEN, "&Open contract names:", getFieldEditorParent());
			addField(leeway);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(final IWorkbench workbench) {
	}

	@Override
	protected Control createContents(final Composite parent) {
		final Composite c = (Composite) super.createContents(parent);
		final Composite sub = new Composite(c, SWT.NONE);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, ((GridLayout) c.getLayout()).numColumns, 1));

		sub.setLayout(new GridLayout(2, false));

		return c;
	}

}