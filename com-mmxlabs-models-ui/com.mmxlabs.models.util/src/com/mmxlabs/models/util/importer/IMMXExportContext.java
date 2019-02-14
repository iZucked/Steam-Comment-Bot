/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import com.mmxlabs.common.csv.IExportContext;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface IMMXExportContext extends IExportContext {

	MMXRootObject getRootObject();

	IScenarioDataProvider getScenarioDataProvider();
}
