/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public interface BasicDrawableElements {
	
	public static int MAX_ALPHA = 255;

	public record Line(int x1, int y1, int x2, int y2, Color c, int alpha, int thickness) implements BasicDrawableElement {
		public Line(int x1, int y1, int x2, int y2) { 
			this(x1, y1, x2, y2, null, MAX_ALPHA, 1);
		}

		@Override
		public Color getBackgroundColour() {
			return null;
		}

		@Override
		public Color getBorderColour() {
			return c;
		}

		@Override
		public int getBorderThickness() {
			return thickness;
		}
		
		@Override
		public int getAlpha() {
			return alpha;
		}
	}

	public record Rectangle(int x, int y, int width, int height, Color bgColour, Color borderColour, int borderThickness, int alpha) implements BasicDrawableElement {

		@Override
		public Color getBackgroundColour() {
			return bgColour;
		}

		@Override
		public Color getBorderColour() {
			return borderColour;
		}

		@Override
		public int getBorderThickness() {
			return borderThickness;
		}
		
		@Override
		public int getAlpha() {
			return alpha;
		}
		
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
				return new Rectangle(x, y, width, height, bgColour, borderColour, borderThickness, alpha);
			}
		}
	}
	
	public record Text(org.eclipse.swt.graphics.Rectangle boundingBox, String text, int alignment, Color c, Font font, Padding p, int alpha) implements BasicDrawableElement {
		public Text(int x, int y, String text) {
			this(new org.eclipse.swt.graphics.Rectangle(x, y, Integer.MAX_VALUE, Integer.MAX_VALUE), text, SWT.LEFT, null, Display.getDefault().getSystemFont(), new Padding(0, 0, 0, 0), MAX_ALPHA);
		}

		@Override
		public Color getBackgroundColour() {
			return null;
		}

		@Override
		public Color getBorderColour() {
			return c;
		}

		@Override
		public int getBorderThickness() {
			return 0;
		}
		
		@Override
		public int getAlpha() {
			return alpha;
		}

		public static TextBuilder from(int x, int y, String s) {
			return new TextBuilder(new org.eclipse.swt.graphics.Rectangle(x, y, Integer.MAX_VALUE, Integer.MAX_VALUE), s);
		}
		
		public static class TextBuilder {
			private final org.eclipse.swt.graphics.Rectangle bb;
			private final String s;
			private Color c = null;
			private Padding p = new Padding(0, 0, 0, 0);
			private int alpha = MAX_ALPHA;
			private int alignment = SWT.LEFT;
			private Font f = Display.getDefault().getSystemFont();
			
			private TextBuilder(org.eclipse.swt.graphics.Rectangle boundingBox, String s) {
				this.bb = boundingBox;
				this.s = s;
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
				this.c = c;
				return this;
			}
			
			public TextBuilder alpha(int alpha) {
				this.alpha = alpha;
				return this;
			}
			
			public TextBuilder alignment(int alignment) {
				this.alignment = alignment;
				return this;
			}
			
			public TextBuilder font(Font f) {
				this.f = f;
				return this;
			}

			public Text create() {
				return new Text(bb, s, alignment, c, f, p, alpha);
			}
		}

	}

	public record Padding(int left, int right, int top, int bottom) {}

}
