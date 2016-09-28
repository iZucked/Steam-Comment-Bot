/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;

public final class TableColourPalette {

	//
	//
	private static final RGB FontColour = new RGB(0, 0, 0);
	private static final RGB HeaderColour = new RGB(230, 239, 249);
	private static final RGB BorderColour = new RGB(96, 96, 96);

	private static TableColourPalette instance;
	private static Map<String, TableColourPalette> namedInstances = new HashMap<>();

	public static TableColourPalette getInstance() {
		if (instance == null) {
			synchronized (TableColourPalette.class) {
				if (instance == null) {
					instance = new TableColourPalette();
				}
			}
		}
		return instance;
	}

	public static TableColourPalette getNamedInstance(final String name) {
		TableColourPalette cp = namedInstances.get(name);
		if (cp == null) {
			synchronized (TableColourPalette.class) {
				cp = namedInstances.get(name);
				if (cp == null) {
					cp = new TableColourPalette();
					namedInstances.put(name, cp);
				}
			}
		}
		return cp;
	}

	private TableColourPalette() {
		initDefaultColours();
	}

	private void initDefaultColours() {

		setItemColour(TableItems.LineBorders, ColourElements.Background, BorderColour);
		setItemColour(TableItems.ColumnHeaders, ColourElements.Background, HeaderColour);
		setItemColour(TableItems.ColumnGroupHeaders, ColourElements.Background, HeaderColour);
		setItemColour(TableItems.RowHeader, ColourElements.Background, HeaderColour);
		setItemColour(TableItems.TopLeftHeader, ColourElements.Background, HeaderColour);
		
		setItemColour(TableItems.ColumnGroupHeaders, ColourElements.Foreground, FontColour);
		setItemColour(TableItems.ColumnHeaders, ColourElements.Foreground, FontColour);
		setItemColour(TableItems.RowHeader, ColourElements.Foreground, FontColour);
		setItemColour(TableItems.TopLeftHeader, ColourElements.Foreground, FontColour);
	}

	public enum ColourElements {
		Background, Foreground
	}

	// @formatter:off
	public enum TableItems {
		 RowHeader,
		 TopLeftHeader ,
		 ColumnHeaders,
		 LineBorders, ColumnGroupHeaders
	}
	// @formatter:on	
	private final Map<RGB, Color> colourMap = new HashMap<>();
	private final Map<Pair<TableItems, ColourElements>, RGB> itemRGBMap = new HashMap<>();
	private final Map<Pair<String, ColourElements>, RGB> nameRGBMap = new HashMap<>();

	public void dispose() {
		final Iterator<Entry<RGB, Color>> itr = colourMap.entrySet().iterator();
		while (itr.hasNext()) {
			final Color c = itr.next().getValue();
			if (c != null) {
				c.dispose();
			}
			itr.remove();
		}
	}

	public Color getColour(final RGB rgb) {
		if (colourMap.containsKey(rgb)) {
			return colourMap.get(rgb);
		} else {
			final Color result = new Color(Display.getCurrent(), rgb);
			colourMap.put(rgb, result);
			return result;
		}
	}

	public void setItemColour(final TableItems item, final ColourElements element, final RGB rgb) {
		final Pair<TableItems, ColourElements> key = new Pair<>(item, element);
		itemRGBMap.put(key, rgb);
	}

	public void setItemColour(final TableItems item, final ColourElements element, final int r, final int g, final int b) {
		setItemColour(item, element, new RGB(r, g, b));
	}

	public RGB getItemRGBFor(final TableItems item, final ColourElements element) {
		final Pair<TableItems, ColourElements> key = new Pair<>(item, element);
		if (itemRGBMap.containsKey(key)) {
			return itemRGBMap.get(key);
		}
		return null;
	}

	public Color getColourFor(final TableItems item, final ColourElements element) {
		final RGB rgb = getItemRGBFor(item, element);
		if (rgb != null) {
			return getColour(rgb);
		}
		return null;
	}

	public void registerNamedColour(final String name, final ColourElements element, final RGB rgb) {
		final Pair<String, ColourElements> key = new Pair<>(name, element);
		nameRGBMap.put(key, rgb);
	}

	public void registerNamedColour(final String name, final ColourElements element, final int r, final int g, final int b) {
		registerNamedColour(name, element, new RGB(r, g, b));
	}

	public RGB getNamedRGBFor(final String name, final ColourElements element) {
		final Pair<String, ColourElements> key = new Pair<>(name, element);
		if (nameRGBMap.containsKey(key)) {
			return nameRGBMap.get(key);
		}
		return null;
	}

	public Color getNamedColourFor(final String name, final ColourElements element) {
		final RGB rgb = getNamedRGBFor(name, element);
		if (rgb != null) {
			return getColour(rgb);
		}
		return null;
	}

}
