/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.deleteservice;

import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * The {@link DeletedScenarioServiceListener} is an {@link IScenarioServiceListener} intended to remove any optimisation jobs running on unload or delete events. This implementation is intended to be
 * registered as a OSGi service.
 * 
 * @author Simon Goodall
 * 
 */
public class DeletedScenarioServiceListener extends ScenarioServiceListener {

	private final Set<IScenarioService> scenarioServices = new HashSet<IScenarioService>();
	private IEclipseJobManager eclipseJobManager;

	public DeletedScenarioServiceListener() {
	}

	@Override
	public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		onPreScenarioInstanceUnload(scenarioService, scenarioInstance);
	}

	@Override
	public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		final String uuid = scenarioInstance.getUuid();

		final IJobDescriptor job = eclipseJobManager.findJobForResource(uuid);
		if (job == null) {
			return;
		}

		final IJobControl control = eclipseJobManager.getControlForJob(job);
		if (control != null) {

			control.cancel();
			control.dispose();
			eclipseJobManager.removeJob(job);
		}
	}

	public void dispose() {
		for (final IScenarioService s : scenarioServices) {
			s.removeScenarioServiceListener(this);
		}
		scenarioServices.clear();

	}

	public void bind(final IScenarioService scenarioService) {
		scenarioServices.add(scenarioService);
		scenarioService.addScenarioServiceListener(this);
	}

	public void unbind(final IScenarioService scenarioService) {
		scenarioServices.add(scenarioService);
		scenarioService.removeScenarioServiceListener(this);
	}

	public void bind(final IEclipseJobManager eclipseJobManager) {
		this.eclipseJobManager = eclipseJobManager;
	}

	public void unbind(final IEclipseJobManager eclipseJobManager) {
		this.eclipseJobManager = null;
	}
}
