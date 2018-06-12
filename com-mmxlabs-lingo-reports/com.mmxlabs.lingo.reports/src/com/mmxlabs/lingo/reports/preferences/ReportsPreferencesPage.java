/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that
 * allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main plug-in class. That way, preferences can be accessed directly via the preference
 * store.
 */

public class ReportsPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ReportsPreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of preferences. Each field editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		final GridDataFactory labelLayoutData = GridDataFactory.fillDefaults().span(2,1).hint(200,SWT.DEFAULT);
		{
			final Label label = new Label(getFieldEditorParent(), SWT.WRAP);
			label.setText("Enter the leeway value (in days) for tight journeys. If a vessel travelling at service speed could not complete a particular journey in the allocated time (taking idle time into account) with that many days to spare, it is considered 'tight'.");
			label.setLayoutData(labelLayoutData.create());
			final IntegerFieldEditor leeway = new IntegerFieldEditor(PreferenceConstants.P_LEEWAY_DAYS, "&Leeway in days:", getFieldEditorParent());
			addField(leeway);
		}
		// Spacer to separate the controls
		final Label spacer = new Label(getFieldEditorParent(), SWT.NONE);
		{
			final Label label = new Label(getFieldEditorParent(), SWT.WRAP);
			label.setText("Choose the format for the event duration columns");
			label.setLayoutData(labelLayoutData.create());

			final String[][] durationValues = new String[][] { //
					{ "Days and hours", Formatters.DurationMode.DAYS_HOURS.name() }, //
					{ "Days to 1 d.p.", Formatters.DurationMode.DECIMAL.name() } };
			addField(new ComboFieldEditor(PreferenceConstants.P_REPORT_DURATION_FORMAT, "&Duration format:", durationValues, getFieldEditorParent()));
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