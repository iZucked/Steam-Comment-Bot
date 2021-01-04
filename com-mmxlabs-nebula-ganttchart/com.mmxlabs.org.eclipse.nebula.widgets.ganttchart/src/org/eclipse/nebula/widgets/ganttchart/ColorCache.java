/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public final class ColorCache {

	public static final RGB BLACK = new RGB(0, 0, 0);
	public static final RGB WHITE = new RGB(255, 255, 255);

	private static Map<RGB, Color> cache;
	private static ColorCache instance;
	private static final Random rand = new Random();

	/**
	 * Disposes all colors held in the cache and colors created when class is created.
	 * <p>
	 * <b>IMPORTANT: ONLY CALL WHEN YOU WANT TO DISPOSE THE WIDGET USING THIS CLASS!</b>
	 * <p>
	 * If you only wish to dispose colors you have created through the use of the class, please use disposeCachedColors()
	 * 
	 * @see #disposeCachedColor()
	 */
	public static void disposeAll() {
		instance.dispose();
	}

	/**
	 * Disposes the cached colors only.
	 */
	public static void disposeCachedColor() {
		final Iterator<Color> iterator = cache.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().dispose();
		}

		cache.clear();
	}

	private ColorCache() {
		if (cache == null) {
			cache = new HashMap<>();
		}
	}

	private static void checkInstance() {
		if (instance == null) {
			instance = new ColorCache();
		}
	}

	// see disposeAll();
	private void dispose() {
		checkInstance();

		final Iterator<Color> iterator = cache.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().dispose();
		}

		cache.clear();
	}

	/**
	 * Returns the color white R255, G255, B255
	 * 
	 * @return White color
	 */
	public static Color getWhite() {
		checkInstance();
		return getColor(WHITE);
	}

	/**
	 * Returns the color black R0, G0, B0
	 * 
	 * @return Black color
	 */
	public static Color getBlack() {
		checkInstance();
		return getColor(BLACK);
	}

	/**
	 * Returns a color that is also cached if it has not been created before.
	 * 
	 * @param rgb
	 *                RGB colors
	 * @return Color
	 */
	public static Color getColor(final RGB rgb) {
		checkInstance();
		Color color = cache.get(rgb);

		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			cache.put(rgb, color);
		}

		return color;
	}

	/**
	 * Returns a color that is also cached if it has not been created before.
	 * 
	 * @param red
	 *                  Red
	 * @param green
	 *                  Green
	 * @param blue
	 *                  Blue
	 * @return Color
	 */
	public static Color getColor(final int red, final int green, final int blue) {
		checkInstance();
		return getColor(new RGB(red, green, blue));
	}

	/**
	 * Returns a random color.
	 * 
	 * @return random color
	 */
	public static Color getRandomColor() {
		checkInstance();
		final int red = rand.nextInt(255);
		final int green = rand.nextInt(255);
		final int blue = rand.nextInt(255);

		return getColor(red, green, blue);
	}
}
