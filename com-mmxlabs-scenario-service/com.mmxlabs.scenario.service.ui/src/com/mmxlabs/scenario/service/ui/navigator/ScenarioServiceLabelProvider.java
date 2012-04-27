/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class ScenarioServiceLabelProvider extends AdapterFactoryLabelProvider implements ITableLabelProvider {
	private final ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> selectionProviderTracker
		= new ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider>(Activator.getDefault().getBundle().getBundleContext(), IScenarioServiceSelectionProvider.class, null);
	
	public ScenarioServiceLabelProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
		selectionProviderTracker.open();
	}
	
	@Override
	public void dispose() {
		selectionProviderTracker.close();
		super.dispose();
	}

	@Override
	public String getColumnText(Object object, int columnIndex) {
		switch (columnIndex) {
		case 1:
			
			return "";
		default:
			return super.getColumnText(object, columnIndex);
		}
	}

	@Override
	public Image getColumnImage(Object object, int columnIndex) {
		switch (columnIndex) {
		case 1:
			// virtual checkbox
			if (object instanceof ScenarioInstance) {
				final IScenarioServiceSelectionProvider service = selectionProviderTracker.getService();
				if (service != null) {
					if (service.isSelected((ScenarioInstance) object)) {
						return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/synced.png").createImage();
					} else {
						return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/synced-grey.png").createImage();
					}
				}
			}
			return null;
		default:
			return super.getColumnImage(object, columnIndex);
		}
	}

}
