/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.List;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface ModelGetter {
	Object getModel(IScenarioDataProvider dp);
}
