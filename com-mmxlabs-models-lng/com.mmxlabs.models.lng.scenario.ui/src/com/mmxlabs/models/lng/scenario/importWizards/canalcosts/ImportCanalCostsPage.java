/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.canalcosts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.scenario.importWizards.ImportUtils;
import com.mmxlabs.models.lng.scenario.ui.ScenarioContentProvider;
import com.mmxlabs.models.lng.scenario.ui.ScenarioLabelProvider;
import com.mmxlabs.rcp.common.controls.RadioSelectionGroup;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;

/**
 */
public class ImportCanalCostsPage extends WizardPage {
	
	protected Control control = null;
	protected RadioSelectionGroup scenarioSelectionGroup;
	protected CheckboxTreeViewer scenarioTreeViewer;
	protected FileFieldEditor importFileEditor;
	
	protected final ScenarioInstance currentScenario;

	public ImportCanalCostsPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName);
		this.currentScenario = currentScenario;
		setTitle("Select data and scenarios");
		setDescription("Choose scenario(s) and a file for canal costs import.");
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		final GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		final GridData ld = new GridData();
		ld.verticalIndent = 0;
		ld.horizontalAlignment = SWT.FILL;
		ld.grabExcessHorizontalSpace = true;
		container.setLayoutData(ld);

		// create a radiobutton group for specifying how scenarios are selected
		final int selectedOnlyIndex;
		if (currentScenario != null) {
			final String currentScenarioOption = String.format("Current ('%s')", currentScenario.getName());
			scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { "All", currentScenarioOption, "Selected" },
					new int[] { ImportUtils.CHOICE_ALL_SCENARIOS, ImportUtils.CHOICE_CURRENT_SCENARIO, ImportUtils.CHOICE_SELECTED_SCENARIOS });
			selectedOnlyIndex = 2;
		} else {
			scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { "All", "Selected" }, new int[] { ImportUtils.CHOICE_ALL_SCENARIOS, ImportUtils.CHOICE_SELECTED_SCENARIOS });
			selectedOnlyIndex = 1;
		}
		scenarioSelectionGroup.setSelectedIndex(1);
		final GridData gd3 = new GridData();
		gd3.horizontalAlignment = SWT.FILL;
		gd3.grabExcessHorizontalSpace = true;
		gd3.horizontalSpan = 2;
		scenarioSelectionGroup.setLayoutData(gd3);

		// create a container for the scenario tree control (so we can hide it)
		final Composite viewerComposite = new Composite(container, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, true));
		final GridData viewerCompositeLayoutData = new GridData();
		// viewerCompositeLayoutData.widthHint = 250;
		viewerCompositeLayoutData.grabExcessVerticalSpace = true;
		viewerCompositeLayoutData.grabExcessHorizontalSpace = true;
		viewerCompositeLayoutData.verticalAlignment = SWT.FILL;
		viewerCompositeLayoutData.horizontalAlignment = SWT.FILL;
		viewerCompositeLayoutData.horizontalSpan = 2;
		viewerComposite.setLayoutData(viewerCompositeLayoutData);

		// create a control to display the scenario tree
		scenarioTreeViewer = new CheckboxTreeViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		scenarioTreeViewer.setContentProvider(new ScenarioContentProvider());
		scenarioTreeViewer.setLabelProvider(new ScenarioLabelProvider());
		scenarioTreeViewer.setAutoExpandLevel(3);
		scenarioTreeViewer.setInput(ImportUtils.getLocalServices());
		final GridData viewerLayoutData = new GridData();
		viewerLayoutData.grabExcessVerticalSpace = true;
		viewerLayoutData.grabExcessHorizontalSpace = true;
		viewerLayoutData.verticalAlignment = SWT.FILL;
		viewerLayoutData.horizontalAlignment = SWT.FILL;
		scenarioTreeViewer.getTree().setLayoutData(viewerLayoutData);

		// when a parent element checkbox is clicked in the tree, propagate the change to its descendants
		scenarioTreeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(final CheckStateChangedEvent event) {
				final Object element = event.getElement();
				if (!(element instanceof ScenarioInstance)) {
					scenarioTreeViewer.setSubtreeChecked(element, event.getChecked());
				}
			}
		});

		// only enable the scenario tree when the appropriate radio button is selected
		final Button selectedOnlyButton = scenarioSelectionGroup.getButtons().get(selectedOnlyIndex);
		scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
		selectedOnlyButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});

		setControl(container);
		control = container;
		
	}
	
	/**
	 * Returns a list of the scenario instances selected in the tree control.
	 * 
	 * @return
	 */
	protected List<ScenarioInstance> getCheckedScenariosFromTree() {
		final List<ScenarioInstance> result = new ArrayList<ScenarioInstance>();

		for (final Object object : scenarioTreeViewer.getCheckedElements()) {
			if (object instanceof ScenarioInstance) {
				result.add((ScenarioInstance) object);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all known scenario instances.
	 * 
	 * @return
	 */
	protected List<ScenarioInstance> getAllAvailableScenarioInstances() {
		final List<ScenarioInstance> result = new LinkedList<ScenarioInstance>();

		for (final ScenarioService service : ImportUtils.getLocalServices()) {
			result.addAll(getAllDescendantScenarioInstances(service));
		}

		return result;
	}

	/**
	 * Returns a list of all scenario instances which descend (directly or indirectly) from a given parent container.
	 * 
	 * @param parent
	 * @return
	 */
	protected List<ScenarioInstance> getAllDescendantScenarioInstances(final Container parent) {
		final List<ScenarioInstance> result = new LinkedList<ScenarioInstance>();

		for (final Object object : parent.getElements()) {
			if (object instanceof ScenarioInstance) {
				result.add((ScenarioInstance) object);
			}
			if (object instanceof Container) {
				result.addAll(getAllDescendantScenarioInstances((Container) object));
			}
		}

		return result;
	}

	/**
	 * Returns the scenarios explicitly or implicitly selected by the user. If the user specified "All scenarios", return all available scenarios. If the user specified "Current scenario", return the
	 * current editing scenario only. If the user specified "Selected scenarios only", return the scenarios selected in the tree control.
	 * 
	 * @return
	 */
	public List<ScenarioInstance> getSelectedScenarios() {

		switch (scenarioSelectionGroup.getSelectedValue()) {
		case ImportUtils.CHOICE_ALL_SCENARIOS: { // "All Scenarios"
			return getAllAvailableScenarioInstances();
		}
		case 1: { // "Current Scenario"
			return Arrays.asList(new ScenarioInstance[] { currentScenario });
		}
		case 2: { // "Selected Scenarios Only"
			return getCheckedScenariosFromTree();
		}
		}

		return null;
	}
	
}
