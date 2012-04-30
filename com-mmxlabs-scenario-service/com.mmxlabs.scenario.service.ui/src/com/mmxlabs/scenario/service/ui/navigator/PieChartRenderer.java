/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.scenario.service.ui.internal.Activator;

/**
 * Draws pie chart icons for the navigator progress indicator.
 * 
 * @author hinton
 *
 */
public class PieChartRenderer {
	
	public static Image renderPie(final Color pieColor, final double p) {
		final int angle = (int) - (p * 360);
		final String imageName = "PIE-" + pieColor.getRGB() + "-" +angle;
		final Image cache = Activator.getDefault().getImageRegistry().get(imageName);
		if (cache != null) return cache;
		final Image image = new Image(Display.getDefault(), 16, 16);
		
		final GC gc = new GC(image);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		
		final Color maskColor = new Color(Display.getDefault(), 
				new RGB(Math.max(0, pieColor.getRed() - 10),Math.max(pieColor.getGreen() - 10, 0),
						Math.max(pieColor.getBlue() - 10, 0)));
		
		gc.setBackground(maskColor);
		gc.setForeground(maskColor);
		
		gc.fillRectangle(0, 0, 16, 16);
		
		gc.setBackground(pieColor);
		gc.setAlpha(255);
		
		gc.fillArc(1, 1, 15, 15, -90, angle);
		
		gc.setBackground(maskColor);
		
		gc.fillArc(4, 4, 9, 9, -90, angle);
		
		final ImageData data = image.getImageData();
		data.alphaData = new byte[16*16];
		Arrays.fill(data.alphaData, (byte) 255);

		final PaletteData palette = data.palette;
		final double dmax = colourDistance(maskColor.getRGB(), pieColor.getRGB());
		for (int x = 0; x<data.width; x++) {
			for (int y = 0; y<data.height; y++) {
				data.setAlpha(x, y, 
						(int) (255 * colourDistance(palette.getRGB(data.getPixel(x, y)), maskColor.getRGB()) / dmax));
			}
		}
		
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
