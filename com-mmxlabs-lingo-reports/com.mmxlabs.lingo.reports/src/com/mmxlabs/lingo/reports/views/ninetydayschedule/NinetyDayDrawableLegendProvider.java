/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableLegendProvider;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.providers.ILegendItem;

public class NinetyDayDrawableLegendProvider implements IDrawableLegendProvider {
	private final List<ILegendItem> legendItems;
	private DrawableElement cachedLegend = null;
	
	private int eventHeight = 10;
	private int eventWidth = 10;
	private static final int EVENT_PADDING = 1;
	private static final int TEXT_PADDING = 40;
	private static final int LINE_SPACING = 20;
	private static final int X_POS = 10;
	private static final int Y_POS = 10;

	
	public NinetyDayDrawableLegendProvider(List<ILegendItem> legendItems) {
		this.legendItems = legendItems;
		this.cachedLegend = getUpdatedLegend();
	}
	
	public DrawableElement getLegend() {
		if(cachedLegend == null)
			updateLegend();
		return cachedLegend;
	}
	
	public void updateLegend() {
		cachedLegend = getUpdatedLegend();
	}
	
	public void invalidateCache() {
		cachedLegend = null;
	}
	
	private DrawableElement getUpdatedLegend() {
		return new DrawableElement() {
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver resolver) {
				List<BasicDrawableElement> res = new ArrayList<>();
				
				
				// Background
				int shadowOffset = 2;
				Color startColor = new Color(new RGB(255,255,255));
				Color endColor = new Color(new RGB(219, 236, 255));
				res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x + shadowOffset, bounds.y + shadowOffset, bounds.width, bounds.height).bgColour(getBackgroundColour()).border(getBackgroundShadowColour(), 2).alpha(150).create());
				res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, bounds.height).bgColour(getBackgroundColour()).border(getStrokeColour(), 2).create());
							
				
				// Add events to legend
				int x;
				int y = Y_POS;
				for(int i = 0; i < legendItems.size(); i++) {
					x = X_POS;
					ILegendItem item = legendItems.get(i);
					List<DrawableScheduleEvent> drawableEvents = item.getDrawableEvents();
					int numEvents = drawableEvents.size();
					eventWidth = item.getItemWidth();
					eventHeight = item.getItemHeight();
					
					// Background of event
					if(item.showBackground()) {
						final int width = numEvents == 1 ? eventWidth + EVENT_PADDING + 1 : numEvents * eventWidth + (numEvents + 1) * EVENT_PADDING + 2;
						final int height = eventHeight + 2 * EVENT_PADDING + 1;
						res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x + x, bounds.y + y, width, height).bgColour(Display.getDefault().getSystemColor(SWT.COLOR_BLACK)).create());
					}

					// Draw event
					for(int j = 0; j < numEvents; j++) {
						DrawableScheduleEvent event = drawableEvents.get(j);
						event.setBounds(new Rectangle(bounds.x + x + EVENT_PADDING, bounds.y + y + EVENT_PADDING, eventWidth, eventHeight));
						res.addAll(event.getBasicDrawableElements(resolver));
						x += eventWidth + EVENT_PADDING + (j + 1 % 2);
					}
					
					res.add(BasicDrawableElements.Text.from(bounds.x + TEXT_PADDING, bounds.y + y, legendItems.get(i).getDescription()).textColour(getTextColour()).create());
					y += LINE_SPACING;
				}
				
				
				return res;
			}
		};
	}
	
	private Color getBackgroundColour() {
		return new Color(new RGB(255, 255, 255));
	}
	
	private Color getStrokeColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}
	
	private Color getBackgroundShadowColour() {
		//return new Color(new RGB(64, 64, 64));
		return new Color(new RGB(0, 0, 0));
	}
	
	private Color getTextColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

}
