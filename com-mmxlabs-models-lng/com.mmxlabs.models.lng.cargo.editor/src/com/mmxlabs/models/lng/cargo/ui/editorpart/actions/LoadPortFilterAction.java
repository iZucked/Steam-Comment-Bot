package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesRowEMFPath;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.NamedObject;

public class LoadPortFilterAction extends EMFPathFilterAction {

	public LoadPortFilterAction(StructuredViewerFilterManager filterManager, CargoModel sourceObject) {
		super(filterManager, "Load Port", sourceObject, PortPackage.Literals.PORT_MODEL__PORTS, new TradesRowEMFPath(false, TradesRowEMFPath.Type.LOAD, CargoPackage.Literals.SLOT__PORT));
	}

	@Override
	protected List<NamedObject> getValues() {
		CargoModel cargoModel = (CargoModel) getSourceObject();
		List<NamedObject> actualLoads = new LinkedList<>();
		for (Slot s : cargoModel.getLoadSlots()) {
			if (!actualLoads.contains(s.getPort())) {
				actualLoads.add(s.getPort());
			}
		}
		Collections.sort(actualLoads, (a, b) -> a.getName().compareTo(b.getName()));
		return actualLoads;
	}
}
