/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.navigator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ShippingCostPlanSSListenerFactoryService {

	private final Map<IScenarioService, ShippingCostPlanSSListener> map = new HashMap<IScenarioService, ShippingCostPlanSSListener>();

	public void bindScenarioService(final IScenarioService service) {
		final ShippingCostPlanSSListener listener = new ShippingCostPlanSSListener();
		service.addScenarioServiceListener(listener);
		map.put(service, listener);

		// This can block fork off in thread.
		new Thread("ShippingCostPlanSSListenerFactoryService:bind") {
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
		final ShippingCostPlanSSListener listener = map.remove(service);
		listener.dispose();
	}
}
