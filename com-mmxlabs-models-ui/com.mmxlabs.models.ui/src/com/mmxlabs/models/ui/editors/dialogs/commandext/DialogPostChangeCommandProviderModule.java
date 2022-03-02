/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs.commandext;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.ui.editors.dialogs.IDialogPostChangeCommandProvider;

public class DialogPostChangeCommandProviderModule extends AbstractModule {

	private final BundleContext context;

	public DialogPostChangeCommandProviderModule(BundleContext context) {
		this.context = context;
	}

	@Override
	protected void configure() {

		// Extension points
		bind(iterable(IDialogPostChangeCommandProviderExtension.class)).toProvider(service(IDialogPostChangeCommandProviderExtension.class).multiple());
		// Services
		bind(iterable(IDialogPostChangeCommandProvider.class)).toProvider(service(IDialogPostChangeCommandProvider.class).multiple());
	}

}
