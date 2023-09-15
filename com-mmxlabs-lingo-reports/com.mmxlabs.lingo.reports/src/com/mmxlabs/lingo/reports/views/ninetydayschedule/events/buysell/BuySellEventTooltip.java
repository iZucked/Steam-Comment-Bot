package com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.AbstractTextContentNinetyDayEventTooltip;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

/**
 * Drawable tooltip that lists all buy/sell events that happen at the same time.
 * @author AP
 * 
 * Notes: Despite the fact that tooltip extends base tooltip classes,
 * It is different. abstract tooltip has header, body and footer, while
 * the buy sell tooltip draws everything in its body.
 */
public class BuySellEventTooltip extends AbstractTextContentNinetyDayEventTooltip {

	private final PositionsSequenceElement positionsSeqenceElement;
	
	private int cachedHeight = -1;

	public BuySellEventTooltip(final ScheduleEventTooltip tooltip, final PositionsSequenceElement positionsSequenceElement) {
		super(tooltip);
		this.positionsSeqenceElement = positionsSequenceElement;
	}
	
	@Override
	protected int getWidth(DrawerQueryResolver r) {
		return 200;
	}
	
	@Override
	protected int getHeaderHeight(final DrawerQueryResolver drawerQueryResolver) {
		return 0;
	}
	
	@Override
	protected int getBodyHeight(final DrawerQueryResolver resolver) {
		super.getBodyHeight(resolver);
		if (cachedHeight == -1) {
			final List<BasicDrawableElement> dontCareList = new ArrayList<>();
			//
			// Pretend drawing for the first time and get the actual height.
			cachedHeight = drawBuySellEventSubToolTipAndGetHeightExtent(dontCareList, 0, 0, positionsSeqenceElement);
		}
		return cachedHeight + TEXT_PADDING;
	}

	@Override
	protected DrawableElement getTooltipHeader() {
		return DrawableElement.drawableElementWithoutAnyDrawing();
	}

	@Override
	protected DrawableElement getTooltipBody() {
		return new DrawableElement() {
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(final Rectangle bounds, final DrawerQueryResolver drawerQueryResolver) {
				List<BasicDrawableElement> drawableElementsToReturn = new ArrayList<>();

				
				int y = bounds.y;
				
				drawBuySellEventSubToolTipAndGetHeightExtent(drawableElementsToReturn, bounds.x, y, positionsSeqenceElement);
				
				return drawableElementsToReturn;
			}
		};
	}
	
	/*
	 * Return the height of resultant drawable
	 */
	private int drawBuySellEventSubToolTipAndGetHeightExtent(final List<BasicDrawableElement> drawableElements, final int x, int y, final PositionsSequenceElement positionSequenceElementProvided) {
		
		final PositionsSequenceClassification classification = PositionsSeqenceElements.getClassification(positionSequenceElementProvided);
		final Object element = positionSequenceElementProvided.getElement();
		
		if (element instanceof final MultiEvent multiEvent) {
			for (final Object multiEventPartElement : multiEvent.getElements()) {
				y = drawBuySellEventSubToolTipAndGetHeightExtent(drawableElements, x, y, PositionsSequenceElement.of(multiEventPartElement, positionsSeqenceElement.isBuyRow(), positionsSeqenceElement.getPositionsSequence()));
				y = newVerticalLineBreak(y);
			}
			return y;
		}
		
		final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getYearMonthStringDisplay());

		final String suffix = classification.positionStateType() == PositionStateType.OPEN ? " (open)" : "";

		final String windowStart;
		final String windowEnd;
		final String portName;
		final String id;
		if (element instanceof final SlotVisit slotVisit) {
			portName = slotVisit.getPort().getName();
			id = slotVisit.getSlotAllocation().getSlot().getName();
			windowStart = dateTimeFormatter.format(slotVisit.getStart().toLocalDateTime());
			windowEnd = dateTimeFormatter.format(slotVisit.getEnd().toLocalDateTime());
		} else if (element instanceof final OpenSlotAllocation openSlotAllocation) {
			portName = openSlotAllocation.getSlot().getPort().getName();
			id = openSlotAllocation.getSlot().getName();
			windowStart = dateTimeFormatter.format(openSlotAllocation.getSlot().getWindowStart());
			windowEnd = dateTimeFormatter.format(openSlotAllocation.getSlot().getSchedulingTimeWindow().getEnd().toLocalDateTime());
		} else {
			throw new IllegalStateException();
		}
		
		final String title = "At " + portName + suffix;
		
		y += TEXT_PADDING;
		drawableElements.add(BasicDrawableElements.Text.from(x + TEXT_PADDING, y, title).textColour(getTextColour()).create());
		y = newVerticalLineBreak(y);
		drawableElements.add(BasicDrawableElements.Text.from(x + TEXT_PADDING, y, "ID: " + id).textColour(getTextColour()).create());
		y = newVerticalLineBreak(y);
		drawableElements.add(BasicDrawableElements.Text.from(x + TEXT_PADDING, y, "Window Start: " + windowStart).textColour(getTextColour()).create());
		y = newVerticalLineBreak(y);
		drawableElements.add(BasicDrawableElements.Text.from(x + TEXT_PADDING, y, "Window End: " + windowEnd).textColour(getTextColour()).create());
		y = newVerticalLineBreak(y);
		
		return y;
	}


	private int newVerticalLineBreak(int y) {
		y += getTextExtent();
		return y;
	}
}
