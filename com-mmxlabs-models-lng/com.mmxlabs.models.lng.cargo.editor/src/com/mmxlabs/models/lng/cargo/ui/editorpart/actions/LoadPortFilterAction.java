/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesRowEMFPath;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.NamedObject;

public class LoadPortFilterAction extends EMFPathFilterAction {

	public LoadPortFilterAction(StructuredViewerFilterManager filterManager, CargoModel sourceObject) {
		super(filterManager, "Load Port", sourceObject, PortPackage.Literals.PORT_MODEL__PORTS, new TradesRowEMFPath(false, TradesRowEMFPath.Type.LOAD, CargoPackage.Literals.SLOT__PORT));
	}

	@Override
	protected List<NamedObject> getValues() {
		final CargoModel cargoModel = (CargoModel) getSourceObject();
		final Set<Port> seenPorts = new HashSet<>();
		final List<NamedObject> usedLoadPorts = new LinkedList<>();
		for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {
			final Port port = loadSlot.getPort();
			if (port != null && seenPorts.add(port)) {
				usedLoadPorts.add(port);
			}
		}
		Collections.sort(usedLoadPorts, (a, b) -> a.getName().compareTo(b.getName()));
		return usedLoadPorts;
	}
}
