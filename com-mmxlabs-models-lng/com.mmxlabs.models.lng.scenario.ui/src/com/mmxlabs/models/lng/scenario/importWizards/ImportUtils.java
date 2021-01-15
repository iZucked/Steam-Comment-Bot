/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.util.LinkedList;
import java.util.List;

import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;

public class ImportUtils {
	
	public static final int CHOICE_ALL_SCENARIOS = 0;
	public static final int CHOICE_CURRENT_SCENARIO = CHOICE_ALL_SCENARIOS + 1;
	public static final int CHOICE_SELECTED_SCENARIOS = CHOICE_CURRENT_SCENARIO + 1;
	/**
	 * Returns a list of all scenario services currently available.
	 * 
	 * @return
	 */
	public static List<ScenarioService> getServices() {
		final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(
				Activator.getDefault().getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();
		final ScenarioModel scenarioModel = tracker.getService().getScenarioModel();
		tracker.close();

		return scenarioModel.getScenarioServices();
	}
	
	/**
	 * Returns a list of all local scenario services currently available.
	 * 
	 * @return
	 */
	public static List<ScenarioService> getLocalServices() {
		final List<ScenarioService> localServices = new LinkedList<>();

		for (final ScenarioService ss : getServices()) {
			if (ss.isLocal()) {
				localServices.add(ss);
			}
		}
		return localServices;
	}
}
