/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.ui.Activator;

class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	private final Shell shell;
	private final IEclipseJobManager jobManager;
	private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

	public ViewLabelProvider(final Shell shell, final IEclipseJobManager jobManager) {
		this.shell = shell;
		this.jobManager = jobManager;
	}

	@Override
	public String getColumnText(final Object obj, final int index) {

		if (obj instanceof IJobManager) {
			if (index == 0) {
				return ((IJobManager) obj).getJobManagerDescriptor().getName();
			}
		} else if (obj instanceof IJobDescriptor) {
			final IJobDescriptor job = (IJobDescriptor) obj;
			final IJobControl control = jobManager.getControlForJob(job);
			switch (index) {
			case 1:
				return job.getJobName();
			case 2:
				return Integer.toString(control.getProgress()) + "%";
			case 3:
				return control.getJobState().toString();
			default:
				break;
			}
		}

		return null;
	}

	@Override
	public Image getColumnImage(final Object obj, final int index) {

		if (index == 1 && obj instanceof IJobDescriptor) {
			final IJobDescriptor job = (IJobDescriptor) obj;
			final IJobControl control = jobManager.getControlForJob(job);
			return getCachedImage(control.getJobState());
		}

		return null;
	}

	@Override
	public Image getImage(final Object obj) {
		return null;
	}

	Image getCachedImage(final Object key) {

		if (imageCache.containsKey(key)) {
			return imageCache.get(key);
		}

		ImageDescriptor desc = null;
		if (key instanceof EJobState) {
			final EJobState state = (EJobState) key;

			switch (state) {
			case CANCELLED:
				return shell.getDisplay().getSystemImage(SWT.ICON_ERROR);
			case CANCELLING:
				return shell.getDisplay().getSystemImage(SWT.ICON_ERROR);
			case COMPLETED:
				desc = Activator.getImageDescriptor("/icons/elcl16/terminate_co.gif");
				break;
			case INITIALISED:
				desc = Activator.getImageDescriptor("/icons/elcl16/terminate_co.gif");
				break;
			case PAUSED:
				desc = Activator.getImageDescriptor("/icons/elcl16/suspend_co.gif");
				break;
			case PAUSING:
				desc = Activator.getImageDescriptor("/icons/dlcl16/suspend_co.gif");
				break;
			case RESUMING:
				desc = Activator.getImageDescriptor("/icons/dlcl16/resume_co.gif");
				break;
			case RUNNING:
				desc = Activator.getImageDescriptor("/icons/elcl16/resume_co.gif");
				break;
			case UNKNOWN:
				return shell.getDisplay().getSystemImage(SWT.ICON_WARNING);
			default:
				break;
			}
		} else {
			desc = Activator.getImageDescriptor(key.toString());
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
}