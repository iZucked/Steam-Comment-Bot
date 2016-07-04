/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.mergeWizards;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
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
public class SharedDataScenariosSelectionPage extends WizardPage {

	public enum DataOptions {
		PortData("Port"), FleetDatabase("Fleet"), CommercialData("Commercial"), PricingData("Price Curves");

		private DataOptions(final String name) {
			this.name = name;
		}

		private final String name;

		public String getName() {
			return name;
		}
	}

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private final ISelection selection;

	private final Set<DataOptions> selectedDataOptions = EnumSet.noneOf(DataOptions.class);

	protected SharedDataScenariosSelectionPage(final ISelection selection) {
		super("Select shared scenario");
		this.selection = selection;
	}

	@Override
	public boolean isPageComplete() {
		return scenarioServiceSelectionGroup.getSelectedContainer() instanceof ScenarioInstance && super.isPageComplete();
	}

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
		final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		gd.heightHint = 320;
		gd.horizontalSpan = 2;
		scenarioServiceSelectionGroup.setLayoutData(gd);

		final Group dataOptions = new Group(container, SWT.NONE);
		dataOptions.setText("Data Options");
		final GridLayout layout2 = new GridLayout();
		dataOptions.setLayout(layout2);
		dataOptions.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		dataOptions.setFont(parent.getFont());

		for (final DataOptions option : DataOptions.values()) {
			final Button btn = new Button(dataOptions, SWT.CHECK);
			btn.setText(option.getName());
			btn.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (btn.getSelection()) {
						selectedDataOptions.add(option);
					} else {
						selectedDataOptions.remove(option);
					}
					dialogChanged();
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
				}
			});
		}

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

		final Container c = scenarioServiceSelectionGroup.getSelectedContainer();
		if (!(c instanceof ScenarioInstance)) {
			updateStatus("A Scenario must be selected");
			return;
		}

		if (getSelectedDataOptions().isEmpty()) {
			updateStatus("At least one data option should be selected");
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

	public Set<DataOptions> getSelectedDataOptions() {
		return selectedDataOptions;
	}
}
