package com.mmxlabs.widgets.schedulechart.draw;

import java.util.function.Function;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.draw.RelativeDrawableElement.RelativeBounds;

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
	
	public static final RelativeBounds CENTER_100P = new RelativeBounds(MIDDLE_CENTER, MIDDLE_CENTER, MAX_SCALE, MAX_SCALE);
	
	private RelativeBoundsCalculationFunctions() {}

	private static Function<DrawableElement, Rectangle> calc(Point relParentAnchor, Point relChildAnchor, int widthScale, int heightScale) {
		return de -> {
			final Rectangle parentBounds = de.getBounds();
			final var parentAnchor = new Point(parentBounds.x + (parentBounds.width * relParentAnchor.x / MAX_SCALE), parentBounds.y + (parentBounds.height * relParentAnchor.y / MAX_SCALE));
			int width = parentBounds.width * widthScale / MAX_SCALE;
			int height = parentBounds.height * heightScale / MAX_SCALE;
			return new Rectangle(parentAnchor.x - (width * relChildAnchor.x / MAX_SCALE), parentAnchor.y - (height * relChildAnchor.y / MAX_SCALE), width, height);
		};
	}
	
	public static RelativeBounds pinToTopLeft(int widthScale, int heightScale) {
		return new RelativeBounds(TOP_LEFT, TOP_LEFT, widthScale, heightScale);
	}

	public static RelativeBounds pinToTopLeft(int widthScale) {
		return new RelativeBounds(TOP_LEFT, TOP_LEFT, widthScale, MAX_SCALE);
	}
	
	public static RelativeBounds pinToTopRight(int widthScale, int heightScale) {
		return new RelativeBounds(TOP_RIGHT, TOP_RIGHT, widthScale, heightScale);
	}
	
	public static RelativeBounds pinToTopRight(int widthScale) {
		return new RelativeBounds(TOP_RIGHT, TOP_RIGHT, widthScale, MAX_SCALE);
	}

	public static Function<DrawableElement, Rectangle> getBoundsFromRelative(RelativeBounds relBounds) {
		return calc(relBounds.relParentAnchor(), relBounds.relChildAnchor(), relBounds.widthScale(), relBounds.heightScale());
	}
}
