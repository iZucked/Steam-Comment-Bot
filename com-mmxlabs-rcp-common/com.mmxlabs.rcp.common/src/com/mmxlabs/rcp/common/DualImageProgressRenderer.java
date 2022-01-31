/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.Arrays;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.internal.Activator;

/**
 * A progress renderer to render between two images flipping from enabled to
 * disabled based on progress completed.
 * 
 * Assumes images are 16x16 pixels.
 * 
 * @author Simon Goodall
 *
 */
public class DualImageProgressRenderer {

	private DualImageProgressRenderer() {

	}

	/**
	 * 
	 * @param path
	 * @param p    Progress (0.0 -> 1.0);
	 * @return
	 */
	public static Image renderProgress(final CommonImages.IconPaths path, final double p) {

		int partialWidth = (int) Math.round(16.0 * p);
		// Ensure bounds are valid
		if (partialWidth > 16) {
			partialWidth = 16;
		}
		if (partialWidth < 0) {
			partialWidth = 0;
		}

		final String imageName = "dProgress-" + path.toString() + "-" + partialWidth;
		final Image cache = Activator.getDefault().getImageRegistry().get(imageName);
		if (cache != null) {
			return cache;
		}

		final ImageDescriptor enabledDesc = CommonImages.getImageDescriptor(path, IconMode.Enabled);
		final ImageDescriptor disabledDesc = CommonImages.getImageDescriptor(path, IconMode.Disabled);
		// Currently some source images are not exactly 16x16
		int width = enabledDesc.getImageData().width;
		int height = enabledDesc.getImageData().height;
		final Image image = new Image(Display.getDefault(), width, height);
		final GC gc = new GC(image);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		// Draw the full disabled image
		{
			final Image img = disabledDesc.createImage();
			gc.drawImage(img, 0, 0);
			img.dispose();
		}
		// Draw the partial enabled case
		{
			final Image img = enabledDesc.createImage();
			gc.drawImage(img, 0, 0, partialWidth, 16, 0, 0, partialWidth, 16);
			img.dispose();
		}

		// Reload (either image) to copy the alpha channel information across.
		final ImageData data = image.getImageData();
		final Image img = enabledDesc.createImage();
		data.alphaData = Arrays.copyOf(img.getImageData().alphaData, img.getImageData().alphaData.length);
		img.dispose();

		gc.dispose();

		image.dispose();

		final Image result = new Image(Display.getDefault(), data);
		Activator.getDefault().getImageRegistry().put(imageName, result);
		return result;
	}
}
