/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Polygon;

public class DrawableDiffLine extends DrawableElement {
	final DrawableScheduleEvent from;
	final DrawableScheduleEvent to;
	final Color inlineColour; 
	final Color outlineColour;
	final Color arrowInlineColour;
	final Color arrowOutlineColour;
	private static final int ARROW_INNER_SIZE = 3;
	private static final int LINE_GAP = 15;
	private static final int LINE_WIDTH = 3;
	private static final int LINE_INNER_WIDTH = 1;
	private static final int LINE_BEND_RADIUS = 2;

	public DrawableDiffLine(DrawableScheduleEvent from, DrawableScheduleEvent to) {
		this.from = from;
		this.to = to;
		this.inlineColour = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		this.outlineColour = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
		arrowInlineColour = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		arrowOutlineColour = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> elements = new LinkedList<>();
		
		ShapeBuilder diffLine = new ShapeBuilder();
		
		
		// Draw lines 
		int x = from.getBounds().x;
		int y = from.getBounds().y + from.getBounds().height/2 + 5;
		diffLine.addVertex(x, y);
		int newX;
		if(to.getBounds().x - LINE_GAP < x) {
			newX = to.getBounds().x - LINE_GAP;
			diffLine.addVertex(x, y);
		} else {
			// Bend
			newX = x - LINE_GAP; 
			diffLine.addVertex(x, y);
		}
		
		int newY = to.getBounds().y + to.getBounds().height/2;
		
		newY-= 5;
		makeBend(diffLine, new Point(x, y), new Point(newX, newY), true);
		makeBend(diffLine, new Point(newX, y), new Point(to.getBounds().x ,newY), false);
		y = newY;
		x = to.getBounds().x;
		diffLine.addVertex(x, y);
		
		
		elements.add(diffLine.withBorder(outlineColour, LINE_WIDTH).createPolyline());
		elements.add(diffLine.withBorder(inlineColour, LINE_INNER_WIDTH).createPolyline());
		
		int endLineHeight = y;
		int arrowSize = 5;
		Polygon arrow = new ShapeBuilder(List.of(new Point(to.getBounds().x - arrowSize, endLineHeight - arrowSize),
				new Point(to.getBounds().x - arrowSize, endLineHeight + arrowSize), //
				new Point(to.getBounds().x, endLineHeight))) //
				.withBorder(arrowOutlineColour, 0) //
				.withBackgroundColour(arrowOutlineColour) //
				.createPolygon();
		Polygon arrowInline = new ShapeBuilder(List.of(new Point(to.getBounds().x - arrowSize, endLineHeight - ARROW_INNER_SIZE),
				new Point(to.getBounds().x - arrowSize, endLineHeight + ARROW_INNER_SIZE), //
				new Point(to.getBounds().x - arrowSize + ARROW_INNER_SIZE, endLineHeight))) //
				.withBorder(arrowInlineColour, 0)
				.withBackgroundColour(arrowInlineColour)
				.createPolygon();
		elements.add(arrow);
		elements.add(arrowInline);
		return elements;
	}
	
	private void makeBend(ShapeBuilder shapeBuilder, Point from, Point to, boolean horizontalFirst) {
		if(horizontalFirst) {
			shapeBuilder.addVertex(to.x > from.x ? to.x - LINE_BEND_RADIUS : to.x + LINE_BEND_RADIUS, from.y)//
						.addVertex(to.x, to.y > from.y ? from.y + LINE_BEND_RADIUS : from.y - LINE_BEND_RADIUS);
		} else {
			shapeBuilder.addVertex(from.x, to.y > from.y ? to.y - LINE_BEND_RADIUS : to.y + LINE_BEND_RADIUS)//
						.addVertex(to.x > from.x ? from.x + LINE_BEND_RADIUS : from.x - LINE_BEND_RADIUS, to.y);			
		}
	}
}
