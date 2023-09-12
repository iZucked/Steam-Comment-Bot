package com.mmxlabs.widgets.schedulechart.draw;

import java.time.LocalDateTime;
import java.util.function.ToIntFunction;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.EditableScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;

public abstract class DrawableScheduleEventAnnotation extends DrawableElement {
	
	protected final int[] dateXCoords;
	protected final IScheduleChartSettings settings;
	protected final ScheduleEventAnnotation annotation;
	protected final ScheduleEvent se;
	
	protected DrawableScheduleEventAnnotation(final ScheduleEventAnnotation annotation, DrawableScheduleEvent dse, IScheduleChartSettings settings, ToIntFunction<LocalDateTime> f) {
		this.settings = settings;
		this.dateXCoords = annotation.getDates().stream().mapToInt(f).toArray();
		this.annotation = annotation;
		this.se = dse.getScheduleEvent();
		setBounds(calculateAnnotationBounds(dse.getBounds()));
	}
	
	public ScheduleEvent getScheduleEvent() {
		return se;
	}
	
	public ScheduleEventAnnotation getAnnotation() {
		return annotation;
	}

	protected abstract Rectangle calculateAnnotationBounds(Rectangle scheduleEventBounds);

	public EditableScheduleEventAnnotation getEditableAnnotation() {
		return (annotation instanceof EditableScheduleEventAnnotation) ? (EditableScheduleEventAnnotation) annotation : null;
	}
}
