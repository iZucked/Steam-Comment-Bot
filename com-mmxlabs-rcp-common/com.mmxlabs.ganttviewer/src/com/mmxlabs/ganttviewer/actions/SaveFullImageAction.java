/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.ganttchart.GanttChart;
import org.eclipse.nebula.widgets.ganttchart.GanttComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.mmxlabs.ganttviewer.internal.Activator;

/**
 */
public class SaveFullImageAction extends Action {
	private final GanttChart ganttChart;

	public SaveFullImageAction(final GanttChart ganttChart) {
		super();
		setText("Save Image");
		setImageDescriptor(Activator.getImageDescriptor("/icons/etool16/saveas_edit.gif"));
		setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/dtool16/saveas_edit.gif"));
		this.ganttChart = ganttChart;
	}

	@Override
	public void run() {

		final GanttComposite composite = ganttChart.getGanttComposite();
		final Image full = composite.getFullImage();

		final FileDialog fd = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		fd.setFilterExtensions(new String[] { ".jpg" });
		fd.setFilterNames(new String[] { "JPG File" });
		fd.setFileName("img.jpg");
		final String path = fd.open();
		if (path == null) {
			return;
		}

		final ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { full.getImageData() };
		imageLoader.save(path, SWT.IMAGE_JPEG);
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && (ganttChart != null) && ganttChart.getSettings().enableZooming();
	}

}
