/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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
		setDescription("Dir Scan");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of preferences. Each field editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(new BooleanFieldEditor(PreferenceConstants.P_ENABLED_KEY, "&Enabled:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_NAME_KEY, "&Name:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH_KEY, "&Path:", getFieldEditorParent()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

	@SuppressWarnings("unused")
	@Override
	protected Control createContents(Composite parent) {
		final Composite c = (Composite) super.createContents(parent);
		final Composite sub = new Composite(c, SWT.NONE);
		sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, ((GridLayout) c.getLayout()).numColumns, 1));

		sub.setLayout(new GridLayout(2, false));

		new Label(sub, SWT.NONE);
		final Button refresh = new Button(sub, SWT.RIGHT);
		refresh.setText("Reload");
		refresh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		refresh.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
//				if (!source.isConnected())
//					source.connect();
//				source.refresh();
//				final ServiceStats stats = source.getStatus();
//				lblConnected.setText(source.getConnectionStatus());
//				lblQueries.setText(stats == null ? "unknown" : source.getDistanceQueriesRemaining() + "");
//				lblConPerDay.setText(stats == null ? "unknown" : stats.getMaximumCalculationsPerDay() + "");
//				lblConcur.setText(stats == null ? "unknown" : stats.getMaximumConnectionsAllowed() + "");
//				lblCalcToday.setText(stats == null ? "unknown" : stats.getNumberOfCalculationsToday() + "");
//				lblSubExp.setText(stats == null ? "unknown" : stats.getSubscriptionExpiryDate().getValue() + "");
//				lblTabVer.setText(stats == null ? "unknown" : stats.getTableVersion().getValue() + "");
//				lblTabUp.setText(stats == null ? "unknown" : stats.getTableDate().getValue() + "");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

		});

		return c;
	}

}