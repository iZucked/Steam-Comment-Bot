/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.widgets.ganttchart.SpecialDrawModes;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.NamedObject;

public class SourcePortHighlighter extends ParameterisedColourScheme {

	private Port selectedObject = null;

	@Override
	public String getName() {
		return "By source port";
	}

	@Override
	public Color getBackground(Object element) {
		if (selectedObject != null) {
			final Port pSelectedObject = selectedObject;
			if (element instanceof SlotVisit slotVisit) {
				final Port visitPort = slotVisit.getPort();
				if (visitPort == pSelectedObject) {
					final Slot<?> slot = slotVisit.getSlotAllocation().getSlot();
					if (slot instanceof LoadSlot ls && !ls.isDESPurchase()) {
						return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
					}
				}
			} else if (element instanceof NonShippedSlotVisit slotVisit) {
				final Port visitPort = slotVisit.getPort();
				if (visitPort == pSelectedObject) {
					final Slot<?> slot = slotVisit.getSlot();
					if (slot instanceof LoadSlot ls && !ls.isDESPurchase()) {
						return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
					}
				}
			}
		}
		return null;
	}

	@Override
	public SpecialDrawModes getSpecialDrawMode(Object element) {
		return SpecialDrawModes.NONE;
	}

	@Override
	public List<NamedObject> getOptions(GanttChartViewer viewer) {
		final Object input = viewer.getInput();
		if (input instanceof Collection<?> collection) {
			if (collection.size() == 1) {
				final Object possibleSchedule = collection.iterator().next();
				if (possibleSchedule instanceof final @NonNull Schedule schedule) {
					return getOptions(schedule);
				}
			}
		} else if (input instanceof final @NonNull Schedule schedule) {
			return getOptions(schedule);
		}
		return null;
	}

	private List<NamedObject> getOptions(final @NonNull Schedule schedule) {
		return schedule.getSequences().stream().map(Sequence::getEvents).flatMap(List::stream) //
				.filter(SlotVisit.class::isInstance) //
				.map(SlotVisit.class::cast) //
				.filter(sv -> sv.getSlotAllocation().getSlot() instanceof LoadSlot ls && !ls.isDESPurchase()) //
				.map(SlotVisit::getPort) //
				.distinct() //
				.sorted((port1, port2) -> ScenarioElementNameHelper.getName(port1, "<Unknown>").compareTo(ScenarioElementNameHelper.getName(port2, "<Unknown>"))) //
				.map(NamedObject.class::cast) //
				.toList();
	}

	@Override
	public void selectOption(Object object) {
		selectedObject = object instanceof Port port ? port : null;
	}

	@Override
	public boolean getIsBorderInner(Object element) {
		return false;
	}

}
