package com.mmxlabs.models.lng.analytics.presentation;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.scenario.service.IScenarioService;

public class LiveEvaluatorServiceListener implements ServiceListener {

	private final Map<IScenarioService, LiveEvaluatorScenarioServiceListener> evaluatorMap = new HashMap<IScenarioService, LiveEvaluatorScenarioServiceListener>();

	private BundleContext context;

	public void init(final BundleContext context) throws InvalidSyntaxException {
		this.context = context;

		// Listen for new events.
		context.addServiceListener(this, "(objectClass=" + IScenarioService.class + ")");

		// Populate intial map
		final ServiceReference<?>[] serviceReferences = context.getServiceReferences(IScenarioService.class.getCanonicalName(), null);
		for (final ServiceReference<?> ref : serviceReferences) {
			final Object service = context.getService(ref);
			if (service instanceof IScenarioService) {
				final IScenarioService scenarioService = (IScenarioService) service;
				final LiveEvaluatorScenarioServiceListener listener = new LiveEvaluatorScenarioServiceListener();
				scenarioService.addScenarioServiceListener(listener);
				evaluatorMap.put(scenarioService, listener);
			}
		}
	}

	public void dispose() {
		context = null;
	}

	@Override
	public void serviceChanged(final ServiceEvent event) {
		if (event.getType() == ServiceEvent.REGISTERED) {
			final Object service = context.getService(event.getServiceReference());
			if (service instanceof IScenarioService) {
				final IScenarioService scenarioService = (IScenarioService) service;
				final LiveEvaluatorScenarioServiceListener listener = new LiveEvaluatorScenarioServiceListener();
				scenarioService.addScenarioServiceListener(listener);
				evaluatorMap.put(scenarioService, listener);
			}

		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			final Object service = context.getService(event.getServiceReference());
			if (service instanceof IScenarioService) {
				final IScenarioService scenarioService = (IScenarioService) service;
				final LiveEvaluatorScenarioServiceListener listener = evaluatorMap.get(scenarioService);
				((IScenarioService) service).removeScenarioServiceListener(listener);
			}
		}

	}

}
