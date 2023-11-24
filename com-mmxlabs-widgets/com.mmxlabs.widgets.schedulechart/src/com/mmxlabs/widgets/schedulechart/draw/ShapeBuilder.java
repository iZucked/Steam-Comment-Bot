/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Polygon;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Polyline;

public class ShapeBuilder {
	private final List<Point> points;
	private Color bgColour = null;
	private Color borderColour = null;
	private int borderThickness = 0;
	private int alpha = BasicDrawableElements.MAX_ALPHA;

	public ShapeBuilder() {
		this.points = new ArrayList<>();
	}
	
	public ShapeBuilder copy() {
		ShapeBuilder result = new ShapeBuilder(points);
		result.alpha = alpha;
		result.borderThickness = borderThickness;
		result.bgColour = bgColour;
		return result;
	}
	
	public ShapeBuilder(List<Point> points) {
		this.points = new ArrayList<>(points);
	}

	public ShapeBuilder addVertex(Point p) {
		points.add(p);
		return this;
	}

	public ShapeBuilder addVertex(int x, int y) {
		points.add(new Point(x, y));
		return this;
	}

	public ShapeBuilder withBorderColour(Color c) {
		this.borderColour = c;
		return this;
	}

	public ShapeBuilder withBorder(Color c, int thickness) {
		this.borderColour = c;
		this.borderThickness = thickness;
		return this;
	}

	public ShapeBuilder withBackgroundColour(Color c) {
		this.bgColour = c;
		return this;
	}

	public ShapeBuilder withAlpha(int a) {
		this.alpha = a;
		return this;
	}

	public Polygon createPolygon() {
		return new Polygon(List.copyOf(points), bgColour, borderColour, borderThickness, alpha);
	}
	
	public Polyline createPolyline() {
		return new Polyline(List.copyOf(points), bgColour != null ? bgColour : borderColour, borderThickness);
	}
}
