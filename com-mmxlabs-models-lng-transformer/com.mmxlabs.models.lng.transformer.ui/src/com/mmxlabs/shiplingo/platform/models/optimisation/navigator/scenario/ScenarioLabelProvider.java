/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.scenario;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

public class ScenarioLabelProvider extends WorkbenchLabelProvider implements ICommonLabelProvider, ITableLabelProvider {

	private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

	private Image getCachedImage(final Object key) {

		if (imageCache.containsKey(key)) {
			return imageCache.get(key);
		}

		ImageDescriptor desc = null;
		if (key instanceof EJobState) {
			final EJobState state = (EJobState) key;

			switch (state) {
			case CANCELLED:
				return Display.getDefault().getSystemImage(SWT.ICON_ERROR);
			case CANCELLING:
				return Display.getDefault().getSystemImage(SWT.ICON_ERROR);

			case COMPLETED:

				desc = Activator.imageDescriptorFromPlugin("com.mmxlabs.jobmananger.ui", "/icons/elcl16/terminate_co.gif");
				break;
			case INITIALISED:
				desc = Activator.imageDescriptorFromPlugin("com.mmxlabs.jobmananger.ui","/icons/elcl16/terminate_co.gif");
				break;
			case PAUSED:
				desc = Activator.imageDescriptorFromPlugin("com.mmxlabs.jobmananger.ui","/icons/elcl16/suspend_co.gif");
				break;
			case PAUSING:
				desc = Activator.imageDescriptorFromPlugin("com.mmxlabs.jobmananger.ui","/icons/dlcl16/suspend_co.gif");
				break;
			case RESUMING:
				desc = Activator.imageDescriptorFromPlugin("com.mmxlabs.jobmananger.ui","/icons/dlcl16/resume_co.gif");
				break;
			case RUNNING:
				desc = Activator.imageDescriptorFromPlugin("com.mmxlabs.jobmananger.ui","/icons/elcl16/resume_co.gif");
				break;
			case UNKNOWN:
				return Display.getDefault().getSystemImage(SWT.ICON_WARNING);
			}
		} else {
			desc = Activator.imageDescriptorFromPlugin("com.mmxlabs.jobmananger.ui",key.toString());
		}

		// Cache image
		if (desc != null) {
			final Image img = desc.createImage();
			imageCache.put(key, img);
			return img;
		}

		return null;

	}

	@Override
	public void dispose() {

		for (final Image image : imageCache.values()) {
			image.dispose();
		}

		imageCache.clear();

		super.dispose();
	}

	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {

		if (columnIndex == 1) {
			if (element instanceof IResource) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IJobDescriptor job = jobManager.findJobForResource(element);

				if (job != null) {
					final IJobControl control = jobManager.getControlForJob(job);

					if (control != null) {
						return getCachedImage(control.getJobState());
					}
				}
			}

		}

		return null;
	}

	@Override
	public String getColumnText(final Object element, final int columnIndex) {
		if (columnIndex == 0) {

			if (element instanceof IResource) {
				return ((IResource) element).getName();
			}
			return getText(element);
		}

		if (columnIndex == 1) {
			if (element instanceof IResource) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IJobDescriptor job = jobManager.findJobForResource(element);
				if (job != null) {
					final IJobControl control = jobManager.getControlForJob(job);
					if (control != null) {
						final EJobState jobState = control.getJobState();
						if ((jobState == EJobState.RUNNING) || (jobState == EJobState.PAUSED) || (jobState == EJobState.PAUSING)) {
							return jobState.toString() + " (" + control.getProgress() + "%)";
						} else {
							return jobState.toString();
						}
					}
				}
			}
			// Needs to be none-empty string to avoid other label providers
			// kicking in!
			return " ";
		}

		return "";
	}

	@Override
	public void init(final ICommonContentExtensionSite aConfig) {

	}

	@Override
	public void restoreState(final IMemento aMemento) {

	}

	@Override
	public void saveState(final IMemento aMemento) {

	}

	@Override
	public String getDescription(final Object anElement) {
		if (anElement instanceof IResource) {
			return ((IResource) anElement).getFullPath().makeRelative().toString();
		}
		return null;
	}
}
