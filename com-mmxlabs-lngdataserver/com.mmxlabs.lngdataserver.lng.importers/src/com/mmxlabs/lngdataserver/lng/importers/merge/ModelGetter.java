/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface ModelGetter {
	Object getModel(IScenarioDataProvider dp);
}
