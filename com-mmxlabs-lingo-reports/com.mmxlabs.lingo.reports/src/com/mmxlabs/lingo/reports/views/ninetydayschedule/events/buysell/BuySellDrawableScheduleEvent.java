package com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.NinetyDayDrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

/**
 * Drawable element that generate drawables for Buy/Sell tiny stripes on a chart,
 * fobs, deses and other types and colors are here.
 * @author AP
 *
 */
public class BuySellDrawableScheduleEvent extends NinetyDayDrawableScheduleEvent {

	private static final @NonNull Color JUST_WHITE = new Color(255, 255, 255);
	private static final int HALF_WIDTH = 1;
	private static final int FULL_WIDTH = 2 * HALF_WIDTH;

	/**
	 * Must be at least 1 for DES bounds to show up
	 */
	private static final int VERTICAL_PADDING = 4;

	private final boolean isBuy;
	private final boolean isMulti;
	private final PositionStateType positionStateType;
	private final PositionType positionType;

	public BuySellDrawableScheduleEvent( //
			ScheduleEvent se, //
			Rectangle bounds, //
			boolean noneSelected, //
			final boolean isBuy, //
			final PositionStateType positionStateType, //
			final PositionType positionType, //
			final boolean isMulti //
	) {
		super(se, properBounds(bounds, positionStateType, isBuy), noneSelected);
		this.isBuy = isBuy;
		this.positionStateType = positionStateType;
		this.positionType = positionType;
		this.isMulti = isMulti;
	}

	private static Rectangle properBounds(final Rectangle bounds, final PositionStateType positionStateType, final boolean isBuy) {
		final int actualPaddingTop;
		final int acutalPaddingBottom;
		if (positionStateType == PositionStateType.PAIRED) {
			if (isBuy) {
				actualPaddingTop = VERTICAL_PADDING;
				acutalPaddingBottom = 0;
			} else {
				actualPaddingTop = 0;
				acutalPaddingBottom = VERTICAL_PADDING;
			}
		} else {
			actualPaddingTop = VERTICAL_PADDING;
			acutalPaddingBottom = VERTICAL_PADDING;
		}
		return new Rectangle(bounds.x - HALF_WIDTH, bounds.y + actualPaddingTop, FULL_WIDTH, bounds.height - actualPaddingTop - acutalPaddingBottom);
	}

	public boolean isBuyRow() {
		return isBuy;
	}

	@Override
	public int getAlpha(final ScheduleEventSelectionState scheduleEventSelectionState) {
		return 255;
	}

	@Override
	public int getBorderThickness(final ScheduleEventSelectionState scheduleEventSelectionState) {
		return 1;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		final List<BasicDrawableElement> drawableElements = new LinkedList<>();

		if (!getScheduleEvent().isVisible()) {
			return drawableElements;
		}

		if (positionType == PositionType.FOB) {
			if (isMulti) {
				drawWhiteRectangle(drawableElements);
				drawTheSmallTopAndBottomFilledFOBRectangles(drawableElements);
				drawTheMiddleFOB(drawableElements);
			} else {
				drawBoundedFilledRectangle(drawableElements);
			}
		} else {
			drawBoundedEmptyRectangle(drawableElements);
			if (isMulti) /* or Mixed, which implies Multiness already */ {
				drawTheMiddleDES(drawableElements);
			}
			if (PositionType.MIXED == positionType) {
				drawTheSmallTopAndBottomFilledFOBRectangles(drawableElements);
			}
		}

		return drawableElements;
	}

	private void drawTheMiddleDES(List<BasicDrawableElement> drawableElements) {
		final int x = getBounds().x;
		final int y = getBounds().y + 6 * getBounds().height / 14;
		final int w = getBounds().width;
		final int h = getBounds().height / 7;
		drawSmallRectangle(drawableElements, x, y, w, h);
	}

	private void drawTheSmallTopAndBottomFilledFOBRectangles(List<BasicDrawableElement> drawableElements) {
		final int x = getBounds().x;
		final int w = getBounds().width;
		final int h = getBounds().height / 5;
		final int y1 = getBounds().y;
		final int y2 = getBounds().y + 4 * getBounds().height / 5;
		drawSmallRectangle(drawableElements, x, y2, w, h);
		drawSmallRectangle(drawableElements, x, y1, w, h);
	}

	private void drawTheMiddleFOB(List<BasicDrawableElement> drawableElements) {
		final int x = getBounds().x;
		final int y = getBounds().y + 4 * getBounds().height / 10;
		final int w = getBounds().width;
		final int h = getBounds().height / 5;
		drawSmallRectangle(drawableElements, x, y, w, h);
	}
	
	private void drawSmallRectangle(List<BasicDrawableElement> drawableElements, final int x, final int y, final int w, final int h) {
		drawableElements.add(BasicDrawableElements.Rectangle //
				.withBounds(new Rectangle(x, y, w, h)) //
				.bgColour(positionStateType.getColour()) //
				.border(positionStateType.getColour(), 0, getIsBorderInner()) //
				.alpha(getAlpha(getSelectionState())) //
				.create());
	}

	private void drawBoundedFilledRectangle(List<BasicDrawableElement> drawableElements) {
		drawBoundedRectangle(drawableElements, positionStateType.getColour());
	}

	private void drawBoundedEmptyRectangle(final List<BasicDrawableElement> drawableElements) {
		drawBoundedRectangle(drawableElements, JUST_WHITE);
	}

	private void drawBoundedRectangle(final List<BasicDrawableElement> drawableElements, final Color colour) {
		drawableElements.add(BasicDrawableElements.Rectangle //
				.withBounds(getBounds()) //
				.bgColour(colour) //
				.border(positionStateType.getColour(), getBorderThickness(getSelectionState()), getIsBorderInner()) //
				.alpha(getAlpha(getSelectionState())) //
				.create());
	}

	private void drawWhiteRectangle(final List<BasicDrawableElement> drawableElements) {
		drawableElements.add(BasicDrawableElements.Rectangle //
				.withBounds(getBounds()) //
				.bgColour(JUST_WHITE) //
				.border(JUST_WHITE, getBorderThickness(getSelectionState()), getIsBorderInner()) //
				.alpha(getAlpha(getSelectionState())) //
				.create());
	}

	@Override
	public Color getBackgroundColour() {
		return positionType != PositionType.FOB ? JUST_WHITE : positionStateType.getColour();
	}

	@Override
	protected Color getBorderColour() {
		return positionStateType.getColour();
	}
}
