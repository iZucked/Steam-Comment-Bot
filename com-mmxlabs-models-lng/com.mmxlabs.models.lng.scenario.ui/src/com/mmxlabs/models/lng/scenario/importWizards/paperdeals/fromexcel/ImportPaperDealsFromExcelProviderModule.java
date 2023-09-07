/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.cargo.util.IPaperDealExporter;

public class ImportPaperDealsFromExcelProviderModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(iterable(IPaperDealExporter.class)).toProvider(service(IPaperDealExporter.class).multiple());
	}
}
