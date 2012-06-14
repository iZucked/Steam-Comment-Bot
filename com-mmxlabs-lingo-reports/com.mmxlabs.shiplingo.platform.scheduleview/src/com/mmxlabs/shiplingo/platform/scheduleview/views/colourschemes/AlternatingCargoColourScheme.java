package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

public class AlternatingCargoColourScheme implements IScheduleViewColourScheme {

	private GanttChartViewer viewer;

	private final Color baseColor;
	private final Color secondaryColor;

	private Color selectedColor;

	public AlternatingCargoColourScheme() {
		this(ColorCache.getColor(220, 20, 50), ColorCache.getColor(20, 155, 124));
	}

	public AlternatingCargoColourScheme(final Color baseColor, final Color second) {
		this.baseColor = baseColor;
		this.secondaryColor = second;
		selectedColor = baseColor;
	}

	@Override
	public String getName() {
		return "Alternating Cargoes";
	}

	@Override
	public GanttChartViewer getViewer() {
		return viewer;
	}

	@Override
	public void setViewer(final GanttChartViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof SlotVisit && ((SlotVisit) element).getSlotAllocation().getSlot() instanceof LoadSlot) {
			// flip colour
			if (selectedColor == baseColor) {
				selectedColor = secondaryColor;
			} else {
				selectedColor = baseColor;
			}
		}
		return selectedColor;
	}

	@Override
	public int getAlpha(final Object element) {
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {
		return null;
	}
}
