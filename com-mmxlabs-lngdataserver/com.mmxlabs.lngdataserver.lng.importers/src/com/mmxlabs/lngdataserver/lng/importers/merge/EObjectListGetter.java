/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface EObjectListGetter {
	List<? extends EObject> getEObjects(final LNGScenarioModel sm);
}
