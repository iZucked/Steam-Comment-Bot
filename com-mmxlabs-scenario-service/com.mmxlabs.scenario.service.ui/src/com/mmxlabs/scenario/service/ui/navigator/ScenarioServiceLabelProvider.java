/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
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
		case 0:
			return super.getColumnText(object, columnIndex);
		default:
			return "";
		}
	}

	private IEclipseJobManager jobManager = null;
	
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
		case 2:
			if (object instanceof ScenarioInstance) {
				final ScenarioInstance instance = (ScenarioInstance) object;
				if (jobManager == null) {
					jobManager = Activator.getDefault().getEclipseJobManager();
				}
				if (jobManager != null) {
					final IJobDescriptor job = jobManager.findJobForResource(instance.getUuid());
					final IJobControl control = jobManager.getControlForJob(job);
					if (control != null) {
						final Color minorColor = 
								(control.getJobState() == EJobState.PAUSED || control.getJobState() == EJobState.PAUSING) ?
										Display.getDefault().getSystemColor(SWT.COLOR_YELLOW) :
								new Color(Display.getDefault(), 100, 230, 120);
						final Color majorColor = new Color(Display.getDefault(), 240, 80, 85);
						
						return PieChartRenderer.renderPie(minorColor, majorColor, control.getProgress() / 100.0);
					}
				}
			}
			break;
		case 0:
			return super.getColumnImage(object, columnIndex);
		}
		return null;
	}

}
