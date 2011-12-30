package com.mmxlabs.scenario.service.ui.resource;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import com.mmxlabs.scenario.service.IScenarioService;

public class ScenarioServiceResourceImpl extends ResourceImpl {

	private final IScenarioService scenarioService;

	public ScenarioServiceResourceImpl(final IScenarioService scenarioService) {
		super();
		this.scenarioService = scenarioService;
	}

	public ScenarioServiceResourceImpl(final IScenarioService scenarioService, final URI uri) {
		super(uri);
		this.scenarioService = scenarioService;
	}

	@Override
	public void save(final Map<?, ?> options) throws IOException {

		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void load(final Map<?, ?> options) throws IOException {

		final EObject scenario = scenarioService.getScenario(uri.path());

		this.getContents().clear();
		this.getContents().add(scenario);
	}
}
