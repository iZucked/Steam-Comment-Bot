/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;

public final class ColourPalette {

	//
	//
	private static final RGB Alert_Crimson = new RGB(255, 0, 0);
	private static final RGB FOBDES_Grey = new RGB(96, 96, 96);
	private static final RGB Gas_Blue = new RGB(50, 60, 225);
	private static final RGB Green = new RGB(0, 180, 50);
	// static final RGB Light_Gas_Blue = new RGB(150,255,255);
	private static final RGB Light_Gas_Blue = new RGB(150, 200, 255);
	private static final RGB Light_Green = new RGB(100, 255, 100);
	private static final RGB Locked_White = new RGB(255, 255, 255);
	private static final RGB Slot_White = new RGB(255, 255, 255);
	private static final RGB Teal = new RGB(0, 120, 120);
	private static final RGB VesselEvent_Purple = new RGB(120, 0, 120);
	private static final RGB VesselEvent_Brown = new RGB(150, 150, 80);
	private static final RGB VesselEvent_LightPurple = new RGB(150, 0, 200);

	private static final RGB Vessel_Ballast_Idle = Light_Gas_Blue;
	private static final RGB Vessel_Ballast_Journey = Gas_Blue;
	private static final RGB Vessel_Charter_Out = VesselEvent_Purple;
	private static final RGB Vessel_Dry_Dock = VesselEvent_Brown;
	private static final RGB Vessel_Generated_Charter_Out = VesselEvent_LightPurple;
	private static final RGB Vessel_Laden_Idle = Light_Green;
	private static final RGB Vessel_Laden_Journey = Green;
	private static final RGB Warning_Orange = new RGB(255, 120, 25);
	private static final RGB Warning_Yellow = new RGB(255, 255, 25);

	// experimental/currently unused
	// static final RGB VesselEvent_Brown = new RGB(120, 125, 60);
	// static final RGB VesselEvent_Brown = new RGB(77, 88, 50);
	private static final RGB VesselEvent_Green = new RGB(0, 225, 150);
	private static final RGB VesselEvent_Green2 = new RGB(80, 180, 50);
	private static final RGB VesselEvent_Green3 = new RGB(50, 200, 80);
	//

	public static RGB Black = new RGB(0, 0, 0);
	private static RGB Grey = new RGB(168, 168, 168);
	private static RGB Header_Grey = new RGB(228, 228, 228);
	private static RGB Light_Grey = new RGB(240, 240, 240);
	private static RGB Light_Orange = new RGB(255, 197, 168);
	private static RGB Orange = new RGB(255, 168, 64);

	private static ColourPalette instance;
	private static Map<String, ColourPalette> namedInstances = new HashMap<>();

	public static ColourPalette getInstance() {
		if (instance == null) {
			synchronized (ColourPalette.class) {
				if (instance == null) {
					instance = new ColourPalette();
				}
			}
		}
		return instance;
	}

	public static ColourPalette getNamedInstance(final String name) {
		ColourPalette cp = namedInstances.get(name);
		if (cp == null) {
			synchronized (ColourPalette.class) {
				cp = namedInstances.get(name);
				if (cp == null) {
					cp = new ColourPalette();
					namedInstances.put(name, cp);
				}
			}
		}
		return cp;
	}

	private ColourPalette() {
		initDefaultColours();
	}

	private void initDefaultColours() {

		setItemColour(ColourPaletteItems.Voyage_Laden_Journey, ColourElements.Background, Vessel_Laden_Journey);
		setItemColour(ColourPaletteItems.Voyage_Laden_Idle, ColourElements.Background, Vessel_Laden_Idle);
		setItemColour(ColourPaletteItems.Voyage_Ballast_Journey, ColourElements.Background, Vessel_Ballast_Journey);
		setItemColour(ColourPaletteItems.Voyage_Ballast_Idle, ColourElements.Background, Vessel_Ballast_Idle);

		setItemColour(ColourPaletteItems.Voyage_GeneratedCharterOut, ColourElements.Background, Vessel_Generated_Charter_Out);

		setItemColour(ColourPaletteItems.Voyage_Discharge, ColourElements.Background, Slot_White);
		setItemColour(ColourPaletteItems.Voyage_Load, ColourElements.Background, Slot_White);
		setItemColour(ColourPaletteItems.Voyage_Start, ColourElements.Background, Slot_White);
		setItemColour(ColourPaletteItems.Voyage_End, ColourElements.Background, Slot_White);
		setItemColour(ColourPaletteItems.Voyage_Cooldown, ColourElements.Background, Slot_White);

		setItemColour(ColourPaletteItems.Event_CharterOut, ColourElements.Background, Vessel_Charter_Out);
		// setColour(ColourPaletteItems.Event_Maintenence, ColourElements.Background, Vessel_Maintenance);
		setItemColour(ColourPaletteItems.Event_DryDock, ColourElements.Background, Vessel_Dry_Dock);

		setItemColour(ColourPaletteItems.Event_Locked, ColourElements.Border, Locked_White);
		setItemColour(ColourPaletteItems.FOB_Sale, ColourElements.Border, FOBDES_Grey);
		setItemColour(ColourPaletteItems.DES_Purchase, ColourElements.Border, FOBDES_Grey);

		setItemColour(ColourPaletteItems.Late_Load, ColourElements.Background, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Discharge, ColourElements.Background, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Event, ColourElements.Background, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Idle, ColourElements.Background, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Journey, ColourElements.Background, Alert_Crimson);

		setItemColour(ColourPaletteItems.Late_Load, ColourElements.Foreground, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Discharge, ColourElements.Foreground, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Event, ColourElements.Foreground, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Idle, ColourElements.Foreground, Alert_Crimson);
		setItemColour(ColourPaletteItems.Late_Journey, ColourElements.Foreground, Alert_Crimson);

		setItemColour(ColourPaletteItems.LONG, ColourElements.Background, Orange);
		setItemColour(ColourPaletteItems.SHORT, ColourElements.Background, Orange);
		
		setItemColour(ColourPaletteItems.Voyage_Tight_Warning, ColourElements.Background, Warning_Yellow);
	}

	public enum ColourElements {
		Background, Foreground, Border, Background_Alpha, Foreground_Alpha, Border_Alpha,
	}

	// @formatter:off
	public enum ColourPaletteItems {
		LONG, SHORT, OPTIONAL,
		FOB_Purchase, FOB_Sale, DES_Purchase, DES_Sale,
		Voyage_Start, Voyage_End,
		Voyage_Load, Voyage_Discharge,
		Voyage_Laden_Journey, Voyage_Laden_Idle,
		Voyage_Ballast_Journey, Voyage_Ballast_Idle,
		Voyage_Cooldown,
		Voyage_GeneratedCharterOut,
		Event_DryDock, Event_Maintenence, Event_CharterOut,
		
		Event_Locked,
		
		Late_Event, Late_Load, Late_Discharge, Late_Journey, Late_Idle,
		
		Voyage_Tight_Warning, 
	}
	// @formatter:on	
	private final Map<RGB, Color> colourMap = new HashMap<>();
	private final Map<Pair<ColourPaletteItems, ColourElements>, RGB> itemRGBMap = new HashMap<>();
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

	public void setItemColour(final ColourPaletteItems item, final ColourElements element, final RGB rgb) {
		final Pair<ColourPaletteItems, ColourElements> key = new Pair<>(item, element);
		itemRGBMap.put(key, rgb);
	}

	public void setItemColour(final ColourPaletteItems item, final ColourElements element, final int r, final int g, final int b) {
		setItemColour(item, element, new RGB(r, g, b));
	}

	public RGB getItemRGBFor(final ColourPaletteItems item, final ColourElements element) {
		final Pair<ColourPaletteItems, ColourElements> key = new Pair<>(item, element);
		if (itemRGBMap.containsKey(key)) {
			return itemRGBMap.get(key);
		}
		return null;
	}

	public Color getColourFor(final ColourPaletteItems item, final ColourElements element) {
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
