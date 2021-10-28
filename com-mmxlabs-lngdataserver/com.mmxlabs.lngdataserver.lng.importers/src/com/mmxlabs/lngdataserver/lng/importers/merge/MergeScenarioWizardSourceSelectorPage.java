/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;

public class MergeScenarioWizardSourceSelectorPage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private final ISelection selection;

	private static final String SECTION_NAME = "MergeScenarioWizardSourceSelectorPage.section";

	protected MergeScenarioWizardSourceSelectorPage(ISelection selection) {
		super("Select scenario to merge in", "Select source scenario", null);
		this.selection = selection;
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

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			IWizardPage[] pages = super.getWizard().getPages();

			// Ugly but best to do this here, to save doing it multiple times.
			ScenarioInstance source = getScenarioInstance();
			ScenarioInstance target = ((MergeScenarioWizard) super.getWizard()).getTargetScenarioInstance();

			final ScenarioModelRecord sourceModelRecordSource = SSDataManager.Instance.getModelRecord(source);
			try (IScenarioDataProvider scenarioDataProviderSource = sourceModelRecordSource.aquireScenarioDataProvider("MergeScenarioWizardContractMapperPage.getItems1")) {
				final LNGScenarioModel smSource = scenarioDataProviderSource.getTypedScenario(LNGScenarioModel.class);

				final ScenarioModelRecord sourceModelRecordTarget = SSDataManager.Instance.getModelRecord(target);
				try (IScenarioDataProvider scenarioDataProviderTarget = sourceModelRecordTarget.aquireScenarioDataProvider("MergeScenarioWizardContractMapperPage.getItems2")) {
					final LNGScenarioModel smTarget = scenarioDataProviderTarget.getTypedScenario(LNGScenarioModel.class);

					for (var p : pages) {
						if (p instanceof IScenarioDependent) {
							((IScenarioDependent) p).update(target.getName(), source.getName(), smTarget, smSource);
						}
					}
				}
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

	public ScenarioInstance getScenarioInstance() {
		if (scenarioServiceSelectionGroup.getSelectedContainer() instanceof ScenarioInstance) {
			return (ScenarioInstance) scenarioServiceSelectionGroup.getSelectedContainer();
		} else {
			return null;
		}
	}
}
