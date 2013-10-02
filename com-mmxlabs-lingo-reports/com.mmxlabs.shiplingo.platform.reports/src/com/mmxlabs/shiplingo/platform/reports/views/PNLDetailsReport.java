package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.properties.views.DetailPropertiesView;

public class PNLDetailsReport extends DetailPropertiesView {

	public PNLDetailsReport() {
		super("pnl", "com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport");
	}

	@Override
	protected Collection<?> adaptSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {

			final Iterator<Object> itr = ((IStructuredSelection) selection).iterator();
			final List<Object> adaptedObjects = new LinkedList<>();
			while (itr.hasNext()) {
				final Object a = itr.next();

				// map to events
				if (a instanceof CargoAllocation) {
					adaptedObjects.add(a);
				} else if (a instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) a;
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					if (slotAllocation.getCargoAllocation() != null) {
						adaptedObjects.add(slotAllocation.getCargoAllocation());
					} else if (slotAllocation.getMarketAllocation() != null) {
						adaptedObjects.add(slotAllocation.getMarketAllocation());
					}
				} else if (a instanceof VesselEventVisit) {
					adaptedObjects.add(a);
				} else if (a instanceof StartEvent) {
					adaptedObjects.add(a);
				} else if (a instanceof Cargo) {

				} else if (a instanceof Slot) {

				}
			}
			return adaptedObjects;
		}

		return Collections.emptySet();
	}
}
