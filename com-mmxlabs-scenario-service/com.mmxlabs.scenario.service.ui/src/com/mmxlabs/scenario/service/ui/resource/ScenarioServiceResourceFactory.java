package com.mmxlabs.scenario.service.ui.resource;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ui.Activator;

public class ScenarioServiceResourceFactory implements Resource.Factory {

	/**
	 * Reference to {@link Activator} maintained map of services.
	 */
	private Map<String, WeakReference<IScenarioService>> services;

	public ScenarioServiceResourceFactory() {
		services = Activator.getDefault().getScenarioServices();
	}

	@Override
	public Resource createResource(URI uri) {

		final String service = uri.host();

		if (services.containsKey(service)) {

			final IScenarioService s = services.get(service).get();

			if (s != null) {
				return new ScenarioServiceResourceImpl(s, uri);
			}
		}

		return null;

	}

}
