package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class NinetyDayScheduleEventStylingProvider implements IScheduleEventStylingProvider {

	@Override
	public Color getBackgroundColour(ScheduleEvent e) {
		return getColourFor(e, ColourElements.Background);
	}

	@Override
	public Color getBorderColour(ScheduleEvent e) {
		return getColourFor(e, ColourElements.Border);
	}

	@Override
	public int getBorderThickness(ScheduleEvent e) {
		return 1;
	}

	@Override
	public boolean getIsBorderInner(ScheduleEvent e) {
		return false;
	}
	
	private Color getColourFor(ScheduleEvent e, ColourElements ce) {
		if (!(e.getData() instanceof Event)) {
			return null;
		}
		
		ColourPaletteItems item = getItemForEvent((Event) e.getData());
		return item == null ? null : ColourPalette.getInstance().getColourFor(item, ce);
	}

	private @Nullable ColourPaletteItems getItemForEvent(Event e) {
		ColourPaletteItems item = null;
		
		if (e instanceof final Journey j) {
			item = j.isLaden() ? ColourPaletteItems.Voyage_Laden_Journey : ColourPaletteItems.Voyage_Ballast_Journey;
		} else if (e instanceof Idle i) {
			item = i.isLaden() ? ColourPaletteItems.Voyage_Laden_Idle : ColourPaletteItems.Voyage_Ballast_Idle;
		} else if (e instanceof SlotVisit sv) {
			item = sv.getSlotAllocation().getSlot() instanceof LoadSlot ? ColourPaletteItems.Voyage_Load : ColourPaletteItems.Voyage_Discharge;
		}
		
		return item;
	}

}
