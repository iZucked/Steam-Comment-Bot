/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXResultRoot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelRecordScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public interface ScenarioResult {

	 
	 @NonNull IScenarioDataProvider getScenarioDataProvider() ;

	 ScenarioInstance getScenarioInstance() ;
 
	 <T extends MMXRootObject> @Nullable T getTypedRoot(final @NonNull Class<T> cls);

	 MMXRootObject getRootObject();

	 MMXResultRoot getResultRoot();

	 <T extends MMXResultRoot> @Nullable T getTypedResult(final Class<T> cls) ;

	 @NonNull ScenarioModelRecord getModelRecord() ;
 
}