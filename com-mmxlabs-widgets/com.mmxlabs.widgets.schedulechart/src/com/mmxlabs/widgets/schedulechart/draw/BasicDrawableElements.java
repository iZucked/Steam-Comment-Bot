/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public interface BasicDrawableElements {
	
	public static int MAX_ALPHA = 255;

	public record Line(int x1, int y1, int x2, int y2, Color c, int alpha, int thickness) implements BasicDrawableElement {
		public Line(int x1, int y1, int x2, int y2) { 
			this(x1, y1, x2, y2, null, MAX_ALPHA, 1);
		}

		@Override
		public Color backgroundColour() {
			return null;
		}

		@Override
		public Color borderColour() {
			return c;
		}

		@Override
		public int borderThickness() {
			return thickness;
		}
	}

	public record Rectangle(int x, int y, int width, int height, Color backgroundColour, Color borderColour, int borderThickness, boolean isBorderInner, int alpha) implements BasicDrawableElement {

		public static RectangleBuilder withBounds(int x, int y, int width, int height) {
			return new RectangleBuilder(x, y, width, height);
		}
		
		public static RectangleBuilder withBounds(org.eclipse.swt.graphics.Rectangle bounds) {
			return new RectangleBuilder(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		
		public static class RectangleBuilder {
			private final int x;
			private final int y;
			private final int width;
			private final int height;
			private Color bgColour = null;
			private Color borderColour = null;
			private int borderThickness = 0;
			private int alpha = MAX_ALPHA;
			private boolean isBorderInner = false;
			
			private RectangleBuilder(int x, int y, int width, int height) {
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
			}
			
			public RectangleBuilder borderColour(Color c) {
				this.borderColour = c;
				return this;
			}
			
			public RectangleBuilder border(Color c, int thickness) {
				this.borderColour = c;
				this.borderThickness = thickness;
				this.isBorderInner = false;
				return this;
			}

			public RectangleBuilder border(Color c, int thickness, boolean isBorderInner) {
				this.borderColour = c;
				this.borderThickness = thickness;
				this.isBorderInner = isBorderInner;
				return this;
			}

			public RectangleBuilder bgColour(Color c) {
				this.bgColour = c;
				return this;
			}
			
			public RectangleBuilder alpha(int a) {
				this.alpha = a;
				return this;
			}
			
			public Rectangle create() {
				return new Rectangle(x, y, width, height, bgColour, borderColour, borderThickness, isBorderInner, alpha);
			}
		}
	}
	
	public record Text(org.eclipse.swt.graphics.Rectangle boundingBox, String text, int horizontalAlignment, int verticalAlignment, Color textColour, Color backgroundColour, Font font, int fontSize, Padding p, int alpha) implements BasicDrawableElement {
		public Text(int x, int y, String text) {
			this(new org.eclipse.swt.graphics.Rectangle(x, y, Integer.MAX_VALUE, Integer.MAX_VALUE), text, SWT.LEFT, SWT.TOP, null, null, Display.getDefault().getSystemFont(), Display.getDefault().getSystemFont().getFontData()[0].getHeight(), new Padding(0, 0, 0, 0), MAX_ALPHA);
		}

		@Override
		public Color borderColour() {
			return textColour;
		}

		@Override
		public int borderThickness() {
			return 0;
		}
		
		public static TextBuilder from(int x, int y, String s) {
			return new TextBuilder(new org.eclipse.swt.graphics.Rectangle(x, y, Integer.MAX_VALUE, Integer.MAX_VALUE), s);
		}
		
		public static TextBuilder from(int x, int y, int width, int height, String s) {
			return new TextBuilder(new org.eclipse.swt.graphics.Rectangle(x, y, width, height), s);
		}

		
		public static class TextBuilder {
			private final org.eclipse.swt.graphics.Rectangle bb;
			private final String s;
			private Color tc = null;
			private Color bgc = null;
			private Padding p = new Padding(0, 0, 0, 0);
			private int alpha = MAX_ALPHA;
			private int horizontalAlignment = SWT.LEFT;
			private int verticalAlignment = SWT.TOP;
			private Font f = Display.getDefault().getSystemFont();
			private int fontSize = 9;
			
			private TextBuilder(org.eclipse.swt.graphics.Rectangle boundingBox, String s) {
				this.bb = boundingBox;
				this.s = s;
				
				if(f.getFontData().length != 0)
					fontSize = f.getFontData()[0].getHeight();
			}
			
			public TextBuilder padding(int p) {
				this.p = new Padding(p, p, p, p);
				return this;
			}
			
			public TextBuilder padding(Padding p) {
				this.p = p;
				return this;
			}
			
			public TextBuilder textColour(Color c) {
				this.tc = c;
				return this;
			}
			
			public TextBuilder bgColour(Color c) {
				this.bgc = c;
				return this;
			}

			public TextBuilder alpha(int alpha) {
				this.alpha = alpha;
				return this;
			}
			
			public TextBuilder horizontalAlignment(int alignment) {
				this.horizontalAlignment = alignment;
				return this;
			}
			
			public TextBuilder verticalAlignment(int alignment) {
				this.verticalAlignment = alignment;
				return this;
			}
			
			public TextBuilder font(Font f) {
				this.f = f;
				return this;
			}
			
			public TextBuilder fontSize(int fontSize) {
				this.fontSize = fontSize;
				return this;
			}

			public Text create() {
				return new Text(bb, s, horizontalAlignment, verticalAlignment, tc, bgc, f, fontSize, p, alpha);
			}
		}

	}

	public record Polygon(List<Point> points, Color backgroundColour, Color borderColour, int borderThickness, int alpha) implements BasicDrawableElement {
		
		public static PolygonBuilder fromTriangle(Point a, Point b, Point c) {
			return new PolygonBuilder(new ArrayList<>(List.of(a, b, c)));
		}
		
		public static PolygonBuilder fromTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
			return new PolygonBuilder(new ArrayList<>(List.of(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3))));
		}
		
		public static PolygonBuilder fromPoints(List<Point> points) {
			return new PolygonBuilder(points);
		}

		public static class PolygonBuilder {
			private final List<Point> points;
			private Color bgColour = null;
			private Color borderColour = null;
			private int borderThickness = 0;
			private int alpha = MAX_ALPHA;
			
			private PolygonBuilder(List<Point> points) {
				this.points = points;
			}
			
			public PolygonBuilder addVertice(Point p) {
				points.add(p);
				return this;
			}
			
			public PolygonBuilder addVertice(int x, int y) {
				points.add(new Point(x, y));
				return this;
			}
			
			public PolygonBuilder borderColour(Color c) {
				this.borderColour = c;
				return this;
			}
			
			public PolygonBuilder border(Color c, int thickness) {
				this.borderColour = c;
				this.borderThickness = thickness;
				return this;
			}

			public PolygonBuilder bgColour(Color c) {
				this.bgColour = c;
				return this;
			}
			
			public PolygonBuilder alpha(int a) {
				this.alpha = a;
				return this;
			}
			
			public Polygon create() {
				return new Polygon(points, bgColour, borderColour, borderThickness, alpha);
			}
		}
	}

	public record Image(org.eclipse.swt.graphics.Image img, int x, int y) implements BasicDrawableElement {

		@Override
		public Color backgroundColour() {
			return null;
		}

		@Override
		public Color borderColour() {
			return null;
		}

		@Override
		public int borderThickness() {
			return 0;
		}

		@Override
		public int alpha() {
			return MAX_ALPHA;
		}

	}

	public record Padding(int left, int right, int top, int bottom) {}

}
