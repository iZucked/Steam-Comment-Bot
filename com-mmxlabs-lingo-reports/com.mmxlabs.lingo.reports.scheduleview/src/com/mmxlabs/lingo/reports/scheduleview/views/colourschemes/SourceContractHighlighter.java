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
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.NamedObject;

public class SourceContractHighlighter extends ParameterisedColourScheme {

	private PurchaseContract selectedObject = null;

	@Override
	public String getName() {
		return "By source contract";
	}

	@Override
	public Color getBackground(Object element) {
		if (selectedObject != null) {
			if (element instanceof SlotVisit slotVisit) {
				final Slot<?> slot  = slotVisit.getSlotAllocation().getSlot();
				if (slot instanceof LoadSlot && slot.getContract() == selectedObject) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
				}
			} else if (element instanceof NonShippedSlotVisit slotVisit) {
				final Slot<?> slot = slotVisit.getSlot();
				if (slot instanceof LoadSlot && slot.getContract() == selectedObject) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Source_Contract_Based_Highlight_Idle, ColourElements.Background);
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
		final LNGReferenceModel lngReferenceModel = ScenarioModelUtil.findReferenceModel(schedule);
		final CommercialModel commercialModel = lngReferenceModel.getCommercialModel();
		return commercialModel.getPurchaseContracts().stream() //
				.filter(pc -> pc.getName() != null) //
				.sorted((pc1, pc2) -> pc1.getName().compareTo(pc2.getName())) //
				.map(NamedObject.class::cast).toList();
	}

	@Override
	public void selectOption(Object object) {
		selectedObject = object instanceof PurchaseContract pc ? pc : null;
	}

}
