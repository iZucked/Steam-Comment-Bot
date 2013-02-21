/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
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

public class ScenarioServiceLabelProvider extends AdapterFactoryLabelProvider {
	private final ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider> selectionProviderTracker = new ServiceTracker<IScenarioServiceSelectionProvider, IScenarioServiceSelectionProvider>(
			Activator.getDefault().getBundle().getBundleContext(), IScenarioServiceSelectionProvider.class, null);

	private IEclipseJobManager jobManager = null;
	private Image showEnabledImage;
	private Image showDisabledImage;

	private Image noJobImage;

	private Image jobComplete;

	private Color majorColor;

	private Color defaultMinorColour;

	private Image pinImage;

	public ScenarioServiceLabelProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
		selectionProviderTracker.open();
		showEnabledImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/console_view.gif").createImage();
		showDisabledImage = ImageDescriptor.createWithFlags(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/console_view.gif"), SWT.IMAGE_DISABLE).createImage();
		pinImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/pin_editor.gif").createImage();
		noJobImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/no_job.gif").createImage();
		jobComplete = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/complete_job.gif").createImage();
		majorColor = new Color(Display.getDefault(), 240, 80, 85);
		defaultMinorColour = new Color(Display.getDefault(), 100, 230, 120);
	}

	@Override
	public void dispose() {
		selectionProviderTracker.close();

		showEnabledImage.dispose();
		showDisabledImage.dispose();
		noJobImage.dispose();
		jobComplete.dispose();
		pinImage.dispose();

		defaultMinorColour.dispose();
		majorColor.dispose();

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

	@Override
	public Image getColumnImage(final Object object, final int columnIndex) {
		switch (columnIndex) {
		case ScenarioServiceNavigator.COLUMN_SHOW_IDX:
			// virtual checkbox
			if (object instanceof ScenarioInstance) {
				final IScenarioServiceSelectionProvider service = selectionProviderTracker.getService();
				if (service != null) {
					if (service.isSelected((ScenarioInstance) object)) {
						if (service.getPinnedInstance() == object) {
							return pinImage;
						} else {
							return showEnabledImage;
						}
					} else {
						return showDisabledImage;
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
							return jobComplete;
						}
						final Color minorColor = (control.getJobState() == EJobState.PAUSED || control.getJobState() == EJobState.PAUSING) ? Display.getDefault().getSystemColor(SWT.COLOR_YELLOW)
								: defaultMinorColour;
						return PieChartRenderer.renderPie(minorColor, majorColor, control.getProgress() / 100.0);
					} else {
						return noJobImage;
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
