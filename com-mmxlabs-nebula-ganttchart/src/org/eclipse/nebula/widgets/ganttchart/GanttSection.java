/*******************************************************************************
 * Copyright (c) Emil Crumhorn - Hexapixel.com - emil.crumhorn@gmail.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    emil.crumhorn@gmail.com - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.ganttchart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * A GanttSection is a "box" section of the chart. A section will automatically get a left-side border that shows the
 * name, and the background colors drawn for that section can differ from the rest of the chart. Here's an view of it: <br>
 * <br>
 * ................................................<br>
 * Header<br>
 * ................................................<br>
 * n<br>
 * a &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Section<br>
 * m<br>
 * e<br>
 * ................................................<br>
 * <br>
 */
public class GanttSection implements IFillBackgroundColors {

    private String                _name;
    private GanttComposite        _parent;
    private List                  _ganttEvents;
    // this list contains events that are being vertically DND'd across the section
    // and need to be rendered but should not count as actual member-events until (if) they are dropped
    private List                  _dndGanttEvents;
    private Rectangle             _bounds;
    private Image                 _nameImage;
    private boolean               _needsNameUpdate;
    private IFillBackgroundColors _fillColorManager;

    private Color                 _saturdayBgColorTop;
    private Color                 _saturdayBgColorBottom;
    private Color                 _sundayBgColorTop;
    private Color                 _sundayBgColorBottom;
    private Color                 _weekdayBgColorTop;
    private Color                 _weekdayBgColorBottom;
    private Color                 _selectedBgColorTop;
    private Color                 _selectedBgColorBottom;
    private Color                 _selectedBgHeaderColorTop;
    private Color                 _selectedBgHeaderColorBottom;

    // private items
    private Point                 _nameExtent;

    private int                   _textOrientation = SWT.VERTICAL;

    private boolean               _inheritBackgroud;

    private GanttSection() {
        this._ganttEvents = new ArrayList();
        this._dndGanttEvents = new ArrayList();
    }

    /**
     * Creates a new GanttSection.
     * 
     * @param parent GanttChart
     * @param name GanttSection name
     */
    public GanttSection(final GanttChart parent, final String name) {
        this();
        this._name = name;
        this._parent = parent.getGanttComposite();
        this._parent.addSection(this);
        this._fillColorManager = parent.getColorManager();
    }

    /**
     * Creates a new GanttSection with a fill manager that controls background colors.
     * 
     * @param parent GanttChart
     * @param name GanttSection name
     * @param fillManager Fill manager
     */
    public GanttSection(final GanttChart parent, final String name, final IFillBackgroundColors fillManager) {
        this();
        this._name = name;
        this._parent = parent.getGanttComposite();
        this._parent.addSection(this);
        this._fillColorManager = fillManager;
    }

    /**
     * Adds a Gantt Chart item (GanttSection, GanttGroup) to this section.
     * 
     * @param event Item to add
     */
    public void addGanttEvent(final IGanttChartItem event) {
        addGanttEvent(-1, event);
    }

    /**
     * Adds a Gantt Chart item at the given index.
     * 
     * @param index Index to add item at
     * @param event Item to add
     */
    public void addGanttEvent(final int index, final IGanttChartItem event) {
        int inx = index;
        
        if (!_ganttEvents.contains(event)) {
            if (inx == -1) {
                _ganttEvents.add(event);
            } else {
                if (inx > _ganttEvents.size()) {
                    inx = _ganttEvents.size();
                }
                _ganttEvents.add(inx, event);
            }
            if (event instanceof GanttEvent) {
                ((GanttEvent) event).setGanttSection(this);
            }
            
        }
    }

    /**
     * Removes a Gantt Chart item (GanttSection, GanttGroup) from this section.
     * 
     * @param event Item to remove
     */
    public void removeGanttEvent(final IGanttChartItem event) {
        _ganttEvents.remove(event);
    }

    /**
     * Returns a list of all IGanttChartItems (GanttEvent and GanttGroup) contained in this section.
     * 
     * @return List of items
     */
    public List getEvents() {
        return _ganttEvents;
    }

    /**
     * Sets the name of this section. This method does not force a redraw.
     * 
     * @param name GanttSection name
     */
    public void setName(final String name) {
        this._name = name;
        this._needsNameUpdate = true;
    }

    /**
     * Returns the name of this section.
     * 
     * @return GanttSection name
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the bounds of this GanttSection
     * 
     * @return Rectangle
     */
    public Rectangle getBounds() {
        return _bounds;
    }

    // note to self: this does not take into account the height the name will take up
    // this method can NOT use the bounds on the events as this method will be called prior to events being drawn and thus have no values for bounds
    /*
     * int _getEventsHeight(ISettings settings) { int height = 0;
     * 
     * if (ganttEvents.size() == 0) return settings.getMinimumSectionHeight();
     * 
     * height += settings.getEventsTopSpacer();
     * 
     * GanttGroup lastGroup = null; for (int i = 0; i < ganttEvents.size(); i++) { IGanttChartItem event = (IGanttChartItem) ganttEvents.get(i); if (event instanceof GanttEvent) {
     * if (lastGroup != null) height += settings.getEventSpacer();
     * 
     * GanttEvent ge = (GanttEvent) ganttEvents.get(i); if (!ge.isAutomaticRowHeight()) height += ge.getFixedRowHeight(); else height += settings.getEventHeight(); // skip last
     * event check, we need spacing there too height += settings.getEventSpacer(); lastGroup = null; } else if (event instanceof GanttGroup) { GanttGroup gg = (GanttGroup) event;
     * 
     * if (gg != lastGroup) { if (!gg.isAutomaticRowHeight()) { height += gg.getFixedRowHeight(); } else { height += settings.getEventHeight(); //height +=
     * settings.getEventHeight(); //height += settings.getEventSpacer()/2; }
     * 
     * if (i != ganttEvents.size()-1) height += settings.getEventSpacer(); }
     * 
     * lastGroup = gg; } }
     * 
     * if (height < settings.getMinimumSectionHeight()) height = settings.getMinimumSectionHeight(); // System.err.println(getName() + " " + height + " " + ganttEvents.size());
     * 
     * return height; }
     */

    public Color getSaturdayBackgroundColorBottom() {
        return _saturdayBgColorBottom == null ? _fillColorManager.getSaturdayBackgroundColorBottom() : _saturdayBgColorBottom;
    }

    public Color getSaturdayBackgroundColorTop() {
        return _saturdayBgColorTop == null ? _fillColorManager.getSaturdayBackgroundColorTop() : _saturdayBgColorTop;
    }

    public Color getSundayBackgroundColorBottom() {
        return _sundayBgColorBottom == null ? _fillColorManager.getSundayBackgroundColorBottom() : _sundayBgColorBottom;
    }

    public Color getSundayBackgroundColorTop() {
        return _sundayBgColorTop == null ? _fillColorManager.getSundayBackgroundColorTop() : _sundayBgColorTop;
    }

    public Color getWeekdayBackgroundColorBottom() {
        return _weekdayBgColorBottom == null ? _fillColorManager.getWeekdayBackgroundColorBottom() : _weekdayBgColorBottom;
    }

    public Color getWeekdayBackgroundColorTop() {
        return _weekdayBgColorTop == null ? _fillColorManager.getWeekdayBackgroundColorTop() : _weekdayBgColorTop;
    }

    public Color getSelectedDayColorBottom() {
        return _selectedBgColorBottom == null ? _fillColorManager.getSelectedDayColorBottom() : _selectedBgColorBottom;
    }

    public Color getSelectedDayColorTop() {
        return _selectedBgColorTop == null ? _fillColorManager.getSelectedDayColorTop() : _selectedBgColorTop;
    }

    public Color getSelectedDayHeaderColorBottom() {
        return _selectedBgHeaderColorBottom == null ? _fillColorManager.getSelectedDayHeaderColorBottom() : _selectedBgHeaderColorBottom;
    }

    public Color getSelectedDayHeaderColorTop() {
        return _selectedBgHeaderColorTop == null ? _fillColorManager.getSelectedDayHeaderColorTop() : _selectedBgHeaderColorTop;
    }

    public void setSaturdayBackgroundColorTop(final Color saturdayBackgroundColorTop) {
        _saturdayBgColorTop = saturdayBackgroundColorTop;
    }

    public void setSaturdayBackgroundColorBottom(final Color saturdayBackgroundColorBottom) {
        _saturdayBgColorBottom = saturdayBackgroundColorBottom;
    }

    public void setSundayBackgroundColorTop(final Color sundayBackgroundColorTop) {
        _sundayBgColorTop = sundayBackgroundColorTop;
    }

    public void setSundayBackgroundColorBottom(final Color sundayBackgroundColorBottom) {
        _sundayBgColorBottom = sundayBackgroundColorBottom;
    }

    public void setWeekdayBackgroundColorTop(final Color weekdayBackgroundColorTop) {
        _weekdayBgColorTop = weekdayBackgroundColorTop;
    }

    public void setWeekdayBackgroundColorBottom(final Color weekdayBackgroundColorBottom) {
        _weekdayBgColorBottom = weekdayBackgroundColorBottom;
    }

    public void setSelectedBackgroundColorTop(final Color selectedBackgroundColorTop) {
        _selectedBgColorTop = selectedBackgroundColorTop;
    }

    public void setSelectedBackgroundColorBottom(final Color selectedBackgroundColorBottom) {
        _selectedBgColorBottom = selectedBackgroundColorBottom;
    }

    public void setSelectedBackgroundHeaderColorTop(final Color selectedBackgroundHeaderColorTop) {
        _selectedBgHeaderColorTop = selectedBackgroundHeaderColorTop;
    }

    public void setSelectedBackgroundHeaderColorBottom(final Color selectedBackgroundHeaderColorBottom) {
        _selectedBgHeaderColorBottom = selectedBackgroundHeaderColorBottom;
    }

    /**
     * Returns the text orientation of the section. Default is SWT.VERTICAL.
     * 
     * @return Text orientation.
     */
    public int getTextOrientation() {
        return _textOrientation;
    }

    /**
     * Sets the text orientation of the section. One of SWT.HORIZONTAL or SWT.VERTICAL. Default is SWT.VERTICAL.
     * 
     * @param textOrientation SWT.VERTICAL or SWT.HORIZONTAL
     */
    public void setTextOrientation(final int textOrientation) {
        _textOrientation = textOrientation;
    }

    /**
     * Whether this section should just inherit the background colors of the main chart.
     * 
     * @return true if set
     * @deprecated IN PROGRESS
     */
    boolean isInheritBackgroud() {
        return _inheritBackgroud;
    }

    /**
     * Sets whether this section should inherit the background colors of the main chart for drawing date fills.
     * 
     * @param inheritBackgroud true to inherit. Default is false.
     * @deprecated IN PROGRESS
     */
    void setInheritBackgroud(final boolean inheritBackgroud) {
        _inheritBackgroud = inheritBackgroud;
    }

    /**
     * Removes this section from the chart. Do note that all belonging GanttEvents will be orphaned, so you should
     * probably deal with that post disposal.
     */
    public void dispose() {
        _parent.removeSection(this);
        _parent.redraw();
    }

    Point getNameExtent() {
        return _nameExtent;
    }

    void setNameExtent(final Point extent) {
        this._nameExtent = extent;
    }

    void setBounds(final Rectangle bounds) {
        this._bounds = bounds;
    }

    Image getNameImage() {
        return _nameImage;
    }

    void setNameImage(final Image nameImage) {
        this._nameImage = nameImage;
        this._needsNameUpdate = false;
    }

    boolean needsNameUpdate() {
        return _needsNameUpdate;
    }

    void setNeedsNameUpdate(final boolean need) {
        _needsNameUpdate = need;
    }

    int getEventsHeight(final ISettings settings) {
        if (_ganttEvents.size() == 0) {
            return settings.getMinimumSectionHeight();
        }

        int height = settings.getEventsTopSpacer();

        for (int i = 0; i < _ganttEvents.size(); i++) {
            final IGanttChartItem event = (IGanttChartItem) _ganttEvents.get(i);

            if (event.isAutomaticRowHeight()) {
                height += settings.getEventHeight();
            }
            else {
                height += event.getFixedRowHeight();                
            }

            if (i != _ganttEvents.size() - 1) {
                height += settings.getEventSpacer();
            }
        }

        height += settings.getEventsBottomSpacer();

        if (height < settings.getMinimumSectionHeight()) {
            height = settings.getMinimumSectionHeight();
        }

        return height;
    }

    void addDNDGanttEvent(final GanttEvent ge) {
        if (!_dndGanttEvents.contains(ge)) {
            _dndGanttEvents.add(ge);
        }
    }

    void clearDNDGanttEvents() {
        _dndGanttEvents.clear();
    }

    List getDNDGanttEvents() {
        return _dndGanttEvents;
    }

    public String toString() {
        return "[GanttSection: " + _name + "]";
    }
    
    
}
