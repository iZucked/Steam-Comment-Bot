package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import com.mmxlabs.scenario.service.model.provider.ScenarioServiceItemProviderAdapterFactory;

public class ScenarioServiceLabelProvider extends AdapterFactoryLabelProvider {

	public ScenarioServiceLabelProvider() {
		super(new ScenarioServiceItemProviderAdapterFactory());
	}
}
