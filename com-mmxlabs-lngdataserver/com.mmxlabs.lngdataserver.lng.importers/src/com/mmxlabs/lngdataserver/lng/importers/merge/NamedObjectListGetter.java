package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.NamedObject;

public interface NamedObjectListGetter extends EObjectListGetter {
	List<? extends NamedObject> getNamedObjects(final LNGScenarioModel sm);
	default List<? extends EObject> getEObjects(final LNGScenarioModel sm) {
		return getNamedObjects(sm);
	}
}
