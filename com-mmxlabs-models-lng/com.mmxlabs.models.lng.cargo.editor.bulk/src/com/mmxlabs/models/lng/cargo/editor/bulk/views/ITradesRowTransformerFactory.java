/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface ITradesRowTransformerFactory {

	void transformRowData(Table table, LNGScenarioModel lngScenarioModel, EClass customRowEClass, Cargo cargo, Row row);
	
}
