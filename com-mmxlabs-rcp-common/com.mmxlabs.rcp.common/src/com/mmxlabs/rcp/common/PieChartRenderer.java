/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.rcp.common.internal.Activator;

/**
 * Draws pie chart icons for the navigator progress indicator.
 * 
 * @author hinton
 * 
 */
public class PieChartRenderer {

	private static Color majorColor = new Color(Display.getDefault(), 240, 80, 85);
	private static Color defaultMinorColour = new Color(Display.getDefault(), 100, 230, 120);
//	-		

	public static Image renderPie(final double p) {
		return renderPie(defaultMinorColour, majorColor, p);
	}

	public static Image renderPie(final Color minorColor, final Color majorColor, final double p) {
		final int angle = (((int) -(p * 360)) / 10) % 36;

		final String imageName = "PIE-" + minorColor.getRGB() + "-" + majorColor.getRGB() + "-" + angle;
		final Image cache = Activator.getDefault().getImageRegistry().get(imageName);
		if (cache != null) {
			return cache;
		}
		final Image image = new Image(Display.getDefault(), 16, 16);

		final GC gc = new GC(image);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);

		final Color maskColor = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

		gc.setBackground(maskColor);
		gc.setForeground(maskColor);

		gc.fillRectangle(0, 0, 16, 16);

		gc.setAlpha(255);

		gc.setBackground(majorColor);
		gc.fillArc(1, 1, 15, 15, 0, 360);

		gc.setBackground(minorColor);
		gc.fillArc(1, 1, 15, 15, 90, angle * 10);

		// clear middle of donut
		gc.setBackground(maskColor);
		gc.fillArc(5, 5, 7, 7, 0, 360);

		final ImageData data = image.getImageData();
		data.alphaData = new byte[16 * 16];
		Arrays.fill(data.alphaData, (byte) 0);

		final PaletteData palette = data.palette;
		final double dmax = Math.max(colourDistance(maskColor.getRGB(), minorColor.getRGB()), colourDistance(maskColor.getRGB(), majorColor.getRGB()));

		for (int x = 0; x < data.width; x++) {
			for (int y = 0; y < data.height; y++) {
				final RGB rgb = palette.getRGB(data.getPixel(x, y));
				if (rgb.equals(maskColor.getRGB())) {
					data.setAlpha(x, y, 0);
				} else {
					double minorDistance = colourDistance(rgb, minorColor.getRGB());
					double majorDistance = colourDistance(rgb, majorColor.getRGB());

					data.setAlpha(x, y, (int) (255 * (1 - (Math.min(minorDistance, majorDistance) / dmax))));
				}
			}
		}

		gc.dispose();

		image.dispose();
		
		final Image result = new Image(Display.getDefault(), data);
		Activator.getDefault().getImageRegistry().put(imageName, result);
		return result;
	}

	private static final double colourDistance(final RGB one, final RGB two) {
		final int dr = one.red - two.red;
		final int dg = one.green - two.green;
		final int db = one.blue - two.blue;
		return Math.sqrt(dr * dr + dg * dg + db * db);
	}
}
