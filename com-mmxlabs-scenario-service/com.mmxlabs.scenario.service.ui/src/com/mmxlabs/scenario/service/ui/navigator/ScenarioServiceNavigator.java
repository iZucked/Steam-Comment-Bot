/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.List;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.internal.Activator;
import com.mmxlabs.scenario.service.model.ScenarioModel;

public class ScenarioServiceNavigator extends CommonNavigator {

	protected AdapterFactoryEditingDomain editingDomain;
	protected ComposedAdapterFactory adapterFactory;

	private final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker;

	public ScenarioServiceNavigator() {
		super();

		tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(Activator.getDefault().getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();
	}

	@Override
	public void dispose() {

		tracker.close();
		super.dispose();
	}

	@Override
	protected Object getInitialInput() {

		final ScenarioModel scenarioModel = tracker.getService().getScenarioModel();

		return scenarioModel;
	}

	/**
	 * This accesses a cached version of the property sheet. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IPropertySheetPage getPropertySheetPage() {
		ExtendedPropertySheetPage propertySheetPage = new ExtendedPropertySheetPage(editingDomain) {
			@Override
			public void setSelectionToViewer(final List<?> selection) {
				// ScenarioServiceNavigator.this.setSelectionToViewer(selection);
				ScenarioServiceNavigator.this.setFocus();
			}

			@Override
			public void setActionBars(final IActionBars actionBars) {
				super.setActionBars(actionBars);
				// getActionBarContributor().shareGlobalActions(this, actionBars);
			}
		};
		propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));

		return propertySheetPage;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		} else {
			return super.getAdapter(key);
		}
	}
}
