/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ScheduleChartColourUtils {
	
	private static final Color WHITE = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	private static final Color BLACK = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private static final Map<Color, Color> cache = new HashMap<>();
	
	public static Color calculateTextColourForBestContrast(Color color) {
		return cache.computeIfAbsent(color, c -> {
			final double contrastRatioWithWhite = calculateContrastRatio(WHITE, c);
			final double contrastRatioWithBlack = calculateContrastRatio(c, BLACK);
			return (contrastRatioWithWhite > contrastRatioWithBlack) ? WHITE : BLACK;
		});
	}

	private static double calculateContrastRatio(Color c1, Color c2) {
		final double l1 = calculateLuminance(c1);
		final double l2 = calculateLuminance(c2);
		return (l1 + 0.05) / (l2 + 0.05);
	}
	
	private static double calculateLuminance(Color c) {
        double RsRGB = c.getRed() / 255.0;
        double GsRGB = c.getGreen() / 255.0;
        double BsRGB = c.getBlue() / 255.0;

        double r = (RsRGB <= 0.03928) ? RsRGB / 12.92 : Math.pow((RsRGB + 0.055) / 1.055, 2.4);
        double g = (GsRGB <= 0.03928) ? GsRGB / 12.92 : Math.pow((GsRGB + 0.055) / 1.055, 2.4);
        double b = (BsRGB <= 0.03928) ? BsRGB / 12.92 : Math.pow((BsRGB + 0.055) / 1.055, 2.4);

        double l = 0.2126 * r + 0.7152 * g + 0.0722 * b;

        return l;
    }

	public static Color getHiddenElementsFilter(Color c) {
		int total = c.getRed() + c.getBlue() + c.getGreen();
		int gr = total / 3;
		gr = (gr / 2) + 255 / 2;
		return new Color(new RGB(gr, gr, gr));
	}

}
