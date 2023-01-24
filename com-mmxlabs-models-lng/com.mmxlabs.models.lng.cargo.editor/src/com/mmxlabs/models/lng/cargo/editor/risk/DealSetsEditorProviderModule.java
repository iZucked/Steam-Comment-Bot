/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;

public class DealSetsEditorProviderModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(iterable(IExposuresCustomiser.class)).toProvider(service(IExposuresCustomiser.class).multiple());
	}
}
