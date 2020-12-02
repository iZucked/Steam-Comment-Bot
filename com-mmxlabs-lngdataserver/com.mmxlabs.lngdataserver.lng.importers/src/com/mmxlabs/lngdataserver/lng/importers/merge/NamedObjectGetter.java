package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.List;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.NamedObject;

public interface NamedObjectGetter {
	List<? extends NamedObject> getNamedObjects(final LNGScenarioModel sm);
}
