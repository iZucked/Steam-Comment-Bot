/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.rowtransformer.extmodule;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */


import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesRowTransformerFactory;

@ExtensionBean("com.mmxlabs.models.lng.cargo.editor.bulk.TradesBasedRowTransformerFactory")
public interface ITradeBasedBulkRowTransformerFactoryExtension {

	@MapName("handlerID")
	String getHandlerID();

	@MapName("factory")
	ITradesRowTransformerFactory getFactory();

}
