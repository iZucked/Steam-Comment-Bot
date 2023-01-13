/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.columns.extmodule;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */


import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.cargo.editor.bulk.columnfilter.extmodule.ITradeBasedBulkColumnFilterExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowmodel.extmodule.ITradeBasedBulkRowModelExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowmodel.extmodule.ITradeBasedBulkRowModelFactoryExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowtransformer.extmodule.ITradeBasedBulkRowTransformerExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowtransformer.extmodule.ITradeBasedBulkRowTransformerFactoryExtension;

/**
 * 
 * 
 */
public class TradeBasedBulkModule extends AbstractModule {

	@Override
	protected void configure() {

		Bundle bundle = FrameworkUtil.getBundle(TradeBasedBulkModule.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		if (bundleContext != null) {
			install(Peaberry.osgiModule(bundleContext, EclipseRegistry.eclipseRegistry()));
		}
		// Extension points
		bind(iterable(ITradeBasedBulkColumnExtension.class)).toProvider(service(ITradeBasedBulkColumnExtension.class).multiple());
		bind(iterable(ITradeBasedBulkColumnFactoryExtension.class)).toProvider(service(ITradeBasedBulkColumnFactoryExtension.class).multiple());
		
		bind(iterable(ITradeBasedBulkRowModelFactoryExtension.class)).toProvider(service(ITradeBasedBulkRowModelFactoryExtension.class).multiple());
		bind(iterable(ITradeBasedBulkRowModelExtension.class)).toProvider(service(ITradeBasedBulkRowModelExtension.class).multiple());
		
		bind(iterable(ITradeBasedBulkRowTransformerFactoryExtension.class)).toProvider(service(ITradeBasedBulkRowTransformerFactoryExtension.class).multiple());
		bind(iterable(ITradeBasedBulkRowTransformerExtension.class)).toProvider(service(ITradeBasedBulkRowTransformerExtension.class).multiple());
		
		bind(iterable(ITradeBasedBulkColumnFilterExtension.class)).toProvider(service(ITradeBasedBulkColumnFilterExtension.class).multiple());
	}
}
