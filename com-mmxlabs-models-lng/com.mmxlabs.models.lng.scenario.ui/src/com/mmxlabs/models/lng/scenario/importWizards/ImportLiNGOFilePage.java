/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;

public class ImportLiNGOFilePage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private final ISelection selection;

	private String filePath;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ImportLiNGOFilePage(final ISelection selection) {
		super("wizardPage");
		setTitle("Choose file name");
		setDescription("Select a name for a new scenario file and a location to store it in.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(final Composite parent) {
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
		scenarioServiceSelectionGroup.getContentProvider();

		scenarioServiceSelectionGroup.setSetShowOnlyCapsImport(true);

		final Button auto = new Button(container, SWT.NONE);
		auto.setText("Choose &File...");
		final Label directory = new Label(container, SWT.NONE);
		directory.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
//		if (lastDirectoryName != null) {
//			directory.setText(lastDirectoryName);
//		}
		auto.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				// display file open dialog and then fill out files if the exist.
				final FileDialog dd = new FileDialog(getShell());

				dd.setFilterExtensions(new String[] { "*.lingo" });

				final String d = dd.open();

				if (d != null) {
					final File dir = new File(d);
					if (dir.exists() && dir.isFile()) {
						filePath = d;
						// Trigger 'Next' button focus
						setPageComplete(true);
					}
				}

				dialogChanged();
			}
		});

		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		final Bundle bundle = FrameworkUtil.getBundle(getClass());
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<ScenarioServiceRegistry> serviceReference = bundleContext.getServiceReference(ScenarioServiceRegistry.class);

		scenarioServiceSelectionGroup.setInput(bundleContext.getService(serviceReference));

		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection ssel) {

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

		final Container c = scenarioServiceSelectionGroup.getSelectedContainer();
		if (!(c instanceof Folder || c instanceof ScenarioService)) {
			updateStatus("A Folder must be selected");
			return;
		}

		if (filePath == null || filePath.isEmpty()) {
			updateStatus("A source .lingo file must be specified");
			return;
		}

		// Check for naming conflicts
		final Set<String> existing = new HashSet<>();
		for (final Container e : c.getElements()) {
			existing.add(e.getName());
		}

		updateStatus(null);
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public Container getScenarioContainer() {
		return scenarioServiceSelectionGroup.getSelectedContainer();
	}

	public String getFilePath() {
		return filePath;
	}
}