package com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public class NinetyDaySlotWindowAnnotation extends DrawableScheduleEventAnnotation {
	
	private final DrawableScheduleEvent dse;

	public NinetyDaySlotWindowAnnotation(ScheduleEventAnnotation annotation, DrawableScheduleEvent dse, IScheduleChartSettings settings, ToIntFunction<LocalDateTime> f) {
		super(annotation, dse, settings, f);
		this.dse = dse;
	}

	@Override
	protected Rectangle calculateAnnotationBounds(Rectangle scheduleEventBounds) {
		Arrays.sort(dateXCoords);
		int x = dateXCoords[0];
		int width = dateXCoords[1] - x;
		
		int spacer = settings.getSpacerWidth();
		int y = scheduleEventBounds.y - spacer - settings.getTopAnnotationHeight();
		int height = getHeight(scheduleEventBounds);
		return new Rectangle(x, y, width, height);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		if (!annotation.isVisible()) {
			return Collections.emptyList();
		}

		Color bg = getBgColour();
		Color border = getBorderColour();
		return List.of(BasicDrawableElements.Rectangle.withBounds(bounds).border(border, getBorderThickness()).bgColour(bg).alpha(dse.getAlpha()).create());
	}
	
	protected int getHeight(Rectangle scheduleEventBounds) {
		return settings.getTopAnnotationHeight();
	}
	
	protected Color getBorderColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW);
	}

	protected Color getBgColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Load, ColourElements.Background);
	}
	
	protected int getBorderThickness() {
		return (dse.getScheduleEvent().getSelectionState() == ScheduleEventSelectionState.SELECTED) ? 1 : 0;
	}
}
