/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.columnfilter.extmodule;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */


import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesRowTransformerFactory;

@ExtensionBean("com.mmxlabs.models.lng.cargo.editor.bulk.TradesBasedMenuFilter")
public interface ITradeBasedBulkColumnFilterExtension {

	@MapName("id")
	String getID();

	@MapName("handler")
	ITradesBasedFilterHandler getFactory();

}
