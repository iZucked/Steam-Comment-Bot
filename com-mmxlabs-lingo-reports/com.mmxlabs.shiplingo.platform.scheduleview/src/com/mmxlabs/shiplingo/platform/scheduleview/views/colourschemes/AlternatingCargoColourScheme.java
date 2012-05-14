package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

public class AlternatingCargoColourScheme implements IScheduleViewColourScheme {
	private final Color baseColor;
	private final Color secondaryColor;

	private Color selectedColor;
	
	public AlternatingCargoColourScheme(final Color baseColor, final Color second) {
		this.baseColor = baseColor;
		this.secondaryColor = second;
		selectedColor = baseColor;
	}
	
	@Override
	public String getName() {
		return "Alternating Cargos";
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		if (element instanceof SlotVisit && ((SlotVisit)element).getSlotAllocation().getSlot() instanceof LoadSlot) {
			// flip colour
			if (selectedColor == baseColor) {
				selectedColor = secondaryColor;
			} else {
				selectedColor = baseColor;
			}
		}
		return selectedColor;
	}
}
