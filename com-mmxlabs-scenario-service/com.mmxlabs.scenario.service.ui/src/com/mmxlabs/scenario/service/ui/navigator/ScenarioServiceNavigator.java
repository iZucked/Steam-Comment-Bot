package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.ui.navigator.CommonNavigator;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.scenario.service.Activator;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;

public class ScenarioServiceNavigator extends CommonNavigator {

	// private ScenarioServiceRegistry registry;

	// TODO: Change to use Servie listener to setINput on registry activation/deactivation.

	private ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker;

	public ScenarioServiceNavigator() {
		super();

		tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(Activator.getDefault().getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();
		// registry = (ScenarioServiceRegistry) PlatformUI.getWorkbench().getService(ScenarioServiceRegistry.class);

	}

	@Override
	public void dispose() {

		tracker.close();
		super.dispose();
	}

	@Override
	protected Object getInitialInput() {

		return tracker.getService().getScenarioModel();
	}
}
