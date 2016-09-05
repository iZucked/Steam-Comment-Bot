/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.wizard;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.ui.celleditor.SingleColumnTableEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;

/**
 * @author hinton
 * 
 */
public class SelectionWizardPage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	private final ISelection selection;

	protected SelectionWizardPage(ISelection selection) {
		super("ADP Selection", "ADP Selection", null);
		this.selection = selection;
	}

	@Override
	public boolean isPageComplete() {
		if (getScenarioInstance().size() == 1) {
			return super.isPageComplete();
		}
		return false;
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
		final BundleContext bundleContext = FrameworkUtil.getBundle(SelectionWizardPage.class).getBundleContext();
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
		Container selectedContainer = scenarioServiceSelectionGroup.getSelectedContainer();
		if (selectedContainer instanceof ScenarioInstance) {
			return Collections.singleton((ScenarioInstance) selectedContainer);
		} else {
			return Collections.emptySet();
		}
	}

}
