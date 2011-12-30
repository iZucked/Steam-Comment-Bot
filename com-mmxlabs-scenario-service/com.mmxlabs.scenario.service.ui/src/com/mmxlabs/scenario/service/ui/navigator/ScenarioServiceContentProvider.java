package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider {

	public ScenarioServiceContentProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
	}
}
