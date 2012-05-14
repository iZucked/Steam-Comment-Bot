/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.exportWizards;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

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

import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;
import com.mmxlabs.shiplingo.platform.models.manifest.internal.Activator;

/**
 * @author hinton
 * 
 */
public class ExportCSVWizardPage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private final ISelection selection;

	private DirectoryFieldEditor editor;

	protected ExportCSVWizardPage(ISelection selection) {
		super("Export Scenario as CSV");
		this.selection = selection;
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete() && new File(editor.getStringValue()).exists();
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

		final DirectoryFieldEditor editor = new DirectoryFieldEditor("destination-directory", "Export to directory:", destination);

		this.editor = editor;
		editor.getTextControl(destination).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

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

		// TODO: Create automatically?
		if (!new File(editor.getStringValue()).exists()) {
			updateStatus("Output diectory must exist");
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

}
