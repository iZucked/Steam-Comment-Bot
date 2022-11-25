/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface ModelGetter {
	Object getModel(@NonNull IScenarioDataProvider sdp);
}
