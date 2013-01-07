/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.scenarios;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * The {@link ScenarioInstanceValidatorScenarioServiceListener} is an {@link IScenarioServiceListener} implementation intended to be registered as a OSGi service. It adds itself a a listener to
 * {@link IScenarioService}s and hooks in to the post load event. It will then attach a {@link ScenarioInstanceValidator} to loaded {@link ScenarioInstance}s.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioInstanceValidatorScenarioServiceListener extends ScenarioServiceListener {

	private final Set<IScenarioService> scenarioServices = new HashSet<IScenarioService>();

	private final Map<ScenarioInstance, ScenarioInstanceValidator> instanceMap = new HashMap<ScenarioInstance, ScenarioInstanceValidator>();

	public ScenarioInstanceValidatorScenarioServiceListener() {
	}

	public void hookExisting(final IScenarioService scenarioService) {
		hookExisting(scenarioService.getServiceModel());
	}

	private void hookExisting(final Container container) {
		for (final Container c : container.getElements()) {
			if (c instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) c;
				if (scenarioInstance.getInstance() != null) {
					final ScenarioInstanceValidator validator = new ScenarioInstanceValidator(scenarioInstance);
					instanceMap.put(scenarioInstance, validator);
					validator.performValidation();
				}
			}
			hookExisting(c);
		}
	}

	@Override
	public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final ScenarioInstanceValidator validator = new ScenarioInstanceValidator(scenarioInstance);
		instanceMap.put(scenarioInstance, validator);
		validator.performValidation();
	}

	@Override
	public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		onPreScenarioInstanceUnload(scenarioService, scenarioInstance);
	}

	@Override
	public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final ScenarioInstanceValidator validator = instanceMap.get(scenarioInstance);
		if (validator != null) {
			validator.dispose();
		}
		instanceMap.remove(scenarioInstance);
	}

	public void dispose() {
		for (final IScenarioService s : scenarioServices) {
			s.removeScenarioServiceListener(this);
		}
		scenarioServices.clear();

		for (final ScenarioInstanceValidator v : instanceMap.values()) {
			v.dispose();
		}
		instanceMap.clear();
	}

	public void bind(final IScenarioService scenarioService) {
		scenarioServices.add(scenarioService);
		hookExisting(scenarioService);
		scenarioService.addScenarioServiceListener(this);
	}

	public void unbind(final IScenarioService scenarioService) {
		scenarioServices.add(scenarioService);
		scenarioService.removeScenarioServiceListener(this);
	}

}
