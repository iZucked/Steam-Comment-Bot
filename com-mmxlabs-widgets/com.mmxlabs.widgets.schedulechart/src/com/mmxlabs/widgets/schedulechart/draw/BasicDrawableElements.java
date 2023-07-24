package com.mmxlabs.widgets.schedulechart.draw;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.graphics.Color;

public interface BasicDrawableElements {

	public record Line(int x1, int y1, int x2, int y2, Color c, int alpha) implements BasicDrawableElement {
		public Line(int x1, int y1, int x2, int y2) { 
			this(x1, y1, x2, y2, null, 100);
		}

		@Override
		public Color getBackgroundColour() {
			return null;
		}

		@Override
		public Color getStrokeColor() {
			return c;
		}
	}

	public record Rectangle(int x, int y, int width, int height, Color bgColour, Color strokeColour, int alpha) implements BasicDrawableElement {

		@Override
		public Color getBackgroundColour() {
			return bgColour;
		}

		@Override
		public Color getStrokeColor() {
			return strokeColour;
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
			private Color strokeColour = null;
			private int alpha = 100;
			
			private RectangleBuilder(int x, int y, int width, int height) {
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
			}
			
			public RectangleBuilder strokeColour(Color c) {
				this.strokeColour = c;
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
				return new Rectangle(x, y, width, height, bgColour, strokeColour, alpha);
			}
		}
	}
	
	public record Text(int x, int y, String text, Color c) implements BasicDrawableElement {
		public Text(int x, int y, String text) {
			this(x, y, text, null);
		}

		@Override
		public Color getBackgroundColour() {
			return null;
		}

		@Override
		public Color getStrokeColor() {
			return c;
		}

	}
}
