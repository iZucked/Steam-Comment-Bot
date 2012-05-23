/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
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
import com.mmxlabs.scenario.service.ui.internal.Activator;
import com.mmxlabs.scenario.service.ui.navigator.PieChartRenderer;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceComposedAdapterFactory;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceNavigator;

public class ScenarioServiceLabelProvider extends AdapterFactoryLabelProvider implements ITableLabelProvider {
	private final ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> selectionProviderTracker = new ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider>(
			Activator.getDefault().getBundle().getBundleContext(), IScenarioServiceSelectionProvider.class, null);

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
	public String getColumnText(final Object object, final int columnIndex) {
		switch (columnIndex) {
		case ScenarioServiceNavigator.COLUMN_NAME_IDX:
			String text = super.getColumnText(object, columnIndex);

			if (object instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) object;
				if (scenarioInstance.isDirty()) {
					text = "* " + text;
				}
			}

			return text;
		default:
			return "";
		}
	}

	private IEclipseJobManager jobManager = null;

	@Override
	public Image getColumnImage(final Object object, final int columnIndex) {
		switch (columnIndex) {
		case ScenarioServiceNavigator.COLUMN_SHOW_IDX:
			// virtual checkbox
			if (object instanceof ScenarioInstance) {
				final IScenarioServiceSelectionProvider service = selectionProviderTracker.getService();
				if (service != null) {
					if (service.isSelected((ScenarioInstance) object)) {
						return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/pin_editor.gif").createImage();
					} else {
						return ImageDescriptor.createWithFlags(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/pin_editor.gif"), SWT.IMAGE_DISABLE).createImage();
					}
				}
			}
			return null;
		case ScenarioServiceNavigator.COLUMN_PROGRESS_IDX:
			if (object instanceof ScenarioInstance) {
				final ScenarioInstance instance = (ScenarioInstance) object;
				if (jobManager == null) {
					jobManager = Activator.getDefault().getEclipseJobManager();
				}
				if (jobManager != null) {
					final IJobDescriptor job = jobManager.findJobForResource(instance.getUuid());
					final IJobControl control = jobManager.getControlForJob(job);
					if (control != null) {
						if (control.getJobState() == EJobState.COMPLETED) {
							return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/complete_job.gif").createImage();
						}
						final Color minorColor = (control.getJobState() == EJobState.PAUSED || control.getJobState() == EJobState.PAUSING) ? Display.getDefault().getSystemColor(SWT.COLOR_YELLOW)
								: new Color(Display.getDefault(), 100, 230, 120);
						final Color majorColor = new Color(Display.getDefault(), 240, 80, 85);

						return PieChartRenderer.renderPie(minorColor, majorColor, control.getProgress() / 100.0);
					} else {
						return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/no_job.gif").createImage();
					}
				}
			}
			break;
		case ScenarioServiceNavigator.COLUMN_NAME_IDX:
			return super.getColumnImage(object, columnIndex);
		}
		return null;
	}

}
