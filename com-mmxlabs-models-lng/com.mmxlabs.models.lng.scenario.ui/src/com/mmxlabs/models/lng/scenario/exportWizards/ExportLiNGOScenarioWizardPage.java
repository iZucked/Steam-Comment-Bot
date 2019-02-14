/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.exportWizards;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;

/**
 * @author hinton
 * 
 */
public class ExportLiNGOScenarioWizardPage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private final ISelection selection;

	private static final String FILTER_KEY = "lastSelection";
	private static final String SECTION_NAME = "ExportLiNGOWizardPage.section";

	private FileFieldEditor editor;

	protected ExportLiNGOScenarioWizardPage(ISelection selection) {
		super("Export .LiNGO Scenario", "Export Scenario", null);
		this.selection = selection;
	}

	@Override
	public boolean isPageComplete() {
		// String stringValue = editor.getStringValue();
		// if (stringValue != null && stringValue.endsWith(".lingo")) {
		// File f= new File(stringValue);
		// if (f.isFile()) {
		// return true;
		//
		// }
		// }
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
		final Listener listener = e -> dialogChanged();

		scenarioServiceSelectionGroup = new ScenarioServiceSelectionGroup(container, listener, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		gd.heightHint = 320;
		gd.horizontalSpan = 2;
		scenarioServiceSelectionGroup.setLayoutData(gd);

		final Group destination = new Group(container, SWT.NONE);
		destination.setText("Destination file");
		GridLayout layout2 = new GridLayout();
		destination.setLayout(layout2);
		destination.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		destination.setFont(parent.getFont());
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.horizontalSpan = 2;
		destination.setLayoutData(gd2);

		final FileFieldEditor editor = new FileFieldEditor("destination-file", "Export to .lingo file:", destination);
		editor.setFileExtensions(new String[] { ".lingo" });
		this.editor = editor;
		editor.getTextControl(destination).addModifyListener(e -> dialogChanged());

		// get the default export directory from the settings
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		final IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		final String filter = section == null ? null : section.get(FILTER_KEY);

		// use it to populate the editor
		editor.setStringValue(filter);

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
		String stringValue = editor.getStringValue();
		if (stringValue == null || !stringValue.endsWith(".lingo")) {
			updateStatus("A .lingo file name needs to be specified.");
			return;
		}
		File f = new File(stringValue);
		File p = f.getParentFile();
		if (p == null) {
			updateStatus("A full path needs to be specified.");
			return;

		}
		updateStatus(null);
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public ScenarioInstance getScenarioInstance() {
		return (ScenarioInstance) scenarioServiceSelectionGroup.getSelectedContainer();
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
	}
}
