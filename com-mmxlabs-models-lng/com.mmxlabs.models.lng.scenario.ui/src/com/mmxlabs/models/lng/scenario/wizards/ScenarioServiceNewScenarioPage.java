/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.wizards;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
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
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;

/**
 * The "New" wizard page allows setting the container for the new file as well as the file name. The page will only accept file name without the extension OR with the extension that matches the
 * expected one (scn).
 */

public class ScenarioServiceNewScenarioPage extends WizardPage {

	private static final String SECTION_NAME = "CSVImportMainPage.section";
	private static final String ScenarioName_KEY = null;
	private static final String DefaultScenarioNameRoot = "CSVimport";
	private static int counter = 0;
	

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private Text fileText;

	private final ISelection selection;


	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ScenarioServiceNewScenarioPage(final ISelection selection) {
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
		
		final Label label = new Label(container, SWT.NULL);
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
		fileText.setFocus();
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		final Bundle bundle = FrameworkUtil.getBundle(getClass());
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
		
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(SECTION_NAME);

		String scenarioName = "";
		if (section == null) {
			section = dialogSettings.addNewSection(SECTION_NAME);
		}
		else{
			scenarioName = section.get(ScenarioName_KEY);
		}
		if(scenarioName == null){
			scenarioName = DefaultScenarioNameRoot + ++counter;			
		}
		
		fileText.setText(scenarioName);		
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

		final String scenarioName = fileText.getText();
		if (scenarioName.isEmpty()) {
			updateStatus("A name for the scenario must be specified");
			return;
		}
		
		if (scenarioName.length() > 0 && !scenarioName.startsWith(DefaultScenarioNameRoot)){
			final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
			IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
			if(section==null) {
				section = dialogSettings.addNewSection(SECTION_NAME);
			}
			section.put(ScenarioName_KEY, scenarioName);
		}
		
		// Check for naming conflicts
		final Set<String> existing = new HashSet<String>();
		for (final Container e : c.getElements()) {
			existing.add(e.getName());
		}
		if (existing.contains(scenarioName)) {
			updateStatus("A item with the specified name already exists.");
			return;
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

	public String getFileName() {
		return fileText.getText();
	}
}