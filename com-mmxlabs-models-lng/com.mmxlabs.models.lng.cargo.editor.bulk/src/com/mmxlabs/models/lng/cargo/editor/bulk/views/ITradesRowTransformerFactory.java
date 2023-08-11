/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface ITradesRowTransformerFactory {

	void transformRowData(TradesTableRoot table, LNGScenarioModel lngScenarioModel, EClass customRowEClass, Cargo cargo, TradesRow row);
	
}
