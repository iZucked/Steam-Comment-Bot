/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.ICommodityCurveImporter;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.IPaperDealExporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioInstanceEvaluator;

public class ImportPaperDealsFromExcelProviderModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(iterable(IPaperDealExporter.class)).toProvider(service(IPaperDealExporter.class).multiple());
		bind(iterable(ICommodityCurveImporter.class)).toProvider(service(ICommodityCurveImporter.class).multiple());
		bind(IScenarioInstanceEvaluator.class).toProvider(service(IScenarioInstanceEvaluator.class).single());
	}
}
