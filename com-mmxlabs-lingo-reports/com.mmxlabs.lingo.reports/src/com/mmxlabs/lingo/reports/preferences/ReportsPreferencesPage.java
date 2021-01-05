/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
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
	
		final Label allSection = new Label(getFieldEditorParent(), SWT.BOLD);
		allSection.setText("All");
		allSection.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
	    gd3.horizontalSpan = 2;
		allSection.setLayoutData(gd3);
	
		final GridDataFactory labelLayoutData = GridDataFactory.fillDefaults().span(2,1).hint(200,SWT.DEFAULT);
		addDurationFormatPreferences(labelLayoutData);
		
	    Label separator1 = new Label(getFieldEditorParent(), SWT.HORIZONTAL | SWT.SEPARATOR);
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    gd.horizontalSpan = 2;
	    separator1.setLayoutData(gd);
	    
		final Label scheduleSection = new Label(getFieldEditorParent(), SWT.BOLD);
		scheduleSection.setText("Schedule");
		scheduleSection.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
	    gd2.horizontalSpan = 2;
		scheduleSection.setLayoutData(gd2);
		
		addLeewayDaysPreferences(labelLayoutData);
		
	    Label separator2 = new Label(getFieldEditorParent(), SWT.HORIZONTAL | SWT.SEPARATOR);
	    separator2.setLayoutData(gd);
	    
		final Label incomeStatementSection = new Label(getFieldEditorParent(), SWT.BOLD);
		incomeStatementSection.setText("Income statement");
		incomeStatementSection.setLayoutData(labelLayoutData.create());
		incomeStatementSection.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
			
		addIncomeStatementRegionsMultilineTextBox();
	}

	private void addIncomeStatementRegionsMultilineTextBox() {
		addField(new MultiLineTextFieldEditor(PreferenceConstants.REPORT_REGIONS_LIST, "&Regions:", getFieldEditorParent()));
	}

	private void addLeewayDaysPreferences(final GridDataFactory labelLayoutData) {
		final String tooltip = "Any journey that cannot be completed at service speed in the available time with this leeway number of days to spare, is considered tight.";
		//final Label label = new Label(getFieldEditorParent(), SWT.WRAP);
		//label.setLayoutData(labelLayoutData.create());
		final IntegerFieldEditor leeway = new IntegerFieldEditor(PreferenceConstants.P_LEEWAY_DAYS, "&Leeway in days for tight journeys:", getFieldEditorParent());
		leeway.getTextControl(getFieldEditorParent()).setToolTipText(tooltip);
		leeway.getLabelControl(getFieldEditorParent()).setToolTipText(tooltip);
		addField(leeway);
	}

	private void addDurationFormatPreferences(final GridDataFactory labelLayoutData) {
		//final Label label = new Label(getFieldEditorParent(), SWT.WRAP);
		//label.setText("Choose the format for the event duration columns");
		//label.setLayoutData(labelLayoutData.create());

		final String[][] durationValues = new String[][] { //
			{ "Days and  hours (1:12)", Formatters.DurationMode.DAYS_HOURS_COLON.name() }, //
			{ "Days and hours (1d 12h)", Formatters.DurationMode.DAYS_HOURS_HUMAN.name() }, //
			{ "Days to 1 d.p. (1.5)", Formatters.DurationMode.DECIMAL.name() } };
			addField(new ComboFieldEditor(PreferenceConstants.P_REPORT_DURATION_FORMAT, "&Event duration column format:", durationValues, getFieldEditorParent()));
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