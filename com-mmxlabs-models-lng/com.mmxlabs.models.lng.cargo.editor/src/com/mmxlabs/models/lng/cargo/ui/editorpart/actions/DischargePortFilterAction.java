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
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesRowEMFPath;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.NamedObject;

public class DischargePortFilterAction extends EMFPathFilterAction {

	public DischargePortFilterAction(StructuredViewerFilterManager filterManager, CargoModel sourceObject) {
		super(filterManager, "Discharge Port", sourceObject, PortPackage.Literals.PORT_MODEL__PORTS, new TradesRowEMFPath(false, TradesRowEMFPath.Type.DISCHARGE, CargoPackage.Literals.SLOT__PORT));
	}

	@Override
	protected List<NamedObject> getValues() {
		final CargoModel cargoModel = (CargoModel) getSourceObject();
		final Set<Port> seenPorts = new HashSet<>();
		final List<NamedObject> usedDischargePorts = new LinkedList<>();
		for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {
			final Port port = dischargeSlot.getPort();
			if (port != null && seenPorts.add(port)) {
				usedDischargePorts.add(port);
			}
		}
		
		Collections.sort(usedDischargePorts, (a, b) -> a.getName().compareTo(b.getName()));
		return usedDischargePorts;
	}
}
