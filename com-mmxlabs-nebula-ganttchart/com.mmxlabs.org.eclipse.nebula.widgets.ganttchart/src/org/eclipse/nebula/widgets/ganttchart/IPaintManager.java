/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

import java.util.Collection;

import org.eclipse.nebula.widgets.ganttchart.label.IEventTextGenerator;
import org.eclipse.nebula.widgets.ganttchart.plaque.IPlaqueContentProvider;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public interface IPaintManager {

	/**
	 * Notifies a redraw is starting from scratch, so you can zero out variables etc
	 */
	void redrawStarting();

	/**
	 * Draws one checkpoint.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param dayWidth
	 *            Width of one day
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawCheckpoint(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, boolean threeDee, int dayWidth, int x, int y, Rectangle bounds);

	/**
	 * Draws one normal event.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param isSelected
	 *            Whether the event is selected or not
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param dayWidth
	 *            Width of one day
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param eventWidth
	 *            Width of event
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawEvent(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, boolean isSelected, boolean threeDee, int dayWidth, int x, int y,
			int eventWidth, Rectangle bounds);

	/**
	 * Draws the planned dates.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param threeDee
	 *            Whether 3D events is on or off.
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param eventWidth
	 *            Width of event
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawPlannedDates(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, boolean threeDee, int x, int y, int eventWidth, Rectangle bounds);

	/**
	 * Draws the little plaque showing how many number of days an event spans over.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param eventWidth
	 *            Width of event
	 * @param daysNumber
	 *            Number of days the event encompasses
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawDaysOnChart(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, boolean threeDee, int x, int y, int eventWidth, int daysNumber,
			Rectangle bounds);

	/**
	 * Draws a little plaque showing how many number of days an event spans over.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param event
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param eventWidth
	 *            Width of event
	 * @param plaqueContents
	 *            String to display
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawPlaqueOnEvent(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent event, GC gc, boolean threeDee, int x, int y, int eventWidth,
			IPlaqueContentProvider[] plaqueContentProviders, Rectangle bounds);

	/**
	 * Draws the little plaque showing how many number of days and hours an event spans over.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param eventWidth
	 *            Width of event
	 * @param daysNumber
	 *            Number of days the event encompasses
	 * @param hoursNumber
	 *            Remainder of hours the event encompasses
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawDaysAndHoursOnChart(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, boolean threeDee, int x, int y, int eventWidth, int daysNumber,
			int hoursNumber, Rectangle bounds);

	/**
	 * Draws a string shown next to an event.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param toDraw
	 *            String to draw
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param eventWidth
	 *            Width of event
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawEventString(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, String toDraw, boolean threeDee, int x, int y, int eventWidth,
			Rectangle bounds);

	void drawEventLabel(GanttComposite composite, ISettings settings, GanttEvent event, GC gc, Collection<Collection<IEventTextGenerator>> generatorsCollection, int x, int y, int eventWidth);

	/**
	 * Draws one scope.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param dayWidth
	 *            Width of one day
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param eventWidth
	 *            Width of event
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawScope(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, boolean threeDee, int dayWidth, int x, int y, int eventWidth, Rectangle bounds);

	/**
	 * Draws one checkpoint.
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param image
	 *            Image
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param dayWidth
	 *            Width of one day
	 * @param x
	 *            x location
	 * @param y
	 *            y location
	 * @param bounds
	 *            full bounds of draw area
	 */
	void drawImage(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, Image image, boolean threeDee, int dayWidth, int x, int y, Rectangle bounds);

	/**
	 * Draws the marker that shows what dates an event are locked down to
	 * 
	 * @param ganttComposite
	 *            GanttComposite parent
	 * @param settings
	 *            ISettings
	 * @param colorManager
	 *            IColorManager
	 * @param ge
	 *            GanttEvent
	 * @param gc
	 *            GC
	 * @param threeDee
	 *            Whether 3D events is on or off
	 * @param dayWidth
	 *            Width of one day
	 * @param y
	 *            y location
	 * @param xStart
	 *            where to draw the being marker. Will be -1 if there is no marker to draw.
	 * @param xEnd
	 *            where to draw the end marker. Will be -1 if there is no marker to draw.
	 * @param bounds
	 */
	void drawLockedDateRangeMarker(GanttComposite ganttComposite, ISettings settings, IColorManager colorManager, GanttEvent ge, GC gc, boolean threeDee, int dayWidth, int y, int xStart, int xEnd,
			Rectangle bounds);

	/**
	 * Draws an arrow head.
	 * 
	 * @param x
	 *            X location
	 * @param y
	 *            Y location
	 * @param face
	 *            What direction the arrows is in (one of SWT.LEFT, SWT.RIGHT, SWT.UP, SWT.DOWN)
	 * @param gc
	 *            GC
	 */
	void drawArrowHead(int x, int y, int face, GC gc, int size);
}
