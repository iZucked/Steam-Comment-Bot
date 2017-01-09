/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.exportWizards;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.RadioSelectionGroup;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;

/**
 * @author hinton
 * 
 */
public class ExportCSVWizardPage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private final ISelection selection;

	private static final String FILTER_KEY = "lastSelection";
	private static final String DELIMITER_KEY = "lastDelimiter";
	private static final String DECIMAL_SEPARATOR_KEY = "lastDecimalSeparator";
	private static final String SECTION_NAME = "ExportCSVWizardPage.section";

	private DirectoryFieldEditor editor;

	private int CHOICE_COMMA = 0;

	private int CHOICE_SEMICOLON = 1;

	private int CHOICE_PERIOD = 1;

	private RadioSelectionGroup csvSelectionGroup;
	private RadioSelectionGroup decimalSelectionGroup;

	protected ExportCSVWizardPage(ISelection selection) {
		super("Export Scenario as CSV", "Export Scenario as CSV", null);
		this.selection = selection;
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete();
	}

	/**
	 * @return
	 */
	public File getOutputDirectory() {
		return new File(editor.getStringValue());
	}

	@Override
	public void createControl(Composite parent) {

		final Composite container = new Composite(parent, SWT.NULL);

		final GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		final Listener listener = new Listener() {
			@Override
			public void handleEvent(final Event event) {

				dialogChanged();

			}
		};

		scenarioServiceSelectionGroup = new ScenarioServiceSelectionGroup(container, listener, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		gd.heightHint = 320;
		gd.horizontalSpan = 2;
		scenarioServiceSelectionGroup.setLayoutData(gd);

		final Group destination = new Group(container, SWT.NONE);
		destination.setText("Destination Directory");
		GridLayout layout2 = new GridLayout();
		destination.setLayout(layout2);
		destination.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		destination.setFont(parent.getFont());
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.horizontalSpan = 2;
		destination.setLayoutData(gd2);
		
		final DirectoryFieldEditor editor = new DirectoryFieldEditor("destination-directory", "Export to directory:", destination);

		this.editor = editor;
		editor.getTextControl(destination).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		csvSelectionGroup = new RadioSelectionGroup(container, "Format separator", SWT.NONE, new String[] { "comma (\",\")", "semicolon (\";\")" }, new int[] { CHOICE_COMMA, CHOICE_SEMICOLON });
		decimalSelectionGroup = new RadioSelectionGroup(container, "Decimal separator", SWT.NONE, new String[] { "comma (\",\")", "period (\".\")" }, new int[] { CHOICE_COMMA, CHOICE_PERIOD });

		// get the default export directory from the settings
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		final IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		final String filter = section == null ? null : section.get(FILTER_KEY);
		int delimiterValue = CHOICE_COMMA;
		if (section != null && section.get(DELIMITER_KEY) != null) {
			delimiterValue = section.getInt(DELIMITER_KEY);
		}
		int decimalValue = CHOICE_PERIOD;
		if (section != null && section.get(DECIMAL_SEPARATOR_KEY) != null) {
			decimalValue = section.getInt(DECIMAL_SEPARATOR_KEY);
		}
		// use it to populate the editor
		editor.setStringValue(filter);
		csvSelectionGroup.setSelectedIndex(delimiterValue);
		decimalSelectionGroup.setSelectedIndex(decimalValue);

		initialize();
		dialogChanged();
		setControl(container);
	}

	private void initialize() {
		final Bundle bundle = Activator.getDefault().getBundle();
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<ScenarioServiceRegistry> serviceReference = bundleContext.getServiceReference(ScenarioServiceRegistry.class);

		scenarioServiceSelectionGroup.setInput(bundleContext.getService(serviceReference));

		if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection) {

			final IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1) {
				return;
			}
			final Object obj = ssel.getFirstElement();
			if (obj instanceof Container) {
				scenarioServiceSelectionGroup.setSelectedContainer((Container) obj);
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {

		Container c = scenarioServiceSelectionGroup.getSelectedContainer();
		if (!(c instanceof ScenarioInstance)) {
			updateStatus("A Scenario must be selected");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public Collection<ScenarioInstance> getScenarioInstance() {
		return Collections.singleton((ScenarioInstance) scenarioServiceSelectionGroup.getSelectedContainer());
	}

	/**
	 * Saves the value of the directory editor field to persistent storage
	 */
	public void saveDirectorySetting() {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		if (section == null) {
			section = dialogSettings.addNewSection(SECTION_NAME);
		}
		section.put(FILTER_KEY, editor.getStringValue());
		section.put(DELIMITER_KEY, csvSelectionGroup.getSelectedValue());
		section.put(DECIMAL_SEPARATOR_KEY, decimalSelectionGroup.getSelectedValue());
	}

	public char getCsvDelimiter() {
		return csvSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : ';';
	}

	public char getDecimalSeparator() {
		return decimalSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : '.';
	}

}
