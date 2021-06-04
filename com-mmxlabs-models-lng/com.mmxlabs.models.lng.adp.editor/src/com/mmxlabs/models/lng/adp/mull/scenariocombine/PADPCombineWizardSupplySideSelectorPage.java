package com.mmxlabs.models.lng.adp.mull.scenariocombine;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceSelectionGroup;

public class PADPCombineWizardSupplySideSelectorPage extends WizardPage {

	private ScenarioServiceSelectionGroup scenarioServiceSelectionGroup;

	protected PADPCombineWizardSupplySideSelectorPage() {
		super("Select supply side scenario to combine with", "Select supply side scenario to combine with", null);
	}

	@Override
	public void createControl(Composite parent) {

		final Composite container = new Composite(parent, SWT.NULL);

		final GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		final Listener listener = event -> dialogChanged();

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
		}
		else {
			return null;
		}
	}
}
