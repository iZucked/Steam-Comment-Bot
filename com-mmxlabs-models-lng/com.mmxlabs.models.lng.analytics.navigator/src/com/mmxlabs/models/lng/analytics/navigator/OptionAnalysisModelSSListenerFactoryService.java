/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.navigator;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class OptionAnalysisModelSSListenerFactoryService {

	private final Map<IScenarioService, OptionAnalysisModelSSListener> map = new ConcurrentHashMap<>();

	public void bindScenarioService(final IScenarioService service) {
		final OptionAnalysisModelSSListener listener = new OptionAnalysisModelSSListener();
		service.addScenarioServiceListener(listener);
		map.put(service, listener);

		// This can block fork off in thread.
		new Thread("OptionAnalysisModelSSListenerFactoryService:bind") {
			@Override
			public void run() {
				final Iterator<EObject> itr = service.getServiceModel().eAllContents();
				while (itr.hasNext()) {
					final EObject eObj = itr.next();
					if (eObj instanceof ScenarioInstance) {
						final ScenarioInstance scenarioInstance = (ScenarioInstance) eObj;
						if (scenarioInstance.getInstance() != null) {
							listener.onPostScenarioInstanceLoad(scenarioInstance.getScenarioService(), scenarioInstance);
						}
					}
				}

			};
		}.start();
	}

	public void unbindScenarioService(final IScenarioService service) {
		final OptionAnalysisModelSSListener listener = map.remove(service);
		listener.dispose();
	}
}
