package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

public class ScenarioServiceLabelProvider extends AdapterFactoryLabelProvider {

	public ScenarioServiceLabelProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
	}
}
