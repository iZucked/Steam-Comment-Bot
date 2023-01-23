/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.rowmodel.extmodule;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */


import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesBasedRowModelTransformerFactory;

@ExtensionBean("com.mmxlabs.models.lng.cargo.editor.bulk.TradesBasedRowModelTransformerFactory")
public interface ITradeBasedBulkRowModelFactoryExtension {

	@MapName("handlerID")
	String getHandlerID();

	@MapName("factory")
	ITradesBasedRowModelTransformerFactory getFactory();

}
