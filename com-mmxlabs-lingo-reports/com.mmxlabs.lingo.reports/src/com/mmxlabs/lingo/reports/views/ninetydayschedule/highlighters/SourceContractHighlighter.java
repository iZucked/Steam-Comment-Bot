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
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.AbstractScheduleEventStylingProvider;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;

public class SourceContractHighlighter extends AbstractScheduleEventStylingProvider implements ParameterisedColourScheme {

	private PurchaseContract selectedObject = null;

	public SourceContractHighlighter() {
		super("By source contract");
	}

	@Override
	public @Nullable Color getBackgroundColour(final DrawableScheduleEvent event, @Nullable final Color defaultColor) {

		final Object element = event.getScheduleEvent().getData();
		if (selectedObject != null) {
			if (element instanceof final SlotVisit slotVisit) {
				final Slot<?> slot = slotVisit.getSlotAllocation().getSlot();
				if (slot instanceof LoadSlot && slot.getContract() == selectedObject) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
				}
			} else if (element instanceof final NonShippedSlotVisit slotVisit) {
				final Slot<?> slot = slotVisit.getSlot();
				if (slot instanceof LoadSlot && slot.getContract() == selectedObject) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
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
		final LNGReferenceModel lngReferenceModel = ScenarioModelUtil.findReferenceModel(schedule);
		final CommercialModel commercialModel = lngReferenceModel.getCommercialModel();
		return commercialModel.getPurchaseContracts()
				.stream() //
				.filter(pc -> pc.getName() != null) //
				.sorted((pc1, pc2) -> pc1.getName().compareTo(pc2.getName())) //
				.map(NamedObject.class::cast)
				.toList();
	}

	@Override
	public void selectOption(final NamedObject object) {
		selectedObject = object instanceof final PurchaseContract pc ? pc : null;
	}

	@Override
	public @Nullable NamedObject getOption() {
		return selectedObject;
	}
}
