/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.views.portrotation.IPortRotationColumnFactory;

@ExtensionBean("com.mmxlabs.lingo.reports.PortRotationBasedColumn")
public interface IPortRotationBasedColumnExtension {

	@MapName("id")
	String getColumnID();

	@MapName("handlerID")
	String getHandlerID();

	@MapName("class")
	IPortRotationColumnFactory getFactory();

}
