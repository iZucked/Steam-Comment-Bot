/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.highlighters;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.NinetyDayScheduleInput;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.AbstractScheduleEventStylingProvider;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;

public class SourcePortHighlighter extends AbstractScheduleEventStylingProvider implements ParameterisedColourScheme {

	private Port selectedObject = null;

	public SourcePortHighlighter() {
		super("By source port");
	}

	@Override
	public @Nullable Color getBackgroundColour(final DrawableScheduleEvent event, @Nullable final Color defaultColor) {

		final Object element = event.getScheduleEvent().getData();

		if (selectedObject != null) {
			final Port pSelectedObject = selectedObject;
			if (element instanceof final SlotVisit slotVisit) {
				final Port visitPort = slotVisit.getPort();
				if (visitPort == pSelectedObject) {
					final Slot<?> slot = slotVisit.getSlotAllocation().getSlot();
					if (slot instanceof final LoadSlot ls && !ls.isDESPurchase()) {
						return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
					}
				}
			} else if (element instanceof final NonShippedSlotVisit slotVisit) {
				final Port visitPort = slotVisit.getPort();
				if (visitPort == pSelectedObject) {
					final Slot<?> slot = slotVisit.getSlot();
					if (slot instanceof final LoadSlot ls && !ls.isDESPurchase()) {
						return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
					}
				}
			}
		}
		return defaultColor;
	}

	@Override
	public List<NamedObject> getOptions(final ScheduleChartViewer<?> viewer) {
		Object input = viewer.getInput();
		if (input instanceof final NinetyDayScheduleInput ndsi) {
			input = ndsi.other();
		}
		if (input instanceof final Collection<?> collection) {
			if (collection.size() == 1) {
				Object possibleSchedule = collection.iterator().next();

				if (possibleSchedule instanceof final ScenarioResult r) {
					possibleSchedule = r.getResultRoot();
				}
				if (possibleSchedule instanceof final ScheduleModel r) {
					possibleSchedule = r.getSchedule();
				}
				if (possibleSchedule instanceof final @NonNull Schedule schedule) {
					return getOptions(schedule);
				}
			}
		} else if (input instanceof final @NonNull Schedule schedule) {
			return getOptions(schedule);
		}
		return Collections.emptyList();
	}

	private List<NamedObject> getOptions(final @NonNull Schedule schedule) {
		return schedule.getSequences()
				.stream()
				.map(Sequence::getEvents)
				.flatMap(List::stream) //
				.filter(SlotVisit.class::isInstance) //
				.map(SlotVisit.class::cast) //
				.filter(sv -> sv.getSlotAllocation().getSlot() instanceof final LoadSlot ls && !ls.isDESPurchase()) //
				.map(SlotVisit::getPort) //
				.distinct() //
				.sorted((port1, port2) -> ScenarioElementNameHelper.getName(port1, "<Unknown>").compareTo(ScenarioElementNameHelper.getName(port2, "<Unknown>"))) //
				.map(NamedObject.class::cast) //
				.toList();
	}

	@Override
	public void selectOption(final NamedObject object) {
		selectedObject = object instanceof final Port port ? port : null;
	}

	@Override
	public @Nullable NamedObject getOption() {
		return selectedObject;
	}
}
