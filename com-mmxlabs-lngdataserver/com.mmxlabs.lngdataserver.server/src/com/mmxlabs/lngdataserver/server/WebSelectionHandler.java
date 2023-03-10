/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.rcp.common.RunnerHelper;

public class WebSelectionHandler {

	private final Map<String, Collection<Object>> hookUpCache = new HashMap<>();

	public void handleSelection(ServletRequest req) {
		final String parameter = req.getParameter("selection");
		System.out.println("Selected: " + parameter);

		final ESelectionService service = PlatformUI.getWorkbench().getService(ESelectionService.class);
		if (service != null) {
			final Collection<Object> collection = hookUpCache.get(parameter);
			if (collection != null) {

				final Set<Object> selected = new HashSet<>();
				selected.addAll(collection);
				for (final Object o : collection) {
					if (o instanceof VesselEventVisit vesselEventVisit) {
						selected.add(vesselEventVisit.getVesselEvent());
					}
					if (o instanceof SlotVisit slotVisit) {

						final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
						if (slotAllocation != null) {
							selected.add(slotAllocation);
							final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
							selected.add(slotAllocation.getSlot());
							if (cargoAllocation != null) {
								selected.add(cargoAllocation);
							}
						}
					}
				}
				selected.remove(null);
				System.out.println("Found objects " + selected.size());
				RunnerHelper.asyncExec(() -> {
					service.setSelection(new ArrayList<>(selected));
					service.setPostSelection(new ArrayList<>(selected));
				});
			}
		}
	}

	public void dispose() {
		hookUpCache.clear();

	}
}
