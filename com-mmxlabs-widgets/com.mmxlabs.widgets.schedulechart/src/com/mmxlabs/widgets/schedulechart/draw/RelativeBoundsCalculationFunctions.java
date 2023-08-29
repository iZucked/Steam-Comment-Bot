package com.mmxlabs.widgets.schedulechart.draw;

import java.util.function.Function;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class RelativeBoundsCalculationFunctions {
	
	public static final Point TOP_LEFT = new Point(0, 0);
	public static final Point TOP_CENTER = new Point(50, 0);
	public static final Point TOP_RIGHT = new Point(100, 0);
	public static final Point MIDDLE_LEFT = new Point(0, 50);
	public static final Point MIDDLE_CENTER = new Point(50, 50);
	public static final Point MIDDLE_RIGHT = new Point(100, 50);
	public static final Point BOTTOM_LEFT = new Point(0, 100);
	public static final Point BOTTOM_CENTER = new Point(50, 100);
	public static final Point BOTTOM_RIGHT = new Point(100, 100);
	
	public static final int MIN_SCALE = 0;
	public static final int MAX_SCALE = 100;
	
	private RelativeBoundsCalculationFunctions() {}

	private static Function<DrawableElement, Rectangle> calc(Point relParentAnchor, Point relChildAnchor, int widthScale, int heightScale) {
		return de -> {
			final Rectangle parentBounds = de.getBounds();
			final var parentAnchor = new Point(parentBounds.x + parentBounds.width * relParentAnchor.x, parentBounds.y + parentBounds.height * relParentAnchor.y);
			int width = parentBounds.width * widthScale;
			int height = parentBounds.height * heightScale;
			return new Rectangle(parentAnchor.x - width, parentAnchor.y - height, width, height);
		};
	}
	
	public static Function<DrawableElement, Rectangle> pinToTopLeft(int widthScale, int heightScale) {
		return calc(TOP_LEFT, TOP_LEFT, widthScale, heightScale);
	}

	public static Function<DrawableElement, Rectangle> pinToTopLeft(int widthScale) {
		return calc(TOP_LEFT, TOP_LEFT, widthScale, MAX_SCALE);
	}
	
	public static Function<DrawableElement, Rectangle> pinToTopRight(int widthScale, int heightScale) {
		return calc(TOP_RIGHT, TOP_RIGHT, widthScale, heightScale);
	}
	
	public static Function<DrawableElement, Rectangle> pinToTopRight(int widthScale) {
		return calc(TOP_RIGHT, TOP_RIGHT, widthScale, MAX_SCALE);
	}
}
