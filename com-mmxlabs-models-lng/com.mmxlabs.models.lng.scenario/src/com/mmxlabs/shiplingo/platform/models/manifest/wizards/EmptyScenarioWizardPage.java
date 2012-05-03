/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.wizards;

import org.eclipse.jface.dialogs.IDialogPage;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.manifest.Activator;

/**
 * The "New" wizard page allows setting the container for the new file as well as the file name. The page will only accept file name without the extension OR with the extension that matches the
 * expected one (scn).
 */

public class EmptyScenarioWizardPage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private Text fileText;

	private final ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public EmptyScenarioWizardPage(final ISelection selection) {
		super("wizardPage");
		setTitle("Choose file name");
		setDescription("This wizard creates a new file with *.scn extension that can be opened by a multi-page editor.");
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

		Label label = new Label(container, SWT.NULL);
		label.setText("&File name:");
		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
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

		fileText.setText("empty_scenario.scn");
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {

		
		Container c = scenarioServiceSelectionGroup.getSelectedContainer();
		if (!(c instanceof Folder || c instanceof ScenarioInstance)) {
			updateStatus("A Folder or Scenario must be selected");
			return;
		}
		// IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));
		// String fileName = getFileName();
		//
		// if (getContainerName().length() == 0) {
		// updateStatus("File container must be specified");
		// return;
		// }
		// if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
		// updateStatus("File container must exist");
		// return;
		// }
		// if (!container.isAccessible()) {
		// updateStatus("Project must be writable");
		// return;
		// }
		// if (fileName.length() == 0) {
		// updateStatus("File name must be specified");
		// return;
		// }
		// if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
		// updateStatus("File name must be valid");
		// return;
		// }
		// int dotLoc = fileName.lastIndexOf('.');
		// if (dotLoc != -1) {
		// String ext = fileName.substring(dotLoc + 1);
		// if (ext.equalsIgnoreCase("scn") == false) {
		// updateStatus("File extension must be \"scn\"");
		// return;
		// }
		// }
		updateStatus(null);
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public Container getScenarioContainer() {
		return scenarioServiceSelectionGroup.getSelectedContainer();
	}

	public String getFileName() {
		return fileText.getText();
	}
}